package eu.gricom.basic.functions;

import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ListDirectory.java
 * <p>
 * Description: The ListDirectory class implements the BASIC function to retrieve a list of files
 * and/or subdirectories from a specified directory. It returns the directory contents as a
 * semicolon-separated string of file and directory names, with filtering options for hidden
 * files and subdirectories.
 * <p>
 * Parameters:
 * - strDirectory: Directory path. If empty string, uses FileManager's current directory
 * - bIncludeHidden: If true, includes hidden files and directories; if false, excludes them
 * - bIncludeSubdirectories: If true, includes subdirectory names; if false, excludes them
 * <p>
 * Return Values:
 * - StringValue containing semicolon-separated list of matching names
 * - Empty string if directory does not exist, is not accessible, or contains no matching items
 * - Returns empty string if directory path is empty and FileManager's current directory is also empty
 * <p>
 * Technical Implementation:
 * - Uses Java NIO Files API for cross-platform file system access
 * - No file registration with FileManager required
 * - Handles absolute and relative paths
 * - Thread-safe and side-effect free
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class ListDirectory {

    /**
     * Private Constructor.
     * Prevents instantiation of this utility class.
     */
    private ListDirectory() {
    }

    /**
     * Retrieves a list of files and/or directories from a specified directory.
     * <p>
     * Execution Flow:
     * 1. Determine the target directory from the strDirectory parameter or FileManager's current directory
     * 2. Return empty StringValue if the resulting directory path is empty
     * 3. List all entries in the directory using Files API
     * 4. Filter entries based on hidden file and subdirectory inclusion flags
     * 5. Collect matching names and join with semicolon separator
     * 6. Return as StringValue
     * <p>
     * Directory Resolution:
     * - If strDirectory is non-empty, use it as the target directory
     * - If strDirectory is empty, use FileManager's _strCurrentDirectory
     * - If both are empty, return empty StringValue
     * <p>
     * Filtering:
     * - bIncludeHidden: When true, includes entries marked as hidden by the file system
     * - bIncludeSubdirectories: When true, includes subdirectory names; when false, only files
     * <p>
     * Error Handling:
     * - Returns empty StringValue if directory does not exist
     * - Returns empty StringValue if directory is not accessible
     * - Returns empty StringValue if no matching entries found
     * <p>
     * Return Values:
     * - List of names separated by semicolons as StringValue
     * - Empty string if any condition prevents directory listing
     * <p>
     * Implementation Notes:
     * - Uses Files.list() to enumerate directory contents
     * - No I/O operations modify file system state
     * - Thread-safe: Can be called concurrently
     * - No side effects: Does not modify any state
     * <p>
     * Usage in BASIC:
     * - LET files$ = LISTDIRECTORY("/tmp", 0, 0)
     * - PRINT LISTDIRECTORY("./data", 1, 1)
     * - IF LISTDIRECTORY("", 0, 1) &lt;&gt; "" THEN ...
     *
     * @param strDirectory Directory path to list. If empty, uses FileManager's current directory.
     *                     May be absolute or relative path.
     * @param bIncludeHidden If true, includes hidden files and directories; if false, excludes them
     * @param bIncludeSubdirectories If true, includes subdirectory names; if false, includes only files
     * @return StringValue containing semicolon-separated list of matching names,
     *         or empty StringValue if directory does not exist or contains no matching entries
     */
    public static Value execute(final String strDirectory, final boolean bIncludeHidden,
                                 final boolean bIncludeSubdirectories) {
        // Step 1: Determine the target directory
        String strTargetDirectory = strDirectory;
        if (strTargetDirectory == null || strTargetDirectory.isEmpty()) {
            FileManager oFileManager = new FileManager();
            strTargetDirectory = oFileManager.getCurrentDirectory();
        }

        // Step 2: Return empty if directory path is empty
        if (strTargetDirectory == null || strTargetDirectory.isEmpty()) {
            return new StringValue("");
        }

        // Step 3: Get the Path object and verify directory exists and is accessible
        Path oPath = Paths.get(strTargetDirectory);
        if (!Files.isDirectory(oPath)) {
            return new StringValue("");
        }

        try {
            // Step 4: List directory contents with filtering
            List<String> oFileNames = Files.list(oPath)
                    .filter(path -> {
                        try {
                            // Filter based on hidden file flag
                            if (!bIncludeHidden && Files.isHidden(path)) {
                                return false;
                            }

                            // Filter based on directory inclusion flag
                            boolean bIsDirectory = Files.isDirectory(path);
                            if (bIsDirectory && !bIncludeSubdirectories) {
                                return false;
                            }

                            return true;
                        } catch (IOException e) {
                            return false;
                        }
                    })
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());

            // Step 5: Join names with semicolon separator
            String strResult = String.join(";", oFileNames);

            // Step 6: Return as StringValue
            return new StringValue(strResult);
        } catch (IOException e) {
            // Return empty string if directory cannot be read
            return new StringValue("");
        }
    }
}
