package eu.gricom.basic.statements;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.memorymanager.FileManager;
import eu.gricom.basic.variabletypes.StringValue;

/**
 * FPeekStatement.java
 * <p>
 * Description: The FPeekStatement class implements the BASIC FPEEK command. It reads the next character
 * from an opened file without advancing the read cursor position. This is similar to FGetStatement but does not
 * update the position, allowing a "look ahead" at the next character without consuming it.
 * <p>
 * The operation involves: closing the file, re-opening it, skipping to the stored position by reading characters
 * line by line, reading one character, and then returning it WITHOUT updating the read cursor position.
 * If the end of file is reached, returns "EOF".
 * <p>
 * Usage:
 * - FPEEK fileID, variableName
 * <p>
 * Execution Flow:
 * 1. Get the current read cursor position from FileManager
 * 2. Get the file name from FileManager
 * 3. Close the file (keeping it on disk)
 * 4. Re-open the file for reading
 * 5. Read lines until accumulated character count reaches or exceeds the stored position
 * 6. Extract the character at the position within the appropriate line
 * 7. Assign the character to the specified variable as a StringValue
 * 8. DO NOT update the read cursor position (unlike FGetStatement)
 * 9. If EOF is reached, assign "EOF" to the variable
 * <p>
 * Difference from FGetStatement:
 * - FGetStatement advances position after reading: position = position + 1
 * - FPeekStatement leaves position unchanged: position remains the same
 * <p>
 * Error Handling:
 * - Throws RuntimeException if file ID is not registered
 * - Throws RuntimeException if file name is empty
 * - Throws RuntimeException if file cannot be closed or re-opened
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FPeekStatement implements Statement {
    private final int _iTokenNumber;
    private final int _iFileId;
    private final String _strVariableName;

    /**
     * Default constructor.
     * <p>
     * @param iTokenNumber the line number of the command in the BASIC program
     * @param iFileId the file ID of the file to read from
     * @param strVariableName the name of the variable to store the character
     */
    public FPeekStatement(final int iTokenNumber, final int iFileId, final String strVariableName) {
        _iTokenNumber = iTokenNumber;
        _iFileId = iFileId;
        _strVariableName = strVariableName;
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
     * execute reads the next character from the file at the current read cursor position without advancing the cursor.
     * The file is closed and re-opened, then repositioned to the stored cursor, and one character is read and
     * returned as a StringValue. The position is NOT updated (unlike FGetStatement).
     * If EOF is reached, "EOF" is returned.
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

            // Step 2: Get the stored read position
            int iStoredPosition = oFileManager.getReadPos(_iFileId).toInt();

            // Step 3: Get the file name
            String strFileName = oFileManager.getFileName(_iFileId).toString();
            if (strFileName.isEmpty()) {
                throw new RuntimeException("File name is empty for file ID " + _iFileId);
            }

            // Step 4: Read the character at the stored position with a temporary reader. The file is not closed or
            // re-opened, so the line reader of FINPUT keeps its place (Phase 7 fix).
            String strCharacter = oFileManager.peekCharacter(_iFileId, iStoredPosition);

            // Step 5: The position is not changed by FPEEK (it only looks ahead). At the end of the file, EOF is set.
            if ("EOF".equals(strCharacter)) {
                oFileManager.markEndOfFile(_iFileId);
            }

            // Step 8: Assign the character to the variable
            StringValue oCharValue = new StringValue(strCharacter);
            AssignStatement oAssignStatement = new AssignStatement(_iTokenNumber, _strVariableName, oCharValue);
            oAssignStatement.execute();

        } catch (Exception oException) {
            Logger oLogger = new Logger("eu.gricom.basic.statements.FPeekStatement");
            oLogger.error(oException.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Content.
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return gives the name of the statement ("FPEEK") and the file ID
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String content() throws Exception {
        return "FPEEK";
    }

    /**
     * Structure.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("FPEEK") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"FPEEK\": {";
        strReturn += "\"TOKEN_NR\": \"" + _iTokenNumber + "\", ";
        strReturn += "\"FILE_ID\": \"" + _iFileId + "\", ";
        strReturn += "\"VARIABLE\": \"" + _strVariableName + "\"";
        strReturn += "}}";
        return strReturn;
    }

    /** @return the FileId of this statement (used by the compiler) */
    public int getFileId() {
        return _iFileId;
    }

    /** @return the VariableName of this statement (used by the compiler) */
    public String getVariableName() {
        return _strVariableName;
    }
}
