package eu.gricom.basic.statements;

import eu.gricom.basic.error.EmptyStackException;
import eu.gricom.basic.memoryManager.Stack;
import eu.gricom.basic.helper.Logger;

/**
 * CleanStatement.java
 * <p>
 * Description: The CleanStatement class removes stale entries from the interpreter stack when program flow exits a
 * block (such as a loop or subroutine) prematurely. This ensures the stack remains consistent after GOTO statements
 * that jump out of structured blocks.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class CleanStatement implements Statement {
    private final int _iTokenNumber;
    private final Logger _oLogger = new Logger(this.getClass().getName());

    /**
     * Default constructor.
     * <p>
     * An "CLEAN" statement cleans the stack from the last entrance if you jump out of the block.
     * @param iTokenNumber - number of the command in the basic program
     */
    public CleanStatement(final int iTokenNumber) {
        _iTokenNumber = iTokenNumber;
    }

    /**
     * Get Token Number.
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
     * Remove the last stack entry.
     */
    public final void execute() {
        Stack oStack = new Stack();

        try {
            oStack.pop();
        } catch (EmptyStackException eException) {
            _oLogger.error(eException.getMessage());
        }
    }

    /**
     * Content.
     * <p>
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
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("INPUT") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"CLEAN\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
