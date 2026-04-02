package eu.gricom.basic.error;

/**
 * UndefinedUserFunctionException.java
 * <p>
 * Description: The UndefinedUserFunctionException is thrown when the interpreter attempts to call a user-defined
 * function (created with DEF FN) that has not been previously defined. This ensures that all function references are
 * validated before execution.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class UndefinedUserFunctionException extends Exception {

    /**
     * Constructor of the Code Generator object.
     *
     * @param strErrorMessage - error message related to the syntax error
     */
    public UndefinedUserFunctionException(final String strErrorMessage) {
        super(strErrorMessage);
    }
}
