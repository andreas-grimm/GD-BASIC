package eu.gricom.basic.functions;

import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

/**
 * VAL Function.
 *
 * Description:
 *
 * The VAL function returns the parsed value of an input string.
 *
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public final class Val {

    /**
     * Private Constructor.
     */
    private Val() {
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
        if (oValue instanceof StringValue) {
            return new RealValue(Double.parseDouble(oValue.toString()));
        }

        throw new RuntimeException("Input value not of type String: " + oValue);
    }

}
