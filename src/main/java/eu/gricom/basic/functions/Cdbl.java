package eu.gricom.basic.functions;

import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.LongValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.Value;

/**
 * CDBL Function.
 *
 * Description:
 *
 * The CDBL function delivers the REAL value of the parameter. This parameter has to be numeric.
 *
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
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
