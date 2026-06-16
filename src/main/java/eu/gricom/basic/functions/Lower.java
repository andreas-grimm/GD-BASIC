package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

public class Lower {
    /**
     * Private Constructor.
     */
    private Lower() {
    }

    /**
     * LOWER$() function — converts a string to lowercase.
     * Implements the BASIC LOWER$ function that transforms all uppercase characters
     * in a string to their lowercase equivalents. Compatible with Tandy Level II and
     * Commodore PET BASIC implementations.
     *
     * @param oValue input string value to convert to lowercase
     * @return StringValue containing the input string converted to lowercase
     * @throws RuntimeException if input is not a string type
     */
    public static Value execute(final Value oValue) throws Exception {
        if (oValue instanceof StringValue) {
            return new StringValue(oValue.toString().toLowerCase());
        }

        throw new RuntimeException("Input value not of type String: " + oValue);
    }
}
