package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

public class Upper {
    /**
     * Private Constructor.
     */
    private Upper() {
    }

    /**
     * UPPER$() function — converts a string to uppercase.
     * Implements the BASIC UPPER$ function that transforms all lowercase characters
     * in a string to their uppercase equivalents. Compatible with Apple II, Tandy Level II,
     * and Commodore PET BASIC implementations.
     *
     * @param oValue input string value to convert to uppercase
     * @return StringValue containing the input string converted to uppercase
     * @throws RuntimeException if input is not a string type
     */
    public static Value execute(final Value oValue) throws Exception {
        if (oValue instanceof StringValue) {
            return new StringValue(oValue.toString().toUpperCase());
        }

        throw new RuntimeException("Input value not of type String: " + oValue);
    }
}
