package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.statements.Expression;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

/**
 * Mid.java
 * <p>
 * Description: The Mid class implements the BASIC MID$ function, which extracts a substring from a string starting at
 * a specified position for a specified length. The first parameter is the source string, the second is the starting
 * position, and the third is the number of characters to extract.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
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
        // Phase 7: a number literal such as 4 is a REAL value; a whole number is accepted as a position.
        if (oValue instanceof StringValue
                && isWholeNumber(oStartPosition)
                && isWholeNumber(oEndPosition)) {
            String strValue = oValue.toString();
            int iStart = (int) ((Value) oStartPosition).toReal();
            int iEnd = (int) ((Value) oEndPosition).toReal();

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

    /**
     * isWholeNumber tells whether a value is an INTEGER, or a REAL value without fraction (such as 4.0).
     *
     * @param oExpression the evaluated parameter
     * @return true when the value can be used as a position
     */
    private static boolean isWholeNumber(final Expression oExpression) {
        if (oExpression instanceof IntegerValue) {
            return true;
        }
        if (oExpression instanceof RealValue) {
            double dValue = ((RealValue) oExpression).toReal();
            return dValue == Math.rint(dValue);
        }
        return false;
    }
}
