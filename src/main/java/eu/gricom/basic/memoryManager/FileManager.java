package eu.gricom.basic.memoryManager;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.variableTypes.BooleanValue;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * FileManager.java
 * <p>
 * Description: The FileManager class handles file I/O operations for the BASIC interpreter. It maintains mappings
 * between file numbers and actual file handles, tracks read/write modes, and provides methods for opening, closing,
 * reading from, and writing to files used by OPEN, CLOSE, INPUT#, and PRINT# statements.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FileManager {
    private static String _strCurrentDirectory = new String();
    private static final Map<Integer, String> _moFileIDMap = new HashMap<>();
    private static final Map<Integer, Boolean> _moEoFMap = new HashMap<>();
    private static final Map<Integer, BufferedReader> _moFileRead = new HashMap<>();
    private static final Map<Integer, Integer> _moReadPos = new HashMap<>();
    private static final Map<Integer, BufferedWriter> _moFileWrite = new HashMap<>();

    private final Logger _oLogger = new Logger(this.getClass().getName());

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
    public final boolean openFile(final String strFileName, final int iFileID, final FileOpenType eReadWrite) {
        // If the filename is an absolute path, use it directly; otherwise prepend current directory
        String strWorkFileName;
        if (Paths.get(strFileName).isAbsolute()) {
            strWorkFileName = strFileName;
        } else {
            strWorkFileName = _strCurrentDirectory + strFileName;
        }
        Integer oFileID = iFileID;

        // Check whether the file ID or the file name have been used before
        if (getFileStatus(iFileID) == true) {
            return false;
        }

        for (String strExistFileName : _moFileIDMap.values()) {
            if (strExistFileName.matches(strWorkFileName)) {   // the file name already exists
                return false;
            }
        }
        // build path from the current location
        Path oPath = Paths.get(strWorkFileName);

        // open the file and store the file handler
        try {
            if (eReadWrite == FileOpenType.READ) {
                BufferedReader oReader = Files.newBufferedReader(oPath, StandardCharsets.UTF_8);
                _moFileRead.put(oFileID, oReader);
                _moEoFMap.put(oFileID, false);
                _moReadPos.put(oFileID, 0);
            }

            if (eReadWrite == FileOpenType.WRITE) {
                BufferedWriter oWriter = Files.newBufferedWriter(oPath, StandardCharsets.UTF_8);
                _moFileWrite.put(oFileID, oWriter);
            }
        } catch (IOException eException) {
            _oLogger.error("Failed to open file: " + strWorkFileName + ": " + eException);
            System.exit(-1);
        }

        // add the file name and file ID, also add the read/write marker
        _moFileIDMap.put(oFileID, strWorkFileName);

        return true;
    }


    /**
     * Close a file: remove files from all lists and close the physical files
     *
     * @param iFileID the file that needs to be closed
     * @param bDeleteFile true if the file should be deleted after closing
     */
    public final void closeFile(final int iFileID, final boolean bDeleteFile) {
        // Cleanup memory
        if (_moFileRead.containsKey(iFileID)) {
            try {
                _moFileRead.get(iFileID).close();
                _moFileRead.remove(iFileID);
                _moEoFMap.remove(iFileID);
                _moReadPos.remove(iFileID);
            } catch (IOException eException) {
                _oLogger.error("Failed to close file: " + eException);
                System.exit(-1);
            }
        }

        if (_moFileWrite.containsKey(iFileID)) {
            try {
                if (_moFileWrite.containsKey(iFileID)) {
                    _moFileWrite.get(iFileID).close();

                    if (bDeleteFile) {
                        Files.deleteIfExists(Paths.get(_moFileIDMap.get(iFileID)));
                    }
                }

                _moFileWrite.remove(iFileID);
            } catch (IOException eException) {
                _oLogger.error("Failed to close file: " + eException);
                System.exit(-1);
            }
        }

        _moFileIDMap.remove(iFileID);
    }

    /**
     * Read from a file based on the file ID
     *
     * @param iFileId the file that is use for the write operation
     */
    public final Value read(int iFileId) throws IOException, RuntimeException {
        Value oValue = null;

        BufferedReader oReader = getFileRead(iFileId);
        if (oReader != null) {
            try {
                String strInput = oReader.readLine();

                if (strInput == null) {
                    _moEoFMap.put(iFileId, true);
                    oValue = new StringValue("");
                } else {
                    // Store it as a number if possible, otherwise use a string.
                    oValue = new StringValue(strInput);
                }
            } catch (IOException eException) {
                throw new eu.gricom.basic.error.RuntimeException("Incorrect input detected: " + eException.getMessage());
            } catch (Exception eUnKnownException) {
                throw new RuntimeException("Unknown Exception: " + eUnKnownException.getMessage());
            }
        }

        return oValue;
    }

    /**
     * Get End-of-File
     *
     * @param iFileId the file that is use for the write operation
     * @return 0 for false, 1 for true
     */
    public final IntegerValue getEOF(int iFileId) {
        int iEOF = 0;

        if (_moEoFMap.getOrDefault(iFileId, false) == true) {
            iEOF = 1;
        }

        return new IntegerValue(iEOF);
    }

    /**
     * Write into a file based on the file ID
     *
     * @param iFileId the file that is use for the write operation
     */
    public final void write(int iFileId, String strData) throws IOException {
        BufferedWriter oWriter = getFileWrite(iFileId);
        if (oWriter != null) {
            oWriter.write(strData);
        }
    }

    /**
     * Return the name of a file based on the file ID.
     * Retrieves the file name from the internal map using the provided file ID.
     * If the file ID does not exist in the map, returns a StringValue containing an empty string.
     *
     * @param iFileID the file ID to look up
     * @return StringValue containing the file name if the file ID is found,
     *         or StringValue containing an empty string if the file ID is not found
     */
    public final StringValue getFileName(final int iFileID) {
        String strFileName = _moFileIDMap.get(iFileID);
        if (strFileName == null) {
            return new StringValue("");
        }
        return new StringValue(strFileName);
    }

    /**
     * Return the state of a file based on the file ID
     *
     * @param iFileID the file that needs to be closed
     * @return true if the file is in the list and managed, false otherwise
     */
    public final boolean getFileStatus(final int iFileID) {
        // Check whether the file ID or the file name have been used before
        for (int iExistFileID : _moFileIDMap.keySet()) {
            if (iExistFileID == iFileID) {                  // the file id already exists
                return true;
            }
        }
        return false;
    }

    /**
     * Return the internal file handler based on the file ID for reading the file
     *
     * @param iFileID the file that needs to be redd
     * @return the internal file handler for reading the file
     */
    public final BufferedReader getFileRead(final int iFileID) {
        if (_moFileRead.containsKey(iFileID)) {
           return _moFileRead.get(iFileID);
        }

        return null;
    }

    /**
     * Return the internal file handler based on the file ID for writing the file
     *
     * @param iFileID the file that needs to be written to
     * @return the internal file handler for writing the file
     */
    public final BufferedWriter getFileWrite(final int iFileID) {
        if (_moFileWrite.containsKey(iFileID)) {
            return _moFileWrite.get(iFileID);
        }

        return null;
    }

    /**
     * Get the file open mode (READ or WRITE) for a file identified by its file ID.
     * Determines whether the file was opened for reading or writing based on which internal map contains the file ID.
     *
     * @param iFileID the file ID that identifies the file
     * @return FileOpenType.READ if the file is open for reading, FileOpenType.WRITE if open for writing,
     *         or null if the file ID is not found in either map
     */
    public final FileOpenType getFileType(final int iFileID) {
        if (_moFileRead.containsKey(iFileID)) {
            return FileOpenType.READ ;
        }

        if (_moFileWrite.containsKey(iFileID)) {
            return FileOpenType.WRITE ;
        }

        return null;
    }

    /**
     * Put the read pointer in a read file into a storage so that the current read cursor is remembered.
     * Only to be used for files that are open for reading.
     *
     * @param iFileID the file ID that identifies the file
     * @param iPosition new position of the read cursor.
     */
    public final void putReadPos(final int iFileID, final int iPosition) throws RuntimeException {
        if (_moFileRead.containsKey(iFileID)) {
            _moReadPos.put(iFileID, iPosition);
        } else {
            throw new RuntimeException("File with ID " + iFileID + " is not open for reading");
        }
    }

    /**
     * Get the read pointer position from a read file. Retrieves the current read cursor position
     * that was previously stored using putReadPos. Only valid for files that are open for reading.
     *
     * @param iFileID the file ID that identifies the file
     * @return IntegerValue containing the read cursor position
     * @throws RuntimeException if the file is not open for reading
     */
    public final IntegerValue getReadPos(final int iFileID) throws RuntimeException {
        if (_moFileRead.containsKey(iFileID)) {
            Integer iPos = _moReadPos.get(iFileID);
            if (iPos == null) {
                return new IntegerValue(0);
            }
            return new IntegerValue(iPos);
        } else {
            throw new RuntimeException("File with ID " + iFileID + " is not open for reading");
        }
    }

    /**
     * Get the current working directory used for file operations.
     * The current directory is used as the base path for opening and accessing files in subsequent file operations.
     *
     * @return the current working directory path as a string
     */
    public final String getCurrentDirectory() {
        return _strCurrentDirectory;
    }

    /**
     * Set the current working directory for file operations.
     * Updates the base path used for opening and accessing files. All subsequent file operations will use this directory
     * as the base path unless an absolute path is provided for the file.
     *
     * @param strDirectory the directory path to set as the current working directory
     */
    public final void setCurrentDirectory(final String strDirectory) {
        _strCurrentDirectory = strDirectory;
    }
}