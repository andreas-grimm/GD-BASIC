package eu.gricom.basic.statements;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * ChDirStatementTest.java
 * <p>
 * Unit tests for the CHDIR statement (ChDirStatement).
 * Tests cover changing the current working directory with positive cases (valid paths)
 * and negative cases (invalid input types).
 */
public class ChDirStatementTest {

    /**
     * Positive test: Change directory to an absolute path.
     * Verifies that the FileManager's current directory is updated correctly.
     */
    @Test
    public void testChDir_WithAbsolutePath_UpdatesCurrentDirectory() throws Exception {
        String strTestPath = "/tmp/test_directory";
        StringValue oValue = new StringValue(strTestPath);
        ChDirStatement oStatement = new ChDirStatement(10, oValue);

        oStatement.execute();

        FileManager oFileManager = new FileManager();
        assertEquals(strTestPath, oFileManager.getCurrentDirectory());
    }

    /**
     * Positive test: Change directory to a relative path.
     * Verifies that relative paths like "../" are accepted and stored.
     */
    @Test
    public void testChDir_WithRelativePath_UpdatesCurrentDirectory() throws Exception {
        String strRelativePath = "../files/data";
        StringValue oValue = new StringValue(strRelativePath);
        ChDirStatement oStatement = new ChDirStatement(20, oValue);

        oStatement.execute();

        FileManager oFileManager = new FileManager();
        assertEquals(strRelativePath, oFileManager.getCurrentDirectory());
    }

    /**
     * Positive test: Change directory to current directory path.
     * Verifies that "./" is accepted as a valid directory path.
     */
    @Test
    public void testChDir_WithCurrentDirectoryPath_UpdatesCurrentDirectory() throws Exception {
        String strCurrentPath = "./";
        StringValue oValue = new StringValue(strCurrentPath);
        ChDirStatement oStatement = new ChDirStatement(30, oValue);

        oStatement.execute();

        FileManager oFileManager = new FileManager();
        assertEquals(strCurrentPath, oFileManager.getCurrentDirectory());
    }

    /**
     * Positive test: Change directory to home directory path.
     * Verifies that home directory shortcuts like "~" are accepted.
     */
    @Test
    public void testChDir_WithHomeDirectoryPath_UpdatesCurrentDirectory() throws Exception {
        String strHomePath = "~/documents";
        StringValue oValue = new StringValue(strHomePath);
        ChDirStatement oStatement = new ChDirStatement(40, oValue);

        oStatement.execute();

        FileManager oFileManager = new FileManager();
        assertEquals(strHomePath, oFileManager.getCurrentDirectory());
    }

    /**
     * Positive test: Change directory multiple times sequentially.
     * Verifies that the last directory change is retained.
     */
    @Test
    public void testChDir_WithMultipleChanges_UpdatesToLastDirectory() throws Exception {
        String strFirst = "/first/path";
        String strSecond = "/second/path";
        String strThird = "/third/path";

        ChDirStatement oStatement1 = new ChDirStatement(10, new StringValue(strFirst));
        ChDirStatement oStatement2 = new ChDirStatement(20, new StringValue(strSecond));
        ChDirStatement oStatement3 = new ChDirStatement(30, new StringValue(strThird));

        oStatement1.execute();
        oStatement2.execute();
        oStatement3.execute();

        FileManager oFileManager = new FileManager();
        assertEquals(strThird, oFileManager.getCurrentDirectory());
    }

    /**
     * Positive test: Change directory with path containing spaces.
     * Verifies that paths with spaces are preserved correctly.
     */
    @Test
    public void testChDir_WithPathContainingSpaces_UpdatesCurrentDirectory() throws Exception {
        String strPathWithSpaces = "/path with spaces/to/directory";
        StringValue oValue = new StringValue(strPathWithSpaces);
        ChDirStatement oStatement = new ChDirStatement(50, oValue);

        oStatement.execute();

        FileManager oFileManager = new FileManager();
        assertEquals(strPathWithSpaces, oFileManager.getCurrentDirectory());
    }

    /**
     * Positive test: Change directory with empty string.
     * Verifies that empty string paths are accepted (may represent current directory).
     */
    @Test
    public void testChDir_WithEmptyString_UpdatesCurrentDirectory() throws Exception {
        String strEmptyPath = "";
        StringValue oValue = new StringValue(strEmptyPath);
        ChDirStatement oStatement = new ChDirStatement(60, oValue);

        oStatement.execute();

        FileManager oFileManager = new FileManager();
        assertEquals(strEmptyPath, oFileManager.getCurrentDirectory());
    }

    /**
     * Negative test: Execute with IntegerValue instead of StringValue.
     * Verifies that non-string input throws RuntimeException.
     */
    @Test
    public void testChDir_WithIntegerValue_ThrowsRuntimeException() {
        IntegerValue oValue = new IntegerValue(42);
        ChDirStatement oStatement = new ChDirStatement(70, oValue);

        assertThrows(RuntimeException.class, oStatement::execute);
    }

    /**
     * Negative test: Execute with null value.
     * Verifies that null input throws RuntimeException.
     */
    @Test
    public void testChDir_WithNullValue_ThrowsRuntimeException() {
        ChDirStatement oStatement = new ChDirStatement(80, null);

        assertThrows(RuntimeException.class, oStatement::execute);
    }

    /**
     * Edge case test: Change directory with Windows-style path.
     * Verifies that Windows path separators are preserved (not validated).
     */
    @Test
    public void testChDir_WithWindowsStylePath_UpdatesCurrentDirectory() throws Exception {
        String strWindowsPath = "C:\\Users\\Documents";
        StringValue oValue = new StringValue(strWindowsPath);
        ChDirStatement oStatement = new ChDirStatement(90, oValue);

        oStatement.execute();

        FileManager oFileManager = new FileManager();
        assertEquals(strWindowsPath, oFileManager.getCurrentDirectory());
    }

    /**
     * Edge case test: Change directory with long path.
     * Verifies that long paths are correctly stored without truncation.
     */
    @Test
    public void testChDir_WithLongPath_UpdatesCurrentDirectory() throws Exception {
        String strLongPath = "/very/long/path/to/some/deeply/nested/directory/structure/for/testing";
        StringValue oValue = new StringValue(strLongPath);
        ChDirStatement oStatement = new ChDirStatement(100, oValue);

        oStatement.execute();

        FileManager oFileManager = new FileManager();
        assertEquals(strLongPath, oFileManager.getCurrentDirectory());
    }

    /**
     * Test: Verify getTokenNumber returns the correct line number.
     */
    @Test
    public void testGetTokenNumber() {
        StringValue oValue = new StringValue("/some/path");
        ChDirStatement oStatement = new ChDirStatement(110, oValue);

        assertEquals(110, oStatement.getTokenNumber());
    }

    /**
     * Test: Verify content method returns "CHDIR".
     */
    @Test
    public void testContent() throws Exception {
        StringValue oValue = new StringValue("/some/path");
        ChDirStatement oStatement = new ChDirStatement(120, oValue);

        assertEquals("CHDIR", oStatement.content());
    }

    /**
     * Test: Verify structure method returns properly formatted JSON.
     */
    @Test
    public void testStructure() throws Exception {
        StringValue oValue = new StringValue("/test/path");
        ChDirStatement oStatement = new ChDirStatement(130, oValue);

        String strStructure = oStatement.structure();
        assertEquals(true, strStructure.contains("\"CHDIR\""));
        assertEquals(true, strStructure.contains("\"TOKEN_NR\": \"130\""));
        assertEquals(true, strStructure.contains("/test/path"));
    }
}
