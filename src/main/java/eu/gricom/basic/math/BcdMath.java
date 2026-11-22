package eu.gricom.basic.math;

import eu.gricom.basic.error.DivideByZeroException;
import eu.gricom.basic.error.OverflowException;
import eu.gricom.basic.error.SyntaxErrorException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * BcdMath.java
 * <p>
 * Description: Maths functions for BCD (decimal, suffix '#') values, decision D12 in the design document. All functions
 * calculate with WORK_DIGITS (60) significant digits and return the result unrounded at that precision. The caller
 * (BcdValue) rounds the result to max_bcd_digits (40) with HALF_UP.
 * <p>
 * Methods used:
 * <ul>
 *   <li>EXP: e^n times a Taylor series for the remainder, where n is the nearest whole number to x.</li>
 *   <li>LN: Halley iteration on exp(y) = x, starting from the REAL logarithm.</li>
 *   <li>LOG10: LN divided by LN(10).</li>
 *   <li>SQR: BigDecimal.sqrt.</li>
 *   <li>SIN, COS, TAN: argument reduced modulo 2 * PI, then Taylor series. PI uses Machin's formula.</li>
 *   <li>ATN: reduction with atan(x) = 2 * atan(x / (1 + sqrt(1 + x*x))), then a Taylor series.</li>
 *   <li>Non-integer POWER: exp(y * ln(x)).</li>
 * </ul>
 * Accuracy limit: SIN and COS lose digits for arguments above about 1E20, because the reduction uses 60 digits.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class BcdMath {
    /** Significant digits used inside the calculations. */
    public static final int WORK_DIGITS = 60;

    private static final MathContext MC = new MathContext(WORK_DIGITS, RoundingMode.HALF_UP);
    /** Terms smaller than this are dropped from the series. */
    private static final BigDecimal EPS = BigDecimal.ONE.movePointLeft(WORK_DIGITS + 5);
    /** Newton / Halley steps smaller than this stop the iteration. */
    private static final BigDecimal STOP = BigDecimal.ONE.movePointLeft(WORK_DIGITS - 5);
    private static final BigDecimal TWO = BigDecimal.valueOf(2);
    /** e^1000 has more than 40 digits before the decimal point, so larger exponents overflow. */
    private static final int MAX_EXP = 1000;
    private static final BigDecimal PI = computePi();
    private static final BigDecimal E = computeE();

    private BcdMath() {
    }

    /**
     * The constant PI with WORK_DIGITS digits.
     *
     * @return PI
     */
    public static BigDecimal pi() {
        return PI;
    }

    /**
     * Exponential function e^x.
     *
     * @param x the exponent
     * @return e^x
     * @throws OverflowException if e^x needs more than max_bcd_digits digits before the decimal point (x above 1000)
     */
    public static BigDecimal exp(final BigDecimal x) throws OverflowException {
        BigDecimal oWhole = x.setScale(0, RoundingMode.HALF_UP);
        if (oWhole.compareTo(BigDecimal.valueOf(MAX_EXP)) > 0) {
            throw new OverflowException("Overflow: EXP(" + x.toPlainString() + ") is too large for a BCD value");
        }
        if (oWhole.compareTo(BigDecimal.valueOf(-MAX_EXP)) < 0) {
            // below 1E-434: such a small value is shown as zero
            return BigDecimal.ZERO;
        }

        BigDecimal oRemainder = x.subtract(oWhole, MC);
        BigDecimal oSum = BigDecimal.ONE;
        BigDecimal oTerm = BigDecimal.ONE;
        for (int k = 1;; k++) {
            oTerm = oTerm.multiply(oRemainder, MC).divide(BigDecimal.valueOf(k), MC);
            if (oTerm.abs().compareTo(EPS) < 0) {
                break;
            }
            oSum = oSum.add(oTerm, MC);
        }

        int iWhole = oWhole.intValue();
        BigDecimal oWholePower = iWhole >= 0
                ? E.pow(iWhole, MC)
                : BigDecimal.ONE.divide(E.pow(-iWhole, MC), MC);
        return oSum.multiply(oWholePower, MC);
    }

    /**
     * Natural logarithm.
     *
     * @param x the value, must be greater than 0
     * @return ln(x)
     * @throws SyntaxErrorException if x is zero or negative
     * @throws OverflowException if an intermediate result is too large (not possible for values of 40 digits)
     */
    public static BigDecimal ln(final BigDecimal x) throws SyntaxErrorException, OverflowException {
        if (x.signum() <= 0) {
            throw new SyntaxErrorException("Syntax Error: LOG needs a value greater than 0, not " + x.toPlainString());
        }
        BigDecimal oY = BigDecimal.valueOf(Math.log(x.doubleValue()));
        for (int i = 0; i < 10; i++) {
            BigDecimal oExpY = exp(oY);
            BigDecimal oStep = x.subtract(oExpY, MC).multiply(TWO, MC).divide(x.add(oExpY, MC), MC);
            oY = oY.add(oStep, MC);
            if (oStep.abs().compareTo(STOP) < 0) {
                break;
            }
        }
        return oY;
    }

    /**
     * Base-10 logarithm.
     *
     * @param x the value, must be greater than 0
     * @return log10(x)
     * @throws SyntaxErrorException if x is zero or negative
     * @throws OverflowException as for ln
     */
    public static BigDecimal log10(final BigDecimal x) throws SyntaxErrorException, OverflowException {
        return ln(x).divide(ln(BigDecimal.TEN), MC);
    }

    /**
     * Square root.
     *
     * @param x the value, must not be negative
     * @return the square root of x
     * @throws SyntaxErrorException if x is negative
     */
    public static BigDecimal sqrt(final BigDecimal x) throws SyntaxErrorException {
        if (x.signum() < 0) {
            throw new SyntaxErrorException("Syntax Error: SQR of a negative value has no real result: "
                    + x.toPlainString());
        }
        return x.sqrt(MC);
    }

    /**
     * Sine of an angle in radians.
     *
     * @param x the angle in radians
     * @return sin(x)
     */
    public static BigDecimal sin(final BigDecimal x) {
        return sinSeries(reduce(x));
    }

    /**
     * Cosine of an angle in radians.
     *
     * @param x the angle in radians
     * @return cos(x)
     */
    public static BigDecimal cos(final BigDecimal x) {
        return cosSeries(reduce(x));
    }

    /**
     * Tangent of an angle in radians.
     *
     * @param x the angle in radians
     * @return tan(x)
     * @throws DivideByZeroException if the cosine is exactly zero (no finite tangent)
     */
    public static BigDecimal tan(final BigDecimal x) throws DivideByZeroException {
        BigDecimal oReduced = reduce(x);
        BigDecimal oCosine = cosSeries(oReduced);
        if (oCosine.signum() == 0) {
            throw new DivideByZeroException("TAN(" + x.toPlainString() + ") is a division by zero");
        }
        return sinSeries(oReduced).divide(oCosine, MC);
    }

    /**
     * Arc tangent, result in radians.
     *
     * @param x the value
     * @return atan(x), between -PI/2 and PI/2
     */
    public static BigDecimal atan(final BigDecimal x) {
        if (x.signum() < 0) {
            return atan(x.negate()).negate();
        }
        if (x.compareTo(BigDecimal.ONE) > 0) {
            return PI.divide(TWO, MC).subtract(atan(BigDecimal.ONE.divide(x, MC)), MC);
        }

        // atan(x) = 2 * atan(x / (1 + sqrt(1 + x*x))), repeated until x is small
        BigDecimal oWork = x;
        int iHalvings = 0;
        BigDecimal oSmall = new BigDecimal("0.1");
        while (oWork.compareTo(oSmall) > 0) {
            BigDecimal oDenominator = BigDecimal.ONE.add(BigDecimal.ONE.add(oWork.multiply(oWork, MC)).sqrt(MC), MC);
            oWork = oWork.divide(oDenominator, MC);
            iHalvings++;
        }

        // Taylor series: x - x^3/3 + x^5/5 - ...
        BigDecimal oX2 = oWork.multiply(oWork, MC);
        BigDecimal oPower = oWork;
        BigDecimal oSum = oWork;
        boolean bSubtract = true;
        for (int k = 1;; k++) {
            oPower = oPower.multiply(oX2, MC);
            BigDecimal oTerm = oPower.divide(BigDecimal.valueOf(2L * k + 1), MC);
            if (oTerm.abs().compareTo(EPS) < 0) {
                break;
            }
            oSum = bSubtract ? oSum.subtract(oTerm, MC) : oSum.add(oTerm, MC);
            bSubtract = !bSubtract;
        }
        return oSum.multiply(BigDecimal.valueOf(1L << iHalvings), MC);
    }

    /**
     * Power with a non-integer exponent: x^y = exp(y * ln(x)). Integer exponents are handled by BigDecimal.pow in
     * BcdValue, which is exact.
     *
     * @param x the base
     * @param y the exponent
     * @return x^y
     * @throws DivideByZeroException if x is zero and y is negative
     * @throws SyntaxErrorException if x is negative (a fractional power of a negative number has no real result)
     * @throws OverflowException if the result is too large
     */
    public static BigDecimal power(final BigDecimal x, final BigDecimal y)
            throws DivideByZeroException, SyntaxErrorException, OverflowException {
        if (x.signum() == 0) {
            if (y.signum() < 0) {
                throw new DivideByZeroException("0 to the power " + y.toPlainString() + " is a division by zero");
            }
            return BigDecimal.ZERO;
        }
        if (x.signum() < 0) {
            throw new SyntaxErrorException("Syntax Error: a negative value to a fractional power has no real result: "
                    + x.toPlainString() + " ^ " + y.toPlainString());
        }
        return exp(y.multiply(ln(x), MC));
    }

    /**
     * Reduces an angle to the range -PI to PI.
     *
     * @param x the angle in radians
     * @return the equivalent angle in the range -PI to PI
     */
    private static BigDecimal reduce(final BigDecimal x) {
        BigDecimal oTwoPi = PI.multiply(TWO, MC);
        BigDecimal oTurns = x.divide(oTwoPi, MC).setScale(0, RoundingMode.HALF_UP);
        return x.subtract(oTwoPi.multiply(oTurns, MC), MC);
    }

    /**
     * Taylor series for sine, valid for angles between -PI and PI.
     *
     * @param r the angle
     * @return sin(r)
     */
    private static BigDecimal sinSeries(final BigDecimal r) {
        BigDecimal oR2 = r.multiply(r, MC);
        BigDecimal oTerm = r;
        BigDecimal oSum = r;
        for (long k = 1;; k++) {
            oTerm = oTerm.multiply(oR2, MC).divide(BigDecimal.valueOf((2 * k) * (2 * k + 1)), MC).negate();
            if (oTerm.abs().compareTo(EPS) < 0) {
                break;
            }
            oSum = oSum.add(oTerm, MC);
        }
        return oSum;
    }

    /**
     * Taylor series for cosine, valid for angles between -PI and PI.
     *
     * @param r the angle
     * @return cos(r)
     */
    private static BigDecimal cosSeries(final BigDecimal r) {
        BigDecimal oR2 = r.multiply(r, MC);
        BigDecimal oTerm = BigDecimal.ONE;
        BigDecimal oSum = BigDecimal.ONE;
        for (long k = 1;; k++) {
            oTerm = oTerm.multiply(oR2, MC).divide(BigDecimal.valueOf((2 * k - 1) * (2 * k)), MC).negate();
            if (oTerm.abs().compareTo(EPS) < 0) {
                break;
            }
            oSum = oSum.add(oTerm, MC);
        }
        return oSum;
    }

    /**
     * arctan(1 / n) by its Taylor series. Used for Machin's formula.
     *
     * @param n the whole number n, greater than 1
     * @return atan(1 / n)
     */
    private static BigDecimal atanInverse(final int n) {
        BigDecimal oX = BigDecimal.ONE.divide(BigDecimal.valueOf(n), MC);
        BigDecimal oX2 = oX.multiply(oX, MC);
        BigDecimal oPower = oX;
        BigDecimal oSum = oX;
        boolean bSubtract = true;
        for (int k = 1;; k++) {
            oPower = oPower.multiply(oX2, MC);
            BigDecimal oTerm = oPower.divide(BigDecimal.valueOf(2L * k + 1), MC);
            if (oTerm.abs().compareTo(EPS) < 0) {
                break;
            }
            oSum = bSubtract ? oSum.subtract(oTerm, MC) : oSum.add(oTerm, MC);
            bSubtract = !bSubtract;
        }
        return oSum;
    }

    /**
     * PI by Machin's formula: PI = 16 * atan(1/5) - 4 * atan(1/239).
     *
     * @return PI with WORK_DIGITS digits
     */
    private static BigDecimal computePi() {
        return atanInverse(5).multiply(BigDecimal.valueOf(16), MC)
                .subtract(atanInverse(239).multiply(BigDecimal.valueOf(4), MC), MC);
    }

    /**
     * e by its series 1 + 1/1! + 1/2! + ...
     *
     * @return e with WORK_DIGITS digits
     */
    private static BigDecimal computeE() {
        BigDecimal oSum = BigDecimal.ONE;
        BigDecimal oTerm = BigDecimal.ONE;
        for (int k = 1;; k++) {
            oTerm = oTerm.divide(BigDecimal.valueOf(k), MC);
            if (oTerm.abs().compareTo(EPS) < 0) {
                break;
            }
            oSum = oSum.add(oTerm, MC);
        }
        return oSum;
    }
}
