package eu.gricom.interpreter.basic.error;

/**
 * OutOfDataException.java
 * <p>
 * Description:
 * <p>
 * The Out Of Data exception is thrown when ever the read command is trying to retrieve a value from a FiFo queue while
 * the queue is empty.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class OutOfDataException extends Exception {

    /**
     * Constructor of the Code Generator object.
     *
     * @param strErrorMessage - error message related to the syntax error
     */
    public OutOfDataException(final String strErrorMessage) {
        super(strErrorMessage);
    }
}
