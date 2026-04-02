package eu.gricom.basic.functions;

import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

/**
 * Asc.java
 * <p>
 * Description: The Asc class implements the BASIC ASC function, which returns the ASCII character code of the first
 * character in the input string. The input parameter must be a non-empty string value.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class Asc {

    /**
     * Private Constructor.
     */
    private Asc() {
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

            if (oValue.toString().length() > 0) {
                return new IntegerValue(oValue.toString().charAt(0));
            } else {
                throw new RuntimeException("Input value empty");
            }
        }

        throw new RuntimeException("Input value not of type String: " + oValue);
    }
}
