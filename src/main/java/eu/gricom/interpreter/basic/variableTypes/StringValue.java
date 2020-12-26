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

    /**
     * Override the standart toString method.
     *
     * @return the content of the variable as a string
     */
    @Override
    public final String toString() {

        return (_strValue);
    }


    /**
     * Transform the content of the number value into a double.
     *
     * @return the content of the variable as a double
     */
    public final double toReal() {

        return (Double.parseDouble(_strValue));
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
     * Implement the 'and' function.
     *
     * @return concationation of the string with the object
     * @throws SyntaxErrorException - will not be thrown
     */
    public final Value plus(Value oValue) throws SyntaxErrorException {
        StringValue oReturn = new StringValue((_strValue + oValue.content()));
        return (oReturn);
    }

    /**
     * Implement the minus function.
     *
     * @return nothing, will always throw an exception
     * @throws SyntaxErrorException as the function is not defined for strings
     */
    public final Value minus(Value oValue) throws SyntaxErrorException {
        throw (new SyntaxErrorException(oValue.content() + " '-' for strings, the expression is not defined"));
    }

    /**
     * Implement the multiply function.
     *
     * @return nothing, will always throw an exception
     * @throws SyntaxErrorException as the function is not defined for strings
     */
    public final Value multiply(Value oValue) throws SyntaxErrorException {
        throw (new SyntaxErrorException(oValue.content() + " '*' for strings, the expression is not defined"));
    }

    /**
     * Blocking the divide function.
     *
     * @return nothing, will always throw an exception
     * @throws SyntaxErrorException as the function is not defined for strings
     */
    public final Value divide(Value oValue) throws SyntaxErrorException {
        throw (new SyntaxErrorException(oValue.content() + " '/' for strings, the expression is not defined"));
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

        return (_strValue);
    }
}
