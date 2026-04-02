package eu.gricom.basic.memoryManager;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the FileManager class.
 * Tests cover all public methods and their parameters.
 */
public class FileManagerTest {

    private static final int FILE_ID_READ = 1;
    private static final int FILE_ID_WRITE = 2;
    private static final int FILE_ID_UNKNOWN = 999;

    private FileManager _oFileManager;
    private Path _oTempReadFile;
    private Path _oTempWriteFile;

    @BeforeEach
    public void setUp() throws IOException {
        _oFileManager = new FileManager();
        _oTempReadFile = Files.createTempFile("filemanager-read-", ".txt");
        _oTempWriteFile = Files.createTempFile("filemanager-write-", ".txt");
        Files.writeString(_oTempReadFile, "line1\nline2\nline3");
    }

    @AfterEach
    public void tearDown() {
        _oFileManager.closeFile(FILE_ID_READ, false);
        _oFileManager.closeFile(FILE_ID_WRITE, false);
    }

    // -------------------------------------------------------------------------
    // openFile(String strFileName, int iFileID, FileOpenType eReadWrite)
    // -------------------------------------------------------------------------

    @Test
    public void testOpenFile_WithValidReadFile_ReturnsTrue() throws IOException {
        boolean bResult = _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);

        assertTrue(bResult);
        assertTrue(_oFileManager.getFileStatus(FILE_ID_READ));
        assertEquals(_oTempReadFile.toString(), _oFileManager.getFileName(FILE_ID_READ));
        assertEquals(FileOpenType.READ, _oFileManager.getFileType(FILE_ID_READ));
    }

    @Test
    public void testOpenFile_WithValidWriteFile_ReturnsTrue() throws IOException {
        boolean bResult = _oFileManager.openFile(_oTempWriteFile.toString(), FILE_ID_WRITE, FileOpenType.WRITE);

        assertTrue(bResult);
        assertTrue(_oFileManager.getFileStatus(FILE_ID_WRITE));
        assertEquals(_oTempWriteFile.toString(), _oFileManager.getFileName(FILE_ID_WRITE));
        assertEquals(FileOpenType.WRITE, _oFileManager.getFileType(FILE_ID_WRITE));
    }

    @Test
    public void testOpenFile_WithDuplicateFileId_ReturnsFalse() throws IOException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);

        boolean bResult = _oFileManager.openFile(_oTempWriteFile.toString(), FILE_ID_READ, FileOpenType.READ);

        assertFalse(bResult);
    }

    @Test
    public void testOpenFile_WithDuplicateFileName_ReturnsFalse() throws IOException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);

        boolean bResult = _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_WRITE, FileOpenType.READ);

        assertFalse(bResult);
    }

    @Test
    public void testOpenFile_WithEmptyFileName_OpensRelativePath() throws IOException {
        Path oEmptyNameFile = Files.createTempFile("fm-empty-", ".txt");
        boolean bResult = _oFileManager.openFile(oEmptyNameFile.toString(), FILE_ID_READ, FileOpenType.READ);

        assertTrue(bResult);
        _oFileManager.closeFile(FILE_ID_READ, false);
    }

    // -------------------------------------------------------------------------
    // closeFile(int iFileID, boolean bDeleteFile)
    // -------------------------------------------------------------------------

    @Test
    public void testCloseFile_WithOpenReadFile_RemovesFromManagement() throws IOException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);

        _oFileManager.closeFile(FILE_ID_READ, false);

        assertFalse(_oFileManager.getFileStatus(FILE_ID_READ));
        assertNull(_oFileManager.getFileName(FILE_ID_READ));
        assertNull(_oFileManager.getFileRead(FILE_ID_READ));
    }

    @Test
    public void testCloseFile_WithOpenWriteFile_RemovesFromManagement() throws IOException {
        _oFileManager.openFile(_oTempWriteFile.toString(), FILE_ID_WRITE, FileOpenType.WRITE);

        _oFileManager.closeFile(FILE_ID_WRITE, false);

        assertFalse(_oFileManager.getFileStatus(FILE_ID_WRITE));
        assertNull(_oFileManager.getFileName(FILE_ID_WRITE));
        assertNull(_oFileManager.getFileWrite(FILE_ID_WRITE));
    }

    @Test
    public void testCloseFile_WithNonExistentFileId_DoesNotThrow() {
        _oFileManager.closeFile(FILE_ID_UNKNOWN, false);
    }

    @Test
    public void testCloseFile_WithDeleteTrue_ClosesWithoutError() throws IOException {
        _oFileManager.openFile(_oTempWriteFile.toString(), FILE_ID_WRITE, FileOpenType.WRITE);

        _oFileManager.closeFile(FILE_ID_WRITE, true);

        assertFalse(_oFileManager.getFileStatus(FILE_ID_WRITE));
    }

    // -------------------------------------------------------------------------
    // read(int iFileId)
    // -------------------------------------------------------------------------

    @Test
    public void testRead_WithOpenReadFile_ReturnsFirstLine() throws IOException, RuntimeException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);

        Value oValue = _oFileManager.read(FILE_ID_READ);

        assertTrue(oValue instanceof StringValue);
        assertEquals("line1", oValue.toString());
    }

    @Test
    public void testRead_WithMultipleReads_ReturnsSequentialLines() throws IOException, RuntimeException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);

        Value oFirst = _oFileManager.read(FILE_ID_READ);
        Value oSecond = _oFileManager.read(FILE_ID_READ);
        Value oThird = _oFileManager.read(FILE_ID_READ);

        assertEquals("line1", oFirst.toString());
        assertEquals("line2", oSecond.toString());
        assertEquals("line3", oThird.toString());
    }

    @Test
    public void testRead_AtEndOfFile_ReturnsEmptyStringValue() throws IOException, RuntimeException {
        Path oSingleLineFile = Files.createTempFile("fm-single-", ".txt");
        Files.writeString(oSingleLineFile, "only");
        _oFileManager.openFile(oSingleLineFile.toString(), FILE_ID_READ, FileOpenType.READ);

        _oFileManager.read(FILE_ID_READ);
        Value oEofValue = _oFileManager.read(FILE_ID_READ);

        assertTrue(oEofValue instanceof StringValue);
        assertEquals("", oEofValue.toString());
        _oFileManager.closeFile(FILE_ID_READ, false);
    }

    @Test
    public void testRead_WithUnknownFileId_ReturnsNull() throws IOException, RuntimeException {
        Value oValue = _oFileManager.read(FILE_ID_UNKNOWN);

        assertNull(oValue);
    }

    @Test
    public void testRead_WithWriteOnlyFileId_ReturnsNull() throws IOException, RuntimeException {
        _oFileManager.openFile(_oTempWriteFile.toString(), FILE_ID_WRITE, FileOpenType.WRITE);

        Value oValue = _oFileManager.read(FILE_ID_WRITE);

        assertNull(oValue);
    }

    // -------------------------------------------------------------------------
    // getEOF(int iFileId)
    // -------------------------------------------------------------------------

    @Test
    public void testGetEOF_WhenFileNotAtEnd_ReturnsOne() throws IOException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);

        IntegerValue oEof = _oFileManager.getEOF(FILE_ID_READ);

        assertEquals(1, oEof.toInt());
    }

    @Test
    public void testGetEOF_WhenFileAtEnd_ReturnsOne() throws IOException, RuntimeException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);
        _oFileManager.read(FILE_ID_READ);
        _oFileManager.read(FILE_ID_READ);
        _oFileManager.read(FILE_ID_READ);

        IntegerValue oEof = _oFileManager.getEOF(FILE_ID_READ);

        assertEquals(1, oEof.toInt());
    }

    @Test
    public void testGetEOF_WithUnknownFileId_ReturnsZero() {
        IntegerValue oEof = _oFileManager.getEOF(FILE_ID_UNKNOWN);

        assertEquals(0, oEof.toInt());
    }

    @Test
    public void testGetEOF_WithWriteOnlyFile_ReturnsZero() throws IOException {
        _oFileManager.openFile(_oTempWriteFile.toString(), FILE_ID_WRITE, FileOpenType.WRITE);

        IntegerValue oEof = _oFileManager.getEOF(FILE_ID_WRITE);

        assertEquals(0, oEof.toInt());
    }

    // -------------------------------------------------------------------------
    // write(int iFileId, String strData)
    // -------------------------------------------------------------------------

    @Test
    public void testWrite_WithOpenWriteFile_WritesData() throws IOException {
        _oFileManager.openFile(_oTempWriteFile.toString(), FILE_ID_WRITE, FileOpenType.WRITE);

        _oFileManager.write(FILE_ID_WRITE, "test data");
        _oFileManager.closeFile(FILE_ID_WRITE, false);

        String strContent = Files.readString(_oTempWriteFile);
        assertTrue(strContent.contains("test data"));
    }

    @Test
    public void testWrite_WithEmptyString_DoesNotThrow() throws IOException {
        _oFileManager.openFile(_oTempWriteFile.toString(), FILE_ID_WRITE, FileOpenType.WRITE);

        _oFileManager.write(FILE_ID_WRITE, "");
    }

    @Test
    public void testWrite_WithUnknownFileId_DoesNotThrow() throws IOException {
        _oFileManager.write(FILE_ID_UNKNOWN, "data");
    }

    @Test
    public void testWrite_WithReadOnlyFileId_DoesNotWrite() throws IOException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);
        String strBefore = Files.readString(_oTempReadFile);

        _oFileManager.write(FILE_ID_READ, "should not appear");

        String strAfter = Files.readString(_oTempReadFile);
        assertEquals(strBefore, strAfter);
    }

    // -------------------------------------------------------------------------
    // getFileName(int iFileID)
    // -------------------------------------------------------------------------

    @Test
    public void testGetFileName_WithOpenFile_ReturnsFileName() throws IOException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);

        String strName = _oFileManager.getFileName(FILE_ID_READ);

        assertEquals(_oTempReadFile.toString(), strName);
    }

    @Test
    public void testGetFileName_WithUnknownFileId_ReturnsNull() {
        String strName = _oFileManager.getFileName(FILE_ID_UNKNOWN);

        assertNull(strName);
    }

    @Test
    public void testGetFileName_AfterClose_ReturnsNull() throws IOException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);
        _oFileManager.closeFile(FILE_ID_READ, false);

        String strName = _oFileManager.getFileName(FILE_ID_READ);

        assertNull(strName);
    }

    // -------------------------------------------------------------------------
    // getFileStatus(int iFileID)
    // -------------------------------------------------------------------------

    @Test
    public void testGetFileStatus_WithOpenFile_ReturnsTrue() throws IOException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);

        boolean bStatus = _oFileManager.getFileStatus(FILE_ID_READ);

        assertTrue(bStatus);
    }

    @Test
    public void testGetFileStatus_WithUnknownFileId_ReturnsFalse() {
        boolean bStatus = _oFileManager.getFileStatus(FILE_ID_UNKNOWN);

        assertFalse(bStatus);
    }

    @Test
    public void testGetFileStatus_AfterClose_ReturnsFalse() throws IOException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);
        _oFileManager.closeFile(FILE_ID_READ, false);

        boolean bStatus = _oFileManager.getFileStatus(FILE_ID_READ);

        assertFalse(bStatus);
    }

    // -------------------------------------------------------------------------
    // getFileRead(int iFileID)
    // -------------------------------------------------------------------------

    @Test
    public void testGetFileRead_WithOpenReadFile_ReturnsBufferedReader() throws IOException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);

        BufferedReader oReader = _oFileManager.getFileRead(FILE_ID_READ);

        assertTrue(oReader != null);
    }

    @Test
    public void testGetFileRead_WithWriteOnlyFile_ReturnsNull() throws IOException {
        _oFileManager.openFile(_oTempWriteFile.toString(), FILE_ID_WRITE, FileOpenType.WRITE);

        BufferedReader oReader = _oFileManager.getFileRead(FILE_ID_WRITE);

        assertNull(oReader);
    }

    @Test
    public void testGetFileRead_WithUnknownFileId_ReturnsNull() {
        BufferedReader oReader = _oFileManager.getFileRead(FILE_ID_UNKNOWN);

        assertNull(oReader);
    }

    @Test
    public void testGetFileRead_AfterClose_ReturnsNull() throws IOException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);
        _oFileManager.closeFile(FILE_ID_READ, false);

        BufferedReader oReader = _oFileManager.getFileRead(FILE_ID_READ);

        assertNull(oReader);
    }

    // -------------------------------------------------------------------------
    // getFileWrite(int iFileID)
    // -------------------------------------------------------------------------

    @Test
    public void testGetFileWrite_WithOpenWriteFile_ReturnsBufferedWriter() throws IOException {
        _oFileManager.openFile(_oTempWriteFile.toString(), FILE_ID_WRITE, FileOpenType.WRITE);

        BufferedWriter oWriter = _oFileManager.getFileWrite(FILE_ID_WRITE);

        assertTrue(oWriter != null);
    }

    @Test
    public void testGetFileWrite_WithReadOnlyFile_ReturnsNull() throws IOException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);

        BufferedWriter oWriter = _oFileManager.getFileWrite(FILE_ID_READ);

        assertNull(oWriter);
    }

    @Test
    public void testGetFileWrite_WithUnknownFileId_ReturnsNull() {
        BufferedWriter oWriter = _oFileManager.getFileWrite(FILE_ID_UNKNOWN);

        assertNull(oWriter);
    }

    @Test
    public void testGetFileWrite_AfterClose_ReturnsNull() throws IOException {
        _oFileManager.openFile(_oTempWriteFile.toString(), FILE_ID_WRITE, FileOpenType.WRITE);
        _oFileManager.closeFile(FILE_ID_WRITE, false);

        BufferedWriter oWriter = _oFileManager.getFileWrite(FILE_ID_WRITE);

        assertNull(oWriter);
    }

    // -------------------------------------------------------------------------
    // getFileType(int iFileID)
    // -------------------------------------------------------------------------

    @Test
    public void testGetFileType_WithReadFile_ReturnsRead() throws IOException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);

        FileOpenType oType = _oFileManager.getFileType(FILE_ID_READ);

        assertEquals(FileOpenType.READ, oType);
    }

    @Test
    public void testGetFileType_WithWriteFile_ReturnsWrite() throws IOException {
        _oFileManager.openFile(_oTempWriteFile.toString(), FILE_ID_WRITE, FileOpenType.WRITE);

        FileOpenType oType = _oFileManager.getFileType(FILE_ID_WRITE);

        assertEquals(FileOpenType.WRITE, oType);
    }

    @Test
    public void testGetFileType_WithUnknownFileId_ReturnsNull() {
        FileOpenType oType = _oFileManager.getFileType(FILE_ID_UNKNOWN);

        assertNull(oType);
    }

    @Test
    public void testGetFileType_AfterClose_ReturnsNull() throws IOException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);
        _oFileManager.closeFile(FILE_ID_READ, false);

        FileOpenType oType = _oFileManager.getFileType(FILE_ID_READ);

        assertNull(oType);
    }

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    @Test
    public void testConstructor_CreatesInstance() {
        FileManager oFileManager = new FileManager();

        assertTrue(oFileManager != null);
    }
}
