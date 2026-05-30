package eu.gricom.basic.functions;

import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

/**
 * GetCwd.java
 * <p>
 * Description: The GetCwd class implements the BASIC function to retrieve the current working directory.
 * It queries the FileManager to obtain the current directory path that is used as the base path for
 * file operations. This function allows BASIC programs to determine the active working directory.
 * <p>
 * Usage Pattern:
 * - Call GETCWD to get the current working directory
 * - Function returns the directory path as a string
 * - The returned path is the base directory used for relative file paths in OPEN statements
 * <p>
 * Return Values:
 * - Current directory path: String containing the current working directory (may be empty if not set)
 * - Always returns a StringValue; never returns null
 * <p>
 * Technical Implementation:
 * - Queries the FileManager's internal _strCurrentDirectory variable
 * - Returns the exact path stored in FileManager
 * - No I/O operations or file system access required
 * - Thread-safe and side-effect free
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class GetCwd {

    /**
     * Private Constructor.
     * Prevents instantiation of this utility class.
     */
    private GetCwd() {
    }

    /**
     * Retrieves the current working directory.
     * <p>
     * Execution Flow:
     * 1. Creates a FileManager instance
     * 2. Calls getCurrentDirectory() to retrieve the current directory path
     * 3. Wraps the result in a StringValue
     * 4. Returns the StringValue to the caller
     * <p>
     * Return Values:
     * - Current directory path as a StringValue
     * - Empty string if no directory has been set
     * - Absolute or relative path as stored in FileManager
     * <p>
     * Implementation Notes:
     * - No parameters required for this function
     * - No I/O operations - only queries internal state
     * - Thread-safe: Can be called concurrently
     * - No side effects: Does not modify any state
     * - Deterministic: Always returns the same value until directory changes
     * <p>
     * Performance Characteristics:
     * - Constant time O(1) operation
     * - Minimal overhead - simple state lookup
     * - No file system access
     * - Suitable for frequent calls
     * <p>
     * Usage in BASIC:
     * - LET cwd$ = GETCWD
     * - PRINT "Current directory: " + GETCWD
     * - IF GETCWD = "/tmp/" THEN ...
     *
     * @return StringValue containing the current working directory path:
     *         - Contains the directory path as stored in FileManager
     *         - Empty string if no directory has been set
     *         - The return value is never null; always returns a valid StringValue
     */
    public static Value execute() {
        // Step 1: Create a FileManager instance
        FileManager oFileManager = new FileManager();

        // Step 2: Get the current working directory
        String strCurrentDirectory = oFileManager.getCurrentDirectory();

        // Step 3: Wrap the result in a StringValue and return
        return new StringValue(strCurrentDirectory);
    }
}
