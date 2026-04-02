package eu.gricom.basic.functions;

import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.Value;

/**
 * Atn.java
 * <p>
 * Description: The Atn class implements the BASIC ATN function, which returns the arctangent (inverse tangent) of the
 * input value in radians, with results in the range -pi/2 through pi/2. The input parameter must be a numeric value.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class Atn {

    /**
     * Private Constructor.
     */
    private Atn() {
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
            return new RealValue(Math.atan(oValue.toReal()));
        }

        throw new RuntimeException("Input value not of type Real: " + oValue);
    }
}
