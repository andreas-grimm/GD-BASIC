package eu.gricom.interpreter.basic.error;

/**
 * EmptyStackException.java
 * <p>
 * Description:
 * <p>
 * The empty stack exception is thrown when ever the interpreter is trying to retrieve a value from a stack while the
 * stack is empty.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class EmptyStackException extends Exception {

    /**
     * Constructor of the Code Generator object.
     *
     * @param strErrorMessage - error message related to the syntax error
     */
    public EmptyStackException(final String strErrorMessage) {
        super(strErrorMessage);
    }
}
