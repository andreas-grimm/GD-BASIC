package eu.gricom.basic.statements;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.memoryManager.LineNumberXRef;
import eu.gricom.basic.memoryManager.ProgramPointer;
import eu.gricom.basic.memoryManager.Stack;
import eu.gricom.basic.variableTypes.IntegerValue;

/**
 * ReturnStatement.java
 * <p>
 * Description:
 * <p>
 * The Return command retrieves the caller address from the stack and calculates the next statement as return address.
 * It then changes the program pointer to that address.
 * <p>
 * (c) = 2004,...,2021 by Andreas Grimm, Den Haag, The Netherlands
 * <p>
 * Created in 2021
 */
public class ReturnStatement implements Statement {
    private final int _iTokenNumber;

    /**
     * Default constructor.
     * <p>
     * A "Return" statement terminates the GoSub subroutine and jumps to the command after the GoSub statement.
     *
     * @param iTokenNumber the line number of this command
     */

    public ReturnStatement(final int iTokenNumber) {
        _iTokenNumber = iTokenNumber;
    }

    @Override
    public final int getTokenNumber() {
        return _iTokenNumber;
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
        String strReturn = "{\"RETURN\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
