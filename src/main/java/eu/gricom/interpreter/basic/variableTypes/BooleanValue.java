package eu.gricom.interpreter.basic.variableTypes;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;

/**
 * BooleanValue.java
 * <p>
 * Description:
 * <p>
 * The BooleanValue is the container to manage all boolean values.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class BooleanValue implements Value {
    private final boolean _bValue;

    /**
     * Default constructor.
     *
     * @param bValue Value to be stored in the container
     */
    public BooleanValue(final boolean bValue) {

        _bValue = bValue;
    }

    /**
     * Douple Type constructor.
     *
     * @param fValue Value to be stored in the container
     */
    public BooleanValue(final double fValue) {

        if (fValue == 0) {
            _bValue = false;
        } else {
            _bValue = true;
        }
    }

    /**
     * Integer Type constructor.
     *
     * @param iValue Value to be stored in the container
     */
    public BooleanValue(final int iValue) {

        if (iValue == 0) {
            _bValue = false;
        } else {
            _bValue = true;
        }
    }

    /**
     * Override the standart toString method.
     *
     * @return the content of the variable as a string
     */
    @Override
    public final String toString() {

        if (_bValue) {
            return ("True");
        }

        return ("False");
    }

    /**
     * Override the standart toString method.
     *
     * @return the content of the variable as a string
     */
    public final boolean toBoolean() {
        return (_bValue);
    }

    /**
     * Transform the content of the number value into a double.
     *
     * @return the content of the variable as a double
     */
    public final double toReal() {

        if (_bValue) {
            return (1);
        }

        return (0);
    }

    /**
     * Return the value field as an object.
     *
     * @return the number value as an object
     */
    public final Value evaluate() {

        return (this);
    }

    /**
     * Return the value field as an object.
     *
     * @return the number value as an object
     */
    public final boolean isTrue() {

        return (_bValue);
    }

    @Override
    public final Value equals(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof BooleanValue) {
            if (this.toReal() == oValue.toReal()) {
                return (new BooleanValue(true));
            }

            return (new BooleanValue(false));
        }

        throw (new SyntaxErrorException(oValue.content() + " is not a boolean"));
    }

    @Override
    public final Value plus(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof BooleanValue) {
            // A + A = A
            if (((BooleanValue) oValue).isTrue() == _bValue) {
                return (this);
            }

            // A + (non A) = 1
            if (((BooleanValue) oValue).isTrue() != _bValue) {
                return (new BooleanValue(true));
            }

            // A + 1 = 1
            if (((BooleanValue) oValue).isTrue()) {
                return (new BooleanValue(true));
            } else {
                // A + 0 = A
                return (this);
            }
        }

        throw (new SyntaxErrorException(oValue.content() + " is not of type boolean"));
    }

    @Override
    public final Value minus(final Value oValue) throws SyntaxErrorException {
        throw (new SyntaxErrorException(oValue.content() + " '-' for boolean expression is not defined"));
    }

    @Override
    public final Value multiply(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof BooleanValue) {
            // 0 * A = 0
            if (!_bValue) {
                return (new BooleanValue(false));
            }

            // 1 * A = A
            if (_bValue) {
                return (new BooleanValue(true));
            }

            // A * A = A: covered above

            // A * (non A) = 0 : covered above
        }

        throw (new SyntaxErrorException(oValue.content() + " is not of type boolean"));
    }

    @Override
    public final Value divide(final Value oValue) throws SyntaxErrorException {
        throw (new SyntaxErrorException(oValue.content() + " '-' for boolean expression is not defined"));
    }

    // This one implemented the XOR statement
    @Override
    public Value power(Value oValue) throws SyntaxErrorException {
        if (oValue instanceof BooleanValue) {
            BooleanValue oWorkValue = (BooleanValue)oValue;

            if (_bValue == oWorkValue.toBoolean()) {
                return (new BooleanValue(false));
            } else {
                return (new BooleanValue(true));
            }
        }

        throw (new SyntaxErrorException(oValue.content() + " is not of type boolean"));
    }

    @Override
    public final Value smallerThan(final Value oValue) throws SyntaxErrorException {
        throw (new SyntaxErrorException(oValue.content() + " '-' for boolean expression is not defined"));
    }

    @Override
    public final Value largerThan(final Value oValue) throws SyntaxErrorException {
        throw (new SyntaxErrorException(oValue.content() + " '-' for boolean expression is not defined"));
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

        return (String.valueOf(_bValue));
    }
}
