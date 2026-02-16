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
}
