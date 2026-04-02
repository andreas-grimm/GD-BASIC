package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.Stack;
import eu.gricom.basic.variableTypes.IntegerValue;

/**
 * DoStatement.java
 * <p>
 * Description: The DoStatement class implements the start of a DO-UNTIL loop construct. Unlike WHILE loops, DO-UNTIL
 * executes the loop body at least once before checking the exit condition. This statement marks the loop entry point
 * and stores its position on the stack for the UNTIL statement to reference.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class DoStatement implements Statement {
    private final int _iTokenNumber;

    /**
     * Default constructor.
     * <p>
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
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("INPUT") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"DO\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
