package eu.gricom.interpreter.basic.error;

/**
 * UndefinedUserFunctionException.java
 * <p>
 * Description:
 * <p>
 * The Exception is thrown if the program is trying to execute a function that is not previous defined by the DEF
 * command.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
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
