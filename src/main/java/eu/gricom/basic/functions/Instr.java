package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

/**
 * Instr.java
 * <p>
 * Description: The Instr class implements the BASIC INSTR function, which searches for a substring within a string and
 * returns its position. It returns 0 if the substring is not found, or the one-based index of the first occurrence.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class Instr {

    /**
     * Private Constructor.
     */
    private Instr() {
    }

    /**
     * Functions implemented here are similar to Statements with the difference
     * that they actually return a result to the caller of type Value. The method execute
     * triggers the function.
     *
     * @param oValue input value
     * @param oSearch search string
     * @return Value the return message of the function
     * @throws Exception as any execution error found during execution
     */
    public static Value execute(final Value oValue, final Value oSearch) throws Exception {
        if (oValue instanceof StringValue
                && oSearch instanceof StringValue) {

            if (oValue.toString().length() < 1
                    || oSearch.toString().length() < 1) {
                throw new RuntimeException("Input string or search string is empty");
            }

            return new IntegerValue(oValue.toString().indexOf(oSearch.toString()));
        }

        throw new RuntimeException("Input values are not String");
    }
}
