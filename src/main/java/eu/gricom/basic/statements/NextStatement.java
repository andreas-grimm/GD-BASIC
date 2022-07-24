package eu.gricom.basic.statements;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.memoryManager.ProgramPointer;
import eu.gricom.basic.memoryManager.Stack;
import eu.gricom.basic.variableTypes.IntegerValue;

/**
 * NextStatement.java
 *
 * Description:
 *
 * A For statement counts an integer or real value from a start value to an end value - and with every increase it
 * loops thru the block from the For statement to the next Next statement. When the target value is reached, the
 * program flow will jump to the statement past the next statement.
 *
 * (c) = 2004,..,2021 by Andreas Grimm, Den Haag, The Netherlands
 *
 * Created in 2021
 */
public class NextStatement implements Statement {
    private final ProgramPointer _oProgramPointer = new ProgramPointer();
    private final int _iTokenNumber;

    /**
     * Default constructor.
     *
     * An "next" statement returns to the "for" loop start.
     *
     * @param iTokenNumber the line number of this command
     */
    public NextStatement(final int iTokenNumber) {
        _iTokenNumber = iTokenNumber;
    }

    @Override
    public final int getTokenNumber() {
        return _iTokenNumber;
    }

    @Override
    public final void execute() throws Exception {
        final Stack oStack = new Stack();

        int iTargetLineNumber = ((IntegerValue) oStack.pop()).toInt();
        if (iTargetLineNumber == 0) {
            throw new SyntaxErrorException("Undefined Jump Target");
        } else {
            _oProgramPointer.setCurrentStatement(iTargetLineNumber);
        }

    }

    @Override
    public final String content() {
        return "NEXT";
    }
}
