package eu.gricom.basic.functions;

import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.memoryManager.FileOpenType;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FGetFileNameTest.java
 * <p>
 * Unit tests for the FGetFileName BASIC function.
 * <p>
 * This test class provides comprehensive coverage of the FGetFileName function, which retrieves
 * the file name associated with a given file ID from the FileManager. The function is used in
 * BASIC programs to query which file is associated with a particular file ID during file I/O operations.
 * <p>
 * Test Categories:
 * - POSITIVE TESTS: File IDs that exist and have associated file names
 * - NEGATIVE TESTS: File IDs that don't exist or have been closed
 * - EDGE CASES: Boundary conditions, invalid IDs, and special scenarios
 * <p>
 * Key Behavior:
 * - Returns StringValue containing the file name for open files
 * - Returns StringValue containing empty string for unknown/closed file IDs
 * - Never returns null; always returns a StringValue
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FGetFileNameTest {

    // -------------------------------------------------------------------------
    // TEST SETUP AND CONSTANTS
    // -------------------------------------------------------------------------

    private static final int FILE_ID_UNKNOWN = 999;
    private static final AtomicInteger _fileIdCounter = new AtomicInteger(100);

    private Path _oTempFile1;
    private Path _oTempFile2;
    private FileManager _oFileManager;
    private int _iCurrentTestFileId1;
    private int _iCurrentTestFileId2;
    private int _iCurrentTestFileId3;

    /**
     * Setup method: Initializes test environment before each test.
     * Creates temporary test files, unique file IDs, and a FileManager instance.
     * Uses unique file IDs for each test to avoid interference from previous tests.
     */
    @BeforeEach
    public void setUp() throws IOException {
        _oFileManager = new FileManager();
        _oTempFile1 = Files.createTempFile("fgetfilename-test-", ".txt");
        _oTempFile2 = Files.createTempFile("fgetfilename-test-", ".txt");
        Files.writeString(_oTempFile1, "Test content for file 1");
        Files.writeString(_oTempFile2, "Test content for file 2");

        // Generate unique file IDs for this test to avoid conflicts
        _iCurrentTestFileId1 = _fileIdCounter.getAndIncrement();
        _iCurrentTestFileId2 = _fileIdCounter.getAndIncrement();
        _iCurrentTestFileId3 = _fileIdCounter.getAndIncrement();
    }

    /**
     * Teardown method: Cleans up after each test.
     * Closes any open files to avoid state pollution between tests.
     */
    @AfterEach
    public void tearDown() {
        _oFileManager.closeFile(_iCurrentTestFileId1, false);
        _oFileManager.closeFile(_iCurrentTestFileId2, false);
        _oFileManager.closeFile(_iCurrentTestFileId3, false);
    }

    // =========================================================================
    // POSITIVE TEST CASES - File IDs that exist
    // =========================================================================

    /**
     * Test: FGetFileName with an open READ file.
     * <p>
     * Given: A file opened in READ mode with a specific file ID
     * When: FGetFileName.execute() is called with that file ID
     * Then: Returns StringValue containing the full file path
     * <p>
     * Purpose: Verifies basic functionality of retrieving file name for READ files
     */
    @Test
    public void testFGetFileName_WithOpenReadFile_ReturnsFileName() throws Exception {
        // Setup: Open a file in READ mode
        _oFileManager.openFile(_oTempFile1.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FGetFileName with the file ID
        Value oResult = FGetFileName.execute(_iCurrentTestFileId1);

        // Verify: Result is a StringValue containing the file path
        assertNotNull(oResult);
        assertTrue(oResult instanceof StringValue);
        assertEquals(_oTempFile1.toString(), oResult.toString());
    }

    /**
     * Test: FGetFileName with an open WRITE file.
     * <p>
     * Given: A file opened in WRITE mode with a specific file ID
     * When: FGetFileName.execute() is called with that file ID
     * Then: Returns StringValue containing the full file path
     * <p>
     * Purpose: Verifies functionality works for WRITE mode files (not just READ)
     */
    @Test
    public void testFGetFileName_WithOpenWriteFile_ReturnsFileName() throws Exception {
        // Setup: Open a file in WRITE mode
        _oFileManager.openFile(_oTempFile1.toString(), _iCurrentTestFileId1, FileOpenType.WRITE);

        // Execute: Call FGetFileName with the file ID
        Value oResult = FGetFileName.execute(_iCurrentTestFileId1);

        // Verify: Result contains the file path
        assertNotNull(oResult);
        assertEquals(_oTempFile1.toString(), oResult.toString());
    }

    /**
     * Test: FGetFileName with multiple open files.
     * <p>
     * Given: Multiple files opened with different file IDs
     * When: FGetFileName.execute() is called for each file ID
     * Then: Returns correct StringValue for each file ID
     * <p>
     * Purpose: Verifies the function correctly distinguishes between multiple open files
     */
    @Test
    public void testFGetFileName_WithMultipleOpenFiles_ReturnsCorrectFileForEachId() throws Exception {
        // Setup: Open multiple files with different IDs
        _oFileManager.openFile(_oTempFile1.toString(), _iCurrentTestFileId1, FileOpenType.READ);
        _oFileManager.openFile(_oTempFile2.toString(), _iCurrentTestFileId2, FileOpenType.WRITE);

        // Execute: Call FGetFileName for each file ID
        Value oResult1 = FGetFileName.execute(_iCurrentTestFileId1);
        Value oResult2 = FGetFileName.execute(_iCurrentTestFileId2);

        // Verify: Each result contains the correct file path
        assertEquals(_oTempFile1.toString(), oResult1.toString());
        assertEquals(_oTempFile2.toString(), oResult2.toString());
    }

    /**
     * Test: FGetFileName returns consistent results.
     * <p>
     * Given: An open file with a known file ID
     * When: FGetFileName.execute() is called multiple times with the same file ID
     * Then: All calls return identical StringValue results
     * <p>
     * Purpose: Verifies the function is deterministic and doesn't have state side effects
     */
    @Test
    public void testFGetFileName_WithMultipleCalls_ReturnsConsistentResults() throws Exception {
        // Setup: Open a file
        _oFileManager.openFile(_oTempFile1.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FGetFileName multiple times
        Value oResult1 = FGetFileName.execute(_iCurrentTestFileId1);
        Value oResult2 = FGetFileName.execute(_iCurrentTestFileId1);
        Value oResult3 = FGetFileName.execute(_iCurrentTestFileId1);

        // Verify: All results are identical
        assertEquals(oResult1.toString(), oResult2.toString());
        assertEquals(oResult2.toString(), oResult3.toString());
        assertEquals(_oTempFile1.toString(), oResult1.toString());
    }

    /**
     * Test: FGetFileName with absolute file path.
     * <p>
     * Given: A file opened with an absolute path
     * When: FGetFileName.execute() is called with that file ID
     * Then: Returns StringValue containing the complete absolute path
     * <p>
     * Purpose: Verifies the function preserves absolute paths correctly
     */
    @Test
    public void testFGetFileName_WithAbsolutePath_ReturnsAbsolutePath() throws Exception {
        // Setup: Get absolute path and open file with it
        String strAbsolutePath = _oTempFile1.toAbsolutePath().toString();
        _oFileManager.openFile(strAbsolutePath, _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FGetFileName
        Value oResult = FGetFileName.execute(_iCurrentTestFileId1);

        // Verify: Result contains the absolute path
        assertEquals(strAbsolutePath, oResult.toString());
    }

    // =========================================================================
    // NEGATIVE TEST CASES - File IDs that don't exist or have been closed
    // =========================================================================

    /**
     * Test: FGetFileName with a file ID that doesn't exist.
     * <p>
     * Given: A file ID that was never opened
     * When: FGetFileName.execute() is called with that file ID
     * Then: Returns StringValue containing an empty string (not null)
     * <p>
     * Purpose: Verifies graceful handling of unknown file IDs
     */
    @Test
    public void testFGetFileName_WithUnknownFileId_ReturnsEmptyString() throws Exception {
        // Execute: Call FGetFileName with an unknown file ID
        Value oResult = FGetFileName.execute(FILE_ID_UNKNOWN);

        // Verify: Result is an empty StringValue (not null)
        assertNotNull(oResult);
        assertTrue(oResult instanceof StringValue);
        assertEquals("", oResult.toString());
    }

    /**
     * Test: FGetFileName with a file that has been closed.
     * <p>
     * Given: A file that was opened but subsequently closed
     * When: FGetFileName.execute() is called with that file ID
     * Then: Returns StringValue containing an empty string
     * <p>
     * Purpose: Verifies the function correctly reflects closed file state
     */
    @Test
    public void testFGetFileName_WithClosedFile_ReturnsEmptyString() throws Exception {
        // Setup: Open a file then close it
        _oFileManager.openFile(_oTempFile1.toString(), _iCurrentTestFileId1, FileOpenType.READ);
        _oFileManager.closeFile(_iCurrentTestFileId1, false);

        // Execute: Call FGetFileName after closing
        Value oResult = FGetFileName.execute(_iCurrentTestFileId1);

        // Verify: Result is an empty StringValue
        assertNotNull(oResult);
        assertEquals("", oResult.toString());
    }

    /**
     * Test: FGetFileName with negative file ID.
     * <p>
     * Given: A negative integer as file ID
     * When: FGetFileName.execute() is called with that file ID
     * Then: Returns StringValue containing an empty string
     * <p>
     * Purpose: Verifies handling of invalid file ID values
     */
    @Test
    public void testFGetFileName_WithNegativeFileId_ReturnsEmptyString() throws Exception {
        // Execute: Call FGetFileName with negative file ID
        Value oResult = FGetFileName.execute(-1);

        // Verify: Result is an empty StringValue
        assertNotNull(oResult);
        assertEquals("", oResult.toString());
    }

    /**
     * Test: FGetFileName with zero file ID.
     * <p>
     * Given: Zero as file ID (typically invalid)
     * When: FGetFileName.execute() is called with zero
     * Then: Returns StringValue containing an empty string
     * <p>
     * Purpose: Verifies handling of zero file ID edge case
     */
    @Test
    public void testFGetFileName_WithZeroFileId_ReturnsEmptyString() throws Exception {
        // Execute: Call FGetFileName with zero
        Value oResult = FGetFileName.execute(0);

        // Verify: Result is an empty StringValue
        assertNotNull(oResult);
        assertEquals("", oResult.toString());
    }

    // =========================================================================
    // EDGE CASE TESTS - Boundary conditions and special scenarios
    // =========================================================================

    /**
     * Test: FGetFileName never returns null.
     * <p>
     * Given: Various file ID values (known and unknown)
     * When: FGetFileName.execute() is called
     * Then: Always returns a non-null StringValue
     * <p>
     * Purpose: Verifies the function never returns null (always returns StringValue)
     */
    @Test
    public void testFGetFileName_NeverReturnsNull() throws Exception {
        // Execute: Call FGetFileName with various IDs
        Value oResult1 = FGetFileName.execute(FILE_ID_UNKNOWN);
        Value oResult2 = FGetFileName.execute(-1);
        Value oResult3 = FGetFileName.execute(Integer.MAX_VALUE);

        // Verify: All results are non-null StringValues
        assertNotNull(oResult1);
        assertNotNull(oResult2);
        assertNotNull(oResult3);
        assertTrue(oResult1 instanceof StringValue);
        assertTrue(oResult2 instanceof StringValue);
        assertTrue(oResult3 instanceof StringValue);
    }

    /**
     * Test: FGetFileName with very large file ID.
     * <p>
     * Given: Integer.MAX_VALUE as file ID
     * When: FGetFileName.execute() is called with this value
     * Then: Returns StringValue containing an empty string (graceful handling)
     * <p>
     * Purpose: Verifies boundary handling at upper limit of file ID range
     */
    @Test
    public void testFGetFileName_WithMaxIntFileId_ReturnsEmptyString() throws Exception {
        // Execute: Call FGetFileName with Integer.MAX_VALUE
        Value oResult = FGetFileName.execute(Integer.MAX_VALUE);

        // Verify: Result is an empty StringValue
        assertNotNull(oResult);
        assertEquals("", oResult.toString());
    }

    /**
     * Test: FGetFileName with file name containing special characters.
     * <p>
     * Given: A file with special characters in the path
     * When: FGetFileName.execute() is called with that file's ID
     * Then: Returns StringValue with the exact path including special characters
     * <p>
     * Purpose: Verifies the function preserves file names with special characters
     */
    @Test
    public void testFGetFileName_WithSpecialCharactersInPath_ReturnsExactPath() throws Exception {
        // Note: Temp files already have special characters in their names
        // Setup: Open the temp file
        _oFileManager.openFile(_oTempFile1.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FGetFileName
        Value oResult = FGetFileName.execute(_iCurrentTestFileId1);

        // Verify: Result contains the exact path with all special characters
        assertEquals(_oTempFile1.toString(), oResult.toString());
        assertTrue(oResult.toString().contains("fgetfilename-test-"));
    }

    /**
     * Test: FGetFileName returns correct type (StringValue).
     * <p>
     * Given: Any file ID (open or closed)
     * When: FGetFileName.execute() is called
     * Then: Always returns a Value that is a StringValue instance
     * <p>
     * Purpose: Verifies the function returns the correct type for proper integration
     */
    @Test
    public void testFGetFileName_AlwaysReturnsStringValueType() throws Exception {
        // Setup: Open a file
        _oFileManager.openFile(_oTempFile1.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FGetFileName with various IDs
        Value oOpenFile = FGetFileName.execute(_iCurrentTestFileId1);
        Value oClosedFile = FGetFileName.execute(FILE_ID_UNKNOWN);

        // Verify: Both results are StringValue instances
        assertTrue(oOpenFile instanceof StringValue);
        assertTrue(oClosedFile instanceof StringValue);
        assertTrue(oOpenFile instanceof Value);
        assertTrue(oClosedFile instanceof Value);
    }

    /**
     * Test: FGetFileName with sequential file IDs.
     * <p>
     * Given: Multiple files opened with sequential file IDs (1, 2, 3, etc.)
     * When: FGetFileName.execute() is called for each ID
     * Then: Returns correct StringValue for each sequential file ID
     * <p>
     * Purpose: Verifies the function correctly handles a sequence of file operations
     */
    @Test
    public void testFGetFileName_WithSequentialFileIds_ReturnsCorrectFiles() throws Exception {
        // Setup: Open files with sequential IDs
        Path oFile3 = Files.createTempFile("fgetfilename-test-", ".txt");
        _oFileManager.openFile(_oTempFile1.toString(), _iCurrentTestFileId1, FileOpenType.READ);
        _oFileManager.openFile(_oTempFile2.toString(), _iCurrentTestFileId2, FileOpenType.WRITE);
        _oFileManager.openFile(oFile3.toString(), _iCurrentTestFileId3, FileOpenType.READ);

        // Execute: Call FGetFileName for each file ID
        Value oResult1 = FGetFileName.execute(_iCurrentTestFileId1);
        Value oResult2 = FGetFileName.execute(_iCurrentTestFileId2);
        Value oResult3 = FGetFileName.execute(_iCurrentTestFileId3);

        // Verify: Each result matches the corresponding file
        assertEquals(_oTempFile1.toString(), oResult1.toString());
        assertEquals(_oTempFile2.toString(), oResult2.toString());
        assertEquals(oFile3.toString(), oResult3.toString());
    }

    /**
     * Test: FGetFileName after closing one file of multiple.
     * <p>
     * Given: Multiple files open, one is closed
     * When: FGetFileName.execute() is called for open and closed file IDs
     * Then: Returns file name for open file, empty string for closed file
     * <p>
     * Purpose: Verifies correct state tracking when managing multiple files
     */
    @Test
    public void testFGetFileName_WithMixedOpenAndClosedFiles_ReturnCorrectly() throws Exception {
        // Setup: Open multiple files, then close one
        _oFileManager.openFile(_oTempFile1.toString(), _iCurrentTestFileId1, FileOpenType.READ);
        _oFileManager.openFile(_oTempFile2.toString(), _iCurrentTestFileId2, FileOpenType.WRITE);
        _oFileManager.closeFile(_iCurrentTestFileId1, false);

        // Execute: Call FGetFileName for both files
        Value oClosedFile = FGetFileName.execute(_iCurrentTestFileId1);
        Value oOpenFile = FGetFileName.execute(_iCurrentTestFileId2);

        // Verify: Closed file returns empty string, open file returns name
        assertEquals("", oClosedFile.toString());
        assertEquals(_oTempFile2.toString(), oOpenFile.toString());
    }
}
