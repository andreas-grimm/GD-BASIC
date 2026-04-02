package eu.gricom.basic.functions;

import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.LongValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.BooleanValue;
import eu.gricom.basic.variableTypes.Value;

/**
 * Not.java
 * <p>
 * Description: The Not class implements the BASIC NOT function for logical negation. For boolean values, it reverses
 * true to false and vice versa. For numeric values, zero or negative becomes 1, and positive becomes 0. The input
 * must be a numeric or boolean value.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class Not {

    /**
     * Private Constructor.
     */
    private Not() {
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
            if (((IntegerValue) oValue).toInt() <= 0) {
                return new IntegerValue(1);
            } else {
                return new IntegerValue(0);
            }
        } else
        if (oValue instanceof LongValue) {
            if (((LongValue) oValue).toLong() <= 0) {
                return new LongValue(1);
            } else {
                return new LongValue(0);
            }
        } else
        if (oValue instanceof RealValue) {
            if (oValue.toReal() <= 0) {
                return new RealValue(1);
            } else {
                return new RealValue(0);
            }
        } else
        if (oValue instanceof BooleanValue) {
            if (((BooleanValue) oValue).isTrue()) {
                return new BooleanValue(false);
            } else {
                return new BooleanValue(true);
            }
        }


        throw new RuntimeException("Input value not numeric: " + oValue);
    }
}
