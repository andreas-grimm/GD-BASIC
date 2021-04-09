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
public class IntegerValue implements Value {
    private final int _iValue;

    /**
     * Default constructor.
     *
     * @param iValue Value to be stored in the container
     */
    public IntegerValue(final int iValue) {

        _iValue = iValue;
    }

    /**
     * Default constructor.
     *
     * @param fValue Value to be stored in the container
     */
    public IntegerValue(final float fValue) {

        _iValue = (int) fValue;
    }

    /**
     * Override the standard toString method.
     *
     * @return the content of the variable as a string
     */
    @Override
    public final String toString() {

        return Integer.toString(_iValue);
    }

    /**
     * Transform the content of the number value into a double.
     *
     * @return the content of the variable as a double
     */
    public final double toReal() {

        return _iValue;
    }

    /**
     * Transform the content of the number value into a double.
     *
     * @return the content of the variable as a double
     */
    public final int toInt() {

        return _iValue;
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
        if (oValue instanceof IntegerValue) {
            if (this.toInt() == ((IntegerValue) oValue).toInt()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
    }

    @Override
    public final Value notEqual(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof IntegerValue) {
            if (this.toInt() != ((IntegerValue) oValue).toInt()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
    }

    @Override
    public final Value plus(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof IntegerValue) {
            IntegerValue oReturn = new IntegerValue(_iValue + ((IntegerValue) oValue).toInt());
            return oReturn;
        } else if (oValue instanceof RealValue) {
            IntegerValue oReturn = new IntegerValue(_iValue + ((RealValue) oValue).toInt());
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
    }

    @Override
    public final Value minus(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof IntegerValue) {
            IntegerValue oReturn = new IntegerValue(_iValue - ((IntegerValue) oValue).toInt());
            return oReturn;
        } else if (oValue instanceof RealValue) {
            IntegerValue oReturn = new IntegerValue(_iValue - ((RealValue) oValue).toInt());
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
    }

    @Override
    public final Value multiply(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof IntegerValue) {
            IntegerValue oReturn = new IntegerValue(_iValue * ((IntegerValue) oValue).toInt());
            return oReturn;
        } else if (oValue instanceof RealValue) {
            IntegerValue oReturn = new IntegerValue(_iValue * ((RealValue) oValue).toInt());
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
    }

    @Override
    public final Value divide(final Value oValue) throws DivideByZeroException, SyntaxErrorException {
        IntegerValue oReturn;

        if (oValue instanceof IntegerValue) {
            if (((IntegerValue) oValue).toInt() != 0) {
                oReturn = new IntegerValue(_iValue / ((IntegerValue) oValue).toInt());
            } else {
                throw new DivideByZeroException(this.toInt() + "/" + ((IntegerValue) oValue).toInt() + " is a " + "division by zero");
            }
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
    }

    @Override
    public final Value shiftLeft(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof IntegerValue) {
            IntegerValue oReturn = new IntegerValue(_iValue * 2 * ((IntegerValue) oValue).toInt());
            return oReturn;
        } else if (oValue instanceof RealValue) {
            IntegerValue oReturn = new IntegerValue(_iValue * 2 * ((RealValue) oValue).toInt());
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
    }

    @Override
    public final Value and(final Value oValue) throws DivideByZeroException, SyntaxErrorException {
        if (oValue instanceof IntegerValue) {

            if (_iValue > 0 && ((IntegerValue) oValue).toInt() > 0) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not of type integer");
    }

    @Override
    public final Value or(final Value oValue) throws DivideByZeroException, SyntaxErrorException {
        if (oValue instanceof IntegerValue) {

            if (_iValue > 0 || ((IntegerValue) oValue).toInt() > 0) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not of type integer");
    }

    @Override
    public final Value shiftRight(final Value oValue) throws DivideByZeroException, SyntaxErrorException {
        IntegerValue oReturn;

        if (oValue instanceof IntegerValue) {
            if (((IntegerValue) oValue).toInt() != 0) {
                oReturn = new IntegerValue(_iValue / (2 * ((IntegerValue) oValue).toInt()));
            } else {
                throw new DivideByZeroException(this.toInt() + ">>" + ((IntegerValue) oValue).toInt()
                                                        + " is a division by zero");
            }
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
    }

    @Override
    public final Value power(final Value oValue) throws SyntaxErrorException {
        IntegerValue oReturn;

        if (oValue instanceof IntegerValue) {
            oReturn = new IntegerValue(new RealValue(Math.pow(_iValue, ((IntegerValue) oValue).toInt())).toInt());

            return oReturn;
        } else if (oValue instanceof RealValue) {
            oReturn = new IntegerValue(new RealValue(Math.pow(_iValue, ((RealValue) oValue).toInt())).toInt());
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not a integer");
    }

    @Override
    public final Value smallerThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof IntegerValue) {
            if (this.toInt() < ((IntegerValue) oValue).toInt()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        } else if (oValue instanceof RealValue) {
            if (this.toInt() < ((RealValue) oValue).toInt()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
    }

    @Override
    public final Value smallerEqualThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof IntegerValue) {
            if (this.toInt() <= ((IntegerValue) oValue).toInt()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        } else if (oValue instanceof RealValue) {
            if (this.toInt() <= ((RealValue) oValue).toInt()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
    }

    @Override
    public final Value largerThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof IntegerValue) {
            if (this.toInt() > ((IntegerValue) oValue).toInt()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        } else if (oValue instanceof RealValue) {
            if (this.toInt() > ((RealValue) oValue).toInt()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
    }

    @Override
    public final Value largerEqualThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof IntegerValue) {
            if (this.toInt() >= ((IntegerValue) oValue).toInt()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        } else if (oValue instanceof RealValue) {
            if (this.toInt() >= ((RealValue) oValue).toInt()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
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

        return String.valueOf(_iValue);
    }
}
