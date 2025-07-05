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
 * 
 * Supported unary operators:
 * - PLUS (+) : Unary plus (usually no effect, but validates the operand)
 * - MINUS (-) : Unary minus (negates the operand)
 * - NOT (!) : Logical NOT (inverts boolean values, bitwise NOT for integers)
 * 
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
     * Constructor for unary operator expressions.
     *
     * @param oOperator the unary operator (PLUS, MINUS, or NOT)
     * @param oOperand the operand to apply the operator to
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
     * Evaluates the unary operator expression.
     * 
     * @return the result of applying the unary operator to the operand
     * @throws Exception if the operation cannot be performed
     */
    @Override
    public final Value evaluate() throws Exception {
        Value operandValue = _oOperand.evaluate();
        
        _oLogger.debug("Evaluating unary operator: " + _oOperator + " on operand: " + operandValue);
        
        switch (_oOperator) {
            case PLUS:
                return evaluateUnaryPlus(operandValue);
                
            case MINUS:
                return evaluateUnaryMinus(operandValue);
                
            case NOT:
                return evaluateUnaryNot(operandValue);
                
            default:
                throw new SyntaxErrorException("Unknown unary operator: " + _oOperator);
        }
    }

    /**
     * Evaluates unary plus operator (+).
     * Unary plus typically has no effect but validates the operand.
     * 
     * @param operand the operand value
     * @return the operand value unchanged
     * @throws SyntaxErrorException if the operand is null
     */
    private Value evaluateUnaryPlus(Value operand) throws SyntaxErrorException {
        // Unary plus is mostly a no-op, but we validate the operand
        if (operand == null) {
            throw new SyntaxErrorException("Cannot apply unary plus to null value");
        }
        return operand;
    }

    /**
     * Evaluates unary minus operator (-).
     * Negates the operand value.
     * 
     * @param operand the operand value
     * @return the negated value
     * @throws SyntaxErrorException if the operand cannot be negated
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
     * Evaluates unary NOT operator (!).
     * For boolean values: inverts the boolean
     * For numeric values: performs bitwise NOT
     * 
     * @param operand the operand value
     * @return the result of the NOT operation
     * @throws SyntaxErrorException if the operand cannot be processed
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
     * Gets the unary operator.
     *
     * @return the unary operator as BasicTokenType
     */
    public final BasicTokenType getOperator() {
        return _oOperator;
    }

    /**
     * Gets the operand expression.
     *
     * @return the operand expression
     */
    public final Expression getOperand() {
        return _oOperand;
    }

    /**
     * Returns a human-readable representation of the unary expression.
     * Used for debugging and testing.
     *
     * @return string representation of the unary expression
     */
    @Override
    public final String content() {
        String operatorSymbol = getOperatorSymbol(_oOperator);
        return operatorSymbol + "(" + _oOperand.content() + ")";
    }

    /**
     * Returns the structure of the unary expression for compilation.
     *
     * @return structure information for the compiler
     * @throws Exception if there's an error getting the structure
     */
    @Override
    public String structure() throws Exception {
        return "UNARY_OP(" + _oOperator + ", " + _oOperand.structure() + ")";
    }

    /**
     * Converts BasicTokenType to its string symbol representation.
     *
     * @param operator the BasicTokenType operator
     * @return the string symbol for the operator
     */
    private String getOperatorSymbol(BasicTokenType operator) {
        switch (operator) {
            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case NOT:
                return "!";
            default:
                return operator.toString();
        }
    }

    /**
     * Returns a string representation of the unary expression.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "UnaryOperatorExpression{" +
                "operator=" + _oOperator +
                ", operand=" + _oOperand +
                '}';
    }
} 