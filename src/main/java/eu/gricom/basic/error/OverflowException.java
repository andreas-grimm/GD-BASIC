package eu.gricom.basic.error;

/**
 * OverflowException.java
 * <p>
 * Description: The OverflowException is thrown when a BCD (decimal, suffix '#') value would need more digits before the
 * decimal point than the configured limit (max_bcd_digits, 40 by default). Digits after the decimal point are rounded
 * instead of raising this error (decision D7 in the design document). This runtime error stops the program and is
 * reported to the user.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class OverflowException extends Exception {

    /**
     * Constructor of the overflow exception.
     *
     * @param strErrorMessage - error message describing the value that does not fit
     */
    public OverflowException(final String strErrorMessage) {
        super(strErrorMessage);
    }
}
