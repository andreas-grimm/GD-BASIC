package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.FileManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the FOpenStatement class.
 * Tests are based on the Statement interface specification and FOpenStatement's public API.
 */
public class FOpenStatementTest {

    private static final int FILE_ID_1 = 101;
    private static final int FILE_ID_2 = 102;
    private static final int FILE_ID_3 = 103;

    private Path _oTempFile1;
    private Path _oTempFile2;
    private Path _oTempFile3;

    @BeforeEach
    public void setUp() throws Exception {
        System.out.println("Setting up test environment...");
        _oTempFile1 = Files.createTempFile("fopen-test-1-", ".txt");
        _oTempFile2 = Files.createTempFile("fopen-test-2-", ".txt");
        _oTempFile3 = Files.createTempFile("fopen-test-3-", ".txt");
    }

    @AfterEach
    public void tearDown() throws Exception {
        System.out.println("Tearing down test environment...");
        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);
        oFileManager.closeFile(FILE_ID_2, false);
        oFileManager.closeFile(FILE_ID_3, false);
        Files.deleteIfExists(_oTempFile1);
        Files.deleteIfExists(_oTempFile2);
        Files.deleteIfExists(_oTempFile3);
    }

    // -------------------------------------------------------------------------
    // getTokenNumber()
    // -------------------------------------------------------------------------

    @Test
    public void testGetTokenNumber_ReturnsConstructorValue() {
        FOpenStatement oStatement = new FOpenStatement(42, 1, "file.txt", "read");

        assertEquals(42, oStatement.getTokenNumber());
    }

    @Test
    public void testGetTokenNumber_WithZero_ReturnsZero() {
        FOpenStatement oStatement = new FOpenStatement(0, 1, "file.txt", "read");

        assertEquals(0, oStatement.getTokenNumber());
    }

    @Test
    public void testGetTokenNumber_WithNegative_ReturnsNegative() {
        FOpenStatement oStatement = new FOpenStatement(-1, 1, "file.txt", "read");

        assertEquals(-1, oStatement.getTokenNumber());
    }

    // -------------------------------------------------------------------------
    // content()
    // -------------------------------------------------------------------------

    @Test
    public void testContent_ReturnsFOPEN() throws Exception {
        FOpenStatement oStatement = new FOpenStatement(1, 1, "file.txt", "read");

        assertEquals("FOPEN", oStatement.content());
    }

    @Test
    public void testContent_WithWriteMode_ReturnsFOPEN() throws Exception {
        FOpenStatement oStatement = new FOpenStatement(1, 1, "file.txt", "write");

        assertEquals("FOPEN", oStatement.content());
    }

    // -------------------------------------------------------------------------
    // structure()
    // -------------------------------------------------------------------------

    @Test
    public void testStructure_ContainsTokenNumber() throws Exception {
        FOpenStatement oStatement = new FOpenStatement(100, 1, "data.txt", "read");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"TOKEN_NR\": \"100\""));
    }

    @Test
    public void testStructure_ContainsFileId() throws Exception {
        FOpenStatement oStatement = new FOpenStatement(1, 7, "data.txt", "read");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FILE_ID\": \"7\""));
    }

    @Test
    public void testStructure_ContainsFileName() throws Exception {
        FOpenStatement oStatement = new FOpenStatement(1, 1, "myfile.dat", "read");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FILE_NAME\": \"myfile.dat\""));
    }

    @Test
    public void testStructure_WithReadMode_ContainsREAD() throws Exception {
        FOpenStatement oStatement = new FOpenStatement(1, 1, "file.txt", "read");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"READ_WRITE\": \"READ\""));
    }

    @Test
    public void testStructure_WithWriteMode_ContainsWRITE() throws Exception {
        FOpenStatement oStatement = new FOpenStatement(1, 1, "file.txt", "write");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"READ_WRITE\": \"WRITE\""));
    }

    @Test
    public void testStructure_WithWriteModeUpperCase_ContainsWRITE() throws Exception {
        FOpenStatement oStatement = new FOpenStatement(1, 1, "file.txt", "WRITE");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"READ_WRITE\": \"WRITE\""));
    }

    @Test
    public void testStructure_WithWriteModeMixedCase_ContainsWRITE() throws Exception {
        FOpenStatement oStatement = new FOpenStatement(1, 1, "file.txt", "Write");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"READ_WRITE\": \"WRITE\""));
    }

    @Test
    public void testStructure_WithNonWriteMode_ContainsREAD() throws Exception {
        FOpenStatement oStatement = new FOpenStatement(1, 1, "file.txt", "read");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"READ_WRITE\": \"READ\""));
    }

    @Test
    public void testStructure_WithEmptyMode_ContainsREAD() throws Exception {
        FOpenStatement oStatement = new FOpenStatement(1, 1, "file.txt", "");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"READ_WRITE\": \"READ\""));
    }

    @Test
    public void testStructure_StartsWithFopenKey() throws Exception {
        FOpenStatement oStatement = new FOpenStatement(1, 1, "file.txt", "read");

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("{\"FOPEN\": {"));
    }

    // -------------------------------------------------------------------------
    // execute()
    // -------------------------------------------------------------------------

    @Test
    public void testExecute_WithValidReadFile_DoesNotThrow() throws Exception {
        FOpenStatement oStatement = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "read");

        oStatement.execute();

        assertTrue(Files.exists(_oTempFile1), "File should exist after open for read");
    }

    @Test
    public void testExecute_WithValidWriteFile_DoesNotThrow() throws Exception {
        FOpenStatement oStatement = new FOpenStatement(1, FILE_ID_2, _oTempFile2.toString(), "write");

        oStatement.execute();

        assertTrue(Files.exists(_oTempFile2), "File should exist after open for write");
    }

    @Test
    public void testExecute_WithWriteModeUpperCase_DoesNotThrow() throws Exception {
        FOpenStatement oStatement = new FOpenStatement(1, FILE_ID_3, _oTempFile3.toString(), "WRITE");

        oStatement.execute();

        assertTrue(Files.exists(_oTempFile3), "File should exist after open for write");
    }

    // -------------------------------------------------------------------------
    // Constructor - parameter handling
    // -------------------------------------------------------------------------

    @Test
    public void testConstructor_WithNullFileName_ThrowsOnExecute() {
        FOpenStatement oStatement = new FOpenStatement(1, 1, null, "read");

        assertThrows(Exception.class, () -> oStatement.execute());
    }

    @Test
    public void testConstructor_WithNullMode_ThrowsOnConstruction() {
        assertThrows(NullPointerException.class, () ->
                new FOpenStatement(1, 1, "file.txt", null));
    }
}
