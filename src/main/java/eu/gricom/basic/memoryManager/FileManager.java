package eu.gricom.basic.memorymanager;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.variabletypes.BooleanValue;
import eu.gricom.basic.variabletypes.IntegerValue;
import eu.gricom.basic.variabletypes.StringValue;
import eu.gricom.basic.variabletypes.Value;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
    // the absolute directory in which the program was started; CHDIR changes it (see ChDirStatement)
    private static String _strCurrentDirectory = System.getProperty("user.dir");
    private static final Map<Integer, String> _moFileIDMap = new HashMap<>();
    // last file name used with each file ID. It is kept after FCLOSE so that FRENAME can still find the file (Phase 7).
    private static final Map<Integer, String> _moLastFileName = new HashMap<>();
    private static final Map<Integer, Boolean> _moEoFMap = new HashMap<>();
    private static final Map<Integer, BufferedReader> _moFileRead = new HashMap<>();
    private static final Map<Integer, Integer> _moReadPos = new HashMap<>();
    private static final Map<Integer, BufferedWriter> _moFileWrite = new HashMap<>();

    private final Logger _oLogger = new Logger(this.getClass().getName());

    /**
     * Initialises the stack if it does not exist.
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
            // Paths.get adds the separator, so a directory with or without a trailing '/' gives a correct file name
            strWorkFileName = Paths.get(_strCurrentDirectory, strFileName).toString();
        }
        Integer oFileID = iFileID;

        // Check whether the file ID or the file name have been used before
        if (getFileStatus(iFileID)) {
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
                // Ensure parent directories exist
                Path oParentPath = oPath.getParent();
                if (oParentPath != null && !Files.exists(oParentPath)) {
                    Files.createDirectories(oParentPath);
                }
                BufferedWriter oWriter = Files.newBufferedWriter(
                    oPath,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
                );
                _moFileWrite.put(oFileID, oWriter);
            }
        } catch (IOException oException) {
            _oLogger.error("Failed to open file: " + strWorkFileName + ": " + oException);
            oException.printStackTrace(System.err);
            System.exit(-1);
        }

        // add the file name and file ID, also add the read/write marker
        _moFileIDMap.put(oFileID, strWorkFileName);
        _moLastFileName.put(oFileID, strWorkFileName);

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
            } catch (IOException oException) {
                _oLogger.error("Failed to close file: " + oException);
                System.exit(-1);
            }
        }

        if (_moFileWrite.containsKey(iFileID)) {
            try {
                if (_moFileWrite.containsKey(iFileID)) {
                    _moFileWrite.get(iFileID).close();
                }

                _moFileWrite.remove(iFileID);
            } catch (IOException oException) {
                _oLogger.error("Failed to close file: " + oException);
                System.exit(-1);
            }
        }

        // Phase 7 fix: the delete used to happen only for files opened for writing. Now it happens for
        // read handles too (FCLOSE id "DELETE").
        if (bDeleteFile && _moFileIDMap.containsKey(iFileID)) {
            try {
                Files.deleteIfExists(Paths.get(_moFileIDMap.get(iFileID)));
            } catch (IOException oException) {
                _oLogger.error("Failed to delete file: " + oException);
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
            } catch (IOException oException) {
                throw new eu.gricom.basic.error.RuntimeException("Incorrect input detected: " + oException.getMessage());
            } catch (Exception oException) {
                throw new RuntimeException("Unknown Exception: " + oException.getMessage());
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
    public final BooleanValue getEOF(int iFileId) {
        boolean bEOF = _moEoFMap.getOrDefault(iFileId, false);
        return new BooleanValue(bEOF);
    }

    /**
     * markEndOfFile sets the end-of-file state of a file. FGET and FPEEK call it when they read past the end, so that
     * "WHILE NOT EOF(n)" loops end (Phase 7).
     *
     * @param iFileId the file ID
     */
    public final void markEndOfFile(final int iFileId) {
        _moEoFMap.put(iFileId, true);
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
     * Returns the character at a character position of an open file, without changing the state of the file.
     * A temporary reader is used, so the line reader of FINPUT keeps its place (Phase 7 fix for FPEEK).
     *
     * @param iFileID the file ID
     * @param iPosition the character position (lines are counted without their line end)
     * @return the one character as text, or "EOF" when the position is behind the end of the file
     * @throws IOException when the file cannot be read
     */
    public final String peekCharacter(final int iFileID, final int iPosition) throws IOException {
        String strFileName = _moFileIDMap.get(iFileID);
        if (strFileName == null) {
            strFileName = _moLastFileName.get(iFileID);
        }
        if (strFileName == null) {
            return "EOF";
        }

        int iAccumulated = 0;
        try (BufferedReader oReader = Files.newBufferedReader(Paths.get(strFileName), StandardCharsets.UTF_8)) {
            String strLine = oReader.readLine();
            while (strLine != null) {
                if (iAccumulated + strLine.length() > iPosition) {
                    return strLine.substring(iPosition - iAccumulated, iPosition - iAccumulated + 1);
                }
                iAccumulated += strLine.length();
                strLine = oReader.readLine();
            }
        }
        return "EOF";
    }

    /**
     * Remembers the file name for a file ID without opening the file. Used by FRENAME (Phase 7).
     *
     * @param iFileID the file ID
     * @param strFileName the new file name
     */
    public final void rememberFileName(final int iFileID, final String strFileName) {
        _moLastFileName.put(iFileID, strFileName);
    }

    /**
     * Returns the last file name that was used with a file ID, also after the file was closed.
     *
     * @param iFileID the file ID to look up
     * @return the last file name, or null when this file ID was never used
     */
    public final String getLastFileName(final int iFileID) {
        return _moLastFileName.get(iFileID);
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
            return FileOpenType.READ;
        }

        if (_moFileWrite.containsKey(iFileID)) {
            return FileOpenType.WRITE;
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
            Integer oPos = _moReadPos.get(iFileID);
            if (oPos == null) {
                return new IntegerValue(0);
            }
            return new IntegerValue(oPos);
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
