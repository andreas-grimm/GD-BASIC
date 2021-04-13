package eu.gricom.interpreter.basic.statements;


import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.helper.Logger;
import eu.gricom.interpreter.basic.tokenizer.BasicTokenType;
import eu.gricom.interpreter.basic.variableTypes.Value;

/**
 * An operator expression evaluates two expressions and then performs some
 * arithmetic operation on the results.
 */

public class OperatorExpression implements Expression {
    private final Logger _oLogger = new Logger(this.getClass().getName());
    private final Expression _oLeft;
    private final String _strOperator;
    private final BasicTokenType _oOperator;
    private final Expression _oRight;

    /**
     * Default constructor.
     *
     * @param oLeft left part of the operation
     * @param strOperator the actual operator - defined as a single char
     * @param oRight right side of the operation
     */
    public OperatorExpression(final Expression oLeft, final String strOperator, final Expression oRight) {
        _oLogger.debug("--->  " + oLeft.content() + " " + strOperator + " " + oRight.content());
        _oLeft = oLeft;
        _strOperator = strOperator;
        _oOperator = null;
        _oRight = oRight;
    }

    /**
     * Default constructor.
     *
     * @param oLeft left part of the operation
     * @param oOperator the actual operator - defined as a BasicTokenType
     * @param oRight right side of the operation
     */
    public OperatorExpression(final Expression oLeft, final BasicTokenType oOperator, final Expression oRight) {
        _oLogger.debug("--->  " + oLeft.content() + " " + oOperator + " " + oRight.content());
        _oLeft = oLeft;
        _strOperator = null;
        _oOperator = oOperator;
        _oRight = oRight;
    }

    /**
     * Return the content of the result of the operation as a value variable.
     *
     * @throws Exception syntax error is the operator cannot be executed
     * @return returns the result of the operation.
     */
    public final Value evaluate() throws Exception {
        Value oLeftValue = _oLeft.evaluate();
        Value oRightValue = _oRight.evaluate();

        if (_strOperator != null) { // needed for Jasic
            switch (_strOperator) {
                case "=":
                    return oLeftValue.equals(oRightValue);

                case "+":
                    return oLeftValue.plus(oRightValue);
                case "-":
                    return oLeftValue.minus(oRightValue);
                case "*":
                    return oLeftValue.multiply(oRightValue);
                case "/":
                    return oLeftValue.divide(oRightValue);
                case "^":
                    return oLeftValue.power(oRightValue);

                case "&":
                case "AND":
                    return oLeftValue.and(oRightValue);
                case "|":
                case "OR":
                    return oLeftValue.or(oRightValue);

                case "==":
                    return oLeftValue.equals(oRightValue);
                case "!=":
                    return oLeftValue.notEqual(oRightValue);
                case "<":
                    return oLeftValue.smallerThan(oRightValue);
                case "<=":
                    return oLeftValue.smallerEqualThan(oRightValue);
                case ">":
                    return oLeftValue.largerThan(oRightValue);
                case ">=":
                    return oLeftValue.largerEqualThan(oRightValue);

                case "%":
                    return oLeftValue.modulo(oRightValue);
                case ">>":
                    return oLeftValue.shiftRight(oRightValue);
                case "<<":
                    return oLeftValue.shiftLeft(oRightValue);

                default:
                    throw new SyntaxErrorException("Unknown operator: " + _strOperator);
            }
        }

        switch (_oOperator) {
            case PLUS:
                return oLeftValue.plus(oRightValue);
            case MINUS:
                return oLeftValue.minus(oRightValue);
            case MULTIPLY:
                return oLeftValue.multiply(oRightValue);
            case DIVIDE:
                return oLeftValue.divide(oRightValue);
            case POWER:
                return oLeftValue.power(oRightValue);

            case AND:
                return oLeftValue.and(oRightValue);
            case OR:
                return oLeftValue.or(oRightValue);

            case COMPARE_EQUAL:
                return oLeftValue.equals(oRightValue);
            case COMPARE_NOT_EQUAL:
                return oLeftValue.notEqual(oRightValue);
            case SMALLER:
                return oLeftValue.smallerThan(oRightValue);
            case SMALLER_EQUAL:
                return oLeftValue.smallerEqualThan(oRightValue);
            case GREATER:
                return oLeftValue.largerThan(oRightValue);
            case GREATER_EQUAL:
                return oLeftValue.largerEqualThan(oRightValue);

            case MODULO:
                return oLeftValue.modulo(oRightValue);
            case SHIFT_RIGHT:
                return oLeftValue.shiftRight(oRightValue);
            case SHIFT_LEFT:
                return oLeftValue.shiftLeft(oRightValue);

            default:
                throw new SyntaxErrorException("Unknown operator: " + _strOperator);
        }
    }

    /**
     * get the left side of the operation.
     *
     * @return the left side of the operation as an expression
     */
    public final Expression getLeft() {

        return _oLeft;
    }

    /**
     * get the operator of the operation.
     *
     * @return - the operator as a string
     */
    public final String getOperator() {

        return String.valueOf(_strOperator);
    }

    /**
     * get the right side of the operation.
     *
     * @return the right side of the operation as an expression
     */
    public final Expression getRight() {

        return _oRight;
    }

    /**
     * This method is used in testing and debugging. It returns the set values when the constructor has been called.
     *
     * @return - readable string with the name and the value of the assignment
     */
    public final String content() {
        return _oLeft.content() + " " + _oOperator + " " + _oRight.content();

    }
}
