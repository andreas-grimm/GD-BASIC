package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.BooleanValue;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import java.io.File;
import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FExistsTest.java
 * <p>
 * Unit tests for the FILEEXISTS function (FExists).
 * Tests both positive cases (file exists) and negative cases (file does not exist).
 */
public class FExistsTest {

    private static final String TEST_FILE_PATH = "test_file_exists.tmp";
    private static final String NON_EXISTENT_FILE = "this_file_does_not_exist_12345.tmp";

    @BeforeAll
    public static void setUp() throws Exception {
        File testFile = new File(TEST_FILE_PATH);
        if (!testFile.exists()) {
            try (FileWriter writer = new FileWriter(testFile)) {
                writer.write("Test file for FILEEXISTS function");
            }
        }
    }

    @AfterAll
    public static void tearDown() {
        File testFile = new File(TEST_FILE_PATH);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testFileExistsPositive() {
        try {
            StringValue oValue = new StringValue(TEST_FILE_PATH);
            BooleanValue oResult = (BooleanValue) FExists.execute(oValue);

            assertTrue(oResult.toBoolean(), "File should exist");
            assertEquals(true, oResult.toBoolean());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFileExistsNegative() {
        try {
            StringValue oValue = new StringValue(NON_EXISTENT_FILE);
            BooleanValue oResult = (BooleanValue) FExists.execute(oValue);

            assertEquals(false, oResult.toBoolean(), "File should not exist");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFileExistsWithInvalidInput() {
        try {
            IntegerValue oValue = new IntegerValue(42);

            assertThrows(RuntimeException.class, () -> {
                FExists.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFileExistsWithEmptyString() {
        try {
            StringValue oValue = new StringValue("");
            BooleanValue oResult = (BooleanValue) FExists.execute(oValue);

            assertEquals(false, oResult.toBoolean(), "Empty string should return false");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
