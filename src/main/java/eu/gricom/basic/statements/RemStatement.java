package eu.gricom.basic.statements;

/**
 * RemStatement.java
 * <p>
 * Description:
 * <p>
 * The EndStatement class terminates the running Basic program.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class RemStatement implements Statement {
    private final int _iTokenNumber;
    private final String _strRemText;

    /**
     * Constructs a REM statement with the specified token number and comment text.
     *
     * @param iTokenNumber the token number (line number) of the REM statement in the Basic program
     * @param strRemText the text content of the REM statement
     */
    public RemStatement(final int iTokenNumber, final String strRemText) {
        _iTokenNumber = iTokenNumber;
        _strRemText = strRemText;
    }

    /**
     * Get Line Number.
     *
     * @return iLineNumber - the command line number of the statement
     */
    @Override
    public final int getTokenNumber() {
        return _iTokenNumber;
    }

    /**
     * Execute.
     *
     * Terminate the running program.
     */
    public final void execute() {
    }

    /**
     * Returns a string representation of the REM statement, including its text content.
     *
     * @return the REM statement in the format "REM [<rem text>]"
     */
    @Override
    public final String content() {

        return "REM [" + _strRemText + "]";
    }

    /**
     * Returns a JSON-like string describing the structure of this REM statement, including its token number and text.
     *
     * @return a string representation of the REM statement's structure with token number and REM text.
     * @throws Exception if an error occurs during structure generation.
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"REM\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\",";
        strReturn += "\"REM_TEXT\": \""+ _strRemText +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
