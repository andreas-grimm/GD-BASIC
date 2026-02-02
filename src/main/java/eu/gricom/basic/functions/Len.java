package eu.gricom.basic.functions;

import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

/**
 * Len.java
 * <p>
 * Description: The Len class implements the BASIC LEN function, which returns the length (number of characters) of
 * the input string. The input parameter must be a string value.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class Len {

    /**
     * Private Constructor.
     */
    private Len() {
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
                return new IntegerValue(oValue.toString().length());

        }

        throw new RuntimeException("Input value not of type String: " + oValue);
    }
}
