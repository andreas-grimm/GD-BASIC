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
 * A For statement counts an integer or real value from a start value to an end value - and with every increase it
 * loops through the block from the "For" statement to the next "Next" statement. When the target value is reached, the
 * program flow will jump to the statement past the next statement.
 * <p>
 * (c) = 2004,...,2021 by Andreas Grimm, Den Haag, The Netherlands
 * <p>
 * Created in 2021
 */
public class EndWhileStatement implements Statement {
    private final ProgramPointer _oProgramPointer = new ProgramPointer();
    private final int _iTokenNumber;

    /**
     * Default constructor.
     * <p>
     * An "end-while" statement returns to the "while" loop start.
     *
     * @param iTokenNumber the line number of this command
     */
    public EndWhileStatement(final int iTokenNumber) {
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

    /**
     * Returns the string representation of this statement as "NEXT".
     *
     * @return the string "NEXT"
     */
    @Override
    public final String content() {
        return "NEXT";
    }

    /**
     * Returns a JSON-like string representing the structure of this END_WHILE statement for compiler use.
     *
     * @return a string containing the statement type "END_WHILE" and its token number.
     * @throws Exception if an error occurs while generating the structure.
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"END_WHILE\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
