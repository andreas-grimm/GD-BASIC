package eu.gricom.basic.error;

/**
 * EmptyStackException.java
 * <p>
 * Description: The EmptyStackException is thrown when the interpreter attempts to pop a value from the stack when no
 * values are available. This typically occurs with mismatched GOSUB/RETURN or FOR/NEXT pairs in the BASIC program.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
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
