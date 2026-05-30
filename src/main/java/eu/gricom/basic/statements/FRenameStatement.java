package eu.gricom.basic.statements;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.memoryManager.FileOpenType;
import eu.gricom.basic.variableTypes.StringValue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * FRenameStatement.java
 * <p>
 * Description: The FRenameStatement class implements the BASIC FRENAME command. It renames a file
 * that is registered in the FileManager. The operation involves: closing the file (keeping it on disk),
 * renaming the file in the file system, and then re-registering the file with the same file ID but
 * with the new file name in FileManager. Any subsequent access to the file using the original file ID
 * will reference the renamed file.
 * <p>
 * Usage:
 * - FRENAME fileID, newFileName
 * <p>
 * Execution Flow:
 * 1. Verify file ID is registered in FileManager
 * 2. Get the current file name from FileManager
 * 3. Close the file (keeping it on disk, not deleting)
 * 4. Rename the file in the file system
 * 5. Re-register the file with the same file ID and new name
 * 6. Return to caller
 * <p>
 * Error Handling:
 * - Throws RuntimeException if file ID is not registered
 * - Throws RuntimeException if file name is empty
 * - Throws RuntimeException if file cannot be closed
 * - Throws RuntimeException if file cannot be renamed
 * - Throws RuntimeException if file cannot be re-registered
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FRenameStatement implements Statement {
    private final int _iTokenNumber;
    private final int _iFileId;
    private final StringValue _oNewFileName;

    /**
     * Default constructor.
     * <p>
     * @param iTokenNumber the line number of the command in the BASIC program
     * @param iFileId the file ID of the file to rename
     * @param oNewFileName the new name for the file as a StringValue
     */
    public FRenameStatement(int iTokenNumber, int iFileId, StringValue oNewFileName) {
        _iTokenNumber = iTokenNumber;
        _iFileId = iFileId;
        _oNewFileName = oNewFileName;
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
     * execute renames the file identified by the file ID. The file is closed (without deletion),
     * renamed in the file system, and then re-registered with the same file ID and new name.
     *
     * @throws Exception as any execution error found during execution
     */
    @Override
    public void execute() throws Exception {
        FileManager oFileManager = new FileManager();

        // Step 1: Verify file ID is registered
        if (!oFileManager.getFileStatus(_iFileId)) {
            throw new RuntimeException("File with ID " + _iFileId + " is not registered in FileManager");
        }

        // Step 2: Get the current file name
        String strCurrentFileName = oFileManager.getFileName(_iFileId).toString();

        // Step 3: Validate current file name is not empty
        if (strCurrentFileName.isEmpty()) {
            throw new RuntimeException("Current file name is empty for file ID " + _iFileId);
        }

        // Step 4: Validate new file name is not empty
        if (_oNewFileName == null) {
            throw new RuntimeException("New file name cannot be null");
        }
        String strNewFileName = _oNewFileName.toString();
        if (strNewFileName.isEmpty()) {
            throw new RuntimeException("New file name cannot be empty");
        }

        try {
            // Step 5: Close the file (keeping it on disk)
            try {
                oFileManager.closeFile(_iFileId, false);
            } catch (Exception e) {
                throw new RuntimeException("Failed to close file before renaming: " + e.getMessage());
            }

            // Step 6: Rename the file in the file system
            try {
                Path oCurrentPath = Paths.get(strCurrentFileName);
                Path oNewPath = Paths.get(strNewFileName);

                // Verify current file exists before renaming
                if (!Files.exists(oCurrentPath)) {
                    throw new RuntimeException("File does not exist: " + strCurrentFileName);
                }

                // Perform the rename
                Files.move(oCurrentPath, oNewPath);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException("Failed to rename file from " + strCurrentFileName + " to " + strNewFileName + ": " + e.getMessage());
            }

            // Step 7: Re-register the file with the same file ID and new name
            try {
                // Determine file open type based on the original state
                // Since we just closed the file, we need to re-open it with appropriate permissions
                // Try to open as READ first (most common for renamed files)
                boolean bOpened = oFileManager.openFile(strNewFileName, _iFileId, FileOpenType.READ);
                if (!bOpened) {
                    throw new RuntimeException("Failed to re-register renamed file in FileManager");
                }
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException("Failed to re-register renamed file: " + e.getMessage());
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during file rename: " + e.getMessage());
        }
    }

    /**
     * Content.
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return gives the name of the statement ("FRENAME") and the file ID
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String content() throws Exception {
        return "FRENAME";
    }

    /**
     * Structure.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("FRENAME") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strNewFileName = _oNewFileName.toString();
        String strReturn = "{\"FRENAME\": {";
        strReturn += "\"TOKEN_NR\": \"" + _iTokenNumber + "\", ";
        strReturn += "\"FILE_ID\": \"" + _iFileId + "\", ";
        strReturn += "\"NEW_FILE_NAME\": \"" + strNewFileName.replace("\"", "\\\"") + "\"";
        strReturn += "}}";
        return strReturn;
    }
}
