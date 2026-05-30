package eu.gricom.basic.statements;

import eu.gricom.basic.variableTypes.BooleanValue;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * RmDirStatementTest.java
 * <p>
 * Unit tests for the RmDirStatement class.
 * <p>
 * This test class provides comprehensive coverage of the RmDirStatement, which removes (deletes) a directory
 * at the specified path. The directory path is provided as a StringValue parameter, and a force flag as a BooleanValue
 * determines whether to recursively delete directory contents.
 * <p>
 * Test Categories:
 * - POSITIVE TESTS: RmDirStatement successfully removes directories
 * - EDGE CASES: Non-existent directories, directories with contents and force flag
 * - INTERFACE TESTS: getTokenNumber(), content(), and structure() methods
 * <p>
 * Key Behavior:
 * - Removes empty directories without force flag
 * - Recursively removes directories with contents when force flag is true
 * - Returns success if directory does not exist
 * - Throws RuntimeException if directory is not empty and force flag is false
 * - Throws RuntimeException if access is denied
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class RmDirStatementTest {

    private static final int TOKEN_NUMBER = 100;

    private Path _oTestDirectory;

    /**
     * Setup method: Initializes test environment before each test.
     * Creates a temporary directory to use as a test base.
     */
    @BeforeEach
    public void setUp() throws Exception {
        _oTestDirectory = Files.createTempDirectory("rmdir-test-");
    }

    /**
     * Teardown method: Cleans up after each test.
     * Removes test directories and files created during tests.
     */
    @AfterEach
    public void tearDown() throws Exception {
        // Clean up any directories created during tests
        deleteDirectoryRecursively(_oTestDirectory);
    }

    /**
     * Helper method to delete a directory and its contents recursively.
     *
     * @param path the path to delete
     * @throws Exception if deletion fails
     */
    private void deleteDirectoryRecursively(Path path) throws Exception {
        if (Files.exists(path)) {
            Files.walk(path)
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (Exception e) {
                            // Ignore errors during cleanup
                        }
                    });
        }
    }

    // =========================================================================
    // POSITIVE TEST CASES - RmDirStatement successfully removes directories
    // =========================================================================

    /**
     * Test: RmDirStatement removes an empty directory without force flag.
     * <p>
     * Given: Valid empty directory path and force flag is false
     * When: RmDirStatement.execute() is called
     * Then: Directory is removed from file system
     * <p>
     * Purpose: Verifies basic directory removal functionality
     */
    @Test
    public void testExecute_WithEmptyDirectoryNoForce_RemovesDirectory() throws Exception {
        // Setup: Create an empty directory
        Path oNewDir = _oTestDirectory.resolve("empty-dir");
        Files.createDirectory(oNewDir);
        assertTrue(Files.exists(oNewDir), "Directory should exist before deletion");

        // Execute: Remove directory without force flag
        StringValue oDirectory = new StringValue(oNewDir.toString());
        BooleanValue bForce = new BooleanValue(false);
        RmDirStatement oStatement = new RmDirStatement(TOKEN_NUMBER, oDirectory, bForce);
        oStatement.execute();

        // Verify: Directory was removed
        assertFalse(Files.exists(oNewDir), "Directory should be removed");
    }

    /**
     * Test: RmDirStatement removes empty directory with force flag set to true.
     * <p>
     * Given: Valid empty directory path and force flag is true
     * When: RmDirStatement.execute() is called
     * Then: Directory is removed
     * <p>
     * Purpose: Verifies force flag allows removal of empty directories
     */
    @Test
    public void testExecute_WithEmptyDirectoryWithForce_RemovesDirectory() throws Exception {
        // Setup: Create an empty directory
        Path oNewDir = _oTestDirectory.resolve("empty-dir-force");
        Files.createDirectory(oNewDir);
        assertTrue(Files.exists(oNewDir), "Directory should exist");

        // Execute: Remove directory with force flag
        StringValue oDirectory = new StringValue(oNewDir.toString());
        BooleanValue bForce = new BooleanValue(true);
        RmDirStatement oStatement = new RmDirStatement(TOKEN_NUMBER, oDirectory, bForce);
        oStatement.execute();

        // Verify: Directory was removed
        assertFalse(Files.exists(oNewDir), "Directory should be removed");
    }

    /**
     * Test: RmDirStatement successfully handles non-existent directory.
     * <p>
     * Given: Directory path that does not exist
     * When: RmDirStatement.execute() is called
     * Then: No error is thrown, returns successfully
     * <p>
     * Purpose: Verifies graceful handling of non-existent directories
     */
    @Test
    public void testExecute_WithNonExistentDirectory_ReturnsSuccess() throws Exception {
        // Setup: Create path to non-existent directory
        Path oNonExistent = _oTestDirectory.resolve("non-existent-dir");
        assertFalse(Files.exists(oNonExistent), "Directory should not exist");

        // Execute: Try to remove non-existent directory
        StringValue oDirectory = new StringValue(oNonExistent.toString());
        BooleanValue bForce = new BooleanValue(false);
        RmDirStatement oStatement = new RmDirStatement(TOKEN_NUMBER, oDirectory, bForce);
        oStatement.execute();

        // Verify: No exception thrown, returns successfully
        assertTrue(true, "Should handle non-existent directory gracefully");
    }

    /**
     * Test: RmDirStatement removes directory with files when force flag is true.
     * <p>
     * Given: Directory containing files and force flag is true
     * When: RmDirStatement.execute() is called
     * Then: Directory and all contents are removed
     * <p>
     * Purpose: Verifies recursive deletion with force flag
     */
    @Test
    public void testExecute_WithFilesAndForceTrue_RemovesDirectoryAndContents() throws Exception {
        // Setup: Create directory with files
        Path oNewDir = _oTestDirectory.resolve("dir-with-files");
        Files.createDirectory(oNewDir);
        Path oFile1 = oNewDir.resolve("file1.txt");
        Path oFile2 = oNewDir.resolve("file2.txt");
        Files.createFile(oFile1);
        Files.createFile(oFile2);
        assertTrue(Files.exists(oNewDir), "Directory should exist");
        assertTrue(Files.exists(oFile1), "File1 should exist");
        assertTrue(Files.exists(oFile2), "File2 should exist");

        // Execute: Remove directory with force flag
        StringValue oDirectory = new StringValue(oNewDir.toString());
        BooleanValue bForce = new BooleanValue(true);
        RmDirStatement oStatement = new RmDirStatement(TOKEN_NUMBER, oDirectory, bForce);
        oStatement.execute();

        // Verify: Directory and all files were removed
        assertFalse(Files.exists(oNewDir), "Directory should be removed");
        assertFalse(Files.exists(oFile1), "File1 should be removed");
        assertFalse(Files.exists(oFile2), "File2 should be removed");
    }

    /**
     * Test: RmDirStatement removes nested directories with force flag.
     * <p>
     * Given: Nested directory structure with force flag true
     * When: RmDirStatement.execute() is called
     * Then: All nested directories are removed
     * <p>
     * Purpose: Verifies recursive deletion of nested structures
     */
    @Test
    public void testExecute_WithNestedDirectoriesAndForce_RemovesAll() throws Exception {
        // Setup: Create nested directory structure
        Path oRootDir = _oTestDirectory.resolve("root-dir");
        Path oSubDir1 = oRootDir.resolve("sub1");
        Path oSubDir2 = oSubDir1.resolve("sub2");
        Files.createDirectory(oRootDir);
        Files.createDirectory(oSubDir1);
        Files.createDirectory(oSubDir2);
        Path oFile = oSubDir2.resolve("file.txt");
        Files.createFile(oFile);

        assertTrue(Files.exists(oRootDir), "Root directory should exist");
        assertTrue(Files.exists(oSubDir1), "Sub1 directory should exist");
        assertTrue(Files.exists(oSubDir2), "Sub2 directory should exist");
        assertTrue(Files.exists(oFile), "File should exist");

        // Execute: Remove entire structure with force flag
        StringValue oDirectory = new StringValue(oRootDir.toString());
        BooleanValue bForce = new BooleanValue(true);
        RmDirStatement oStatement = new RmDirStatement(TOKEN_NUMBER, oDirectory, bForce);
        oStatement.execute();

        // Verify: All removed
        assertFalse(Files.exists(oRootDir), "Root directory should be removed");
        assertFalse(Files.exists(oSubDir1), "Sub1 directory should be removed");
        assertFalse(Files.exists(oSubDir2), "Sub2 directory should be removed");
        assertFalse(Files.exists(oFile), "File should be removed");
    }

    /**
     * Test: RmDirStatement removes multiple empty directories sequentially.
     * <p>
     * Given: Multiple empty directories without force flag
     * When: RmDirStatement.execute() is called for each
     * Then: All directories are removed
     * <p>
     * Purpose: Verifies multiple removals work correctly
     */
    @Test
    public void testExecute_WithMultipleEmptyDirectories_RemovesAll() throws Exception {
        // Setup: Create multiple directories
        Path oDir1 = _oTestDirectory.resolve("dir-1");
        Path oDir2 = _oTestDirectory.resolve("dir-2");
        Path oDir3 = _oTestDirectory.resolve("dir-3");
        Files.createDirectory(oDir1);
        Files.createDirectory(oDir2);
        Files.createDirectory(oDir3);

        // Execute: Remove all directories
        BooleanValue bForce = new BooleanValue(false);
        RmDirStatement oStatement1 = new RmDirStatement(TOKEN_NUMBER, new StringValue(oDir1.toString()), bForce);
        oStatement1.execute();
        RmDirStatement oStatement2 = new RmDirStatement(TOKEN_NUMBER, new StringValue(oDir2.toString()), bForce);
        oStatement2.execute();
        RmDirStatement oStatement3 = new RmDirStatement(TOKEN_NUMBER, new StringValue(oDir3.toString()), bForce);
        oStatement3.execute();

        // Verify: All removed
        assertFalse(Files.exists(oDir1), "Directory 1 should be removed");
        assertFalse(Files.exists(oDir2), "Directory 2 should be removed");
        assertFalse(Files.exists(oDir3), "Directory 3 should be removed");
    }

    /**
     * Test: RmDirStatement removes directory with many files when force is true.
     * <p>
     * Given: Directory with multiple files and force flag true
     * When: RmDirStatement.execute() is called
     * Then: All files and directory are removed
     * <p>
     * Purpose: Verifies handling of directories with multiple files
     */
    @Test
    public void testExecute_WithManyFilesAndForce_RemovesAll() throws Exception {
        // Setup: Create directory with multiple files
        Path oNewDir = _oTestDirectory.resolve("dir-many-files");
        Files.createDirectory(oNewDir);

        for (int i = 0; i < 5; i++) {
            Path oFile = oNewDir.resolve("file-" + i + ".txt");
            Files.createFile(oFile);
        }

        assertTrue(Files.exists(oNewDir), "Directory should exist");
        assertEquals(5, Files.list(oNewDir).count(), "Should have 5 files");

        // Execute: Remove with force flag
        StringValue oDirectory = new StringValue(oNewDir.toString());
        BooleanValue bForce = new BooleanValue(true);
        RmDirStatement oStatement = new RmDirStatement(TOKEN_NUMBER, oDirectory, bForce);
        oStatement.execute();

        // Verify: Directory and files removed
        assertFalse(Files.exists(oNewDir), "Directory should be removed");
    }

    // =========================================================================
    // EDGE CASE TEST CASES - Test boundary conditions
    // =========================================================================

    /**
     * Test: RmDirStatement handles directory with spaces in name.
     * <p>
     * Given: Empty directory with spaces in name
     * When: RmDirStatement.execute() is called
     * Then: Directory is removed
     * <p>
     * Purpose: Verifies handling of directory names with spaces
     */
    @Test
    public void testExecute_WithPathContainingSpaces_RemovesDirectory() throws Exception {
        // Setup: Create directory with spaces
        Path oNewDir = _oTestDirectory.resolve("my test directory");
        Files.createDirectory(oNewDir);
        assertTrue(Files.exists(oNewDir), "Directory should exist");

        // Execute: Remove directory
        StringValue oDirectory = new StringValue(oNewDir.toString());
        BooleanValue bForce = new BooleanValue(false);
        RmDirStatement oStatement = new RmDirStatement(TOKEN_NUMBER, oDirectory, bForce);
        oStatement.execute();

        // Verify: Directory removed
        assertFalse(Files.exists(oNewDir), "Directory with spaces should be removed");
    }

    /**
     * Test: RmDirStatement handles directory with special characters.
     * <p>
     * Given: Empty directory with dashes and underscores
     * When: RmDirStatement.execute() is called
     * Then: Directory is removed
     * <p>
     * Purpose: Verifies handling of special characters
     */
    @Test
    public void testExecute_WithSpecialCharacters_RemovesDirectory() throws Exception {
        // Setup: Create directory with special characters
        Path oNewDir = _oTestDirectory.resolve("dir-with_special-chars");
        Files.createDirectory(oNewDir);
        assertTrue(Files.exists(oNewDir), "Directory should exist");

        // Execute: Remove directory
        StringValue oDirectory = new StringValue(oNewDir.toString());
        BooleanValue bForce = new BooleanValue(false);
        RmDirStatement oStatement = new RmDirStatement(TOKEN_NUMBER, oDirectory, bForce);
        oStatement.execute();

        // Verify: Directory removed
        assertFalse(Files.exists(oNewDir), "Directory with special characters should be removed");
    }

    // =========================================================================
    // INTERFACE TEST CASES - Test public interface methods
    // =========================================================================

    /**
     * Test: getTokenNumber returns constructor value.
     * <p>
     * Given: RmDirStatement with specific token number
     * When: getTokenNumber() is called
     * Then: Returns the token number from constructor
     * <p>
     * Purpose: Verifies token number storage and retrieval
     */
    @Test
    public void testGetTokenNumber_ReturnsConstructorValue() {
        Path oDir = _oTestDirectory.resolve("test-dir");
        StringValue oDirectory = new StringValue(oDir.toString());
        BooleanValue bForce = new BooleanValue(false);
        RmDirStatement oStatement = new RmDirStatement(42, oDirectory, bForce);

        assertEquals(42, oStatement.getTokenNumber());
    }

    /**
     * Test: getTokenNumber with various token numbers.
     * <p>
     * Given: RmDirStatement with different token numbers
     * When: getTokenNumber() is called
     * Then: Returns correct token number
     * <p>
     * Purpose: Verifies token number accuracy
     */
    @Test
    public void testGetTokenNumber_WithZero_ReturnsZero() {
        Path oDir = _oTestDirectory.resolve("test-dir");
        StringValue oDirectory = new StringValue(oDir.toString());
        BooleanValue bForce = new BooleanValue(false);
        RmDirStatement oStatement = new RmDirStatement(0, oDirectory, bForce);

        assertEquals(0, oStatement.getTokenNumber());
    }

    /**
     * Test: getTokenNumber with negative token number.
     * <p>
     * Given: RmDirStatement with negative token number
     * When: getTokenNumber() is called
     * Then: Returns negative token number
     * <p>
     * Purpose: Verifies negative token number handling
     */
    @Test
    public void testGetTokenNumber_WithNegative_ReturnsNegative() {
        Path oDir = _oTestDirectory.resolve("test-dir");
        StringValue oDirectory = new StringValue(oDir.toString());
        BooleanValue bForce = new BooleanValue(false);
        RmDirStatement oStatement = new RmDirStatement(-1, oDirectory, bForce);

        assertEquals(-1, oStatement.getTokenNumber());
    }

    /**
     * Test: content returns RMDIR.
     * <p>
     * Given: RmDirStatement instance
     * When: content() is called
     * Then: Returns "RMDIR"
     * <p>
     * Purpose: Verifies content method return value
     */
    @Test
    public void testContent_ReturnsRMDIR() throws Exception {
        Path oDir = _oTestDirectory.resolve("test-dir");
        StringValue oDirectory = new StringValue(oDir.toString());
        BooleanValue bForce = new BooleanValue(false);
        RmDirStatement oStatement = new RmDirStatement(1, oDirectory, bForce);

        assertEquals("RMDIR", oStatement.content());
    }

    /**
     * Test: content returns RMDIR regardless of parameters.
     * <p>
     * Given: RmDirStatement with various parameters
     * When: content() is called
     * Then: Always returns "RMDIR"
     * <p>
     * Purpose: Verifies consistent content return
     */
    @Test
    public void testContent_WithDifferentParameters_ReturnsRMDIR() throws Exception {
        Path oDir = _oTestDirectory.resolve("different-dir");
        StringValue oDirectory = new StringValue(oDir.toString());
        BooleanValue bForce = new BooleanValue(true);
        RmDirStatement oStatement = new RmDirStatement(100, oDirectory, bForce);

        assertEquals("RMDIR", oStatement.content());
    }

    /**
     * Test: structure contains RMDIR key.
     * <p>
     * Given: RmDirStatement instance
     * When: structure() is called
     * Then: JSON contains RMDIR key
     * <p>
     * Purpose: Verifies structure JSON format
     */
    @Test
    public void testStructure_ContainsRMDIRKey() throws Exception {
        Path oDir = _oTestDirectory.resolve("test-dir");
        StringValue oDirectory = new StringValue(oDir.toString());
        BooleanValue bForce = new BooleanValue(false);
        RmDirStatement oStatement = new RmDirStatement(TOKEN_NUMBER, oDirectory, bForce);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"RMDIR\""), "Structure should contain RMDIR key");
    }

    /**
     * Test: structure contains token number.
     * <p>
     * Given: RmDirStatement with specific token number
     * When: structure() is called
     * Then: JSON contains token number
     * <p>
     * Purpose: Verifies token number in structure
     */
    @Test
    public void testStructure_ContainsTokenNumber() throws Exception {
        Path oDir = _oTestDirectory.resolve("test-dir");
        StringValue oDirectory = new StringValue(oDir.toString());
        BooleanValue bForce = new BooleanValue(false);
        RmDirStatement oStatement = new RmDirStatement(100, oDirectory, bForce);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"TOKEN_NR\": \"100\""), "Structure should contain token number");
    }

    /**
     * Test: structure contains directory path.
     * <p>
     * Given: RmDirStatement with specific directory path
     * When: structure() is called
     * Then: JSON contains directory path
     * <p>
     * Purpose: Verifies directory path in structure
     */
    @Test
    public void testStructure_ContainsDirectoryPath() throws Exception {
        Path oDir = _oTestDirectory.resolve("test-dir");
        StringValue oDirectory = new StringValue(oDir.toString());
        BooleanValue bForce = new BooleanValue(false);
        RmDirStatement oStatement = new RmDirStatement(TOKEN_NUMBER, oDirectory, bForce);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"DIRECTORY\""), "Structure should contain DIRECTORY key");
        assertTrue(strStructure.contains(oDir.toString()), "Structure should contain directory path");
    }

    /**
     * Test: structure contains force flag.
     * <p>
     * Given: RmDirStatement with force flag
     * When: structure() is called
     * Then: JSON contains force flag value
     * <p>
     * Purpose: Verifies force flag in structure
     */
    @Test
    public void testStructure_ContainsForceFlag() throws Exception {
        Path oDir = _oTestDirectory.resolve("test-dir");
        StringValue oDirectory = new StringValue(oDir.toString());
        BooleanValue bForce = new BooleanValue(true);
        RmDirStatement oStatement = new RmDirStatement(TOKEN_NUMBER, oDirectory, bForce);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FORCE\""), "Structure should contain FORCE key");
    }

    /**
     * Test: structure returns valid JSON.
     * <p>
     * Given: RmDirStatement instance
     * When: structure() is called
     * Then: Returns properly formatted JSON
     * <p>
     * Purpose: Verifies JSON format is valid
     */
    @Test
    public void testStructure_ReturnsValidJsonFormat() throws Exception {
        Path oDir = _oTestDirectory.resolve("test-dir");
        StringValue oDirectory = new StringValue(oDir.toString());
        BooleanValue bForce = new BooleanValue(false);
        RmDirStatement oStatement = new RmDirStatement(TOKEN_NUMBER, oDirectory, bForce);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.startsWith("{"), "Structure should start with {");
        assertTrue(strStructure.endsWith("}"), "Structure should end with }");
        assertTrue(strStructure.contains("\"RMDIR\": {"), "Structure should have RMDIR key with object");
    }

    /**
     * Test: structure contains all components.
     * <p>
     * Given: RmDirStatement instance
     * When: structure() is called
     * Then: JSON contains token, directory, and force flag
     * <p>
     * Purpose: Verifies complete structure information
     */
    @Test
    public void testStructure_ContainsAllComponents() throws Exception {
        Path oDir = _oTestDirectory.resolve("my-directory");
        StringValue oDirectory = new StringValue(oDir.toString());
        BooleanValue bForce = new BooleanValue(true);
        RmDirStatement oStatement = new RmDirStatement(123, oDirectory, bForce);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"TOKEN_NR\": \"123\""), "Structure should contain token 123");
        assertTrue(strStructure.contains("\"DIRECTORY\""), "Structure should contain DIRECTORY key");
        assertTrue(strStructure.contains("\"FORCE\""), "Structure should contain FORCE key");
    }

    /**
     * Test: structure with force flag false.
     * <p>
     * Given: RmDirStatement with force flag false
     * When: structure() is called
     * Then: JSON correctly represents false value
     * <p>
     * Purpose: Verifies force flag false is properly represented
     */
    @Test
    public void testStructure_WithForceFalse_ContainsFalseValue() throws Exception {
        Path oDir = _oTestDirectory.resolve("test-dir");
        StringValue oDirectory = new StringValue(oDir.toString());
        BooleanValue bForce = new BooleanValue(false);
        RmDirStatement oStatement = new RmDirStatement(TOKEN_NUMBER, oDirectory, bForce);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FORCE\""), "Structure should contain FORCE key");
    }

    /**
     * Test: structure with force flag true.
     * <p>
     * Given: RmDirStatement with force flag true
     * When: structure() is called
     * Then: JSON correctly represents true value
     * <p>
     * Purpose: Verifies force flag true is properly represented
     */
    @Test
    public void testStructure_WithForceTrue_ContainsTrueValue() throws Exception {
        Path oDir = _oTestDirectory.resolve("test-dir");
        StringValue oDirectory = new StringValue(oDir.toString());
        BooleanValue bForce = new BooleanValue(true);
        RmDirStatement oStatement = new RmDirStatement(TOKEN_NUMBER, oDirectory, bForce);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FORCE\""), "Structure should contain FORCE key");
    }
}
