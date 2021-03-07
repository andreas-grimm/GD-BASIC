package eu.gricom.interpreter.basic.functions;

import eu.gricom.interpreter.basic.variableTypes.IntegerValue;

/**
 * MEM Function.
 *
 * Description:
 *
 * The MEM function returns the available memory.
 *
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
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
     * @throws Exception as any execution error found during execution
     */
    public static IntegerValue execute() {

        return new IntegerValue((int) Runtime.getRuntime().freeMemory());
    }
}
