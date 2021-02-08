package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.memoryManager.LineNumberXRef;
import eu.gricom.interpreter.basic.memoryManager.ProgramPointer;
import eu.gricom.interpreter.basic.variableTypes.BooleanValue;

/**
 * IfThenStatement.java
 * <p>
 * Description:
 * <p>
 * An if then statement jumps execution to another place in the program, but only if an expression evaluates to
 * something other than 0.
 * <p>
 * (c) = 2004,..,2021 by Andreas Grimm, Den Haag, The Netherlands
 * <p>
 * Created in 2021
 *
 */
public final class IfThenStatement implements Statement {
    private final Expression _oCondition;
    private final String _strLabel;
    private int _iStatementNumber = 0;
    private final ProgramPointer _oProgramPointer = new ProgramPointer();
    private final LabelStatement _oLabelStatement = new LabelStatement();
    private final LineNumberXRef _oLineNumberObject = new LineNumberXRef();
    private final int _iEndIfLine;

    /**
     * JASIC constructor.
     *
     * @param oCondition - condition to be tested.
     * @param strLabel - destination for the jump after successful completion of the condition.
     */
    public IfThenStatement(final Expression oCondition, final String strLabel) {
        _oCondition = oCondition;
        _strLabel = strLabel;
        _iEndIfLine = 0;
    }

    /**
     * BASIC constructor.
     *
     * @param oCondition - condition to be tested.
     * @param iStatementNumber - the sequence number of this statement in the program
     * @param iEndIfLine - destination for the jump after unsuccessful completion of the condition.
     */
    public IfThenStatement(final Expression oCondition, final int iStatementNumber, final int iEndIfLine) {
        _iStatementNumber = iStatementNumber;
        _oCondition = oCondition;
        _strLabel = "";
        _iEndIfLine = iEndIfLine;
    }

    /**
     * Get Line Number.
     *
     * @return iLineNumber - the command line number of the statement
     */
    @Override
    public int getLineNumber() {
        return _iStatementNumber;
    }

    /**
     * Execute the If statement.
     *
     * @throws Exception - exposes any exception coming from the memory management
     */
    public void execute() throws Exception {
        // This part of the method is executed if the BASIC interpreter uses labels (e.g. we are using JASIC)
        if (_oLabelStatement.containsLabelKey(_strLabel)) {
            BooleanValue bValue = (BooleanValue) _oCondition.evaluate();
            if (bValue.isTrue()) {
                _oProgramPointer.setCurrentStatement(_oLabelStatement.getLabelStatement(_strLabel));
            }

            return;
        }

        // here we are using line numbers to jump to the destination. This is only done for BASIC programs.
        BooleanValue bValue = (BooleanValue) _oCondition.evaluate();

        // different to the code above: when the result of the condition is false, then ignore the next block and
        // jump to the END-IF statement.
        if (!bValue.isTrue()) {
            try {

                if (_iEndIfLine != 0) {
                    int iStatementNo =
                            _oLineNumberObject.getStatementFromLineNumber(_oLineNumberObject.getNextLineNumber(_iEndIfLine));
                    if (iStatementNo != 0) {
                        _oProgramPointer.setCurrentStatement(iStatementNo);
                        return;
                    }

                    return;
                }

                throw new SyntaxErrorException("IF [unknown]: Target: [" + _iEndIfLine + "]");
            } catch (NumberFormatException eNumberException) {
                throw new SyntaxErrorException("IF [incorrect format]: Target: " + _strLabel);
            }
        }

    }

    /**
     * This method is used in testing and debugging. It returns the set values when the constructor has been called.
     *
     * @return - readable string with the name and the value of the assignment
     */
    @Override
    public String content() {

        return "IF (" + _oCondition.content() + ") THEN " + _strLabel;
    }
}
