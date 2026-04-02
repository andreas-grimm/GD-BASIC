package eu.gricom.basic.memoryManager;

/**
 * ProgramPointer.java
 * <p>
 * Description: The ProgramPointer class tracks the current execution position within the BASIC program. It provides
 * methods to get and set the current statement index, and to calculate the next statement for sequential execution.
 * Control flow statements modify this pointer to implement jumps, loops, and subroutine calls.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class ProgramPointer {
    private static int _iCurrentStatement = 0;

    /**
     * set the current statement number.
     *
     * @param iCurrentStatement - number of the current statement
     */
    public final void setCurrentStatement(final int iCurrentStatement) {

        _iCurrentStatement = iCurrentStatement;
    }

    /**
     * get the current statement number.
     *
     * @return - current statement number
     */
    public final int getCurrentStatement() {

        return _iCurrentStatement;
    }

    /**
     * calculate the next statement number.
     */
    public final void calcNextStatement() {

        _iCurrentStatement++;
    }

}
