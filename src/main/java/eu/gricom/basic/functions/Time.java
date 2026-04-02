package eu.gricom.basic.functions;

import eu.gricom.basic.variableTypes.LongValue;

/**
 * Time.java
 * <p>
 * Description: The Time class implements the BASIC TIME function, which returns the current system time in
 * milliseconds since the Unix epoch. This is useful for timing operations or generating time-based seed values.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
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
