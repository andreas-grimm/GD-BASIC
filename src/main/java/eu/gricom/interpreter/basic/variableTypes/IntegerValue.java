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
     * Override the standart toString method.
     *
     * @return the content of the variable as a string
     */
    @Override
    public final String toString() {

        return (Integer.toString(_iValue));
    }

    /**
     * Transform the content of the number value into a double.
     *
     * @return the content of the variable as a double
     */
    public final double toReal() {

        return (_iValue);
    }

    /**
     * Transform the content of the number value into a double.
     *
     * @return the content of the variable as a double
     */
    public final int toInt() {

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
     * Implement the add function.
     *
     * @return the sum of this object plus another number object as a NumberValue
     */
    public final Value plus(Value oValue) throws SyntaxErrorException {
        if (oValue instanceof IntegerValue) {
            IntegerValue oReturn = new IntegerValue(_iValue + ((IntegerValue) oValue).toInt());
            return (oReturn);
        }

        throw (new SyntaxErrorException(oValue.content() + " is not an integer"));
    }

    /**
     * Implement the minus function.
     *
     * @return the difference of this object and another number object as a NumberValue
     */
    public final Value minus(Value oValue) throws SyntaxErrorException {
        if (oValue instanceof IntegerValue) {
            IntegerValue oReturn = new IntegerValue(_iValue - ((IntegerValue) oValue).toInt());
            return (oReturn);
        }

        throw (new SyntaxErrorException(oValue.content() + " is not an integer"));
    }

    /**
     * Implement the multiply function.
     *
     * @return the result of the multiplication of this object and another number object as a NumberValue
     */
    public final Value multiply(Value oValue) throws SyntaxErrorException {
        if (oValue instanceof IntegerValue) {
            IntegerValue oReturn = new IntegerValue(_iValue / ((IntegerValue) oValue).toInt());
            return (oReturn);
        }

        throw (new SyntaxErrorException(oValue.content() + " is not an integer"));
    }

    /**
     * Implement the divide function.
     *
     * @return the result of the division of this object and another number object as a NumberValue
     */
    public final Value divide(Value oValue) throws DivideByZeroException, SyntaxErrorException {
        IntegerValue oReturn;

        if (oValue instanceof IntegerValue) {
            if (((IntegerValue) oValue).toInt() != 0) {
                oReturn = new IntegerValue(_iValue / ((IntegerValue) oValue).toInt());
            } else {
                throw (new DivideByZeroException(this.toInt() + "/" + ((IntegerValue) oValue).toInt() + " is a " +
                        "division by zero"));
            }
            return (oReturn);
        }

        throw (new SyntaxErrorException(oValue.content() + " is not an integer"));
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
