package eu.gricom.basic.statements;


import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.tokenizer.BasicTokenType;
import eu.gricom.basic.variableTypes.Value;

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

    /****
     * Constructs an OperatorExpression with the specified left and right expressions and an operator represented as a string.
     *
     * @param oLeft the left-hand side expression
     * @param strOperator the operator as a string (e.g., "+", "-", "*", etc.)
     * @param oRight the right-hand side expression
     */
    public OperatorExpression(final Expression oLeft, final String strOperator, final Expression oRight) {
        _oLogger.debug("--->  " + oLeft.content() + " " + strOperator + " " + oRight.content());
        _oLeft = oLeft;
        _strOperator = strOperator;
        _oOperator = null;
        _oRight = oRight;
    }

    /****
     * Constructs an OperatorExpression with the specified left and right expressions and an operator represented as a BasicTokenType.
     *
     * @param oLeft the left-hand side expression
     * @param oOperator the operator as a BasicTokenType
     * @param oRight the right-hand side expression
     */
    public OperatorExpression(final Expression oLeft, final BasicTokenType oOperator, final Expression oRight) {
        _oLogger.debug("--->  " + oLeft.content() + " " + oOperator + " " + oRight.content());
        _oLeft = oLeft;
        _strOperator = null;
        _oOperator = oOperator;
        _oRight = oRight;
    }

    /**
     * Evaluates the binary operator expression and returns the resulting value.
     *
     * Recursively evaluates the left and right sub-expressions, applies the specified arithmetic, logical, comparison, or bitwise operator, and returns the computed result as a {@code Value}.
     *
     * @return the result of applying the operator to the evaluated left and right expressions
     * @throws SyntaxErrorException if the operator is unknown or unsupported
     * @throws Exception if evaluation of sub-expressions fails
     */
    public final Value evaluate() throws Exception {
        Value oLeftValue = _oLeft.evaluate();
        Value oRightValue = _oRight.evaluate();

        if (_strOperator != null) { // needed for Jasic
            return switch (_strOperator) {
                case "=" -> oLeftValue.equals(oRightValue); //TODO should this not be an assign?
                case "+" -> oLeftValue.plus(oRightValue);
                case "-" -> oLeftValue.minus(oRightValue);
                case "*" -> oLeftValue.multiply(oRightValue);
                case "/" -> oLeftValue.divide(oRightValue);
                case "^" -> oLeftValue.power(oRightValue);
                case "&", "AND" -> oLeftValue.and(oRightValue);
                case "|", "OR" -> oLeftValue.or(oRightValue);
                case "==" -> oLeftValue.equals(oRightValue);
                case "!=" -> oLeftValue.notEqual(oRightValue);
                case "<" -> oLeftValue.smallerThan(oRightValue);
                case "<=" -> oLeftValue.smallerEqualThan(oRightValue);
                case ">" -> oLeftValue.largerThan(oRightValue);
                case ">=" -> oLeftValue.largerEqualThan(oRightValue);
                case "%" -> oLeftValue.modulo(oRightValue);
                case ">>" -> oLeftValue.shiftRight(oRightValue);
                case "<<" -> oLeftValue.shiftLeft(oRightValue);
                default -> throw new SyntaxErrorException("Unknown operator: " + _strOperator);
            };
        }

        return switch (_oOperator) {
            case PLUS -> oLeftValue.plus(oRightValue);
            case MINUS -> oLeftValue.minus(oRightValue);
            case MULTIPLY -> oLeftValue.multiply(oRightValue);
            case DIVIDE -> oLeftValue.divide(oRightValue);
            case POWER -> oLeftValue.power(oRightValue);
            case AND -> oLeftValue.and(oRightValue);
            case OR -> oLeftValue.or(oRightValue);
            case COMPARE_EQUAL -> oLeftValue.equals(oRightValue);
            case COMPARE_NOT_EQUAL -> oLeftValue.notEqual(oRightValue);
            case SMALLER -> oLeftValue.smallerThan(oRightValue);
            case SMALLER_EQUAL -> oLeftValue.smallerEqualThan(oRightValue);
            case GREATER -> oLeftValue.largerThan(oRightValue);
            case GREATER_EQUAL -> oLeftValue.largerEqualThan(oRightValue);
            case MODULO -> oLeftValue.modulo(oRightValue);
            case SHIFT_RIGHT -> oLeftValue.shiftRight(oRightValue);
            case SHIFT_LEFT -> oLeftValue.shiftLeft(oRightValue);
            default -> throw new SyntaxErrorException("Unknown operator: " + _strOperator);
        };
    }

    /**
     * Returns the left-hand side expression of this operator expression.
     *
     * @return the left sub-expression
     */
    public final Expression getLeft() {

        return _oLeft;
    }

    /**
     * Returns the operator of this expression as a string.
     *
     * @return the operator as a string, or "null" if the operator is not set as a string
     */
    public final String getOperator() {

        return String.valueOf(_strOperator);
    }

    /**
     * Returns the right-hand side expression of this operator expression.
     *
     * @return the right sub-expression
     */
    public final Expression getRight() {

        return _oRight;
    }

    /**
     * Returns a string representation of the operator expression, including the contents of the left and right sub-expressions and the operator.
     *
     * @return a readable string showing the structure of the operator expression for debugging or testing purposes
     */
    public final String content() {
        return _oLeft.content() + " " + _oOperator + " " + _oRight.content();

    }

    /**
     * Returns a JSON-like string representing the structure of this operator expression, including its left and right sub-expressions and the operator.
     *
     * @return a string describing the structure of the operator expression for use by the compiler
     * @throws Exception if retrieving the structure of a sub-expression fails
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"OPERATOR\": {";
        strReturn += "\"LEFT_EXPRESSION\": " + _oLeft.structure() + ",";

        if (_strOperator != null) {
            strReturn += "\"OPERATOR_STRING\": \""+ _strOperator +"\"";
        }
        if (_oOperator != null) {
            strReturn += "\"OPERATOR_OBJECT\": \""+ _oOperator + "\",";
        }
        strReturn += "\"RIGHT_EXPRESSION\": "+ _oRight.structure();
        strReturn += "}}";

        return strReturn;
    }
}
