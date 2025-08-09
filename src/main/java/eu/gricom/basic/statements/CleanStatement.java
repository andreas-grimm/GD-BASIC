package eu.gricom.basic.statements;

import eu.gricom.basic.error.EmptyStackException;
import eu.gricom.basic.memoryManager.Stack;
import eu.gricom.basic.helper.Logger;

/**
 * CleanStatement.java
 * <p>
 * Description:
 * <p>
 * The DimStatement class defines all kinds of arrays.
 * <p>
 * (c) = 2020,...,2025 by Andreas Grimm, Den Haag, The Netherlands
 */
public class CleanStatement implements Statement {
    private final int _iTokenNumber;
    private final Logger _oLogger = new Logger(this.getClass().getName());

    /**
     * Constructs a CleanStatement with the specified token number.
     *
     * @param iTokenNumber the position of the "CLEAN" statement in the source program
     */
    public CleanStatement(final int iTokenNumber) {
        _iTokenNumber = iTokenNumber;
    }

    /**
     * Returns the token number indicating the position of this statement in the source code.
     *
     * @return the token number of the statement
     */
    @Override
    public final int getTokenNumber() {
        return _iTokenNumber;
    }

    /**
     * Removes the last entry from the stack, if present.
     *
     * If the stack is empty, logs an error message.
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
     * Returns the string representation of the statement for testing purposes.
     *
     * @return the string "END"
     */
    @Override
    public final String content() {

        return "END";
    }

    /**
     * Returns a JSON-formatted string describing the statement type as "CLEAN" and its token number.
     *
     * @return a JSON string with the statement type and token number.
     * @throws Exception if an error occurs during structure generation.
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"CLEAN\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
