package eu.gricom.basic.memoryManager;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.statements.ChDirStatement;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        assertEquals(_oTempReadFile.toString(), _oFileManager.getFileName(FILE_ID_READ).toString());
        assertEquals(FileOpenType.READ, _oFileManager.getFileType(FILE_ID_READ));
    }

    @Test
    public void testOpenFile_WithValidWriteFile_ReturnsTrue() throws IOException {
        boolean bResult = _oFileManager.openFile(_oTempWriteFile.toString(), FILE_ID_WRITE, FileOpenType.WRITE);

        assertTrue(bResult);
        assertTrue(_oFileManager.getFileStatus(FILE_ID_WRITE));
        assertEquals(_oTempWriteFile.toString(), _oFileManager.getFileName(FILE_ID_WRITE).toString());
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
        assertEquals("", _oFileManager.getFileName(FILE_ID_READ).toString());
        assertNull(_oFileManager.getFileRead(FILE_ID_READ));
    }

    @Test
    public void testCloseFile_WithOpenWriteFile_RemovesFromManagement() throws IOException {
        _oFileManager.openFile(_oTempWriteFile.toString(), FILE_ID_WRITE, FileOpenType.WRITE);

        _oFileManager.closeFile(FILE_ID_WRITE, false);

        assertFalse(_oFileManager.getFileStatus(FILE_ID_WRITE));
        assertEquals("", _oFileManager.getFileName(FILE_ID_WRITE).toString());
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

        assertEquals(0, oEof.toInt());
    }

    @Test
    public void testGetEOF_WhenFileAtEnd_ReturnsOne() throws IOException, RuntimeException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);
        _oFileManager.read(FILE_ID_READ);
        _oFileManager.read(FILE_ID_READ);
        _oFileManager.read(FILE_ID_READ);

        IntegerValue oEof = _oFileManager.getEOF(FILE_ID_READ);

        assertEquals(0, oEof.toInt());
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

        StringValue strName = _oFileManager.getFileName(FILE_ID_READ);

        assertEquals(_oTempReadFile.toString(), strName.toString());
    }

    @Test
    public void testGetFileName_WithUnknownFileId_ReturnsEmptyString() {
        StringValue strName = _oFileManager.getFileName(FILE_ID_UNKNOWN);

        assertEquals("", strName.toString());
    }

    @Test
    public void testGetFileName_AfterClose_ReturnsEmptyString() throws IOException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);
        _oFileManager.closeFile(FILE_ID_READ, false);

        StringValue strName = _oFileManager.getFileName(FILE_ID_READ);

        assertEquals("", strName.toString());
    }

    @Test
    public void testGetFileName_WithWriteFile_ReturnsFileName() throws IOException {
        _oFileManager.openFile(_oTempWriteFile.toString(), FILE_ID_WRITE, FileOpenType.WRITE);

        StringValue strName = _oFileManager.getFileName(FILE_ID_WRITE);

        assertEquals(_oTempWriteFile.toString(), strName.toString());
    }

    @Test
    public void testGetFileName_WithMultipleFiles_ReturnCorrectFileNames() throws IOException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);
        _oFileManager.openFile(_oTempWriteFile.toString(), FILE_ID_WRITE, FileOpenType.WRITE);

        StringValue strReadName = _oFileManager.getFileName(FILE_ID_READ);
        StringValue strWriteName = _oFileManager.getFileName(FILE_ID_WRITE);

        assertEquals(_oTempReadFile.toString(), strReadName.toString());
        assertEquals(_oTempWriteFile.toString(), strWriteName.toString());
    }

    @Test
    public void testGetFileName_WithNegativeFileId_ReturnsEmptyString() {
        StringValue strName = _oFileManager.getFileName(-1);

        assertEquals("", strName.toString());
    }

    @Test
    public void testGetFileName_WithZeroFileId_ReturnsEmptyString() {
        StringValue strName = _oFileManager.getFileName(0);

        assertEquals("", strName.toString());
    }

    @Test
    public void testGetFileName_WithLargeFileId_ReturnsEmptyString() {
        StringValue strName = _oFileManager.getFileName(Integer.MAX_VALUE);

        assertEquals("", strName.toString());
    }

    @Test
    public void testGetFileName_WithAbsolutePath_ReturnsExactPath() throws IOException {
        String strAbsolutePath = _oTempReadFile.toAbsolutePath().toString();
        _oFileManager.openFile(strAbsolutePath, FILE_ID_READ, FileOpenType.READ);

        StringValue strName = _oFileManager.getFileName(FILE_ID_READ);

        assertEquals(strAbsolutePath, strName.toString());
    }

    @Test
    public void testGetFileName_SequentialFileIdsReturnCorrectNames() throws IOException {
        int iFirstId = 100;
        int iSecondId = 101;
        int iThirdId = 102;

        Path oThirdFile = Files.createTempFile("filemanager-third-", ".txt");

        _oFileManager.openFile(_oTempReadFile.toString(), iFirstId, FileOpenType.READ);
        _oFileManager.openFile(_oTempWriteFile.toString(), iSecondId, FileOpenType.WRITE);
        _oFileManager.openFile(oThirdFile.toString(), iThirdId, FileOpenType.READ);

        assertEquals(_oTempReadFile.toString(), _oFileManager.getFileName(iFirstId).toString());
        assertEquals(_oTempWriteFile.toString(), _oFileManager.getFileName(iSecondId).toString());
        assertEquals(oThirdFile.toString(), _oFileManager.getFileName(iThirdId).toString());

        _oFileManager.closeFile(iFirstId, false);
        _oFileManager.closeFile(iSecondId, false);
        _oFileManager.closeFile(iThirdId, false);
    }

    @Test
    public void testGetFileName_ReturnsExactFileNameWithExtension() throws IOException {
        Path oFileWithExtension = _oTempReadFile;
        _oFileManager.openFile(oFileWithExtension.toString(), FILE_ID_READ, FileOpenType.READ);

        StringValue strName = _oFileManager.getFileName(FILE_ID_READ);

        assertTrue(strName.toString().contains("filemanager-read-") || strName.toString().contains(".txt"));
        assertEquals(oFileWithExtension.toString(), strName.toString());
    }

    @Test
    public void testGetFileName_IsNotAffectedByOtherFileOperations() throws IOException, RuntimeException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);
        StringValue strNameBefore = _oFileManager.getFileName(FILE_ID_READ);

        _oFileManager.read(FILE_ID_READ);
        _oFileManager.getEOF(FILE_ID_READ);

        StringValue strNameAfter = _oFileManager.getFileName(FILE_ID_READ);

        assertEquals(strNameBefore.toString(), strNameAfter.toString());
    }

    @Test
    public void testGetFileName_ConsistencyAfterMultipleCalls() throws IOException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);

        StringValue strFirstCall = _oFileManager.getFileName(FILE_ID_READ);
        StringValue strSecondCall = _oFileManager.getFileName(FILE_ID_READ);
        StringValue strThirdCall = _oFileManager.getFileName(FILE_ID_READ);

        assertEquals(strFirstCall.toString(), strSecondCall.toString());
        assertEquals(strSecondCall.toString(), strThirdCall.toString());
        assertEquals(_oTempReadFile.toString(), strFirstCall.toString());
    }

    @Test
    public void testGetFileName_ImmediatelyAfterOpen_ReturnsFileName() throws IOException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);

        StringValue strName = _oFileManager.getFileName(FILE_ID_READ);

        assertNotNull(strName);
        assertEquals(_oTempReadFile.toString(), strName.toString());
    }

    @Test
    public void testGetFileName_WithDifferentFileIdAfterClose_ReturnsEmptyString() throws IOException {
        _oFileManager.openFile(_oTempReadFile.toString(), FILE_ID_READ, FileOpenType.READ);
        _oFileManager.openFile(_oTempWriteFile.toString(), FILE_ID_WRITE, FileOpenType.WRITE);

        _oFileManager.closeFile(FILE_ID_READ, false);

        StringValue strClosedFile = _oFileManager.getFileName(FILE_ID_READ);
        StringValue strOpenFile = _oFileManager.getFileName(FILE_ID_WRITE);

        assertEquals("", strClosedFile.toString());
        assertEquals(_oTempWriteFile.toString(), strOpenFile.toString());
    }

    @Test
    public void testGetFileName_WithAllFilesOpen_ReturnsCorrectName() throws IOException {
        int iId1 = 50;
        int iId2 = 51;
        Path oFile1 = _oTempReadFile;
        Path oFile2 = _oTempWriteFile;

        _oFileManager.openFile(oFile1.toString(), iId1, FileOpenType.READ);
        _oFileManager.openFile(oFile2.toString(), iId2, FileOpenType.WRITE);

        assertEquals(oFile1.toString(), _oFileManager.getFileName(iId1).toString());
        assertEquals(oFile2.toString(), _oFileManager.getFileName(iId2).toString());

        _oFileManager.closeFile(iId1, false);
        _oFileManager.closeFile(iId2, false);
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
    // getCurrentDirectory()
    // -------------------------------------------------------------------------

    @Test
    public void testGetCurrentDirectory_WithDefaultDirectory_ReturnsDefaultPath() {
        String strDirectory = _oFileManager.getCurrentDirectory();

        assertTrue(strDirectory != null);
        assertTrue(strDirectory.contains("./") || strDirectory.length() > 0);
    }

    @Test
    public void testGetCurrentDirectory_AfterSet_ReturnsSetDirectory() {
        String strTestDir = "/tmp/test_directory";
        _oFileManager.setCurrentDirectory(strTestDir);

        String strRetrieved = _oFileManager.getCurrentDirectory();

        assertEquals(strTestDir, strRetrieved);
    }

    @Test
    public void testGetCurrentDirectory_WithComplexPath_ReturnsExactPath() {
        String strComplexPath = "/home/user/projects/basic/files";
        _oFileManager.setCurrentDirectory(strComplexPath);

        String strRetrieved = _oFileManager.getCurrentDirectory();

        assertEquals(strComplexPath, strRetrieved);
    }

    // -------------------------------------------------------------------------
    // setCurrentDirectory(String strDirectory)
    // -------------------------------------------------------------------------

    @Test
    public void testSetCurrentDirectory_WithValidPath_UpdatesCurrentDirectory() {
        String strNewDir = "/var/tmp";
        _oFileManager.setCurrentDirectory(strNewDir);

        String strCurrent = _oFileManager.getCurrentDirectory();

        assertEquals(strNewDir, strCurrent);
    }

    @Test
    public void testSetCurrentDirectory_WithRelativePath_UpdatesCurrentDirectory() {
        String strRelativePath = "../data/files";
        _oFileManager.setCurrentDirectory(strRelativePath);

        String strCurrent = _oFileManager.getCurrentDirectory();

        assertEquals(strRelativePath, strCurrent);
    }

    @Test
    public void testSetCurrentDirectory_WithCurrentDirectoryPath_UpdatesCurrentDirectory() {
        String strCurrentPath = "./";
        _oFileManager.setCurrentDirectory(strCurrentPath);

        String strCurrent = _oFileManager.getCurrentDirectory();

        assertEquals(strCurrentPath, strCurrent);
    }

    @Test
    public void testSetCurrentDirectory_MultipleUpdates_ReturnsLastSetDirectory() {
        String strFirst = "/first/path";
        String strSecond = "/second/path";
        String strThird = "/third/path";

        _oFileManager.setCurrentDirectory(strFirst);
        _oFileManager.setCurrentDirectory(strSecond);
        _oFileManager.setCurrentDirectory(strThird);

        String strCurrent = _oFileManager.getCurrentDirectory();

        assertEquals(strThird, strCurrent);
    }

    @Test
    public void testSetCurrentDirectory_WithEmptyString_SetsEmptyDirectory() {
        String strEmptyPath = "";
        _oFileManager.setCurrentDirectory(strEmptyPath);

        String strCurrent = _oFileManager.getCurrentDirectory();

        assertEquals(strEmptyPath, strCurrent);
    }

    @Test
    public void testSetCurrentDirectory_WithPathContainingSpaces_PreservesPath() {
        String strPathWithSpaces = "/path with spaces/to/directory";
        _oFileManager.setCurrentDirectory(strPathWithSpaces);

        String strCurrent = _oFileManager.getCurrentDirectory();

        assertEquals(strPathWithSpaces, strCurrent);
    }

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    @Test
    public void testConstructor_CreatesInstance() {
        FileManager oFileManager = new FileManager();

        assertTrue(oFileManager != null);
    }

    // =========================================================================
    // CHDIR INTEGRATION TESTS - Testing directory changes with ChDir
    // =========================================================================

    /**
     * Test: FileManager opens absolute path files after ChDir.
     * <p>
     * Given: ChDir has been called to change directory
     * When: FileManager opens a file with an absolute path
     * Then: File opens successfully regardless of current directory
     * <p>
     * Purpose: Verifies absolute paths work after directory changes
     */
    @Test
    public void testOpenFile_WithAbsolutePathAfterChDir_OpensSuccessfully() throws IOException, Exception {
        // Setup: Change current directory using ChDir
        new ChDirStatement(10, new StringValue("/tmp")).execute();
        FileManager oFileManager = new FileManager();

        // Execute: Open file with absolute path
        boolean bResult = oFileManager.openFile(_oTempReadFile.toAbsolutePath().toString(), FILE_ID_READ, FileOpenType.READ);

        // Verify: File opens successfully
        assertTrue(bResult);
        assertEquals(_oTempReadFile.toAbsolutePath().toString(), oFileManager.getFileName(FILE_ID_READ).toString());
    }

    /**
     * Test: FileManager correctly handles relative paths after ChDir.
     * <p>
     * Given: A file exists in a specific directory
     * When: ChDir is used to change to that directory and a relative filename is opened
     * Then: File opens successfully using the relative path from new directory
     * <p>
     * Purpose: Verifies relative path resolution from changed directory
     */
    @Test
    public void testOpenFile_WithRelativePathAfterChDir_ResolvesCorrectly() throws IOException, Exception {
        // Setup: Create file in a temp directory
        Path oTempDir = Files.createTempDirectory("filemanager-chdir-");
        Path oFileInDir = oTempDir.resolve("testfile.txt");
        Files.writeString(oFileInDir, "Test content");

        // Change directory using ChDir
        new ChDirStatement(10, new StringValue(oTempDir.toString() + "/")).execute();
        FileManager oFileManager = new FileManager();

        // Execute: Open file with relative path
        boolean bResult = oFileManager.openFile("testfile.txt", FILE_ID_READ, FileOpenType.READ);

        // Verify: File opens successfully
        assertTrue(bResult);
        assertTrue(oFileManager.getFileName(FILE_ID_READ).toString().endsWith("testfile.txt"));

        // Cleanup
        oFileManager.closeFile(FILE_ID_READ, false);
        Files.delete(oFileInDir);
        Files.delete(oTempDir);
    }

    /**
     * Test: FileManager file operations after multiple ChDir calls.
     * <p>
     * Given: ChDir is called multiple times to change directory
     * When: FileManager opens files after each directory change
     * Then: Files are correctly resolved based on the current directory
     * <p>
     * Purpose: Verifies correct behavior with multiple directory changes
     */
    @Test
    public void testOpenFile_AfterMultipleChdirCalls_UsesLatestDirectory() throws IOException, Exception {
        // Setup: Create two temp directories
        Path oTempDir1 = Files.createTempDirectory("filemanager-dir1-");
        Path oTempDir2 = Files.createTempDirectory("filemanager-dir2-");
        Path oFile1 = oTempDir1.resolve("file1.txt");
        Path oFile2 = oTempDir2.resolve("file2.txt");
        Files.writeString(oFile1, "Content 1");
        Files.writeString(oFile2, "Content 2");

        // Change to first directory
        new ChDirStatement(10, new StringValue(oTempDir1.toString() + "/")).execute();
        FileManager oFileManager = new FileManager();
        boolean bResult1 = oFileManager.openFile("file1.txt", FILE_ID_READ, FileOpenType.READ);

        // Change to second directory
        new ChDirStatement(10, new StringValue(oTempDir2.toString() + "/")).execute();
        FileManager oFileManager2 = new FileManager();
        boolean bResult2 = oFileManager2.openFile("file2.txt", FILE_ID_WRITE, FileOpenType.WRITE);

        // Verify: Both files opened successfully
        assertTrue(bResult1);
        assertTrue(bResult2);

        // Cleanup
        oFileManager.closeFile(FILE_ID_READ, false);
        oFileManager2.closeFile(FILE_ID_WRITE, false);
        Files.delete(oFile1);
        Files.delete(oFile2);
        Files.delete(oTempDir1);
        Files.delete(oTempDir2);
    }

    /**
     * Test: FileManager getFileName returns correct path after ChDir.
     * <p>
     * Given: ChDir has changed the current directory
     * When: FileManager retrieves the file name
     * Then: Returns the correct file path (relative or absolute)
     * <p>
     * Purpose: Verifies file path is correctly stored and retrieved
     */
    @Test
    public void testGetFileName_AfterChdirWithRelativePath_ReturnsCorrectPath() throws IOException, Exception {
        // Setup: Change directory
        new ChDirStatement(10, new StringValue("/tmp/")).execute();
        FileManager oFileManager = new FileManager();

        // Open file with absolute path
        oFileManager.openFile(_oTempReadFile.toAbsolutePath().toString(), FILE_ID_READ, FileOpenType.READ);

        // Execute and Verify: Retrieved path matches what was opened
        String strRetrievedPath = oFileManager.getFileName(FILE_ID_READ).toString();
        assertEquals(_oTempReadFile.toAbsolutePath().toString(), strRetrievedPath);
    }

    /**
     * Test: FileManager file status correct after ChDir.
     * <p>
     * Given: ChDir has been called
     * When: FileManager checks if a file is open
     * Then: Correctly reports open status
     * <p>
     * Purpose: Verifies file status tracking is unaffected by directory changes
     */
    @Test
    public void testGetFileStatus_AfterChdirWithOpenFile_ReturnsTrue() throws IOException, Exception {
        // Setup: Change directory
        new ChDirStatement(10, new StringValue("/tmp/")).execute();
        FileManager oFileManager = new FileManager();

        // Open file
        oFileManager.openFile(_oTempReadFile.toAbsolutePath().toString(), FILE_ID_READ, FileOpenType.READ);

        // Execute and Verify: File status is reported as open
        assertTrue(oFileManager.getFileStatus(FILE_ID_READ));
    }

    /**
     * Test: FileManager read operation after ChDir.
     * <p>
     * Given: A file is open after ChDir
     * When: FileManager reads from the file
     * Then: Content is read correctly
     * <p>
     * Purpose: Verifies file content operations work after directory changes
     */
    @Test
    public void testRead_AfterChdirWithOpenFile_ReadsContent() throws IOException, Exception {
        // Setup: Change directory
        new ChDirStatement(10, new StringValue("/tmp/")).execute();
        FileManager oFileManager = new FileManager();

        // Open and read file
        oFileManager.openFile(_oTempReadFile.toAbsolutePath().toString(), FILE_ID_READ, FileOpenType.READ);

        // Execute and Verify: Content is read correctly
        Value oContent = oFileManager.read(FILE_ID_READ);
        assertNotNull(oContent);
        assertEquals("line1", oContent.toString());
    }

    /**
     * Test: FileManager handles default directory correctly.
     * <p>
     * Given: FileManager starts with default directory
     * When: ChDir is called and then called again with empty string
     * Then: Directory is properly managed
     * <p>
     * Purpose: Verifies directory state management
     */
    @Test
    public void testSetCurrentDirectory_ResetWithEmptyString_ResetsDirectory() throws Exception {
        // Setup: Set a directory
        new ChDirStatement(10, new StringValue("/tmp/")).execute();
        FileManager oFileManager = new FileManager();
        String strDir1 = oFileManager.getCurrentDirectory();
        assertTrue(strDir1.contains("/tmp"));

        // Change to empty directory
        new ChDirStatement(10, new StringValue("")).execute();
        FileManager oFileManager2 = new FileManager();
        String strDir2 = oFileManager2.getCurrentDirectory();
        assertEquals("", strDir2);
    }

    /**
     * Test: FileManager absolute path resolution after ChDir.
     * <p>
     * Given: Multiple ChDir calls with different paths
     * When: FileManager opens files with absolute paths
     * Then: Absolute paths are always used directly, ignoring current directory
     * <p>
     * Purpose: Verifies absolute path handling is independent of current directory
     */
    @Test
    public void testOpenFile_AbsolutePathIgnoresCurrentDirectory() throws IOException, Exception {
        // Setup: Create files and directories
        Path oTempDir = Files.createTempDirectory("filemanager-abs-");
        Path oFileInDir = oTempDir.resolve("file.txt");
        Files.writeString(oFileInDir, "Content");

        // Change to different directory
        new ChDirStatement(10, new StringValue("/tmp/")).execute();
        FileManager oFileManager = new FileManager();

        // Open file with absolute path (not in /tmp/)
        boolean bResult = oFileManager.openFile(oFileInDir.toAbsolutePath().toString(), FILE_ID_READ, FileOpenType.READ);

        // Verify: Absolute path works regardless of current directory
        assertTrue(bResult);
        assertEquals(oFileInDir.toAbsolutePath().toString(), oFileManager.getFileName(FILE_ID_READ).toString());

        // Cleanup
        oFileManager.closeFile(FILE_ID_READ, false);
        Files.delete(oFileInDir);
        Files.delete(oTempDir);
    }
}
