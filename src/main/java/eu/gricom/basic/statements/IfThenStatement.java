package eu.gricom.basic.statements;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.memoryManager.LineNumberXRef;
import eu.gricom.basic.memoryManager.ProgramPointer;
import eu.gricom.basic.memoryManager.Stack;
import eu.gricom.basic.variableTypes.BooleanValue;
import eu.gricom.basic.variableTypes.IntegerValue;

/**
 * IfThenStatement.java
 * <p>
 * Description:
 * <p>
 * An "IF" then statement jumps execution to another place in the program, but only if an expression evaluates to
 * something other than 0.
 * <p>
 * (c) = 2004,...,2021 by Andreas Grimm, Den Haag, The Netherlands
 * <p>
 * Created in 2021
 *
 */
public final class IfThenStatement implements Statement {
    private final Expression _oCondition;
    private final String _strLabel;
    private int _iTokenNumber = 0;
    private int _iTargetLineNumber = 0;
    private  int _iElseStatement = 0;
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
     * Constructs an IF THEN statement for BASIC mode using line numbers for control flow.
     *
     * @param oCondition the condition expression to evaluate for the IF statement
     * @param iTokenNumber the token number associated with this statement
     * @param iELseStatement the line number of the ELSE statement, or -1 if not present
     * @param iEndIfLine the line number of the END-IF statement, or -1 if not present
     * @param iTargetLineNumber the line number to jump to if the condition evaluates to true, or -1 if not used
     */
    public IfThenStatement(final Expression oCondition,
                           final int iTokenNumber,
                           final int iELseStatement,
                           final int iEndIfLine,
                           final int iTargetLineNumber) {
        _iTokenNumber = iTokenNumber;
        _oCondition = oCondition;
        _strLabel = "";
        _iElseStatement = iELseStatement;
            _iEndIfLine = iEndIfLine;
        _iTargetLineNumber = iTargetLineNumber;
    }

    /**
     * Get Token Number.
     *
     * @return the command line number of the statement
     */
    @Override
    public int getTokenNumber() {
        return _iTokenNumber;
    }

    /**
     * Executes the IF statement, evaluating its condition and directing program flow accordingly.
     *
     * In label-based mode, jumps to the labeled statement if the condition is true. In line-number mode, evaluates the condition and jumps to the appropriate target line, ELSE, or END-IF statement as required. Throws a SyntaxErrorException if jump targets are invalid or missing.
     *
     * @throws Exception if memory management or statement resolution fails, or if jump targets are invalid.
     */
    public void execute() throws Exception {
        Stack oStack = new Stack();

        // This part of the method is executed if the BASIC interpreter uses labels (e.g., we are using JASIC)
        if (_oLabelStatement.containsLabelKey(_strLabel)) {
            BooleanValue bValue = (BooleanValue) _oCondition.evaluate();
            if (bValue.isTrue()) {
                _oProgramPointer.setCurrentStatement(_oLabelStatement.getLabelStatement(_strLabel));
            }

            return;
        }

        // Here we are using line numbers to jump to the destination. This is only done for BASIC programs.
        BooleanValue bValue = (BooleanValue) _oCondition.evaluate();

        // different to the code above: when the result of the condition is false, then ignore the next block and
        // jump to the END-IF statement.
        if (!bValue.isTrue()) {
            try {

                int iStatementNo = 0;

                if (_iElseStatement != 0) { // ok - we found an ELSE statement - so we jump there rather than to the END-IF
                    iStatementNo = _oLineNumberObject.getStatementFromLineNumber(_oLineNumberObject.getNextLineNumber(_iElseStatement));
                } else {
                    // no ELSE - check for an END-IF
                    if (_iEndIfLine != 0) {
                        iStatementNo = _oLineNumberObject.getStatementFromLineNumber(_oLineNumberObject.getNextLineNumber(_iEndIfLine));
                    }
                }

                if (iStatementNo != 0) {
                    _oProgramPointer.setCurrentStatement(iStatementNo);
                    return;
                }


                // only if the _iElseStatement, _iEndIfLine number and the _iTargetLineNumber are 0 then we have a
                // problem
                if (_iTargetLineNumber == 0) {
                    throw new SyntaxErrorException("IF [unknown]: Target: [" + _iEndIfLine + "]");
                }

                return;
            } catch (NumberFormatException eNumberException) {
                throw new SyntaxErrorException("IF [incorrect format]: Target: " + _strLabel);
            }
        }

        // If the condition is true, check whether we have a jump target. The jump target needs to be valid otherwise
        // an exception is thrown
        if (_iTargetLineNumber != 0) {
            int iStatementNo = _oLineNumberObject.getStatementFromLineNumber(_iTargetLineNumber);

            if (iStatementNo != 0) {
                _oProgramPointer.setCurrentStatement(iStatementNo);
                return;
            }
            throw new SyntaxErrorException("IF [unknown]: Target: [" + _iTargetLineNumber + "]");
        }

        if (_iElseStatement != 0) { // Ok - we found an ELSE statement - and we will run into it. So put the
            // necessary info on the stack, then ELSE can use it to jump over the ELSE block.
            int iStatementNo = _oLineNumberObject.getStatementFromLineNumber(_oLineNumberObject.getNextLineNumber(_iEndIfLine));
            oStack.push(new IntegerValue(iStatementNo));
        }

    }

    /**
     * Returns a string representation of the IF statement, showing its condition and target line number.
     *
     * @return a readable string in the format "IF (<condition>) THEN <target line number>"
     */
    @Override
    public String content() {

        return "IF (" + _oCondition.content() + ") THEN " + _iTargetLineNumber;
    }

    /**
     * Returns a JSON-like string describing the structure of this IF-THEN statement.
     *
     * The returned string includes the token number, condition structure, ELSE statement line, END-IF line, and target line number.
     *
     * @return a JSON-like string representing the internal structure of the IF-THEN statement
     * @throws Exception if retrieving the structure of the condition or other components fails
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"IF\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\",";
        strReturn += "\"CONDITION\": "+ _oCondition.structure() +",";
        strReturn += "\"ELSE_STATEMENT\": \""+ _iElseStatement +"\",";
        strReturn += "\"END_IF\": \""+ _iEndIfLine +"\",";
        strReturn += "\"TARGET_LINE\": \"" + _iTargetLineNumber +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
