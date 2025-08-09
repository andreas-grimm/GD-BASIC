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
 * Description:
 * <p>
 * A While statement loops through the block from the While statement to the next End-While statement as long as the
 * condition in the While statement is True. When the condition defined is changing from True to False, the program
 * flow will jump to the next statement past the End-While statement.
 * <p>
 * (c) = 2004,...,2021 by Andreas Grimm, Den Haag, The Netherlands
 * <p>
 * Created in 2021
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

    /**
     * Executes the WHILE statement by evaluating its condition and controlling program flow accordingly.
     *
     * If the condition evaluates to false, advances execution to the statement following the corresponding END-WHILE line.
     * If the condition evaluates to true, pushes the current statement number onto the loop control stack to mark the start of the loop.
     *
     * @throws SyntaxErrorException if the END-WHILE target line is invalid or cannot be parsed.
     */
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

    /**
     * Returns a string representation of the WHILE statement, including its condition.
     *
     * @return the WHILE statement as a string with its condition
     * @throws Exception if retrieving the condition's content fails
     */
    @Override
    public String content() throws Exception {
        return "WHILE (" + _oCondition.content() + ")";
    }

    /**
     * Returns a JSON-like string describing the structure of this WHILE statement for use by the compiler.
     *
     * @return a string containing the statement type, token number, condition structure, and END-WHILE line number
     * @throws Exception if an error occurs while generating the condition structure
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
