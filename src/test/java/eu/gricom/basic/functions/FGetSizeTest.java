package eu.gricom.basic.functions;

import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.memoryManager.FileOpenType;
import eu.gricom.basic.variableTypes.LongValue;
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
 * FGetSizeTest.java
 * <p>
 * Unit tests for the FGetSize BASIC function.
 * <p>
 * This test class provides comprehensive coverage of the FGetSize function, which retrieves
 * the file size in bytes for a file associated with a given file ID. The function queries
 * the operating system's file system to determine the current size of the file.
 * <p>
 * Test Categories:
 * - POSITIVE TESTS: Files with various sizes and content
 * - NEGATIVE TESTS: Unknown file IDs, closed files, missing files
 * - EDGE CASES: Empty files, large files, special characters in paths
 * <p>
 * Key Behavior:
 * - Returns LongValue containing file size in bytes for open files
 * - Returns LongValue(0) for unknown/closed file IDs
 * - Returns LongValue(0) for inaccessible files
 * - Never returns null; always returns a LongValue
 * - Size is retrieved from the actual file system on each call (not cached)
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FGetSizeTest {

    // -------------------------------------------------------------------------
    // TEST SETUP AND CONSTANTS
    // -------------------------------------------------------------------------

    private static final int FILE_ID_UNKNOWN = 999;
    private static final AtomicInteger _fileIdCounter = new AtomicInteger(200);

    private Path _oTempFileEmpty;
    private Path _oTempFileSmall;
    private Path _oTempFileLarge;
    private FileManager _oFileManager;
    private int _iCurrentTestFileId1;
    private int _iCurrentTestFileId2;
    private int _iCurrentTestFileId3;

    /**
     * Setup method: Initializes test environment before each test.
     * Creates temporary test files with different sizes and unique file IDs.
     */
    @BeforeEach
    public void setUp() throws IOException {
        _oFileManager = new FileManager();

        // Create test files with different sizes
        _oTempFileEmpty = Files.createTempFile("fgetsize-empty-", ".txt");
        _oTempFileSmall = Files.createTempFile("fgetsize-small-", ".txt");
        _oTempFileLarge = Files.createTempFile("fgetsize-large-", ".txt");

        // Leave empty file empty (0 bytes)
        // Create small file with known content
        Files.writeString(_oTempFileSmall, "This is a small test file with some content.");
        // Create large file with multiple lines
        StringBuilder largeContent = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            largeContent.append("This is line ").append(i + 1).append(" of the large test file.\n");
        }
        Files.writeString(_oTempFileLarge, largeContent.toString());

        // Generate unique file IDs for this test
        _iCurrentTestFileId1 = _fileIdCounter.getAndIncrement();
        _iCurrentTestFileId2 = _fileIdCounter.getAndIncrement();
        _iCurrentTestFileId3 = _fileIdCounter.getAndIncrement();
    }

    /**
     * Teardown method: Cleans up after each test.
     * Closes any open files and deletes temporary test files.
     */
    @AfterEach
    public void tearDown() throws IOException {
        _oFileManager.closeFile(_iCurrentTestFileId1, false);
        _oFileManager.closeFile(_iCurrentTestFileId2, false);
        _oFileManager.closeFile(_iCurrentTestFileId3, false);

        // Delete temporary files
        Files.deleteIfExists(_oTempFileEmpty);
        Files.deleteIfExists(_oTempFileSmall);
        Files.deleteIfExists(_oTempFileLarge);
    }

    // =========================================================================
    // POSITIVE TEST CASES - Files with various sizes
    // =========================================================================

    /**
     * Test: FGetSize with an empty file.
     * <p>
     * Given: An empty file (0 bytes) opened with a specific file ID
     * When: FGetSize.execute() is called with that file ID
     * Then: Returns LongValue(0) indicating zero bytes
     * <p>
     * Purpose: Verifies correct handling of empty files
     */
    @Test
    public void testFGetSize_WithEmptyFile_ReturnsZero() throws Exception {
        // Setup: Open an empty file
        _oFileManager.openFile(_oTempFileEmpty.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FGetSize with the file ID
        Value oResult = FGetSize.execute(_iCurrentTestFileId1);

        // Verify: Result is a LongValue containing 0
        assertNotNull(oResult);
        assertTrue(oResult instanceof LongValue);
        assertEquals(0L, Long.parseLong(oResult.toString()));
    }

    /**
     * Test: FGetSize with a small file.
     * <p>
     * Given: A file with known content opened with a specific file ID
     * When: FGetSize.execute() is called with that file ID
     * Then: Returns LongValue matching the actual file size in bytes
     * <p>
     * Purpose: Verifies correct file size retrieval for typical files
     */
    @Test
    public void testFGetSize_WithSmallFile_ReturnsCorrectSize() throws Exception {
        // Setup: Get the expected file size and open the file
        long lExpectedSize = Files.size(_oTempFileSmall);
        _oFileManager.openFile(_oTempFileSmall.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FGetSize with the file ID
        Value oResult = FGetSize.execute(_iCurrentTestFileId1);

        // Verify: Result matches the expected file size
        assertNotNull(oResult);
        assertEquals(lExpectedSize, Long.parseLong(oResult.toString()));
    }

    /**
     * Test: FGetSize with a larger file.
     * <p>
     * Given: A file with substantial content opened with a specific file ID
     * When: FGetSize.execute() is called with that file ID
     * Then: Returns LongValue matching the actual larger file size
     * <p>
     * Purpose: Verifies correct size retrieval for files larger than small files
     */
    @Test
    public void testFGetSize_WithLargeFile_ReturnsCorrectSize() throws Exception {
        // Setup: Get the expected file size and open the file
        long lExpectedSize = Files.size(_oTempFileLarge);
        _oFileManager.openFile(_oTempFileLarge.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FGetSize with the file ID
        Value oResult = FGetSize.execute(_iCurrentTestFileId1);

        // Verify: Result matches the expected file size
        assertNotNull(oResult);
        assertTrue(Long.parseLong(oResult.toString()) > 0);
        assertEquals(lExpectedSize, Long.parseLong(oResult.toString()));
    }

    /**
     * Test: FGetSize with READ mode file.
     * <p>
     * Given: A file opened in READ mode
     * When: FGetSize.execute() is called
     * Then: Returns correct file size (mode doesn't affect size)
     * <p>
     * Purpose: Verifies function works with READ mode files
     */
    @Test
    public void testFGetSize_WithReadModeFile_ReturnsCorrectSize() throws Exception {
        // Setup: Open file in READ mode
        long lExpectedSize = Files.size(_oTempFileSmall);
        _oFileManager.openFile(_oTempFileSmall.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FGetSize
        Value oResult = FGetSize.execute(_iCurrentTestFileId1);

        // Verify: Size is correct
        assertEquals(lExpectedSize, Long.parseLong(oResult.toString()));
    }

    /**
     * Test: FGetSize with WRITE mode file.
     * <p>
     * Given: A file opened in WRITE mode
     * When: FGetSize.execute() is called
     * Then: Returns correct file size
     * <p>
     * Purpose: Verifies function works with WRITE mode files
     */
    @Test
    public void testFGetSize_WithWriteModeFile_ReturnsCorrectSize() throws Exception {
        // Setup: Open file in WRITE mode (which truncates the file to 0 bytes)
        _oFileManager.openFile(_oTempFileSmall.toString(), _iCurrentTestFileId1, FileOpenType.WRITE);
        long lExpectedSize = Files.size(_oTempFileSmall);  // Capture size AFTER opening in WRITE mode

        // Execute: Call FGetSize
        Value oResult = FGetSize.execute(_iCurrentTestFileId1);

        // Verify: Size is correct (should be 0 because WRITE mode truncates the file)
        assertEquals(lExpectedSize, Long.parseLong(oResult.toString()));
    }

    /**
     * Test: FGetSize with multiple files returns correct size for each.
     * <p>
     * Given: Multiple files of different sizes opened with different file IDs
     * When: FGetSize.execute() is called for each file ID
     * Then: Returns correct LongValue for each file's size
     * <p>
     * Purpose: Verifies function correctly retrieves size for multiple files
     */
    @Test
    public void testFGetSize_WithMultipleFiles_ReturnsCorrectSizeForEach() throws Exception {
        // Setup: Open multiple files
        long lEmptySize = Files.size(_oTempFileEmpty);
        long lSmallSize = Files.size(_oTempFileSmall);
        long lLargeSize = Files.size(_oTempFileLarge);

        _oFileManager.openFile(_oTempFileEmpty.toString(), _iCurrentTestFileId1, FileOpenType.READ);
        _oFileManager.openFile(_oTempFileSmall.toString(), _iCurrentTestFileId2, FileOpenType.READ);
        _oFileManager.openFile(_oTempFileLarge.toString(), _iCurrentTestFileId3, FileOpenType.READ);

        // Execute: Get size for each file
        Value oEmptyResult = FGetSize.execute(_iCurrentTestFileId1);
        Value oSmallResult = FGetSize.execute(_iCurrentTestFileId2);
        Value oLargeResult = FGetSize.execute(_iCurrentTestFileId3);

        // Verify: Each result matches the correct file size
        assertEquals(lEmptySize, Long.parseLong(oEmptyResult.toString()));
        assertEquals(lSmallSize, Long.parseLong(oSmallResult.toString()));
        assertEquals(lLargeSize, Long.parseLong(oLargeResult.toString()));
    }

    /**
     * Test: FGetSize returns consistent results on multiple calls.
     * <p>
     * Given: An open file with a known file ID
     * When: FGetSize.execute() is called multiple times
     * Then: All calls return identical LongValue results
     * <p>
     * Purpose: Verifies deterministic behavior and no side effects
     */
    @Test
    public void testFGetSize_WithMultipleCalls_ReturnsConsistentResults() throws Exception {
        // Setup: Open a file
        long lExpectedSize = Files.size(_oTempFileSmall);
        _oFileManager.openFile(_oTempFileSmall.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FGetSize multiple times
        Value oResult1 = FGetSize.execute(_iCurrentTestFileId1);
        Value oResult2 = FGetSize.execute(_iCurrentTestFileId1);
        Value oResult3 = FGetSize.execute(_iCurrentTestFileId1);

        // Verify: All results are identical
        long lSize1 = Long.parseLong(oResult1.toString());
        long lSize2 = Long.parseLong(oResult2.toString());
        long lSize3 = Long.parseLong(oResult3.toString());

        assertEquals(lSize1, lSize2);
        assertEquals(lSize2, lSize3);
        assertEquals(lExpectedSize, lSize1);
    }

    // =========================================================================
    // NEGATIVE TEST CASES - Unknown or closed files
    // =========================================================================

    /**
     * Test: FGetSize with unknown file ID.
     * <p>
     * Given: A file ID that was never opened
     * When: FGetSize.execute() is called with that file ID
     * Then: Returns LongValue(0) gracefully
     * <p>
     * Purpose: Verifies graceful handling of invalid file IDs
     */
    @Test
    public void testFGetSize_WithUnknownFileId_ReturnsZero() throws Exception {
        // Execute: Call FGetSize with an unknown file ID
        Value oResult = FGetSize.execute(FILE_ID_UNKNOWN);

        // Verify: Result is LongValue(0)
        assertNotNull(oResult);
        assertTrue(oResult instanceof LongValue);
        assertEquals(0L, Long.parseLong(oResult.toString()));
    }

    /**
     * Test: FGetSize with a closed file.
     * <p>
     * Given: A file that was opened but subsequently closed
     * When: FGetSize.execute() is called with that file ID
     * Then: Returns LongValue(0)
     * <p>
     * Purpose: Verifies correct handling of closed file IDs
     */
    @Test
    public void testFGetSize_WithClosedFile_ReturnsZero() throws Exception {
        // Setup: Open a file then close it
        _oFileManager.openFile(_oTempFileSmall.toString(), _iCurrentTestFileId1, FileOpenType.READ);
        _oFileManager.closeFile(_iCurrentTestFileId1, false);

        // Execute: Call FGetSize after closing
        Value oResult = FGetSize.execute(_iCurrentTestFileId1);

        // Verify: Result is LongValue(0)
        assertNotNull(oResult);
        assertEquals(0L, Long.parseLong(oResult.toString()));
    }

    /**
     * Test: FGetSize with negative file ID.
     * <p>
     * Given: A negative integer as file ID
     * When: FGetSize.execute() is called
     * Then: Returns LongValue(0) gracefully
     * <p>
     * Purpose: Verifies handling of invalid file ID values
     */
    @Test
    public void testFGetSize_WithNegativeFileId_ReturnsZero() throws Exception {
        // Execute: Call FGetSize with negative file ID
        Value oResult = FGetSize.execute(-1);

        // Verify: Result is LongValue(0)
        assertNotNull(oResult);
        assertEquals(0L, Long.parseLong(oResult.toString()));
    }

    /**
     * Test: FGetSize with zero file ID.
     * <p>
     * Given: Zero as file ID (typically invalid)
     * When: FGetSize.execute() is called
     * Then: Returns LongValue(0)
     * <p>
     * Purpose: Verifies handling of zero file ID edge case
     */
    @Test
    public void testFGetSize_WithZeroFileId_ReturnsZero() throws Exception {
        // Execute: Call FGetSize with zero
        Value oResult = FGetSize.execute(0);

        // Verify: Result is LongValue(0)
        assertNotNull(oResult);
        assertEquals(0L, Long.parseLong(oResult.toString()));
    }

    // =========================================================================
    // EDGE CASE TESTS - Boundary conditions and special scenarios
    // =========================================================================

    /**
     * Test: FGetSize never returns null.
     * <p>
     * Given: Various invalid file IDs
     * When: FGetSize.execute() is called
     * Then: Always returns a non-null LongValue
     * <p>
     * Purpose: Verifies the function never returns null (always returns LongValue)
     */
    @Test
    public void testFGetSize_NeverReturnsNull() throws Exception {
        // Execute: Call FGetSize with various invalid IDs
        Value oResult1 = FGetSize.execute(FILE_ID_UNKNOWN);
        Value oResult2 = FGetSize.execute(-1);
        Value oResult3 = FGetSize.execute(Integer.MAX_VALUE);

        // Verify: All results are non-null LongValues
        assertNotNull(oResult1);
        assertNotNull(oResult2);
        assertNotNull(oResult3);
        assertTrue(oResult1 instanceof LongValue);
        assertTrue(oResult2 instanceof LongValue);
        assertTrue(oResult3 instanceof LongValue);
    }

    /**
     * Test: FGetSize returns correct type (LongValue).
     * <p>
     * Given: Any file ID (open or unknown)
     * When: FGetSize.execute() is called
     * Then: Always returns a Value that is a LongValue instance
     * <p>
     * Purpose: Verifies the function returns the correct type for proper integration
     */
    @Test
    public void testFGetSize_AlwaysReturnsLongValueType() throws Exception {
        // Setup: Open a file
        _oFileManager.openFile(_oTempFileSmall.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FGetSize with various IDs
        Value oOpenFile = FGetSize.execute(_iCurrentTestFileId1);
        Value oClosedFile = FGetSize.execute(FILE_ID_UNKNOWN);

        // Verify: Both results are LongValue instances
        assertTrue(oOpenFile instanceof LongValue);
        assertTrue(oClosedFile instanceof LongValue);
        assertTrue(oOpenFile instanceof Value);
        assertTrue(oClosedFile instanceof Value);
    }

    /**
     * Test: FGetSize returns non-negative values.
     * <p>
     * Given: Any file (open or closed)
     * When: FGetSize.execute() is called
     * Then: Always returns a non-negative LongValue
     * <p>
     * Purpose: Verifies file sizes are never negative (they can't be)
     */
    @Test
    public void testFGetSize_AlwaysReturnsNonNegativeValue() throws Exception {
        // Setup: Open files
        _oFileManager.openFile(_oTempFileEmpty.toString(), _iCurrentTestFileId1, FileOpenType.READ);
        _oFileManager.openFile(_oTempFileSmall.toString(), _iCurrentTestFileId2, FileOpenType.READ);

        // Execute: Get sizes
        Value oEmpty = FGetSize.execute(_iCurrentTestFileId1);
        Value oSmall = FGetSize.execute(_iCurrentTestFileId2);
        Value oUnknown = FGetSize.execute(FILE_ID_UNKNOWN);

        // Verify: All values are non-negative
        assertTrue(Long.parseLong(oEmpty.toString()) >= 0);
        assertTrue(Long.parseLong(oSmall.toString()) >= 0);
        assertTrue(Long.parseLong(oUnknown.toString()) >= 0);
    }

    /**
     * Test: FGetSize with absolute file path.
     * <p>
     * Given: A file opened with an absolute path
     * When: FGetSize.execute() is called
     * Then: Returns correct size regardless of path format
     * <p>
     * Purpose: Verifies function works with absolute paths
     */
    @Test
    public void testFGetSize_WithAbsolutePath_ReturnsCorrectSize() throws Exception {
        // Setup: Get absolute path and open file
        String strAbsolutePath = _oTempFileSmall.toAbsolutePath().toString();
        long lExpectedSize = Files.size(_oTempFileSmall);
        _oFileManager.openFile(strAbsolutePath, _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FGetSize
        Value oResult = FGetSize.execute(_iCurrentTestFileId1);

        // Verify: Result matches expected size
        assertEquals(lExpectedSize, Long.parseLong(oResult.toString()));
    }

    /**
     * Test: FGetSize with very large file ID.
     * <p>
     * Given: Integer.MAX_VALUE as file ID
     * When: FGetSize.execute() is called
     * Then: Returns LongValue(0) gracefully
     * <p>
     * Purpose: Verifies boundary handling at upper limit of file ID range
     */
    @Test
    public void testFGetSize_WithMaxIntFileId_ReturnsZero() throws Exception {
        // Execute: Call FGetSize with Integer.MAX_VALUE
        Value oResult = FGetSize.execute(Integer.MAX_VALUE);

        // Verify: Result is LongValue(0)
        assertNotNull(oResult);
        assertEquals(0L, Long.parseLong(oResult.toString()));
    }

    /**
     * Test: FGetSize with mixed open and closed files.
     * <p>
     * Given: Multiple files where some are open and some are closed
     * When: FGetSize.execute() is called for each
     * Then: Open files return correct size, closed files return 0
     * <p>
     * Purpose: Verifies correct state tracking with mixed file states
     */
    @Test
    public void testFGetSize_WithMixedOpenAndClosedFiles_ReturnsCorrectly() throws Exception {
        // Setup: Open multiple files, then close one
        long lSmallSize = Files.size(_oTempFileSmall);
        long lLargeSize = Files.size(_oTempFileLarge);

        _oFileManager.openFile(_oTempFileSmall.toString(), _iCurrentTestFileId1, FileOpenType.READ);
        _oFileManager.openFile(_oTempFileLarge.toString(), _iCurrentTestFileId2, FileOpenType.READ);
        _oFileManager.closeFile(_iCurrentTestFileId1, false);

        // Execute: Get sizes for both files
        Value oClosedFile = FGetSize.execute(_iCurrentTestFileId1);
        Value oOpenFile = FGetSize.execute(_iCurrentTestFileId2);

        // Verify: Closed file returns 0, open file returns correct size
        assertEquals(0L, Long.parseLong(oClosedFile.toString()));
        assertEquals(lLargeSize, Long.parseLong(oOpenFile.toString()));
    }
}
