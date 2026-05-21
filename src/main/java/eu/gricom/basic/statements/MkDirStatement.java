package eu.gricom.basic.statements;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.variableTypes.StringValue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * MkDirStatement.java
 * <p>
 * Description: The MkDirStatement class implements the BASIC MKDIR command. It creates a directory
 * at the specified path. The directory path is provided as a StringValue parameter.
 * <p>
 * Usage:
 * - MKDIR directoryPath
 * <p>
 * Execution Flow:
 * 1. Verify directory path is not empty
 * 2. Create the directory using Files.createDirectory() or Files.createDirectories()
 * 3. If directory creation fails, throw RuntimeException
 * <p>
 * Error Handling:
 * - Throws RuntimeException if directory path is empty
 * - Throws RuntimeException if directory path is null
 * - Throws RuntimeException if directory creation fails (permission denied, path exists, etc.)
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class MkDirStatement implements Statement {
    private final int _iTokenNumber;
    private final StringValue _oDirectory;

    /**
     * Default constructor.
     * <p>
     * @param iTokenNumber the line number of the command in the BASIC program
     * @param oDirectory the directory path as a StringValue
     */
    public MkDirStatement(final int iTokenNumber, final StringValue oDirectory) {
        _iTokenNumber = iTokenNumber;
        _oDirectory = oDirectory;
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
     * execute creates a directory at the specified path.
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

            // Step 4: Create the directory
            try {
                Path oPath = Paths.get(strDirectory);
                Files.createDirectory(oPath);
            } catch (java.nio.file.FileAlreadyExistsException e) {
                throw new RuntimeException("Directory already exists: " + strDirectory);
            } catch (java.nio.file.NoSuchFileException e) {
                throw new RuntimeException("Parent directory does not exist: " + strDirectory);
            } catch (java.nio.file.AccessDeniedException e) {
                throw new RuntimeException("Access denied creating directory: " + strDirectory);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create directory: " + strDirectory + ": " + e.getMessage());
            }

        } catch (Exception e) {
            Logger oLogger = new Logger("eu.gricom.basic.statements.MkDirStatement");
            oLogger.error(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Content.
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return gives the name of the statement ("MKDIR") and the directory path
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String content() throws Exception {
        return "MKDIR";
    }

    /**
     * Structure.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("MKDIR") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strDirectory = _oDirectory.toString();
        String strReturn = "{\"MKDIR\": {";
        strReturn += "\"TOKEN_NR\": \"" + _iTokenNumber + "\", ";
        strReturn += "\"DIRECTORY\": \"" + strDirectory.replace("\"", "\\\"") + "\"";
        strReturn += "}}";
        return strReturn;
    }
}
