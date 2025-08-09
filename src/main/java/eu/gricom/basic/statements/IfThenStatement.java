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
     * BASIC constructor.
     *
     * @param oCondition - condition to be tested.
     * @param iTokenNumber - the number of this token related to this statement
     * @param iELseStatement location of the next command to be processed after the else command
     * @param iEndIfLine - destination for the jump after unsuccessful completion of the condition.
     * @param iTargetLineNumber - alternative syntax: if the coder wants to use a jump target if the condition is
     *                          true, the jump target is added here.
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
     * Execute the If statement.
     *
     * @throws Exception - exposes any exception coming from the memory management
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
     * This method is used in testing and debugging. It returns the set values when the constructor has been called.
     *
     * @return - readable string with the name and the value of the assignment
     */
    @Override
    public String content() {

        return "IF (" + _oCondition.content() + ") THEN " + _iTargetLineNumber;
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
