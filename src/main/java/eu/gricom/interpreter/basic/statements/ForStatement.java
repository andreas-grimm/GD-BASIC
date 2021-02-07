package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.helper.Logger;
import eu.gricom.interpreter.basic.memoryManager.LineNumberXRef;
import eu.gricom.interpreter.basic.memoryManager.ProgramPointer;
import eu.gricom.interpreter.basic.memoryManager.Stack;
import eu.gricom.interpreter.basic.memoryManager.VariableManagement;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.Value;

/**
 * ForStatement.java
 *
 * Description:
 *
 * A For statement counts an integer or real value from a start value to an end value - and with every increase it
 * loops thru the block from the For statement to the next Next statement. When the target value is reached, the
 * program flow will jump to the statement past the next statement.
 *
 * (c) = 2004,..,2021 by Andreas Grimm, Den Haag, The Netherlands
 *
 * Created in 2021
 */
public final class ForStatement implements Statement {
    private boolean _bForStarted = false;
    private final String _strName;
    private final Expression _oStartValue;
    private final Value _oEndValue;
    private final Value _oStepSize;
    private final int _iPostLoopStatement;
    private final int _iTokenNo;
    private final VariableManagement _oVariableManagement = new VariableManagement();
    private final ProgramPointer _oProgramPointer = new ProgramPointer();
    private final Stack _oStack = new Stack();
    private final boolean _bCountingDown;

    /**
     * Gets a previously consumed token, indexing backwards. last(1) will
     * be the token just consumed, last(2) the one before that, etc.
     *
     * @param iTokenNo number of the token that is translated into the FOR statement
     * @param strName Name of the counting variable.
     * @param oStartValueExpression Expression for the calculation of start value.
     * @param oEndValueExpression Expression for the calculation of the end value.
     * @param oStepSize size of the steps in which the loop is processed
     * @param iPostLoopStatement location of the next command to be processed after the loop
     */
    public ForStatement(final int iTokenNo,
                        final String strName,
                        final Expression oStartValueExpression,
                        final Value oEndValueExpression,
                        final Value oStepSize,
                        final int iPostLoopStatement) {
        _iTokenNo = iTokenNo;
        _strName = strName;
        _oStartValue = oStartValueExpression;
        _oEndValue = oEndValueExpression;
        _oStepSize = oStepSize;
        _iPostLoopStatement = iPostLoopStatement;

        if (oStepSize.toReal() < 0) {
            _bCountingDown = true;
        } else {
            _bCountingDown = false;
        }
    }

    @Override
    public int getLineNumber() {
        return _iTokenNo;
    }

    @Override
    public void execute() throws Exception {
        final Logger oLogger = new Logger(this.getClass().getName());
        final LineNumberXRef oLineNumberObject = new LineNumberXRef();

        if (_bForStarted) {
            // if the FOR loop is already started - ie. this is the second iteration then process here
            double dEndValue = _oEndValue.evaluate().toReal();
            double dStepSize = _oStepSize.toReal();
            double dCounter = _oVariableManagement.getMap(_strName).toReal();

            if (((dCounter + dStepSize <= dEndValue) && (!_bCountingDown))
                || ((dCounter + dStepSize >= dEndValue) && (_bCountingDown))) {

                // if the sum of the current value + step size remains lower than the end value, continue
                _oVariableManagement.putMap(_strName, new RealValue(RealValue.round(dCounter + dStepSize, 2)));
                oLogger.debug("_iTokenNo: " + _iTokenNo);
                oLogger.debug("_iStatementNo: " + oLineNumberObject.getStatementFromToken(_iTokenNo));
                _oStack.push(new IntegerValue(oLineNumberObject.getStatementFromToken(_iTokenNo)));

                return;
            } else  {
                // calculate the final sum of the current value and leave the loop
                _oProgramPointer.setCurrentStatement(
                        oLineNumberObject.getStatementFromLineNumber(
                                oLineNumberObject.getNextLineNumber(_iPostLoopStatement)));
                return;
            }
        } else {
            // define the variable and set the initial value. If the value already exists - overwrite.
            _oStack.push(new IntegerValue(oLineNumberObject.getStatementFromToken(_iTokenNo)));
            oLogger.debug("_iTokenNo: " + _iTokenNo);
            oLogger.debug("_iStatementNo: " + oLineNumberObject.getStatementFromToken(_iTokenNo));
            _oVariableManagement.putMap(_strName, _oStartValue.evaluate());
            _bForStarted = true;
        }
    }

    @Override
    public String content() throws Exception {
        double dCounter;
        double dEndValue = _oEndValue.evaluate().toReal();
        double dStepSize = _oStepSize.toReal();
        if (_oVariableManagement.getMap(_strName) != null) {
            dCounter = _oVariableManagement.getMap(_strName).toReal();
        } else {
            dCounter = _oStartValue.evaluate().toReal();
        }

        String strReturn =
                "FOR (" + _strName + " = " + _oStartValue.evaluate().toString() + " TO " + dEndValue + " STEP " + dStepSize + "): [" + dCounter + "]";
        return strReturn;
    }
}
