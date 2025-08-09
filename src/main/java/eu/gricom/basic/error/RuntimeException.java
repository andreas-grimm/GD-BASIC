package eu.gricom.basic.error;

/**
 * RuntimeException.java
 * <p>
 * Description:
 * <p>
 * The runtime exception is thrown if
 *  - the link between BASIC line number, token number, and statement number is broken - the program is unable to
 *  navigate
 *  - ...
 *
 * <p>
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
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
