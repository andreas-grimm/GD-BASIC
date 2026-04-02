package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the FPrintStatement class.
 * Tests are based on the Statement interface specification and FPrintStatement's public API.
 * Uses FOpenStatement to create files and FPrintStatement to write to them.
 */
public class FPrintStatementTest {

    private static final int FILE_ID_1 = 201;
    private static final int FILE_ID_2 = 202;

    private Path _oTempFile1;
    private Path _oTempFile2;

    @BeforeEach
    public void setUp() throws Exception {
        _oTempFile1 = Files.createTempFile("fprint-test-1-", ".txt");
        _oTempFile2 = Files.createTempFile("fprint-test-2-", ".txt");
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
        List<Expression> aoExpression = List.of(new StringValue("test"));
        FPrintStatement oStatement = new FPrintStatement(42, FILE_ID_1, aoExpression, true);

        assertEquals(42, oStatement.getTokenNumber());
    }

    @Test
    public void testGetTokenNumber_WithSimpleConstructor_ReturnsZero() {
        FPrintStatement oStatement = new FPrintStatement(FILE_ID_1, new StringValue("test"));

        assertEquals(0, oStatement.getTokenNumber());
    }

    // -------------------------------------------------------------------------
    // content()
    // -------------------------------------------------------------------------

    @Test
    public void testContent_WithListExpression_ReturnsFprintFormat() {
        List<Expression> aoExpression = List.of(new StringValue("hello"));
        FPrintStatement oStatement = new FPrintStatement(1, FILE_ID_1, aoExpression, true);

        String strContent = oStatement.content();

        assertTrue(strContent.contains("FPRINT"));
        assertTrue(strContent.contains("201"));
    }

    @Test
    public void testContent_WithSimpleExpression_ReturnsFprintFormat() {
        FPrintStatement oStatement = new FPrintStatement(FILE_ID_1, new StringValue("x"));

        String strContent = oStatement.content();

        assertTrue(strContent.contains("FPRINT"));
        assertTrue(strContent.contains("201"));
    }

    // -------------------------------------------------------------------------
    // structure()
    // -------------------------------------------------------------------------

