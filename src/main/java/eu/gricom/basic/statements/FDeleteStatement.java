package eu.gricom.basic.statements;

import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.memoryManager.FileManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * FDeleteStatement.java
 * <p>
 * Description: The FDeleteStatement class implements the BASIC FDELETE command. It deletes a file
 * identified by its file ID from the file system. The file ID is used to retrieve the file name
 * from the FileManager. If the file does not exist or cannot be deleted, a warning message is logged
 * but no exception is thrown, allowing the program to continue execution.
 * <p>
 * Usage:
 * - FDELETE fileID
 * <p>
 * Error Handling:
 * - If the file does not exist, a warning is logged (no exception thrown)
 * - If the file cannot be deleted, a warning is logged (no exception thrown)
 * - Unregistered file IDs are handled gracefully with a warning
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FDeleteStatement implements Statement {
    private final int _iTokenNumber;
    private final int _iFileId;
    private final Logger _oLogger = new Logger(this.getClass().getName());

    /**
     * Default constructor.
     * <p>
     * @param iTokenNumber - number of the command in the basic program
     * @param iFileId - the file ID identifying the file to delete
     */
    public FDeleteStatement(int iTokenNumber, int iFileId) {
        _iTokenNumber = iTokenNumber;
        _iFileId = iFileId;
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
     * deleteFile deletes the file identified by the file ID from the file system.
     * If the file does not exist or cannot be deleted, a warning is logged.
     * The file does not need to be registered in FileManager as an open file.
     *
     * @throws Exception as any execution error found during execution
     */
    @Override
    public void execute() throws Exception {
        FileManager oFileManager = new FileManager();

        // Step 1: Check if the file ID is registered in FileManager
        if (!oFileManager.getFileStatus(_iFileId)) {
            _oLogger.warning("File ID " + _iFileId + " is not registered in FileManager");
            return;
        }

        // Step 2: Get the file name from FileManager
        String strFileName = oFileManager.getFileName(_iFileId).toString();

        // Step 3: Check if the file name is empty
        if (strFileName.isEmpty()) {
            _oLogger.warning("File name is empty for file ID " + _iFileId);
            return;
        }

        // Step 4: Delete the file
        try {
            Path oPath = Paths.get(strFileName);
            boolean bDeleted = Files.deleteIfExists(oPath);

            if (!bDeleted) {
                _oLogger.warning("File does not exist: " + strFileName);
            }
        } catch (Exception e) {
            _oLogger.warning("Failed to delete file: " + strFileName + ": " + e.getMessage());
        }
    }

    /**
     * Content.
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return gives the name of the statement ("FDELETE") and the file ID
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String content() throws Exception {
        return "FDELETE";
    }

    /**
     * Structure.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("FDELETE") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"FDELETE\": {";
        strReturn += "\"TOKEN_NR\": \"" + _iTokenNumber + "\", ";
        strReturn += "\"FILE_ID\": \"" + _iFileId + "\"";
        strReturn += "}}";
        return strReturn;
    }
}
