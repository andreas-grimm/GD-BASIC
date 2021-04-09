package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.error.EmptyStackException;
import eu.gricom.interpreter.basic.memoryManager.Stack;

/**
 * CleanStatement.java
 * <p>
 * Description:
 * <p>
 * The DimStatement class defines all kind of arrays.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class CleanStatement implements Statement {
    private final int _iLineNumber;

    /**
     * Default constructor.
     *
     * An "DIM" statement initializes an array of any type.
     * @param iLineNumber - number of the command in the basic program
     */
    public CleanStatement(final int iLineNumber) {
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
     * Remove the last stack entry.
     */
    public final void execute() {
        Stack oStack = new Stack();

        try {
            oStack.pop();
        } catch (EmptyStackException e) {
            e.printStackTrace();
        }
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
