package eu.gricom.interpreter.basic.statements;

/**
 * NumberValue.java
 * <p>
 * Description:
 * <p>
 * The NumberValue is the container to hold all numeric values.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class NumberValue implements Value {
    private final double _iValue;

    /**
     * Default constructor.
     *
     * @param iValue Value to be stored in the container
     */
    public NumberValue(final double iValue) {

        _iValue = iValue;
    }

    /**
     * Override the standart toString method.
     *
     * @return the content of the variable as a string
     */
    @Override
    public final String toString() {

        return (Double.toString(_iValue));
    }

    /**
     * Transform the content of the number value into a double.
     *
     * @return the content of the variable as a double
     */
    public final double toNumber() {

        return (_iValue);
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
     * Content.
     *
     * Method for JUnit to return the content of the statement.
     *
     * @return - gives the name of the statement ("INPUT") and the variable name
     */
    @Override
    public final String content() {

        return (String.valueOf(_iValue));
    }
}
