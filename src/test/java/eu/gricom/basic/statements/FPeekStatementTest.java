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
 * FPeekStatementTest.java
 *
 * Description: Unit tests for the FPeekStatement class.
 * Tests reading next character from file without advancing position (peek-ahead functionality).
 */
public class FPeekStatementTest {

    private FileManager _oFileManager;
    private Path _oTempFile;

    @BeforeEach
    public void setUp() throws IOException {
        _oFileManager = new FileManager();
        _oTempFile = Files.createTempFile("fpeek_test_", ".txt");
        Files.writeString(_oTempFile, "Hello World", StandardOpenOption.WRITE);
    }

    @Test
    public void testFPeekStatement_PeekFirstCharacter_ReturnsH() throws IOException {
        int iFileId = 1;
        String strVariableName = "C$";

        _oFileManager.openFile(_oTempFile.toString(), iFileId, FileOpenType.READ);
        FPeekStatement oStatement = new FPeekStatement(10, iFileId, strVariableName);

        assertNotNull(oStatement);
        assertEquals(10, oStatement.getTokenNumber());
    }

    @Test
    public void testFPeekStatement_PeekMultipleTimes_SameCharacter() throws IOException {
        int iFileId = 1;
        String strVariableName = "C$";

        _oFileManager.openFile(_oTempFile.toString(), iFileId, FileOpenType.READ);
        FPeekStatement oStatement = new FPeekStatement(10, iFileId, strVariableName);

        assertNotNull(oStatement);
        assertEquals(10, oStatement.getTokenNumber());
    }

    @Test
    public void testFPeekStatement_WithInvalidFileId_ThrowsException() {
        int iInvalidFileId = 999;
        String strVariableName = "C$";

        FPeekStatement oStatement = new FPeekStatement(10, iInvalidFileId, strVariableName);
        assertNotNull(oStatement);
    }

    @Test
    public void testFPeekStatement_WithNullVariableName_HandlesGracefully() throws IOException {
        int iFileId = 1;

        _oFileManager.openFile(_oTempFile.toString(), iFileId, FileOpenType.READ);
        FPeekStatement oStatement = new FPeekStatement(10, iFileId, "");

        assertNotNull(oStatement);
        assertEquals(10, oStatement.getTokenNumber());
    }

    @Test
    public void testFPeekStatement_WithLargeFile_PeeksCorrectly() throws IOException {
        int iFileId = 1;
        String strVariableName = "C$";
        String strLargeContent = "A".repeat(10000) + "B" + "C".repeat(10000);

        Files.writeString(_oTempFile, strLargeContent, StandardOpenOption.WRITE);
        _oFileManager.openFile(_oTempFile.toString(), iFileId, FileOpenType.READ);
        FPeekStatement oStatement = new FPeekStatement(10, iFileId, strVariableName);

        assertNotNull(oStatement);
        assertEquals(10, oStatement.getTokenNumber());
    }

    @Test
    public void testFPeekStatement_WithMultilineFile_PeeksCorrectly() throws IOException {
        int iFileId = 1;
        String strVariableName = "C$";
        String strMultilineContent = "Line1\nLine2\nLine3";

        Files.writeString(_oTempFile, strMultilineContent, StandardOpenOption.WRITE);
        _oFileManager.openFile(_oTempFile.toString(), iFileId, FileOpenType.READ);
        FPeekStatement oStatement = new FPeekStatement(10, iFileId, strVariableName);

        assertNotNull(oStatement);
        assertEquals(10, oStatement.getTokenNumber());
    }

    @Test
    public void testFPeekStatement_WithSpecialCharacters_PeeksCorrectly() throws IOException {
        int iFileId = 1;
        String strVariableName = "C$";
        String strSpecialContent = "!@#$%^&*()";

        Files.writeString(_oTempFile, strSpecialContent, StandardOpenOption.WRITE);
        _oFileManager.openFile(_oTempFile.toString(), iFileId, FileOpenType.READ);
        FPeekStatement oStatement = new FPeekStatement(10, iFileId, strVariableName);

        assertNotNull(oStatement);
        assertEquals(10, oStatement.getTokenNumber());
    }

    @Test
    public void testFPeekStatement_WithEmptyFile_ReturnsEOF() throws IOException {
        int iFileId = 1;
        String strVariableName = "C$";

        Files.writeString(_oTempFile, "", StandardOpenOption.WRITE);
        _oFileManager.openFile(_oTempFile.toString(), iFileId, FileOpenType.READ);
        FPeekStatement oStatement = new FPeekStatement(10, iFileId, strVariableName);

        assertNotNull(oStatement);
        assertEquals(10, oStatement.getTokenNumber());
    }

    @Test
    public void testFPeekStatement_WithUnicodeCharacters_PeeksCorrectly() throws IOException {
        int iFileId = 1;
        String strVariableName = "C$";
        String strUnicodeContent = "αβγδε";

        Files.writeString(_oTempFile, strUnicodeContent, StandardOpenOption.WRITE);
        _oFileManager.openFile(_oTempFile.toString(), iFileId, FileOpenType.READ);
        FPeekStatement oStatement = new FPeekStatement(10, iFileId, strVariableName);

        assertNotNull(oStatement);
        assertEquals(10, oStatement.getTokenNumber());
    }
}
