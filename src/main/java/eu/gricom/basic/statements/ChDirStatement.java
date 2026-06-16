package eu.gricom.basic.statements;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

/**
 * ChDirStatement.java
 * <p>
 * Description: The ChDirStatement class implements the BASIC CHDIR command to change the current working directory.
 * It updates the file manager's current directory to the specified path, allowing subsequent file operations
 * to use the new directory as their base path.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class ChDirStatement implements Statement {
    private final int _iTokenNumber;
    private final Value _oValue;

    /**
     * Constructor for ChDirStatement.
     *
     * @param iTokenNumber the line number of the statement
     * @param oValue the directory path value (expects StringValue)
     */
    public ChDirStatement(final int iTokenNumber, final Value oValue) {
        _iTokenNumber = iTokenNumber;
        _oValue = oValue;
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
     * Changes the current working directory to the specified path.
     * Updates the FileManager's current directory setting, which is used as the base path
     * for subsequent file operations (OPEN, CLOSE, INPUT#, PRINT#). The directory path is
     * stored but not validated for existence at the time of the call.
     *
     * @throws Exception if input is not a StringValue or other execution errors
     */
    @Override
    public void execute() throws Exception {
        if (_oValue instanceof StringValue) {
            StringValue strValue = (StringValue) _oValue;
            FileManager oFileManager = new FileManager();
            oFileManager.setCurrentDirectory(strValue.toString());
            return;
        }

        throw new RuntimeException("Input value not of type String: " + _oValue);
    }

    /**
     * Content.
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return gives the name of the statement ("CHDIR") and the directory path
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String content() throws Exception {
        return "CHDIR";
    }

    /**
     * Structure.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("CHDIR") and the directory path parameter
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"CHDIR\": {";
        strReturn += "\"TOKEN_NR\": \"" + _iTokenNumber + "\"";
        strReturn += ",\"PATH\": \"" + _oValue.toString() + "\"";
        strReturn += "}}";
        return strReturn;
    }
}
