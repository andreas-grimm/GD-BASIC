package eu.gricom.basic.variableTypes;

import eu.gricom.basic.error.DivideByZeroException;
import eu.gricom.basic.error.OverflowException;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.helper.EnvParam;
import eu.gricom.basic.math.BcdMath;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * BcdValue.java
 * <p>
 * Description: The BcdValue class holds a decimal number for variables with the '#' suffix (BCD type). It wraps a
 * java.math.BigDecimal, so no binary rounding error occurs (decision D1 in the design document).
 * <p>
 * Rules (design decisions D6, D7, D8, D10, D11, D13, D14):
 * <ul>
 *   <li>The value has at most max_bcd_digits significant digits (40 by default). Digits after the decimal point are
 *   rounded with HALF_UP to fit.</li>
 *   <li>If the part before the decimal point needs more digits than that limit, an OverflowException is thrown.</li>
 *   <li>The value is stored without trailing zeros, and it is printed in plain decimal notation (no exponent).</li>
 *   <li>INTEGER, LONG and REAL operands convert to BCD. The result of a mix with BCD is BCD.</li>
 *   <li>The bit operators AND and OR work on the integer part of a BCD value, as for INTEGER (D14).
 *       Shifts (&lt;&lt;, &gt;&gt;) are not defined for BCD values (owner decision, 2026-11-22).</li>
 * </ul>
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class BcdValue implements Value, Comparable<BcdValue> {
    private final BigDecimal _oValue;

    /**
     * Creates a BCD value from text, for example "1234.56".
     *
     * @param strValue - the decimal number as text
     * @throws SyntaxErrorException if the text is not a decimal number
     * @throws OverflowException if the integer part needs more digits than max_bcd_digits
     */
    public BcdValue(final String strValue) throws SyntaxErrorException, OverflowException {
        this(parse(strValue));
    }

    /**
     * Creates a BCD value from a BigDecimal.
     *
     * @param oValue - the decimal number
     * @throws OverflowException if the integer part needs more digits than max_bcd_digits
     */
    public BcdValue(final BigDecimal oValue) throws OverflowException {
        _oValue = fit(oValue);
    }

    /**
     * Creates a BCD value from a whole number.
     *
     * @param lValue - the whole number
     * @throws OverflowException if the number needs more digits than max_bcd_digits (not possible for a long)
     */
    public BcdValue(final long lValue) throws OverflowException {
        this(BigDecimal.valueOf(lValue));
    }

    /**
     * Creates a BCD value from a double. The double is taken in its shortest decimal form (for example 0.1 stays 0.1).
     *
     * @param dValue - the number
     * @throws SyntaxErrorException if the number is NaN
     * @throws OverflowException if the number is infinite or its integer part needs more than max_bcd_digits digits
     */
    public BcdValue(final double dValue) throws SyntaxErrorException, OverflowException {
        this(fromDouble(dValue));
    }

    /**
     * Converts any numeric value to a BCD value (decision D11).
     *
     * @param oValue - an INTEGER, LONG, REAL or BCD value
     * @return the BCD value
     * @throws SyntaxErrorException if the value is not a number (TRUE, FALSE or text)
     * @throws OverflowException if the number needs more digits than max_bcd_digits
     */
    public static BcdValue of(final Value oValue) throws SyntaxErrorException, OverflowException {
        if (oValue instanceof BcdValue) {
            return (BcdValue) oValue;
        }
        return new BcdValue(toDecimal(oValue));
    }

    /**
     * Converts any numeric value to a BigDecimal, exactly for INTEGER and LONG.
     *
     * @param oValue - the value
     * @return the exact decimal value
     * @throws SyntaxErrorException if the value is not a number, or a REAL value is NaN or infinite
     */
    static BigDecimal toDecimal(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof BcdValue) {
            return ((BcdValue) oValue)._oValue;
        }
        if (oValue instanceof IntegerValue) {
            return BigDecimal.valueOf(((IntegerValue) oValue).toInt());
        }
        if (oValue instanceof LongValue) {
            return BigDecimal.valueOf(((LongValue) oValue).toLong());
        }
        if (oValue instanceof RealValue) {
            double dValue = oValue.toReal();
            if (Double.isNaN(dValue) || Double.isInfinite(dValue)) {
                throw new SyntaxErrorException("Syntax Error: " + dValue + " cannot be used in a BCD calculation");
            }
            return BigDecimal.valueOf(dValue);
        }
        throw new SyntaxErrorException(oValue.content() + " is not a number");
    }

    /**
     * Converts text to a BigDecimal.
     *
     * @param strValue - the text
     * @return the BigDecimal
     * @throws SyntaxErrorException if the text is not a decimal number
     */
    private static BigDecimal parse(final String strValue) throws SyntaxErrorException {
        try {
            return new BigDecimal(strValue.trim());
        } catch (NumberFormatException oException) {
            throw new SyntaxErrorException("Syntax Error: not a decimal number: " + strValue);
        }
    }

    /**
     * Converts a double to a BigDecimal in its shortest decimal form.
     *
     * @param dValue - the number
     * @return the BigDecimal
     * @throws SyntaxErrorException if the number is NaN
     * @throws OverflowException if the number is infinite
     */
    private static BigDecimal fromDouble(final double dValue) throws SyntaxErrorException, OverflowException {
        if (Double.isNaN(dValue)) {
            throw new SyntaxErrorException("Syntax Error: NaN cannot be a decimal number");
        }
        if (Double.isInfinite(dValue)) {
            throw new OverflowException("Overflow: an infinite value cannot be a decimal number");
        }
        return BigDecimal.valueOf(dValue);
    }

    /**
     * Rounds a value to max_bcd_digits significant digits (HALF_UP), checks the integer part and removes trailing zeros.
     * Rounding keeps the size of the number, so a value that is too large is still too large after rounding.
     *
     * @param oValue - the value to fit
     * @return the value with at most max_bcd_digits significant digits and no trailing zeros
     * @throws OverflowException if the integer part needs more digits than max_bcd_digits
     */
    private static BigDecimal fit(final BigDecimal oValue) throws OverflowException {
        int iMaxDigits = EnvParam.getMaxBcdDigits();
        BigDecimal oRounded = oValue.round(new MathContext(iMaxDigits, RoundingMode.HALF_UP));

        int iIntegerDigits = oRounded.abs().setScale(0, RoundingMode.DOWN).precision();
        if (iIntegerDigits > iMaxDigits) {
            throw new OverflowException("Overflow: a BCD value may have at most " + iMaxDigits
                    + " digits before the decimal point, this value needs " + iIntegerDigits);
        }
        return oRounded.stripTrailingZeros();
    }

    /**
     * Returns the exact decimal value.
     *
     * @return the BigDecimal that holds this value
     */
    public BigDecimal getBigDecimal() {
        return _oValue;
    }

    /**
     * Returns the value as a double (may lose precision; use only for display of approximate results).
     *
     * @return the value as a double
     */
    @Override
    public double toReal() {
        return _oValue.doubleValue();
    }

    /**
     * Returns the value in plain decimal notation without trailing zeros, for example "1234.56" or "100".
     *
     * @return the plain decimal text
     */
    @Override
    public String toString() {
        return _oValue.toPlainString();
    }

    /**
     * Compares two BCD values by number. Uses compareTo, so 2.0 and 2.00 are equal.
     *
     * @param oOther - the other value
     * @return negative, zero or positive, as for Comparable
     */
    @Override
    public int compareTo(final BcdValue oOther) {
        return _oValue.compareTo(oOther._oValue);
    }

    /**
     * Returns this value (used by the expression evaluator).
     *
     * @return this value
     */
    @Override
    public Value evaluate() {
        return this;
    }

    /**
     * Returns the text of the value, used in error messages.
     *
     * @return the plain decimal text
     */
    @Override
    public String content() {
        return toString();
    }

    /**
     * Returns the text of the value, used when the program structure is listed.
     *
     * @return the plain decimal text
     */
    @Override
    public String structure() {
        return toString();
    }

    /**
     * Equality by number. Returns TRUE or FALSE.
     *
     * @param oValue - the other operand (any number)
     * @return a BooleanValue
     * @throws SyntaxErrorException if the other operand is not a number
     */
    @Override
    public Value equals(final Value oValue) throws SyntaxErrorException {
        return new BooleanValue(_oValue.compareTo(toDecimal(oValue)) == 0);
    }

    /**
     * Inequality by number. Returns TRUE or FALSE.
     *
     * @param oValue - the other operand (any number)
     * @return a BooleanValue
     * @throws SyntaxErrorException if the other operand is not a number
     */
    @Override
    public Value notEqual(final Value oValue) throws SyntaxErrorException {
        return new BooleanValue(_oValue.compareTo(toDecimal(oValue)) != 0);
    }

    /**
     * Addition. The result is rounded to max_bcd_digits significant digits.
     *
     * @param oValue - the other operand (any number)
     * @return the sum as a BcdValue
     * @throws SyntaxErrorException if the other operand is not a number
     * @throws OverflowException if the result needs more digits before the decimal point than allowed
     */
    @Override
    public Value plus(final Value oValue) throws SyntaxErrorException, OverflowException {
        return new BcdValue(_oValue.add(toDecimal(oValue)));
    }

    /**
     * Subtraction. The result is rounded to max_bcd_digits significant digits.
     *
     * @param oValue - the other operand (any number)
     * @return the difference as a BcdValue
     * @throws SyntaxErrorException if the other operand is not a number
     * @throws OverflowException if the result needs more digits before the decimal point than allowed
     */
    @Override
    public Value minus(final Value oValue) throws SyntaxErrorException, OverflowException {
        return new BcdValue(_oValue.subtract(toDecimal(oValue)));
    }

    /**
     * Multiplication. The result is rounded to max_bcd_digits significant digits.
     *
     * @param oValue - the other operand (any number)
     * @return the product as a BcdValue
     * @throws SyntaxErrorException if the other operand is not a number
     * @throws OverflowException if the result needs more digits before the decimal point than allowed
     */
    @Override
    public Value multiply(final Value oValue) throws SyntaxErrorException, OverflowException {
        return new BcdValue(_oValue.multiply(toDecimal(oValue)));
    }

    /**
     * Division. The quotient is rounded to max_bcd_digits significant digits (HALF_UP).
     *
     * @param oValue - the divisor (any number)
     * @return the quotient as a BcdValue
     * @throws DivideByZeroException if the divisor is zero (decision D13)
     * @throws SyntaxErrorException if the divisor is not a number
     * @throws OverflowException if the result needs more digits before the decimal point than allowed
     */
    @Override
    public Value divide(final Value oValue) throws DivideByZeroException, SyntaxErrorException, OverflowException {
        BigDecimal oDivisor = toDecimal(oValue);
        if (oDivisor.signum() == 0) {
            throw new DivideByZeroException(this + "/" + oDivisor + " is a division by zero");
        }
        return new BcdValue(_oValue.divide(oDivisor, new MathContext(EnvParam.getMaxBcdDigits(),
                RoundingMode.HALF_UP)));
    }

    /**
     * Remainder after division. The sign follows the dividend, as for REAL values.
     *
     * @param oValue - the divisor (any number)
     * @return the remainder as a BcdValue
     * @throws DivideByZeroException if the divisor is zero
     * @throws SyntaxErrorException if the divisor is not a number
     * @throws OverflowException if the result needs more digits than allowed (not possible for a remainder)
     */
    @Override
    public Value modulo(final Value oValue) throws DivideByZeroException, SyntaxErrorException, OverflowException {
        BigDecimal oDivisor = toDecimal(oValue);
        if (oDivisor.signum() == 0) {
            throw new DivideByZeroException(this + "%" + oDivisor + " is a division by zero");
        }
        return new BcdValue(_oValue.remainder(oDivisor));
    }



    /**
     * Bitwise AND for BCD values. Works on the integer part, as for INTEGER (decision D14).
     *
     * @param oValue - the other operand
     * @return the result of the INTEGER operation
     * @throws DivideByZeroException as for INTEGER
     * @throws SyntaxErrorException if the other operand is not a number
     */
    @Override
    public Value and(final Value oValue) throws DivideByZeroException, SyntaxErrorException {
        return asInteger().and(asIntegerOperand(oValue));
    }

    /**
     * Bitwise OR for BCD values. Works on the integer part, as for INTEGER (decision D14).
     *
     * @param oValue - the other operand
     * @return the result of the INTEGER operation
     * @throws DivideByZeroException as for INTEGER
     * @throws SyntaxErrorException if the other operand is not a number
     */
    @Override
    public Value or(final Value oValue) throws DivideByZeroException, SyntaxErrorException {
        return asInteger().or(asIntegerOperand(oValue));
    }

    /**
     * Power. An integer exponent between -999 and 999 gives an exact result (rounded to max_bcd_digits). Any other
     * exponent uses BcdMath.power (exp(y * ln x)), calculated with 60 digits and rounded to max_bcd_digits (decision D12).
     *
     * @param oValue - the exponent (any number)
     * @return the power as a BcdValue
     * @throws DivideByZeroException if the base is zero and the exponent is negative
     * @throws SyntaxErrorException if the exponent is not a number, or the result is not a real number
     * @throws OverflowException if the result needs more digits before the decimal point than allowed
     */
    @Override
    public Value power(final Value oValue) throws DivideByZeroException, SyntaxErrorException, OverflowException {
        BigDecimal oExponent = toDecimal(oValue);
        MathContext oContext = new MathContext(EnvParam.getMaxBcdDigits(), RoundingMode.HALF_UP);

        boolean bIsWhole = oExponent.signum() == 0 || oExponent.stripTrailingZeros().scale() <= 0;
        if (bIsWhole && oExponent.abs().compareTo(BigDecimal.valueOf(999)) <= 0) {
            int iExponent = oExponent.intValue();
            if (iExponent >= 0) {
                return new BcdValue(_oValue.pow(iExponent, oContext));
            }
            if (_oValue.signum() == 0) {
                throw new DivideByZeroException(this + " to a negative power is a division by zero");
            }
            return new BcdValue(BigDecimal.ONE.divide(_oValue.pow(-iExponent, oContext), oContext));
        }

        return new BcdValue(BcdMath.power(_oValue, oExponent));
    }

    /**
     * Less than, by number.
     *
     * @param oValue - the other operand
     * @return a BooleanValue
     * @throws SyntaxErrorException if the other operand is not a number
     */
    @Override
    public Value smallerThan(final Value oValue) throws SyntaxErrorException {
        return new BooleanValue(_oValue.compareTo(toDecimal(oValue)) < 0);
    }

    /**
     * Less than or equal, by number.
     *
     * @param oValue - the other operand
     * @return a BooleanValue
     * @throws SyntaxErrorException if the other operand is not a number
     */
    @Override
    public Value smallerEqualThan(final Value oValue) throws SyntaxErrorException {
        return new BooleanValue(_oValue.compareTo(toDecimal(oValue)) <= 0);
    }

    /**
     * Greater than, by number.
     *
     * @param oValue - the other operand
     * @return a BooleanValue
     * @throws SyntaxErrorException if the other operand is not a number
     */
    @Override
    public Value largerThan(final Value oValue) throws SyntaxErrorException {
        return new BooleanValue(_oValue.compareTo(toDecimal(oValue)) > 0);
    }

    /**
     * Greater than or equal, by number.
     *
     * @param oValue - the other operand
     * @return a BooleanValue
     * @throws SyntaxErrorException if the other operand is not a number
     */
    @Override
    public Value largerEqualThan(final Value oValue) throws SyntaxErrorException {
        return new BooleanValue(_oValue.compareTo(toDecimal(oValue)) >= 0);
    }

    /**
     * The integer part of this value, as an INTEGER value (decision D14).
     *
     * @return an IntegerValue with the whole part (fraction dropped)
     */
    private IntegerValue asInteger() {
        return new IntegerValue(_oValue.intValue());
    }

    /**
     * The other operand of a bit operator, as an INTEGER value (decision D14).
     *
     * @param oValue - the operand
     * @return the operand as an IntegerValue
     * @throws SyntaxErrorException if the operand is not a number
     */
    private static IntegerValue asIntegerOperand(final Value oValue) throws SyntaxErrorException {
        return new IntegerValue(toDecimal(oValue).intValue());
    }
}
