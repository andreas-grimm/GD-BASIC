package eu.gricom.basic.functions;

import eu.gricom.basic.variableTypes.IntegerValue;

/**
 * Mem.java
 * <p>
 * Description: The Mem class implements the BASIC MEM function, which returns the amount of free memory available to
 * the Java Virtual Machine in bytes. This is useful for monitoring memory usage during program execution.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class Mem {

    /**
     * Private Constructor.
     */
    private Mem() {
    }

    /**
     * Functions implemented here are similar to Statements with the difference
     * that they actually return a result to the caller of type Value. The method execute
     * triggers the function.
     *
     * @return Value the return message of the function
     */
    public static IntegerValue execute() {

        return new IntegerValue((int) Runtime.getRuntime().freeMemory());
    }
}
