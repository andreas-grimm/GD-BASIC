package eu.gricom.interpreter.basic.error;

/**
 * DivideByZeroException.java
 * <p>
 * Description:
 * <p>
 * The divide by zero exception is thrown when ever the interpreter are running into a programmed mathematical
 * error: a division by zero. The error is then moved to the main classes to terminate the running program.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
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
