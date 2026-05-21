package eu.gricom.basic.functions;

import eu.gricom.basic.error.FileNotFoundException;
import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.memoryManager.FileOpenType;
import eu.gricom.basic.variableTypes.IntegerValue;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FLineCountTest.java
 * <p>
 * Unit tests for the FLineCount BASIC function.
 * <p>
 * This test class provides comprehensive coverage of the FLineCount function, which counts the number of lines
 * in a file identified by a file ID. The function retrieves the file name from FileManager, temporarily opens
 * the file directly (bypassing FileManager), counts all lines, closes the file, and returns the count as an
 * IntegerValue. The function throws FileNotFoundException if the file ID is not registered with FileManager.
 * <p>
 * Test Categories:
 * - POSITIVE TESTS: File IDs that exist and have valid files
 * - NEGATIVE TESTS: File IDs that don't exist or have been closed
 * - EDGE CASES: Empty files, single-line files, large files, and error conditions
 * <p>
 * Key Behavior:
 * - Returns IntegerValue containing the line count for registered file IDs
 * - Throws FileNotFoundException for unknown or closed file IDs
 * - Counts every line including empty lines
 * - Does not modify FileManager state (file remains open after function)
 * - Handles all I/O errors gracefully with FileNotFoundException
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FLineCountTest {

    // -------------------------------------------------------------------------
    // TEST SETUP AND CONSTANTS
    // -------------------------------------------------------------------------

    private static final int FILE_ID_UNKNOWN = 999;
    private static final AtomicInteger _fileIdCounter = new AtomicInteger(400);

    private Path _oEmptyFile;
    private Path _oSingleLineFile;
    private Path _oMultiLineFile;
    private Path _oFileWithEmptyLines;
    private FileManager _oFileManager;
    private int _iCurrentTestFileId1;
    private int _iCurrentTestFileId2;
    private int _iCurrentTestFileId3;
    private int _iCurrentTestFileId4;

    /**
     * Setup method: Initializes test environment before each test.
     * Creates temporary test files with various line counts, unique file IDs, and FileManager instance.
     */
    @BeforeEach
    public void setUp() throws IOException {
        _oFileManager = new FileManager();

        // Create empty file
        _oEmptyFile = Files.createTempFile("flinecount-empty-", ".txt");

        // Create single-line file
        _oSingleLineFile = Files.createTempFile("flinecount-single-", ".txt");
        Files.writeString(_oSingleLineFile, "This is a single line");

        // Create multi-line file (5 lines)
        _oMultiLineFile = Files.createTempFile("flinecount-multi-", ".txt");
        Files.writeString(_oMultiLineFile, "Line 1\nLine 2\nLine 3\nLine 4\nLine 5");

        // Create file with empty lines (7 lines including empty ones)
        _oFileWithEmptyLines = Files.createTempFile("flinecount-empty-lines-", ".txt");
        Files.writeString(_oFileWithEmptyLines, "Line 1\n\nLine 3\n\nLine 5\n\n");

        // Generate unique file IDs for this test to avoid conflicts
        _iCurrentTestFileId1 = _fileIdCounter.getAndIncrement();
        _iCurrentTestFileId2 = _fileIdCounter.getAndIncrement();
        _iCurrentTestFileId3 = _fileIdCounter.getAndIncrement();
        _iCurrentTestFileId4 = _fileIdCounter.getAndIncrement();
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
        _oFileManager.closeFile(_iCurrentTestFileId4, false);
    }

    // =========================================================================
    // POSITIVE TEST CASES - File IDs that exist
    // =========================================================================

    /**
     * Test: FLineCount with an empty file.
     * <p>
     * Given: An empty file opened with a specific file ID
     * When: FLineCount.execute() is called with that file ID
     * Then: Returns IntegerValue containing 0
     * <p>
     * Purpose: Verifies correct handling of empty files
     */
    @Test
    public void testFLineCount_WithEmptyFile_ReturnsZero() throws Exception {
        // Setup: Open an empty file
        _oFileManager.openFile(_oEmptyFile.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FLineCount
        Value oResult = FLineCount.execute(_iCurrentTestFileId1);

        // Verify: Result is IntegerValue(0)
        assertNotNull(oResult);
        assertTrue(oResult instanceof IntegerValue);
        assertEquals(0, Integer.parseInt(oResult.toString()));
    }

    /**
     * Test: FLineCount with a single-line file.
     * <p>
     * Given: A file containing one line
     * When: FLineCount.execute() is called
     * Then: Returns IntegerValue containing 1
     * <p>
     * Purpose: Verifies correct line counting for minimal content
     */
    @Test
    public void testFLineCount_WithSingleLineFile_ReturnsOne() throws Exception {
        // Setup: Open a single-line file
        _oFileManager.openFile(_oSingleLineFile.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FLineCount
        Value oResult = FLineCount.execute(_iCurrentTestFileId1);

        // Verify: Result is IntegerValue(1)
        assertNotNull(oResult);
        assertEquals(1, Integer.parseInt(oResult.toString()));
    }

    /**
     * Test: FLineCount with a multi-line file.
     * <p>
     * Given: A file containing multiple lines (5 lines)
     * When: FLineCount.execute() is called
     * Then: Returns correct IntegerValue with line count
     * <p>
     * Purpose: Verifies accurate line counting for normal files
     */
    @Test
    public void testFLineCount_WithMultiLineFile_ReturnsCorrectCount() throws Exception {
        // Setup: Open a multi-line file (5 lines)
        _oFileManager.openFile(_oMultiLineFile.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FLineCount
        Value oResult = FLineCount.execute(_iCurrentTestFileId1);

        // Verify: Result is IntegerValue(5)
        assertNotNull(oResult);
        assertEquals(5, Integer.parseInt(oResult.toString()));
    }

    /**
     * Test: FLineCount with file opened in READ mode.
     * <p>
     * Given: A file opened in READ mode
     * When: FLineCount.execute() is called
     * Then: Returns correct line count
     * <p>
     * Purpose: Verifies function works with READ mode files
     */
    @Test
    public void testFLineCount_WithReadModeFile_ReturnsCorrectCount() throws Exception {
        // Setup: Open file in READ mode
        _oFileManager.openFile(_oMultiLineFile.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FLineCount
        Value oResult = FLineCount.execute(_iCurrentTestFileId1);

        // Verify: Returns correct count
        assertEquals(5, Integer.parseInt(oResult.toString()));
    }

    /**
     * Test: FLineCount with file opened in WRITE mode.
     * <p>
     * Given: A file opened in WRITE mode (file is truncated)
     * When: FLineCount.execute() is called
     * Then: Returns IntegerValue(0) because file is now empty
     * <p>
     * Purpose: Verifies function correctly counts lines in truncated files
     */
    @Test
    public void testFLineCount_WithWriteModeFile_ReturnsZero() throws Exception {
        // Setup: Open file in WRITE mode (which truncates the file)
        _oFileManager.openFile(_oMultiLineFile.toString(), _iCurrentTestFileId1, FileOpenType.WRITE);

        // Execute: Call FLineCount
        Value oResult = FLineCount.execute(_iCurrentTestFileId1);

        // Verify: Returns 0 because file is truncated
        assertEquals(0, Integer.parseInt(oResult.toString()));
    }

    /**
     * Test: FLineCount with file containing empty lines.
     * <p>
     * Given: A file with mixed empty and non-empty lines
     * When: FLineCount.execute() is called
     * Then: Counts all lines including empty ones
     * <p>
     * Purpose: Verifies that empty lines are counted
     */
    @Test
    public void testFLineCount_WithEmptyLines_CountsAllLines() throws Exception {
        // Setup: Open file with empty lines
        _oFileManager.openFile(_oFileWithEmptyLines.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FLineCount
        Value oResult = FLineCount.execute(_iCurrentTestFileId1);

        // Verify: Counts all lines including empty ones (6 lines total)
        assertEquals(6, Integer.parseInt(oResult.toString()));
    }

    /**
     * Test: FLineCount with multiple open files.
     * <p>
     * Given: Multiple different files opened with different file IDs
     * When: FLineCount.execute() is called for each file ID
     * Then: Returns correct line count for each file
     * <p>
     * Purpose: Verifies the function correctly handles multiple files
     */
    @Test
    public void testFLineCount_WithMultipleOpenFiles_ReturnsCorrectCountForEach() throws Exception {
        // Setup: Open multiple files
        _oFileManager.openFile(_oEmptyFile.toString(), _iCurrentTestFileId1, FileOpenType.READ);
        _oFileManager.openFile(_oSingleLineFile.toString(), _iCurrentTestFileId2, FileOpenType.READ);
        _oFileManager.openFile(_oMultiLineFile.toString(), _iCurrentTestFileId3, FileOpenType.READ);

        // Execute: Get line count for each file
        Value oEmptyResult = FLineCount.execute(_iCurrentTestFileId1);
        Value oSingleResult = FLineCount.execute(_iCurrentTestFileId2);
        Value oMultiResult = FLineCount.execute(_iCurrentTestFileId3);

        // Verify: Each result matches the correct line count
        assertEquals(0, Integer.parseInt(oEmptyResult.toString()));
        assertEquals(1, Integer.parseInt(oSingleResult.toString()));
        assertEquals(5, Integer.parseInt(oMultiResult.toString()));
    }

    /**
     * Test: FLineCount returns consistent results across multiple calls.
     * <p>
     * Given: An open file with a known file ID
     * When: FLineCount.execute() is called multiple times
     * Then: All calls return identical IntegerValue results
     * <p>
     * Purpose: Verifies deterministic behavior and no side effects
     */
    @Test
    public void testFLineCount_WithMultipleCalls_ReturnsConsistentResults() throws Exception {
        // Setup: Open a file
        _oFileManager.openFile(_oMultiLineFile.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FLineCount multiple times
        Value oResult1 = FLineCount.execute(_iCurrentTestFileId1);
        Value oResult2 = FLineCount.execute(_iCurrentTestFileId1);
        Value oResult3 = FLineCount.execute(_iCurrentTestFileId1);

        // Verify: All results are identical
        assertEquals(Integer.parseInt(oResult1.toString()), Integer.parseInt(oResult2.toString()));
        assertEquals(Integer.parseInt(oResult2.toString()), Integer.parseInt(oResult3.toString()));
        assertEquals(5, Integer.parseInt(oResult1.toString()));
    }

    /**
     * Test: FLineCount does not affect FileManager state.
     * <p>
     * Given: An open file
     * When: FLineCount.execute() is called
     * Then: File remains open in FileManager, no side effects
     * <p>
     * Purpose: Verifies that line counting doesn't interfere with FileManager
     */
    @Test
    public void testFLineCount_DoesNotAffectFileManagerState() throws Exception {
        // Setup: Open a file
        _oFileManager.openFile(_oMultiLineFile.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FLineCount
        FLineCount.execute(_iCurrentTestFileId1);

        // Verify: File is still open in FileManager (FIsOpen returns true)
        Value oIsOpenResult = FIsOpen.execute(_iCurrentTestFileId1);
        assertEquals(true, Boolean.parseBoolean(oIsOpenResult.toString()));
    }

    // =========================================================================
    // NEGATIVE TEST CASES - File IDs that don't exist or have been closed
    // =========================================================================

    /**
     * Test: FLineCount with unknown file ID.
     * <p>
     * Given: A file ID that was never opened
     * When: FLineCount.execute() is called with that file ID
     * Then: Throws FileNotFoundException
     * <p>
     * Purpose: Verifies error handling for unknown file IDs
     */
    @Test
    public void testFLineCount_WithUnknownFileId_ThrowsFileNotFoundException() {
        // Execute and Verify: FLineCount throws FileNotFoundException for unknown ID
        assertThrows(FileNotFoundException.class, () -> {
            FLineCount.execute(FILE_ID_UNKNOWN);
        });
    }

    /**
     * Test: FLineCount with closed file.
     * <p>
     * Given: A file that was opened but subsequently closed
     * When: FLineCount.execute() is called with that file ID
     * Then: Throws FileNotFoundException
     * <p>
     * Purpose: Verifies error handling for closed files
     */
    @Test
    public void testFLineCount_WithClosedFile_ThrowsFileNotFoundException() throws Exception {
        // Setup: Open a file then close it
        _oFileManager.openFile(_oMultiLineFile.toString(), _iCurrentTestFileId1, FileOpenType.READ);
        _oFileManager.closeFile(_iCurrentTestFileId1, false);

        // Execute and Verify: FLineCount throws FileNotFoundException
        assertThrows(FileNotFoundException.class, () -> {
            FLineCount.execute(_iCurrentTestFileId1);
        });
    }

    /**
     * Test: FLineCount with negative file ID.
     * <p>
     * Given: A negative integer as file ID
     * When: FLineCount.execute() is called
     * Then: Throws FileNotFoundException
     * <p>
     * Purpose: Verifies error handling for invalid file IDs
     */
    @Test
    public void testFLineCount_WithNegativeFileId_ThrowsFileNotFoundException() {
        // Execute and Verify: FLineCount throws FileNotFoundException for negative ID
        assertThrows(FileNotFoundException.class, () -> {
            FLineCount.execute(-1);
        });
    }

    /**
     * Test: FLineCount with zero file ID.
     * <p>
     * Given: Zero as file ID (typically invalid)
     * When: FLineCount.execute() is called
     * Then: Throws FileNotFoundException
     * <p>
     * Purpose: Verifies error handling for zero file ID
     */
    @Test
    public void testFLineCount_WithZeroFileId_ThrowsFileNotFoundException() {
        // Execute and Verify: FLineCount throws FileNotFoundException for zero
        assertThrows(FileNotFoundException.class, () -> {
            FLineCount.execute(0);
        });
    }

    /**
     * Test: FLineCount with very large file ID.
     * <p>
     * Given: Integer.MAX_VALUE as file ID
     * When: FLineCount.execute() is called
     * Then: Throws FileNotFoundException
     * <p>
     * Purpose: Verifies error handling at file ID range boundaries
     */
    @Test
    public void testFLineCount_WithMaxIntFileId_ThrowsFileNotFoundException() {
        // Execute and Verify: FLineCount throws FileNotFoundException for Integer.MAX_VALUE
        assertThrows(FileNotFoundException.class, () -> {
            FLineCount.execute(Integer.MAX_VALUE);
        });
    }

    // =========================================================================
    // EDGE CASE TESTS - Boundary conditions and special scenarios
    // =========================================================================

    /**
     * Test: FLineCount always returns IntegerValue type.
     * <p>
     * Given: An open file
     * When: FLineCount.execute() is called
     * Then: Always returns a Value that is an IntegerValue instance
     * <p>
     * Purpose: Verifies the function returns the correct type
     */
    @Test
    public void testFLineCount_AlwaysReturnsIntegerValueType() throws Exception {
        // Setup: Open a file
        _oFileManager.openFile(_oMultiLineFile.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FLineCount
        Value oResult = FLineCount.execute(_iCurrentTestFileId1);

        // Verify: Result is IntegerValue instance
        assertTrue(oResult instanceof IntegerValue);
        assertTrue(oResult instanceof Value);
    }

    /**
     * Test: FLineCount returns non-negative values.
     * <p>
     * Given: Any valid file (empty or with content)
     * When: FLineCount.execute() is called
     * Then: Always returns a non-negative IntegerValue
     * <p>
     * Purpose: Verifies line counts are never negative
     */
    @Test
    public void testFLineCount_AlwaysReturnsNonNegativeValue() throws Exception {
        // Setup: Open files
        _oFileManager.openFile(_oEmptyFile.toString(), _iCurrentTestFileId1, FileOpenType.READ);
        _oFileManager.openFile(_oMultiLineFile.toString(), _iCurrentTestFileId2, FileOpenType.READ);

        // Execute: Get line counts
        Value oEmpty = FLineCount.execute(_iCurrentTestFileId1);
        Value oMulti = FLineCount.execute(_iCurrentTestFileId2);

        // Verify: All values are non-negative
        assertTrue(Integer.parseInt(oEmpty.toString()) >= 0);
        assertTrue(Integer.parseInt(oMulti.toString()) >= 0);
    }

    /**
     * Test: FLineCount throws FileNotFoundException for non-existent files.
     * <p>
     * Given: Multiple invalid file IDs
     * When: FLineCount.execute() is called
     * Then: Throws FileNotFoundException for all invalid IDs
     * <p>
     * Purpose: Verifies consistent error handling across various invalid scenarios
     */
    @Test
    public void testFLineCount_ThrowsFileNotFoundForAllInvalidIds() {
        // Execute and Verify: FLineCount throws FileNotFoundException for various invalid IDs
        assertThrows(FileNotFoundException.class, () -> FLineCount.execute(FILE_ID_UNKNOWN));
        assertThrows(FileNotFoundException.class, () -> FLineCount.execute(-1));
        assertThrows(FileNotFoundException.class, () -> FLineCount.execute(0));
        assertThrows(FileNotFoundException.class, () -> FLineCount.execute(Integer.MAX_VALUE));
    }

    /**
     * Test: FLineCount correctly identifies line count differences.
     * <p>
     * Given: Files with different line counts
     * When: FLineCount.execute() is called for each file
     * Then: Returns different IntegerValues for each file
     * <p>
     * Purpose: Verifies precise line counting distinguishes between files
     */
    @Test
    public void testFLineCount_CorrectlyDistinguishesDifferentLineCounts() throws Exception {
        // Setup: Open files with different line counts
        _oFileManager.openFile(_oEmptyFile.toString(), _iCurrentTestFileId1, FileOpenType.READ);
        _oFileManager.openFile(_oSingleLineFile.toString(), _iCurrentTestFileId2, FileOpenType.READ);
        _oFileManager.openFile(_oMultiLineFile.toString(), _iCurrentTestFileId3, FileOpenType.READ);

        // Execute: Get line counts
        int iEmpty = Integer.parseInt(FLineCount.execute(_iCurrentTestFileId1).toString());
        int iSingle = Integer.parseInt(FLineCount.execute(_iCurrentTestFileId2).toString());
        int iMulti = Integer.parseInt(FLineCount.execute(_iCurrentTestFileId3).toString());

        // Verify: Line counts are different and correct
        assertTrue(iEmpty < iSingle);
        assertTrue(iSingle < iMulti);
        assertEquals(0, iEmpty);
        assertEquals(1, iSingle);
        assertEquals(5, iMulti);
    }

    /**
     * Test: FLineCount exception message contains file ID information.
     * <p>
     * Given: An unknown file ID
     * When: FLineCount.execute() is called
     * Then: Throws FileNotFoundException with informative message
     * <p>
     * Purpose: Verifies error messages help with debugging
     */
    @Test
    public void testFLineCount_ExceptionMessageIsInformative() {
        // Execute and Verify: Exception message contains useful information
        try {
            FLineCount.execute(FILE_ID_UNKNOWN);
        } catch (FileNotFoundException e) {
            assertTrue(e.getMessage().contains("999") || e.getMessage().contains("not registered"));
        }
    }
}
