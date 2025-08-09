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
