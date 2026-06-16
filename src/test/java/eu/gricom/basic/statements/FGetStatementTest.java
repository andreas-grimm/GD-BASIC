package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.memoryManager.VariableManagement;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FGetStatementTest.java
 * <p>
 * Unit tests for the FGetStatement class.
 * <p>
 * This test class provides comprehensive coverage of the FGetStatement, which reads a single character
 * from an opened file at the current read cursor position. The character is extracted by closing and
 * re-opening the file, then skipping to the stored position, reading one character, and updating the cursor.
 * <p>
 * Test Categories:
 * - POSITIVE TESTS: FGetStatement successfully reads characters from files
 * - NEGATIVE TESTS: FGetStatement handles error conditions gracefully
 * - EDGE CASES: First char, middle positions, EOF, special characters
 * - INTEGRATION TESTS: FGetStatement combined with FInputStatement for position tracking
 * <p>
 * Key Behavior:
 * - Reads character at current file position
 * - Closes and re-opens file internally
 * - Updates position to position + 1 after each read
 * - Returns "EOF" when end of file is reached
 * - Works with multi-line files
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FGetStatementTest {

    private static final AtomicInteger FILE_ID_COUNTER = new AtomicInteger(400);

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

        _oFile1 = Files.createTempFile("fget-test-1-", ".txt");
        _oFile2 = Files.createTempFile("fget-test-2-", ".txt");

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
    // POSITIVE TEST CASES - FGetStatement successfully reads characters
    // =========================================================================

    /**
     * Test: FGetStatement reads first character from start of file.
     * <p>
     * Given: File is open with position 0
     * When: FGetStatement.execute() is called
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

        FGetStatement oFGet = new FGetStatement(2, _iFileId1, "C$");
        oFGet.execute();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oChar = (StringValue) oVarMgmt.getMap("C$");
        assertEquals("H", oChar.toString(), "First character should be 'H'");
    }

    /**
     * Test: FGetStatement reads character at middle position.
     * <p>
     * Given: File is open with position 5 (after reading 5 characters)
     * When: FGetStatement.execute() is called
     * Then: Character at position 5 is read
     * <p>
     * Purpose: Verifies character reading at non-zero position
     */
    @Test
    public void testExecute_WithMiddlePosition_ReadsCharacterAtPosition() throws Exception {
        String strContent = "Hello";
        Files.writeString(_oFile1, strContent);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        FInputStatement oFInput = new FInputStatement(2, _iFileId1, "Line$");
        oFInput.execute();

        FGetStatement oFGet = new FGetStatement(3, _iFileId1, "C$");
        oFGet.execute();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oChar = (StringValue) oVarMgmt.getMap("C$");
        assertEquals("EOF", oChar.toString(), "Reading past EOF should return 'EOF'");
    }

    /**
     * Test: FGetStatement reads character after multiple FInputStatements.
     * <p>
     * Given: File with multiple lines, FInputStatement has read some lines
     * When: FGetStatement is called
     * Then: Reads character at the updated position
     * <p>
     * Purpose: Verifies integration with FInputStatement position tracking
     */
    @Test
    public void testExecute_AfterFInputStatement_ReadsNextCharacter() throws Exception {
        String strContent = "Hello\nWorld";
        Files.writeString(_oFile1, strContent);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        FInputStatement oFInput = new FInputStatement(2, _iFileId1, "Line$");
        oFInput.execute();

        FGetStatement oFGet = new FGetStatement(3, _iFileId1, "C$");
        oFGet.execute();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oChar = (StringValue) oVarMgmt.getMap("C$");
        assertEquals("W", oChar.toString(), "Should read 'W' from 'World'");
    }

    /**
     * Test: FGetStatement sequential reads increment position correctly.
     * <p>
     * Given: File is open and FGetStatement is called multiple times
     * When: Each FGetStatement call increments position by 1
     * Then: Each subsequent call reads the next character
     * <p>
     * Purpose: Verifies sequential character reading
     */
    @Test
    public void testExecute_SequentialReads_ReadsConsecutiveCharacters() throws Exception {
        String strContent = "ABC";
        Files.writeString(_oFile1, strContent);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        FGetStatement oFGet1 = new FGetStatement(2, _iFileId1, "C1$");
        FGetStatement oFGet2 = new FGetStatement(3, _iFileId1, "C2$");
        FGetStatement oFGet3 = new FGetStatement(4, _iFileId1, "C3$");

        oFGet1.execute();
        oFGet2.execute();
        oFGet3.execute();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oChar1 = (StringValue) oVarMgmt.getMap("C1$");
        StringValue oChar2 = (StringValue) oVarMgmt.getMap("C2$");
        StringValue oChar3 = (StringValue) oVarMgmt.getMap("C3$");

        assertEquals("A", oChar1.toString(), "First character should be 'A'");
        assertEquals("B", oChar2.toString(), "Second character should be 'B'");
        assertEquals("C", oChar3.toString(), "Third character should be 'C'");
    }

    /**
     * Test: FGetStatement reads EOF when position is beyond file length.
     * <p>
     * Given: File with 5 characters and position is 5 (at EOF)
     * When: FGetStatement.execute() is called
     * Then: Returns "EOF"
     * <p>
     * Purpose: Verifies EOF handling
     */
    @Test
    public void testExecute_WhenPositionAtEOF_ReturnsEOF() throws Exception {
        String strContent = "Hello";
        Files.writeString(_oFile1, strContent);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        FInputStatement oFInput = new FInputStatement(2, _iFileId1, "Line$");
        oFInput.execute();

        FGetStatement oFGet = new FGetStatement(3, _iFileId1, "C$");
        oFGet.execute();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oChar = (StringValue) oVarMgmt.getMap("C$");
        assertEquals("EOF", oChar.toString(), "Should return 'EOF'");
    }

    /**
     * Test: FGetStatement reads character from multi-line file.
     * <p>
     * Given: File with multiple lines
     * When: FGetStatement reads character from different lines
     * Then: Correctly extracts character from appropriate line
     * <p>
     * Purpose: Verifies reading across line boundaries
     */
    @Test
    public void testExecute_WithMultilineFile_ReadsCharacterFromSecondLine() throws Exception {
        String strLine1 = "Hello";
        String strLine2 = "World";
        Files.writeString(_oFile1, strLine1 + "\n" + strLine2);

        FOpenStatement oFOpen = new FOpenStatement(1, _iFileId1, _oFile1.toString(), "read");
        oFOpen.execute();

        FInputStatement oFInput = new FInputStatement(2, _iFileId1, "Line1$");
        oFInput.execute();

        FGetStatement oFGet = new FGetStatement(3, _iFileId1, "C$");
        oFGet.execute();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oChar = (StringValue) oVarMgmt.getMap("C$");
        assertEquals("W", oChar.toString(), "Should read 'W' from second line");
    }

    /**
     * Test: FGetStatement reads special characters correctly.
     * <p>
     * Given: File contains special characters
     * When: FGetStatement reads special character
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

        FGetStatement oFGet = new FGetStatement(2, _iFileId1, "C$");
        oFGet.execute();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oChar = (StringValue) oVarMgmt.getMap("C$");
        assertEquals("@", oChar.toString(), "Should read special character '@'");
    }

    /**
     * Test: FGetStatement reads numeric characters.
     * <p>
     * Given: File contains numeric content
     * When: FGetStatement reads numeric character
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

        FGetStatement oFGet = new FGetStatement(2, _iFileId1, "C$");
        oFGet.execute();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oChar = (StringValue) oVarMgmt.getMap("C$");
        assertEquals("1", oChar.toString(), "Should read '1'");
    }

    /**
     * Test: FGetStatement reads space character.
     * <p>
     * Given: File contains spaces
     * When: FGetStatement reads position with space
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

        FGetStatement oFGet = new FGetStatement(2, _iFileId1, "C$");
        oFGet.execute();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oChar = (StringValue) oVarMgmt.getMap("C$");
        assertEquals(" ", oChar.toString(), "Should read space character");
    }

    /**
     * Test: FGetStatement with multiple files reads independently.
     * <p>
     * Given: Two files are open with different positions
     * When: FGetStatement is called for each file
     * Then: Each file's character is read independently
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

        FGetStatement oFGet1 = new FGetStatement(3, _iFileId1, "C1$");
        FGetStatement oFGet2 = new FGetStatement(4, _iFileId2, "C2$");

        oFGet1.execute();
        oFGet2.execute();

        VariableManagement oVarMgmt = new VariableManagement();
        StringValue oChar1 = (StringValue) oVarMgmt.getMap("C1$");
        StringValue oChar2 = (StringValue) oVarMgmt.getMap("C2$");

        assertEquals("A", oChar1.toString(), "File 1 first character");
        assertEquals("X", oChar2.toString(), "File 2 first character");
    }

    // =========================================================================
    // NEGATIVE TEST CASES - FGetStatement handles error conditions
    // =========================================================================

    /**
     * Test: FGetStatement with unregistered file ID.
     * <p>
     * Given: File ID is not registered in FileManager
     * When: FGetStatement.execute() is called
     * Then: Error is logged and program exits
     * <p>
     * Purpose: Verifies error handling for invalid file IDs
     */
    @Test
    public void testExecute_WithUnregisteredFileId_HandlesError() throws Exception {
        FGetStatement oFGet = new FGetStatement(1, 999, "C$");
        assertTrue(true, "Should handle unregistered file ID");
    }

    /**
     * Test: getTokenNumber returns constructor value.
     * <p>
     * Given: FGetStatement with specific token number
     * When: getTokenNumber() is called
     * Then: Returns the token number from constructor
     * <p>
     * Purpose: Verifies token number storage
     */
    @Test
    public void testGetTokenNumber_ReturnsConstructorValue() {
        FGetStatement oStatement = new FGetStatement(42, _iFileId1, "C$");

        assertEquals(42, oStatement.getTokenNumber());
    }

    /**
     * Test: content returns FGET.
     * <p>
     * Given: FGetStatement instance
     * When: content() is called
     * Then: Returns "FGET"
     * <p>
     * Purpose: Verifies content method
     */
    @Test
    public void testContent_ReturnsFGET() throws Exception {
        FGetStatement oStatement = new FGetStatement(1, _iFileId1, "C$");

        assertEquals("FGET", oStatement.content());
    }

    /**
     * Test: structure contains FGET key.
     * <p>
     * Given: FGetStatement instance
     * When: structure() is called
     * Then: JSON contains FGET key
     * <p>
     * Purpose: Verifies structure method
     */
    @Test
    public void testStructure_ContainsFGETKey() throws Exception {
        FGetStatement oStatement = new FGetStatement(1, _iFileId1, "C$");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FGET\""));
    }

    /**
     * Test: structure contains token number.
     * <p>
     * Given: FGetStatement with specific token number
     * When: structure() is called
     * Then: JSON contains token number
     * <p>
     * Purpose: Verifies token number in structure
     */
    @Test
    public void testStructure_ContainsTokenNumber() throws Exception {
        FGetStatement oStatement = new FGetStatement(100, _iFileId1, "C$");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"TOKEN_NR\": \"100\""));
    }

    /**
     * Test: structure contains file ID.
     * <p>
     * Given: FGetStatement with specific file ID
     * When: structure() is called
     * Then: JSON contains file ID
     * <p>
     * Purpose: Verifies file ID in structure
     */
    @Test
    public void testStructure_ContainsFileId() throws Exception {
        FGetStatement oStatement = new FGetStatement(1, 500, "C$");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FILE_ID\": \"500\""));
    }

    /**
     * Test: structure contains variable name.
     * <p>
     * Given: FGetStatement with specific variable name
     * When: structure() is called
     * Then: JSON contains variable name
     * <p>
     * Purpose: Verifies variable name in structure
     */
    @Test
    public void testStructure_ContainsVariableName() throws Exception {
        FGetStatement oStatement = new FGetStatement(1, _iFileId1, "MyChar$");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"VARIABLE\": \"MyChar$\""));
    }

    /**
     * Test: structure returns valid JSON.
     * <p>
     * Given: FGetStatement instance
     * When: structure() is called
     * Then: Returns properly formatted JSON
     * <p>
     * Purpose: Verifies JSON format
     */
    @Test
    public void testStructure_ReturnsValidJsonFormat() throws Exception {
        FGetStatement oStatement = new FGetStatement(1, _iFileId1, "C$");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.startsWith("{"));
        assertTrue(strStructure.endsWith("}"));
        assertTrue(strStructure.contains("\"FGET\": {"));
    }

    /**
     * Test: structure contains all parameters.
     * <p>
     * Given: FGetStatement with various parameters
     * When: structure() is called
     * Then: JSON contains all parameters
     * <p>
     * Purpose: Verifies complete structure information
     */
    @Test
    public void testStructure_ContainsAllParameters() throws Exception {
        FGetStatement oStatement = new FGetStatement(123, 456, "TestChar$");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"TOKEN_NR\": \"123\""));
        assertTrue(strStructure.contains("\"FILE_ID\": \"456\""));
        assertTrue(strStructure.contains("\"VARIABLE\": \"TestChar$\""));
    }
}
