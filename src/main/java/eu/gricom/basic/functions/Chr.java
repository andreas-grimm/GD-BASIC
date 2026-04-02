package eu.gricom.basic.functions;

import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

/**
 * Chr.java
 * <p>
 * Description: The Chr class implements the BASIC CHR$ function, which returns the character corresponding to the
 * specified ASCII code value. The input parameter must be an integer in the valid ASCII range (0-255).
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class Chr {

    /**
     * Private Constructor.
     */
    private Chr() {
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
            return new StringValue(Character.toString((char) ((IntegerValue) oValue).toInt()));
        }

        throw new RuntimeException("Input value not of type integer: " + oValue);
    }
}
