package eu.gricom.basic.statements;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.memoryManager.FileOpenType;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

/**
 * FGetStatement.java
 * <p>
 * Description: The FGetStatement class implements the BASIC FGET command. It reads the next character
 * from an opened file, starting from the current read cursor position stored in FileManager. The operation
 * involves: closing the file, re-opening it, skipping to the stored position by reading characters line by line,
 * reading one character, and then updating the read cursor position. If the end of file is reached, returns "EOF".
 * <p>
 * Usage:
 * - FGET fileID, variableName
 * <p>
 * Execution Flow:
 * 1. Get the current read cursor position from FileManager
 * 2. Get the file name from FileManager
 * 3. Close the file (keeping it on disk)
 * 4. Re-open the file for reading
 * 5. Read lines until accumulated character count reaches or exceeds the stored position
 * 6. Extract the character at the position within the appropriate line
 * 7. Assign the character to the specified variable as a StringValue
 * 8. Update the read cursor position to position + 1
 * 9. If EOF is reached, assign "EOF" to the variable
 * <p>
 * Error Handling:
 * - Throws RuntimeException if file ID is not registered
 * - Throws RuntimeException if file name is empty
 * - Throws RuntimeException if file cannot be closed or re-opened
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FGetStatement implements Statement {
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
    public FGetStatement(final int iTokenNumber, final int iFileId, final String strVariableName) {
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
     * execute reads the next character from the file at the current read cursor position. The file is closed
     * and re-opened, then repositioned to the stored cursor, and one character is read and returned as a StringValue.
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

            // Step 4: Read the character at the stored position with a temporary reader (Phase 7 fix). The file is
            // not closed or re-opened, so the line reader of FINPUT keeps its place.
            String strCharacter = oFileManager.peekCharacter(_iFileId, iStoredPosition);

            // Step 5: Move the read position one character on, or mark the end of the file (EOF) when nothing is left
            if ("EOF".equals(strCharacter)) {
                oFileManager.markEndOfFile(_iFileId);
            } else {
                oFileManager.putReadPos(_iFileId, iStoredPosition + 1);
            }

            // Step 7: Assign the character to the variable
            StringValue oCharValue = new StringValue(strCharacter);
            AssignStatement oAssignStatement = new AssignStatement(_iTokenNumber, _strVariableName, oCharValue);
            oAssignStatement.execute();

        } catch (Exception e) {
            Logger oLogger = new Logger("eu.gricom.basic.statements.FGetStatement");
            oLogger.error(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Content.
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return gives the name of the statement ("FGET") and the file ID
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String content() throws Exception {
        return "FGET";
    }

    /**
     * Structure.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("FGET") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"FGET\": {";
        strReturn += "\"TOKEN_NR\": \"" + _iTokenNumber + "\", ";
        strReturn += "\"FILE_ID\": \"" + _iFileId + "\", ";
        strReturn += "\"VARIABLE\": \"" + _strVariableName + "\"";
        strReturn += "}}";
        return strReturn;
    }
}
