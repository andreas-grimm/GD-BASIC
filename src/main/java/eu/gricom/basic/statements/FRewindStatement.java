package eu.gricom.basic.statements;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.memoryManager.FileOpenType;

/**
 * FRewindStatement.java
 * <p>
 * Description: The FRewindStatement class implements the BASIC FREWIND command. It resets the read cursor
 * position of an opened file to the beginning (position 0). This allows re-reading the file from the start
 * without closing and re-opening it.
 * <p>
 * Usage:
 * - FREWIND fileID
 * <p>
 * Execution Flow:
 * 1. Verify file ID is registered in FileManager
 * 2. Set the read cursor position to 0 in FileManager
 * <p>
 * Error Handling:
 * - Throws RuntimeException if file ID is not registered
 * - Throws RuntimeException if position cannot be set
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FRewindStatement implements Statement {
    private final int _iTokenNumber;
    private final int _iFileId;

    /**
     * Default constructor.
     * <p>
     * @param iTokenNumber the line number of the command in the BASIC program
     * @param iFileId the file ID of the file to rewind
     */
    public FRewindStatement(final int iTokenNumber, final int iFileId) {
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
     * execute resets the read cursor position of the file to 0 (beginning of file).
     * This is done by closing the file, re-opening it for reading, and setting position to 0.
     *
     * @throws Exception as any execution error found during execution
     */
    @Override
    public void execute() throws Exception {
        FileManager oFileManager = new FileManager();

        try {
            // Step 1: Verify file ID is registered
            if (!oFileManager.getFileStatus(_iFileId)) {
                throw new RuntimeException("File with ID " + _iFileId + " is not registered in FileManager");
            }

            // Step 2: Get the file name
            String strFileName = oFileManager.getFileName(_iFileId).toString();
            if (strFileName.isEmpty()) {
                throw new RuntimeException("File name is empty for file ID " + _iFileId);
            }

            // Step 3: Close the file
            try {
                oFileManager.closeFile(_iFileId, false);
            } catch (Exception e) {
                throw new RuntimeException("Failed to close file: " + e.getMessage());
            }

            // Step 4: Re-open the file for reading (resets file pointer to beginning)
            try {
                boolean bOpened = oFileManager.openFile(strFileName, _iFileId, FileOpenType.READ);
                if (!bOpened) {
                    throw new RuntimeException("Failed to re-open file for rewind: " + strFileName);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to re-open file: " + e.getMessage());
            }

            // Step 5: Verify position is 0 (should be set by openFile)
            try {
                oFileManager.putReadPos(_iFileId, 0);
            } catch (Exception e) {
                throw new RuntimeException("Failed to reset read position: " + e.getMessage());
            }

        } catch (Exception e) {
            Logger oLogger = new Logger("eu.gricom.basic.statements.FRewindStatement");
            oLogger.error(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Content.
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return gives the name of the statement ("FREWIND") and the file ID
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String content() throws Exception {
        return "FREWIND";
    }

    /**
     * Structure.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("FREWIND") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"FREWIND\": {";
        strReturn += "\"TOKEN_NR\": \"" + _iTokenNumber + "\", ";
        strReturn += "\"FILE_ID\": \"" + _iFileId + "\"";
        strReturn += "}}";
        return strReturn;
    }
}
