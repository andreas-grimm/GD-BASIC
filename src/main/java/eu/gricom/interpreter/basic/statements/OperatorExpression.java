package eu.gricom.interpreter.basic.statements;


import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.helper.Logger;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
import eu.gricom.interpreter.basic.variableTypes.Value;

/**
 * An operator expression evaluates two expressions and then performs some
 * arithmetic operation on the results.
 */

public class OperatorExpression implements Expression {
    private Logger _oLogger = new Logger(this.getClass().getName());
    private final Expression _oLeft;
    private final char _strOperator;
    private final Expression _oRight;

    /**
     * Default constructor.
     *
     * @param oLeft left part of the operation
     * @param strOperator the actual operator - defined as a single char
     * @param oRight right side of the operation
     */
    public OperatorExpression(final Expression oLeft, final char strOperator, final Expression oRight) {
        _oLogger.debug("--->  " + oLeft.content() + " " + strOperator + " " + oRight.content());
        _oLeft = oLeft;
        _strOperator = strOperator;
        _oRight = oRight;
    }

    /**
     * Return the content of the result of the operation as a value variable.
     *
     * @throws Exception syntax error is the operator cannot be executed
     * @return returns the result of the operation.
     */
    public final Value evaluate() throws Exception {
        Value leftVal = _oLeft.evaluate();
        Value rightVal = _oRight.evaluate();

        switch (_strOperator) {
            case '=':
                // Coerce to the left argument's type, then compare.
                if (leftVal instanceof RealValue) {
                    if (leftVal.toReal() == rightVal.toReal()) {
                        return (new RealValue(1));
                    } else {
                        return (new RealValue(0));
                    }
                }
                if (leftVal.toString().equals(rightVal.toString())) {
                    return (new RealValue(1));
                } else {
                    return (new RealValue(0));
                }

            case '+':
                // Addition if the left argument is a number, otherwise do
                // string concatenation.
                if (leftVal instanceof RealValue) {
                    return new RealValue(leftVal.toReal() + rightVal.toReal());
                }
                return new StringValue(leftVal.toString() + rightVal.toString());

            case '-':
                return new RealValue(leftVal.toReal() - rightVal.toReal());
            case '*':
                return new RealValue(leftVal.toReal() * rightVal.toReal());
            case '/':
                return new RealValue(leftVal.toReal() / rightVal.toReal());
            case '<':
                // Coerce to the left argument's type, then compare.
                if (leftVal instanceof RealValue) {
                    if (leftVal.toReal() < rightVal.toReal()) {
                        return (new RealValue(1));
                    } else {
                        return (new RealValue(0));
                    }
                }
                if (leftVal.toString().compareTo(rightVal.toString()) < 0) {
                    return (new RealValue(1));
                } else {
                    return (new RealValue(0));
                }

            case '>':
                // Coerce to the left argument's type, then compare.
                if (leftVal instanceof RealValue) {
                    if (leftVal.toReal() > rightVal.toReal()) {
                        return (new RealValue(1));
                    } else {
                        return (new RealValue(0));
                    }
                }

                if (leftVal.toString().compareTo(rightVal.toString()) > 0) {
                    return (new RealValue(1));
                } else {
                    return (new RealValue(0));
                }

            default:
                // TODO - return a syntax error
                throw new SyntaxErrorException("Unknown operator: " + _strOperator);
        }
    }

    /**
     * get the left side of the operation.
     *
     * @return the left side of the operation as an expression
     */
    public final Expression getLeft() {

        return (_oLeft);
    }

    /**
     * get the operator of the operation.
     *
     * @return - the operator as a string
     */
    public final String getOperator() {

        return (String.valueOf(_strOperator));
    }

    /**
     * get the right side of the operation.
     *
     * @return the right side of the operation as an expression
     */
    public final Expression getRight() {

        return (_oRight);
    }

    /**
     * This method is used in testing and debugging. It returns the set values when the constructor has been called.
     *
     * @return - readable string with the name and the value of the assignment
     */
    public final String content() {
        return (_oLeft.content() + " " + _strOperator + " " + _oRight.content());

    }
}
