package eu.gricom.basic.statements;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.variableTypes.Value;

public class FInputStatement implements Statement {
    private final String _strName;
    private final int _iTokenNumber;
    private final int _iFileId;

    /**
     * Default constructor.
     * <p>
     * An "input" statement reads input from the user and stores it in a variable.
     *
     * @param strName - the name of the variable to be read.
     */
    public FInputStatement(final int iFileId, final String strName) {
        _iTokenNumber = 0;
        _iFileId = iFileId;
        _strName = strName;
    }

    /**
     * Default constructor.
     * <p>
     * An "input" statement reads input from the user and stores it in a variable.
     *
     * @param iTokenNumber the line number of this command
     * @param strName the name of the variable to be read.
     */
    public FInputStatement(final int iTokenNumber, final int iFileId, final String strName) {
        _iTokenNumber = iTokenNumber;
        _iFileId = iFileId;
        _strName = strName;
    }

    /**
     * Get Line Number.
     *
     * @return the command line number of the statement
     */
    @Override
    public final int getTokenNumber() {
        return _iTokenNumber;
    }

    /**
     * Execute.
     * <p>
     * Execute the input statement.
     *
     * @throws eu.gricom.basic.error.RuntimeException if an incorrect input is detected
     */
    public final void execute() throws Exception {
        AssignStatement oAssignStatement;
        Value oStringValue = null;

        try {
            FileManager oFileManager = new FileManager();
            oStringValue = oFileManager.read(_iFileId);
        } catch (Exception e) {
            Logger oLogger = new Logger("eu.gricom.basic.statements.FInputStatement");
            oLogger.error(e.getMessage());
            System.exit(-1);
        }
        if (oStringValue != null) {
            oAssignStatement = new AssignStatement(_iTokenNumber, _strName, oStringValue);
            oAssignStatement.execute();
        } else {
            throw new RuntimeException("EOF");
        }
    }

    /**
     * Content.
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return - gives the name of the statement ("INPUT") and the variable name
     */
    @Override
    public final String content() {

        return "FINPUT (" + _iFileId + " " +_strName + ")";
    }

    /**
     * Structure.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("INPUT") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"FINPUT\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\",";
        strReturn += "\"FILE_ID\": \""+ _iFileId +"\",";
        strReturn += "\"VARIABLE\": \""+ _strName +"\"";
        strReturn += "}}";
        return strReturn;
    }

}
