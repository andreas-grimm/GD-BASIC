package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.Stack;
import eu.gricom.basic.variableTypes.IntegerValue;

/**
 * DoStatement.java
 * <p>
 * Description:
 * <p>
 * A 'Do' statement counts is an accepting loop, executing first ('Do') and checks the result and exit
 * condition at the end ('Until').
 * <p>
 * (c) = 2004,...,2021 by Andreas Grimm, Den Haag, The Netherlands
 * <p>
 * Created in 2021
 */
public class DoStatement implements Statement {
    private final int _iTokenNumber;

    /**
     * Constructs a DoStatement marking the position of a "Do" statement in a do-until loop.
     *
     * @param iTokenNumber the line number where the "Do" statement appears
     */
    public DoStatement(final int iTokenNumber) {
        _iTokenNumber = iTokenNumber;
    }

    /**
     * Returns the token number associated with this "Do" statement.
     *
     * @return the line number where the "Do" statement appears in the source code
     */
    @Override
    public final int getTokenNumber() {
        return _iTokenNumber;
    }

    /**
     * Pushes the token number of this "Do" statement onto a new stack as an IntegerValue.
     *
     * This method is typically used to mark the position of the "Do" statement for loop control constructs.
     *
     * @throws Exception if an error occurs during stack operations
     */
    @Override
    public final void execute() throws Exception {
        final Stack oStack = new Stack();

        oStack.push(new IntegerValue(_iTokenNumber));
    }

    /**
     * Returns the string representation of the "Do" statement.
     *
     * @return the string "DO"
     */
    @Override
    public final String content() {
        return "DO";
    }

    /**
     * Returns a JSON-formatted string describing the structure of the "DO" statement, including its token number.
     *
     * @return a JSON string with the statement name "DO" and its associated token number.
     * @throws Exception if an error occurs during structure generation.
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"DO\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
