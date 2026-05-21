package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.memoryManager.FileOpenType;
import eu.gricom.basic.memoryManager.VariableManagement;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the FInputStatement class.
 * Tests are based on the Statement interface specification and FInputStatement's public API.
 * Uses FOpenStatement to create files, FPrintStatement to write one line per file,
 * and FInputStatement to read from the file.
 */
public class FInputStatementTest {

    private static final int FILE_ID_1 = 301;
    private static final int FILE_ID_2 = 302;

    private Path _oTempFile1;
    private Path _oTempFile2;

    @BeforeEach
    public void setUp() throws Exception {
        _oTempFile1 = Files.createTempFile("finput-test-1-", ".txt");
        _oTempFile2 = Files.createTempFile("finput-test-2-", ".txt");
    }

    @AfterEach
    public void tearDown() throws Exception {
        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);
        oFileManager.closeFile(FILE_ID_2, false);
        Files.deleteIfExists(_oTempFile1);
        Files.deleteIfExists(_oTempFile2);
    }

    // -------------------------------------------------------------------------
    // getTokenNumber()
    // -------------------------------------------------------------------------

    @Test
    public void testGetTokenNumber_ReturnsConstructorValue() {
        FInputStatement oStatement = new FInputStatement(42, FILE_ID_1, "A$");

        assertEquals(42, oStatement.getTokenNumber());
    }

    @Test
    public void testGetTokenNumber_WithSimpleConstructor_ReturnsZero() {
        FInputStatement oStatement = new FInputStatement(FILE_ID_1, "A$");

        assertEquals(0, oStatement.getTokenNumber());
    }

    // -------------------------------------------------------------------------
    // content()
    // -------------------------------------------------------------------------

    @Test
    public void testContent_ReturnsFinputFormat() {
        FInputStatement oStatement = new FInputStatement(FILE_ID_1, "A$");

        String strContent = oStatement.content();

        assertTrue(strContent.contains("FINPUT"));
        assertTrue(strContent.contains("301"));
        assertTrue(strContent.contains("A$"));
    }

    @Test
    public void testContent_WithTokenNumberConstructor_ReturnsFinputFormat() {
        FInputStatement oStatement = new FInputStatement(10, FILE_ID_2, "B$");

        String strContent = oStatement.content();

        assertTrue(strContent.contains("FINPUT"));
        assertTrue(strContent.contains("302"));
        assertTrue(strContent.contains("B$"));
    }

    // -------------------------------------------------------------------------
    // structure()
    // -------------------------------------------------------------------------

    @Test
    public void testStructure_ContainsTokenNumber() throws Exception {
        FInputStatement oStatement = new FInputStatement(100, FILE_ID_1, "A$");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"TOKEN_NR\": \"100\""));
    }

    @Test
    public void testStructure_ContainsFileId() throws Exception {
        FInputStatement oStatement = new FInputStatement(1, 7, "A$");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FILE_ID\": \"7\""));
    }

    @Test
    public void testStructure_ContainsVariable() throws Exception {
        FInputStatement oStatement = new FInputStatement(1, FILE_ID_1, "MyVar$");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"VARIABLE\": \"MyVar$\""));
    }

    @Test
    public void testStructure_StartsWithFinputKey() throws Exception {
        FInputStatement oStatement = new FInputStatement(1, FILE_ID_1, "A$");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("{\"FINPUT\": {"));
    }

    // -------------------------------------------------------------------------
    // execute() - FOpen + FPrint + FInput workflow (1 line per file)
    // -------------------------------------------------------------------------

    @Test
    public void testExecute_WithFOpenFPrintFInput_ReadsLineIntoVariable() throws Exception {
        String strLineContent = "Hello from file";

        FOpenStatement oFOpenWrite = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        FPrintStatement oFPrint = new FPrintStatement(2, FILE_ID_1, List.of(new StringValue(strLineContent)), false);

        oFOpenWrite.execute();
        oFPrint.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        FOpenStatement oFOpenRead = new FOpenStatement(3, FILE_ID_1, _oTempFile1.toString(), "read");
        FInputStatement oFInput = new FInputStatement(4, FILE_ID_1, "A$");

        oFOpenRead.execute();
        oFInput.execute();

        VariableManagement oVariableManagement = new VariableManagement();
        StringValue oValue = (StringValue) oVariableManagement.getMap("A$");
        assertEquals(strLineContent, oValue.toString());
    }

    @Test
    public void testExecute_WithNumericLine_ReadsIntoStringVariable() throws Exception {
        String strLineContent = "42";

        FOpenStatement oFOpenWrite = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        FPrintStatement oFPrint = new FPrintStatement(2, FILE_ID_1, List.of(new StringValue(strLineContent)), false);

        oFOpenWrite.execute();
        oFPrint.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        FOpenStatement oFOpenRead = new FOpenStatement(3, FILE_ID_1, _oTempFile1.toString(), "read");
        FInputStatement oFInput = new FInputStatement(4, FILE_ID_1, "X$");

        oFOpenRead.execute();
        oFInput.execute();

        VariableManagement oVariableManagement = new VariableManagement();
        StringValue oValue = (StringValue) oVariableManagement.getMap("X$");
        assertEquals("42", oValue.toString());
    }

    @Test
    public void testExecute_WithEmptyLine_ReadsEmptyString() throws Exception {
        String strLineContent = "";

        FOpenStatement oFOpenWrite = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        FPrintStatement oFPrint = new FPrintStatement(2, FILE_ID_1, List.of(new StringValue(strLineContent)), false);

        oFOpenWrite.execute();
        oFPrint.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        FOpenStatement oFOpenRead = new FOpenStatement(3, FILE_ID_1, _oTempFile1.toString(), "read");
        FInputStatement oFInput = new FInputStatement(4, FILE_ID_1, "Empty$");

        oFOpenRead.execute();
        oFInput.execute();

        VariableManagement oVariableManagement = new VariableManagement();
        StringValue oValue = (StringValue) oVariableManagement.getMap("Empty$");
        assertEquals("", oValue.toString());
    }

    @Test
    public void testExecute_WithSimpleConstructor_ReadsLineIntoVariable() throws Exception {
        String strLineContent = "Simple constructor test";

        FOpenStatement oFOpenWrite = new FOpenStatement(1, FILE_ID_2, _oTempFile2.toString(), "write");
        FPrintStatement oFPrint = new FPrintStatement(2, FILE_ID_2, List.of(new StringValue(strLineContent)), false);

        oFOpenWrite.execute();
        oFPrint.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_2, false);

        FOpenStatement oFOpenRead = new FOpenStatement(3, FILE_ID_2, _oTempFile2.toString(), "read");
        FInputStatement oFInput = new FInputStatement(FILE_ID_2, "B$");

        oFOpenRead.execute();
        oFInput.execute();

        VariableManagement oVariableManagement = new VariableManagement();
        StringValue oValue = (StringValue) oVariableManagement.getMap("B$");
        assertEquals(strLineContent, oValue.toString());
    }

    @Test
    public void testExecute_WhenFileNotOpen_ThrowsException() {
        FInputStatement oFInput = new FInputStatement(FILE_ID_1, "A$");

        assertThrows(Exception.class, oFInput::execute);
    }

    // -------------------------------------------------------------------------
    // execute() - Read Position Tracking Tests
    // -------------------------------------------------------------------------

    /**
     * Test: FInputStatement updates read cursor position after reading a line.
     * <p>
     * Given: File is open for reading with initial position 0
     * When: FInputStatement.execute() reads a line
     * Then: Read cursor position is updated to the length of the read input
     * <p>
     * Purpose: Verifies position tracking after single read
     */
    @Test
    public void testExecute_UpdatesReadPosition_AfterReadingLine() throws Exception {
        String strLineContent = "Hello";

        FOpenStatement oFOpenWrite = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        FPrintStatement oFPrint = new FPrintStatement(2, FILE_ID_1, List.of(new StringValue(strLineContent)), false);

        oFOpenWrite.execute();
        oFPrint.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        FOpenStatement oFOpenRead = new FOpenStatement(3, FILE_ID_1, _oTempFile1.toString(), "read");
        FInputStatement oFInput = new FInputStatement(4, FILE_ID_1, "A$");

        oFOpenRead.execute();
        oFInput.execute();

        oFileManager = new FileManager();
        int iNewPosition = oFileManager.getReadPos(FILE_ID_1).toInt();

        assertEquals(strLineContent.length(), iNewPosition, "Position should equal length of read input");
    }

    /**
     * Test: FInputStatement accumulates read cursor position across multiple reads.
     * <p>
     * Given: File with multiple lines is open for reading
     * When: FInputStatement.execute() is called multiple times
     * Then: Read cursor position accumulates (previous position + new read length)
     * <p>
     * Purpose: Verifies position tracking across sequential reads
     */
    @Test
    public void testExecute_AccumulatesReadPosition_AcrossMultipleReads() throws Exception {
        String strLine1 = "First";
        String strLine2 = "Second";
        String strLine3 = "Third";

        Files.writeString(_oTempFile1, strLine1 + "\n" + strLine2 + "\n" + strLine3 + "\n");

        FOpenStatement oFOpenRead = new FOpenStatement(5, FILE_ID_1, _oTempFile1.toString(), "read");
        oFOpenRead.execute();

        FInputStatement oFInput1 = new FInputStatement(6, FILE_ID_1, "A$");
        FInputStatement oFInput2 = new FInputStatement(7, FILE_ID_1, "B$");
        FInputStatement oFInput3 = new FInputStatement(8, FILE_ID_1, "C$");

        oFInput1.execute();
        int iPos1 = new FileManager().getReadPos(FILE_ID_1).toInt();

        oFInput2.execute();
        int iPos2 = new FileManager().getReadPos(FILE_ID_1).toInt();

        oFInput3.execute();
        int iPos3 = new FileManager().getReadPos(FILE_ID_1).toInt();

        assertEquals(strLine1.length(), iPos1, "Position after first read");
        assertEquals(strLine1.length() + strLine2.length(), iPos2, "Position after second read");
        assertEquals(strLine1.length() + strLine2.length() + strLine3.length(), iPos3, "Position after third read");
    }

    /**
     * Test: FInputStatement with empty line updates position correctly.
     * <p>
     * Given: File contains an empty line
     * When: FInputStatement.execute() reads the empty line
     * Then: Read cursor position is incremented by 0 (length of empty string)
     * <p>
     * Purpose: Verifies edge case with empty input
     */
    @Test
    public void testExecute_WithEmptyLine_PositionIncrementsByZero() throws Exception {
        String strLineContent = "";

        FOpenStatement oFOpenWrite = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        FPrintStatement oFPrint = new FPrintStatement(2, FILE_ID_1, List.of(new StringValue(strLineContent)), false);

        oFOpenWrite.execute();
        oFPrint.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        FOpenStatement oFOpenRead = new FOpenStatement(3, FILE_ID_1, _oTempFile1.toString(), "read");
        FInputStatement oFInput = new FInputStatement(4, FILE_ID_1, "Empty$");

        oFOpenRead.execute();
        oFInput.execute();

        int iNewPosition = new FileManager().getReadPos(FILE_ID_1).toInt();
        assertEquals(0, iNewPosition, "Position should remain 0 for empty line");
    }

    /**
     * Test: FInputStatement with long input updates position correctly.
     * <p>
     * Given: File contains a long line
     * When: FInputStatement.execute() reads the line
     * Then: Read cursor position equals the full length of the input
     * <p>
     * Purpose: Verifies position tracking with larger inputs
     */
    @Test
    public void testExecute_WithLongInput_PositionEqualsInputLength() throws Exception {
        String strLineContent = "This is a much longer line with many characters";

        FOpenStatement oFOpenWrite = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        FPrintStatement oFPrint = new FPrintStatement(2, FILE_ID_1, List.of(new StringValue(strLineContent)), false);

        oFOpenWrite.execute();
        oFPrint.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        FOpenStatement oFOpenRead = new FOpenStatement(3, FILE_ID_1, _oTempFile1.toString(), "read");
        FInputStatement oFInput = new FInputStatement(4, FILE_ID_1, "Long$");

        oFOpenRead.execute();
        oFInput.execute();

        int iNewPosition = new FileManager().getReadPos(FILE_ID_1).toInt();
        assertEquals(strLineContent.length(), iNewPosition, "Position should equal input length");
    }

    /**
     * Test: FInputStatement with numeric content updates position correctly.
     * <p>
     * Given: File contains numeric content
     * When: FInputStatement.execute() reads the numeric line
     * Then: Read cursor position is updated by the length of the number string
     * <p>
     * Purpose: Verifies position tracking is based on string length, not value
     */
    @Test
    public void testExecute_WithNumericContent_PositionByStringLength() throws Exception {
        String strLineContent = "123456";

        FOpenStatement oFOpenWrite = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        FPrintStatement oFPrint = new FPrintStatement(2, FILE_ID_1, List.of(new StringValue(strLineContent)), false);

        oFOpenWrite.execute();
        oFPrint.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        FOpenStatement oFOpenRead = new FOpenStatement(3, FILE_ID_1, _oTempFile1.toString(), "read");
        FInputStatement oFInput = new FInputStatement(4, FILE_ID_1, "Num$");

        oFOpenRead.execute();
        oFInput.execute();

        int iNewPosition = new FileManager().getReadPos(FILE_ID_1).toInt();
        assertEquals(6, iNewPosition, "Position should be based on string length (6)");
    }

    /**
     * Test: Direct position verification after multiple reads from different files.
     * <p>
     * Given: Multiple files are open and being read independently
     * When: FInputStatement.execute() reads from each file
     * Then: Each file maintains its own independent cursor position
     * <p>
     * Purpose: Verifies isolation of position tracking between file IDs
     */
    @Test
    public void testExecute_WithMultipleFiles_MaintainsIndependentPositions() throws Exception {
        String strFile1Line = "File1";
        String strFile2Line = "File2Data";

        FOpenStatement oFOpenWrite1 = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        FOpenStatement oFOpenWrite2 = new FOpenStatement(2, FILE_ID_2, _oTempFile2.toString(), "write");
        FPrintStatement oFPrint1 = new FPrintStatement(3, FILE_ID_1, List.of(new StringValue(strFile1Line)), false);
        FPrintStatement oFPrint2 = new FPrintStatement(4, FILE_ID_2, List.of(new StringValue(strFile2Line)), false);

        oFOpenWrite1.execute();
        oFOpenWrite2.execute();
        oFPrint1.execute();
        oFPrint2.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);
        oFileManager.closeFile(FILE_ID_2, false);

        FOpenStatement oFOpenRead1 = new FOpenStatement(5, FILE_ID_1, _oTempFile1.toString(), "read");
        FOpenStatement oFOpenRead2 = new FOpenStatement(6, FILE_ID_2, _oTempFile2.toString(), "read");
        oFOpenRead1.execute();
        oFOpenRead2.execute();

        FInputStatement oFInput1 = new FInputStatement(7, FILE_ID_1, "A$");
        FInputStatement oFInput2 = new FInputStatement(8, FILE_ID_2, "B$");

        oFInput1.execute();
        oFInput2.execute();

        oFileManager = new FileManager();
        int iPos1 = oFileManager.getReadPos(FILE_ID_1).toInt();
        int iPos2 = oFileManager.getReadPos(FILE_ID_2).toInt();

        assertEquals(strFile1Line.length(), iPos1, "File 1 position");
        assertEquals(strFile2Line.length(), iPos2, "File 2 position");
    }

    /**
     * Test: Read position tracking with special characters in input.
     * <p>
     * Given: File contains line with special characters
     * When: FInputStatement.execute() reads the line
     * Then: Position is incremented by the full character count including special chars
     * <p>
     * Purpose: Verifies position tracking counts all characters correctly
     */
    @Test
    public void testExecute_WithSpecialCharacters_PositionCountsAllCharacters() throws Exception {
        String strLineContent = "Hello@#$%^&*()";

        FOpenStatement oFOpenWrite = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        FPrintStatement oFPrint = new FPrintStatement(2, FILE_ID_1, List.of(new StringValue(strLineContent)), false);

        oFOpenWrite.execute();
        oFPrint.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        FOpenStatement oFOpenRead = new FOpenStatement(3, FILE_ID_1, _oTempFile1.toString(), "read");
        FInputStatement oFInput = new FInputStatement(4, FILE_ID_1, "Special$");

        oFOpenRead.execute();
        oFInput.execute();

        int iNewPosition = new FileManager().getReadPos(FILE_ID_1).toInt();
        assertEquals(strLineContent.length(), iNewPosition, "Position should count all characters");
    }
}
