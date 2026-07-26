package eu.gricom.basic.error;

/**
 * EmptyProgramException.java
 * <p>
 * Description: The EmptyProgramException class is thrown when attempting to load a BASIC program
 * from a file that exists but contains no content. This exception indicates that the program file
 * is empty and cannot be processed by the interpreter.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class EmptyProgramException extends Exception {

    /**
     * Constructor for EmptyProgramException.
     *
     * @param strErrorMessage error message describing the empty program condition
     */
    public EmptyProgramException(final String strErrorMessage) {
        super(strErrorMessage);
    }
}
