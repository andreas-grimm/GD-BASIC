package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.memoryManager.FileOpenType;
import eu.gricom.basic.variableTypes.BooleanValue;
import eu.gricom.basic.variableTypes.Value;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FCompareTest.java
 * <p>
 * Unit tests for the FCompare BASIC function.
 * <p>
 * This test class provides comprehensive coverage of the FCompare function, which compares the
 * content of two files identified by their file IDs. The function returns true if both files have
 * identical content, line by line, and false otherwise.
 * <p>
 * Test Categories:
 * - POSITIVE TESTS: FCompare identifies identical files correctly
 * - NEGATIVE TESTS: FCompare identifies different files correctly
 * - EDGE CASES: Empty files, blank lines, special characters
 * - ERROR HANDLING: Unregistered file IDs and invalid file names
 * <p>
 * Key Behavior:
 * - Returns BooleanValue(true) only if all lines are identical
 * - Returns BooleanValue(false) if any line differs
 * - Returns BooleanValue(false) if file line counts differ
 * - Never returns null
 * - Throws RuntimeException for unregistered file IDs
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FCompareTest {

    private static final int FILE_ID_1 = 601;
    private static final int FILE_ID_2 = 602;

    private Path _oFile1;
    private Path _oFile2;
    private FileManager _oFileManager;

    /**
     * Setup method: Initializes test environment before each test.
     * Creates temporary files and FileManager instance.
     */
    @BeforeEach
    public void setUp() throws Exception {
        _oFile1 = Files.createTempFile("fcompare-file1-", ".txt");
        _oFile2 = Files.createTempFile("fcompare-file2-", ".txt");
        _oFileManager = new FileManager();
    }

    /**
     * Teardown method: Cleans up after each test.
     * Closes files and deletes temporary files.
     */
    @AfterEach
    public void tearDown() throws Exception {
        _oFileManager.closeFile(FILE_ID_1, false);
        _oFileManager.closeFile(FILE_ID_2, false);
        Files.deleteIfExists(_oFile1);
        Files.deleteIfExists(_oFile2);
    }

    // =========================================================================
    // POSITIVE TEST CASES - FCompare identifies identical files
    // =========================================================================

    /**
     * Test: FCompare with identical single-line files.
     * <p>
     * Given: Both files contain the same single line
     * When: FCompare.execute() is called
     * Then: Returns BooleanValue(true)
     * <p>
     * Purpose: Verifies basic file comparison functionality
     */
    @Test
    public void testExecute_WithIdenticalSingleLineFiles_ReturnsTrue() throws Exception {
        // Setup: Write same content to both files
        Files.write(_oFile1, "Hello World".getBytes(StandardCharsets.UTF_8));
        Files.write(_oFile2, "Hello World".getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.READ);
        _oFileManager.openFile(_oFile2.toString(), FILE_ID_2, FileOpenType.READ);

        // Execute: Compare files
        Value oResult = FCompare.execute(FILE_ID_1, FILE_ID_2);

        // Verify: Returns true
        assertNotNull(oResult);
        assertTrue(oResult instanceof BooleanValue);
        assertTrue(Boolean.parseBoolean(oResult.toString()));
    }

    /**
     * Test: FCompare with identical multi-line files.
     * <p>
     * Given: Both files contain the same multiple lines
     * When: FCompare.execute() is called
     * Then: Returns BooleanValue(true)
     * <p>
     * Purpose: Verifies multi-line comparison preserves all content
     */
    @Test
    public void testExecute_WithIdenticalMultiLineFiles_ReturnsTrue() throws Exception {
        // Setup: Write same content to both files
        String strContent = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
        Files.write(_oFile1, strContent.getBytes(StandardCharsets.UTF_8));
        Files.write(_oFile2, strContent.getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.READ);
        _oFileManager.openFile(_oFile2.toString(), FILE_ID_2, FileOpenType.READ);

        // Execute: Compare files
        Value oResult = FCompare.execute(FILE_ID_1, FILE_ID_2);

        // Verify: Returns true
        assertTrue(Boolean.parseBoolean(oResult.toString()));
    }

    /**
     * Test: FCompare with identical empty files.
     * <p>
     * Given: Both files are empty
     * When: FCompare.execute() is called
     * Then: Returns BooleanValue(true)
     * <p>
     * Purpose: Verifies empty file comparison
     */
    @Test
    public void testExecute_WithIdenticalEmptyFiles_ReturnsTrue() throws Exception {
        // Setup: Create empty files
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.READ);
        _oFileManager.openFile(_oFile2.toString(), FILE_ID_2, FileOpenType.READ);

        // Execute: Compare files
        Value oResult = FCompare.execute(FILE_ID_1, FILE_ID_2);

        // Verify: Returns true
        assertTrue(Boolean.parseBoolean(oResult.toString()));
    }

    /**
     * Test: FCompare with files containing blank lines.
     * <p>
     * Given: Both files contain identical blank lines
     * When: FCompare.execute() is called
     * Then: Returns BooleanValue(true)
     * <p>
     * Purpose: Verifies blank line handling
     */
    @Test
    public void testExecute_WithIdenticalBlankLines_ReturnsTrue() throws Exception {
        // Setup: Write content with blank lines to both files
        String strContent = "Line 1\n\nLine 3\n\n\nLine 6";
        Files.write(_oFile1, strContent.getBytes(StandardCharsets.UTF_8));
        Files.write(_oFile2, strContent.getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.READ);
        _oFileManager.openFile(_oFile2.toString(), FILE_ID_2, FileOpenType.READ);

        // Execute: Compare files
        Value oResult = FCompare.execute(FILE_ID_1, FILE_ID_2);

        // Verify: Returns true
        assertTrue(Boolean.parseBoolean(oResult.toString()));
    }

    /**
     * Test: FCompare with identical files containing special characters.
     * <p>
     * Given: Both files contain identical special characters and unicode
     * When: FCompare.execute() is called
     * Then: Returns BooleanValue(true)
     * <p>
     * Purpose: Verifies special character handling
     */
    @Test
    public void testExecute_WithIdenticalSpecialCharacters_ReturnsTrue() throws Exception {
        // Setup: Write special characters to both files
        String strContent = "Special: @#$%^&*()\nUnicode: café, naïve";
        Files.write(_oFile1, strContent.getBytes(StandardCharsets.UTF_8));
        Files.write(_oFile2, strContent.getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.READ);
        _oFileManager.openFile(_oFile2.toString(), FILE_ID_2, FileOpenType.READ);

        // Execute: Compare files
        Value oResult = FCompare.execute(FILE_ID_1, FILE_ID_2);

        // Verify: Returns true
        assertTrue(Boolean.parseBoolean(oResult.toString()));
    }

    // =========================================================================
    // NEGATIVE TEST CASES - FCompare identifies different files
    // =========================================================================

    /**
     * Test: FCompare with different single-line files.
     * <p>
     * Given: Files contain different single lines
     * When: FCompare.execute() is called
     * Then: Returns BooleanValue(false)
     * <p>
     * Purpose: Verifies detection of content differences
     */
    @Test
    public void testExecute_WithDifferentSingleLineFiles_ReturnsFalse() throws Exception {
        // Setup: Write different content to files
        Files.write(_oFile1, "Hello World".getBytes(StandardCharsets.UTF_8));
        Files.write(_oFile2, "Hello Universe".getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.READ);
        _oFileManager.openFile(_oFile2.toString(), FILE_ID_2, FileOpenType.READ);

        // Execute: Compare files
        Value oResult = FCompare.execute(FILE_ID_1, FILE_ID_2);

        // Verify: Returns false
        assertFalse(Boolean.parseBoolean(oResult.toString()));
    }

    /**
     * Test: FCompare with different line counts.
     * <p>
     * Given: File 1 has more lines than File 2
     * When: FCompare.execute() is called
     * Then: Returns BooleanValue(false)
     * <p>
     * Purpose: Verifies detection of different line counts
     */
    @Test
    public void testExecute_WithDifferentLineCount_ReturnsFalse() throws Exception {
        // Setup: Write different number of lines
        Files.write(_oFile1, "Line 1\nLine 2\nLine 3".getBytes(StandardCharsets.UTF_8));
        Files.write(_oFile2, "Line 1\nLine 2".getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.READ);
        _oFileManager.openFile(_oFile2.toString(), FILE_ID_2, FileOpenType.READ);

        // Execute: Compare files
        Value oResult = FCompare.execute(FILE_ID_1, FILE_ID_2);

        // Verify: Returns false
        assertFalse(Boolean.parseBoolean(oResult.toString()));
    }

    /**
     * Test: FCompare with one empty and one non-empty file.
     * <p>
     * Given: File 1 is empty, File 2 contains content
     * When: FCompare.execute() is called
     * Then: Returns BooleanValue(false)
     * <p>
     * Purpose: Verifies detection of empty vs non-empty file
     */
    @Test
    public void testExecute_WithEmptyVsNonEmptyFile_ReturnsFalse() throws Exception {
        // Setup: One empty file, one with content
        Files.write(_oFile2, "Some content".getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.READ);
        _oFileManager.openFile(_oFile2.toString(), FILE_ID_2, FileOpenType.READ);

        // Execute: Compare files
        Value oResult = FCompare.execute(FILE_ID_1, FILE_ID_2);

        // Verify: Returns false
        assertFalse(Boolean.parseBoolean(oResult.toString()));
    }

    /**
     * Test: FCompare with files differing on last line.
     * <p>
     * Given: Files are identical until the last line which differs
     * When: FCompare.execute() is called
     * Then: Returns BooleanValue(false)
     * <p>
     * Purpose: Verifies detection of differences at end of file
     */
    @Test
    public void testExecute_WithDifferenceOnLastLine_ReturnsFalse() throws Exception {
        // Setup: Files differ only on last line
        Files.write(_oFile1, "Line 1\nLine 2\nLine 3A".getBytes(StandardCharsets.UTF_8));
        Files.write(_oFile2, "Line 1\nLine 2\nLine 3B".getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.READ);
        _oFileManager.openFile(_oFile2.toString(), FILE_ID_2, FileOpenType.READ);

        // Execute: Compare files
        Value oResult = FCompare.execute(FILE_ID_1, FILE_ID_2);

        // Verify: Returns false
        assertFalse(Boolean.parseBoolean(oResult.toString()));
    }

    /**
     * Test: FCompare with files differing on first line.
     * <p>
     * Given: Files differ on the first line
     * When: FCompare.execute() is called
     * Then: Returns BooleanValue(false)
     * <p>
     * Purpose: Verifies early detection of differences
     */
    @Test
    public void testExecute_WithDifferenceOnFirstLine_ReturnsFalse() throws Exception {
        // Setup: Files differ on first line
        Files.write(_oFile1, "Different\nLine 2\nLine 3".getBytes(StandardCharsets.UTF_8));
        Files.write(_oFile2, "First\nLine 2\nLine 3".getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.READ);
        _oFileManager.openFile(_oFile2.toString(), FILE_ID_2, FileOpenType.READ);

        // Execute: Compare files
        Value oResult = FCompare.execute(FILE_ID_1, FILE_ID_2);

        // Verify: Returns false
        assertFalse(Boolean.parseBoolean(oResult.toString()));
    }

    /**
     * Test: FCompare with case-sensitive differences.
     * <p>
     * Given: Files differ only in case (uppercase vs lowercase)
     * When: FCompare.execute() is called
     * Then: Returns BooleanValue(false)
     * <p>
     * Purpose: Verifies case-sensitive comparison
     */
    @Test
    public void testExecute_WithCaseSensitiveDifference_ReturnsFalse() throws Exception {
        // Setup: Files differ in case
        Files.write(_oFile1, "Hello world".getBytes(StandardCharsets.UTF_8));
        Files.write(_oFile2, "Hello World".getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.READ);
        _oFileManager.openFile(_oFile2.toString(), FILE_ID_2, FileOpenType.READ);

        // Execute: Compare files
        Value oResult = FCompare.execute(FILE_ID_1, FILE_ID_2);

        // Verify: Returns false (case-sensitive)
        assertFalse(Boolean.parseBoolean(oResult.toString()));
    }

    // =========================================================================
    // ERROR HANDLING TEST CASES
    // =========================================================================

    /**
     * Test: FCompare with unregistered first file ID.
     * <p>
     * Given: First file ID is not registered in FileManager
     * When: FCompare.execute() is called
     * Then: RuntimeException is thrown
     * <p>
     * Purpose: Verifies error handling for unregistered file
     */
    @Test
    public void testExecute_WithUnregisteredFirstFileId_ThrowsRuntimeException() throws Exception {
        // Setup: Register only second file
        _oFileManager.openFile(_oFile2.toString(), FILE_ID_2, FileOpenType.READ);

        // Execute and verify: RuntimeException thrown
        assertThrows(RuntimeException.class, () -> FCompare.execute(FILE_ID_1, FILE_ID_2));
    }

    /**
     * Test: FCompare with unregistered second file ID.
     * <p>
     * Given: Second file ID is not registered in FileManager
     * When: FCompare.execute() is called
     * Then: RuntimeException is thrown
     * <p>
     * Purpose: Verifies error handling for unregistered file
     */
    @Test
    public void testExecute_WithUnregisteredSecondFileId_ThrowsRuntimeException() throws Exception {
        // Setup: Register only first file
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.READ);

        // Execute and verify: RuntimeException thrown
        assertThrows(RuntimeException.class, () -> FCompare.execute(FILE_ID_1, FILE_ID_2));
    }

    /**
     * Test: FCompare with both file IDs unregistered.
     * <p>
     * Given: Neither file ID is registered in FileManager
     * When: FCompare.execute() is called
     * Then: RuntimeException is thrown
     * <p>
     * Purpose: Verifies error handling when both files unregistered
     */
    @Test
    public void testExecute_WithBothFilesUnregistered_ThrowsRuntimeException() {
        // Execute and verify: RuntimeException thrown
        assertThrows(RuntimeException.class, () -> FCompare.execute(FILE_ID_1, FILE_ID_2));
    }

    // =========================================================================
    // TYPE AND FORMAT TEST CASES
    // =========================================================================

    /**
     * Test: FCompare always returns BooleanValue type.
     * <p>
     * Given: FCompare is called with various file contents
     * When: The return type is checked
     * Then: Always returns a BooleanValue instance
     * <p>
     * Purpose: Verifies correct return type for BASIC integration
     */
    @Test
    public void testExecute_AlwaysReturnsBooleanValueType() throws Exception {
        // Setup: Create test files
        Files.write(_oFile1, "test".getBytes(StandardCharsets.UTF_8));
        Files.write(_oFile2, "test".getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.READ);
        _oFileManager.openFile(_oFile2.toString(), FILE_ID_2, FileOpenType.READ);

        // Execute: Compare files
        Value oResult = FCompare.execute(FILE_ID_1, FILE_ID_2);

        // Verify: Returns BooleanValue
        assertTrue(oResult instanceof BooleanValue);
        assertTrue(oResult instanceof Value);
    }

    /**
     * Test: FCompare never returns null.
     * <p>
     * Given: FCompare is called with various parameters
     * When: Result is checked for null
     * Then: Result is never null
     * <p>
     * Purpose: Verifies safe usage in BASIC programs
     */
    @Test
    public void testExecute_NeverReturnsNull() throws Exception {
        // Setup: Create identical and different files
        Files.write(_oFile1, "content".getBytes(StandardCharsets.UTF_8));
        Files.write(_oFile2, "content".getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.READ);
        _oFileManager.openFile(_oFile2.toString(), FILE_ID_2, FileOpenType.READ);

        // Execute: Compare files
        Value oResult1 = FCompare.execute(FILE_ID_1, FILE_ID_2);

        // Verify: Result is non-null
        assertNotNull(oResult1);
    }

    /**
     * Test: FCompare with large files.
     * <p>
     * Given: Both files contain many lines
     * When: FCompare.execute() is called
     * Then: Returns correct result for identical large files
     * <p>
     * Purpose: Verifies performance and correctness with large files
     */
    @Test
    public void testExecute_WithLargeIdenticalFiles_ReturnsTrue() throws Exception {
        // Setup: Create large files with 1000 lines each
        StringBuilder sbContent = new StringBuilder();
        for (int i = 1; i <= 1000; i++) {
            sbContent.append("Line ").append(i).append("\n");
        }
        String strLargeContent = sbContent.toString();
        Files.write(_oFile1, strLargeContent.getBytes(StandardCharsets.UTF_8));
        Files.write(_oFile2, strLargeContent.getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.READ);
        _oFileManager.openFile(_oFile2.toString(), FILE_ID_2, FileOpenType.READ);

        // Execute: Compare files
        Value oResult = FCompare.execute(FILE_ID_1, FILE_ID_2);

        // Verify: Returns true
        assertTrue(Boolean.parseBoolean(oResult.toString()));
    }

    /**
     * Test: FCompare with whitespace-sensitive comparison.
     * <p>
     * Given: Files differ only in whitespace
     * When: FCompare.execute() is called
     * Then: Returns BooleanValue(false)
     * <p>
     * Purpose: Verifies whitespace is considered in comparison
     */
    @Test
    public void testExecute_WithWhitespaceDifference_ReturnsFalse() throws Exception {
        // Setup: Files differ in whitespace
        Files.write(_oFile1, "Hello  World".getBytes(StandardCharsets.UTF_8));
        Files.write(_oFile2, "Hello World".getBytes(StandardCharsets.UTF_8));
        _oFileManager.openFile(_oFile1.toString(), FILE_ID_1, FileOpenType.READ);
        _oFileManager.openFile(_oFile2.toString(), FILE_ID_2, FileOpenType.READ);

        // Execute: Compare files
        Value oResult = FCompare.execute(FILE_ID_1, FILE_ID_2);

        // Verify: Returns false (whitespace-sensitive)
        assertFalse(Boolean.parseBoolean(oResult.toString()));
    }
}
