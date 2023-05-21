package eu.gricom.basic.error;

/**
 * SyntaxErrorException.java
 * <p>
 * Description:
 * <p>
 * The syntax error exception is thrown when ever the parser or the interpreter are running into a programmed
 * syntax error. The error is then moved to the main classes to terminate the running program or the parsing
 * process.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
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
