package eu.gricom.basic.error;

/**
 * OutOfDataException.java
 * <p>
 * Description: The OutOfDataException is thrown when a READ statement attempts to retrieve a value from the DATA queue
 * but no more values are available. This indicates the program has consumed all DATA values without a corresponding
 * RESTORE or sufficient DATA statements.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
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
