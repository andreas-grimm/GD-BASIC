package eu.gricom.interpreter.basic.functions;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
import eu.gricom.interpreter.basic.variableTypes.Value;

/**
 * ASC Function.
 *
 * Description:
 *
 * The ASC function returns the ASCII code of the first character of a string. This parameter has to be a string.
 *
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
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
        if (oValue instanceof StringValue ) {

            if (oValue.toString().length() > 0) {
                return new IntegerValue(oValue.toString().charAt(0));
            } else {
                throw new RuntimeException("Input value empty");
            }
        }

        throw new RuntimeException("Input value not of type String: " + oValue);
    }
}
