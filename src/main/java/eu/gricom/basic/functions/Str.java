package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.LongValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

/**
 * Str.java
 * <p>
 * Description: The Str class implements the BASIC STR$ function, which converts a numeric value into its string
 * representation. It handles integers, longs, and real numbers, returning the appropriate string format.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class Str {

    /**
     * Private Constructor.
     */
    private Str() {
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
        if (oValue instanceof RealValue
                || oValue instanceof IntegerValue
                || oValue instanceof LongValue) {
            return new StringValue(oValue.toString());
        }

        throw new RuntimeException("Input value not of type numeric: " + oValue);
    }

}
