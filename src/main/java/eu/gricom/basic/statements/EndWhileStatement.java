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
public class EndWhileStatement implements Statement {
    private final ProgramPointer _oProgramPointer = new ProgramPointer();
    private final int _iTokenNumber;

    /**
     * Default constructor.
     *
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

    @Override
    public final String content() {
        return "NEXT";
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
