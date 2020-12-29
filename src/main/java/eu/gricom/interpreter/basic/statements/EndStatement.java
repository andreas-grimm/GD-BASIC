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
    private int _iPosition;

    /**
     * Default constructor.
     *
     * An "END" statement performs the hard termination of the interpreter.
     */
    public EndStatement(int iPosition) {
        _iPosition = iPosition;
        _oLogger.debug("--> END: " +  iPosition);
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

        return ("END");
    }
}
