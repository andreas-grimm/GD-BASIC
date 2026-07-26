package eu.gricom.basic.statements;

import eu.gricom.basic.error.EndOfProgramException;

/**
 * EndStatement.java
 * <p>
 * Description: The EndStatement class implements the BASIC END command, which terminates program execution. When
 * reached, it signals the interpreter to stop processing statements and exit the program cleanly.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
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
    public final void execute() throws Exception {
        throw new EndOfProgramException();
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
        String strReturn = "{\"END\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
