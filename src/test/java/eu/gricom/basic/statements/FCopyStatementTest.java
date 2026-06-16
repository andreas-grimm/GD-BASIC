package eu.gricom.basic.statements;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.memoryManager.FileOpenType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FCopyStatementTest.java
 * <p>
 * Unit tests for the FCopyStatement class.
 * <p>
 * This test class provides comprehensive coverage of the FCopyStatement, which copies the content
 * from a source file to a destination file. Both files must be registered in FileManager before
 * the copy operation can proceed.
 * <p>
 * Test Categories:
 * - POSITIVE TESTS: FCopyStatement copies files with various content
 * - NEGATIVE TESTS: FCopyStatement handles error conditions
 * - EDGE CASES: Empty files, special characters, and file state verification
 * - INTERFACE TESTS: getTokenNumber(), content(), and structure() methods
 * <p>
 * Key Behavior:
 * - Copies content line-by-line from source to destination
 * - Requires both file IDs to be registered in FileManager
 * - Throws RuntimeException for missing or unregistered files
 * - Preserves line structure with newline characters
 * - Overwrites destination file content completely
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FCopyStatementTest {

    private static final int SOURCE_FILE_ID = 501;
    private static final int DEST_FILE_ID = 502;
    private static final int TOKEN_NUMBER = 100;

    private Path _oSourceFile;
    private Path _oDestFile;
    private FileManager _oFileManager;

    /**
     * Setup method: Initializes test environment before each test.
     * Creates temporary files and FileManager instance.
     */
    @BeforeEach
    public void setUp() throws Exception {
        _oSourceFile = Files.createTempFile("fcopy-source-", ".txt");
        _oDestFile = Files.createTempFile("fcopy-dest-", ".txt");
        _oFileManager = new FileManager();
    }

    /**
     * Teardown method: Cleans up after each test.
     * Closes files and deletes temporary files.
     */
    @AfterEach
    public void tearDown() throws Exception {
        _oFileManager.closeFile(SOURCE_FILE_ID, false);
        _oFileManager.closeFile(DEST_FILE_ID, false);
        Files.deleteIfExists(_oSourceFile);
        Files.deleteIfExists(_oDestFile);
    }

    // =========================================================================
    // POSITIVE TEST CASES - FCopyStatement copies files successfully
    // =========================================================================

    /**
     * Test: FCopyStatement copies single line file.
     * <p>
     * Given: Source file contains a single line
     * When: FCopyStatement.execute() is called
     * Then: Destination file contains the same line
     * <p>
     * Purpose: Verifies basic copy functionality
     */
    @Test
    public void testExecute_WithSingleLineFile_CopiesContentSuccessfully() throws Exception {
        // Setup: Write single line to source file
        Files.write(_oSourceFile, "Hello World".getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oSourceFile.toString(), SOURCE_FILE_ID, FileOpenType.READ);
        _oFileManager.openFile(_oDestFile.toString(), DEST_FILE_ID, FileOpenType.WRITE);

        // Execute: Copy file
        FCopyStatement oStatement = new FCopyStatement(TOKEN_NUMBER, SOURCE_FILE_ID, DEST_FILE_ID);
        oStatement.execute();

        // Verify: Destination contains same content
        String strContent = new String(Files.readAllBytes(_oDestFile), StandardCharsets.UTF_8).trim();
        assertEquals("Hello World", strContent);
    }

    /**
     * Test: FCopyStatement copies multi-line file.
     * <p>
     * Given: Source file contains multiple lines
     * When: FCopyStatement.execute() is called
     * Then: Destination file contains all lines in correct order
     * <p>
     * Purpose: Verifies multi-line copy preserves content and structure
     */
    @Test
    public void testExecute_WithMultiLineFile_CopiesAllLines() throws Exception {
        // Setup: Write multiple lines to source file
        String strContent = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
        Files.write(_oSourceFile, strContent.getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oSourceFile.toString(), SOURCE_FILE_ID, FileOpenType.READ);
        _oFileManager.openFile(_oDestFile.toString(), DEST_FILE_ID, FileOpenType.WRITE);

        // Execute: Copy file
        FCopyStatement oStatement = new FCopyStatement(TOKEN_NUMBER, SOURCE_FILE_ID, DEST_FILE_ID);
        oStatement.execute();

        // Verify: Destination contains all lines
        List<String> oLines = Files.readAllLines(_oDestFile);
        assertEquals(5, oLines.size());
        assertEquals("Line 1", oLines.get(0));
        assertEquals("Line 5", oLines.get(4));
    }

    /**
     * Test: FCopyStatement copies empty file.
     * <p>
     * Given: Source file is empty
     * When: FCopyStatement.execute() is called
     * Then: Destination file remains empty or becomes empty
     * <p>
     * Purpose: Verifies empty file copy handles gracefully
     */
    @Test
    public void testExecute_WithEmptySourceFile_CopiesEmptyFile() throws Exception {
        // Setup: Create empty source file
        _oFileManager.openFile(_oSourceFile.toString(), SOURCE_FILE_ID, FileOpenType.READ);
        _oFileManager.openFile(_oDestFile.toString(), DEST_FILE_ID, FileOpenType.WRITE);

        // Execute: Copy file
        FCopyStatement oStatement = new FCopyStatement(TOKEN_NUMBER, SOURCE_FILE_ID, DEST_FILE_ID);
        oStatement.execute();

        // Verify: Destination is empty
        List<String> oLines = Files.readAllLines(_oDestFile);
        assertEquals(0, oLines.size());
    }

    /**
     * Test: FCopyStatement copies file with special characters.
     * <p>
     * Given: Source file contains special characters and unicode
     * When: FCopyStatement.execute() is called
     * Then: Destination file preserves all special characters
     * <p>
     * Purpose: Verifies special character preservation in copy
     */
    @Test
    public void testExecute_WithSpecialCharacters_PreservesContent() throws Exception {
        // Setup: Write special characters to source file
        String strContent = "Line with special chars: @#$%^&*()\nUnicode: café, naïve";
        Files.write(_oSourceFile, strContent.getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oSourceFile.toString(), SOURCE_FILE_ID, FileOpenType.READ);
        _oFileManager.openFile(_oDestFile.toString(), DEST_FILE_ID, FileOpenType.WRITE);

        // Execute: Copy file
        FCopyStatement oStatement = new FCopyStatement(TOKEN_NUMBER, SOURCE_FILE_ID, DEST_FILE_ID);
        oStatement.execute();

        // Verify: Destination preserves special characters
        List<String> oLines = Files.readAllLines(_oDestFile);
        assertTrue(oLines.get(0).contains("@#$%^&*()"));
        assertTrue(oLines.get(1).contains("café"));
    }

    /**
     * Test: FCopyStatement overwrites existing destination file.
     * <p>
     * Given: Destination file already contains content
     * When: FCopyStatement.execute() is called
     * Then: Destination file content is replaced with source content
     * <p>
     * Purpose: Verifies destination file is overwritten
     */
    @Test
    public void testExecute_WithExistingDestinationFile_OverwritesContent() throws Exception {
        // Setup: Write content to both files
        Files.write(_oSourceFile, "New Content".getBytes(StandardCharsets.UTF_8));
        Files.write(_oDestFile, "Old Content".getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oSourceFile.toString(), SOURCE_FILE_ID, FileOpenType.READ);
        _oFileManager.openFile(_oDestFile.toString(), DEST_FILE_ID, FileOpenType.WRITE);

        // Execute: Copy file
        FCopyStatement oStatement = new FCopyStatement(TOKEN_NUMBER, SOURCE_FILE_ID, DEST_FILE_ID);
        oStatement.execute();

        // Verify: Destination contains new content
        String strContent = new String(Files.readAllBytes(_oDestFile), StandardCharsets.UTF_8).trim();
        assertEquals("New Content", strContent);
    }

    /**
     * Test: FCopyStatement with file containing blank lines.
     * <p>
     * Given: Source file contains blank lines
     * When: FCopyStatement.execute() is called
     * Then: Destination file preserves blank lines
     * <p>
     * Purpose: Verifies blank line preservation
     */
    @Test
    public void testExecute_WithBlankLines_PreservesBlankLines() throws Exception {
        // Setup: Write content with blank lines
        String strContent = "Line 1\n\nLine 3\n\n\nLine 6";
        Files.write(_oSourceFile, strContent.getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oSourceFile.toString(), SOURCE_FILE_ID, FileOpenType.READ);
        _oFileManager.openFile(_oDestFile.toString(), DEST_FILE_ID, FileOpenType.WRITE);

        // Execute: Copy file
        FCopyStatement oStatement = new FCopyStatement(TOKEN_NUMBER, SOURCE_FILE_ID, DEST_FILE_ID);
        oStatement.execute();

        // Verify: Destination preserves blank lines
        List<String> oLines = Files.readAllLines(_oDestFile);
        assertEquals(6, oLines.size());
        assertEquals("", oLines.get(1));
        assertEquals("", oLines.get(3));
    }

    // =========================================================================
    // NEGATIVE TEST CASES - FCopyStatement handles error conditions
    // =========================================================================

    /**
     * Test: FCopyStatement with unregistered source file ID.
     * <p>
     * Given: Source file ID is not registered in FileManager
     * When: FCopyStatement.execute() is called
     * Then: RuntimeException is thrown
     * <p>
     * Purpose: Verifies error handling for unregistered source file
     */
    @Test
    public void testExecute_WithUnregisteredSourceFileId_ThrowsRuntimeException() throws Exception {
        // Setup: Register only destination file
        _oFileManager.openFile(_oDestFile.toString(), DEST_FILE_ID, FileOpenType.WRITE);

        // Execute and verify: RuntimeException thrown
        FCopyStatement oStatement = new FCopyStatement(TOKEN_NUMBER, SOURCE_FILE_ID, DEST_FILE_ID);
        assertThrows(RuntimeException.class, oStatement::execute);
    }

    /**
     * Test: FCopyStatement with unregistered destination file ID.
     * <p>
     * Given: Destination file ID is not registered in FileManager
     * When: FCopyStatement.execute() is called
     * Then: RuntimeException is thrown
     * <p>
     * Purpose: Verifies error handling for unregistered destination file
     */
    @Test
    public void testExecute_WithUnregisteredDestinationFileId_ThrowsRuntimeException() throws Exception {
        // Setup: Register only source file
        _oFileManager.openFile(_oSourceFile.toString(), SOURCE_FILE_ID, FileOpenType.READ);

        // Execute and verify: RuntimeException thrown
        FCopyStatement oStatement = new FCopyStatement(TOKEN_NUMBER, SOURCE_FILE_ID, DEST_FILE_ID);
        assertThrows(RuntimeException.class, oStatement::execute);
    }

    /**
     * Test: FCopyStatement with both files unregistered.
     * <p>
     * Given: Neither file ID is registered in FileManager
     * When: FCopyStatement.execute() is called
     * Then: RuntimeException is thrown
     * <p>
     * Purpose: Verifies error handling when both files unregistered
     */
    @Test
    public void testExecute_WithBothFilesUnregistered_ThrowsRuntimeException() {
        // Execute and verify: RuntimeException thrown
        FCopyStatement oStatement = new FCopyStatement(TOKEN_NUMBER, SOURCE_FILE_ID, DEST_FILE_ID);
        assertThrows(RuntimeException.class, oStatement::execute);
    }

    /**
     * Test: FCopyStatement copies large file with many lines.
     * <p>
     * Given: Source file contains large amount of content
     * When: FCopyStatement.execute() is called
     * Then: All content is copied correctly to destination
     * <p>
     * Purpose: Verifies copy performance with larger files
     */
    @Test
    public void testExecute_WithLargeFile_CopiesAllContent() throws Exception {
        // Setup: Create large file with 100 lines
        StringBuilder sbContent = new StringBuilder();
        for (int i = 1; i <= 100; i++) {
            sbContent.append("Line ").append(i).append("\n");
        }
        Files.write(_oSourceFile, sbContent.toString().getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oSourceFile.toString(), SOURCE_FILE_ID, FileOpenType.READ);
        _oFileManager.openFile(_oDestFile.toString(), DEST_FILE_ID, FileOpenType.WRITE);

        // Execute: Copy file
        FCopyStatement oStatement = new FCopyStatement(TOKEN_NUMBER, SOURCE_FILE_ID, DEST_FILE_ID);
        oStatement.execute();

        // Verify: Destination contains all 100 lines
        List<String> oLines = Files.readAllLines(_oDestFile);
        assertEquals(100, oLines.size());
        assertEquals("Line 1", oLines.get(0));
        assertEquals("Line 100", oLines.get(99));
    }

    // =========================================================================
    // INTERFACE TEST CASES - Test public interface methods
    // =========================================================================

    /**
     * Test: getTokenNumber returns constructor value.
     * <p>
     * Given: FCopyStatement with specific token number
     * When: getTokenNumber() is called
     * Then: Returns the token number from constructor
     * <p>
     * Purpose: Verifies token number storage and retrieval
     */
    @Test
    public void testGetTokenNumber_ReturnsConstructorValue() {
        FCopyStatement oStatement = new FCopyStatement(42, SOURCE_FILE_ID, DEST_FILE_ID);

        assertEquals(42, oStatement.getTokenNumber());
    }

    /**
     * Test: getTokenNumber with various token numbers.
     * <p>
     * Given: FCopyStatement with different token numbers
     * When: getTokenNumber() is called
     * Then: Returns the correct token number
     * <p>
     * Purpose: Verifies token number accuracy
     */
    @Test
    public void testGetTokenNumber_WithZero_ReturnsZero() {
        FCopyStatement oStatement = new FCopyStatement(0, SOURCE_FILE_ID, DEST_FILE_ID);

        assertEquals(0, oStatement.getTokenNumber());
    }

    /**
     * Test: getTokenNumber with negative token number.
     * <p>
     * Given: FCopyStatement with negative token number
     * When: getTokenNumber() is called
     * Then: Returns the negative token number
     * <p>
     * Purpose: Verifies negative token number handling
     */
    @Test
    public void testGetTokenNumber_WithNegative_ReturnsNegative() {
        FCopyStatement oStatement = new FCopyStatement(-1, SOURCE_FILE_ID, DEST_FILE_ID);

        assertEquals(-1, oStatement.getTokenNumber());
    }

    /**
     * Test: content returns FCOPY.
     * <p>
     * Given: FCopyStatement instance
     * When: content() is called
     * Then: Returns "FCOPY"
     * <p>
     * Purpose: Verifies content method return value
     */
    @Test
    public void testContent_ReturnsFCOPY() throws Exception {
        FCopyStatement oStatement = new FCopyStatement(1, SOURCE_FILE_ID, DEST_FILE_ID);

        assertEquals("FCOPY", oStatement.content());
    }

    /**
     * Test: content returns FCOPY regardless of parameters.
     * <p>
     * Given: FCopyStatement with various parameters
     * When: content() is called
     * Then: Always returns "FCOPY"
     * <p>
     * Purpose: Verifies consistent content return
     */
    @Test
    public void testContent_WithDifferentParameters_ReturnsFCOPY() throws Exception {
        FCopyStatement oStatement = new FCopyStatement(100, 999, 888);

        assertEquals("FCOPY", oStatement.content());
    }

    /**
     * Test: structure contains FCOPY key.
     * <p>
     * Given: FCopyStatement instance
     * When: structure() is called
     * Then: JSON contains FCOPY key
     * <p>
     * Purpose: Verifies structure JSON format
     */
    @Test
    public void testStructure_ContainsFCOPYKey() throws Exception {
        FCopyStatement oStatement = new FCopyStatement(TOKEN_NUMBER, SOURCE_FILE_ID, DEST_FILE_ID);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FCOPY\""));
    }

    /**
     * Test: structure contains token number.
     * <p>
     * Given: FCopyStatement with specific token number
     * When: structure() is called
     * Then: JSON contains token number
     * <p>
     * Purpose: Verifies token number in structure
     */
    @Test
    public void testStructure_ContainsTokenNumber() throws Exception {
        FCopyStatement oStatement = new FCopyStatement(100, SOURCE_FILE_ID, DEST_FILE_ID);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"TOKEN_NR\": \"100\""));
    }

    /**
     * Test: structure contains source file ID.
     * <p>
     * Given: FCopyStatement with specific source file ID
     * When: structure() is called
     * Then: JSON contains source file ID
     * <p>
     * Purpose: Verifies source file ID in structure
     */
    @Test
    public void testStructure_ContainsSourceFileId() throws Exception {
        FCopyStatement oStatement = new FCopyStatement(TOKEN_NUMBER, 501, DEST_FILE_ID);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"SOURCE_FILE_ID\": \"501\""));
    }

    /**
     * Test: structure contains destination file ID.
     * <p>
     * Given: FCopyStatement with specific destination file ID
     * When: structure() is called
     * Then: JSON contains destination file ID
     * <p>
     * Purpose: Verifies destination file ID in structure
     */
    @Test
    public void testStructure_ContainsDestinationFileId() throws Exception {
        FCopyStatement oStatement = new FCopyStatement(TOKEN_NUMBER, SOURCE_FILE_ID, 502);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"DESTINATION_FILE_ID\": \"502\""));
    }

    /**
     * Test: structure returns valid JSON.
     * <p>
     * Given: FCopyStatement instance
     * When: structure() is called
     * Then: Returns properly formatted JSON
     * <p>
     * Purpose: Verifies JSON format is valid
     */
    @Test
    public void testStructure_ReturnsValidJsonFormat() throws Exception {
        FCopyStatement oStatement = new FCopyStatement(TOKEN_NUMBER, SOURCE_FILE_ID, DEST_FILE_ID);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.startsWith("{"));
        assertTrue(strStructure.endsWith("}"));
        assertTrue(strStructure.contains("\"FCOPY\": {"));
    }
}
