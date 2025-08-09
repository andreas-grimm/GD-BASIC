package eu.gricom.basic.statements;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.tokenizer.BasicTokenType;
import eu.gricom.basic.variableTypes.Value;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.BooleanValue;

/**
 * UnaryOperatorExpression handles unary operators that operate on a single operand.
 * <p>
 * Supported unary operators:
 * - PLUS (+): Unary plus (usually no effect, but validates the operand)
 * - MINUS (-): Unary minus (negates the operand)
 * - NOT (!): Logical NOT (inverts boolean values, bitwise NOT for integers)
 * <p>
 * This class follows the same pattern as OperatorExpression but handles
 * single-operand operations instead of binary operations.
 * 
 * @author GD-BASIC Team
 * @version 1.0
 */
public class UnaryOperatorExpression implements Expression {
    private final Logger _oLogger = new Logger(this.getClass().getName());
    private final BasicTokenType _oOperator;
    private final Expression _oOperand;

    /**
     * Creates a unary operator expression with the specified operator and operand.
     *
     * @param oOperator the unary operator to apply (must be PLUS, MINUS, or NOT)
     * @param oOperand the operand expression to which the operator is applied
     * @throws IllegalArgumentException if the operator is not PLUS, MINUS, or NOT
     */
    public UnaryOperatorExpression(final BasicTokenType oOperator, final Expression oOperand) {
        _oLogger.debug("--->  " + oOperator + " " + oOperand.content());
        _oOperator = oOperator;
        _oOperand = oOperand;
        
        // Validate that the operator is a valid unary operator
        if (oOperator != BasicTokenType.PLUS && 
            oOperator != BasicTokenType.MINUS && 
            oOperator != BasicTokenType.NOT) {
            throw new IllegalArgumentException("Invalid unary operator: " + oOperator);
        }
    }

    /**
     * Evaluates the unary operator expression by applying the specified operator to the evaluated operand.
     *
     * @return the result of applying the unary operator to the operand
     * @throws SyntaxErrorException if the operator is unknown or the operand is invalid for the operation
     * @throws Exception if an error occurs during operand evaluation
     */
    @Override
    public final Value evaluate() throws Exception {
        Value operandValue = _oOperand.evaluate();
        
        _oLogger.debug("Evaluating unary operator: " + _oOperator + " on operand: " + operandValue);

        return switch (_oOperator) {
            case PLUS -> evaluateUnaryPlus(operandValue);
            case MINUS -> evaluateUnaryMinus(operandValue);
            case NOT -> evaluateUnaryNot(operandValue);
            default -> throw new SyntaxErrorException("Unknown unary operator: " + _oOperator);
        };
    }

    /**
     * Applies the unary plus operator to the operand, returning it unchanged.
     *
     * @param operand the value to which the unary plus is applied
     * @return the operand value, unchanged
     * @throws SyntaxErrorException if the operand is null
     */
    private Value evaluateUnaryPlus(Value operand) throws SyntaxErrorException {
        // Unary plus is mostly a no-op, but we validate the operand
        if (operand == null) {
            throw new SyntaxErrorException("Cannot apply unary plus to null value");
        }
        return operand;
    }

    /****
     * Applies the unary minus operator to the given operand, returning its numeric negation as a `RealValue`.
     *
     * @param operand the value to negate; must be convertible to a real number
     * @return a `RealValue` representing the negated operand
     * @throws SyntaxErrorException if the operand is null or cannot be converted to a numeric value
     */
    private Value evaluateUnaryMinus(Value operand) throws SyntaxErrorException {
        if (operand == null) {
            throw new SyntaxErrorException("Cannot apply unary minus to null value");
        }
        
        try {
            // Convert to real number and negate
            double numericValue = operand.toReal();
            return new RealValue(-numericValue);
        } catch (NumberFormatException e) {
            throw new SyntaxErrorException("Cannot apply unary minus to non-numeric value: " + operand);
        }
    }

    /**
     * Applies the unary NOT operator to the given operand.
     *
     * For boolean operands, returns the logical negation. For numeric operands, performs a bitwise NOT on the operand's long integer representation and returns the result as an integer or real value as appropriate.
     *
     * @param operand the value to which the NOT operator is applied
     * @return the result of the NOT operation as a BooleanValue, IntegerValue, or RealValue
     * @throws SyntaxErrorException if the operand is null or cannot be converted to a boolean or numeric value
     */
    private Value evaluateUnaryNot(Value operand) throws SyntaxErrorException {
        if (operand == null) {
            throw new SyntaxErrorException("Cannot apply unary NOT to null value");
        }
        
        try {
            // Try to convert to boolean first
            if (operand instanceof BooleanValue) {
                boolean boolValue = operand.toReal() != 0.0;
                return new BooleanValue(!boolValue);
            }
            
            // For numeric values, perform bitwise NOT
            double numericValue = operand.toReal();
            long longValue = (long) numericValue;
            long notValue = ~longValue;
            
            // Return as integer if it fits, otherwise as real
            if (notValue >= Integer.MIN_VALUE && notValue <= Integer.MAX_VALUE) {
                return new IntegerValue((int) notValue);
            } else {
                return new RealValue((double) notValue);
            }
            
        } catch (NumberFormatException e) {
            throw new SyntaxErrorException("Cannot apply unary NOT to non-numeric value: " + operand);
        }
    }

    /**
     * Returns the unary operator used in this expression.
     *
     * @return the unary operator as a BasicTokenType
     */
    public final BasicTokenType getOperator() {
        return _oOperator;
    }

    /**
     * Returns the operand expression to which the unary operator is applied.
     *
     * @return the operand expression
     */
    public final Expression getOperand() {
        return _oOperand;
    }

    /**
     * Returns a string representation of the unary operator expression in the format "operator(operand)".
     *
     * @return a human-readable string representing the unary expression
     */
    @Override
    public final String content() {
        String operatorSymbol = getOperatorSymbol(_oOperator);
        return operatorSymbol + "(" + _oOperand.content() + ")";
    }

    /**
     * Returns a JSON-like string representing the structure of this unary operator expression, including its operator and operand.
     *
     * @return a string describing the structure of the unary operator expression for use by the compiler
     * @throws Exception if retrieving the operand's structure fails
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"UNARY_OP\": {";
        strReturn += "\"OPERATOR\": \""+ _oOperator.toString() +"\",";
        strReturn += "\"OPERAND\": \""+ _oOperand.structure() +"\"";
        strReturn += "}}";
        return strReturn;
    }

    /**
     * Returns the string symbol corresponding to the given unary operator.
     *
     * @param operator the unary operator token
     * @return the symbol representing the operator ("+", "-", or "!")
     */
    private String getOperatorSymbol(BasicTokenType operator) {
        return switch (operator) {
            case PLUS -> "+";
            case MINUS -> "-";
            case NOT -> "!";
            default -> operator.toString();
        };
    }

    /**
     * Returns a string describing the unary operator expression, including its operator and operand.
     *
     * @return a string representation of the unary operator expression
     */
    @Override
    public String toString() {
        return "UnaryOperatorExpression{" +
                "operator=" + _oOperator +
                ", operand=" + _oOperand +
                '}';
    }
} 