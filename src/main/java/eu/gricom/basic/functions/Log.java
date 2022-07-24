package eu.gricom.basic.functions;

import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.Value;

/**
 * EXP Function.
 *
 * Description:
 *
 * The LOG function returns the logarithm naturalis (to the basis 'e') of the input value. This parameter has to be
 * numeric.
 *
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public final class Log {

    /**
     * Private Constructor.
     */
    private Log() {
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
        if (oValue instanceof RealValue) {
            return new RealValue(Math.log(oValue.toReal()));
        }

        throw new RuntimeException("Input value not of type Real: " + oValue);
    }

}
