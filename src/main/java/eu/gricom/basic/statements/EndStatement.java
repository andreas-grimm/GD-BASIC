package eu.gricom.basic.statements;

/**
 * EndStatement.java
 * <p>
 * Description:
 * <p>
 * The EndStatement class terminates the running Basic program.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class EndStatement implements Statement {
    private final int _iTokenNumber;

    /**
     * Default constructor.
     *
     * An "END" statement performs the hard termination of the interpreter.
     */
    public EndStatement() {
        _iTokenNumber = 0;
    }

    /**
     * Default constructor.
     *
     * An "END" statement performs the hard termination of the interpreter.
     * @param iTokenNumber - number of the command in the basic program
     */
    public EndStatement(final int iTokenNumber) {
        _iTokenNumber = iTokenNumber;
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
        System.exit(0);
    }

    /**
     * Returns the name of this statement, which is "END".
     *
     * @return the string "END"
     */
    @Override
    public final String content() {

        return "END";
    }

    /**
     * Returns a JSON-formatted string describing the structure of the END statement, including its token number.
     *
     * @return a JSON string with the statement name "END" and its associated token number.
     * @throws Exception if an error occurs during structure generation.
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"END\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
