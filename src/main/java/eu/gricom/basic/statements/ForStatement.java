package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.LineNumberXRef;
import eu.gricom.basic.memoryManager.ProgramPointer;
import eu.gricom.basic.memoryManager.Stack;
import eu.gricom.basic.memoryManager.VariableManagement;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.RealValue;

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
    private final Expression _oEndValue;
    private final Expression _oStepSize;
    private final int _iEndIfStatement;
    private final int _iTokenNumber;
    private final VariableManagement _oVariableManagement = new VariableManagement();
    private final ProgramPointer _oProgramPointer = new ProgramPointer();
    private final Stack _oStack = new Stack();

    /**
     * Gets a previously consumed token, indexing backwards. last(1) will
     * be the token just consumed, last(2) the one before that, etc.
     *
     * @param iTokenNumber number of the token that is translated into the FOR statement
     * @param strName Name of the counting variable.
     * @param oStartValueExpression Expression for the calculation of start value.
     * @param oEndValueExpression Expression for the calculation of the end value.
     * @param oStepSize size of the steps in which the loop is processed
     * @param iEndIfStatement location of the next command to be processed after the loop
     */
    public ForStatement(final int iTokenNumber,
                        final String strName,
                        final Expression oStartValueExpression,
                        final Expression oEndValueExpression,
                        final Expression oStepSize,
                        final int iEndIfStatement) throws Exception {
        _iTokenNumber = iTokenNumber;
        _strName = strName;
        _oStartValue = oStartValueExpression;
        _oEndValue = oEndValueExpression;
        _oStepSize = oStepSize;
        _iEndIfStatement = iEndIfStatement;
   }

    @Override
    public int getTokenNumber() {
        return _iTokenNumber;
    }

    @Override
    public void execute() throws Exception {
        boolean bCountingDown = true;
        if (_oStepSize.evaluate().toReal() < 0) {
            bCountingDown = true;
        } else {
            bCountingDown = false;
        }

        final LineNumberXRef oLineNumberObject = new LineNumberXRef();

        if (_bForStarted) {
            // if the FOR loop is already started - ie. this is the second iteration then process here
            double dEndValue = _oEndValue.evaluate().toReal();
            double dStepSize = _oStepSize.evaluate().toReal();
            double dCounter = _oVariableManagement.getMap(_strName).toReal();

            if (dCounter + dStepSize <= dEndValue && !bCountingDown
                || dCounter + dStepSize >= dEndValue && bCountingDown) {

                // if the sum of the current value + step size remains lower than the end value, continue
                _oVariableManagement.putMap(_strName, new RealValue(RealValue.round(dCounter + dStepSize, 2)));
                _oStack.push(new IntegerValue(oLineNumberObject.getStatementFromToken(_iTokenNumber)));

            } else  {
                // calculate the final sum of the current value and leave the loop
                _oProgramPointer.setCurrentStatement(
                        oLineNumberObject.getStatementFromLineNumber(
                                oLineNumberObject.getNextLineNumber(_iEndIfStatement)));
            }
            return;
        } else {
            // define the variable and set the initial value. If the value already exists - overwrite.
            _oStack.push(new IntegerValue(oLineNumberObject.getStatementFromToken(_iTokenNumber)));
            _oVariableManagement.putMap(_strName, _oStartValue.evaluate());
            _bForStarted = true;
        }
    }

    @Override
    public String content() throws Exception {
        double dCounter;
        String strStartValue = _oEndValue.content();
        String strEndValue = _oEndValue.content();
        String strStepSize = _oStepSize.content();
        if (_oVariableManagement.getMap(_strName) != null) {
            dCounter = _oVariableManagement.getMap(_strName).toReal();
        } else {
            dCounter = _oStartValue.evaluate().toReal();
        }

        String strReturn =
                "FOR (" + _strName + " = " + strStartValue + " TO " + strEndValue + " STEP " + strStepSize + "): ["
                        + dCounter + "]";
        return strReturn;
    }

    /**
     * Structure.
     *
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("INPUT") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = this.toString();
        return strReturn;
    }
}
