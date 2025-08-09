package eu.gricom.basic.statements;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.memoryManager.ProgramPointer;
import eu.gricom.basic.memoryManager.Stack;
import eu.gricom.basic.variableTypes.IntegerValue;

/**
 * NextStatement.java
 * <p>
 * Description:
 * <p>
 * A "Next" statement closes the most outside open "For" loop and triggers the next run of the "For"
 * loop.
 * <p>
 * (c) = 2004,...,2021 by Andreas Grimm, Den Haag, The Netherlands
 * <p>
 * Created in 2021
 */
public class NextStatement implements Statement {
    private final ProgramPointer _oProgramPointer = new ProgramPointer();
    private final int _iTokenNumber;

    /**
     * Constructs a NextStatement with the specified line number.
     *
     * @param iTokenNumber the line number where this "NEXT" statement appears in the program
     */
    public NextStatement(final int iTokenNumber) {
        _iTokenNumber = iTokenNumber;
    }

    /**
     * Returns the line number (token number) associated with this "Next" statement.
     *
     * @return the token number of this statement
     */
    @Override
    public final int getTokenNumber() {
        return _iTokenNumber;
    }

    /**
     * Advances execution to the next iteration of the most outer open "For" loop.
     *
     * Pops the target line number for the next loop iteration from the stack and sets the program pointer to that line.
     * Throws a {@code SyntaxErrorException} if the target line number is zero, indicating an undefined jump target.
     *
     * @throws SyntaxErrorException if the target line number is zero
     */
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

    /**
     * Returns the string representation of this statement, which is "NEXT".
     *
     * @return the string "NEXT"
     */
    @Override
    public final String content() {
        return "NEXT";
    }

    /**
     * Returns a JSON-like string describing the structure of this "NEXT" statement, including its token number.
     *
     * @return a string representation of the statement type and its token number
     * @throws Exception if an error occurs while generating the structure
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"NEXT\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
