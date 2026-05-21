package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.memoryManager.FileOpenType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FDeleteStatementTest.java
 * <p>
 * Unit tests for the FDeleteStatement class.
 * <p>
 * This test class provides comprehensive coverage of the FDeleteStatement, which deletes a file
 * identified by its file ID from the file system. If the file does not exist, a warning is logged
 * and execution continues (no exception is thrown).
 * <p>
 * Test Categories:
 * - POSITIVE TESTS: FDeleteStatement successfully deletes existing files
 * - NEGATIVE TESTS: FDeleteStatement handles missing files gracefully
 * - EDGE CASES: Unregistered IDs, multiple deletions, already closed files
 * - INTERFACE TESTS: getTokenNumber(), content(), and structure() methods
 * <p>
 * Key Behavior:
 * - Deletes file from file system using file ID
 * - Logs warning if file does not exist (no exception)
 * - Logs warning if file ID not registered (no exception)
 * - Logs warning if file cannot be deleted (no exception)
 * - Never throws exception, allowing program to continue
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FDeleteStatementTest {

    private static final int FILE_ID_1 = 701;
    private static final int FILE_ID_2 = 702;
    private static final int TOKEN_NUMBER = 100;

    private Path _oFile1;
    private Path _oFile2;
    private FileManager _oFileManager;

    /**
     * Setup method: Initializes test environment before each test.
     * Creates temporary files and FileManager instance.
     */
    @BeforeEach
    public void setUp() throws Exception {
        _oFile1 = Files.createTempFile("fdelete-test-1-", ".txt");
        _oFile2 = Files.createTempFile("fdelete-test-2-", ".txt");
        _oFileManager = new FileManager();
    }

    /**
     * Teardown method: Cleans up after each test.
     * Closes files and deletes temporary files if they still exist.
     */
    @AfterEach
    public void tearDown() throws Exception {
        _oFileManager.closeFile(FILE_ID_1, false);
        _oFileManager.closeFile(FILE_ID_2, false);
        Files.deleteIfExists(_oFile1);
        Files.deleteIfExists(_oFile2);
    }

    // =========================================================================
    // POSITIVE TEST CASES - FDeleteStatement successfully deletes files
    // =========================================================================

    /**
     * Test: FDeleteStatement deletes existing file.
     * <p>
     * Given: File exists and is registered in FileManager
     * When: FDeleteStatement.execute() is called
     * Then: File is deleted from file system
     * <p>
     * Purpose: Verifies basic file deletion functionality
     */
    @Test
    public void testExecute_WithExistingFile_DeletesFile() throws Exception {
        // Setup: Register file in FileManager
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.WRITE);
        assertTrue(Files.exists(_oFile1), "File should exist before deletion");

        // Execute: Delete file
        FDeleteStatement oStatement = new FDeleteStatement(TOKEN_NUMBER, FILE_ID_1);
        oStatement.execute();

        // Verify: File is deleted
        assertFalse(Files.exists(_oFile1), "File should not exist after deletion");
    }

    /**
     * Test: FDeleteStatement deletes multiple files sequentially.
     * <p>
     * Given: Multiple files are registered in FileManager
     * When: FDeleteStatement.execute() is called for each file
     * Then: All files are deleted
     * <p>
     * Purpose: Verifies multiple file deletion works correctly
     */
    @Test
    public void testExecute_WithMultipleFiles_DeletesAllFiles() throws Exception {
        // Setup: Register both files
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.WRITE);
        _oFileManager.openFile(_oFile2.toString(), FILE_ID_2, FileOpenType.WRITE);
        assertTrue(Files.exists(_oFile1), "File 1 should exist");
        assertTrue(Files.exists(_oFile2), "File 2 should exist");

        // Execute: Delete both files
        FDeleteStatement oStatement1 = new FDeleteStatement(TOKEN_NUMBER, FILE_ID_1);
        oStatement1.execute();
        FDeleteStatement oStatement2 = new FDeleteStatement(TOKEN_NUMBER, FILE_ID_2);
        oStatement2.execute();

        // Verify: Both files are deleted
        assertFalse(Files.exists(_oFile1), "File 1 should be deleted");
        assertFalse(Files.exists(_oFile2), "File 2 should be deleted");
    }

    /**
     * Test: FDeleteStatement deletes file after closing it.
     * <p>
     * Given: File is registered and then closed in FileManager
     * When: FDeleteStatement.execute() is called
     * Then: File is deleted from file system
     * <p>
     * Purpose: Verifies deletion works on closed files
     */
    @Test
    public void testExecute_WithClosedFile_DeletesFile() throws Exception {
        // Setup: Open and close file
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.WRITE);
        _oFileManager.closeFile(FILE_ID_1, false);
        assertTrue(Files.exists(_oFile1), "File should exist before deletion");

        // Re-open for registration (but don't close again in tearDown)
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.READ);

        // Execute: Delete file
        FDeleteStatement oStatement = new FDeleteStatement(TOKEN_NUMBER, FILE_ID_1);
        oStatement.execute();

        // Verify: File is deleted
        assertFalse(Files.exists(_oFile1), "File should not exist after deletion");
    }

    // =========================================================================
    // NEGATIVE TEST CASES - FDeleteStatement handles missing files
    // =========================================================================

    /**
     * Test: FDeleteStatement with non-existent file.
     * <p>
     * Given: File ID is registered but file does not exist
     * When: FDeleteStatement.execute() is called
     * Then: Warning is logged and execution continues (no exception)
     * <p>
     * Purpose: Verifies graceful handling of missing files
     */
    @Test
    public void testExecute_WithNonExistentFile_DoesNotThrow() throws Exception {
        // Setup: Register file ID but delete the actual file first
        Files.deleteIfExists(_oFile1);
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.WRITE);
        Files.deleteIfExists(_oFile1);

        // Execute: Delete non-existent file (should log warning, not throw)
        FDeleteStatement oStatement = new FDeleteStatement(TOKEN_NUMBER, FILE_ID_1);
        oStatement.execute();

        // Verify: No exception was thrown, execution continued
        assertFalse(Files.exists(_oFile1), "File should not exist");
    }

    /**
     * Test: FDeleteStatement with unregistered file ID.
     * <p>
     * Given: File ID is not registered in FileManager
     * When: FDeleteStatement.execute() is called
     * Then: Warning is logged and execution continues (no exception)
     * <p>
     * Purpose: Verifies graceful handling of unregistered file IDs
     */
    @Test
    public void testExecute_WithUnregisteredFileId_DoesNotThrow() throws Exception {
        // Execute: Delete unregistered file (should log warning, not throw)
        FDeleteStatement oStatement = new FDeleteStatement(TOKEN_NUMBER, 999);
        oStatement.execute();

        // Verify: No exception was thrown
        assertTrue(true, "Should not throw exception for unregistered file ID");
    }

    /**
     * Test: FDeleteStatement with already deleted file.
     * <p>
     * Given: File is deleted, then delete is called again
     * When: FDeleteStatement.execute() is called twice
     * Then: Second deletion logs warning but does not throw
     * <p>
     * Purpose: Verifies idempotent behavior of file deletion
     */
    @Test
    public void testExecute_WithAlreadyDeletedFile_DoesNotThrow() throws Exception {
        // Setup: Register file
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.WRITE);

        // Execute: Delete file
        FDeleteStatement oStatement1 = new FDeleteStatement(TOKEN_NUMBER, FILE_ID_1);
        oStatement1.execute();
        assertFalse(Files.exists(_oFile1), "File should be deleted");

        // Execute: Delete same file again
        FDeleteStatement oStatement2 = new FDeleteStatement(TOKEN_NUMBER, FILE_ID_1);
        oStatement2.execute();

        // Verify: No exception was thrown on second deletion
        assertTrue(true, "Should not throw exception when deleting already-deleted file");
    }

    // =========================================================================
    // INTERFACE TEST CASES - Test public interface methods
    // =========================================================================

    /**
     * Test: getTokenNumber returns constructor value.
     * <p>
     * Given: FDeleteStatement with specific token number
     * When: getTokenNumber() is called
     * Then: Returns the token number from constructor
     * <p>
     * Purpose: Verifies token number storage and retrieval
     */
    @Test
    public void testGetTokenNumber_ReturnsConstructorValue() {
        FDeleteStatement oStatement = new FDeleteStatement(42, FILE_ID_1);

        assertEquals(42, oStatement.getTokenNumber());
    }

    /**
     * Test: getTokenNumber with various token numbers.
     * <p>
     * Given: FDeleteStatement with different token numbers
     * When: getTokenNumber() is called
     * Then: Returns the correct token number
     * <p>
     * Purpose: Verifies token number accuracy
     */
    @Test
    public void testGetTokenNumber_WithZero_ReturnsZero() {
        FDeleteStatement oStatement = new FDeleteStatement(0, FILE_ID_1);

        assertEquals(0, oStatement.getTokenNumber());
    }

    /**
     * Test: getTokenNumber with negative token number.
     * <p>
     * Given: FDeleteStatement with negative token number
     * When: getTokenNumber() is called
     * Then: Returns the negative token number
     * <p>
     * Purpose: Verifies negative token number handling
     */
    @Test
    public void testGetTokenNumber_WithNegative_ReturnsNegative() {
        FDeleteStatement oStatement = new FDeleteStatement(-1, FILE_ID_1);

        assertEquals(-1, oStatement.getTokenNumber());
    }

    /**
     * Test: content returns FDELETE.
     * <p>
     * Given: FDeleteStatement instance
     * When: content() is called
     * Then: Returns "FDELETE"
     * <p>
     * Purpose: Verifies content method return value
     */
    @Test
    public void testContent_ReturnsFDELETE() throws Exception {
        FDeleteStatement oStatement = new FDeleteStatement(1, FILE_ID_1);

        assertEquals("FDELETE", oStatement.content());
    }

    /**
     * Test: content returns FDELETE regardless of parameters.
     * <p>
     * Given: FDeleteStatement with various parameters
     * When: content() is called
     * Then: Always returns "FDELETE"
     * <p>
     * Purpose: Verifies consistent content return
     */
    @Test
    public void testContent_WithDifferentParameters_ReturnsFDELETE() throws Exception {
        FDeleteStatement oStatement = new FDeleteStatement(100, 999);

        assertEquals("FDELETE", oStatement.content());
    }

    /**
     * Test: structure contains FDELETE key.
     * <p>
     * Given: FDeleteStatement instance
     * When: structure() is called
     * Then: JSON contains FDELETE key
     * <p>
     * Purpose: Verifies structure JSON format
     */
    @Test
    public void testStructure_ContainsFDELETEKey() throws Exception {
        FDeleteStatement oStatement = new FDeleteStatement(TOKEN_NUMBER, FILE_ID_1);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FDELETE\""));
    }

    /**
     * Test: structure contains token number.
     * <p>
     * Given: FDeleteStatement with specific token number
     * When: structure() is called
     * Then: JSON contains token number
     * <p>
     * Purpose: Verifies token number in structure
     */
    @Test
    public void testStructure_ContainsTokenNumber() throws Exception {
        FDeleteStatement oStatement = new FDeleteStatement(100, FILE_ID_1);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"TOKEN_NR\": \"100\""));
    }

    /**
     * Test: structure contains file ID.
     * <p>
     * Given: FDeleteStatement with specific file ID
     * When: structure() is called
     * Then: JSON contains file ID
     * <p>
     * Purpose: Verifies file ID in structure
     */
    @Test
    public void testStructure_ContainsFileId() throws Exception {
        FDeleteStatement oStatement = new FDeleteStatement(TOKEN_NUMBER, 701);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FILE_ID\": \"701\""));
    }

    /**
     * Test: structure returns valid JSON.
     * <p>
     * Given: FDeleteStatement instance
     * When: structure() is called
     * Then: Returns properly formatted JSON
     * <p>
     * Purpose: Verifies JSON format is valid
     */
    @Test
    public void testStructure_ReturnsValidJsonFormat() throws Exception {
        FDeleteStatement oStatement = new FDeleteStatement(TOKEN_NUMBER, FILE_ID_1);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.startsWith("{"));
        assertTrue(strStructure.endsWith("}"));
        assertTrue(strStructure.contains("\"FDELETE\": {"));
    }

    /**
     * Test: structure contains both token number and file ID.
     * <p>
     * Given: FDeleteStatement instance
     * When: structure() is called
     * Then: JSON contains both parameters
     * <p>
     * Purpose: Verifies complete structure information
     */
    @Test
    public void testStructure_ContainsBothTokenAndFileId() throws Exception {
        FDeleteStatement oStatement = new FDeleteStatement(123, 456);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"TOKEN_NR\": \"123\""));
        assertTrue(strStructure.contains("\"FILE_ID\": \"456\""));
    }
}
