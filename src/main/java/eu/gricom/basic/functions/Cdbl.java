package eu.gricom.basic.functions;

import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.LongValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.Value;

/**
 * Cdbl.java
 * <p>
 * Description: The Cdbl class implements the BASIC CDBL function, which converts a numeric value to a double-precision
 * floating-point (real) number. The input parameter must be a numeric value (integer, long, or real).
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class Cdbl {

    /**
     * Private Constructor.
     */
    private Cdbl() {
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
        if (oValue instanceof IntegerValue) {
            return new RealValue(oValue.toReal());
        } else
        if (oValue instanceof LongValue) {
            return new RealValue(oValue.toReal());
        } else
        if (oValue instanceof RealValue) {
                return oValue;
        }

        throw new RuntimeException("Input value not numeric: " + oValue);
    }
}
