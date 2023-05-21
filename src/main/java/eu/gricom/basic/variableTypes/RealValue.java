package eu.gricom.basic.variableTypes;

import eu.gricom.basic.helper.EnvParam;

import eu.gricom.basic.error.DivideByZeroException;
import eu.gricom.basic.error.SyntaxErrorException;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * RealValue.java
 *
 * Description:
 *
 * The NumberValue is the container to hold all numeric values.
 *
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class RealValue implements Value {
    private double _fValue;

    // BCD Representation
    private final int _iMaxNumOfDigits = EnvParam.getMAX_BCD_DIGITS(); // max value of float + decimal point
    private char[] _aiBCDValue = new char[_iMaxNumOfDigits];
    private char[] _aiBCDMaxValue = new char[_iMaxNumOfDigits];
    private char[] _aiBCDMinValue = new char[_iMaxNumOfDigits];
    private final double _fMaxReal = Float.MAX_VALUE;
    private final double _fMinReal = Float.MIN_VALUE;

    /**
     * Default constructor.
     *
     * @param fValue Value to be stored in the container
     */
    public RealValue(final double fValue) {

        _fValue = fValue;
    }

    /**
     * Override the standart toString method.
     *
     * @return the content of the variable as a string
     */
    @Override
    public final String toString() {

        return Double.toString(_fValue);
    }

    /**
     * Transform the content of the number value into a double.
     *
     * @return the content of the variable as a double
     */
    public final double toReal() {

        return _fValue;
    }

    /**
     * Transform the content of the number value into a double.
     *
     * @return the content of the variable as a double
     */
    public final int toInt() {

        return (int) _fValue;
    }

    /**
     * Return the value field as an object.
     *
     * @return the number value as an object
     */
    public final Value evaluate() {

        return this;
    }

    @Override
    public final Value equals(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof RealValue) {
            if (this.toReal() == oValue.toReal()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not a number");
    }

    @Override
    public final Value notEqual(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof RealValue) {
            if (this.toReal() != oValue.toReal()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not a number");
    }

    @Override
    public final Value plus(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof RealValue) {
            RealValue oReturn = new RealValue(_fValue + oValue.toReal());
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not a number");
    }

    @Override
    public final Value minus(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof RealValue) {
            RealValue oReturn = new RealValue(_fValue - oValue.toReal());
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not a number");
    }

    @Override
    public final Value multiply(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof RealValue) {
            RealValue oReturn = new RealValue(_fValue * oValue.toReal());
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not a number");
    }

    @Override
    public final Value divide(final Value oValue) throws DivideByZeroException, SyntaxErrorException {
        RealValue oReturn;

        if (oValue instanceof RealValue) {
            if (oValue.toReal() != 0) {
                oReturn = new RealValue(_fValue / oValue.toReal());
            } else {
                throw new DivideByZeroException(this.toReal() + "/" + oValue.toReal() + " is a division by zero");
            }
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not a number");
    }

    @Override
    public final Value modulo(final Value oValue) throws DivideByZeroException, SyntaxErrorException {
        RealValue oReturn;

        if (oValue instanceof RealValue) {
            if (oValue.toReal() != 0) {
                oReturn = new RealValue(_fValue % oValue.toReal());
            } else {
                throw new DivideByZeroException(this.toReal() + "%" + oValue.toReal() + " is a division by zero");
            }
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not a number");
    }

    @Override
    public final Value shiftLeft(final Value oValue) throws SyntaxErrorException {
        throw new SyntaxErrorException(oValue.content() + " '<<' for real type variables is not defined");
    }

    @Override
    public final Value and(final Value oValue) throws DivideByZeroException, SyntaxErrorException {
        if (oValue instanceof RealValue) {

            if (_fValue > 0 && oValue.toReal() > 0) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not of type Real");
    }

    @Override
    public final Value or(final Value oValue) throws DivideByZeroException, SyntaxErrorException {
        if (oValue instanceof RealValue) {

            if (_fValue > 0 || oValue.toReal() > 0) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not of type Real");
    }

    @Override
    public final Value shiftRight(final Value oValue) throws SyntaxErrorException {
        throw new SyntaxErrorException(oValue.content() + " '>>' for real type variables is not defined");
    }

    @Override
    public final Value power(final Value oValue) throws SyntaxErrorException {
        RealValue oReturn;

        if (oValue instanceof RealValue) {
            oReturn = new RealValue(Math.pow(_fValue, oValue.toReal()));

            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not a number");
    }

    @Override
    public final Value smallerThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof RealValue) {
            if (this.toReal() < oValue.toReal()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not a number");
    }

    @Override
    public final Value smallerEqualThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof RealValue) {
            if (this.toReal() <= oValue.toReal()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not a number");
    }

    @Override
    public final Value largerThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof RealValue) {
            if (this.toReal() > oValue.toReal()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not a number");
    }

    @Override
    public final Value largerEqualThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof RealValue) {
            if (this.toReal() >= oValue.toReal()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not a number");
    }

    /**
     * The method round does a mathematical correct rounding of the input dValue,
     * to the number of digits defined by iPlaces.
     *
     * @param dValue number to be rounded
     * @param iPlaces digits behind the decimal point
     * @return rounded number
     */
    public static double round(final double dValue, final int iPlaces) {
        if (iPlaces < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal oBigDecimal = BigDecimal.valueOf(dValue);
        oBigDecimal = oBigDecimal.setScale(iPlaces, RoundingMode.HALF_UP);
        return oBigDecimal.doubleValue();
    }

    /**
     * Content.
     *
     * Method for JUnit to return the content of the statement.
     *
     * @return - gives the name of the statement ("INPUT") and the variable name
     */
    @Override
    public final String content() {

        return String.valueOf(_fValue);
    }

    /**
     * Structure.
     *
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("INPUT") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = this.toString();
        return strReturn;
    }
}
