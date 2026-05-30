package eu.gricom.basic.functions;

import eu.gricom.basic.error.FileNotFoundException;
import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * FLineCount.java
 * <p>
 * Description: The FLineCount class implements the BASIC function to count the number of lines in a file
 * identified by its file ID. It retrieves the file name from the FileManager, then temporarily opens the file
 * directly (bypassing FileManager) to count all lines. The file is not registered as open in the FileManager,
 * and is automatically closed after the count is complete. This function allows querying line count without
 * modifying the FileManager's file open state.
 * <p>
 * Usage Pattern:
 * - Open a file with OPEN statement (returns a file ID)
 * - Call FLINECOUNT with the file ID
 * - Function returns the number of lines in the file as an integer
 * - The file remains open in FileManager (this function doesn't affect its state)
 * <p>
 * Return Values:
 * - Valid file: Number of lines in the file (>=0)
 * - Empty file: 0 lines
 * - Closed file or unknown ID: Throws FileNotFoundException
 * <p>
 * Technical Implementation:
 * - Uses Java NIO Files API with BufferedReader for efficient line counting
 * - Opens file directly without registering with FileManager
 * - Closes file immediately after counting (not persistent)
 * - Handles all I/O exceptions gracefully with FileNotFoundException
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FLineCount {

    /**
     * Private Constructor.
     * Prevents instantiation of this utility class.
     */
    private FLineCount() {
    }

    /**
     * Counts the number of lines in a file identified by its file ID.
     * <p>
     * Execution Flow:
     * 1. Uses FileManager to look up the file path by file ID
     * 2. Checks if the file ID is registered (exists in FileManager)
     * 3. Opens the file temporarily using BufferedReader (not registering with FileManager)
     * 4. Counts all lines in the file by reading until EOF
     * 5. Closes the temporary file handle
     * 6. Returns the line count as an IntegerValue
     * <p>
     * Error Handling:
     * - Unknown file ID: Throws FileNotFoundException - file not registered
     * - Closed file: Throws FileNotFoundException - file was closed and unregistered
     * - File not found on disk: Throws FileNotFoundException - file was deleted or moved
     * - Access denied: Throws FileNotFoundException - file exists but is unreadable
     * - Other I/O errors: Throws FileNotFoundException - file cannot be read
     * <p>
     * Implementation Notes:
     * - Uses Files.newBufferedReader() from Java NIO for cross-platform compatibility
     * - Temporary file handle is not registered with FileManager
     * - File is automatically closed after counting (no resource leaks)
     * - Thread-safe: Can be called concurrently from multiple contexts
     * - No side effects: Does not modify the file or FileManager state
     * - Deterministic: Same input always returns same output (unless file is modified externally)
     * <p>
     * Performance Characteristics:
     * - Single pass through the file (O(n) where n is number of lines)
     * - Minimal memory overhead - only reads one line at a time
     * - No buffering of entire file in memory
     * - Suitable for large files
     * <p>
     * Platform Support:
     * - Windows: Native file access via Win32 APIs
     * - Linux/Unix: Native file access via system calls
     * - macOS: Native file access via system calls
     * - All Java-supported platforms: Via JVM's abstraction layer
     *
     * @param iFileID the file ID for which to count lines
     *                This ID must have been previously returned from an OPEN statement
     *                and must be currently registered in FileManager
     * @return IntegerValue containing the number of lines in the file:
     *         - >=0: The actual line count (guaranteed to be non-negative)
     *         - The return value is never null; always returns a valid IntegerValue
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

            // Step 5: Open the file temporarily (not registering with FileManager)
            // Use Files API directly instead of FileManager to avoid side effects
            BufferedReader oReader = Files.newBufferedReader(Paths.get(strFilePath), StandardCharsets.UTF_8);

            // Step 6: Count all lines in the file
            int iLineCount = 0;
            try {
                String strLine;
                while ((strLine = oReader.readLine()) != null) {
                    iLineCount++;
                }
            } finally {
                // Step 7: Always close the temporary file handle
                try {
                    oReader.close();
                } catch (IOException eCloseException) {
                    // Ignore close exceptions - we have the line count
                }
            }

            // Step 8: Return the line count as IntegerValue
            return new IntegerValue(iLineCount);

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
            throw new FileNotFoundException("Unexpected error counting lines: " + e.getMessage());
        }
    }
}
