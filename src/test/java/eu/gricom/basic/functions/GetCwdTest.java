package eu.gricom.basic.functions;

import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.statements.ChDirStatement;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * GetCwdTest.java
 * <p>
 * Unit tests for the GetCwd BASIC function.
 * <p>
 * This test class provides comprehensive coverage of the GetCwd function, which retrieves the current
 * working directory from the FileManager. The function returns the directory path that is used as the
 * base path for relative file operations. GetCwd takes no parameters and always returns a StringValue.
 * <p>
 * Test Categories:
 * - POSITIVE TESTS: GetCwd retrieves various directory values
 * - CONSISTENCY TESTS: GetCwd returns consistent results
 * - INTEGRATION TESTS: GetCwd works correctly with ChDir and FileManager
 * - EDGE CASES: Empty strings, special paths, and state verification
 * <p>
 * Key Behavior:
 * - Returns StringValue containing the current working directory
 * - Takes no parameters
 * - Never returns null
 * - Returns empty string if no directory has been set
 * - Does not modify any state
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class GetCwdTest {

    private FileManager _oFileManager;

    /**
     * Setup method: Initializes test environment before each test.
     * Resets FileManager to default state.
     */
    @BeforeEach
    public void setUp() {
        _oFileManager = new FileManager();
        // Reset to empty directory
        _oFileManager.setCurrentDirectory("");
    }

    // =========================================================================
    // POSITIVE TEST CASES - GetCwd retrieves directory values
    // =========================================================================

    /**
     * Test: GetCwd with default empty directory.
     * <p>
     * Given: FileManager starts with empty directory
     * When: GetCwd.execute() is called
     * Then: Returns StringValue containing empty string
     * <p>
     * Purpose: Verifies initial state returns empty string
     */
    @Test
    public void testGetCwd_WithDefaultEmptyDirectory_ReturnsEmptyString() {
        // Setup: Ensure default empty directory
        _oFileManager.setCurrentDirectory("");

        // Execute: Call GetCwd
        Value oResult = GetCwd.execute();

        // Verify: Result is empty StringValue
        assertNotNull(oResult);
        assertTrue(oResult instanceof StringValue);
        assertEquals("", oResult.toString());
    }

    /**
     * Test: GetCwd with absolute directory path.
     * <p>
     * Given: Current directory is set to an absolute path
     * When: GetCwd.execute() is called
     * Then: Returns the absolute path
     * <p>
     * Purpose: Verifies absolute paths are returned correctly
     */
    @Test
    public void testGetCwd_WithAbsolutePath_ReturnsAbsolutePath() {
        // Setup: Set absolute directory
        String strAbsolutePath = "/tmp/";
        _oFileManager.setCurrentDirectory(strAbsolutePath);

        // Execute: Call GetCwd
        Value oResult = GetCwd.execute();

        // Verify: Result contains the absolute path
        assertNotNull(oResult);
        assertEquals(strAbsolutePath, oResult.toString());
    }

    /**
     * Test: GetCwd with relative directory path.
     * <p>
     * Given: Current directory is set to a relative path
     * When: GetCwd.execute() is called
     * Then: Returns the relative path
     * <p>
     * Purpose: Verifies relative paths are returned correctly
     */
    @Test
    public void testGetCwd_WithRelativePath_ReturnsRelativePath() {
        // Setup: Set relative directory
        String strRelativePath = "./mydir/";
        _oFileManager.setCurrentDirectory(strRelativePath);

        // Execute: Call GetCwd
        Value oResult = GetCwd.execute();

        // Verify: Result contains the relative path
        assertNotNull(oResult);
        assertEquals(strRelativePath, oResult.toString());
    }

    /**
     * Test: GetCwd with complex path containing special characters.
     * <p>
     * Given: Current directory path contains spaces and special characters
     * When: GetCwd.execute() is called
     * Then: Returns the exact path with all characters preserved
     * <p>
     * Purpose: Verifies paths with special characters are handled correctly
     */
    @Test
    public void testGetCwd_WithPathContainingSpaces_PreservesExactPath() {
        // Setup: Set path with spaces
        String strPathWithSpaces = "/home/user/my documents/";
        _oFileManager.setCurrentDirectory(strPathWithSpaces);

        // Execute: Call GetCwd
        Value oResult = GetCwd.execute();

        // Verify: Path with spaces is preserved exactly
        assertEquals(strPathWithSpaces, oResult.toString());
    }

    // =========================================================================
    // CONSISTENCY TEST CASES - GetCwd consistency and state preservation
    // =========================================================================

    /**
     * Test: GetCwd returns consistent results across multiple calls.
     * <p>
     * Given: Current directory is set to a known path
     * When: GetCwd.execute() is called multiple times
     * Then: All calls return identical results
     * <p>
     * Purpose: Verifies deterministic behavior
     */
    @Test
    public void testGetCwd_WithMultipleCalls_ReturnsConsistentResults() {
        // Setup: Set directory
        String strDirectory = "/home/user/";
        _oFileManager.setCurrentDirectory(strDirectory);

        // Execute: Call GetCwd multiple times
        Value oResult1 = GetCwd.execute();
        Value oResult2 = GetCwd.execute();
        Value oResult3 = GetCwd.execute();

        // Verify: All results are identical
        assertEquals(oResult1.toString(), oResult2.toString());
        assertEquals(oResult2.toString(), oResult3.toString());
        assertEquals(strDirectory, oResult1.toString());
    }

    /**
     * Test: GetCwd returns current value after directory change.
     * <p>
     * Given: Directory is changed multiple times
     * When: GetCwd.execute() is called after each change
     * Then: Returns the latest directory value
     * <p>
     * Purpose: Verifies GetCwd reflects current directory state
     */
    @Test
    public void testGetCwd_AfterDirectoryChange_ReturnsLatestDirectory() {
        // Setup: Change directory multiple times
        String strDir1 = "/home/";
        String strDir2 = "/tmp/";
        String strDir3 = "/var/";

        _oFileManager.setCurrentDirectory(strDir1);
        Value oResult1 = GetCwd.execute();
        assertEquals(strDir1, oResult1.toString());

        _oFileManager.setCurrentDirectory(strDir2);
        Value oResult2 = GetCwd.execute();
        assertEquals(strDir2, oResult2.toString());

        _oFileManager.setCurrentDirectory(strDir3);
        Value oResult3 = GetCwd.execute();
        assertEquals(strDir3, oResult3.toString());
    }

    // =========================================================================
    // INTEGRATION TEST CASES - GetCwd integration with other functions
    // =========================================================================

    /**
     * Test: GetCwd with ChDir statement.
     * <p>
     * Given: ChDir is used to change directory
     * When: GetCwd.execute() is called
     * Then: Returns the directory set by ChDir
     * <p>
     * Purpose: Verifies GetCwd correctly reflects ChDir changes
     */
    @Test
    public void testGetCwd_AfterChdir_ReturnsChangedDirectory() throws Exception {
        // Setup: Use ChDir to change directory
        String strNewDirectory = "/tmp/";
        ChDirStatement oStatement = new ChDirStatement(10, new StringValue(strNewDirectory));
        oStatement.execute();

        // Execute: Call GetCwd
        Value oResult = GetCwd.execute();

        // Verify: GetCwd returns the directory set by ChDir
        assertEquals(strNewDirectory, oResult.toString());
    }

    /**
     * Test: GetCwd returns correct directory for file operations.
     * <p>
     * Given: Directory is set and files are referenced
     * When: GetCwd.execute() is called
     * Then: Returns directory used for relative file paths
     * <p>
     * Purpose: Verifies GetCwd returns the same directory used by FileManager
     */
    @Test
    public void testGetCwd_ConsistentWithFileManagerDirectory_ReturnsExpectedPath() {
        // Setup: Set directory in FileManager
        String strDirectory = "/home/user/documents/";
        _oFileManager.setCurrentDirectory(strDirectory);

        // Execute: Get directory via GetCwd and FileManager
        Value oGetCwdResult = GetCwd.execute();
        String strFileManagerDirectory = _oFileManager.getCurrentDirectory();

        // Verify: Both return the same directory
        assertEquals(strFileManagerDirectory, oGetCwdResult.toString());
    }

    // =========================================================================
    // TYPE AND FORMAT TEST CASES - Return type and format verification
    // =========================================================================

    /**
     * Test: GetCwd always returns StringValue type.
     * <p>
     * Given: GetCwd is called with various directory states
     * When: The return type is checked
     * Then: Always returns a StringValue instance
     * <p>
     * Purpose: Verifies correct return type for BASIC integration
     */
    @Test
    public void testGetCwd_AlwaysReturnsStringValueType() {
        // Execute: Call GetCwd with different directory states
        _oFileManager.setCurrentDirectory("");
        Value oEmptyResult = GetCwd.execute();

        _oFileManager.setCurrentDirectory("/tmp/");
        Value oWithPathResult = GetCwd.execute();

        // Verify: Both are StringValue instances
        assertTrue(oEmptyResult instanceof StringValue);
        assertTrue(oWithPathResult instanceof StringValue);
        assertTrue(oEmptyResult instanceof Value);
        assertTrue(oWithPathResult instanceof Value);
    }

    /**
     * Test: GetCwd never returns null.
     * <p>
     * Given: GetCwd is called regardless of state
     * When: The result is checked for null
     * Then: Result is never null
     * <p>
     * Purpose: Verifies safe usage in BASIC programs
     */
    @Test
    public void testGetCwd_NeverReturnsNull() {
        // Execute: Call GetCwd with various states
        _oFileManager.setCurrentDirectory("");
        Value oEmptyResult = GetCwd.execute();

        _oFileManager.setCurrentDirectory("/");
        Value oRootResult = GetCwd.execute();

        _oFileManager.setCurrentDirectory("/a/very/long/path/");
        Value oLongPathResult = GetCwd.execute();

        // Verify: All results are non-null
        assertNotNull(oEmptyResult);
        assertNotNull(oRootResult);
        assertNotNull(oLongPathResult);
    }

    // =========================================================================
    // EDGE CASE TEST CASES - Special scenarios and boundary conditions
    // =========================================================================

    /**
     * Test: GetCwd with single slash (root directory).
     * <p>
     * Given: Current directory is set to root "/"
     * When: GetCwd.execute() is called
     * Then: Returns "/"
     * <p>
     * Purpose: Verifies root directory is handled correctly
     */
    @Test
    public void testGetCwd_WithRootDirectory_ReturnsRootPath() {
        // Setup: Set to root directory
        String strRoot = "/";
        _oFileManager.setCurrentDirectory(strRoot);

        // Execute: Call GetCwd
        Value oResult = GetCwd.execute();

        // Verify: Returns root path
        assertEquals(strRoot, oResult.toString());
    }

    /**
     * Test: GetCwd with dot directory (current directory notation).
     * <p>
     * Given: Current directory is set to "./"
     * When: GetCwd.execute() is called
     * Then: Returns "./"
     * <p>
     * Purpose: Verifies relative current directory notation
     */
    @Test
    public void testGetCwd_WithCurrentDirectoryNotation_ReturnsDotPath() {
        // Setup: Set to current directory notation
        String strCurrentDir = "./";
        _oFileManager.setCurrentDirectory(strCurrentDir);

        // Execute: Call GetCwd
        Value oResult = GetCwd.execute();

        // Verify: Returns "./"
        assertEquals(strCurrentDir, oResult.toString());
    }

    /**
     * Test: GetCwd with parent directory notation.
     * <p>
     * Given: Current directory is set to "../"
     * When: GetCwd.execute() is called
     * Then: Returns "../"
     * <p>
     * Purpose: Verifies parent directory notation
     */
    @Test
    public void testGetCwd_WithParentDirectoryNotation_ReturnsDotDotPath() {
        // Setup: Set to parent directory notation
        String strParentDir = "../";
        _oFileManager.setCurrentDirectory(strParentDir);

        // Execute: Call GetCwd
        Value oResult = GetCwd.execute();

        // Verify: Returns "../"
        assertEquals(strParentDir, oResult.toString());
    }

    /**
     * Test: GetCwd returns string without null characters.
     * <p>
     * Given: GetCwd is called
     * When: Result string is examined
     * Then: Contains no null characters or unexpected bytes
     * <p>
     * Purpose: Verifies string integrity
     */
    @Test
    public void testGetCwd_ResultStringIsValid_NoNullCharacters() {
        // Setup: Set directory
        _oFileManager.setCurrentDirectory("/home/user/");

        // Execute: Call GetCwd
        String strResult = GetCwd.execute().toString();

        // Verify: String is valid with no null characters
        assertNotNull(strResult);
        assertTrue(!strResult.contains("\0"));
        assertTrue(strResult.length() > 0);
    }

    /**
     * Test: GetCwd handles reset to empty directory.
     * <p>
     * Given: Directory is changed and then reset
     * When: GetCwd.execute() is called after reset
     * Then: Returns empty string
     * <p>
     * Purpose: Verifies directory reset functionality
     */
    @Test
    public void testGetCwd_AfterResetToEmpty_ReturnsEmptyString() {
        // Setup: Set directory then reset
        _oFileManager.setCurrentDirectory("/tmp/");
        Value oBeforeReset = GetCwd.execute();
        assertEquals("/tmp/", oBeforeReset.toString());

        // Reset to empty
        _oFileManager.setCurrentDirectory("");
        Value oAfterReset = GetCwd.execute();

        // Verify: Returns empty string
        assertEquals("", oAfterReset.toString());
    }

    /**
     * Test: GetCwd with Windows-style path.
     * <p>
     * Given: Directory is set with Windows path separators
     * When: GetCwd.execute() is called
     * Then: Returns path exactly as stored
     * <p>
     * Purpose: Verifies cross-platform path handling
     */
    @Test
    public void testGetCwd_WithWindowsStylePath_PreservesPath() {
        // Setup: Set Windows-style path
        String strWindowsPath = "C:\\Users\\Documents\\";
        _oFileManager.setCurrentDirectory(strWindowsPath);

        // Execute: Call GetCwd
        Value oResult = GetCwd.execute();

        // Verify: Path is preserved exactly
        assertEquals(strWindowsPath, oResult.toString());
    }
}
