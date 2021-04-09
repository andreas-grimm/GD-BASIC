package eu.gricom.interpreter.basic.statements;

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
     * Content.
     *
     * Method for JUnit to return the content of the statement.
     *
     * @return - gives the name of the statement ("END")
     */
    @Override
    public final String content() {

        return "END";
    }
}
