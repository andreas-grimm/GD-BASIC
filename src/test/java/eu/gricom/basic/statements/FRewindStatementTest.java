package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.memoryManager.VariableManagement;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FRewindStatementTest.java
 * <p>
 * Unit tests for the FRewindStatement class.
 * <p>
 * This test class provides comprehensive coverage of the FRewindStatement, which resets the read cursor
 * position of an opened file to the beginning (position 0). This allows re-reading the file from the start
 * without closing and re-opening it.
 * <p>
 * Test Categories:
 * - POSITIVE TESTS: FRewindStatement successfully resets position to 0
 * - POSITION TESTS: Verify position is reset correctly
 * - INTEGRATION TESTS: Rewind with FInputStatement and FGetStatement
 * - EDGE CASES: Rewind when already at position 0, multiple rewinds
 * - INTERFACE TESTS: getTokenNumber, content, structure methods
 * <p>
 * Key Behavior:
 * - Sets read position to 0
 * - Works with any file ID that is registered
 * - Allows re-reading file from beginning
 * - Does not close or re-open the file
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FRewindStatementTest {

    private static final AtomicInteger FILE_ID_COUNTER = new AtomicInteger(500);

    private int _iFileId1;
    private int _iFileId2;

    private Path _oFile1;
    private Path _oFile2;

    private FileManager _oFileManager;

    /**
     * Setup method: Initializes test environment before each test.
     * Creates temporary files and FileManager instance.
     */
    @BeforeEach
    public void setUp() throws Exception {
        _iFileId1 = FILE_ID_COUNTER.getAndAdd(10);
        _iFileId2 = FILE_ID_COUNTER.getAndAdd(10);

        _oFile1 = Files.createTempFile("frewind-test-1-", ".txt");
        _oFile2 = Files.createTempFile("frewind-test-2-", ".txt");

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
        Files.deleteIfExists(_oFile1);
        Files.deleteIfExists(_oFile2);
    }

    // =========================================================================
    // POSITIVE TEST CASES - FRewindStatement successfully resets position
    // =========================================================================

    /**
     * Test: FRewindStatement resets position to 0 when at position 0.
     * <p>
     * Given: File is open with position 0
     * When: FRewindStatement.execute() is called
     * Then: Position remains 0
     * <p>
     * Purpose: Verifies rewind when already at beginning
     */
    @Test
    public void testExecute_WhenAtPositionZero_PositionRemains0() throws Exception {
        String strContent = "Hello";
        Files.writeString(_oFile1, strContent);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        FRewindStatement oFRewind = new FRewindStatement(2, _iFileId1);
        oFRewind.execute();

        int iPosition = _oFileManager.getReadPos(_iFileId1).toInt();
        assertEquals(0, iPosition, "Position should be 0");
    }

    /**
     * Test: FRewindStatement resets position from middle of file to 0.
     * <p>
     * Given: File is open with position advanced to middle
     * When: FRewindStatement.execute() is called
     * Then: Position is reset to 0
     * <p>
     * Purpose: Verifies position reset from non-zero position
     */
    @Test
    public void testExecute_FromMiddlePosition_ResetsTo0() throws Exception {
        String strContent = "Hello World";
        Files.writeString(_oFile1, strContent);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        _oFileManager.putReadPos(_iFileId1, 5);

        FRewindStatement oFRewind = new FRewindStatement(2, _iFileId1);
        oFRewind.execute();

        int iPosition = _oFileManager.getReadPos(_iFileId1).toInt();
        assertEquals(0, iPosition, "Position should be reset to 0");
    }

    /**
     * Test: FRewindStatement resets position from EOF to 0.
     * <p>
     * Given: File is open with position at end of file
     * When: FRewindStatement.execute() is called
     * Then: Position is reset to 0
     * <p>
     * Purpose: Verifies rewind from EOF position
     */
    @Test
    public void testExecute_FromEOF_ResetsTo0() throws Exception {
        String strContent = "Hello";
        Files.writeString(_oFile1, strContent);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        FInputStatement oFInput = new FInputStatement(2, _iFileId1, "Line$");
        oFInput.execute();

        int iPositionBefore = _oFileManager.getReadPos(_iFileId1).toInt();
        assertEquals(5, iPositionBefore, "Position should be 5 after read");

        FRewindStatement oFRewind = new FRewindStatement(3, _iFileId1);
        oFRewind.execute();

        int iPosition = _oFileManager.getReadPos(_iFileId1).toInt();
        assertEquals(0, iPosition, "Position should be reset to 0");
    }

    /**
     * Test: FRewindStatement allows re-reading file from beginning.
     * <p>
     * Given: File is read, then rewound
     * When: File is read again
     * Then: Gets the same first line again
     * <p>
     * Purpose: Verifies rewind enables re-reading
     */
    @Test
    public void testExecute_RewindEnablesRereadingFromBeginning() throws Exception {
        String strLine1 = "First";
        String strLine2 = "Second";
        Files.writeString(_oFile1, strLine1 + "\n" + strLine2);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        FInputStatement oFInput1 = new FInputStatement(2, _iFileId1, "Line1$");
        oFInput1.execute();

        FRewindStatement oFRewind = new FRewindStatement(3, _iFileId1);
        oFRewind.execute();

        FInputStatement oFInput2 = new FInputStatement(4, _iFileId1, "Line1Again$");
        oFInput2.execute();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oLine1 = (StringValue) oVarMgmt.getMap("Line1$");
        StringValue oLine1Again = (StringValue) oVarMgmt.getMap("Line1Again$");

        assertEquals(strLine1, oLine1.toString(), "First read should be 'First'");
        assertEquals(strLine1, oLine1Again.toString(), "After rewind, should read 'First' again");
    }

    /**
     * Test: FRewindStatement allows re-reading character from beginning.
     * <p>
     * Given: File position is advanced and rewound
     * When: FGetStatement reads character
     * Then: Reads first character
     * <p>
     * Purpose: Verifies rewind works with FGetStatement
     */
    @Test
    public void testExecute_RewindEnablesRereadingFirstCharacter() throws Exception {
        String strContent = "ABC";
        Files.writeString(_oFile1, strContent);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        FGetStatement oFGet1 = new FGetStatement(2, _iFileId1, "C1$");
        oFGet1.execute();

        int iPositionAfterGet = _oFileManager.getReadPos(_iFileId1).toInt();
        assertEquals(1, iPositionAfterGet, "Position should be 1 after get");

        FRewindStatement oFRewind = new FRewindStatement(3, _iFileId1);
        oFRewind.execute();

        FGetStatement oFGet2 = new FGetStatement(4, _iFileId1, "C1Again$");
        oFGet2.execute();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oChar1 = (StringValue) oVarMgmt.getMap("C1$");
        StringValue oChar1Again = (StringValue) oVarMgmt.getMap("C1Again$");

        assertEquals("A", oChar1.toString(), "First get should be 'A'");
        assertEquals("A", oChar1Again.toString(), "After rewind, should get 'A' again");
    }

    /**
     * Test: Multiple sequential rewind statements.
     * <p>
     * Given: File is read multiple times with rewinds between each
     * When: Each read starts from position 0
     * Then: All reads return same first line
     * <p>
     * Purpose: Verifies multiple rewinds work correctly
     */
    @Test
    public void testExecute_MultipleRewinds_AllowMultipleReads() throws Exception {
        String strLine1 = "First";
        String strLine2 = "Second";
        Files.writeString(_oFile1, strLine1 + "\n" + strLine2);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        FInputStatement oFInput1 = new FInputStatement(2, _iFileId1, "Read1$");
        oFInput1.execute();

        FRewindStatement oFRewind1 = new FRewindStatement(3, _iFileId1);
        oFRewind1.execute();

        FInputStatement oFInput2 = new FInputStatement(4, _iFileId1, "Read2$");
        oFInput2.execute();

        FRewindStatement oFRewind2 = new FRewindStatement(5, _iFileId1);
        oFRewind2.execute();

        FInputStatement oFInput3 = new FInputStatement(6, _iFileId1, "Read3$");
        oFInput3.execute();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oRead1 = (StringValue) oVarMgmt.getMap("Read1$");
        StringValue oRead2 = (StringValue) oVarMgmt.getMap("Read2$");
        StringValue oRead3 = (StringValue) oVarMgmt.getMap("Read3$");

        assertEquals(strLine1, oRead1.toString(), "First read");
        assertEquals(strLine1, oRead2.toString(), "Second read after rewind");
        assertEquals(strLine1, oRead3.toString(), "Third read after rewind");
    }

    /**
     * Test: Rewind with multiple files independently.
     * <p>
     * Given: Two files open with different positions
     * When: Rewind is called on each file
     * Then: Each file's position is reset to 0 independently
     * <p>
     * Purpose: Verifies file independence
     */
    @Test
    public void testExecute_WithMultipleFiles_RewindsIndependently() throws Exception {
        Files.writeString(_oFile1, "File1Line1\nFile1Line2");
        Files.writeString(_oFile2, "File2Line1\nFile2Line2");

        FOpenStatement oFOpen1 = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        FOpenStatement oFOpen2 = new FOpenStatement(2, _iFileId2, _oFile2.toString(), "read");
        oFOpen1.execute();
        oFOpen2.execute();

        FInputStatement oFInput1 = new FInputStatement(3, _iFileId1, "Line1$");
        FInputStatement oFInput2 = new FInputStatement(4, _iFileId2, "Line1$");
        oFInput1.execute();
        oFInput2.execute();

        FRewindStatement oFRewind1 = new FRewindStatement(5, _iFileId1);
        FRewindStatement oFRewind2 = new FRewindStatement(6, _iFileId2);
        oFRewind1.execute();
        oFRewind2.execute();

        int iPos1 = _oFileManager.getReadPos(_iFileId1).toInt();
        int iPos2 = _oFileManager.getReadPos(_iFileId2).toInt();

        assertEquals(0, iPos1, "File 1 position should be 0");
        assertEquals(0, iPos2, "File 2 position should be 0");
    }

    /**
     * Test: Rewind after peek operation.
     * <p>
     * Given: File position is at 5 after peek
     * When: FRewindStatement is called
     * Then: Position is reset to 0
     * <p>
     * Purpose: Verifies rewind works after peek
     */
    @Test
    public void testExecute_AfterPeekOperation_ResetsPosition() throws Exception {
        String strContent = "Hello World";
        Files.writeString(_oFile1, strContent);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        FInputStatement oFInput = new FInputStatement(2, _iFileId1, "Line$");
        oFInput.execute();

        _oFileManager.putReadPos(_iFileId1, 5);

        FPeekStatement oFPeek = new FPeekStatement(3, _iFileId1, "Char$");
        oFPeek.execute();

        int iPositionAfterPeek = _oFileManager.getReadPos(_iFileId1).toInt();
        assertEquals(5, iPositionAfterPeek, "Position should remain 5 after peek");

        FRewindStatement oFRewind = new FRewindStatement(4, _iFileId1);
        oFRewind.execute();

        int iPositionAfterRewind = _oFileManager.getReadPos(_iFileId1).toInt();
        assertEquals(0, iPositionAfterRewind, "Position should be 0 after rewind");
    }

    /**
     * Test: Rewind with large file position.
     * <p>
     * Given: File position is at large value
     * When: FRewindStatement is called
     * Then: Position is reset to 0
     * <p>
     * Purpose: Verifies rewind works with large positions
     */
    @Test
    public void testExecute_WithLargePosition_ResetsTo0() throws Exception {
        Files.writeString(_oFile1, "A");

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        _oFileManager.putReadPos(_iFileId1, 1000000);

        FRewindStatement oFRewind = new FRewindStatement(2, _iFileId1);
        oFRewind.execute();

        int iPosition = _oFileManager.getReadPos(_iFileId1).toInt();
        assertEquals(0, iPosition, "Position should be 0 after rewind");
    }

    // =========================================================================
    // INTERFACE TEST CASES - Test public interface methods
    // =========================================================================

    /**
     * Test: getTokenNumber returns constructor value.
     * <p>
     * Given: FRewindStatement with specific token number
     * When: getTokenNumber() is called
     * Then: Returns the token number from constructor
     * <p>
     * Purpose: Verifies token number storage
     */
    @Test
    public void testGetTokenNumber_ReturnsConstructorValue() {
        FRewindStatement oStatement = new FRewindStatement(42, _iFileId1);

        assertEquals(42, oStatement.getTokenNumber());
    }

    /**
     * Test: content returns FREWIND.
     * <p>
     * Given: FRewindStatement instance
     * When: content() is called
     * Then: Returns "FREWIND"
     * <p>
     * Purpose: Verifies content method
     */
    @Test
    public void testContent_ReturnsFREWIND() throws Exception {
        FRewindStatement oStatement = new FRewindStatement(1, _iFileId1);

        assertEquals("FREWIND", oStatement.content());
    }

    /**
     * Test: structure contains FREWIND key.
     * <p>
     * Given: FRewindStatement instance
     * When: structure() is called
     * Then: JSON contains FREWIND key
     * <p>
     * Purpose: Verifies structure method
     */
    @Test
    public void testStructure_ContainsFREWINDKey() throws Exception {
        FRewindStatement oStatement = new FRewindStatement(1, _iFileId1);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FREWIND\""));
    }

    /**
     * Test: structure contains token number.
     * <p>
     * Given: FRewindStatement with specific token number
     * When: structure() is called
     * Then: JSON contains token number
     * <p>
     * Purpose: Verifies token number in structure
     */
    @Test
    public void testStructure_ContainsTokenNumber() throws Exception {
        FRewindStatement oStatement = new FRewindStatement(100, _iFileId1);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"TOKEN_NR\": \"100\""));
    }

    /**
     * Test: structure contains file ID.
     * <p>
     * Given: FRewindStatement with specific file ID
     * When: structure() is called
     * Then: JSON contains file ID
     * <p>
     * Purpose: Verifies file ID in structure
     */
    @Test
    public void testStructure_ContainsFileId() throws Exception {
        FRewindStatement oStatement = new FRewindStatement(1, 500);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FILE_ID\": \"500\""));
    }

    /**
     * Test: structure returns valid JSON.
     * <p>
     * Given: FRewindStatement instance
     * When: structure() is called
     * Then: Returns properly formatted JSON
     * <p>
     * Purpose: Verifies JSON format
     */
    @Test
    public void testStructure_ReturnsValidJsonFormat() throws Exception {
        FRewindStatement oStatement = new FRewindStatement(1, _iFileId1);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.startsWith("{"));
        assertTrue(strStructure.endsWith("}"));
        assertTrue(strStructure.contains("\"FREWIND\": {"));
    }

    /**
     * Test: structure contains all parameters.
     * <p>
     * Given: FRewindStatement with various parameters
     * When: structure() is called
     * Then: JSON contains all parameters
     * <p>
     * Purpose: Verifies complete structure information
     */
    @Test
    public void testStructure_ContainsAllParameters() throws Exception {
        FRewindStatement oStatement = new FRewindStatement(123, 456);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"TOKEN_NR\": \"123\""));
        assertTrue(strStructure.contains("\"FILE_ID\": \"456\""));
    }
}
