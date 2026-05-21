package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * ChDirTest.java
 * <p>
 * Unit tests for the CHDIR function (ChDir).
 * Tests cover changing the current working directory with positive cases (valid paths)
 * and negative cases (invalid input types).
 */
public class ChDirTest {

    /**
     * Positive test: Change directory to an absolute path.
     * Verifies that the FileManager's current directory is updated correctly.
     */
    @Test
    public void testChDir_WithAbsolutePath_UpdatesCurrentDirectory() {
        try {
            String strTestPath = "/tmp/test_directory";
            StringValue oValue = new StringValue(strTestPath);

            ChDir.execute(oValue);

            FileManager oFileManager = new FileManager();
            assertEquals(strTestPath, oFileManager.getCurrentDirectory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Positive test: Change directory to a relative path.
     * Verifies that relative paths like "../" are accepted and stored.
     */
    @Test
    public void testChDir_WithRelativePath_UpdatesCurrentDirectory() {
        try {
            String strRelativePath = "../files/data";
            StringValue oValue = new StringValue(strRelativePath);

            ChDir.execute(oValue);

            FileManager oFileManager = new FileManager();
            assertEquals(strRelativePath, oFileManager.getCurrentDirectory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Positive test: Change directory to current directory path.
     * Verifies that "./" is accepted as a valid directory path.
     */
    @Test
    public void testChDir_WithCurrentDirectoryPath_UpdatesCurrentDirectory() {
        try {
            String strCurrentPath = "./";
            StringValue oValue = new StringValue(strCurrentPath);

            ChDir.execute(oValue);

            FileManager oFileManager = new FileManager();
            assertEquals(strCurrentPath, oFileManager.getCurrentDirectory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Positive test: Change directory to home directory path.
     * Verifies that home directory shortcuts like "~" are accepted.
     */
    @Test
    public void testChDir_WithHomeDirectoryPath_UpdatesCurrentDirectory() {
        try {
            String strHomePath = "~/documents";
            StringValue oValue = new StringValue(strHomePath);

            ChDir.execute(oValue);

            FileManager oFileManager = new FileManager();
            assertEquals(strHomePath, oFileManager.getCurrentDirectory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Positive test: Change directory multiple times sequentially.
     * Verifies that the last directory change is retained.
     */
    @Test
    public void testChDir_WithMultipleChanges_UpdatesToLastDirectory() {
        try {
            String strFirst = "/first/path";
            String strSecond = "/second/path";
            String strThird = "/third/path";

            ChDir.execute(new StringValue(strFirst));
            ChDir.execute(new StringValue(strSecond));
            ChDir.execute(new StringValue(strThird));

            FileManager oFileManager = new FileManager();
            assertEquals(strThird, oFileManager.getCurrentDirectory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Positive test: Change directory with path containing spaces.
     * Verifies that paths with spaces are preserved correctly.
     */
    @Test
    public void testChDir_WithPathContainingSpaces_UpdatesCurrentDirectory() {
        try {
            String strPathWithSpaces = "/path with spaces/to/directory";
            StringValue oValue = new StringValue(strPathWithSpaces);

            ChDir.execute(oValue);

            FileManager oFileManager = new FileManager();
            assertEquals(strPathWithSpaces, oFileManager.getCurrentDirectory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Positive test: Change directory with empty string.
     * Verifies that empty string paths are accepted (may represent current directory).
     */
    @Test
    public void testChDir_WithEmptyString_UpdatesCurrentDirectory() {
        try {
            String strEmptyPath = "";
            StringValue oValue = new StringValue(strEmptyPath);

            ChDir.execute(oValue);

            FileManager oFileManager = new FileManager();
            assertEquals(strEmptyPath, oFileManager.getCurrentDirectory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Negative test: Execute with IntegerValue instead of StringValue.
     * Verifies that non-string input throws RuntimeException.
     */
    @Test
    public void testChDir_WithIntegerValue_ThrowsRuntimeException() {
        try {
            IntegerValue oValue = new IntegerValue(42);

            assertThrows(RuntimeException.class, () -> {
                ChDir.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Negative test: Execute with null value.
     * Verifies that null input throws RuntimeException.
     */
    @Test
    public void testChDir_WithNullValue_ThrowsRuntimeException() {
        try {
            assertThrows(RuntimeException.class, () -> {
                ChDir.execute(null);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Edge case test: Change directory with Windows-style path.
     * Verifies that Windows path separators are preserved (not validated).
     */
    @Test
    public void testChDir_WithWindowsStylePath_UpdatesCurrentDirectory() {
        try {
            String strWindowsPath = "C:\\Users\\Documents";
            StringValue oValue = new StringValue(strWindowsPath);

            ChDir.execute(oValue);

            FileManager oFileManager = new FileManager();
            assertEquals(strWindowsPath, oFileManager.getCurrentDirectory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Edge case test: Change directory with long path.
     * Verifies that long paths are correctly stored without truncation.
     */
    @Test
    public void testChDir_WithLongPath_UpdatesCurrentDirectory() {
        try {
            String strLongPath = "/very/long/path/to/some/deeply/nested/directory/structure/for/testing";
            StringValue oValue = new StringValue(strLongPath);

            ChDir.execute(oValue);

            FileManager oFileManager = new FileManager();
            assertEquals(strLongPath, oFileManager.getCurrentDirectory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
