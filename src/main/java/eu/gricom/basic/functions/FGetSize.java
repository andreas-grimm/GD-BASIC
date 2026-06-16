package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.variableTypes.LongValue;
import eu.gricom.basic.variableTypes.Value;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * FGetSize.java
 * <p>
 * Description: The FGetSize class implements the BASIC function to retrieve the file size in bytes for a file
 * associated with a given file ID. It queries the FileManager to obtain the file path, then uses the operating
 * system's file system APIs to determine the file size in bytes. The size is returned as a LongValue to support
 * files larger than 2GB (which would overflow an IntegerValue).
 * <p>
 * Usage Pattern:
 * - Open a file with OPEN statement (returns a file ID)
 * - Call FGETSIZE with the file ID
 * - Function returns the file size in bytes as a long integer
 * <p>
 * Return Values:
 * - Valid file size: Number of bytes in the file (>=0)
 * - Unknown file ID: 0 bytes
 * - Closed file: 0 bytes
 * - Error retrieving size: 0 bytes (graceful fallback)
 * <p>
 * Technical Implementation:
 * - Uses Java NIO Files API for cross-platform file system access
 * - Queries actual file system for current size (not cached)
 * - Handles missing files gracefully without exceptions
 * - Returns LongValue to support files up to 9,223,372,036,854,775,807 bytes (~8 exabytes)
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FGetSize {

    /**
     * Private Constructor.
     * Prevents instantiation of this utility class.
     */
    private FGetSize() {
    }

    /**
     * Retrieves the file size in bytes for a file identified by its file ID.
     * <p>
     * Execution Flow:
     * 1. Uses FileManager to look up the file path by file ID
     * 2. Converts the file path string to a Path object
     * 3. Queries the file system for the current file size
     * 4. Returns the size as a LongValue (supporting files up to ~8EB)
     * <p>
     * Error Handling:
     * - Unknown file ID: Returns LongValue(0) - graceful handling without exception
     * - Closed file: Returns LongValue(0) - file has been removed from management
     * - File not found: Returns LongValue(0) - file may have been deleted by OS
     * - Access denied: Returns LongValue(0) - file exists but is unreadable
     * - Other I/O errors: Returns LongValue(0) - caught and handled gracefully
     * <p>
     * Implementation Notes:
     * - Uses Files.size() from Java NIO for cross-platform compatibility
     * - Queries the actual file system on each call (not cached)
     * - Thread-safe: Can be called concurrently from multiple contexts
     * - No side effects: Does not modify the file or file system
     * - Deterministic: Same input always returns same output (unless file is modified by external process)
     * <p>
     * Performance Characteristics:
     * - Single file system query per call (O(1) complexity)
     * - Minimal overhead - only retrieves basic file metadata
     * - No file I/O operations - only metadata queries
     * - Suitable for frequent calls in loops or tight performance-critical code
     * <p>
     * Platform Support:
     * - Windows: Native file size retrieval via Win32 APIs
     * - Linux/Unix: Native file size retrieval via stat system call
     * - macOS: Native file size retrieval via stat system call
     * - All other Java-supported platforms: Via JVM's abstraction layer
     *
     * @param iFileID the file ID for which to retrieve the file size
     *                This ID must have been previously returned from an OPEN statement.
     * @return LongValue containing the file size in bytes:
     *         - >=0: The actual file size in bytes (guaranteed to be non-negative)
     *         - 0: If the file ID is unknown, the file is closed, or the file cannot be accessed
     *         The return value is never null; always returns a valid LongValue
     * @throws Exception should theoretically not throw exceptions; all errors are caught and handled gracefully
     *                   by returning LongValue(0)
     */
    public static Value execute(final int iFileID) throws Exception {
        try {
            // Step 1: Get the file name from FileManager using the file ID
            // This returns a StringValue which may contain a file path or empty string if unknown
            FileManager oFileManager = new FileManager();
            Value oFileNameValue = oFileManager.getFileName(iFileID);

            // If FileManager returns empty string (unknown file ID or closed file)
            if (oFileNameValue == null || oFileNameValue.toString().isEmpty()) {
                return new LongValue(0);
            }

            // Step 2: Get the file path string
            String strFilePath = oFileNameValue.toString();

            // Step 3: Convert string path to Path object for file system API
            Path oFilePath = Paths.get(strFilePath);

            // Step 4: Query the file system for the file size in bytes
            // Files.size() throws NoSuchFileException if file doesn't exist
            // We catch this and return 0 for graceful error handling
            long lFileSize = Files.size(oFilePath);

            // Step 5: Return the file size as a LongValue
            // LongValue can hold values from -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807
            // but file sizes are always non-negative
            return new LongValue(lFileSize);

        } catch (java.nio.file.NoSuchFileException e) {
            // File does not exist - return 0 bytes
            return new LongValue(0);

        } catch (java.nio.file.AccessDeniedException e) {
            // File exists but cannot be accessed - return 0 bytes
            return new LongValue(0);

        } catch (java.io.IOException e) {
            // Other I/O errors (permission denied, I/O error, etc.) - return 0 bytes
            return new LongValue(0);

        } catch (java.nio.file.InvalidPathException e) {
            // Invalid path format - return 0 bytes
            return new LongValue(0);

        } catch (SecurityException e) {
            // Security manager prevents file access - return 0 bytes
            return new LongValue(0);

        } catch (NullPointerException e) {
            // Unexpected null reference (should not occur with proper FileManager) - return 0 bytes
            return new LongValue(0);

        } catch (Exception e) {
            // Catch-all for any other unexpected exceptions - return 0 bytes gracefully
            return new LongValue(0);
        }
    }
}
