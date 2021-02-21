package eu.gricom.interpreter.basic.functions;

import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import eu.gricom.interpreter.basic.variableTypes.LongValue;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.Value;

/**
 * CINT Function.
 *
 * Description:
 *
 * The CINT function delivers the integer value of the parameter. This parameter has to be numeric.
 *
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
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
