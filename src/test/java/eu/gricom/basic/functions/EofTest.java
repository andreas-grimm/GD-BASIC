package eu.gricom.basic.functions;

import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.memoryManager.VariableManagement;
import eu.gricom.basic.statements.FInputStatement;
import eu.gricom.basic.statements.FOpenStatement;
import eu.gricom.basic.statements.FPrintStatement;
import eu.gricom.basic.variableTypes.BooleanValue;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the Eof function class.
 * Tests are based on the interface specification of the Eof class.
 * Uses FOpenStatement to create files, FPrintStatement to write multiple lines,
 * and a loop with FInputStatement and Eof to read until file is completely read.
 */
public class EofTest {

    private static final int FILE_ID_1 = 401;
    private static final int FILE_ID_2 = 402;

    private Path _oTempFile1;
    private Path _oTempFile2;

    @BeforeEach
    public void setUp() throws Exception {
        _oTempFile1 = Files.createTempFile("eof-test-1-", ".txt");
        _oTempFile2 = Files.createTempFile("eof-test-2-", ".txt");
    }

    @AfterEach
    public void tearDown() throws Exception {
        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);
        oFileManager.closeFile(FILE_ID_2, false);
        Files.deleteIfExists(_oTempFile1);
        Files.deleteIfExists(_oTempFile2);
    }

    // -------------------------------------------------------------------------
    // execute(Value oValue) - interface specification
    // -------------------------------------------------------------------------

    @Test
    public void testExecute_WithIntegerValue_ReturnsBooleanValue() throws Exception {
        IntegerValue oFileId = new IntegerValue(FILE_ID_1);
        Value oResult = Eof.execute(oFileId);

        assertTrue(oResult instanceof BooleanValue);
    }

    @Test
    public void testExecute_WithNonOpenFile_ReturnsFalse() throws Exception {
        IntegerValue oFileId = new IntegerValue(FILE_ID_1);
        BooleanValue oResult = (BooleanValue) Eof.execute(oFileId);

        assertFalse(oResult.toBoolean());
    }

    @Test
    public void testExecute_WithNonIntegerValue_ThrowsRuntimeException() {
        StringValue oValue = new StringValue("1");

        assertThrows(Exception.class, () -> Eof.execute(oValue));
    }

    // -------------------------------------------------------------------------
    // execute() with FOpen + FPrint + loop(FInput + Eof) - read until EOF
    // -------------------------------------------------------------------------

    @Test
    public void testExecute_WithMultipleLines_ReadsAllLinesUntilEof() throws Exception {
        List<String> astrExpectedLines = List.of("Line1", "Line2", "Line3");

        FOpenStatement oFOpenWrite = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        oFOpenWrite.execute();

        FPrintStatement oFPrint1 = new FPrintStatement(2, FILE_ID_1, List.of(new StringValue("Line1\n")), false);
        FPrintStatement oFPrint2 = new FPrintStatement(3, FILE_ID_1, List.of(new StringValue("Line2\n")), false);
        FPrintStatement oFPrint3 = new FPrintStatement(4, FILE_ID_1, List.of(new StringValue("Line3")), false);

        oFPrint1.execute();
        oFPrint2.execute();
        oFPrint3.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        FOpenStatement oFOpenRead = new FOpenStatement(5, FILE_ID_1, _oTempFile1.toString(), "read");
        oFOpenRead.execute();

        List<String> astrReadLines = new ArrayList<>();
        FInputStatement oFInput = new FInputStatement(6, FILE_ID_1, "A$");

        while (!((BooleanValue) Eof.execute(new IntegerValue(FILE_ID_1))).toBoolean()) {
            try {
                oFInput.execute();
                VariableManagement oVariableManagement = new VariableManagement();
                Value oValue = oVariableManagement.getMap("A$");
                if (oValue != null && !oValue.toString().isEmpty()) {
                    astrReadLines.add(oValue.toString());
                }
            } catch (Exception e) {
                if (!e.getMessage().equals("EOF")) throw e;
            }
        }

        assertEquals(astrExpectedLines, astrReadLines);
    }

    @Test
    public void testExecute_WithSingleLine_ReadsLineAndStopsAtEof() throws Exception {
        List<String> astrExpectedLines = List.of("Single line");

        FOpenStatement oFOpenWrite = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        oFOpenWrite.execute();

        FPrintStatement oFPrint = new FPrintStatement(2, FILE_ID_1, List.of(new StringValue("Single line")), false);
        oFPrint.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        FOpenStatement oFOpenRead = new FOpenStatement(3, FILE_ID_1, _oTempFile1.toString(), "read");
        oFOpenRead.execute();

        List<String> astrReadLines = new ArrayList<>();
        FInputStatement oFInput = new FInputStatement(4, FILE_ID_1, "A$");

        while (!((BooleanValue) Eof.execute(new IntegerValue(FILE_ID_1))).toBoolean()) {
            try {
                oFInput.execute();
                VariableManagement oVariableManagement = new VariableManagement();
                Value oValue = oVariableManagement.getMap("A$");
                if (oValue != null && !oValue.toString().isEmpty()) {
                    astrReadLines.add(oValue.toString());
                }
            } catch (Exception e) {
                if (!e.getMessage().equals("EOF")) throw e;
            }
        }

        assertEquals(astrExpectedLines, astrReadLines);
        assertTrue(astrReadLines.contains("Single line"));
    }

    @Test
    public void testExecute_WithFiveLines_ReadsAllFiveLines() throws Exception {
        List<String> astrExpectedLines = List.of("One", "Two", "Three", "Four", "Five");

        FOpenStatement oFOpenWrite = new FOpenStatement(1, FILE_ID_2, _oTempFile2.toString(), "write");
        oFOpenWrite.execute();

        FPrintStatement oFPrint1 = new FPrintStatement(2, FILE_ID_2, List.of(new StringValue("One\n")), false);
        FPrintStatement oFPrint2 = new FPrintStatement(3, FILE_ID_2, List.of(new StringValue("Two\n")), false);
        FPrintStatement oFPrint3 = new FPrintStatement(4, FILE_ID_2, List.of(new StringValue("Three\n")), false);
        FPrintStatement oFPrint4 = new FPrintStatement(5, FILE_ID_2, List.of(new StringValue("Four\n")), false);
        FPrintStatement oFPrint5 = new FPrintStatement(6, FILE_ID_2, List.of(new StringValue("Five")), false);

        oFPrint1.execute();
        oFPrint2.execute();
        oFPrint3.execute();
        oFPrint4.execute();
        oFPrint5.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_2, false);

        FOpenStatement oFOpenRead = new FOpenStatement(7, FILE_ID_2, _oTempFile2.toString(), "read");
        oFOpenRead.execute();

        List<String> astrReadLines = new ArrayList<>();
        FInputStatement oFInput = new FInputStatement(8, FILE_ID_2, "B$");

        while (!((BooleanValue) Eof.execute(new IntegerValue(FILE_ID_2))).toBoolean()) {
            try {
                oFInput.execute();
                VariableManagement oVariableManagement = new VariableManagement();
                Value oValue = oVariableManagement.getMap("B$");
                if (oValue != null && !oValue.toString().isEmpty()) {
                    astrReadLines.add(oValue.toString());
                }
            } catch (Exception e) {
                if (!e.getMessage().equals("EOF")) throw e;
            }
        }

        assertEquals(astrExpectedLines, astrReadLines);
    }

    @Test
    public void testExecute_ReturnsFalseWhenMoreDataToRead() throws Exception {
        FOpenStatement oFOpenWrite = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        oFOpenWrite.execute();
        FPrintStatement oFPrint = new FPrintStatement(2, FILE_ID_1, List.of(new StringValue("data\n")), false);
        oFPrint.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        FOpenStatement oFOpenRead = new FOpenStatement(3, FILE_ID_1, _oTempFile1.toString(), "read");
        oFOpenRead.execute();

        BooleanValue oResult = (BooleanValue) Eof.execute(new IntegerValue(FILE_ID_1));
        assertFalse(oResult.toBoolean());
    }

    @Test
    public void testExecute_ReturnsTrueAfterAllLinesRead() throws Exception {
        FOpenStatement oFOpenWrite = new FOpenStatement(1, FILE_ID_1, _oTempFile1.toString(), "write");
        oFOpenWrite.execute();
        FPrintStatement oFPrint = new FPrintStatement(2, FILE_ID_1, List.of(new StringValue("only line")), false);
        oFPrint.execute();

        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(FILE_ID_1, false);

        FOpenStatement oFOpenRead = new FOpenStatement(3, FILE_ID_1, _oTempFile1.toString(), "read");
        oFOpenRead.execute();

        FInputStatement oFInput = new FInputStatement(4, FILE_ID_1, "A$");
        oFInput.execute();
        oFInput.execute();

        BooleanValue oResult = (BooleanValue) Eof.execute(new IntegerValue(FILE_ID_1));
        assertTrue(oResult.toBoolean());
    }
}
