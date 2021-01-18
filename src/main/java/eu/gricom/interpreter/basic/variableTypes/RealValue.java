package eu.gricom.interpreter.basic.variableTypes;

import eu.gricom.interpreter.basic.error.DivideByZeroException;
import eu.gricom.interpreter.basic.error.SyntaxErrorException;

/**
 * RealValue.java
 * <p>
 * Description:
 * <p>
 * The NumberValue is the container to hold all numeric values.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class RealValue implements Value {
    private final double _fValue;

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

        return (Double.toString(_fValue));
    }

    /**
     * Transform the content of the number value into a double.
     *
     * @return the content of the variable as a double
     */
    public final double toReal() {

        return (_fValue);
    }

    /**
     * Transform the content of the number value into a double.
     *
     * @return the content of the variable as a double
     */
    public final int toInt() {

        return ((int)_fValue);
    }

    /**
     * Return the value field as an object.
     *
     * @return the number value as an object
     */
    public final Value evaluate() {

        return (this);
    }

    @Override
    public final Value equals(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof RealValue) {
            if (this.toReal() == oValue.toReal()) {
                return (new BooleanValue(true));
            }

            return (new BooleanValue(false));
        }

        throw (new SyntaxErrorException(oValue.content() + " is not a number"));
    }

    @Override
    public final Value plus(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof RealValue) {
            RealValue oReturn = new RealValue(_fValue + oValue.toReal());
            return (oReturn);
        }

        throw (new SyntaxErrorException(oValue.content() + " is not a number"));
    }

    @Override
    public final Value minus(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof RealValue) {
            RealValue oReturn = new RealValue(_fValue - oValue.toReal());
            return (oReturn);
        }

        throw (new SyntaxErrorException(oValue.content() + " is not a number"));
    }

    @Override
    public final Value multiply(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof RealValue) {
            RealValue oReturn = new RealValue(_fValue * oValue.toReal());
            return (oReturn);
        }

        throw (new SyntaxErrorException(oValue.content() + " is not a number"));
    }

    @Override
    public final Value divide(final Value oValue) throws DivideByZeroException, SyntaxErrorException {
        RealValue oReturn;

        if (oValue instanceof RealValue) {
            if (oValue.toReal() != 0) {
                oReturn = new RealValue(_fValue / oValue.toReal());
            } else {
                throw (new DivideByZeroException(this.toReal() + "/" + oValue.toReal() + " is a division by zero"));
            }
            return (oReturn);
        }

        throw (new SyntaxErrorException(oValue.content() + " is not a number"));
    }

    @Override
    public Value power(Value oValue) throws SyntaxErrorException {
        RealValue oReturn;

        if (oValue instanceof RealValue) {
            oReturn = new RealValue(Math.pow(_fValue,oValue.toReal()));

            return oReturn;
        }

        throw (new SyntaxErrorException(oValue.content() + " is not a number"));
    }

    @Override
    public final Value smallerThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof RealValue) {
            if (this.toReal() < oValue.toReal()) {
                return (new BooleanValue(true));
            }

            return (new BooleanValue(false));
        }

        throw (new SyntaxErrorException(oValue.content() + " is not a number"));
    }

    @Override
    public final Value largerThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof RealValue) {
            if (this.toReal() > oValue.toReal()) {
                return (new BooleanValue(true));
            }

            return (new BooleanValue(false));
        }

        throw (new SyntaxErrorException(oValue.content() + " is not a number"));
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

        return (String.valueOf(_fValue));
    }
}
