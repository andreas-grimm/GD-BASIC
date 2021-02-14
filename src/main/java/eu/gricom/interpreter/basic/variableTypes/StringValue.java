package eu.gricom.interpreter.basic.variableTypes;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;

/**
 * StringValue.java
 * <p>
 * Description:
 * <p>
 * The StringValue is the container to hold all strings.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class StringValue implements Value {
    private final String _strValue;


    /**
     * Default constructor.
     *
     * @param strValue Value to be stored in the container
     */
    public StringValue(final String strValue) {

        _strValue = strValue;
    }

    @Override
    public final String toString() {

        return (_strValue);
    }


    @Override
    public final double toReal() {

        return (Double.parseDouble(_strValue));
    }


    @Override
    public final Value evaluate() {

        return (this);
    }


    @Override
    public final Value equals(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof StringValue) {
            if (this.toString().matches(oValue.toString())) {
                return (new BooleanValue(true));
            }

            return (new BooleanValue(false));
        }

        throw (new SyntaxErrorException(oValue.content() + " is not a String"));
    }

    @Override
    public Value notEqual(Value oValue) throws SyntaxErrorException {
        if (oValue instanceof StringValue) {
            if (this.toString().matches(oValue.toString())) {
                return (new BooleanValue(false));
            }

            return (new BooleanValue(true));
        }

        throw (new SyntaxErrorException(oValue.content() + " is not a String"));
    }

    @Override
    public final Value plus(final Value oValue) throws SyntaxErrorException {
        StringValue oReturn = new StringValue((_strValue + oValue.content()));
        return (oReturn);
    }

    @Override
    public final Value minus(final Value oValue) throws SyntaxErrorException {
        throw (new SyntaxErrorException(oValue.content() + " '-' for strings, the expression is not defined"));
    }

    @Override
    public final Value multiply(final Value oValue) throws SyntaxErrorException {
        throw (new SyntaxErrorException(oValue.content() + " '*' for strings, the expression is not defined"));
    }

    @Override
    public final Value divide(final Value oValue) throws SyntaxErrorException {
        throw (new SyntaxErrorException(oValue.content() + " '/' for strings, the expression is not defined"));
    }

    @Override
    public Value power(Value oValue) throws SyntaxErrorException {
        throw (new SyntaxErrorException(oValue.content() + " '^' for strings, the expression is not defined"));
    }

    @Override
    public final Value smallerThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof StringValue) {
            if (this.toString().compareTo(oValue.toString()) < 0) {
                return (new BooleanValue(true));
            } else {
                return (new BooleanValue(false));
            }
        }

        throw (new SyntaxErrorException(oValue.content() + " value is not a String"));
    }

    @Override
    public Value smallerEqualThan(Value oValue) throws SyntaxErrorException {
        if ((equals(oValue).toString() == "True") || (smallerThan(oValue).toString() == "True")) {
            return (new BooleanValue(true));
        } else {
            return (new BooleanValue(false));
        }
    }

    @Override
    public final Value largerThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof StringValue) {
            if (this.toString().compareTo(oValue.toString()) > 0) {
                return (new BooleanValue(true));
            } else {
                return (new BooleanValue(false));
            }
        }

        throw (new SyntaxErrorException(oValue.content() + " value is not a String"));
    }

    @Override
    public Value largerEqualThan(Value oValue) throws SyntaxErrorException {
        if ((equals(oValue).toString() == "True") || (largerThan(oValue).toString() == "True")) {
            return (new BooleanValue(true));
        } else {
            return (new BooleanValue(false));
        }
    }

    @Override
    public final String content() {

        return (_strValue);
    }
}
