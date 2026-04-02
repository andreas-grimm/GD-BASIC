package eu.gricom.basic.error;

/**
 * SyntaxErrorException.java
 * <p>
 * Description: The SyntaxErrorException is thrown when the parser or interpreter encounters invalid BASIC syntax. This
 * includes unrecognised keywords, malformed expressions, and structural errors. The exception propagates to the main
 * class for error reporting and program termination.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class SyntaxErrorException extends Exception {

    /**
     * Constructor of the Code Generator object.
     *
     * @param strErrorMessage - error message related to the syntax error
     */
    public SyntaxErrorException(final String strErrorMessage) {
        super(strErrorMessage);
    }
}
