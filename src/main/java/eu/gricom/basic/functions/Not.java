package eu.gricom.basic.functions;

import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.LongValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.BooleanValue;
import eu.gricom.basic.variableTypes.Value;

/**
 * NOT Function.
 *
 * Description:
 *
 * The NOT function revereses the true / false content of a variable, ie. true becomes false and vice versa. For
 * numerical values, any value that is smaller or equal to 0 becomes 1, every value that is larger than 0 becomes 0.
 * For boolean variables the content is swapped. This parameter has to be numeric or boolean.
 *
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
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
