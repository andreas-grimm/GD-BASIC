package eu.gricom.basic.error;

/**
 * RuntimeException.java
 * <p>
 * Description: The RuntimeException class is thrown when the interpreter encounters an error during program execution.
 * This includes situations such as broken navigation links between BASIC line numbers, token numbers, and statement
 * numbers, invalid array index access, or other runtime errors that prevent normal program execution.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class RuntimeException extends Exception {

    /**
     * Constructor of the Code Generator object.
     *
     * @param strErrorMessage - error message related to the syntax error
     */
    public RuntimeException(final String strErrorMessage) {
        super(strErrorMessage);
    }
}
