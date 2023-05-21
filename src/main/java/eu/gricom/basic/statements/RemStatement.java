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
     * Default constructor.
     *
     * An "REM" statement performs the hard termination of the interpreter.
     * @param iTokenNumber - number of the command in the basic program
     * @param strRemText - text of the rem statement
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
     * Content.
     *
     * Method for JUnit to return the content of the statement.
     *
     * @return - gives the name of the statement ("REM")
     */
    @Override
    public final String content() {

        return "REM [" + _strRemText + "]";
    }

    /**
     * Structure.
     *
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("INPUT") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = this.toString();
        return strReturn;
    }
}
