package eu.gricom.basic.memoryManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * FileManager.java
 * <p>
 * Description:
 * <p>
 * This class is implements the management functionality for used files.
 * <p>
 * (c) = 2021 by Andreas Grimm, Den Haag, The Netherlands
 */
public class FileManager {
    private static final Map<Integer, String> _aoFileIDMap = new HashMap<>();
    private static final Map<Integer, FileOpenType> _aoReadWriteSetting = new HashMap<>();
    private static final Map<Integer, File> _aoFileHandler = new HashMap<>();

    /**
     * Initializes the stack if it does not exist.
     */
    public FileManager() {
    }

    /**
     * Open a file: add them into lists and open the actual file.
     *
     * @param strFileName name of the file to be handled.
     * @param iFileID     integer as a primary key to identify files
     * @param eReadWrite  describes how the file is used
     * @return true if file has been opened
     */
    public final boolean fileOpen(final String strFileName, final int iFileID, final FileOpenType eReadWrite) {
        Integer oFileID = iFileID;

        _aoFileIDMap.put(oFileID, strFileName);
        _aoReadWriteSetting.put(oFileID, eReadWrite);

        return true;
    }


    /**
     * Close a file: remove files from all lists and close the physical files
     *
     * @param iFileID the file that needs to be closed
     * @return true if successful
     */
    public final boolean fileClose(final int iFileID) {

        return true;
    }


    /**
     * Return the name of a file based on the file ID
     *
     * @param iFileID the file that needs to be closed
     * @return name of the file if file known, null otherwise
     */
    public final String getFileName(final int iFileID) {
        return null;

    }

    /**
     * Return the state of a file based on the file ID
     *
     * @param iFileID the file that needs to be closed
     * @return true if the file is in the list and managed, false otherwise
     */
    public final boolean getFileStatus(final int iFileID) {
        return true;

    }

    /**
     * Return the type of file based on the file ID
     *
     * @param iFileID the file that needs to be closed
     * @return read or write, exception if file not known
     */
    public final String getFileType(final int iFileID) {
        return null;

    }
}