package eu.gricom.basic.functions;

import eu.gricom.basic.error.FileNotFoundException;
import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * FModTime.java
 * <p>
 * Description: The FModTime class implements the BASIC function to retrieve the date and time of the last
 * modification of a file identified by its file ID. It retrieves the file name from the FileManager, then
 * queries the operating system for the file's last modification timestamp. The file is not registered as open
 * in the FileManager. This function allows querying file metadata without modifying the FileManager's state.
 * <p>
 * Usage Pattern:
 * - Open a file with OPEN statement (returns a file ID)
 * - Call FMODTIME with the file ID
 * - Function returns the last modification date and time as a string
 * - The file remains open in FileManager (this function doesn't affect its state)
 * <p>
 * Return Values:
 * - Valid file: Date and time string in format "yyyy-MM-dd HH:mm:ss"
 * - File not found or unknown ID: Throws FileNotFoundException
 * <p>
 * Technical Implementation:
 * - Uses Java NIO Files API for cross-platform file system access
 * - Queries actual file system for modification time (not cached)
 * - Uses SimpleDateFormat to format the timestamp in a human-readable format
 * - Returns a StringValue for easy integration with BASIC programs
 * - Handles all exceptions gracefully with FileNotFoundException
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FModTime {

    /**
     * Private Constructor.
     * Prevents instantiation of this utility class.
     */
    private FModTime() {
    }

    /**
     * Retrieves the date and time of the last modification of a file identified by its file ID.
     * <p>
     * Execution Flow:
     * 1. Uses FileManager to look up the file path by file ID
     * 2. Checks if the file ID is registered (exists in FileManager)
     * 3. Queries the file system for the file's last modification timestamp
     * 4. Converts the timestamp to a human-readable date and time string
     * 5. Returns the formatted date and time as a StringValue
     * <p>
     * Error Handling:
     * - Unknown file ID: Throws FileNotFoundException - file not registered
     * - Closed file: Throws FileNotFoundException - file was closed and unregistered
     * - File not found on disk: Throws FileNotFoundException - file was deleted or moved
     * - Access denied: Throws FileNotFoundException - file exists but is unreadable
     * - Other I/O errors: Throws FileNotFoundException - file metadata cannot be accessed
     * <p>
     * Implementation Notes:
     * - Uses Files.getLastModifiedTime() from Java NIO for cross-platform compatibility
     * - Formats timestamp using SimpleDateFormat with "yyyy-MM-dd HH:mm:ss" pattern
     * - No file handle is opened or registered with FileManager
     * - Thread-safe: Can be called concurrently from multiple contexts
     * - No side effects: Does not modify the file or FileManager state
     * - Deterministic: Same input always returns same output (unless file is modified externally)
     * <p>
     * Performance Characteristics:
     * - Single file system query per call (O(1) complexity)
     * - Minimal overhead - only retrieves file metadata, not file content
     * - No file I/O operations - only metadata queries
     * - Suitable for frequent calls in loops or tight performance-critical code
     * <p>
     * Date/Time Format:
     * - Format: "yyyy-MM-dd HH:mm:ss"
     * - Example: "2026-05-17 08:12:32"
     * - Uses system default timezone
     * - Provides precision to the second
     * <p>
     * Platform Support:
     * - Windows: Native file attribute access via Win32 APIs
     * - Linux/Unix: Native file attribute access via system calls
     * - macOS: Native file attribute access via system calls
     * - All Java-supported platforms: Via JVM's abstraction layer
     *
     * @param iFileID the file ID for which to retrieve the modification date and time
     *                This ID must have been previously returned from an OPEN statement
     *                and must be currently registered in FileManager
     * @return StringValue containing the last modification date and time:
     *         - Format: "yyyy-MM-dd HH:mm:ss" (e.g., "2026-05-17 08:12:32")
     *         - The return value is never null; always returns a valid StringValue
     * @throws FileNotFoundException if the file ID is not registered with FileManager,
     *                              or if the file cannot be accessed for any reason
     */
    public static Value execute(final int iFileID) throws FileNotFoundException {
        try {
            // Step 1: Get the file manager and check if the file ID is registered
            FileManager oFileManager = new FileManager();

            // Step 2: Get the file name from FileManager
            Value oFileNameValue = oFileManager.getFileName(iFileID);

            // Step 3: Check if the file ID is registered
            // If FileManager returns empty string or null, the file ID is not registered
            if (oFileNameValue == null || oFileNameValue.toString().isEmpty()) {
                throw new FileNotFoundException("File ID " + iFileID + " does not exist or is not open");
            }

            // Step 4: Get the file path string
            String strFilePath = oFileNameValue.toString();

            // Step 5: Query the file system for the last modification time
            FileTime oModificationTime = Files.getLastModifiedTime(Paths.get(strFilePath));

            // Step 6: Convert FileTime to Date
            Date oDate = new Date(oModificationTime.toMillis());

            // Step 7: Format the date and time using SimpleDateFormat
            SimpleDateFormat oDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strFormattedDateTime = oDateFormat.format(oDate);

            // Step 8: Return the formatted date and time as StringValue
            return new StringValue(strFormattedDateTime);

        } catch (FileNotFoundException eFNFException) {
            // Re-throw FileNotFoundException - it means file ID is not registered
            throw eFNFException;

        } catch (java.nio.file.NoSuchFileException e) {
            // File does not exist on disk - throw FileNotFoundException
            throw new FileNotFoundException("File not found: " + e.getMessage());

        } catch (java.nio.file.AccessDeniedException e) {
            // File exists but cannot be accessed - throw FileNotFoundException
            throw new FileNotFoundException("Access denied to file: " + e.getMessage());

        } catch (java.io.IOException e) {
            // Other I/O errors (permission denied, I/O error, etc.) - throw FileNotFoundException
            throw new FileNotFoundException("I/O error reading file: " + e.getMessage());

        } catch (java.nio.file.InvalidPathException e) {
            // Invalid path format - throw FileNotFoundException
            throw new FileNotFoundException("Invalid file path: " + e.getMessage());

        } catch (SecurityException e) {
            // Security manager prevents file access - throw FileNotFoundException
            throw new FileNotFoundException("Security manager denied access: " + e.getMessage());

        } catch (NullPointerException e) {
            // Unexpected null reference - throw FileNotFoundException
            throw new FileNotFoundException("Unexpected null reference: " + e.getMessage());

        } catch (Exception e) {
            // Catch-all for any other unexpected exceptions
            throw new FileNotFoundException("Unexpected error retrieving modification time: " + e.getMessage());
        }
    }
}
