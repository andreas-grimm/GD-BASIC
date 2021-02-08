package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.memoryManager.LineNumberXRef;
import eu.gricom.interpreter.basic.memoryManager.ProgramPointer;
import eu.gricom.interpreter.basic.memoryManager.Stack;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;

/**
 * ReturnStatement.java
 *
 * Description:
 *
 * The Return command retrieves the caller address from the stack and calculates the next statement as return address.
 * It then changes the program pointer to that address.
 *
 * (c) = 2004,..,2021 by Andreas Grimm, Den Haag, The Netherlands
 *
 * Created in 2021
 */
public class ReturnStatement implements Statement {
    private final int _iLineNumber;

    /**
     * Default constructor.
     *
     * An "return" statement terminates the GoSub subroutine and jumps to the command after the GoSub statement.
     *
     * @param iLineNumber the line number of this command
     */

    public ReturnStatement(final int iLineNumber) {
        _iLineNumber = iLineNumber;
    }

    @Override
    public final int getLineNumber() {
        return _iLineNumber;
    }

    @Override
    public final void execute() throws Exception {
        final Stack oStack = new Stack();
        final ProgramPointer oProgramPointer = new ProgramPointer();
        final LineNumberXRef oLineNumberObject = new LineNumberXRef();

        int iTargetLineNumber = ((IntegerValue) oStack.pop()).toInt();

        if (iTargetLineNumber == 0) {
            throw new SyntaxErrorException("Undefined Jump Target");
        } else {
            oProgramPointer.setCurrentStatement(oLineNumberObject.getStatementFromLineNumber(
                    oLineNumberObject.getNextLineNumber(
                            oLineNumberObject.getLineNumberFromToken(
                                    oLineNumberObject.getTokenFromStatement(iTargetLineNumber)))));
        }

    }

    @Override
    public final String content() {
        return "RETURN";
    }
}