    @Test
    public void testStructure_ContainsTokenNumber() throws Exception {
        List<Expression> aoExpression = List.of(new StringValue("test"));
        FPrintStatement oStatement = new FPrintStatement(100, FILE_ID_1, aoExpression, true);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"TOKEN_NR\": \"100\""));
    }

    @Test
    public void testStructure_ContainsFileId() throws Exception {
        List<Expression> aoExpression = List.of(new StringValue("test"));
        FPrintStatement oStatement = new FPrintStatement(1, 7, aoExpression, true);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FILE_ID\": \"7\""));
    }

    @Test
    public void testStructure_WithCRLFTrue_ContainsTRUE() throws Exception {
        List<Expression> aoExpression = List.of(new StringValue("test"));
        FPrintStatement oStatement = new FPrintStatement(1, FILE_ID_1, aoExpression, true);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"CRLF\": \"TRUE\""));
    }

    @Test
    public void testStructure_WithCRLFFalse_ContainsFALSE() throws Exception {
        List<Expression> aoExpression = List.of(new StringValue("test"));
        FPrintStatement oStatement = new FPrintStatement(1, FILE_ID_1, aoExpression, false);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"CRLF\": \"FALSE\""));
    }

    @Test
    public void testStructure_StartsWithFprintKey() throws Exception {
        List<Expression> aoExpression = List.of(new StringValue("test"));
        FPrintStatement oStatement = new FPrintStatement(1, FILE_ID_1, aoExpression, true);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("{\"FPRINT\": {"));
    }

    // -------------------------------------------------------------------------
    // execute() - FOpen + FPrint workflow
    // -------------------------------------------------------------------------

    @Test
    public void testExecute_WithFOpenAndFPrint_WritesContentToFile() throws Exception {
        FOpenStatement oFOpen = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        List<Expression> aoExpression = new ArrayList<>();
        aoExpression.add(new StringValue("Hello, World"));

        FPrintStatement oFPrint = new FPrintStatement(2, FILE_ID_1, aoExpression, true);

        oFOpen.execute();
        oFPrint.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        String strContent = Files.readString(_oTempFile1);
        assertTrue(strContent.contains("Hello, World"));
    }

    @Test
    public void testExecute_WithMultipleExpressions_WritesAllToFile() throws Exception {
        FOpenStatement oFOpen = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        List<Expression> aoExpression = new ArrayList<>();
        aoExpression.add(new StringValue("Part1"));
        aoExpression.add(new StringValue("Part2"));

        FPrintStatement oFPrint = new FPrintStatement(2, FILE_ID_1, aoExpression, true);

        oFOpen.execute();
        oFPrint.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        String strContent = Files.readString(_oTempFile1);
        assertTrue(strContent.contains("Part1"));
        assertTrue(strContent.contains("Part2"));
    }

    @Test
    public void testExecute_WithMultipleFPrintCalls_AppendsContent() throws Exception {
        FOpenStatement oFOpen = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        List<Expression> aoExpression1 = List.of(new StringValue("First"));
        List<Expression> aoExpression2 = List.of(new StringValue("Second"));

        FPrintStatement oFPrint1 = new FPrintStatement(2, FILE_ID_1, aoExpression1, true);
        FPrintStatement oFPrint2 = new FPrintStatement(3, FILE_ID_1, aoExpression2, true);

        oFOpen.execute();
        oFPrint1.execute();
        oFPrint2.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        String strContent = Files.readString(_oTempFile1);
        assertTrue(strContent.contains("First"));
        assertTrue(strContent.contains("Second"));
    }

    @Test
    public void testExecute_WithCRLFFalse_DoesNotAddNewline() throws Exception {
        FOpenStatement oFOpen = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        List<Expression> aoExpression = List.of(new StringValue("NoNewline"));

        FPrintStatement oFPrint = new FPrintStatement(2, FILE_ID_1, aoExpression, false);

        oFOpen.execute();
        oFPrint.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        String strContent = Files.readString(_oTempFile1);
        assertEquals("NoNewline", strContent);
    }

    @Test
    public void testExecute_WithEmptyString_WritesToFile() throws Exception {
        FOpenStatement oFOpen = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        List<Expression> aoExpression = List.of(new StringValue(""));

        FPrintStatement oFPrint = new FPrintStatement(2, FILE_ID_1, aoExpression, true);

        oFOpen.execute();
        oFPrint.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        String strContent = Files.readString(_oTempFile1);
        assertEquals("", strContent);
    }

    // -------------------------------------------------------------------------
    // getExpression()
    // -------------------------------------------------------------------------

    @Test
    public void testGetExpression_ReturnsFirstExpression() {
        List<Expression> aoExpression = new ArrayList<>();
        Expression oFirst = new StringValue("first");
        aoExpression.add(oFirst);
        aoExpression.add(new StringValue("second"));

        FPrintStatement oStatement = new FPrintStatement(1, FILE_ID_1, aoExpression, true);

        assertEquals(oFirst, oStatement.getExpression());
    }

    @Test
    public void testGetExpression_WithSimpleConstructor_ThrowsOnCall() {
        FPrintStatement oStatement = new FPrintStatement(FILE_ID_1, new StringValue("test"));

        assertThrows(AssertionError.class, oStatement::getExpression);
    }

    // -------------------------------------------------------------------------
    // structure() with null assertion
    // -------------------------------------------------------------------------

    @Test
    public void testStructure_WithSimpleConstructor_ThrowsOnCall() {
        FPrintStatement oStatement = new FPrintStatement(FILE_ID_1, new StringValue("test"));

        assertThrows(AssertionError.class, () -> oStatement.structure());
    }
}
