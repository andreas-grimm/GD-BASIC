package eu.gricom.basic.statements;

import static org.junit.jupiter.api.Assertions.assertTrue;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.memorymanager.FileManager;
import eu.gricom.basic.variabletypes.IntegerValue;
import eu.gricom.basic.variabletypes.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

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

    /** Every test starts in the directory in which the program (the JVM) was started. */
    @BeforeEach
    public void startInProgramDirectory() {
        new FileManager().setCurrentDirectory(System.getProperty("user.dir"));
    }

    /** The absolute path that CHDIR must store: the text is resolved against the start directory and normalised. */
    private static String absoluteFrom(final String strPath) {
        return Paths.get(System.getProperty("user.dir")).resolve(strPath).toAbsolutePath().normalize().toString();
    }

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
        assertEquals(absoluteFrom(strTestPath), oFileManager.getCurrentDirectory());
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
        assertEquals(absoluteFrom(strRelativePath), oFileManager.getCurrentDirectory());
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
        assertEquals(absoluteFrom(strCurrentPath), oFileManager.getCurrentDirectory());
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
        assertEquals(absoluteFrom(strHomePath), oFileManager.getCurrentDirectory());
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
        assertEquals(absoluteFrom(strThird), oFileManager.getCurrentDirectory());
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
        assertEquals(absoluteFrom(strPathWithSpaces), oFileManager.getCurrentDirectory());
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
        assertEquals(absoluteFrom(strEmptyPath), oFileManager.getCurrentDirectory());
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
        assertEquals(absoluteFrom(strWindowsPath), oFileManager.getCurrentDirectory());
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
        assertEquals(absoluteFrom(strLongPath), oFileManager.getCurrentDirectory());
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

    /** Relative names are taken from the current directory, and ".." goes one level up. */
    @Test
    public void testChDir_RelativeNamesChainFromCurrentDirectory() throws Exception {
        String strStart = System.getProperty("user.dir");
        new ChDirStatement(10, new StringValue("alpha")).execute();
        new ChDirStatement(20, new StringValue("beta")).execute();
        FileManager oFileManager = new FileManager();
        assertEquals(Paths.get(strStart, "alpha", "beta").toString(), oFileManager.getCurrentDirectory());

        new ChDirStatement(30, new StringValue("..")).execute();
        assertEquals(Paths.get(strStart, "alpha").toString(), oFileManager.getCurrentDirectory());
    }

    /** An empty name means "stay here", so the stored directory does not change. */
    @Test
    public void testChDir_EmptyNameStaysInCurrentDirectory() throws Exception {
        new ChDirStatement(10, new StringValue("alpha")).execute();
        String strBefore = new FileManager().getCurrentDirectory();
        new ChDirStatement(20, new StringValue("")).execute();
        assertEquals(strBefore, new FileManager().getCurrentDirectory());
    }

    /** The tilde is not expanded: "~" is an ordinary directory name in BASIC. */
    @Test
    public void testChDir_TildeIsNotExpanded() throws Exception {
        new ChDirStatement(10, new StringValue("~")).execute();
        assertTrue(new FileManager().getCurrentDirectory().endsWith(Paths.get("~").toString()));
    }
}
