package eu.gricom.interpreter.basic.functions;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.Value;

/**
 * TAN Function.
 *
 * Description:
 *
 * The TAN function returns the tangent of an angle. This parameter has to be numeric.
 *
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public final class Tan {

    /**
     * Private Constructor.
     */
    private Tan() {
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
            return new RealValue(Math.tan(oValue.toReal()));
        }

        throw new RuntimeException("Input value not of type Real: " + oValue);
    }

}
