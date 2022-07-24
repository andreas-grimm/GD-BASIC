package eu.gricom.basic.statements;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.memoryManager.LineNumberXRef;
import eu.gricom.basic.memoryManager.ProgramPointer;
import eu.gricom.basic.memoryManager.Stack;
import eu.gricom.basic.variableTypes.BooleanValue;
import eu.gricom.basic.variableTypes.IntegerValue;

/**
 *
 * Description:
 *
 * A While statement loops thru the block from the While statement to the next End-While statement as long as the
 * condition in the While statement is True. When the condition defined is changing from True to False, the program
 * flow will jump to the next statement past the End-While statement.
 *
 * (c) = 2004,..,2021 by Andreas Grimm, Den Haag, The Netherlands
 *
 * Created in 2021
 */
public final class UntilStatement implements Statement {
    private final int _iTokenNumber;
    private final Expression _oCondition;
    private final ProgramPointer _oProgramPointer = new ProgramPointer();
    private final Stack _oStack = new Stack();


    /**
     * Gets a previously consumed token, indexing backwards. last(1) will
     * be the token just consumed, last(2) the one before that, etc.
     *
     * @param iTokenNumber number of the token that is translated into the FOR statement
     * @param oCondition the condition, when true, enters the loop
     */
    public UntilStatement(final int iTokenNumber,
                          final Expression oCondition) {
        _iTokenNumber = iTokenNumber;
        _oCondition = oCondition;
    }

    @Override
    public int getTokenNumber() {
        return _iTokenNumber;
    }

    @Override
    public void execute() throws Exception {
        final LineNumberXRef oLineNumberObject = new LineNumberXRef();

        int iDoCommandLine = ((IntegerValue) _oStack.pop()).toInt();

        // here we are using line numbers to jump to the destination. This is only done for BASIC programs.
        BooleanValue bValue = (BooleanValue) _oCondition.evaluate();

        // different to the code above: when the result of the condition is false, then ignore the next block and
        // jump to the END-IF statement.
        if (!bValue.isTrue()) {
            try {
                int iStatementNo =
                    oLineNumberObject.getStatementFromToken(iDoCommandLine);
                if (iStatementNo != 0) {
                    _oProgramPointer.setCurrentStatement(iStatementNo);
                }
            } catch (NumberFormatException eNumberException) {
                throw new SyntaxErrorException("UNTIL [incorrect format]: Target: " + iDoCommandLine);
            }
        }
    }

    @Override
    public String content() throws Exception {
        return "UNTIL (" + _oCondition.content() + ")";
    }
}
