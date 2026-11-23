package eu.gricom.basic.functions;

import eu.gricom.basic.variabletypes.RealValue;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variabletypes.StringValue;
import eu.gricom.basic.variabletypes.Value;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Val.java
 * <p>
 * Description: The Val class implements the BASIC VAL function, which converts a string representation of a number
 * into its numeric value. The input must be a string containing a valid numeric format.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class Val {

    /** Optional leading spaces, then an optional sign, then digits with an optional fraction and exponent. */
    private static final Pattern NUMBER_AT_START =
            Pattern.compile("^\\s*[+-]?(\\d+\\.?\\d*|\\.\\d+)([eE][+-]?\\d+)?");

    /**
     * Private Constructor.
     */
    private Val() {
    }

    /**
     * Functions implemented here are similar to Statements with the difference
     * that they actually return a result to the caller of type Value. The method execute
     * triggers the function.
     *
     * @param oValue input value
     * @return Value the return message of the function
     * @throws Exception as any execution error found during execution
     */
    public static Value execute(final Value oValue) throws Exception {
        if (oValue instanceof StringValue) {
            // BASIC rule: skip leading spaces, read the number at the start of the text and ignore the rest.
            // If the text does not start with a number, the result is 0.
            Matcher oMatcher = NUMBER_AT_START.matcher(oValue.toString());
            if (oMatcher.find()) {
                return new RealValue(Double.parseDouble(oMatcher.group()));
            }
            return new RealValue(0.0);
        }

        throw new RuntimeException("Input value not of type String: " + oValue);
    }

}
