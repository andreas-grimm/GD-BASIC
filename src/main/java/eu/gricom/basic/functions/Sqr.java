package eu.gricom.basic.functions;

import eu.gricom.basic.math.BcdMath;
import eu.gricom.basic.variabletypes.BcdValue;
import eu.gricom.basic.variabletypes.RealValue;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variabletypes.Value;

/**
 * Sqr.java
 * <p>
 * Description: The Sqr class implements the BASIC SQR function, which returns the square root of the input value.
 * The input parameter must be a non-negative numeric value.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class Sqr {

    /**
     * Private Constructor.
     */
    private Sqr() {
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
        if (oValue instanceof BcdValue) {
            return new BcdValue(BcdMath.sqrt(((BcdValue) oValue).getBigDecimal()));
        }
        if (oValue instanceof RealValue) {
            return new RealValue(Math.sqrt(oValue.toReal()));
        }

        throw new RuntimeException("Input value not of type Real: " + oValue);
    }

}
