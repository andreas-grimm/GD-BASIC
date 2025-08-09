package eu.gricom.basic.variableTypes;

import eu.gricom.basic.error.DivideByZeroException;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.error.SyntaxErrorException;

import java.util.Objects;

/**
 * StringValue.java
 *
 * Description:
 *
 * The StringValue is the container to hold all strings.
 *
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

        return _strValue;
    }


    @Override
    public final double toReal() {

        return Double.parseDouble(_strValue);
    }


    @Override
    public final Value evaluate() {
        return this;
    }


    @Override
    public final Value equals(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof StringValue) {
            if (this.toString().matches(oValue.toString())) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not a String");
    }

    @Override
    public final Value notEqual(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof StringValue) {
            if (this.toString().matches(oValue.toString())) {
                return new BooleanValue(false);
            }

            return new BooleanValue(true);
        }

        throw new SyntaxErrorException(oValue.content() + " is not a String");
    }

    @Override
    public final Value plus(final Value oValue) throws SyntaxErrorException {
        return new StringValue(_strValue + oValue.toString());
    }

    @Override
    public final Value minus(final Value oValue) throws SyntaxErrorException {
        throw new SyntaxErrorException(oValue.content() + " '-' for strings, the expression is not defined");
    }

    @Override
    public final Value multiply(final Value oValue) throws SyntaxErrorException {
        throw new SyntaxErrorException(oValue.content() + " '*' for strings, the expression is not defined");
    }

    @Override
    public final Value divide(final Value oValue) throws SyntaxErrorException {
        throw new SyntaxErrorException(oValue.content() + " '/' for strings, the expression is not defined");
    }

    @Override
    public final Value modulo(final Value oValue) throws SyntaxErrorException {
        throw new SyntaxErrorException(oValue.content() + " '%' for strings, the expression is not defined");
    }

    @Override
    public final Value shiftLeft(final Value oValue) throws SyntaxErrorException {
        throw new SyntaxErrorException(oValue.content() + " '<<' for strings, the expression is not defined");
    }

    @Override
    public final Value and(final Value oValue) throws DivideByZeroException, SyntaxErrorException {
        throw new SyntaxErrorException(oValue.content() + " 'and' for strings, the expression is not defined");
    }

    @Override
    public final Value or(final Value oValue) throws DivideByZeroException, SyntaxErrorException {
        throw new SyntaxErrorException(oValue.content() + " 'or' for strings, the expression is not defined");
    }

    @Override
    public final Value shiftRight(final Value oValue) throws SyntaxErrorException {
        throw new SyntaxErrorException(oValue.content() + " '>>' for strings, the expression is not defined");
    }

    @Override
    public final Value power(final Value oValue) throws SyntaxErrorException {
        throw new SyntaxErrorException(oValue.content() + " '^' for strings, the expression is not defined");
    }

    @Override
    public final Value smallerThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof StringValue) {
            if (this.toString().compareTo(oValue.toString()) < 0) {
                return new BooleanValue(true);
            } else {
                return new BooleanValue(false);
            }
        }

        throw new SyntaxErrorException(oValue.content() + " value is not a String");
    }

    @Override
    public final Value smallerEqualThan(final Value oValue) throws SyntaxErrorException {
        if (Objects.equals(equals(oValue).toString(), "True")
                || Objects.equals(smallerThan(oValue).toString(), "True")) {
            return new BooleanValue(true);
        } else {
            return new BooleanValue(false);
        }
    }

    @Override
    public final Value largerThan(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof StringValue) {
            if (this.toString().compareTo(oValue.toString()) > 0) {
                return new BooleanValue(true);
            } else {
                return new BooleanValue(false);
            }
        }

        throw new SyntaxErrorException(oValue.content() + " value is not a String");
    }

    @Override
    public final Value largerEqualThan(final Value oValue) throws SyntaxErrorException {
        if (Objects.equals(equals(oValue).toString(), "True")
                || Objects.equals(largerThan(oValue).toString(), "True")) {
            return new BooleanValue(true);
        } else {
            return new BooleanValue(false);
        }
    }

    @Override
    public final String content() {

        return _strValue;
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
        return "\""+ this.toString() + "\"";
    }

    /**
     * The keys extentions (squared ('[]') or round ('()') brackets are ignored in previous steps of the value
     * retrieval. During this step they are now processed.
     *
     * @param strKey the original requesting key.
     * @return processed string, either using round or squared brackets: substring or string being part of an array.
     * @throws RuntimeException for any errors occuring in the execution of the evaluation. Currently this happens if
     * the index in an array subscription is larger than the array.
     */
    public final Value process(final String strKey) throws RuntimeException {

        if (strKey.indexOf("[") > 0) {
            return new StringValue(squareBrackets(strKey));
        }

        return this;
    }

    private String squareBrackets(final String strKey) throws RuntimeException {
        // lets check whether between the brackets is a comma
        int iComma = strKey.indexOf(",");
        int iStart = strKey.indexOf("[");
        int iEnd = strKey.indexOf("]");

        if (iComma > iStart && iComma < iEnd) {
            int iFirstNo = Integer.parseInt(strKey.substring(iStart + 1, iComma));
            int iSecondNo = Integer.parseInt(strKey.substring(iComma + 1, iEnd));

            if (iSecondNo >= _strValue.length()) {
                throw new RuntimeException("Index value " + iSecondNo + " out of bounds");
            }

            return _strValue.substring(iFirstNo, iSecondNo + 1);
        }

        // no - no comma, we return the pointed character
        int iPosition = Integer.parseInt(strKey.substring(iStart + 1, iEnd));

        if (iPosition >= _strValue.length()) {
            throw new RuntimeException("Index value " + iPosition + " out of bounds");
        }

        return String.valueOf(_strValue.charAt(iPosition));
    }
}
