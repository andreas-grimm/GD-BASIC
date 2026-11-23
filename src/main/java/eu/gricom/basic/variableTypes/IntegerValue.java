package eu.gricom.basic.variabletypes;

import eu.gricom.basic.error.DivideByZeroException;
import eu.gricom.basic.error.OverflowException;
import eu.gricom.basic.error.SyntaxErrorException;

/**
 * IntegerValue.java
 * <p>
 * Description: The IntegerValue class implements the Value interface for integer data types. It stores 32-bit integer
 * values and provides arithmetic operations, comparisons, and type conversions. Integer variables in BASIC are
 * identified by the % suffix and offer faster computation than floating-point types.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
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
        if (oValue instanceof BcdValue) {
            return oValue.equals(this);
        }
        if (oValue instanceof RealValue || oValue instanceof LongValue) {
            return new BooleanValue(this.toReal() == oValue.toReal());
        }
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
        if (oValue instanceof BcdValue) {
            return oValue.notEqual(this);
        }
        if (oValue instanceof RealValue || oValue instanceof LongValue) {
            return new BooleanValue(this.toReal() != oValue.toReal());
        }
        if (oValue instanceof IntegerValue) {
            if (this.toInt() != ((IntegerValue) oValue).toInt()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
    }

    @Override
    public final Value plus(final Value oValue) throws SyntaxErrorException, OverflowException {
        if (oValue instanceof BcdValue) {
            return BcdValue.of(this).plus(oValue);
        }
        if (oValue instanceof IntegerValue) {
            IntegerValue oReturn = new IntegerValue(_iValue + ((IntegerValue) oValue).toInt());
            return oReturn;
        } else if (oValue instanceof RealValue) {
            return new RealValue(this.toReal()).plus(oValue);
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
    }

    @Override
    public final Value minus(final Value oValue) throws SyntaxErrorException, OverflowException {
        if (oValue instanceof BcdValue) {
            return BcdValue.of(this).minus(oValue);
        }
        if (oValue instanceof IntegerValue) {
            IntegerValue oReturn = new IntegerValue(_iValue - ((IntegerValue) oValue).toInt());
            return oReturn;
        } else if (oValue instanceof RealValue) {
            return new RealValue(this.toReal()).minus(oValue);
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
    }

    @Override
    public final Value multiply(final Value oValue) throws SyntaxErrorException, OverflowException {
        if (oValue instanceof BcdValue) {
            return BcdValue.of(this).multiply(oValue);
        }
        if (oValue instanceof IntegerValue) {
            IntegerValue oReturn = new IntegerValue(_iValue * ((IntegerValue) oValue).toInt());
            return oReturn;
        } else if (oValue instanceof RealValue) {
            return new RealValue(this.toReal()).multiply(oValue);
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
    }

    @Override
    public final Value divide(final Value oValue) throws DivideByZeroException, SyntaxErrorException,
            OverflowException {
        IntegerValue oReturn;

        if (oValue instanceof BcdValue) {
            return BcdValue.of(this).divide(oValue);
        }
        if (oValue instanceof RealValue) {
            return new RealValue(this.toReal()).divide(oValue);
        }
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
    public final Value modulo(final Value oValue) throws DivideByZeroException, SyntaxErrorException,
            OverflowException {
        IntegerValue oReturn;

        if (oValue instanceof BcdValue) {
            return BcdValue.of(this).modulo(oValue);
        }
        if (oValue instanceof IntegerValue) {
            if (((IntegerValue) oValue).toInt() != 0) {
                oReturn = new IntegerValue(_iValue % ((IntegerValue) oValue).toInt());
            } else {
                throw new DivideByZeroException(this.toInt() + "%" + ((IntegerValue) oValue).toInt() + " is a division by zero");
            }
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
    }

    public final Value shiftLeft(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof BcdValue) {
            return shiftLeft(new IntegerValue(((BcdValue) oValue).getBigDecimal().intValue()));
        }
        if (oValue instanceof IntegerValue) {
            // a left shift by n bits multiplies by 2 to the power n (bug fixed in Phase 7: was * 2 * n)
            IntegerValue oReturn = new IntegerValue(_iValue << ((IntegerValue) oValue).toInt());
            return oReturn;
        } else if (oValue instanceof RealValue) {
            IntegerValue oReturn = new IntegerValue(_iValue << ((RealValue) oValue).toInt());
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
    }

    @Override
    public final Value and(final Value oValue) throws DivideByZeroException, SyntaxErrorException {
        if (oValue instanceof BcdValue) {
            return and(new IntegerValue(((BcdValue) oValue).getBigDecimal().intValue()));
        }
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
        if (oValue instanceof BcdValue) {
            return or(new IntegerValue(((BcdValue) oValue).getBigDecimal().intValue()));
        }
        if (oValue instanceof IntegerValue) {

            if (_iValue > 0 || ((IntegerValue) oValue).toInt() > 0) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not of type integer");
    }

    public final Value shiftRight(final Value oValue) throws DivideByZeroException, SyntaxErrorException {
        IntegerValue oReturn;

        if (oValue instanceof BcdValue) {
            return shiftRight(new IntegerValue(((BcdValue) oValue).getBigDecimal().intValue()));
        }
        if (oValue instanceof IntegerValue) {
            // a right shift by n bits divides by 2 to the power n (bug fixed in Phase 7: was / (2 * n)).
            // A shift by 0 is not a division by zero, so no exception is thrown here.
            oReturn = new IntegerValue(_iValue >> ((IntegerValue) oValue).toInt());
            return oReturn;
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
    }

    @Override
    public final Value power(final Value oValue) throws DivideByZeroException, SyntaxErrorException,
            OverflowException {
        IntegerValue oReturn;

        if (oValue instanceof BcdValue) {
            return BcdValue.of(this).power(oValue);
        }
        if (oValue instanceof IntegerValue) {
            oReturn = new IntegerValue(new RealValue(Math.pow(_iValue, ((IntegerValue) oValue).toInt())).toInt());

            return oReturn;
        } else if (oValue instanceof RealValue) {
            return new RealValue(Math.pow(this.toReal(), oValue.toReal()));
        }

        throw new SyntaxErrorException(oValue.content() + " is not a integer");
    }

    @Override
    public final Value smallerThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof BcdValue) {
            return oValue.largerThan(this);
        }
        if (oValue instanceof IntegerValue) {
            if (this.toInt() < ((IntegerValue) oValue).toInt()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        } else if (oValue instanceof RealValue) {
            if (this.toReal() < oValue.toReal()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
    }

    @Override
    public final Value smallerEqualThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof BcdValue) {
            return oValue.largerEqualThan(this);
        }
        if (oValue instanceof IntegerValue) {
            if (this.toInt() <= ((IntegerValue) oValue).toInt()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        } else if (oValue instanceof RealValue) {
            if (this.toReal() <= oValue.toReal()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
    }

    @Override
    public final Value largerThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof BcdValue) {
            return oValue.smallerThan(this);
        }
        if (oValue instanceof IntegerValue) {
            if (this.toInt() > ((IntegerValue) oValue).toInt()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        } else if (oValue instanceof RealValue) {
            if (this.toReal() > oValue.toReal()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not an integer");
    }

    @Override
    public final Value largerEqualThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof BcdValue) {
            return oValue.smallerEqualThan(this);
        }
        if (oValue instanceof IntegerValue) {
            if (this.toInt() >= ((IntegerValue) oValue).toInt()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        } else if (oValue instanceof RealValue) {
            if (this.toReal() >= oValue.toReal()) {
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
