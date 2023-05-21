package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.Stack;
import eu.gricom.basic.variableTypes.IntegerValue;

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
    private final int _iTokenNumber;

    /**
     * Default constructor.
     *
     * The "Do" statement defines the return location for the "Until" command for the "do-until" loop.
     * The class has no further function.
     *
     * @param iTokenNumber the line number of this command
     */
    public DoStatement(final int iTokenNumber) {
        _iTokenNumber = iTokenNumber;
    }

    @Override
    public final int getTokenNumber() {
        return _iTokenNumber;
    }

    @Override
    public final void execute() throws Exception {
        final Stack oStack = new Stack();

        oStack.push(new IntegerValue(_iTokenNumber));
    }

    @Override
    public final String content() {
        return "DO";
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
        String strReturn = this.toString();
        return strReturn;
    }
}
