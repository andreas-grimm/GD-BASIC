package eu.gricom.basic.functions;

import eu.gricom.basic.error.FileNotFoundException;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FModTimeTest.java
 * <p>
 * Unit tests for the FModTime BASIC function.
 * <p>
 * This test class provides comprehensive coverage of the FModTime function, which retrieves the date
 * and time of the last modification of a file identified by a file ID. The function retrieves the file
 * name from FileManager, queries the operating system for the modification timestamp, and returns the
 * date and time as a formatted string. The function throws FileNotFoundException if the file ID is not
 * registered with FileManager.
 * <p>
 * Test Categories:
 * - POSITIVE TESTS: File IDs that exist and have valid files
 * - NEGATIVE TESTS: File IDs that don't exist or have been closed
 * - EDGE CASES: Format verification, consistency, and error conditions
 * <p>
 * Key Behavior:
 * - Returns StringValue containing date and time in format "yyyy-MM-dd HH:mm:ss"
 * - Throws FileNotFoundException for unknown or closed file IDs
 * - Does not modify FileManager state (file remains open after function)
 * - Handles all I/O errors gracefully with FileNotFoundException
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FModTimeTest {

    // -------------------------------------------------------------------------
    // TEST SETUP AND CONSTANTS
    // -------------------------------------------------------------------------

    private static final int FILE_ID_UNKNOWN = 999;
    private static final AtomicInteger _fileIdCounter = new AtomicInteger(500);

    private Path _oTestFile1;
    private Path _oTestFile2;
    private Path _oTestFile3;
    private FileManager _oFileManager;
    private int _iCurrentTestFileId1;
    private int _iCurrentTestFileId2;
    private int _iCurrentTestFileId3;

    /**
     * Setup method: Initializes test environment before each test.
     * Creates temporary test files, unique file IDs, and a FileManager instance.
     */
    @BeforeEach
    public void setUp() throws IOException {
        _oFileManager = new FileManager();

        // Create test files
        _oTestFile1 = Files.createTempFile("fmodtime-test-", ".txt");
        _oTestFile2 = Files.createTempFile("fmodtime-test-", ".txt");
        _oTestFile3 = Files.createTempFile("fmodtime-test-", ".txt");
        Files.writeString(_oTestFile1, "Test content for file 1");
        Files.writeString(_oTestFile2, "Test content for file 2");
        Files.writeString(_oTestFile3, "Test content for file 3");

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
     * Test: FModTime with a READ mode file.
     * <p>
     * Given: A file opened in READ mode
     * When: FModTime.execute() is called
     * Then: Returns StringValue with formatted date and time
     * <p>
     * Purpose: Verifies basic functionality with READ mode files
     */
    @Test
    public void testFModTime_WithReadModeFile_ReturnsFormattedDateTime() throws Exception {
        // Setup: Open file in READ mode
        _oFileManager.openFile(_oTestFile1.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FModTime
        Value oResult = FModTime.execute(_iCurrentTestFileId1);

        // Verify: Result is StringValue with proper format
        assertNotNull(oResult);
        assertTrue(oResult instanceof StringValue);
        String strDateTime = oResult.toString();
        assertTrue(strDateTime.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    /**
     * Test: FModTime with a WRITE mode file.
     * <p>
     * Given: A file opened in WRITE mode (file is truncated)
     * When: FModTime.execute() is called
     * Then: Returns correct modification time
     * <p>
     * Purpose: Verifies function works with WRITE mode files
     */
    @Test
    public void testFModTime_WithWriteModeFile_ReturnsFormattedDateTime() throws Exception {
        // Setup: Open file in WRITE mode
        _oFileManager.openFile(_oTestFile1.toString(), _iCurrentTestFileId1, FileOpenType.WRITE);

        // Execute: Call FModTime
        Value oResult = FModTime.execute(_iCurrentTestFileId1);

        // Verify: Result is formatted date and time
        assertNotNull(oResult);
        String strDateTime = oResult.toString();
        assertTrue(strDateTime.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    /**
     * Test: FModTime with multiple open files.
     * <p>
     * Given: Multiple different files opened with different file IDs
     * When: FModTime.execute() is called for each file ID
     * Then: Returns valid date and time for each file
     * <p>
     * Purpose: Verifies the function correctly handles multiple files
     */
    @Test
    public void testFModTime_WithMultipleOpenFiles_ReturnsValidDateTimeForEach() throws Exception {
        // Setup: Open multiple files
        _oFileManager.openFile(_oTestFile1.toString(), _iCurrentTestFileId1, FileOpenType.READ);
        _oFileManager.openFile(_oTestFile2.toString(), _iCurrentTestFileId2, FileOpenType.READ);
        _oFileManager.openFile(_oTestFile3.toString(), _iCurrentTestFileId3, FileOpenType.READ);

        // Execute: Get modification time for each file
        Value oResult1 = FModTime.execute(_iCurrentTestFileId1);
        Value oResult2 = FModTime.execute(_iCurrentTestFileId2);
        Value oResult3 = FModTime.execute(_iCurrentTestFileId3);

        // Verify: Each result is valid formatted date and time
        assertTrue(oResult1.toString().matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
        assertTrue(oResult2.toString().matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
        assertTrue(oResult3.toString().matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    /**
     * Test: FModTime returns consistent results across multiple calls.
     * <p>
     * Given: An open file with a known file ID
     * When: FModTime.execute() is called multiple times
     * Then: All calls return identical results
     * <p>
     * Purpose: Verifies deterministic behavior and no side effects
     */
    @Test
    public void testFModTime_WithMultipleCalls_ReturnsConsistentResults() throws Exception {
        // Setup: Open a file
        _oFileManager.openFile(_oTestFile1.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FModTime multiple times
        Value oResult1 = FModTime.execute(_iCurrentTestFileId1);
        Value oResult2 = FModTime.execute(_iCurrentTestFileId1);
        Value oResult3 = FModTime.execute(_iCurrentTestFileId1);

        // Verify: All results are identical
        assertEquals(oResult1.toString(), oResult2.toString());
        assertEquals(oResult2.toString(), oResult3.toString());
    }

    /**
     * Test: FModTime does not affect FileManager state.
     * <p>
     * Given: An open file
     * When: FModTime.execute() is called
     * Then: File remains open in FileManager, no side effects
     * <p>
     * Purpose: Verifies that retrieving modification time doesn't interfere with FileManager
     */
    @Test
    public void testFModTime_DoesNotAffectFileManagerState() throws Exception {
        // Setup: Open a file
        _oFileManager.openFile(_oTestFile1.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FModTime
        FModTime.execute(_iCurrentTestFileId1);

        // Verify: File is still open in FileManager
        Value oIsOpenResult = FIsOpen.execute(_iCurrentTestFileId1);
        assertEquals(true, Boolean.parseBoolean(oIsOpenResult.toString()));
    }

    /**
     * Test: FModTime returns valid date and time format.
     * <p>
     * Given: An open file
     * When: FModTime.execute() is called
     * Then: Returns date and time in format "yyyy-MM-dd HH:mm:ss"
     * <p>
     * Purpose: Verifies correct date/time formatting
     */
    @Test
    public void testFModTime_ReturnsCorrectDateTimeFormat() throws Exception {
        // Setup: Open a file
        _oFileManager.openFile(_oTestFile1.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Get modification time
        String strDateTime = FModTime.execute(_iCurrentTestFileId1).toString();

        // Verify: Format is correct and parseable
        SimpleDateFormat oFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date oDate = oFormat.parse(strDateTime);
        assertNotNull(oDate);
    }

    /**
     * Test: FModTime returns date and time after file modification.
     * <p>
     * Given: A file with known modification time
     * When: FModTime.execute() is called
     * Then: Returned date/time is reasonable (not too old, not in future)
     * <p>
     * Purpose: Verifies retrieved modification time is accurate
     */
    @Test
    public void testFModTime_ReturnsRecentModificationTime() throws Exception {
        // Setup: Create and open file
        long lCurrentTimeMillis = java.lang.System.currentTimeMillis();
        _oFileManager.openFile(_oTestFile1.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Get modification time
        String strDateTime = FModTime.execute(_iCurrentTestFileId1).toString();
        SimpleDateFormat oFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date oReturnedDate = oFormat.parse(strDateTime);

        // Verify: Returned date is reasonable (within last 10 seconds)
        long lTimeDifferenceMillis = lCurrentTimeMillis - oReturnedDate.getTime();
        assertTrue(lTimeDifferenceMillis >= 0, "Modification time should not be in the future");
        assertTrue(lTimeDifferenceMillis < 10000, "Modification time should be recent (within 10 seconds)");
    }

    // =========================================================================
    // NEGATIVE TEST CASES - File IDs that don't exist or have been closed
    // =========================================================================

    /**
     * Test: FModTime with unknown file ID.
     * <p>
     * Given: A file ID that was never opened
     * When: FModTime.execute() is called with that file ID
     * Then: Throws FileNotFoundException
     * <p>
     * Purpose: Verifies error handling for unknown file IDs
     */
    @Test
    public void testFModTime_WithUnknownFileId_ThrowsFileNotFoundException() {
        // Execute and Verify: FModTime throws FileNotFoundException
        assertThrows(FileNotFoundException.class, () -> {
            FModTime.execute(FILE_ID_UNKNOWN);
        });
    }

    /**
     * Test: FModTime with closed file.
     * <p>
     * Given: A file that was opened but subsequently closed
     * When: FModTime.execute() is called with that file ID
     * Then: Throws FileNotFoundException
     * <p>
     * Purpose: Verifies error handling for closed files
     */
    @Test
    public void testFModTime_WithClosedFile_ThrowsFileNotFoundException() throws Exception {
        // Setup: Open a file then close it
        _oFileManager.openFile(_oTestFile1.toString(), _iCurrentTestFileId1, FileOpenType.READ);
        _oFileManager.closeFile(_iCurrentTestFileId1, false);

        // Execute and Verify: FModTime throws FileNotFoundException
        assertThrows(FileNotFoundException.class, () -> {
            FModTime.execute(_iCurrentTestFileId1);
        });
    }

    /**
     * Test: FModTime with negative file ID.
     * <p>
     * Given: A negative integer as file ID
     * When: FModTime.execute() is called
     * Then: Throws FileNotFoundException
     * <p>
     * Purpose: Verifies error handling for invalid file IDs
     */
    @Test
    public void testFModTime_WithNegativeFileId_ThrowsFileNotFoundException() {
        // Execute and Verify: FModTime throws FileNotFoundException
        assertThrows(FileNotFoundException.class, () -> {
            FModTime.execute(-1);
        });
    }

    /**
     * Test: FModTime with zero file ID.
     * <p>
     * Given: Zero as file ID (typically invalid)
     * When: FModTime.execute() is called
     * Then: Throws FileNotFoundException
     * <p>
     * Purpose: Verifies error handling for zero file ID
     */
    @Test
    public void testFModTime_WithZeroFileId_ThrowsFileNotFoundException() {
        // Execute and Verify: FModTime throws FileNotFoundException
        assertThrows(FileNotFoundException.class, () -> {
            FModTime.execute(0);
        });
    }

    /**
     * Test: FModTime with very large file ID.
     * <p>
     * Given: Integer.MAX_VALUE as file ID
     * When: FModTime.execute() is called
     * Then: Throws FileNotFoundException
     * <p>
     * Purpose: Verifies error handling at file ID range boundaries
     */
    @Test
    public void testFModTime_WithMaxIntFileId_ThrowsFileNotFoundException() {
        // Execute and Verify: FModTime throws FileNotFoundException
        assertThrows(FileNotFoundException.class, () -> {
            FModTime.execute(Integer.MAX_VALUE);
        });
    }

    // =========================================================================
    // EDGE CASE TESTS - Boundary conditions and special scenarios
    // =========================================================================

    /**
     * Test: FModTime always returns StringValue type.
     * <p>
     * Given: An open file
     * When: FModTime.execute() is called
     * Then: Always returns a Value that is a StringValue instance
     * <p>
     * Purpose: Verifies the function returns the correct type
     */
    @Test
    public void testFModTime_AlwaysReturnsStringValueType() throws Exception {
        // Setup: Open a file
        _oFileManager.openFile(_oTestFile1.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Call FModTime
        Value oResult = FModTime.execute(_iCurrentTestFileId1);

        // Verify: Result is StringValue instance
        assertTrue(oResult instanceof StringValue);
        assertTrue(oResult instanceof Value);
    }

    /**
     * Test: FModTime throws FileNotFoundException for all invalid IDs.
     * <p>
     * Given: Multiple invalid file IDs
     * When: FModTime.execute() is called
     * Then: Throws FileNotFoundException for all invalid IDs
     * <p>
     * Purpose: Verifies consistent error handling across various invalid scenarios
     */
    @Test
    public void testFModTime_ThrowsFileNotFoundForAllInvalidIds() {
        // Execute and Verify: FModTime throws FileNotFoundException for various invalid IDs
        assertThrows(FileNotFoundException.class, () -> FModTime.execute(FILE_ID_UNKNOWN));
        assertThrows(FileNotFoundException.class, () -> FModTime.execute(-1));
        assertThrows(FileNotFoundException.class, () -> FModTime.execute(0));
        assertThrows(FileNotFoundException.class, () -> FModTime.execute(Integer.MAX_VALUE));
    }

    /**
     * Test: FModTime date string is non-empty.
     * <p>
     * Given: An open file
     * When: FModTime.execute() is called
     * Then: Returns a non-empty date and time string
     * <p>
     * Purpose: Verifies the returned string has content
     */
    @Test
    public void testFModTime_ReturnsNonEmptyString() throws Exception {
        // Setup: Open a file
        _oFileManager.openFile(_oTestFile1.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Get modification time
        String strDateTime = FModTime.execute(_iCurrentTestFileId1).toString();

        // Verify: String is not empty
        assertTrue(strDateTime.length() > 0);
        assertTrue(strDateTime.length() >= 19); // Minimum length for "yyyy-MM-dd HH:mm:ss"
    }

    /**
     * Test: FModTime exception message contains file ID information.
     * <p>
     * Given: An unknown file ID
     * When: FModTime.execute() is called
     * Then: Throws FileNotFoundException with informative message
     * <p>
     * Purpose: Verifies error messages help with debugging
     */
    @Test
    public void testFModTime_ExceptionMessageIsInformative() {
        // Execute and Verify: Exception message contains useful information
        try {
            FModTime.execute(FILE_ID_UNKNOWN);
        } catch (FileNotFoundException e) {
            assertTrue(e.getMessage().contains("999") || e.getMessage().contains("not open"));
        }
    }

    /**
     * Test: FModTime result can be used in string operations.
     * <p>
     * Given: A valid modification date and time string
     * When: String operations are performed on the result
     * Then: String operations work correctly
     * <p>
     * Purpose: Verifies the returned StringValue integrates with BASIC string functions
     */
    @Test
    public void testFModTime_ResultCanBeUsedInStringOperations() throws Exception {
        // Setup: Open a file
        _oFileManager.openFile(_oTestFile1.toString(), _iCurrentTestFileId1, FileOpenType.READ);

        // Execute: Get modification time
        String strDateTime = FModTime.execute(_iCurrentTestFileId1).toString();

        // Verify: String operations work correctly
        assertTrue(strDateTime.contains("-")); // Date separator
        assertTrue(strDateTime.contains(":")); // Time separator
        assertTrue(strDateTime.contains(" ")); // Date/time separator
        String[] arrParts = strDateTime.split(" ");
        assertEquals(2, arrParts.length); // Date and time parts
    }
}
