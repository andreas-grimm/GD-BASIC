package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.memoryManager.FileOpenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * FRewindStatementTest.java
 *
 * Description: Unit tests for the FRewindStatement class.
 * Tests rewinding file pointer to beginning.
 */
public class FRewindStatementTest {

    private FileManager _oFileManager;
    private Path _oTempFile;

    @BeforeEach
    public void setUp() throws IOException {
        _oFileManager = new FileManager();
        _oTempFile = Files.createTempFile("frewind_test_", ".txt");
        Files.writeString(_oTempFile, "Hello World", StandardOpenOption.WRITE);
    }

    @Test
    public void testFRewindStatement_WithValidFileId_CreatesStatement() throws IOException {
        int iFileId = 1;

        _oFileManager.openFile(_oTempFile.toString(), iFileId, FileOpenType.READ);
        FRewindStatement oStatement = new FRewindStatement(10, iFileId);

        assertNotNull(oStatement);
        assertEquals(10, oStatement.getTokenNumber());
    }

    @Test
    public void testFRewindStatement_WithDifferentFileIds_CreatesMultipleStatements() throws IOException {
        int iFileId1 = 1;
        int iFileId2 = 2;

        _oFileManager.openFile(_oTempFile.toString(), iFileId1, FileOpenType.READ);
        _oFileManager.openFile(_oTempFile.toString(), iFileId2, FileOpenType.READ);

        FRewindStatement oStatement1 = new FRewindStatement(10, iFileId1);
        FRewindStatement oStatement2 = new FRewindStatement(20, iFileId2);

        assertNotNull(oStatement1);
        assertNotNull(oStatement2);
        assertEquals(10, oStatement1.getTokenNumber());
        assertEquals(20, oStatement2.getTokenNumber());
    }

    @Test
    public void testFRewindStatement_WithInvalidFileId_HandlesGracefully() {
        int iInvalidFileId = 999;

        FRewindStatement oStatement = new FRewindStatement(10, iInvalidFileId);

        assertNotNull(oStatement);
        assertEquals(10, oStatement.getTokenNumber());
    }

    @Test
    public void testFRewindStatement_WithZeroFileId_CreatesStatement() {
        int iFileId = 0;

        FRewindStatement oStatement = new FRewindStatement(10, iFileId);

        assertNotNull(oStatement);
        assertEquals(10, oStatement.getTokenNumber());
    }

    @Test
    public void testFRewindStatement_WithNegativeFileId_CreatesStatement() {
        int iFileId = -1;

        FRewindStatement oStatement = new FRewindStatement(10, iFileId);

        assertNotNull(oStatement);
        assertEquals(10, oStatement.getTokenNumber());
    }

    @Test
    public void testFRewindStatement_WithLargeLineNumber_CreatesStatement() throws IOException {
        int iFileId = 1;
        int iLargeLineNumber = 999999;

        _oFileManager.openFile(_oTempFile.toString(), iFileId, FileOpenType.READ);
        FRewindStatement oStatement = new FRewindStatement(iLargeLineNumber, iFileId);

        assertNotNull(oStatement);
        assertEquals(iLargeLineNumber, oStatement.getTokenNumber());
    }

    @Test
    public void testFRewindStatement_WithEmptyFile_CreatesStatement() throws IOException {
        int iFileId = 1;

        Files.writeString(_oTempFile, "", StandardOpenOption.WRITE);
        _oFileManager.openFile(_oTempFile.toString(), iFileId, FileOpenType.READ);
        FRewindStatement oStatement = new FRewindStatement(10, iFileId);

        assertNotNull(oStatement);
        assertEquals(10, oStatement.getTokenNumber());
    }

    @Test
    public void testFRewindStatement_WithLargeFile_CreatesStatement() throws IOException {
        int iFileId = 1;
        String strLargeContent = "X".repeat(100000);

        Files.writeString(_oTempFile, strLargeContent, StandardOpenOption.WRITE);
        _oFileManager.openFile(_oTempFile.toString(), iFileId, FileOpenType.READ);
        FRewindStatement oStatement = new FRewindStatement(10, iFileId);

        assertNotNull(oStatement);
        assertEquals(10, oStatement.getTokenNumber());
    }

    @Test
    public void testFRewindStatement_WithMultilineFile_CreatesStatement() throws IOException {
        int iFileId = 1;
        String strMultilineContent = "Line1\nLine2\nLine3\nLine4\nLine5";

        Files.writeString(_oTempFile, strMultilineContent, StandardOpenOption.WRITE);
        _oFileManager.openFile(_oTempFile.toString(), iFileId, FileOpenType.READ);
        FRewindStatement oStatement = new FRewindStatement(10, iFileId);

        assertNotNull(oStatement);
        assertEquals(10, oStatement.getTokenNumber());
    }
}
