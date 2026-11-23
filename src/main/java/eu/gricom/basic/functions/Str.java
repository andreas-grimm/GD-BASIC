package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variabletypes.BcdValue;
import eu.gricom.basic.variabletypes.IntegerValue;
import eu.gricom.basic.variabletypes.LongValue;
import eu.gricom.basic.variabletypes.RealValue;
import eu.gricom.basic.variabletypes.StringValue;
import eu.gricom.basic.variabletypes.Value;

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
        // BCD values print in plain decimal notation without trailing zeros (decision D10)
        if (oValue instanceof RealValue
                || oValue instanceof IntegerValue
                || oValue instanceof LongValue
                || oValue instanceof BcdValue) {
            return new StringValue(oValue.toString());
        }

        throw new RuntimeException("Input value not of type numeric: " + oValue);
    }

}
