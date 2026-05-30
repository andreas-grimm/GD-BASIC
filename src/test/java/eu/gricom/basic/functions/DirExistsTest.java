package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.BooleanValue;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DirExistsTest.java
 * <p>
 * Unit tests for the DIREXISTS function (DirExists).
 * Tests both positive cases (directory exists) and negative cases (directory does not exist).
 */
public class DirExistsTest {

    private static final String TEST_DIR_PATH = "test_dir_exists_tmp";
    private static final String TEST_FILE_PATH = "test_file_for_dir_check.tmp";
    private static final String NON_EXISTENT_DIR = "this_directory_does_not_exist_12345";

    @BeforeAll
    public static void setUp() throws Exception {
        File testDir = new File(TEST_DIR_PATH);
        if (!testDir.exists()) {
            testDir.mkdir();
        }

        File testFile = new File(TEST_FILE_PATH);
        if (!testFile.exists()) {
            try (FileWriter writer = new FileWriter(testFile)) {
                writer.write("Test file for DIREXISTS function");
            }
        }
    }

    @AfterAll
    public static void tearDown() {
        File testFile = new File(TEST_FILE_PATH);
        if (testFile.exists()) {
            testFile.delete();
        }

        File testDir = new File(TEST_DIR_PATH);
        if (testDir.exists()) {
            testDir.delete();
        }
    }

    @Test
    public void testDirExistsPositive() {
        try {
            StringValue oValue = new StringValue(TEST_DIR_PATH);
            BooleanValue oResult = (BooleanValue) DirExists.execute(oValue);

            assertTrue(oResult.toBoolean(), "Directory should exist");
            assertEquals(true, oResult.toBoolean());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDirExistsNegative() {
        try {
            StringValue oValue = new StringValue(NON_EXISTENT_DIR);
            BooleanValue oResult = (BooleanValue) DirExists.execute(oValue);

            assertEquals(false, oResult.toBoolean(), "Directory should not exist");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDirExistsWithFile() {
        try {
            StringValue oValue = new StringValue(TEST_FILE_PATH);
            BooleanValue oResult = (BooleanValue) DirExists.execute(oValue);

            assertEquals(false, oResult.toBoolean(), "File is not a directory");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDirExistsWithInvalidInput() {
        try {
            IntegerValue oValue = new IntegerValue(42);

            assertThrows(RuntimeException.class, () -> {
                DirExists.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDirExistsWithEmptyString() {
        try {
            StringValue oValue = new StringValue("");
            BooleanValue oResult = (BooleanValue) DirExists.execute(oValue);

            assertEquals(false, oResult.toBoolean(), "Empty string should return false");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
