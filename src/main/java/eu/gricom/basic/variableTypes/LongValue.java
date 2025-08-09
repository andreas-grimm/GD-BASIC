package eu.gricom.basic.variableTypes;

import eu.gricom.basic.error.DivideByZeroException;
import eu.gricom.basic.error.SyntaxErrorException;

/**
 * LongValue.java
 * <p>
 * Description:
 * <p>
 * The NumberValue is the container to hold all numeric values.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class LongValue implements Value {
    private final long _lValue;

    /**
     * Default constructor.
     *
     * @param lValue Value to be stored in the container
     */
    public LongValue(final long lValue) {

        _lValue = lValue;
    }

    /**
     * Override the standart toString method.
     *
     * @return the content of the variable as a string
     */
    @Override
    public final String toString() {

        return Long.toString(_lValue);
    }

    /**
     * Transform the content of the number value into a double.
     *
     * @return the content of the variable as a double
     */
    public final double toReal() {

        return _lValue;
    }

    /**
     * Transform the content of the number value into a long.
     *
     * @return the content of the variable as a long
     */
    public final Long toLong() {

        return _lValue;
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
        if (oValue instanceof LongValue) {
            if (this.toLong() == ((LongValue) oValue).toLong()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not an Long");
    }

    @Override
    public final Value notEqual(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof LongValue) {
            if (this.toLong() != ((LongValue) oValue).toLong()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not an Long");
    }

    @Override
    public final Value plus(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof LongValue) {
            LongValue oReturn = new LongValue(_lValue + ((LongValue) oValue).toLong());
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not an Long");
    }

    @Override
    public final Value minus(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof LongValue) {
            LongValue oReturn = new LongValue(_lValue - ((LongValue) oValue).toLong());
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not an Long");
    }

    @Override
    public final Value multiply(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof LongValue) {
            LongValue oReturn = new LongValue(_lValue * ((LongValue) oValue).toLong());
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not an Long");
    }

    @Override
    public final Value divide(final Value oValue) throws DivideByZeroException, SyntaxErrorException {
        LongValue oReturn;

        if (oValue instanceof LongValue) {
            if (((LongValue) oValue).toLong() != 0) {
                oReturn = new LongValue(_lValue / ((LongValue) oValue).toLong());
            } else {
                throw new DivideByZeroException(this.toLong() + "/" + ((LongValue) oValue).toLong() + " is a "
                        + "division by zero");
            }
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not an Long");
    }

    @Override
    public final Value modulo(final Value oValue) throws DivideByZeroException, SyntaxErrorException {
        LongValue oReturn;

        if (oValue instanceof LongValue) {
            if (((LongValue) oValue).toLong() != 0) {
                oReturn = new LongValue(_lValue % ((LongValue) oValue).toLong());
            } else {
                throw new DivideByZeroException(this.toLong() + "%" + ((LongValue) oValue).toLong() + " is a "
                                                        + "division by zero");
            }
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not an Long");
    }

    @Override
    public final Value shiftLeft(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof LongValue) {
            LongValue oReturn = new LongValue(_lValue * 2 * ((LongValue) oValue).toLong());
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not an Long");
    }

    @Override
    public final Value and(final Value oValue) throws DivideByZeroException, SyntaxErrorException {
        if (oValue instanceof LongValue) {

            if (_lValue > 0 && ((LongValue) oValue).toLong() > 0) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not of type Long");
    }

    @Override
    public final Value or(final Value oValue) throws DivideByZeroException, SyntaxErrorException {
        if (oValue instanceof LongValue) {

            if (_lValue > 0 || ((LongValue) oValue).toLong() > 0) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not of type Long");
    }

    @Override
    public final Value shiftRight(final Value oValue) throws DivideByZeroException, SyntaxErrorException {
        LongValue oReturn;

        if (oValue instanceof LongValue) {
            if (((LongValue) oValue).toLong() != 0) {
                oReturn = new LongValue(_lValue / (2 * ((LongValue) oValue).toLong()));
            } else {
                throw new DivideByZeroException(this.toLong()
                    + ">>" + ((LongValue) oValue).toLong() + " is a division by zero");
            }
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not an Long");
    }

    @Override
    public final Value power(final Value oValue) throws SyntaxErrorException {
        LongValue oReturn;

        if (oValue instanceof LongValue) {
            oReturn = new LongValue(new RealValue(Math.pow(_lValue, oValue.toReal())).toInt());

            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not a Long");
    }

    @Override
    public final Value smallerThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof LongValue) {
            if (this.toLong() < ((LongValue) oValue).toLong()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not an Long");
    }

    @Override
    public final Value smallerEqualThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof LongValue) {
            if (this.toLong() <= ((LongValue) oValue).toLong()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not an Long");
    }

    @Override
    public final Value largerThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof LongValue) {
            if (this.toLong() > ((LongValue) oValue).toLong()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not an Long");
    }

    @Override
    public final Value largerEqualThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof LongValue) {
            if (this.toLong() >= ((LongValue) oValue).toLong()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not an Long");
    }

    /**
     * Returns the string representation of the stored long value.
     *
     * @return the stored long value as a string
     */
    @Override
    public final String content() {

        return String.valueOf(_lValue);
    }

    /**
     * Returns the string representation of this value for use in program structure analysis.
     *
     * @return the string representation of the stored long value
     * @throws Exception if an error occurs in implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = this.toString();
        return strReturn;
    }
}
