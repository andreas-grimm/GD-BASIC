package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.statements.Expression;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

/**
 * ABS Function.
 *
 * Description:
 *
 * The ABS function delivers the absolute value of the parameter. This parameter has to be numeric.
 *
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public final class Mid {

    /**
     * Private Constructor.
     */
    private Mid() {
    }

    /**
     * Functions implemented here are similar to Statements with the difference
     * that they actually return a result to the caller of type Value. The method execute
     * triggers the function.
     *
     * @param oValue input value
     * @param oStartPosition Starting position of the substring
     * @param oEndPosition Ending position of the substring
     * @return Value the return message of the function
     * @throws Exception as any execution error found during execution
     */
    public static Value execute(final Expression oValue,
                                final Expression oStartPosition,
                                final Expression oEndPosition) throws Exception {
        if (oValue instanceof StringValue
                && oStartPosition instanceof IntegerValue
                && oEndPosition instanceof IntegerValue) {
            String strValue = oValue.toString();
            int iStart = ((IntegerValue) oStartPosition).toInt();
            int iEnd = ((IntegerValue) oEndPosition).toInt();

            if (iStart < 0
                || iEnd < 0
                || iStart > iEnd
                || iEnd > oValue.toString().length()) {
                throw new RuntimeException("Requested parameter does not fit the String");
            }

            return new StringValue(strValue.substring(iStart, iEnd + 1));
        }

        throw new RuntimeException("Input value not String, parameter not integer");
    }
}
