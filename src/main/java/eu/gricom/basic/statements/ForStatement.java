package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.LineNumberXRef;
import eu.gricom.basic.memoryManager.ProgramPointer;
import eu.gricom.basic.memoryManager.Stack;
import eu.gricom.basic.memoryManager.VariableManagement;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.RealValue;

/**
 * ForStatement.java
 * <p>
 * Description:
 * <p>
 * A For statement counts an integer or real value from a start value to an end value - and with every increase it
 * loops through the block from the "For" statement to the next "Next" statement. When the target value is reached, the
 * program flow will jump to the statement past the next statement.
 * <p>
 * (c) = 2004,...,2021 by Andreas Grimm, Den Haag, The Netherlands
 * <p>
 * Created in 2021
 */
public final class ForStatement implements Statement {
    private boolean _bForStarted = false;
    private final String _strName;
    private final Expression _oStartValue;
    private final Expression _oEndValue;
    private final Expression _oStepSize;
    private final int _iEndForStatement;
    private final int _iTokenNumber;
    private final VariableManagement _oVariableManagement = new VariableManagement();
    private final ProgramPointer _oProgramPointer = new ProgramPointer();
    private final Stack _oStack = new Stack();

    /**
    * Constructs a ForStatement representing a "For" loop with the specified loop variable, start value, end value, step size, and the statement location following the loop.
    *
    * @param iTokenNumber the token number associated with this "For" statement
    * @param strName the name of the loop counter variable
    * @param oStartValueExpression the expression representing the loop's start value
    * @param oEndValueExpression the expression representing the loop's end value
    * @param oStepSize the expression representing the loop's step size
    * @param iEndForStatement the location of the statement to execute after the loop ends
    */
    public ForStatement(final int iTokenNumber,
                        final String strName,
                        final Expression oStartValueExpression,
                        final Expression oEndValueExpression,
                        final Expression oStepSize,
                        final int iEndForStatement) {
        _iTokenNumber = iTokenNumber;
        _strName = strName;
        _oStartValue = oStartValueExpression;
        _oEndValue = oEndValueExpression;
        _oStepSize = oStepSize;
        _iEndForStatement = iEndForStatement;
   }

    /**
     * Returns the token number associated with this "For" statement.
     *
     * @return the token number identifying this statement
     */
    @Override
    public int getTokenNumber() {
        return _iTokenNumber;
    }

    /**
     * Executes one iteration of the "For" loop, updating the loop variable and controlling loop flow.
     *
     * If the loop has already started, evaluates whether the next counter value remains within the loop bounds and either continues the loop or exits it. If the loop is starting, initializes the loop variable and prepares for iteration.
     *
     * @throws Exception if evaluation of expressions or variable management fails.
     */
    @Override
    public void execute() throws Exception {
        boolean bCountingDown = _oStepSize.evaluate().toReal() < 0;

        final LineNumberXRef oLineNumberObject = new LineNumberXRef();

        if (_bForStarted) {
            // if the FOR loop is already started - i.e., this is the second iteration then process here
            double dEndValue = _oEndValue.evaluate().toReal();
            double dStepSize = _oStepSize.evaluate().toReal();
            double dCounter = _oVariableManagement.getMap(_strName).toReal();

            if (dCounter + dStepSize <= dEndValue && !bCountingDown
                || dCounter + dStepSize >= dEndValue && bCountingDown) {

                // if the sum of the current value and step size remains lower than the end value, continue
                _oVariableManagement.putMap(_strName, new RealValue(RealValue.round(dCounter + dStepSize, 2)));
                _oStack.push(new IntegerValue(oLineNumberObject.getStatementFromToken(_iTokenNumber)));

            } else  {
                // calculate the final sum of the current value and leave the loop
                _oProgramPointer.setCurrentStatement(
                        oLineNumberObject.getStatementFromLineNumber(
                                oLineNumberObject.getNextLineNumber(_iEndForStatement)));
            }
        } else {
            // Define the variable and set the initial value. If the value already exists - overwrite.
            _oStack.push(new IntegerValue(oLineNumberObject.getStatementFromToken(_iTokenNumber)));
            _oVariableManagement.putMap(_strName, _oStartValue.evaluate());
            _bForStarted = true;
        }
    }

    /**
     * Returns a string representation of the current state of the "For" loop, including the loop variable name, start value, end value, step size, and current counter value.
     *
     * @return A string summarizing the loop's configuration and current counter.
     * @throws Exception if evaluating expressions or retrieving variable values fails.
     */
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

        return "FOR (" + _strName + " = " + strStartValue + " TO " + strEndValue + " STEP " + strStepSize + "): ["
                + dCounter + "]";
    }

    /**
     * Returns a JSON-like string describing the structure of this "For" statement for use by the compiler.
     *
     * The returned string includes the token number, loop variable name, serialized start value, end value, step size expressions, and the statement location following the loop.
     *
     * @return a JSON-like string representing the structure of the "For" statement and its parameters
     * @throws Exception if an error occurs while serializing any of the expression components
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"FOR\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\",";
        strReturn += "\"NAME\": \""+ _strName +"\",";
        strReturn += "\"START_VALUE_EXPRESSION\": \""+ _oStartValue.structure() +"\",";
        strReturn += "\"END_VALUE_EXPRESSION\": \""+ _oEndValue.structure() +"\",";
        strReturn += "\"STEP_SIZE\": \""+ _oStepSize.structure() +"\",";
        strReturn += "\"END_IF_STATEMENT\": \""+ _iEndForStatement +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
