package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.memoryManager.Stack;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;

/**
 * DoStatement.java
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
public class DoStatement implements Statement {
    private final int _iLineNumber;

    /**
     * Default constructor.
     *
     * The "Do" statement defines the return location for the "Until" command for the "do-until" loop.
     * The class has no further function.
     *
     * @param iLineNumber the line number of this command
     */
    public DoStatement(final int iLineNumber) {
        _iLineNumber = iLineNumber;
    }

    @Override
    public final int getLineNumber() {
        return _iLineNumber;
    }

    @Override
    public final void execute() throws Exception {
        final Stack oStack = new Stack();

        oStack.push(new IntegerValue(_iLineNumber));
    }

    @Override
    public final String content() {
        return "DO";
    }
}
