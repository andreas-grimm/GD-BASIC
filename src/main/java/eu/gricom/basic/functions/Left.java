package eu.gricom.basic.functions;

import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

/**
 * LEFT Function.
 *
 * Description:
 *
 * The LEFT function delivers a left substring of a string of a given length. This first parameter has to be a string,
 * the second has to be an integer.
 *
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public final class Left {

    /**
     * Private Constructor.
     */
    private Left() {
    }

    /**
     * Functions implemented here are similar to Statements with the difference
     * that they actually return a result to the caller of type Value. The method execute
     * triggers the function.
     *
     * @param oValue input string value
     * @param oLength length of the expected substring
     * @return Value the return message of the function
     * @throws Exception as any execution error found during execution
     */
    public static Value execute(final Value oValue, final Value oLength) throws Exception {
        int iLength = (int) oLength.toReal();

        if (oValue instanceof StringValue) {

            if (iLength >= ((IntegerValue) Len.execute(oValue)).toInt()) {
                throw new RuntimeException("Requested length exceeds size of String");
            }

            return new StringValue(oValue.toString().substring(0, iLength));
        }

        throw new RuntimeException("First Input value not String: " + oValue + " or second value is not integer: "
            + oLength.toString());
    }
}
