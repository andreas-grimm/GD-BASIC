package eu.gricom.basic.error;

/**
 * FileAlreadyExistsException.java
 * <p>
 * Description: The FileAlreadyExistsException class is thrown when attempting to save a BASIC program
 * to a file that already exists. This exception indicates that the file operation cannot proceed because
 * the destination file is already present in the file system and would be overwritten.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FileAlreadyExistsException extends Exception {

    /**
     * Constructor for FileAlreadyExistsException.
     *
     * @param strErrorMessage error message describing the file already exists condition
     */
    public FileAlreadyExistsException(final String strErrorMessage) {
        super(strErrorMessage);
    }
}
