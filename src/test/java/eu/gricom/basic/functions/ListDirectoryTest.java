package eu.gricom.basic.functions;

import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributeView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ListDirectoryTest.java
 * <p>
 * Unit tests for the ListDirectory BASIC function.
 * <p>
 * This test class provides comprehensive coverage of the ListDirectory function, which retrieves
 * a semicolon-separated list of files and directories from a specified directory. The function
 * supports filtering for hidden files and subdirectories.
 * <p>
 * Test Categories:
 * - POSITIVE TESTS: ListDirectory retrieves directory contents with various configurations
 * - NEGATIVE TESTS: ListDirectory handles non-existent directories and empty paths
 * - EDGE CASES: Empty directories, hidden files, subdirectories, and path resolution
 * - INTEGRATION TESTS: ListDirectory works correctly with FileManager current directory
 * <p>
 * Key Behavior:
 * - Returns StringValue containing semicolon-separated file/directory names
 * - Filters hidden files based on bIncludeHidden parameter
 * - Filters subdirectories based on bIncludeSubdirectories parameter
 * - Returns empty string if directory does not exist or is not accessible
 * - Uses FileManager's current directory if provided directory is empty
 * - Never returns null; always returns a valid StringValue
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class ListDirectoryTest {

    @TempDir
    Path _oTempDir;
    private FileManager _oFileManager;

    /**
     * Setup method: Initializes test environment before each test.
     * Creates temporary directory and resets FileManager.
     */
    @BeforeEach
    public void setUp() {
        _oFileManager = new FileManager();
        _oFileManager.setCurrentDirectory("");
    }

    // =========================================================================
    // POSITIVE TEST CASES - ListDirectory retrieves directory contents
    // =========================================================================

    /**
     * Test: ListDirectory with directory containing only files.
     * <p>
     * Given: Directory contains multiple files
     * When: ListDirectory.execute() is called with includeHidden=false, includeSubdirectories=false
     * Then: Returns semicolon-separated list of file names
     * <p>
     * Purpose: Verifies files are correctly listed and separated by semicolons
     */
    @Test
    public void testListDirectory_WithFilesOnly_ReturnsFilesWithSemicolonSeparator() throws IOException {
        // Setup: Create test files
        Path oFile1 = Files.createFile(_oTempDir.resolve("file1.txt"));
        Path oFile2 = Files.createFile(_oTempDir.resolve("file2.txt"));
        Path oFile3 = Files.createFile(_oTempDir.resolve("file3.txt"));

        // Execute: List directory
        Value oResult = ListDirectory.execute(_oTempDir.toString(), false, false);

        // Verify: Result contains all file names separated by semicolons
        assertNotNull(oResult);
        assertTrue(oResult instanceof StringValue);
        String strResult = oResult.toString();
        assertTrue(strResult.contains("file1.txt"));
        assertTrue(strResult.contains("file2.txt"));
        assertTrue(strResult.contains("file3.txt"));
        assertTrue(strResult.contains(";"));
    }

    /**
     * Test: ListDirectory with directory containing only subdirectories.
     * <p>
     * Given: Directory contains multiple subdirectories
     * When: ListDirectory.execute() is called with includeSubdirectories=true
     * Then: Returns semicolon-separated list of directory names
     * <p>
     * Purpose: Verifies subdirectories are included when requested
     */
    @Test
    public void testListDirectory_WithSubdirectories_ReturnsSubdirectoryNames() throws IOException {
        // Setup: Create test subdirectories
        Files.createDirectory(_oTempDir.resolve("dir1"));
        Files.createDirectory(_oTempDir.resolve("dir2"));
        Files.createDirectory(_oTempDir.resolve("dir3"));

        // Execute: List directory with includeSubdirectories=true
        Value oResult = ListDirectory.execute(_oTempDir.toString(), false, true);

        // Verify: Result contains all directory names
        assertNotNull(oResult);
        String strResult = oResult.toString();
        assertTrue(strResult.contains("dir1"));
        assertTrue(strResult.contains("dir2"));
        assertTrue(strResult.contains("dir3"));
    }

    /**
     * Test: ListDirectory excludes subdirectories when flag is false.
     * <p>
     * Given: Directory contains both files and subdirectories
     * When: ListDirectory.execute() is called with includeSubdirectories=false
     * Then: Returns only file names, excluding subdirectories
     * <p>
     * Purpose: Verifies subdirectories are excluded when not requested
     */
    @Test
    public void testListDirectory_WithIncludeSubdirectoriesFalse_ExcludesDirectories() throws IOException {
        // Setup: Create files and subdirectories
        Files.createFile(_oTempDir.resolve("file1.txt"));
        Files.createDirectory(_oTempDir.resolve("subdir1"));
        Files.createFile(_oTempDir.resolve("file2.txt"));

        // Execute: List directory with includeSubdirectories=false
        Value oResult = ListDirectory.execute(_oTempDir.toString(), false, false);

        // Verify: Result contains files but not directories
        String strResult = oResult.toString();
        assertTrue(strResult.contains("file1.txt"));
        assertTrue(strResult.contains("file2.txt"));
        assertFalse(strResult.contains("subdir1"));
    }

    /**
     * Test: ListDirectory includes hidden files when flag is true.
     * <p>
     * Given: Directory contains hidden files
     * When: ListDirectory.execute() is called with includeHidden=true
     * Then: Returns list including hidden file names
     * <p>
     * Purpose: Verifies hidden files are included when requested
     */
    @Test
    public void testListDirectory_WithIncludeHiddenTrue_IncludesHiddenFiles() throws IOException {
        // Setup: Create regular and hidden files
        Files.createFile(_oTempDir.resolve("visible.txt"));
        Path oHiddenFile = Files.createFile(_oTempDir.resolve(".hidden"));
        setHidden(oHiddenFile);

        // Execute: List directory with includeHidden=true
        Value oResult = ListDirectory.execute(_oTempDir.toString(), true, false);

        // Verify: Result contains both visible and hidden files
        String strResult = oResult.toString();
        assertTrue(strResult.contains("visible.txt"));
        assertTrue(strResult.contains(".hidden"));
    }

    /**
     * Test: ListDirectory excludes hidden files when flag is false.
     * <p>
     * Given: Directory contains hidden files
     * When: ListDirectory.execute() is called with includeHidden=false
     * Then: Returns list excluding hidden file names
     * <p>
     * Purpose: Verifies hidden files are excluded when not requested
     */
    @Test
    public void testListDirectory_WithIncludeHiddenFalse_ExcludesHiddenFiles() throws IOException {
        // Setup: Create regular and hidden files
        Files.createFile(_oTempDir.resolve("visible.txt"));
        Path oHiddenFile = Files.createFile(_oTempDir.resolve(".hidden"));
        setHidden(oHiddenFile);

        // Execute: List directory with includeHidden=false
        Value oResult = ListDirectory.execute(_oTempDir.toString(), false, false);

        // Verify: Result contains visible file but not hidden file
        String strResult = oResult.toString();
        assertTrue(strResult.contains("visible.txt"));
        assertFalse(strResult.contains(".hidden"));
    }

    /**
     * Test: ListDirectory with mixed content (files and subdirectories).
     * <p>
     * Given: Directory contains both files and subdirectories
     * When: ListDirectory.execute() is called with includeSubdirectories=true
     * Then: Returns both file and directory names separated by semicolons
     * <p>
     * Purpose: Verifies mixed directory content is handled correctly
     */
    @Test
    public void testListDirectory_WithMixedContent_ReturnsBothFilesAndDirectories() throws IOException {
        // Setup: Create mixed content
        Files.createFile(_oTempDir.resolve("file1.txt"));
        Files.createDirectory(_oTempDir.resolve("dir1"));
        Files.createFile(_oTempDir.resolve("file2.txt"));
        Files.createDirectory(_oTempDir.resolve("dir2"));

        // Execute: List directory with includeSubdirectories=true
        Value oResult = ListDirectory.execute(_oTempDir.toString(), false, true);

        // Verify: Result contains both files and directories
        String strResult = oResult.toString();
        assertTrue(strResult.contains("file1.txt"));
        assertTrue(strResult.contains("file2.txt"));
        assertTrue(strResult.contains("dir1"));
        assertTrue(strResult.contains("dir2"));
    }

    // =========================================================================
    // NEGATIVE TEST CASES - ListDirectory handles non-existent paths
    // =========================================================================

    /**
     * Test: ListDirectory with non-existent directory.
     * <p>
     * Given: Directory path does not exist
     * When: ListDirectory.execute() is called
     * Then: Returns empty StringValue
     * <p>
     * Purpose: Verifies safe handling of non-existent directories
     */
    @Test
    public void testListDirectory_WithNonExistentDirectory_ReturnsEmptyString() {
        // Setup: Use non-existent path
        String strNonExistent = _oTempDir.toString() + "/nonexistent/";

        // Execute: List non-existent directory
        Value oResult = ListDirectory.execute(strNonExistent, false, false);

        // Verify: Returns empty string
        assertEquals("", oResult.toString());
    }

    /**
     * Test: ListDirectory with file path instead of directory.
     * <p>
     * Given: Path points to a file, not a directory
     * When: ListDirectory.execute() is called
     * Then: Returns empty StringValue
     * <p>
     * Purpose: Verifies safe handling when path is a file
     */
    @Test
    public void testListDirectory_WithFilePath_ReturnsEmptyString() throws IOException {
        // Setup: Create a file instead of directory
        Path oFile = Files.createFile(_oTempDir.resolve("testfile.txt"));

        // Execute: Try to list file as directory
        Value oResult = ListDirectory.execute(oFile.toString(), false, false);

        // Verify: Returns empty string
        assertEquals("", oResult.toString());
    }

    /**
     * Test: ListDirectory with empty directory.
     * <p>
     * Given: Directory exists but is empty
     * When: ListDirectory.execute() is called
     * Then: Returns empty StringValue
     * <p>
     * Purpose: Verifies correct handling of empty directories
     */
    @Test
    public void testListDirectory_WithEmptyDirectory_ReturnsEmptyString() {
        // Setup: Empty temp directory

        // Execute: List empty directory
        Value oResult = ListDirectory.execute(_oTempDir.toString(), false, false);

        // Verify: Returns empty string
        assertEquals("", oResult.toString());
    }

    // =========================================================================
    // EDGE CASE TEST CASES - Path resolution and special scenarios
    // =========================================================================

    /**
     * Test: ListDirectory with null directory string.
     * <p>
     * Given: Directory string is null
     * When: ListDirectory.execute() is called
     * Then: Uses FileManager's current directory
     * <p>
     * Purpose: Verifies null handling delegates to FileManager
     */
    @Test
    public void testListDirectory_WithNullDirectory_UsesFileManagerCurrentDirectory() throws IOException {
        // Setup: Set FileManager current directory and create files
        _oFileManager.setCurrentDirectory(_oTempDir.toString());
        Files.createFile(_oTempDir.resolve("testfile.txt"));

        // Execute: Call with null directory (should use FileManager)
        Value oResult = ListDirectory.execute(null, false, false);

        // Verify: Returns files from FileManager's current directory
        assertEquals("testfile.txt", oResult.toString());
    }

    /**
     * Test: ListDirectory with empty directory string.
     * <p>
     * Given: Directory string is empty
     * When: ListDirectory.execute() is called
     * Then: Uses FileManager's current directory
     * <p>
     * Purpose: Verifies empty string delegation to FileManager
     */
    @Test
    public void testListDirectory_WithEmptyDirectory_UsesFileManagerCurrentDirectory() throws IOException {
        // Setup: Set FileManager current directory and create files
        _oFileManager.setCurrentDirectory(_oTempDir.toString());
        Files.createFile(_oTempDir.resolve("file1.txt"));
        Files.createFile(_oTempDir.resolve("file2.txt"));

        // Execute: Call with empty directory
        Value oResult = ListDirectory.execute("", false, false);

        // Verify: Returns files from FileManager's current directory
        String strResult = oResult.toString();
        assertTrue(strResult.contains("file1.txt"));
        assertTrue(strResult.contains("file2.txt"));
    }

    /**
     * Test: ListDirectory with empty FileManager directory.
     * <p>
     * Given: Both directory parameter and FileManager current directory are empty
     * When: ListDirectory.execute() is called
     * Then: Returns empty StringValue
     * <p>
     * Purpose: Verifies empty path returns empty result
     */
    @Test
    public void testListDirectory_WithEmptyFileManagerDirectory_ReturnsEmptyString() {
        // Setup: Ensure both paths are empty
        _oFileManager.setCurrentDirectory("");

        // Execute: Call with empty directory
        Value oResult = ListDirectory.execute("", false, false);

        // Verify: Returns empty string
        assertEquals("", oResult.toString());
    }

    /**
     * Test: ListDirectory returns StringValue type.
     * <p>
     * Given: ListDirectory is called
     * When: Return type is checked
     * Then: Always returns StringValue instance
     * <p>
     * Purpose: Verifies correct return type for BASIC integration
     */
    @Test
    public void testListDirectory_AlwaysReturnsStringValueType() throws IOException {
        // Setup: Create test file
        Files.createFile(_oTempDir.resolve("testfile.txt"));

        // Execute: Call ListDirectory
        Value oResult = ListDirectory.execute(_oTempDir.toString(), false, false);

        // Verify: Returns StringValue
        assertTrue(oResult instanceof StringValue);
        assertTrue(oResult instanceof Value);
    }

    /**
     * Test: ListDirectory never returns null.
     * <p>
     * Given: ListDirectory is called with various parameters
     * When: Result is checked for null
     * Then: Result is never null
     * <p>
     * Purpose: Verifies safe usage in BASIC programs
     */
    @Test
    public void testListDirectory_NeverReturnsNull() throws IOException {
        // Execute: Call with various parameters
        Value oResult1 = ListDirectory.execute(_oTempDir.toString(), false, false);
        Value oResult2 = ListDirectory.execute("", false, false);
        Value oResult3 = ListDirectory.execute("/nonexistent", false, false);

        // Verify: All results are non-null
        assertNotNull(oResult1);
        assertNotNull(oResult2);
        assertNotNull(oResult3);
    }

    /**
     * Test: ListDirectory with relative path.
     * <p>
     * Given: Directory path is relative
     * When: ListDirectory.execute() is called
     * Then: Resolves path and lists directory contents
     * <p>
     * Purpose: Verifies relative paths are handled correctly
     */
    @Test
    public void testListDirectory_WithAbsolutePath_ReturnsDirectoryContents() throws IOException {
        // Setup: Create test file
        Files.createFile(_oTempDir.resolve("testfile.txt"));

        // Execute: List with absolute path
        Value oResult = ListDirectory.execute(_oTempDir.toString(), false, false);

        // Verify: Returns directory contents
        assertEquals("testfile.txt", oResult.toString());
    }

    /**
     * Test: ListDirectory with special characters in file names.
     * <p>
     * Given: Directory contains files with special characters
     * When: ListDirectory.execute() is called
     * Then: Returns names with special characters preserved
     * <p>
     * Purpose: Verifies special characters in names are handled
     */
    @Test
    public void testListDirectory_WithSpecialCharactersInNames_PreservesNames() throws IOException {
        // Setup: Create files with special characters
        Files.createFile(_oTempDir.resolve("file-with-dash.txt"));
        Files.createFile(_oTempDir.resolve("file_with_underscore.txt"));

        // Execute: List directory
        Value oResult = ListDirectory.execute(_oTempDir.toString(), false, false);

        // Verify: Special characters are preserved
        String strResult = oResult.toString();
        assertTrue(strResult.contains("file-with-dash.txt"));
        assertTrue(strResult.contains("file_with_underscore.txt"));
    }

    /**
     * Helper method to mark a file as hidden (cross-platform).
     * On Windows systems, uses DOS file attributes. On Unix, relies on dot prefix.
     */
    private void setHidden(Path path) throws IOException {
        DosFileAttributeView oView = Files.getFileAttributeView(path, DosFileAttributeView.class);
        if (oView != null) {
            oView.setHidden(true);
        }
    }
}
