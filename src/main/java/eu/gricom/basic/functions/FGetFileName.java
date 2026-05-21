package eu.gricom.basic.functions;

import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

/**
 * FGetFileName.java
 * <p>
 * Description: The FGetFileName class implements the BASIC function to retrieve the file name associated with a given file ID.
 * It returns the file path as a string value for a file that is currently managed by the FileManager. If the file ID does not
 * correspond to an open file, the function returns a null value.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FGetFileName {

    /**
     * Private Constructor.
     */
    private FGetFileName() {
    }

    /**
     * Retrieves the file name for a given file ID from the FileManager.
     * Queries the FileManager to obtain the file path associated with the specified file ID.
     * This function is typically used to verify or display which file is associated with a particular file ID
     * during file I/O operations.
     *
     * @param iFileID the file ID for which to retrieve the associated file name
     * @return StringValue containing the file name/path if the file ID is open,
     *         or StringValue containing an empty string if the file ID is not found
     * @throws Exception if an error occurs during file name retrieval
     */
    public static Value execute(final int iFileID) throws Exception {
        FileManager oFileManager = new FileManager();
        return oFileManager.getFileName(iFileID);
    }
}
