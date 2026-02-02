package eu.gricom.basic.statements;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.memoryManager.LineNumberXRef;
import eu.gricom.basic.memoryManager.ProgramPointer;
import eu.gricom.basic.memoryManager.Stack;
import eu.gricom.basic.variableTypes.BooleanValue;
import eu.gricom.basic.variableTypes.IntegerValue;

/**
 * WhileStatement.java
 * <p>
 * Description: The WhileStatement class implements the BASIC WHILE-ENDWHILE loop construct. It evaluates a condition
 * at the start of each iteration and continues executing the loop body while the condition is true. When false, control
 * passes to the statement following ENDWHILE.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class WhileStatement implements Statement {
    private final int _iEndWhileLine;
    private final int _iTokenNumber;
    private final Expression _oCondition;
    private final ProgramPointer _oProgramPointer = new ProgramPointer();
    private final LineNumberXRef _oLineNumberObject = new LineNumberXRef();
    private final Stack _oStack = new Stack();


    /**
     * Gets a previously consumed token, indexing backwards. Last (1) will
     * be the token just consumed, last(2) the one before that, etc.
     *
     * @param iTokenNumber number of the token that is translated into the 'FOR' statement
     * @param oCondition the condition, when true, enters the loop
     * @param iEndWhileLine location of the next command to be processed after the loop
     */
    public WhileStatement(final int iTokenNumber,
                          final Expression oCondition,
                          final int iEndWhileLine) {
        _iTokenNumber = iTokenNumber;
        _oCondition = oCondition;
        _iEndWhileLine = iEndWhileLine;
    }

    @Override
    public int getTokenNumber() {
        return _iTokenNumber;
    }

    @Override
    public void execute() throws Exception {
        final LineNumberXRef oLineNumberObject = new LineNumberXRef();

        // Here we are using line numbers to jump to the destination. This is only done for BASIC programs.
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
            _oStack.push(new IntegerValue(oLineNumberObject.getStatementFromToken(_iTokenNumber)));
        }
    }

    @Override
    public String content() throws Exception {
        return "WHILE (" + _oCondition.content() + ")";
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
        String strReturn = "{\"WHILE\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\",";
        strReturn += "\"CONDITION\": \""+ _oCondition.structure() +"\",";
        strReturn += "\"END_WHILE\": \""+ _iEndWhileLine +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
