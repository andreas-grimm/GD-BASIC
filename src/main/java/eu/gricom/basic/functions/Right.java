package eu.gricom.basic.functions;

import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

/**
 * RIGHT Function.
 *
 * Description:
 *
 * The RIGHT function delivers a substring of the first, string value with length as a second parameter. The first
 * parameter has to be of type string, the second parameter has to be an integer.
 *
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public final class Right {

    /**
     * Private Constructor.
     */
    private Right() {
    }

    /**
     * Functions implemented here are similar to Statements with the difference
     * that they actually return a result to the caller of type Value. The method execute
     * triggers the function.
     *
     * @param oValue input value: string as a source
     * @param oLength length of the substring
     * @return Value the return message of the function
     * @throws Exception as any execution error found during execution
     */
    public static Value execute(final Value oValue, final Value oLength) throws Exception {
        int iLength = (int) oLength.toReal();

        if (oValue instanceof StringValue) {
            String strValue = oValue.toString();

            if (iLength >= ((IntegerValue) Len.execute(oValue)).toInt()) {
                throw new RuntimeException("Requested length exceeds size of String");
            }

            return new StringValue(strValue.substring(strValue.length() - iLength));
        }

        throw new RuntimeException("First Input value not String: " + oValue + " or second value is not integer: "
            + oLength.toString());
    }
}
