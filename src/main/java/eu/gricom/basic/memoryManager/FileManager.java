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
    private static final Map<Integer, String> _moFileIDMap = new HashMap<>();
    private static final Map<Integer, Boolean> _moEoFMap = new HashMap<>();
    private static final Map<Integer, BufferedReader> _moFileRead = new HashMap<>();
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
        Integer oFileID = iFileID;

        // Check whether the file ID or the file name have been used before
        if (getFileStatus(iFileID) == true) {
            return false;
        }

        for (String strExistFileName : _moFileIDMap.values()) {
            if (strExistFileName.matches(strFileName)) {   // the file name already exists
                return false;
            }
        }
        // build path from the current location
        Path oPath = Paths.get(strFileName);

        // open the file and store the file handler
        try {
            if (eReadWrite == FileOpenType.READ) {
                BufferedReader oReader = Files.newBufferedReader(oPath, StandardCharsets.UTF_8);
                _moFileRead.put(oFileID, oReader);
                _moEoFMap.put(oFileID, false);
            }

            if (eReadWrite == FileOpenType.WRITE) {
                BufferedWriter oWriter = Files.newBufferedWriter(oPath, StandardCharsets.UTF_8);
                _moFileWrite.put(oFileID, oWriter);
            }
        } catch (IOException eException) {
            _oLogger.error("Failed to open file: " + strFileName + ": " + eException);
            System.exit(-1);
        }

        // add the file name and file ID, also add the read/write marker
        _moFileIDMap.put(oFileID, strFileName);

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

        if (_moEoFMap.getOrDefault(iFileId, true) == false) {
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
     * Return the name of a file based on the file ID
     *
     * @param iFileID the file that needs to be closed
     * @return name of the file if file known, null otherwise
     */
    public final String getFileName(final int iFileID) {
        return _moFileIDMap.get(iFileID);
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

    public final FileOpenType getFileType(final int iFileID) {
        if (_moFileRead.containsKey(iFileID)) {
            return FileOpenType.READ ;
        }

        if (_moFileWrite.containsKey(iFileID)) {
            return FileOpenType.WRITE ;
        }

        return null;
    }
}