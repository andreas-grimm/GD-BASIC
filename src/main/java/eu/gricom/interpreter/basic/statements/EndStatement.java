package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.helper.Logger;

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
    private final Logger _oLogger = new Logger(this.getClass().getName());
    private final int _iLineNumber;

    /**
     * Default constructor.
     *
     * An "END" statement performs the hard termination of the interpreter.
     */
    public EndStatement() {
        _iLineNumber = 0;
    }

    /**
     * Default constructor.
     *
     * An "END" statement performs the hard termination of the interpreter.
     * @param iLineNumber - number of the command in the basic program
     */
    public EndStatement(final int iLineNumber) {
        _iLineNumber = iLineNumber;
    }

    /**
     * Get Line Number.
     *
     * @return iLineNumber - the command line number of the statement
     */
    @Override
    public int getLineNumber() {
        return (_iLineNumber);
    }

    /**
     * Execute.
     *
     * Terminate the running program.
     */
    public final void execute() throws Exception {
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

        return ("END");
    }
}
