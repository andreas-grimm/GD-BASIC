package eu.gricom.basic.memoryManager;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.IntegerValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FileManagerReadPosTest.java
 * <p>
 * Unit tests for FileManager read position functions: putReadPos and getReadPos.
 * <p>
 * Test Categories:
 * - POSITIVE TESTS: putReadPos and getReadPos work correctly with files open for reading
 * - NEGATIVE TESTS: Both functions handle error conditions (unregistered IDs, non-reading files)
 * - EDGE CASES: Position values (0, large numbers), multiple files, overwrites
 * <p>
 * Key Behavior:
 * - putReadPos stores a read cursor position for a file open for reading
 * - getReadPos retrieves the stored read cursor position
 * - Both throw RuntimeException if file is not open for reading
 * - getReadPos returns 0 if position was never set (defensive)
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FileManagerReadPosTest {

    private static final AtomicInteger FILE_ID_COUNTER = new AtomicInteger(500);

    private int _iFileId1;
    private int _iFileId2;
    private int _iFileId3;

    private Path _oFile1;
    private Path _oFile2;
    private Path _oFile3;

    private FileManager _oFileManager;

    /**
     * Setup method: Initializes test environment before each test.
     * Creates temporary files and FileManager instance.
     */
    @BeforeEach
    public void setUp() throws Exception {
        _iFileId1 = FILE_ID_COUNTER.getAndAdd(10);
        _iFileId2 = FILE_ID_COUNTER.getAndAdd(10);
        _iFileId3 = FILE_ID_COUNTER.getAndAdd(10);

        _oFile1 = Files.createTempFile("readpos-test-1-", ".txt");
        _oFile2 = Files.createTempFile("readpos-test-2-", ".txt");
        _oFile3 = Files.createTempFile("readpos-test-3-", ".txt");

        Files.writeString(_oFile1, "line1\nline2\nline3\n");
        Files.writeString(_oFile2, "data1\ndata2\ndata3\n");
        Files.writeString(_oFile3, "content1\ncontent2\n");

        _oFileManager = new FileManager();
    }

    /**
     * Teardown method: Cleans up after each test.
     * Closes files and deletes temporary files if they still exist.
     */
    @AfterEach
    public void tearDown() throws Exception {
        _oFileManager.closeFile(_iFileId1, false);
        _oFileManager.closeFile(_iFileId2, false);
        _oFileManager.closeFile(_iFileId3, false);
        Files.deleteIfExists(_oFile1);
        Files.deleteIfExists(_oFile2);
        Files.deleteIfExists(_oFile3);
    }

    // =========================================================================
    // POSITIVE TEST CASES - putReadPos and getReadPos work correctly
    // =========================================================================

    /**
     * Test: putReadPos stores position for file open for reading.
     * <p>
     * Given: File is open for reading
     * When: putReadPos is called with a position
     * Then: Position is stored without exception
     * <p>
     * Purpose: Verifies basic putReadPos functionality
     */
    @Test
    public void testPutReadPos_WithValidFileId_StoresPosition() throws Exception {
        // Setup: Open file for reading
        _oFileManager.openFile(_oFile1.toString(), _iFileId1, FileOpenType.READ);

        // Execute: Store read position
        _oFileManager.putReadPos(_iFileId1, 10);

        // Verify: No exception thrown, operation succeeded
        assertTrue(true, "Position should be stored without exception");
    }

    /**
     * Test: getReadPos retrieves stored position.
     * <p>
     * Given: Position is stored with putReadPos
     * When: getReadPos is called
     * Then: Returns the stored position value
     * <p>
     * Purpose: Verifies basic getReadPos functionality
     */
    @Test
    public void testGetReadPos_AfterPutReadPos_ReturnsStoredPosition() throws Exception {
        // Setup: Open file and store position
        _oFileManager.openFile(_oFile1.toString(), _iFileId1, FileOpenType.READ);
        _oFileManager.putReadPos(_iFileId1, 42);

        // Execute: Retrieve position
        IntegerValue oPosition = _oFileManager.getReadPos(_iFileId1);

        // Verify: Position matches what was stored
        assertEquals(42, oPosition.toInt());
    }

    /**
     * Test: putReadPos and getReadPos with position 0.
     * <p>
     * Given: Position 0 is stored (start of file)
     * When: getReadPos is called
     * Then: Returns 0
     * <p>
     * Purpose: Verifies boundary value (position 0)
     */
    @Test
    public void testReadPos_WithPositionZero_ReturnsZero() throws Exception {
        // Setup: Open file and store position 0
        _oFileManager.openFile(_oFile1.toString(), _iFileId1, FileOpenType.READ);
        _oFileManager.putReadPos(_iFileId1, 0);

        // Execute: Retrieve position
        IntegerValue oPosition = _oFileManager.getReadPos(_iFileId1);

        // Verify: Returns 0
        assertEquals(0, oPosition.toInt());
    }

    /**
     * Test: putReadPos and getReadPos with large position value.
     * <p>
     * Given: Large position value is stored
     * When: getReadPos is called
     * Then: Returns the large position value
     * <p>
     * Purpose: Verifies boundary value (large numbers)
     */
    @Test
    public void testReadPos_WithLargePosition_ReturnsLargeValue() throws Exception {
        // Setup: Open file and store large position
        _oFileManager.openFile(_oFile1.toString(), _iFileId1, FileOpenType.READ);
        int iLargePosition = 1000000;
        _oFileManager.putReadPos(_iFileId1, iLargePosition);

        // Execute: Retrieve position
        IntegerValue oPosition = _oFileManager.getReadPos(_iFileId1);

        // Verify: Returns large position value
        assertEquals(iLargePosition, oPosition.toInt());
    }

    /**
     * Test: putReadPos overwrites previous position.
     * <p>
     * Given: Position is stored, then overwritten
     * When: getReadPos is called
     * Then: Returns the new (overwritten) position
     * <p>
     * Purpose: Verifies position can be updated
     */
    @Test
    public void testReadPos_WhenOverwritten_ReturnsNewPosition() throws Exception {
        // Setup: Open file and store initial position
        _oFileManager.openFile(_oFile1.toString(), _iFileId1, FileOpenType.READ);
        _oFileManager.putReadPos(_iFileId1, 10);

        // Execute: Overwrite with new position
        _oFileManager.putReadPos(_iFileId1, 25);
        IntegerValue oPosition = _oFileManager.getReadPos(_iFileId1);

        // Verify: Returns new position value
        assertEquals(25, oPosition.toInt());
    }

    /**
     * Test: putReadPos and getReadPos for multiple files independently.
     * <p>
     * Given: Multiple files are open for reading with different positions
     * When: getReadPos is called for each file
     * Then: Returns correct position for each file
     * <p>
     * Purpose: Verifies isolation between file IDs
     */
    @Test
    public void testReadPos_WithMultipleFiles_MaintainsIndependentPositions() throws Exception {
        // Setup: Open multiple files with different positions
        _oFileManager.openFile(_oFile1.toString(), _iFileId1, FileOpenType.READ);
        _oFileManager.openFile(_oFile2.toString(), _iFileId2, FileOpenType.READ);
        _oFileManager.openFile(_oFile3.toString(), _iFileId3, FileOpenType.READ);

        _oFileManager.putReadPos(_iFileId1, 10);
        _oFileManager.putReadPos(_iFileId2, 20);
        _oFileManager.putReadPos(_iFileId3, 30);

        // Execute: Retrieve positions for each file
        IntegerValue oPos1 = _oFileManager.getReadPos(_iFileId1);
        IntegerValue oPos2 = _oFileManager.getReadPos(_iFileId2);
        IntegerValue oPos3 = _oFileManager.getReadPos(_iFileId3);

        // Verify: Each file maintains its own position
        assertEquals(10, oPos1.toInt());
        assertEquals(20, oPos2.toInt());
        assertEquals(30, oPos3.toInt());
    }

    /**
     * Test: getReadPos returns IntegerValue instance.
     * <p>
     * Given: Position is stored for a file
     * When: getReadPos is called
     * Then: Returns IntegerValue object (not null)
     * <p>
     * Purpose: Verifies return type is IntegerValue
     */
    @Test
    public void testGetReadPos_ReturnsIntegerValueInstance() throws Exception {
        // Setup: Open file and store position
        _oFileManager.openFile(_oFile1.toString(), _iFileId1, FileOpenType.READ);
        _oFileManager.putReadPos(_iFileId1, 15);

        // Execute: Retrieve position
        IntegerValue oPosition = _oFileManager.getReadPos(_iFileId1);

        // Verify: Returns IntegerValue instance
        assertNotNull(oPosition);
        assertTrue(oPosition instanceof IntegerValue);
    }

    /**
     * Test: getReadPos returns 0 when position was initialized at file open.
     * <p>
     * Given: File is opened (position initialized to 0 in openFile)
     * When: getReadPos is called without putReadPos
     * Then: Returns 0
     * <p>
     * Purpose: Verifies default initialization behavior
     */
    @Test
    public void testGetReadPos_WithoutPutReadPos_ReturnsInitializedZero() throws Exception {
        // Setup: Open file without calling putReadPos
        _oFileManager.openFile(_oFile1.toString(), _iFileId1, FileOpenType.READ);

        // Execute: Retrieve position directly
        IntegerValue oPosition = _oFileManager.getReadPos(_iFileId1);

        // Verify: Returns 0 (initialized value from openFile)
        assertEquals(0, oPosition.toInt());
    }

    // =========================================================================
    // NEGATIVE TEST CASES - Error conditions are handled correctly
    // =========================================================================

    /**
     * Test: putReadPos throws exception for unregistered file ID.
     * <p>
     * Given: File ID is not registered in FileManager
     * When: putReadPos is called
     * Then: Throws RuntimeException
     * <p>
     * Purpose: Verifies error handling for invalid file IDs
     */
    @Test
    public void testPutReadPos_WithUnregisteredFileId_ThrowsException() {
        // Execute & Verify: Should throw exception
        RuntimeException oException = assertThrows(RuntimeException.class, () -> {
            _oFileManager.putReadPos(999, 10);
        });

        // Verify: Exception message is descriptive
        assertTrue(oException.getMessage().contains("not open for reading"));
    }

    /**
     * Test: putReadPos throws exception for file open for writing.
     * <p>
     * Given: File is open for writing (not reading)
     * When: putReadPos is called
     * Then: Throws RuntimeException
     * <p>
     * Purpose: Verifies read position only works with READ mode files
     */
    @Test
    public void testPutReadPos_WithWriteMode_ThrowsException() throws Exception {
        // Setup: Open file for writing
        _oFileManager.openFile(_oFile1.toString(), _iFileId1, FileOpenType.WRITE);

        // Execute & Verify: Should throw exception
        RuntimeException oException = assertThrows(RuntimeException.class, () -> {
            _oFileManager.putReadPos(_iFileId1, 10);
        });

        // Verify: Exception message references reading
        assertTrue(oException.getMessage().contains("not open for reading"));
    }

    /**
     * Test: getReadPos throws exception for unregistered file ID.
     * <p>
     * Given: File ID is not registered in FileManager
     * When: getReadPos is called
     * Then: Throws RuntimeException
     * <p>
     * Purpose: Verifies error handling for invalid file IDs
     */
    @Test
    public void testGetReadPos_WithUnregisteredFileId_ThrowsException() {
        // Execute & Verify: Should throw exception
        RuntimeException oException = assertThrows(RuntimeException.class, () -> {
            _oFileManager.getReadPos(999);
        });

        // Verify: Exception message is descriptive
        assertTrue(oException.getMessage().contains("not open for reading"));
    }

    /**
     * Test: getReadPos throws exception for file open for writing.
     * <p>
     * Given: File is open for writing (not reading)
     * When: getReadPos is called
     * Then: Throws RuntimeException
     * <p>
     * Purpose: Verifies read position only works with READ mode files
     */
    @Test
    public void testGetReadPos_WithWriteMode_ThrowsException() throws Exception {
        // Setup: Open file for writing
        _oFileManager.openFile(_oFile1.toString(), _iFileId1, FileOpenType.WRITE);

        // Execute & Verify: Should throw exception
        RuntimeException oException = assertThrows(RuntimeException.class, () -> {
            _oFileManager.getReadPos(_iFileId1);
        });

        // Verify: Exception message references reading
        assertTrue(oException.getMessage().contains("not open for reading"));
    }

    /**
     * Test: getReadPos throws exception for closed file.
     * <p>
     * Given: File is opened then closed
     * When: getReadPos is called
     * Then: Throws RuntimeException
     * <p>
     * Purpose: Verifies read position is only valid for open files
     */
    @Test
    public void testGetReadPos_WithClosedFile_ThrowsException() throws Exception {
        // Setup: Open and close file
        _oFileManager.openFile(_oFile1.toString(), _iFileId1, FileOpenType.READ);
        _oFileManager.closeFile(_iFileId1, false);

        // Execute & Verify: Should throw exception
        RuntimeException oException = assertThrows(RuntimeException.class, () -> {
            _oFileManager.getReadPos(_iFileId1);
        });

        // Verify: Exception message is appropriate
        assertTrue(oException.getMessage().contains("not open for reading"));
    }

    /**
     * Test: putReadPos throws exception for closed file.
     * <p>
     * Given: File is opened then closed
     * When: putReadPos is called
     * Then: Throws RuntimeException
     * <p>
     * Purpose: Verifies read position is only valid for open files
     */
    @Test
    public void testPutReadPos_WithClosedFile_ThrowsException() throws Exception {
        // Setup: Open and close file
        _oFileManager.openFile(_oFile1.toString(), _iFileId1, FileOpenType.READ);
        _oFileManager.closeFile(_iFileId1, false);

        // Execute & Verify: Should throw exception
        RuntimeException oException = assertThrows(RuntimeException.class, () -> {
            _oFileManager.putReadPos(_iFileId1, 10);
        });

        // Verify: Exception message is appropriate
        assertTrue(oException.getMessage().contains("not open for reading"));
    }

    // =========================================================================
    // EDGE CASE TESTS - Boundary conditions and special values
    // =========================================================================

    /**
     * Test: putReadPos with negative position value.
     * <p>
     * Given: Negative position value is provided
     * When: putReadPos is called
     * Then: Stores the negative value (no validation)
     * <p>
     * Purpose: Verifies negative positions are stored (application responsibility to validate)
     */
    @Test
    public void testReadPos_WithNegativeValue_StoresAndRetrievesNegative() throws Exception {
        // Setup: Open file and store negative position
        _oFileManager.openFile(_oFile1.toString(), _iFileId1, FileOpenType.READ);
        _oFileManager.putReadPos(_iFileId1, -1);

        // Execute: Retrieve position
        IntegerValue oPosition = _oFileManager.getReadPos(_iFileId1);

        // Verify: Returns negative value as stored
        assertEquals(-1, oPosition.toInt());
    }

    /**
     * Test: Sequential position updates for same file.
     * <p>
     * Given: Position is updated multiple times for the same file
     * When: getReadPos is called after each update
     * Then: Returns the most recent position
     * <p>
     * Purpose: Verifies correct behavior with rapid updates
     */
    @Test
    public void testReadPos_WithSequentialUpdates_ReturnsMostRecent() throws Exception {
        // Setup: Open file
        _oFileManager.openFile(_oFile1.toString(), _iFileId1, FileOpenType.READ);

        // Execute: Sequential updates
        int[] iPositions = {5, 15, 25, 35, 45};
        for (int iPos : iPositions) {
            _oFileManager.putReadPos(_iFileId1, iPos);
        }

        // Verify: Most recent position is returned
        IntegerValue oPosition = _oFileManager.getReadPos(_iFileId1);
        assertEquals(45, oPosition.toInt());
    }

    /**
     * Test: putReadPos and getReadPos with file ID value of 0.
     * <p>
     * Given: File ID is 0 (unusual but technically valid)
     * When: putReadPos and getReadPos are called
     * Then: Operations work correctly
     * <p>
     * Purpose: Verifies boundary value for file IDs
     */
    @Test
    public void testReadPos_WithFileIdZero_WorksCorrectly() throws Exception {
        // Setup: Create temp file and open with file ID 0
        Path oTempFile = Files.createTempFile("readpos-id0-", ".txt");
        Files.writeString(oTempFile, "test content\n");

        try {
            FileManager oManager = new FileManager();
            oManager.openFile(oTempFile.toString(), 0, FileOpenType.READ);

            // Execute: Store and retrieve position
            oManager.putReadPos(0, 5);
            IntegerValue oPosition = oManager.getReadPos(0);

            // Verify: Works correctly with file ID 0
            assertEquals(5, oPosition.toInt());

            // Cleanup
            oManager.closeFile(0, false);
        } finally {
            Files.deleteIfExists(oTempFile);
        }
    }

    /**
     * Test: getReadPos is idempotent.
     * <p>
     * Given: Position is stored
     * When: getReadPos is called multiple times
     * Then: Returns the same value each time
     * <p>
     * Purpose: Verifies getReadPos doesn't modify state
     */
    @Test
    public void testGetReadPos_CalledMultipleTimes_ReturnsSameValue() throws Exception {
        // Setup: Open file and store position
        _oFileManager.openFile(_oFile1.toString(), _iFileId1, FileOpenType.READ);
        _oFileManager.putReadPos(_iFileId1, 42);

        // Execute: Call getReadPos multiple times
        IntegerValue oPos1 = _oFileManager.getReadPos(_iFileId1);
        IntegerValue oPos2 = _oFileManager.getReadPos(_iFileId1);
        IntegerValue oPos3 = _oFileManager.getReadPos(_iFileId1);

        // Verify: All calls return same value
        assertEquals(42, oPos1.toInt());
        assertEquals(42, oPos2.toInt());
        assertEquals(42, oPos3.toInt());
    }
}
