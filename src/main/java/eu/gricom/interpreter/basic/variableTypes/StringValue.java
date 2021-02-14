package eu.gricom.interpreter.basic.variableTypes;

import eu.gricom.interpreter.basic.error.RuntimeException;
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
    private final String _strNoIndex = "noIndex";
    private String _strKey = _strNoIndex;
    private String _strValue = "noValue";

    /**
     * Default constructor.
     *
     * @param strValue Value to be stored in the container
     */
    public StringValue(final String strValue) {
        _strValue = strValue;
    }

    /**
     * Default constructor.
     *
     * @param strKey to determine the index in the array
     * @param strValue Value to be stored in the container
     */
    public StringValue(String strKey, final String strValue) {
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
        StringValue oReturn = new StringValue(_strValue + oValue.toString());
        return oReturn;
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
        if (equals(oValue).toString() == "True"
                || smallerThan(oValue).toString() == "True") {
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
        if (equals(oValue).toString() == "True"
                || largerThan(oValue).toString() == "True") {
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
     * The keys extentions (squared ('[]') or round ('()') brackets are ignored in previous steps of the value
     * retrieval. During this step they are now processed.
     *
     * @param strKey the original requesting key.
     * @return processed string, either using round or squared brackets: substring or string being part of an array.
     * @throws RuntimeException for any errors occuring in the execution of the evaluation. Currently this happens if
     * the index in an array subscription is larger than the array.
     */
    public final Value process(final String strKey) throws RuntimeException {
        //String strWork = strKey;
        int iIndex = -1;
        /*
        String strIndex = strNoIndex;

        iIndex = strKey.indexOf("(");
        if (iIndex > 0) {

            int iEndBracket = strKey.indexOf(")");
            if (iIndex > 0 && iEndBracket > 0) {
                strIndex = Normalizer.normalizeIndex(strKey);
            }
        }
*/
        String strWorkString = _strValue;

        iIndex = strKey.indexOf("[");
        if (iIndex > 0) {
            int iEndBracket = strKey.indexOf("]");
            int iPosition = Integer.parseInt(strKey.substring(iIndex + 1, iEndBracket));

            if (iPosition >= strWorkString.length()) {
                throw new RuntimeException("Index value " + iPosition + " out of bounds");
            }

            return new StringValue(String.valueOf(strWorkString.charAt(iPosition)));
        }

        return this;
    }
}
