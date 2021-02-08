package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.helper.Logger;

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
    private final int _iLineNumber;
    private final String _strRemMessage;
    private final Logger _oLogger = new Logger(this.getClass().getName());

    /**
     * Default constructor.
     *
     * An "END" statement performs the hard termination of the interpreter.
     */
    public RemStatement(final String strRemMessage) {
        _strRemMessage = strRemMessage;
        _iLineNumber = 0;
    }

    /**
     * Default constructor.
     *
     * An "REM" statement performs the hard termination of the interpreter.
     * @param iLineNumber - number of the command in the basic program
     */
    public RemStatement(final int iLineNumber, final String strRemMessage) {
        _strRemMessage = strRemMessage;
        _iLineNumber = iLineNumber;
    }

    /**
     * Get Line Number.
     *
     * @return iLineNumber - the command line number of the statement
     */
    @Override
    public final int getLineNumber() {
        return _iLineNumber;
    }

    /**
     * Execute.
     *
     * Terminate the running program.
     */
    public final void execute() {
        _oLogger.debug("REM - " + _strRemMessage);
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

        return "REM";
    }
}
