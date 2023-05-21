package eu.gricom.basic.memoryManager;

/**
 * ProgramPointer.java
 * <p>
 * Description:
 * <p>
 * The Program Pointer contains the current position of the program.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
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
