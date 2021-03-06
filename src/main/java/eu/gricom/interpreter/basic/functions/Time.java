package eu.gricom.interpreter.basic.functions;

import eu.gricom.interpreter.basic.variableTypes.LongValue;

/**
 * TIME Function.
 *
 * Description:
 *
 * The TIME function returns the current time in milli seconds.
 *
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public final class Time {

    /**
     * Private Constructor.
     */
    private Time() {
    }

    /**
     * Functions implemented here are similar to Statements with the difference
     * that they actually return a result to the caller of type Value. The method execute
     * triggers the function.
     *
     * @return Value the return message of the function
     */
    public static LongValue execute() {
            return new LongValue(java.lang.System.currentTimeMillis());
    }
}
