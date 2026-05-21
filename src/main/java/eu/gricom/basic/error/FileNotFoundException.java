package eu.gricom.basic.error;

/**
 * FileNotFoundException.java
 * <p>
 * Description: The FileNotFoundException class is thrown when the interpreter attempts to access a file
 * that is not registered with the FileManager. This occurs when a file ID is provided that does not
 * correspond to any open file in the FileManager's internal registry. This exception indicates that
 * a file operation was requested with an invalid or unknown file ID.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FileNotFoundException extends Exception {

    /**
     * Constructor for FileNotFoundException.
     *
     * @param strErrorMessage error message describing the file not found condition
     */
    public FileNotFoundException(final String strErrorMessage) {
        super(strErrorMessage);
    }
}
