package eu.gricom.basic.functions;

import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.Value;

/**
 * EXP Function.
 *
 * Description:
 *
 * The EXP function returns the exponent naturalis (to the basis 'e'). This parameter has to be numeric.
 *
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public final class Exp {

    /**
     * Private Constructor.
     */
    private Exp() {
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
            return new RealValue(Math.exp(oValue.toReal()));
        }

        throw new RuntimeException("Input value not of type Real: " + oValue);
    }

}
