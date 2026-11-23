package eu.gricom.basic.statements;

import eu.gricom.basic.memorymanager.LineNumberXRef;
import eu.gricom.basic.memorymanager.ProgramPointer;
import eu.gricom.basic.memorymanager.Stack;
import eu.gricom.basic.memorymanager.VariableManagement;
import eu.gricom.basic.variabletypes.IntegerValue;
import eu.gricom.basic.variabletypes.RealValue;

/**
 * ForStatement.java
 * <p>
 * Description: The ForStatement class implements the BASIC FOR-NEXT loop construct. It initialises a loop counter,
 * evaluates the end condition and optional step value, and manages iteration through the loop body until the target
 * value is reached. The loop state is maintained on the stack for nested loop support.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
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
     * Gets a previously consumed token, indexing backwards. Last(1) will
     * be the token just consumed, last(2) the one before that, etc.
     *
     * @param iTokenNumber number of the token that is translated into the "FOR" statement
     * @param strName Name of the counting variable.
     * @param oStartValueExpression Expression for the calculation of start value.
     * @param oEndValueExpression Expression for the calculation of the end value.
     * @param oStepSize size of the steps in which the loop is processed
     * @param iEndForStatement location of the next command to be processed after the loop
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

    @Override
    public int getTokenNumber() {
        return _iTokenNumber;
    }

    @Override
    public void execute() throws Exception {
        boolean bCountingDown = _oStepSize.evaluate().toReal() < 0;

        final LineNumberXRef oLineNumberObject = new LineNumberXRef();

        if (_bForStarted) {
            // if the FOR loop is already started - i.e., this is the second iteration then process here
            double fEndValue = _oEndValue.evaluate().toReal();
            double fStepSize = _oStepSize.evaluate().toReal();
            double fCounter = _oVariableManagement.getMap(_strName).toReal();

            if (fCounter + fStepSize <= fEndValue && !bCountingDown
                || fCounter + fStepSize >= fEndValue && bCountingDown) {

                // if the sum of the current value and step size remains lower than the end value, continue
                _oVariableManagement.putMap(_strName, new RealValue(RealValue.round(fCounter + fStepSize, 2)));
                _oStack.push(new IntegerValue(oLineNumberObject.getStatementFromToken(_iTokenNumber)));

            } else  {
                // the loop is finished: reset the flag so that the next time this FOR statement is reached
                // (for example from another outer loop) it starts a new loop with the start value
                _bForStarted = false;
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

    @Override
    public String content() throws Exception {
        double fCounter;
        String strStartValue = _oEndValue.content();
        String strEndValue = _oEndValue.content();
        String strStepSize = _oStepSize.content();
        if (_oVariableManagement.getMap(_strName) != null) {
            fCounter = _oVariableManagement.getMap(_strName).toReal();
        } else {
            fCounter = _oStartValue.evaluate().toReal();
        }

        return "FOR (" + _strName + " = " + strStartValue + " TO " + strEndValue + " STEP " + strStepSize + "): ["
                + fCounter + "]";
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
        String strReturn = "{\"FOR\": {";
        strReturn += "\"TOKEN_NR\": \"" + _iTokenNumber + "\",";
        strReturn += "\"NAME\": \"" + _strName + "\",";
        strReturn += "\"START_VALUE_EXPRESSION\": \"" + _oStartValue.structure() + "\",";
        strReturn += "\"END_VALUE_EXPRESSION\": \"" + _oEndValue.structure() + "\",";
        strReturn += "\"STEP_SIZE\": \"" + _oStepSize.structure() + "\",";
        strReturn += "\"END_IF_STATEMENT\": \"" + _iEndForStatement + "\"";
        strReturn += "}}";
        return strReturn;
    }

    /** @return the loop variable name (used by the compiler) */
    public String getName() {
        return _strName;
    }

    /** @return the start value expression (used by the compiler) */
    public Expression getStartValue() {
        return _oStartValue;
    }

    /** @return the end value expression (used by the compiler) */
    public Expression getEndValue() {
        return _oEndValue;
    }

    /** @return the step expression (used by the compiler) */
    public Expression getStepSize() {
        return _oStepSize;
    }

    /** @return the line number of the matching NEXT (used by the compiler) */
    public int getEndForStatement() {
        return _iEndForStatement;
    }
}

