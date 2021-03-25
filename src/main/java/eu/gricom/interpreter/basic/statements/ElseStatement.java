package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.memoryManager.ProgramPointer;
import eu.gricom.interpreter.basic.memoryManager.Stack;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;

/**
 * ElseStatement.java
 *
 * Description:
 *
 * A ELSE statement only appears in connection with the IF command. It has two functions: the IF command will use the
 * location of the ELSE command in case the condition is not met. The second function is for the program flow. When
 * the program flow, after successful evaluation of the IF command, hits the ELSE function, the program flow jumps to
 * the command past the END-IF command.
 *
 * (c) = 2004,..,2021 by Andreas Grimm, Den Haag, The Netherlands
 *
 * Created in 2021
 */
public class ElseStatement implements Statement {
    private final ProgramPointer _oProgramPointer = new ProgramPointer();
    private final int _iLineNumber;

    /**
     * Default constructor.
     *
     * An "ELSE" statement jumps to the command past the "END-IF" command.
     *
     * @param iLineNumber the line number of this command
     */
    public ElseStatement(final int iLineNumber) {

        _iLineNumber = iLineNumber;
    }

    @Override
    public final int getLineNumber() {
        return _iLineNumber;
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
        return "ELSE";
    }
}
