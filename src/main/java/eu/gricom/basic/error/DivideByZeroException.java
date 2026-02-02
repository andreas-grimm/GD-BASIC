package eu.gricom.basic.error;

/**
 * DivideByZeroException.java
 * <p>
 * Description: The DivideByZeroException is thrown when a division operation encounters a zero divisor. This runtime
 * error prevents undefined mathematical behaviour and propagates to the main class for error handling and program
 * termination.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class DivideByZeroException extends Exception {

    /**
     * Constructor of the Code Generator object.
     *
     * @param strErrorMessage - error message related to the syntax error
     */
    public DivideByZeroException(final String strErrorMessage) {
        super(strErrorMessage);
    }
}
