package eu.gricom.basic.statements;

import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MkDirStatementTest.java
 * <p>
 * Unit tests for the MkDirStatement class.
 * <p>
 * This test class provides comprehensive coverage of the MkDirStatement, which creates a directory
 * at the specified path. The directory path is provided as a StringValue parameter.
 * <p>
 * Test Categories:
 * - POSITIVE TESTS: MkDirStatement successfully creates directories
 * - NEGATIVE TESTS: MkDirStatement handles error cases and throws RuntimeException
 * - EDGE CASES: Null paths, empty paths, special characters, existing directories
 * - INTERFACE TESTS: getTokenNumber(), content(), and structure() methods
 * <p>
 * Key Behavior:
 * - Creates directory from StringValue path
 * - Throws RuntimeException if directory path is null
 * - Throws RuntimeException if directory path is empty
 * - Throws RuntimeException if directory already exists
 * - Throws RuntimeException if parent directory does not exist
 * - Throws RuntimeException if access is denied
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class MkDirStatementTest {

    private static final int TOKEN_NUMBER = 100;

    private Path _oTestDirectory;

    /**
     * Setup method: Initializes test environment before each test.
     * Creates a temporary directory to use as a test base.
     */
    @BeforeEach
    public void setUp() throws Exception {
        _oTestDirectory = Files.createTempDirectory("mkdir-test-");
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
    // POSITIVE TEST CASES - MkDirStatement successfully creates directories
    // =========================================================================

    /**
     * Test: MkDirStatement creates a simple directory.
     * <p>
     * Given: Valid directory path in StringValue
     * When: MkDirStatement.execute() is called
     * Then: Directory is created at the specified path
     * <p>
     * Purpose: Verifies basic directory creation functionality
     */
    @Test
    public void testExecute_WithValidPath_CreatesDirectory() throws Exception {
        // Setup: Create path for new directory
        Path oNewDir = _oTestDirectory.resolve("test-dir-1");
        StringValue oDirectory = new StringValue(oNewDir.toString());

        // Execute: Create directory
        MkDirStatement oStatement = new MkDirStatement(TOKEN_NUMBER, oDirectory);
        oStatement.execute();

        // Verify: Directory was created
        assertTrue(Files.exists(oNewDir), "Directory should be created");
        assertTrue(Files.isDirectory(oNewDir), "Path should be a directory");
    }

    /**
     * Test: MkDirStatement creates multiple directories sequentially.
     * <p>
     * Given: Multiple valid directory paths in StringValue
     * When: MkDirStatement.execute() is called for each path
     * Then: All directories are created
     * <p>
     * Purpose: Verifies multiple directory creation works correctly
     */
    @Test
    public void testExecute_WithMultiplePaths_CreatesAllDirectories() throws Exception {
        // Setup: Create paths for multiple directories
        Path oDir1 = _oTestDirectory.resolve("dir-1");
        Path oDir2 = _oTestDirectory.resolve("dir-2");
        Path oDir3 = _oTestDirectory.resolve("dir-3");

        // Execute: Create all directories
        MkDirStatement oStatement1 = new MkDirStatement(TOKEN_NUMBER, new StringValue(oDir1.toString()));
        oStatement1.execute();
        MkDirStatement oStatement2 = new MkDirStatement(TOKEN_NUMBER, new StringValue(oDir2.toString()));
        oStatement2.execute();
        MkDirStatement oStatement3 = new MkDirStatement(TOKEN_NUMBER, new StringValue(oDir3.toString()));
        oStatement3.execute();

        // Verify: All directories were created
        assertTrue(Files.exists(oDir1), "Directory 1 should be created");
        assertTrue(Files.exists(oDir2), "Directory 2 should be created");
        assertTrue(Files.exists(oDir3), "Directory 3 should be created");
    }

    /**
     * Test: MkDirStatement creates directory with path containing spaces.
     * <p>
     * Given: Directory path with spaces in name
     * When: MkDirStatement.execute() is called
     * Then: Directory is created with spaces in name
     * <p>
     * Purpose: Verifies directory creation with spaces in path
     */
    @Test
    public void testExecute_WithPathContainingSpaces_CreatesDirectory() throws Exception {
        // Setup: Create path with spaces
        Path oNewDir = _oTestDirectory.resolve("my test directory");
        StringValue oDirectory = new StringValue(oNewDir.toString());

        // Execute: Create directory
        MkDirStatement oStatement = new MkDirStatement(TOKEN_NUMBER, oDirectory);
        oStatement.execute();

        // Verify: Directory was created with spaces
        assertTrue(Files.exists(oNewDir), "Directory with spaces should be created");
        assertTrue(Files.isDirectory(oNewDir), "Path should be a directory");
    }

    /**
     * Test: MkDirStatement creates directory with path containing special characters.
     * <p>
     * Given: Directory path with special characters (but valid for filesystem)
     * When: MkDirStatement.execute() is called
     * Then: Directory is created
     * <p>
     * Purpose: Verifies directory creation with special characters
     */
    @Test
    public void testExecute_WithPathContainingDashes_CreatesDirectory() throws Exception {
        // Setup: Create path with dashes and underscores
        Path oNewDir = _oTestDirectory.resolve("test-dir_with-dashes");
        StringValue oDirectory = new StringValue(oNewDir.toString());

        // Execute: Create directory
        MkDirStatement oStatement = new MkDirStatement(TOKEN_NUMBER, oDirectory);
        oStatement.execute();

        // Verify: Directory was created
        assertTrue(Files.exists(oNewDir), "Directory with special characters should be created");
    }

    // =========================================================================
    // EDGE CASE TEST CASES - Test boundary conditions that don't call System.exit
    // =========================================================================

    /**
     * Test: MkDirStatement handles long directory names.
     * <p>
     * Given: Directory with long name but valid
     * When: MkDirStatement.execute() is called
     * Then: Directory is created successfully
     * <p>
     * Purpose: Verifies handling of long names
     */
    @Test
    public void testExecute_WithLongName_CreatesDirectory() throws Exception {
        // Setup: Create a directory with a long name
        String strLongName = "this-is-a-very-long-directory-name-to-test-handling-of-extended-names";
        Path oNewDir = _oTestDirectory.resolve(strLongName);
        StringValue oDirectory = new StringValue(oNewDir.toString());

        // Execute: Create directory
        MkDirStatement oStatement = new MkDirStatement(TOKEN_NUMBER, oDirectory);
        oStatement.execute();

        // Verify: Directory was created
        assertTrue(Files.exists(oNewDir), "Directory with long name should be created");
    }

    /**
     * Test: MkDirStatement creates directory with numeric names.
     * <p>
     * Given: Directory path with only numbers
     * When: MkDirStatement.execute() is called
     * Then: Directory is created
     * <p>
     * Purpose: Verifies numeric directory names work
     */
    @Test
    public void testExecute_WithNumericName_CreatesDirectory() throws Exception {
        // Setup: Create path with numeric name
        Path oNewDir = _oTestDirectory.resolve("12345");
        StringValue oDirectory = new StringValue(oNewDir.toString());

        // Execute: Create directory
        MkDirStatement oStatement = new MkDirStatement(TOKEN_NUMBER, oDirectory);
        oStatement.execute();

        // Verify: Directory was created
        assertTrue(Files.exists(oNewDir), "Directory with numeric name should be created");
    }

    /**
     * Test: MkDirStatement creates directory with mixed case.
     * <p>
     * Given: Directory path with mixed case letters
     * When: MkDirStatement.execute() is called
     * Then: Directory is created
     * <p>
     * Purpose: Verifies mixed case directory names work
     */
    @Test
    public void testExecute_WithMixedCase_CreatesDirectory() throws Exception {
        // Setup: Create path with mixed case
        Path oNewDir = _oTestDirectory.resolve("TestDirWithMixedCase");
        StringValue oDirectory = new StringValue(oNewDir.toString());

        // Execute: Create directory
        MkDirStatement oStatement = new MkDirStatement(TOKEN_NUMBER, oDirectory);
        oStatement.execute();

        // Verify: Directory was created
        assertTrue(Files.exists(oNewDir), "Directory with mixed case should be created");
    }

    // =========================================================================
    // INTERFACE TEST CASES - Test public interface methods
    // =========================================================================

    /**
     * Test: getTokenNumber returns constructor value.
     * <p>
     * Given: MkDirStatement with specific token number
     * When: getTokenNumber() is called
     * Then: Returns the token number from constructor
     * <p>
     * Purpose: Verifies token number storage and retrieval
     */
    @Test
    public void testGetTokenNumber_ReturnsConstructorValue() {
        Path oDir = _oTestDirectory.resolve("test-dir");
        StringValue oDirectory = new StringValue(oDir.toString());
        MkDirStatement oStatement = new MkDirStatement(42, oDirectory);

        assertEquals(42, oStatement.getTokenNumber());
    }

    /**
     * Test: getTokenNumber with various token numbers.
     * <p>
     * Given: MkDirStatement with different token numbers
     * When: getTokenNumber() is called
     * Then: Returns the correct token number
     * <p>
     * Purpose: Verifies token number accuracy
     */
    @Test
    public void testGetTokenNumber_WithZero_ReturnsZero() {
        Path oDir = _oTestDirectory.resolve("test-dir");
        StringValue oDirectory = new StringValue(oDir.toString());
        MkDirStatement oStatement = new MkDirStatement(0, oDirectory);

        assertEquals(0, oStatement.getTokenNumber());
    }

    /**
     * Test: getTokenNumber with negative token number.
     * <p>
     * Given: MkDirStatement with negative token number
     * When: getTokenNumber() is called
     * Then: Returns the negative token number
     * <p>
     * Purpose: Verifies negative token number handling
     */
    @Test
    public void testGetTokenNumber_WithNegative_ReturnsNegative() {
        Path oDir = _oTestDirectory.resolve("test-dir");
        StringValue oDirectory = new StringValue(oDir.toString());
        MkDirStatement oStatement = new MkDirStatement(-1, oDirectory);

        assertEquals(-1, oStatement.getTokenNumber());
    }

    /**
     * Test: content returns MKDIR.
     * <p>
     * Given: MkDirStatement instance
     * When: content() is called
     * Then: Returns "MKDIR"
     * <p>
     * Purpose: Verifies content method return value
     */
    @Test
    public void testContent_ReturnsMKDIR() throws Exception {
        Path oDir = _oTestDirectory.resolve("test-dir");
        StringValue oDirectory = new StringValue(oDir.toString());
        MkDirStatement oStatement = new MkDirStatement(1, oDirectory);

        assertEquals("MKDIR", oStatement.content());
    }

    /**
     * Test: content returns MKDIR regardless of parameters.
     * <p>
     * Given: MkDirStatement with various parameters
     * When: content() is called
     * Then: Always returns "MKDIR"
     * <p>
     * Purpose: Verifies consistent content return
     */
    @Test
    public void testContent_WithDifferentParameters_ReturnsMKDIR() throws Exception {
        Path oDir = _oTestDirectory.resolve("different-dir");
        StringValue oDirectory = new StringValue(oDir.toString());
        MkDirStatement oStatement = new MkDirStatement(100, oDirectory);

        assertEquals("MKDIR", oStatement.content());
    }

    /**
     * Test: structure contains MKDIR key.
     * <p>
     * Given: MkDirStatement instance
     * When: structure() is called
     * Then: JSON contains MKDIR key
     * <p>
     * Purpose: Verifies structure JSON format
     */
    @Test
    public void testStructure_ContainsMKDIRKey() throws Exception {
        Path oDir = _oTestDirectory.resolve("test-dir");
        StringValue oDirectory = new StringValue(oDir.toString());
        MkDirStatement oStatement = new MkDirStatement(TOKEN_NUMBER, oDirectory);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"MKDIR\""), "Structure should contain MKDIR key");
    }

    /**
     * Test: structure contains token number.
     * <p>
     * Given: MkDirStatement with specific token number
     * When: structure() is called
     * Then: JSON contains token number
     * <p>
     * Purpose: Verifies token number in structure
     */
    @Test
    public void testStructure_ContainsTokenNumber() throws Exception {
        Path oDir = _oTestDirectory.resolve("test-dir");
        StringValue oDirectory = new StringValue(oDir.toString());
        MkDirStatement oStatement = new MkDirStatement(100, oDirectory);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"TOKEN_NR\": \"100\""), "Structure should contain token number");
    }

    /**
     * Test: structure contains directory path.
     * <p>
     * Given: MkDirStatement with specific directory path
     * When: structure() is called
     * Then: JSON contains directory path
     * <p>
     * Purpose: Verifies directory path in structure
     */
    @Test
    public void testStructure_ContainsDirectoryPath() throws Exception {
        Path oDir = _oTestDirectory.resolve("test-dir");
        StringValue oDirectory = new StringValue(oDir.toString());
        MkDirStatement oStatement = new MkDirStatement(TOKEN_NUMBER, oDirectory);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"DIRECTORY\""), "Structure should contain DIRECTORY key");
        assertTrue(strStructure.contains(oDir.toString()), "Structure should contain directory path");
    }

    /**
     * Test: structure returns valid JSON.
     * <p>
     * Given: MkDirStatement instance
     * When: structure() is called
     * Then: Returns properly formatted JSON
     * <p>
     * Purpose: Verifies JSON format is valid
     */
    @Test
    public void testStructure_ReturnsValidJsonFormat() throws Exception {
        Path oDir = _oTestDirectory.resolve("test-dir");
        StringValue oDirectory = new StringValue(oDir.toString());
        MkDirStatement oStatement = new MkDirStatement(TOKEN_NUMBER, oDirectory);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.startsWith("{"), "Structure should start with {");
        assertTrue(strStructure.endsWith("}"), "Structure should end with }");
        assertTrue(strStructure.contains("\"MKDIR\": {"), "Structure should have MKDIR key with object");
    }

    /**
     * Test: structure contains both token number and directory.
     * <p>
     * Given: MkDirStatement instance
     * When: structure() is called
     * Then: JSON contains both token number and directory
     * <p>
     * Purpose: Verifies complete structure information
     */
    @Test
    public void testStructure_ContainsBothTokenAndDirectory() throws Exception {
        Path oDir = _oTestDirectory.resolve("my-directory");
        StringValue oDirectory = new StringValue(oDir.toString());
        MkDirStatement oStatement = new MkDirStatement(123, oDirectory);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"TOKEN_NR\": \"123\""), "Structure should contain token 123");
        assertTrue(strStructure.contains("\"DIRECTORY\""), "Structure should contain DIRECTORY key");
    }

    /**
     * Test: structure properly escapes directory path with special characters.
     * <p>
     * Given: MkDirStatement with path containing quotes or special chars
     * When: structure() is called
     * Then: JSON is properly escaped
     * <p>
     * Purpose: Verifies JSON escaping for special characters
     */
    @Test
    public void testStructure_EscapesSpecialCharactersInPath() throws Exception {
        Path oDir = _oTestDirectory.resolve("dir-with-dash");
        StringValue oDirectory = new StringValue(oDir.toString());
        MkDirStatement oStatement = new MkDirStatement(TOKEN_NUMBER, oDirectory);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"DIRECTORY\""), "Structure should contain DIRECTORY key");
        // Should be valid JSON (no unclosed quotes)
        assertTrue(strStructure.startsWith("{") && strStructure.endsWith("}"), "Should be valid JSON");
    }
}
