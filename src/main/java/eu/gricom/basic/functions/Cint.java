package eu.gricom.basic.functions;

import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.LongValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.Value;

/**
 * Cint.java
 * <p>
 * Description: The Cint class implements the BASIC CINT function, which converts a numeric value to an integer by
 * truncating any fractional part. The input parameter must be a numeric value (integer, long, or real).
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class Cint {

    /**
     * Private Constructor.
     */
    private Cint() {
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
            return oValue;
        } else
        if (oValue instanceof LongValue) {
            return new IntegerValue((float) oValue.toReal());
        } else
        if (oValue instanceof RealValue) {
            return new IntegerValue(((RealValue) oValue).toInt());
        }

        throw new RuntimeException("Input value not numeric: " + oValue);
    }
}
