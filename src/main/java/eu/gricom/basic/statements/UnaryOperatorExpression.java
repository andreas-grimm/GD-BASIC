package eu.gricom.basic.statements;

import eu.gricom.basic.error.OverflowException;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.tokenizer.BasicTokenType;
import eu.gricom.basic.variabletypes.Value;
import eu.gricom.basic.variabletypes.RealValue;
import eu.gricom.basic.variabletypes.IntegerValue;
import eu.gricom.basic.variabletypes.BooleanValue;
import eu.gricom.basic.variabletypes.LongValue;
import eu.gricom.basic.variabletypes.StringValue;

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
     * Constructor for unary operator expressions.
     *
     * @param oOperator the unary operator (PLUS, MINUS, or NOT)
     * @param oOperand the operand to apply the operator to
     */
    public UnaryOperatorExpression(final BasicTokenType eOperator, final Expression oOperand) {
        _oLogger.debug("--->  " + eOperator + " " + oOperand.content());
        _oOperator = eOperator;
        _oOperand = oOperand;
        
        // Validate that the operator is a valid unary operator
        if (eOperator != BasicTokenType.PLUS
            && eOperator != BasicTokenType.MINUS
            && eOperator != BasicTokenType.NOT) {
            throw new IllegalArgumentException("Invalid unary operator: " + eOperator);
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
        Value oOperandValue = _oOperand.evaluate();
        
        _oLogger.debug("Evaluating unary operator: " + _oOperator + " on operand: " + oOperandValue);

        return switch (_oOperator) {
            case PLUS -> evaluateUnaryPlus(oOperandValue);
            case MINUS -> evaluateUnaryMinus(oOperandValue);
            case NOT -> evaluateUnaryNot(oOperandValue);
            default -> throw new SyntaxErrorException("Unknown unary operator: " + _oOperator);
        };
    }

    /**
     * Evaluates unary plus operator (+).
     * Unary plus typically has no effect but validates the operand.
     * 
     * @param operand the operand value
     * @return the operand value unchanged
     * @throws SyntaxErrorException if the operand is null
     */
    private Value evaluateUnaryPlus(Value oOperand) throws SyntaxErrorException {
        // Unary plus is mostly a no-op, but we validate the operand
        if (oOperand == null) {
            throw new SyntaxErrorException("Cannot apply unary plus to null value");
        }
        return oOperand;
    }

    /**
     * Evaluates unary minus operator (-).
     * Negates the operand value.
     * 
     * @param operand the operand value
     * @return the negated value
     * @throws SyntaxErrorException if the operand cannot be negated
     */
    /**
     * Unary minus: -A gives the value of A times -1. The type is kept: an integer stays an integer, a decimal (BCD)
     * stays a decimal. A boolean swaps TRUE and FALSE. A string is refused with an error.
     *
     * @param operand the value after the minus sign
     * @return the negated value
     * @throws SyntaxErrorException if the operand is missing or is a string
     */
    public static Value evaluateUnaryMinus(final Value oOperand) throws SyntaxErrorException {
        if (oOperand == null) {
            throw new SyntaxErrorException("Cannot apply unary minus to null value");
        }
        if (oOperand instanceof StringValue) {
            throw new SyntaxErrorException("Cannot apply unary minus to a string: " + oOperand);
        }

        try {
            if (oOperand instanceof BooleanValue) {
                // -TRUE is FALSE and -FALSE is TRUE: a boolean is true when its value is not zero
                return new BooleanValue(oOperand.toReal() == 0.0);
            }
            // value times -1, using a -1 of the same family as the operand (a long cannot be multiplied by an integer)
            Value oMinusOne = oOperand instanceof LongValue ? new LongValue(-1L) : new IntegerValue(-1);
            return oOperand.multiply(oMinusOne);
        } catch (NumberFormatException oException) {
            throw new SyntaxErrorException("Cannot apply unary minus to non-numeric value: " + oOperand);
        } catch (OverflowException oException) {
            throw new SyntaxErrorException("Overflow while applying unary minus to: " + oOperand);
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
    public static Value evaluateUnaryNot(final Value oOperand) throws SyntaxErrorException {
        if (oOperand == null) {
            throw new SyntaxErrorException("Cannot apply unary NOT to null value");
        }
        
        try {
            // Try to convert to boolean first
            if (oOperand instanceof BooleanValue) {
                boolean bValue = oOperand.toReal() != 0.0;
                return new BooleanValue(!bValue);
            }
            
            // For numeric values, perform bitwise NOT
            double fNumericValue = oOperand.toReal();
            long lValue = (long) fNumericValue;
            long lNotValue = ~lValue;
            
            // Return as integer if it fits, otherwise as real
            if (lNotValue >= Integer.MIN_VALUE && lNotValue <= Integer.MAX_VALUE) {
                return new IntegerValue((int) lNotValue);
            } else {
                return new RealValue((double) lNotValue);
            }
            
        } catch (NumberFormatException oException) {
            throw new SyntaxErrorException("Cannot apply unary NOT to non-numeric value: " + oOperand);
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
        String strOperatorSymbol = getOperatorSymbol(_oOperator);
        return strOperatorSymbol + "(" + _oOperand.content() + ")";
    }

    /**
     * Returns the structure of the unary expression for compilation.
     *
     * @return structure information for the compiler
     * @throws Exception if there's an error getting the structure
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"UNARY_OP\": {";
        strReturn += "\"OPERATOR\": \"" + _oOperator.toString() + "\",";
        strReturn += "\"OPERAND\": \"" + _oOperand.structure() + "\"";
        strReturn += "}}";
        return strReturn;
    }

    /**
     * Converts BasicTokenType to its string symbol representation.
     *
     * @param operator the BasicTokenType operator
     * @return the string symbol for the operator
     */
    private String getOperatorSymbol(BasicTokenType eOperator) {
        return switch (eOperator) {
            case PLUS -> "+";
            case MINUS -> "-";
            case NOT -> "!";
            default -> eOperator.toString();
        };
    }

    /**
     * Returns a string representation of the unary expression.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "UnaryOperatorExpression{"
                + "operator=" + _oOperator
                + ", operand=" + _oOperand
                + '}';
    }

    /** @return the operator token type (used by the compiler) */
    public BasicTokenType getTokenType() {
        return _oOperator;
    }
}
 
