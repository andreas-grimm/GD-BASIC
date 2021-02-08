package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.memoryManager.LineNumberXRef;
import eu.gricom.interpreter.basic.memoryManager.ProgramPointer;
import eu.gricom.interpreter.basic.memoryManager.Stack;
import eu.gricom.interpreter.basic.variableTypes.BooleanValue;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;

/**
 * WhileStatement.java
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
public final class WhileStatement implements Statement {
    private final int _iEndWhileLine;
    private final int _iTokenNo;
    private final Expression _oCondition;
    private final ProgramPointer _oProgramPointer = new ProgramPointer();
    private final LineNumberXRef _oLineNumberObject = new LineNumberXRef();
    private final Stack _oStack = new Stack();


    /**
     * Gets a previously consumed token, indexing backwards. last(1) will
     * be the token just consumed, last(2) the one before that, etc.
     *
     * @param iTokenNo number of the token that is translated into the FOR statement
     * @param oCondition the condition, when true, enters the loop
     * @param iEndWhileLine location of the next command to be processed after the loop
     */
    public WhileStatement(final int iTokenNo,
                          final Expression oCondition,
                          final int iEndWhileLine) {
        _iTokenNo = iTokenNo;
        _oCondition = oCondition;
        _iEndWhileLine = iEndWhileLine;
    }

    @Override
    public int getLineNumber() {
        return _iTokenNo;
    }

    @Override
    public void execute() throws Exception {
        final LineNumberXRef oLineNumberObject = new LineNumberXRef();

        // here we are using line numbers to jump to the destination. This is only done for BASIC programs.
        BooleanValue bValue = (BooleanValue) _oCondition.evaluate();

        // different to the code above: when the result of the condition is false, then ignore the next block and
        // jump to the END-IF statement.
        if (!bValue.isTrue()) {
            try {
                if (_iEndWhileLine != 0) {
                    int iStatementNo =
                        _oLineNumberObject.getStatementFromLineNumber(_oLineNumberObject.getNextLineNumber(_iEndWhileLine));
                    if (iStatementNo != 0) {
                        _oProgramPointer.setCurrentStatement(iStatementNo);
                        return;
                    }
                    return;
                }

                throw new SyntaxErrorException("WHILE [unknown]: Target: [" + _iEndWhileLine + "]");
            } catch (NumberFormatException eNumberException) {
                throw new SyntaxErrorException("WHILE [incorrect format]: Target: " + _iEndWhileLine);
            }
        } else {
            _oStack.push(new IntegerValue(oLineNumberObject.getStatementFromToken(_iTokenNo)));
        }
    }

    @Override
    public String content() throws Exception {
        return "WHILE (" + _oCondition.content() + ")";
    }
}
