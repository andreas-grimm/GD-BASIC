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
 * FPeekStatementTest.java
 * <p>
 * Unit tests for the FPeekStatement class.
 * <p>
 * This test class provides comprehensive coverage of the FPeekStatement, which reads a single character
 * from an opened file at the current read cursor position WITHOUT advancing the position (unlike FGetStatement).
 * The character is extracted by closing and re-opening the file, then skipping to the stored position,
 * reading one character, but NOT updating the cursor.
 * <p>
 * Test Categories:
 * - POSITIVE TESTS: FPeekStatement successfully reads characters from files
 * - NEGATIVE TESTS: FPeekStatement handles error conditions gracefully
 * - EDGE CASES: First char, middle positions, EOF, special characters
 * - POSITION TESTS: Verify that position is NOT advanced after peek
 * - INTEGRATION TESTS: FPeekStatement vs FGetStatement comparison
 * <p>
 * Key Behavior:
 * - Reads character at current file position
 * - Closes and re-opens file internally
 * - Does NOT update position to position + 1 (key difference from FGetStatement)
 * - Returns "EOF" when end of file is reached
 * - Works with multi-line files
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FPeekStatementTest {

    private static final AtomicInteger FILE_ID_COUNTER = new AtomicInteger(450);

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

        _oFile1 = Files.createTempFile("fpeek-test-1-", ".txt");
        _oFile2 = Files.createTempFile("fpeek-test-2-", ".txt");

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
    // POSITIVE TEST CASES - FPeekStatement successfully reads characters
    // =========================================================================

    /**
     * Test: FPeekStatement reads first character from start of file.
     * <p>
     * Given: File is open with position 0
     * When: FPeekStatement.execute() is called
     * Then: First character of the file is read and assigned to variable
     * <p>
     * Purpose: Verifies basic character reading at position 0
     */
    @Test
    public void testExecute_WithPositionZero_ReadsFirstCharacter() throws Exception {
        String strContent = "Hello";
        Files.writeString(_oFile1, strContent);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        FPeekStatement oFPeek = new FPeekStatement(2, _iFileId1, "C$");
        oFPeek.execute();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oChar = (StringValue) oVarMgmt.getMap("C$");
        assertEquals("H", oChar.toString(), "First character should be 'H'");
    }

    /**
     * Test: FPeekStatement does NOT advance position after read.
     * <p>
     * Given: File is open and position is 0
     * When: FPeekStatement.execute() is called
     * Then: Character is read but position remains 0
     * <p>
     * Purpose: Verifies that position is NOT updated (key difference from FGetStatement)
     */
    @Test
    public void testExecute_DoesNotAdvancePosition() throws Exception {
        String strContent = "ABC";
        Files.writeString(_oFile1, strContent);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        FPeekStatement oFPeek = new FPeekStatement(2, _iFileId1, "C$");
        oFPeek.execute();

        int iPositionAfterPeek = _oFileManager.getReadPos(_iFileId1).toInt();
        assertEquals(0, iPositionAfterPeek, "Position should remain 0 after peek");
    }

    /**
     * Test: Multiple FPeekStatement calls return same character (position unchanged).
     * <p>
     * Given: File is open with position 0
     * When: FPeekStatement is called multiple times
     * Then: Each call returns the same character
     * <p>
     * Purpose: Verifies that peek doesn't advance position, so multiple peeks return same char
     */
    @Test
    public void testExecute_MultiplePeeks_ReturnSameCharacter() throws Exception {
        String strContent = "ABC";
        Files.writeString(_oFile1, strContent);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        FPeekStatement oFPeek1 = new FPeekStatement(2, _iFileId1, "C1$");
        FPeekStatement oFPeek2 = new FPeekStatement(3, _iFileId1, "C2$");
        FPeekStatement oFPeek3 = new FPeekStatement(4, _iFileId1, "C3$");

        oFPeek1.execute();
        oFPeek2.execute();
        oFPeek3.execute();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oChar1 = (StringValue) oVarMgmt.getMap("C1$");
        StringValue oChar2 = (StringValue) oVarMgmt.getMap("C2$");
        StringValue oChar3 = (StringValue) oVarMgmt.getMap("C3$");

        assertEquals("A", oChar1.toString(), "First peek should be 'A'");
        assertEquals("A", oChar2.toString(), "Second peek should also be 'A'");
        assertEquals("A", oChar3.toString(), "Third peek should also be 'A'");
    }

    /**
     * Test: FPeekStatement with FGetStatement comparison (position behavior).
     * <p>
     * Given: File with multiple characters
     * When: FPeekStatement reads and then FGetStatement reads
     * Then: FPeekStatement doesn't advance, FGetStatement does
     * <p>
     * Purpose: Verifies difference between peek and get operations
     */
    @Test
    public void testExecute_PeekVsGet_PositionAdvancementDifference() throws Exception {
        String strContent = "ABC";
        Files.writeString(_oFile1, strContent);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        FPeekStatement oFPeek = new FPeekStatement(2, _iFileId1, "Peek$");
        oFPeek.execute();

        int iPositionAfterPeek = _oFileManager.getReadPos(_iFileId1).toInt();

        FGetStatement oFGet = new FGetStatement(3, _iFileId1, "Get$");
        oFGet.execute();

        int iPositionAfterGet = _oFileManager.getReadPos(_iFileId1).toInt();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oPeeked = (StringValue) oVarMgmt.getMap("Peek$");
        StringValue oGot = (StringValue) oVarMgmt.getMap("Get$");

        assertEquals("A", oPeeked.toString(), "Peek should read 'A'");
        assertEquals("A", oGot.toString(), "Get should read 'A'");
        assertEquals(0, iPositionAfterPeek, "Position should be 0 after peek");
        assertEquals(1, iPositionAfterGet, "Position should be 1 after get");
    }

    /**
     * Test: FPeekStatement reads character at middle position.
     * <p>
     * Given: File with multiple lines, position is advanced
     * When: FPeekStatement.execute() is called
     * Then: Character at current position is read, position unchanged
     * <p>
     * Purpose: Verifies character reading at non-zero position without advancing
     */
    @Test
    public void testExecute_WithMiddlePosition_ReadsPeekCharacter() throws Exception {
        String strLine1 = "Hello";
        String strLine2 = "World";
        Files.writeString(_oFile1, strLine1 + "\n" + strLine2);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        FInputStatement oFInput = new FInputStatement(2, _iFileId1, "Line1$");
        oFInput.execute();

        FPeekStatement oFPeek = new FPeekStatement(3, _iFileId1, "C$");
        oFPeek.execute();

        int iPositionAfterPeek = _oFileManager.getReadPos(_iFileId1).toInt();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oChar = (StringValue) oVarMgmt.getMap("C$");

        assertEquals("W", oChar.toString(), "Should peek 'W' from second line");
        assertEquals(5, iPositionAfterPeek, "Position should remain 5 after peek");
    }

    /**
     * Test: FPeekStatement reads EOF when position is at end of file.
     * <p>
     * Given: File with 5 characters and position is 5 (at EOF)
     * When: FPeekStatement.execute() is called
     * Then: Returns "EOF"
     * <p>
     * Purpose: Verifies EOF handling without advancing position
     */
    @Test
    public void testExecute_WhenPositionAtEOF_ReturnsEOF() throws Exception {
        String strContent = "Hello";
        Files.writeString(_oFile1, strContent);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        FInputStatement oFInput = new FInputStatement(2, _iFileId1, "Line$");
        oFInput.execute();

        FPeekStatement oFPeek = new FPeekStatement(3, _iFileId1, "C$");
        oFPeek.execute();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oChar = (StringValue) oVarMgmt.getMap("C$");
        assertEquals("EOF", oChar.toString(), "Should return 'EOF'");
    }

    /**
     * Test: FPeekStatement with special characters.
     * <p>
     * Given: File contains special characters
     * When: FPeekStatement reads special character
     * Then: Special character is returned correctly
     * <p>
     * Purpose: Verifies special character handling
     */
    @Test
    public void testExecute_WithSpecialCharacters_ReadsSpecialChar() throws Exception {
        String strContent = "@#$%";
        Files.writeString(_oFile1, strContent);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        FPeekStatement oFPeek = new FPeekStatement(2, _iFileId1, "C$");
        oFPeek.execute();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oChar = (StringValue) oVarMgmt.getMap("C$");
        assertEquals("@", oChar.toString(), "Should peek special character '@'");
    }

    /**
     * Test: FPeekStatement reads numeric characters.
     * <p>
     * Given: File contains numeric content
     * When: FPeekStatement reads numeric character
     * Then: Numeric character is returned as string
     * <p>
     * Purpose: Verifies numeric character reading
     */
    @Test
    public void testExecute_WithNumericContent_ReadsNumericChar() throws Exception {
        String strContent = "12345";
        Files.writeString(_oFile1, strContent);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        FPeekStatement oFPeek = new FPeekStatement(2, _iFileId1, "C$");
        oFPeek.execute();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oChar = (StringValue) oVarMgmt.getMap("C$");
        assertEquals("1", oChar.toString(), "Should peek '1'");
    }

    /**
     * Test: FPeekStatement reads space character.
     * <p>
     * Given: File contains spaces
     * When: FPeekStatement reads position with space
     * Then: Space character is returned
     * <p>
     * Purpose: Verifies space character handling
     */
    @Test
    public void testExecute_WithSpaceCharacter_ReadsSpace() throws Exception {
        String strContent = "A B";
        Files.writeString(_oFile1, strContent);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        _oFileManager.putReadPos(_iFileId1, 1);

        FPeekStatement oFPeek = new FPeekStatement(2, _iFileId1, "C$");
        oFPeek.execute();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oChar = (StringValue) oVarMgmt.getMap("C$");
        assertEquals(" ", oChar.toString(), "Should peek space character");
    }

    /**
     * Test: FPeekStatement with multiple files reads independently.
     * <p>
     * Given: Two files are open with different positions
     * When: FPeekStatement is called for each file
     * Then: Each file's character is read independently without advancing
     * <p>
     * Purpose: Verifies file independence
     */
    @Test
    public void testExecute_WithMultipleFiles_ReadsIndependently() throws Exception {
        Files.writeString(_oFile1, "ABC");
        Files.writeString(_oFile2, "XYZ");

        FOpenStatement oFOpen1 = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        FOpenStatement oFOpen2 = new FOpenStatement(2, _iFileId2, _oFile2.toString(), "read");
        oFOpen1.execute();
        oFOpen2.execute();

        FPeekStatement oFPeek1 = new FPeekStatement(3, _iFileId1, "C1$");
        FPeekStatement oFPeek2 = new FPeekStatement(4, _iFileId2, "C2$");

        oFPeek1.execute();
        oFPeek2.execute();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oChar1 = (StringValue) oVarMgmt.getMap("C1$");
        StringValue oChar2 = (StringValue) oVarMgmt.getMap("C2$");

        assertEquals("A", oChar1.toString(), "File 1 first character");
        assertEquals("X", oChar2.toString(), "File 2 first character");
    }

    /**
     * Test: Sequential peek operations return same character.
     * <p>
     * Given: File with content
     * When: FPeekStatement, FPeekStatement, FGetStatement are called in sequence
     * Then: Two peeks return same char, get returns same and advances position
     * <p>
     * Purpose: Verifies peek doesn't consume position
     */
    @Test
    public void testExecute_PeekPeekGet_PositionTracking() throws Exception {
        String strContent = "ABCD";
        Files.writeString(_oFile1, strContent);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        FPeekStatement oFPeek1 = new FPeekStatement(2, _iFileId1, "Peek1$");
        oFPeek1.execute();
        int iPos1 = _oFileManager.getReadPos(_iFileId1).toInt();

        FPeekStatement oFPeek2 = new FPeekStatement(3, _iFileId1, "Peek2$");
        oFPeek2.execute();
        int iPos2 = _oFileManager.getReadPos(_iFileId1).toInt();

        FGetStatement oFGet = new FGetStatement(4, _iFileId1, "Get$");
        oFGet.execute();
        int iPos3 = _oFileManager.getReadPos(_iFileId1).toInt();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oPeek1 = (StringValue) oVarMgmt.getMap("Peek1$");
        StringValue oPeek2 = (StringValue) oVarMgmt.getMap("Peek2$");
        StringValue oGet = (StringValue) oVarMgmt.getMap("Get$");

        assertEquals("A", oPeek1.toString(), "First peek returns 'A'");
        assertEquals("A", oPeek2.toString(), "Second peek returns 'A'");
        assertEquals("A", oGet.toString(), "Get returns 'A'");
        assertEquals(0, iPos1, "Position should be 0 after first peek");
        assertEquals(0, iPos2, "Position should be 0 after second peek");
        assertEquals(1, iPos3, "Position should be 1 after get");
    }

    // =========================================================================
    // INTERFACE TEST CASES - Test public interface methods
    // =========================================================================

    /**
     * Test: getTokenNumber returns constructor value.
     * <p>
     * Given: FPeekStatement with specific token number
     * When: getTokenNumber() is called
     * Then: Returns the token number from constructor
     * <p>
     * Purpose: Verifies token number storage
     */
    @Test
    public void testGetTokenNumber_ReturnsConstructorValue() {
        FPeekStatement oStatement = new FPeekStatement(42, _iFileId1, "C$");

        assertEquals(42, oStatement.getTokenNumber());
    }

    /**
     * Test: content returns FPEEK.
     * <p>
     * Given: FPeekStatement instance
     * When: content() is called
     * Then: Returns "FPEEK"
     * <p>
     * Purpose: Verifies content method
     */
    @Test
    public void testContent_ReturnsFPEEK() throws Exception {
        FPeekStatement oStatement = new FPeekStatement(1, _iFileId1, "C$");

        assertEquals("FPEEK", oStatement.content());
    }

    /**
     * Test: structure contains FPEEK key.
     * <p>
     * Given: FPeekStatement instance
     * When: structure() is called
     * Then: JSON contains FPEEK key
     * <p>
     * Purpose: Verifies structure method
     */
    @Test
    public void testStructure_ContainsFPEEKKey() throws Exception {
        FPeekStatement oStatement = new FPeekStatement(1, _iFileId1, "C$");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FPEEK\""));
    }

    /**
     * Test: structure contains token number.
     * <p>
     * Given: FPeekStatement with specific token number
     * When: structure() is called
     * Then: JSON contains token number
     * <p>
     * Purpose: Verifies token number in structure
     */
    @Test
    public void testStructure_ContainsTokenNumber() throws Exception {
        FPeekStatement oStatement = new FPeekStatement(100, _iFileId1, "C$");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"TOKEN_NR\": \"100\""));
    }

    /**
     * Test: structure contains file ID.
     * <p>
     * Given: FPeekStatement with specific file ID
     * When: structure() is called
     * Then: JSON contains file ID
     * <p>
     * Purpose: Verifies file ID in structure
     */
    @Test
    public void testStructure_ContainsFileId() throws Exception {
        FPeekStatement oStatement = new FPeekStatement(1, 500, "C$");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FILE_ID\": \"500\""));
    }

    /**
     * Test: structure contains variable name.
     * <p>
     * Given: FPeekStatement with specific variable name
     * When: structure() is called
     * Then: JSON contains variable name
     * <p>
     * Purpose: Verifies variable name in structure
     */
    @Test
    public void testStructure_ContainsVariableName() throws Exception {
        FPeekStatement oStatement = new FPeekStatement(1, _iFileId1, "MyChar$");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"VARIABLE\": \"MyChar$\""));
    }

    /**
     * Test: structure returns valid JSON.
     * <p>
     * Given: FPeekStatement instance
     * When: structure() is called
     * Then: Returns properly formatted JSON
     * <p>
     * Purpose: Verifies JSON format
     */
    @Test
    public void testStructure_ReturnsValidJsonFormat() throws Exception {
        FPeekStatement oStatement = new FPeekStatement(1, _iFileId1, "C$");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.startsWith("{"));
        assertTrue(strStructure.endsWith("}"));
        assertTrue(strStructure.contains("\"FPEEK\": {"));
    }

    /**
     * Test: structure contains all parameters.
     * <p>
     * Given: FPeekStatement with various parameters
     * When: structure() is called
     * Then: JSON contains all parameters
     * <p>
     * Purpose: Verifies complete structure information
     */
    @Test
    public void testStructure_ContainsAllParameters() throws Exception {
        FPeekStatement oStatement = new FPeekStatement(123, 456, "TestChar$");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"TOKEN_NR\": \"123\""));
        assertTrue(strStructure.contains("\"FILE_ID\": \"456\""));
        assertTrue(strStructure.contains("\"VARIABLE\": \"TestChar$\""));
    }
}
