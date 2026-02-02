package eu.gricom.basic.functions;

import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.LongValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.Value;

/**
 * Abs.java
 * <p>
 * Description: The Abs class implements the BASIC ABS function, which returns the absolute (non-negative) value of a
 * numeric parameter. It handles integer, long, and real value types.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class Abs {

    /**
     * Private Constructor.
     */
    private Abs() {
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
            if (((IntegerValue) oValue).toInt() < 0) {
                return oValue.multiply(new IntegerValue(-1));
            } else {
                return oValue;
            }
        } else
        if (oValue instanceof LongValue) {
            if (((LongValue) oValue).toLong() < 0) {
                return oValue.multiply(new LongValue(-1));
            } else {
                return oValue;
            }
        } else
        if (oValue instanceof RealValue) {
            if (oValue.toReal() < 0) {
                return oValue.multiply(new RealValue(-1));
            } else {
                return oValue;
            }
        }

        throw new RuntimeException("Input value not numeric: " + oValue);
    }
}
