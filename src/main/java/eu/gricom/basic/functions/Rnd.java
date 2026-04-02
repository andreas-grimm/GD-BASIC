package eu.gricom.basic.functions;

import eu.gricom.basic.variableTypes.RealValue;

/**
 * Rnd.java
 * <p>
 * Description: The Rnd class implements the BASIC RND function, which returns a pseudo-random floating-point number
 * between 0 and 1. It utilises Java's built-in random number generator for generating the values.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class Rnd {

    /**
     * Private Constructor.
     */
    private Rnd() {
    }

    /**
     * Functions implemented here are similar to Statements with the difference
     * that they actually return a result to the caller of type Value. The method execute
     * triggers the function.
     *
     * @return Value the return message of the function
     */
    public static RealValue execute() {
            return new RealValue(Math.random());
    }
}
