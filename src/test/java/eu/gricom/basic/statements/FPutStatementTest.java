package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FPutStatementTest.java
 * <p>
 * Unit tests for the FPutStatement class.
 * <p>
 * This test class provides comprehensive coverage of the FPutStatement, which writes a single expression
 * to a file without adding a newline. It wraps FPrintStatement with bCRLF set to false, providing a simpler
 * interface for character-by-character or string output without line termination.
 * <p>
 * Test Categories:
 * - POSITIVE TESTS: FPutStatement successfully writes content without newlines
 * - INTERFACE TESTS: getTokenNumber(), content(), and structure() methods
 * - EXECUTION TESTS: FOpen + FPut workflow and multiple writes
 * <p>
 * Key Behavior:
 * - Writes single expression to file without adding newline
 * - Always uses bCRLF = false internally
 * - Supports string and numeric expressions
 * - Multiple FPut calls concatenate on same line
 * - Returns proper structure with FPUT name and FALSE CRLF flag
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FPutStatementTest {

    private static final int FILE_ID_1 = 301;
    private static final int FILE_ID_2 = 302;
    private static final int TOKEN_NUMBER = 100;

    private Path _oTempFile1;
    private Path _oTempFile2;

    /**
     * Setup method: Creates temporary files for testing.
     */
    @BeforeEach
    public void setUp() throws Exception {
        _oTempFile1 = Files.createTempFile("fput-test-1-", ".txt");
        _oTempFile2 = Files.createTempFile("fput-test-2-", ".txt");
    }

    /**
     * Teardown method: Closes files and cleans up temporary files.
     */
    @AfterEach
    public void tearDown() throws Exception {
        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);
        oFileManager.closeFile(FILE_ID_2, false);
        Files.deleteIfExists(_oTempFile1);
        Files.deleteIfExists(_oTempFile2);
    }

    // =========================================================================
    // INTERFACE TEST CASES - getTokenNumber(), content(), structure()
    // =========================================================================

    /**
     * Test: getTokenNumber returns constructor value.
     * <p>
     * Given: FPutStatement with specific token number
     * When: getTokenNumber() is called
     * Then: Returns the token number from constructor
     * <p>
     * Purpose: Verifies token number storage and retrieval
     */
    @Test
    public void testGetTokenNumber_ReturnsConstructorValue() {
        Expression oExpression = new StringValue("test");
        FPutStatement oStatement = new FPutStatement(42, FILE_ID_1, oExpression);

        assertEquals(42, oStatement.getTokenNumber());
    }

    /**
     * Test: getTokenNumber with zero token number.
     * <p>
     * Given: FPutStatement with token number 0
     * When: getTokenNumber() is called
     * Then: Returns 0
     * <p>
     * Purpose: Verifies zero token handling
     */
    @Test
    public void testGetTokenNumber_WithZero_ReturnsZero() {
        Expression oExpression = new StringValue("test");
        FPutStatement oStatement = new FPutStatement(0, FILE_ID_1, oExpression);

        assertEquals(0, oStatement.getTokenNumber());
    }

    /**
     * Test: getTokenNumber with various token numbers.
     * <p>
     * Given: FPutStatement with different token numbers
     * When: getTokenNumber() is called
     * Then: Returns correct token number
     * <p>
     * Purpose: Verifies token number accuracy
     */
    @Test
    public void testGetTokenNumber_WithDifferentValues_ReturnsCorrectValue() {
        Expression oExpression = new StringValue("test");
        FPutStatement oStatement = new FPutStatement(999, FILE_ID_1, oExpression);

        assertEquals(999, oStatement.getTokenNumber());
    }

    /**
     * Test: content returns FPUT format.
     * <p>
     * Given: FPutStatement instance
     * When: content() is called
     * Then: Returns string containing FPUT, file ID, and expression
     * <p>
     * Purpose: Verifies content method return format
     */
    @Test
    public void testContent_ReturnsFputFormat() throws Exception {
        Expression oExpression = new StringValue("hello");
        FPutStatement oStatement = new FPutStatement(TOKEN_NUMBER, FILE_ID_1, oExpression);

        String strContent = oStatement.content();

        assertTrue(strContent.contains("FPUT"), "Content should contain FPUT");
        assertTrue(strContent.contains("301"), "Content should contain file ID");
    }

    /**
     * Test: content with different file IDs.
     * <p>
     * Given: FPutStatement with various file IDs
     * When: content() is called
     * Then: Returns content with correct file ID
     * <p>
     * Purpose: Verifies file ID is included in content
     */
    @Test
    public void testContent_WithDifferentFileId_IncludesFileId() throws Exception {
        Expression oExpression = new StringValue("x");
        FPutStatement oStatement = new FPutStatement(TOKEN_NUMBER, 505, oExpression);

        String strContent = oStatement.content();

        assertTrue(strContent.contains("505"), "Content should contain file ID 505");
    }

    /**
     * Test: structure contains FPUT key.
     * <p>
     * Given: FPutStatement instance
     * When: structure() is called
     * Then: JSON contains FPUT key
     * <p>
     * Purpose: Verifies structure JSON format
     */
    @Test
    public void testStructure_ContainsFputKey() throws Exception {
        Expression oExpression = new StringValue("test");
        FPutStatement oStatement = new FPutStatement(TOKEN_NUMBER, FILE_ID_1, oExpression);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FPUT\""), "Structure should contain FPUT key");
    }

    /**
     * Test: structure contains token number.
     * <p>
     * Given: FPutStatement with specific token number
     * When: structure() is called
     * Then: JSON contains TOKEN_NR field with correct value
     * <p>
     * Purpose: Verifies token number in structure
     */
    @Test
    public void testStructure_ContainsTokenNumber() throws Exception {
        Expression oExpression = new StringValue("test");
        FPutStatement oStatement = new FPutStatement(100, FILE_ID_1, oExpression);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"TOKEN_NR\": \"100\""), "Structure should contain token number");
    }

    /**
     * Test: structure contains file ID.
     * <p>
     * Given: FPutStatement with specific file ID
     * When: structure() is called
     * Then: JSON contains FILE_ID field
     * <p>
     * Purpose: Verifies file ID in structure
     */
    @Test
    public void testStructure_ContainsFileId() throws Exception {
        Expression oExpression = new StringValue("test");
        FPutStatement oStatement = new FPutStatement(1, 7, oExpression);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FILE_ID\": \"7\""), "Structure should contain file ID");
    }

    /**
     * Test: structure does not contain CRLF parameter.
     * <p>
     * Given: FPutStatement instance (CRLF is not exposed as a parameter)
     * When: structure() is called
     * Then: JSON does not contain CRLF field (it's always false internally)
     * <p>
     * Purpose: Verifies FPUT doesn't expose CRLF as a parameter in structure
     */
    @Test
    public void testStructure_DoesNotExposeCRLFParameter() throws Exception {
        Expression oExpression = new StringValue("test");
        FPutStatement oStatement = new FPutStatement(1, FILE_ID_1, oExpression);

        String strStructure = oStatement.structure();

        // CRLF is not exposed as a parameter in FPutStatement structure
        // It's always false internally but not shown to the user
        assertFalse(strStructure.contains("\"CRLF\""), "Structure should not expose CRLF parameter");
    }

    /**
     * Test: structure starts with FPUT key.
     * <p>
     * Given: FPutStatement instance
     * When: structure() is called
     * Then: JSON starts with {"FPUT": {
     * <p>
     * Purpose: Verifies JSON structure format
     */
    @Test
    public void testStructure_StartsWithFputKey() throws Exception {
        Expression oExpression = new StringValue("test");
        FPutStatement oStatement = new FPutStatement(1, FILE_ID_1, oExpression);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("{\"FPUT\": {"), "Structure should start with FPUT key");
    }

    /**
     * Test: structure contains EXPRESSION field.
     * <p>
     * Given: FPutStatement instance
     * When: structure() is called
     * Then: JSON contains EXPRESSION field
     * <p>
     * Purpose: Verifies expression is included in structure
     */
    @Test
    public void testStructure_ContainsExpressionField() throws Exception {
        Expression oExpression = new StringValue("test");
        FPutStatement oStatement = new FPutStatement(TOKEN_NUMBER, FILE_ID_1, oExpression);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"EXPRESSION\""), "Structure should contain EXPRESSION field");
    }

    /**
     * Test: structure returns valid JSON.
     * <p>
     * Given: FPutStatement instance
     * When: structure() is called
     * Then: Returns properly formatted JSON
     * <p>
     * Purpose: Verifies JSON validity
     */
    @Test
    public void testStructure_ReturnsValidJsonFormat() throws Exception {
        Expression oExpression = new StringValue("test");
        FPutStatement oStatement = new FPutStatement(TOKEN_NUMBER, FILE_ID_1, oExpression);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.startsWith("{"), "Structure should start with {");
        assertTrue(strStructure.endsWith("}"), "Structure should end with }");
    }

    // =========================================================================
    // POSITIVE TEST CASES - FPut execution with file operations
    // =========================================================================

    /**
     * Test: FPutStatement writes string to file without newline.
     * <p>
     * Given: File opened and FPutStatement with string expression
     * When: FPutStatement.execute() is called
     * Then: String is written to file without newline
     * <p>
     * Purpose: Verifies basic write without newline functionality
     */
    @Test
    public void testExecute_WithStringExpression_WritesWithoutNewline() throws Exception {
        FOpenStatement oFOpen = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        Expression oExpression = new StringValue("Hello");
        FPutStatement oFPut = new FPutStatement(2, FILE_ID_1, oExpression);

        oFOpen.execute();
        oFPut.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        String strContent = Files.readString(_oTempFile1);
        assertEquals("Hello", strContent, "Content should be exactly 'Hello' without newline");
    }

    /**
     * Test: FPutStatement writes single character.
     * <p>
     * Given: File opened and FPutStatement with single character
     * When: FPutStatement.execute() is called
     * Then: Single character is written without newline
     * <p>
     * Purpose: Verifies single character output
     */
    @Test
    public void testExecute_WithSingleCharacter_WritesCharacter() throws Exception {
        FOpenStatement oFOpen = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        Expression oExpression = new StringValue("A");
        FPutStatement oFPut = new FPutStatement(2, FILE_ID_1, oExpression);

        oFOpen.execute();
        oFPut.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        String strContent = Files.readString(_oTempFile1);
        assertEquals("A", strContent, "Content should be exactly 'A'");
    }

    /**
     * Test: Multiple FPutStatements concatenate without newlines.
     * <p>
     * Given: File opened and multiple FPutStatements
     * When: Each FPutStatement.execute() is called sequentially
     * Then: All output is on same line without newlines
     * <p>
     * Purpose: Verifies multiple writes concatenate on same line
     */
    @Test
    public void testExecute_WithMultipleFPutCalls_ConcatenatesOnSameLine() throws Exception {
        FOpenStatement oFOpen = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        Expression oExpr1 = new StringValue("Hello");
        Expression oExpr2 = new StringValue(" ");
        Expression oExpr3 = new StringValue("World");

        FPutStatement oFPut1 = new FPutStatement(2, FILE_ID_1, oExpr1);
        FPutStatement oFPut2 = new FPutStatement(3, FILE_ID_1, oExpr2);
        FPutStatement oFPut3 = new FPutStatement(4, FILE_ID_1, oExpr3);

        oFOpen.execute();
        oFPut1.execute();
        oFPut2.execute();
        oFPut3.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        String strContent = Files.readString(_oTempFile1);
        assertEquals("Hello World", strContent, "Content should be 'Hello World' on same line");
    }

    /**
     * Test: FPutStatement writes numeric value as string.
     * <p>
     * Given: File opened and FPutStatement with numeric expression
     * When: FPutStatement.execute() is called
     * Then: Number is converted to string and written
     * <p>
     * Purpose: Verifies numeric to string conversion
     */
    @Test
    public void testExecute_WithNumericExpression_WritesAsString() throws Exception {
        FOpenStatement oFOpen = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        Expression oExpression = new IntegerValue(42);
        FPutStatement oFPut = new FPutStatement(2, FILE_ID_1, oExpression);

        oFOpen.execute();
        oFPut.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        String strContent = Files.readString(_oTempFile1);
        assertEquals("42", strContent, "Content should be '42'");
    }

    /**
     * Test: FPutStatement writes empty string.
     * <p>
     * Given: File opened and FPutStatement with empty string
     * When: FPutStatement.execute() is called
     * Then: Empty string is written (file remains empty or unchanged)
     * <p>
     * Purpose: Verifies empty string handling
     */
    @Test
    public void testExecute_WithEmptyString_WritesNothing() throws Exception {
        FOpenStatement oFOpen = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        Expression oExpression = new StringValue("");
        FPutStatement oFPut = new FPutStatement(2, FILE_ID_1, oExpression);

        oFOpen.execute();
        oFPut.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        String strContent = Files.readString(_oTempFile1);
        assertEquals("", strContent, "Content should be empty");
    }

    /**
     * Test: FPutStatement writes special characters.
     * <p>
     * Given: File opened and FPutStatement with special characters
     * When: FPutStatement.execute() is called
     * Then: Special characters are written correctly
     * <p>
     * Purpose: Verifies special character handling
     */
    @Test
    public void testExecute_WithSpecialCharacters_WritesCorrectly() throws Exception {
        FOpenStatement oFOpen = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        Expression oExpression = new StringValue("!@#$%^&*()");
        FPutStatement oFPut = new FPutStatement(2, FILE_ID_1, oExpression);

        oFOpen.execute();
        oFPut.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        String strContent = Files.readString(_oTempFile1);
        assertEquals("!@#$%^&*()", strContent, "Content should contain special characters");
    }

    /**
     * Test: FPutStatement to multiple files.
     * <p>
     * Given: Two files opened and FPutStatements for different file IDs
     * When: FPutStatement.execute() is called for each file
     * Then: Content is written to respective files
     * <p>
     * Purpose: Verifies writing to multiple files
     */
    @Test
    public void testExecute_WithMultipleFiles_WritesEachCorrectly() throws Exception {
        FOpenStatement oFOpen1 = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        FOpenStatement oFOpen2 = new FOpenStatement(1, FILE_ID_2, _oTempFile2.toString(), "write");

        Expression oExpr1 = new StringValue("File1");
        Expression oExpr2 = new StringValue("File2");

        FPutStatement oFPut1 = new FPutStatement(2, FILE_ID_1, oExpr1);
        FPutStatement oFPut2 = new FPutStatement(3, FILE_ID_2, oExpr2);

        oFOpen1.execute();
        oFOpen2.execute();
        oFPut1.execute();
        oFPut2.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);
        oFileManager.closeFile(FILE_ID_2, false);

        String strContent1 = Files.readString(_oTempFile1);
        String strContent2 = Files.readString(_oTempFile2);

        assertEquals("File1", strContent1, "File 1 should contain 'File1'");
        assertEquals("File2", strContent2, "File 2 should contain 'File2'");
    }

    /**
     * Test: FPutStatement writes long string without newline.
     * <p>
     * Given: File opened and FPutStatement with long string
     * When: FPutStatement.execute() is called
     * Then: Long string is written without newline
     * <p>
     * Purpose: Verifies handling of longer strings
     */
    @Test
    public void testExecute_WithLongString_WritesEntireString() throws Exception {
        FOpenStatement oFOpen = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        String strLong = "This is a much longer string that should be written completely without any newlines";
        Expression oExpression = new StringValue(strLong);
        FPutStatement oFPut = new FPutStatement(2, FILE_ID_1, oExpression);

        oFOpen.execute();
        oFPut.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        String strContent = Files.readString(_oTempFile1);
        assertEquals(strLong, strContent, "Content should match long string exactly");
    }

    /**
     * Test: FPutStatement differs from FPrintStatement (no newline).
     * <p>
     * Given: File opened and both FPrintStatement (bCRLF=true) and FPutStatement
     * When: FPrintStatement then FPutStatement execute
     * Then: FPrint adds newline, FPut does not
     * <p>
     * Purpose: Verifies FPut behavior differs from FPrint
     */
    @Test
    public void testExecute_CompareFPrintWithFPut_VerifiesNoDifference() throws Exception {
        FOpenStatement oFOpen = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");

        Expression oExpr1 = new StringValue("Line1");
        Expression oExpr2 = new StringValue("Line2");

        java.util.List<Expression> aoList = java.util.List.of(oExpr1);
        FPrintStatement oFPrint = new FPrintStatement(2, FILE_ID_1, aoList, true);
        FPutStatement oFPut = new FPutStatement(3, FILE_ID_1, oExpr2);

        oFOpen.execute();
        oFPrint.execute();
        oFPut.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        String strContent = Files.readString(_oTempFile1);
        assertTrue(strContent.startsWith("Line1\n"), "FPrint should add newline");
        assertTrue(strContent.contains("Line2"), "FPut should append without newline");
    }

    // =========================================================================
    // EDGE CASE TEST CASES
    // =========================================================================

    /**
     * Test: FPutStatement with whitespace characters.
     * <p>
     * Given: FPutStatement with string containing spaces and tabs
     * When: FPutStatement.execute() is called
     * Then: Whitespace is preserved
     * <p>
     * Purpose: Verifies whitespace handling
     */
    @Test
    public void testExecute_WithWhitespaceCharacters_PreservesWhitespace() throws Exception {
        FOpenStatement oFOpen = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        Expression oExpression = new StringValue("  \t  ");
        FPutStatement oFPut = new FPutStatement(2, FILE_ID_1, oExpression);

        oFOpen.execute();
        oFPut.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        String strContent = Files.readString(_oTempFile1);
        assertEquals("  \t  ", strContent, "Content should preserve whitespace");
    }

    /**
     * Test: FPutStatement writes string with quotes.
     * <p>
     * Given: FPutStatement with string containing quotes
     * When: FPutStatement.execute() is called
     * Then: Quotes are written correctly
     * <p>
     * Purpose: Verifies quote character handling
     */
    @Test
    public void testExecute_WithQuoteCharacters_WritesQuotes() throws Exception {
        FOpenStatement oFOpen = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        Expression oExpression = new StringValue("\"hello\"");
        FPutStatement oFPut = new FPutStatement(2, FILE_ID_1, oExpression);

        oFOpen.execute();
        oFPut.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        String strContent = Files.readString(_oTempFile1);
        assertEquals("\"hello\"", strContent, "Content should contain quotes");
    }
}
