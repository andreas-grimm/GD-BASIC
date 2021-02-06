package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.memoryManager.ProgramPointer;
import eu.gricom.interpreter.basic.memoryManager.Stack;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;

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

    int  _iLineNumber;

    public NextStatement (final int iLineNumber) {
        _iLineNumber = iLineNumber;
    }

    @Override
    public int getLineNumber() {
        return _iLineNumber;
    }

    @Override
    public void execute() throws Exception {
        final Stack oStack = new Stack();

        int iTargetLineNumber = ((IntegerValue) oStack.pop()).toInt();

        if (iTargetLineNumber == 0) {
            throw (new SyntaxErrorException("Undefined Jump Target"));
        } else {
            _oProgramPointer.setCurrentStatement(iTargetLineNumber);
        }

    }

    @Override
    public String content() {
        return ("NEXT");
    }
}