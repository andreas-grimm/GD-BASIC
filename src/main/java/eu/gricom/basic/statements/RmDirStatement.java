package eu.gricom.basic.statements;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.variabletypes.BooleanValue;
import eu.gricom.basic.variabletypes.StringValue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * RmDirStatement.java
 * <p>
 * Description: The RmDirStatement class implements the BASIC RMDIR command. It removes (deletes) a directory
 * at the specified path. The directory path is provided as a StringValue parameter, and a force flag as a BooleanValue
 * determines whether to recursively delete directory contents.
 * <p>
 * Usage:
 * - RMDIR directoryPath
 * - RMDIR directoryPath, forceFlag
 * <p>
 * Execution Flow:
 * 1. Verify directory path is not empty
 * 2. Check if directory exists (if not, return success)
 * 3. If force flag is true, recursively delete directory and all contents (rm -rf style)
 * 4. If force flag is false, delete only empty directory
 * 5. If directory deletion fails, throw RuntimeException
 * <p>
 * Error Handling:
 * - Throws RuntimeException if directory path is null
 * - Throws RuntimeException if directory path is empty
 * - Throws RuntimeException if directory is not empty and force flag is false
 * - Throws RuntimeException if access is denied
 * - No exception if directory does not exist (considered successful)
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class RmDirStatement implements Statement {
    private final int _iTokenNumber;
    private final StringValue _oDirectory;
    private final BooleanValue _bForce;

    /**
     * Default constructor.
     * <p>
     * @param iTokenNumber the line number of the command in the BASIC program
     * @param oDirectory the directory path as a StringValue
     * @param bForce whether to force recursive deletion (BooleanValue)
     */
    public RmDirStatement(final int iTokenNumber, final StringValue oDirectory, final BooleanValue oForce) {
        _iTokenNumber = iTokenNumber;
        _oDirectory = oDirectory;
        _bForce = oForce;
    }

    /**
     * Get Token Number - get the number of the corresponding token to this statement.
     *
     * @return the command line number of the statement
     */
    @Override
    public int getTokenNumber() {
        return _iTokenNumber;
    }

    /**
     * execute removes a directory at the specified path.
     * If force flag is true, recursively deletes all contents.
     * If directory does not exist, returns successfully.
     *
     * @throws Exception as any execution error found during execution
     */
    @Override
    public void execute() throws Exception {
        try {
            // Step 1: Verify directory path is not null
            if (_oDirectory == null) {
                throw new RuntimeException("Directory path cannot be null");
            }

            // Step 2: Get the directory path as a string
            String strDirectory = _oDirectory.toString();

            // Step 3: Verify directory path is not empty
            if (strDirectory.isEmpty()) {
                throw new RuntimeException("Directory path cannot be empty");
            }

            // Step 4: Convert to Path object
            Path oPath = Paths.get(strDirectory);

            // Step 5: If directory does not exist, return success
            if (!Files.exists(oPath)) {
                // Directory doesn't exist - consider this successful
                return;
            }

            // Step 6: Check if force flag is set
            boolean bForceDelete = _bForce.toBoolean();

            // Step 7: Delete directory based on force flag
            try {
                if (bForceDelete) {
                    // Recursive deletion (rm -rf style)
                    deleteDirectoryRecursively(oPath);
                } else {
                    // Simple directory deletion (fails if not empty)
                    try {
                        Files.delete(oPath);
                    } catch (java.nio.file.DirectoryNotEmptyException oException) {
                        throw new RuntimeException("Directory is not empty: " + strDirectory);
                    } catch (java.nio.file.AccessDeniedException oException) {
                        throw new RuntimeException("Access denied removing directory: " + strDirectory);
                    } catch (Exception oException) {
                        throw new RuntimeException("Failed to remove directory: " + strDirectory + ": " + oException.getMessage());
                    }
                }
            } catch (RuntimeException oException) {
                throw oException;
            } catch (Exception oException) {
                throw new RuntimeException("Failed to remove directory: " + strDirectory + ": " + oException.getMessage());
            }

        } catch (Exception oException) {
            Logger oLogger = new Logger("eu.gricom.basic.statements.RmDirStatement");
            oLogger.error(oException.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Helper method to recursively delete a directory and all its contents.
     * Similar to Unix rm -rf command.
     *
     * @param oPath the path to delete recursively
     * @throws Exception if deletion fails
     */
    private void deleteDirectoryRecursively(Path oPath) throws Exception {
        try {
            Files.walk(oPath)
                    .sorted((a, b) -> b.compareTo(a))  // Reverse order: delete files before dirs
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (java.io.IOException oException) {
                            throw new java.io.UncheckedIOException("Failed to delete: " + p.toString() + ": " + oException.getMessage(), oException);
                        }
                    });
        } catch (java.io.UncheckedIOException oException) {
            throw new RuntimeException(oException.getMessage());
        } catch (Exception oException) {
            throw new RuntimeException("Failed to recursively delete directory: " + oPath.toString() + ": " + oException.getMessage());
        }
    }

    /**
     * Content.
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return gives the name of the statement ("RMDIR") and the directory path
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String content() throws Exception {
        return "RMDIR";
    }

    /**
     * Structure.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("RMDIR") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strDirectory = _oDirectory.toString();
        String strForce = _bForce.toString();
        String strReturn = "{\"RMDIR\": {";
        strReturn += "\"TOKEN_NR\": \"" + _iTokenNumber + "\", ";
        strReturn += "\"DIRECTORY\": \"" + strDirectory.replace("\"", "\\\"") + "\", ";
        strReturn += "\"FORCE\": \"" + strForce + "\"";
        strReturn += "}}";
        return strReturn;
    }

    /** @return the Directory of this statement (used by the compiler) */
    public StringValue getDirectory() {
        return _oDirectory;
    }

    /** @return the Force of this statement (used by the compiler) */
    public BooleanValue getForce() {
        return _bForce;
    }
}
