package eu.gricom.basic.functions;

import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

/**
 * Val.java
 * <p>
 * Description: The Val class implements the BASIC VAL function, which converts a string representation of a number
 * into its numeric value. The input must be a string containing a valid numeric format.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
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
