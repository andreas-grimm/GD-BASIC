package eu.gricom.basic.statements;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.memoryManager.FileOpenType;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FRenameStatementTest.java
 * <p>
 * Unit tests for the FRenameStatement class.
 * <p>
 * This test class provides comprehensive coverage of the FRenameStatement, which renames a file
 * that is registered in the FileManager. The operation closes the file, renames it in the file system,
 * and then re-registers it with the same file ID but new name.
 * <p>
 * Test Categories:
 * - POSITIVE TESTS: FRenameStatement successfully renames files
 * - NEGATIVE TESTS: FRenameStatement handles error conditions
 * - EDGE CASES: Empty names, non-existent files, special characters
 * - INTERFACE TESTS: getTokenNumber(), content(), and structure() methods
 * <p>
 * Key Behavior:
 * - Closes file before renaming (keeping file on disk)
 * - Renames file in file system
 * - Re-registers file with same ID but new name
 * - Throws RuntimeException on errors (cannot continue)
 * - File ID continues to reference renamed file after operation
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FRenameStatementTest {

    private static final int FILE_ID = 801;
    private static final int TOKEN_NUMBER = 100;

    private Path _oSourceFile;
    private Path _oRenamedFile;
    private FileManager _oFileManager;

    /**
     * Setup method: Initializes test environment before each test.
     * Creates temporary files and FileManager instance.
     */
    @BeforeEach
    public void setUp() throws Exception {
        _oSourceFile = Files.createTempFile("frename-source-", ".txt");
        _oRenamedFile = null;
        _oFileManager = new FileManager();
    }

    /**
     * Teardown method: Cleans up after each test.
     * Closes files and deletes temporary files if they still exist.
     */
    @AfterEach
    public void tearDown() throws Exception {
        _oFileManager.closeFile(FILE_ID, false);
        Files.deleteIfExists(_oSourceFile);
        if (_oRenamedFile != null) {
            Files.deleteIfExists(_oRenamedFile);
            // Clean up parent directory if it's empty (created during test)
            try {
                Files.deleteIfExists(_oRenamedFile.getParent());
            } catch (Exception e) {
                // Ignore if directory is not empty or cannot be deleted
            }
        }
    }

    // =========================================================================
    // POSITIVE TEST CASES - FRenameStatement successfully renames files
    // =========================================================================

    /**
     * Test: FRenameStatement renames existing file.
     * <p>
     * Given: File exists and is registered in FileManager
     * When: FRenameStatement.execute() is called with new name
     * Then: File is renamed in file system and re-registered in FileManager
     * <p>
     * Purpose: Verifies basic file rename functionality
     */
    @Test
    public void testExecute_WithExistingFile_RenamesFile() throws Exception {
        // Setup: Write content to file and register it
        Files.write(_oSourceFile, "Test content".getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oSourceFile.toString(), FILE_ID, FileOpenType.READ);
        assertTrue(Files.exists(_oSourceFile), "Source file should exist");

        // Determine new file name
        Path oNewPath = _oSourceFile.resolveSibling("frename-target-" + System.currentTimeMillis() + ".txt");
        _oRenamedFile = oNewPath;

        // Execute: Rename file
        FRenameStatement oStatement = new FRenameStatement(TOKEN_NUMBER, FILE_ID, new StringValue(oNewPath.toString()));
        oStatement.execute();

        // Verify: Source file no longer exists, renamed file exists
        assertFalse(Files.exists(_oSourceFile), "Source file should not exist after rename");
        assertTrue(Files.exists(oNewPath), "Renamed file should exist");

        // Verify: File content is preserved
        String strContent = new String(Files.readAllBytes(oNewPath), StandardCharsets.UTF_8);
        assertEquals("Test content", strContent);
    }

    /**
     * Test: FRenameStatement renames file with content.
     * <p>
     * Given: File with content is registered in FileManager
     * When: FRenameStatement.execute() is called
     * Then: File is renamed and content is preserved
     * <p>
     * Purpose: Verifies content preservation during rename
     */
    @Test
    public void testExecute_WithContentPreservation_RenamesFile() throws Exception {
        // Setup: Write multi-line content to file
        String strContent = "Line 1\nLine 2\nLine 3";
        Files.write(_oSourceFile, strContent.getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oSourceFile.toString(), FILE_ID, FileOpenType.READ);

        // Determine new file name
        Path oNewPath = _oSourceFile.resolveSibling("frename-content-" + System.currentTimeMillis() + ".txt");
        _oRenamedFile = oNewPath;

        // Execute: Rename file
        FRenameStatement oStatement = new FRenameStatement(TOKEN_NUMBER, FILE_ID, new StringValue(oNewPath.toString()));
        oStatement.execute();

        // Verify: Content is preserved
        String strReadContent = new String(Files.readAllBytes(oNewPath), StandardCharsets.UTF_8);
        assertEquals(strContent, strReadContent);
    }

    /**
     * Test: FRenameStatement renames file to different directory.
     * <p>
     * Given: File exists in one directory
     * When: FRenameStatement.execute() is called with path in different directory
     * Then: File is moved and renamed to new location
     * <p>
     * Purpose: Verifies file can be moved to different directory during rename
     */
    @Test
    public void testExecute_WithDifferentDirectory_MovesAndRenamesFile() throws Exception {
        // Setup: Create file in source directory
        Files.write(_oSourceFile, "test".getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oSourceFile.toString(), FILE_ID, FileOpenType.READ);

        // Create target directory and path
        Path oTargetDir = _oSourceFile.getParent().resolve("frename-target-dir-" + System.currentTimeMillis());
        Files.createDirectory(oTargetDir);
        Path oNewPath = oTargetDir.resolve("renamed-file.txt");
        _oRenamedFile = oNewPath;

        // Execute: Rename file (move to different directory)
        FRenameStatement oStatement = new FRenameStatement(TOKEN_NUMBER, FILE_ID, new StringValue(oNewPath.toString()));
        oStatement.execute();

        // Verify: File exists in new location
        assertTrue(Files.exists(oNewPath), "File should exist in new directory");
        assertFalse(Files.exists(_oSourceFile), "File should not exist in original directory");

        // Note: Do not try to delete oTargetDir as the tearDown will clean up _oRenamedFile
    }

    /**
     * Test: FRenameStatement renames file with special characters in new name.
     * <p>
     * Given: New file name contains special characters
     * When: FRenameStatement.execute() is called
     * Then: File is renamed with special characters preserved
     * <p>
     * Purpose: Verifies special character handling in file names
     */
    @Test
    public void testExecute_WithSpecialCharactersInNewName_RenamesFile() throws Exception {
        // Setup: Create and register file
        Files.write(_oSourceFile, "test".getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oSourceFile.toString(), FILE_ID, FileOpenType.READ);

        // Determine new file name with special characters
        Path oNewPath = _oSourceFile.resolveSibling("frename-special_chars-" + System.currentTimeMillis() + ".txt");
        _oRenamedFile = oNewPath;

        // Execute: Rename file
        FRenameStatement oStatement = new FRenameStatement(TOKEN_NUMBER, FILE_ID, new StringValue(oNewPath.toString()));
        oStatement.execute();

        // Verify: File exists with special character name
        assertTrue(Files.exists(oNewPath), "File should be renamed with special characters");
    }

    /**
     * Test: FRenameStatement changes file extension.
     * <p>
     * Given: File exists with one extension
     * When: FRenameStatement.execute() is called with different extension
     * Then: File extension is changed
     * <p>
     * Purpose: Verifies file extension changes
     */
    @Test
    public void testExecute_WithDifferentExtension_ChangesFileExtension() throws Exception {
        // Setup: Create .txt file and register it
        Files.write(_oSourceFile, "test".getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oSourceFile.toString(), FILE_ID, FileOpenType.READ);

        // Determine new file name with different extension
        Path oNewPath = _oSourceFile.resolveSibling(_oSourceFile.getFileName().toString().replace(".txt", ".bak"));
        _oRenamedFile = oNewPath;

        // Execute: Rename file
        FRenameStatement oStatement = new FRenameStatement(TOKEN_NUMBER, FILE_ID, new StringValue(oNewPath.toString()));
        oStatement.execute();

        // Verify: File exists with new extension
        assertTrue(Files.exists(oNewPath), "File should exist with new extension");
        assertTrue(oNewPath.toString().endsWith(".bak"), "File should have .bak extension");
    }

    // =========================================================================
    // NEGATIVE TEST CASES - FRenameStatement handles error conditions
    // =========================================================================

    /**
     * Test: FRenameStatement with unregistered file ID.
     * <p>
     * Given: File ID is not registered in FileManager
     * When: FRenameStatement.execute() is called
     * Then: RuntimeException is thrown
     * <p>
     * Purpose: Verifies error handling for unregistered file ID
     */
    @Test
    public void testExecute_WithUnregisteredFileId_ThrowsRuntimeException() {
        // Execute and verify: RuntimeException thrown
        FRenameStatement oStatement = new FRenameStatement(TOKEN_NUMBER, 999, new StringValue("newname.txt"));
        assertThrows(RuntimeException.class, oStatement::execute);
    }

    /**
     * Test: FRenameStatement with empty new file name.
     * <p>
     * Given: New file name is empty string
     * When: FRenameStatement.execute() is called
     * Then: RuntimeException is thrown
     * <p>
     * Purpose: Verifies validation of new file name
     */
    @Test
    public void testExecute_WithEmptyNewFileName_ThrowsRuntimeException() throws Exception {
        // Setup: Register file
        Files.write(_oSourceFile, "test".getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oSourceFile.toString(), FILE_ID, FileOpenType.READ);

        // Execute and verify: RuntimeException thrown
        FRenameStatement oStatement = new FRenameStatement(TOKEN_NUMBER, FILE_ID, new StringValue(""));
        assertThrows(RuntimeException.class, oStatement::execute);
    }

    /**
     * Test: FRenameStatement with null new file name.
     * <p>
     * Given: New file name is null
     * When: FRenameStatement.execute() is called
     * Then: RuntimeException is thrown
     * <p>
     * Purpose: Verifies null validation for new file name
     */
    @Test
    public void testExecute_WithNullNewFileName_ThrowsRuntimeException() throws Exception {
        // Setup: Register file
        Files.write(_oSourceFile, "test".getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oSourceFile.toString(), FILE_ID, FileOpenType.READ);

        // Execute and verify: RuntimeException thrown
        FRenameStatement oStatement = new FRenameStatement(TOKEN_NUMBER, FILE_ID, (StringValue) null);
        assertThrows(RuntimeException.class, oStatement::execute);
    }

    /**
     * Test: FRenameStatement with non-existent file.
     * <p>
     * Given: File ID is registered but file no longer exists on disk
     * When: FRenameStatement.execute() is called
     * Then: RuntimeException is thrown
     * <p>
     * Purpose: Verifies error handling for missing files
     */
    @Test
    public void testExecute_WithNonExistentFile_ThrowsRuntimeException() throws Exception {
        // Setup: Register file but delete it first
        Files.deleteIfExists(_oSourceFile);
        _oSourceFile = Files.createTempFile("frename-nonexistent-", ".txt");
        Files.deleteIfExists(_oSourceFile);

        // Create a temporary file just to register
        Path oTempFile = Files.createTempFile("frename-temp-", ".txt");
        _oFileManager.openFile(oTempFile.toString(), FILE_ID, FileOpenType.READ);
        Files.deleteIfExists(oTempFile);

        // Execute and verify: RuntimeException thrown
        FRenameStatement oStatement = new FRenameStatement(TOKEN_NUMBER, FILE_ID, new StringValue("newname.txt"));
        assertThrows(RuntimeException.class, oStatement::execute);
    }

    /**
     * Test: FRenameStatement with destination file already existing.
     * <p>
     * Given: Target file name already exists
     * When: FRenameStatement.execute() is called
     * Then: RuntimeException is thrown (file move fails)
     * <p>
     * Purpose: Verifies error handling when target exists
     */
    @Test
    public void testExecute_WithExistingDestinationFile_ThrowsRuntimeException() throws Exception {
        // Setup: Create source file and register it
        Files.write(_oSourceFile, "source".getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oSourceFile.toString(), FILE_ID, FileOpenType.READ);

        // Create target file that already exists
        Path oTargetFile = _oSourceFile.resolveSibling("frename-existing-" + System.currentTimeMillis() + ".txt");
        Files.write(oTargetFile, "target".getBytes(StandardCharsets.UTF_8));
        _oRenamedFile = oTargetFile;

        // Execute and verify: RuntimeException thrown
        FRenameStatement oStatement = new FRenameStatement(TOKEN_NUMBER, FILE_ID, new StringValue(oTargetFile.toString()));
        assertThrows(RuntimeException.class, oStatement::execute);
    }

    // =========================================================================
    // INTERFACE TEST CASES - Test public interface methods
    // =========================================================================

    /**
     * Test: getTokenNumber returns constructor value.
     * <p>
     * Given: FRenameStatement with specific token number
     * When: getTokenNumber() is called
     * Then: Returns the token number from constructor
     * <p>
     * Purpose: Verifies token number storage and retrieval
     */
    @Test
    public void testGetTokenNumber_ReturnsConstructorValue() {
        FRenameStatement oStatement = new FRenameStatement(42, FILE_ID, new StringValue("newname.txt"));

        assertEquals(42, oStatement.getTokenNumber());
    }

    /**
     * Test: getTokenNumber with various token numbers.
     * <p>
     * Given: FRenameStatement with different token numbers
     * When: getTokenNumber() is called
     * Then: Returns the correct token number
     * <p>
     * Purpose: Verifies token number accuracy
     */
    @Test
    public void testGetTokenNumber_WithZero_ReturnsZero() {
        FRenameStatement oStatement = new FRenameStatement(0, FILE_ID, new StringValue("newname.txt"));

        assertEquals(0, oStatement.getTokenNumber());
    }

    /**
     * Test: getTokenNumber with negative token number.
     * <p>
     * Given: FRenameStatement with negative token number
     * When: getTokenNumber() is called
     * Then: Returns the negative token number
     * <p>
     * Purpose: Verifies negative token number handling
     */
    @Test
    public void testGetTokenNumber_WithNegative_ReturnsNegative() {
        FRenameStatement oStatement = new FRenameStatement(-1, FILE_ID, new StringValue("newname.txt"));

        assertEquals(-1, oStatement.getTokenNumber());
    }

    /**
     * Test: content returns FRENAME.
     * <p>
     * Given: FRenameStatement instance
     * When: content() is called
     * Then: Returns "FRENAME"
     * <p>
     * Purpose: Verifies content method return value
     */
    @Test
    public void testContent_ReturnsFRENAME() throws Exception {
        FRenameStatement oStatement = new FRenameStatement(1, FILE_ID, new StringValue("newname.txt"));

        assertEquals("FRENAME", oStatement.content());
    }

    /**
     * Test: structure contains FRENAME key.
     * <p>
     * Given: FRenameStatement instance
     * When: structure() is called
     * Then: JSON contains FRENAME key
     * <p>
     * Purpose: Verifies structure JSON format
     */
    @Test
    public void testStructure_ContainsFRENAMEKey() throws Exception {
        FRenameStatement oStatement = new FRenameStatement(TOKEN_NUMBER, FILE_ID, new StringValue("newname.txt"));

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FRENAME\""));
    }

    /**
     * Test: structure contains token number.
     * <p>
     * Given: FRenameStatement with specific token number
     * When: structure() is called
     * Then: JSON contains token number
     * <p>
     * Purpose: Verifies token number in structure
     */
    @Test
    public void testStructure_ContainsTokenNumber() throws Exception {
        FRenameStatement oStatement = new FRenameStatement(100, FILE_ID, new StringValue("newname.txt"));

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"TOKEN_NR\": \"100\""));
    }

    /**
     * Test: structure contains file ID.
     * <p>
     * Given: FRenameStatement with specific file ID
     * When: structure() is called
     * Then: JSON contains file ID
     * <p>
     * Purpose: Verifies file ID in structure
     */
    @Test
    public void testStructure_ContainsFileId() throws Exception {
        FRenameStatement oStatement = new FRenameStatement(TOKEN_NUMBER, 801, new StringValue("newname.txt"));

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FILE_ID\": \"801\""));
    }

    /**
     * Test: structure contains new file name.
     * <p>
     * Given: FRenameStatement with specific new file name
     * When: structure() is called
     * Then: JSON contains new file name
     * <p>
     * Purpose: Verifies new file name in structure
     */
    @Test
    public void testStructure_ContainsNewFileName() throws Exception {
        FRenameStatement oStatement = new FRenameStatement(TOKEN_NUMBER, FILE_ID, new StringValue("/path/to/newfile.txt"));

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"NEW_FILE_NAME\": \"/path/to/newfile.txt\""));
    }

    /**
     * Test: structure returns valid JSON.
     * <p>
     * Given: FRenameStatement instance
     * When: structure() is called
     * Then: Returns properly formatted JSON
     * <p>
     * Purpose: Verifies JSON format is valid
     */
    @Test
    public void testStructure_ReturnsValidJsonFormat() throws Exception {
        FRenameStatement oStatement = new FRenameStatement(TOKEN_NUMBER, FILE_ID, new StringValue("newname.txt"));

        String strStructure = oStatement.structure();

        assertTrue(strStructure.startsWith("{"));
        assertTrue(strStructure.endsWith("}"));
        assertTrue(strStructure.contains("\"FRENAME\": {"));
    }
}
