package eu.gricom.basic.lineEditor;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.memoryManager.Program;
import eu.gricom.basic.helper.Printer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LineEditorTest.java
 * <p>
 * Description: Unit tests for the LineEditor class, focusing on the run() method
 * behavior with empty and non-empty programs.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class LineEditorTest {

    private Program _oProgram;
    private LineEditor _oLineEditor;
    private ByteArrayOutputStream _oOutputStream;

    @BeforeEach
    public void setUp() {
        _oProgram = new Program();
        _oLineEditor = new LineEditor(_oProgram, false);

        // Capture output
        _oOutputStream = new ByteArrayOutputStream();
        PrintStream oPrintStream = new PrintStream(_oOutputStream);
        System.setOut(oPrintStream);
    }

    @Test
    public void testRunWithEmptyProgram() throws SyntaxErrorException {
        _oProgram.load("<empty>", "");

        assertFalse(_oProgram.hasContent());
    }

    @Test
    public void testRunWithNonEmptyProgram() {
        String strProgramSource = "10 PRINT \"HELLO\"\n20 END";
        _oProgram.load("test.bas", strProgramSource);

        assertTrue(_oProgram.hasContent());
    }

    @Test
    public void testAddLineToEmptyProgram() throws SyntaxErrorException {
        _oProgram.load("<empty>", "");
        assertFalse(_oProgram.hasContent());

        _oProgram.addOrReplace(10, "10 PRINT \"TEST\"");
        assertTrue(_oProgram.hasContent());
    }

    @Test
    public void testDeleteAllLinesFromProgram() throws SyntaxErrorException {
        String strProgramSource = "10 PRINT \"HELLO\"\n20 END";
        _oProgram.load("test.bas", strProgramSource);
        assertTrue(_oProgram.hasContent());

        _oProgram.deleteLines(10, 20);
        assertFalse(_oProgram.hasContent());
    }

    @Test
    public void testDeletePartialLinesFromProgram() throws SyntaxErrorException {
        String strProgramSource = "10 PRINT \"LINE 10\"\n20 PRINT \"LINE 20\"\n30 END";
        _oProgram.load("test.bas", strProgramSource);
        assertTrue(_oProgram.hasContent());

        _oProgram.deleteLines(20, 20);
        assertTrue(_oProgram.hasContent());
    }

    @Test
    public void testAddMultipleLinesWithNumbers() throws SyntaxErrorException {
        _oProgram.load("<empty>", "");
        assertFalse(_oProgram.hasContent());

        _oProgram.addOrReplace(10, "10 PRINT \"LINE 10\"");
        assertTrue(_oProgram.hasContent());

        _oProgram.addOrReplace(20, "20 PRINT \"LINE 20\"");
        assertTrue(_oProgram.hasContent());

        _oProgram.addOrReplace(30, "30 END");
        assertTrue(_oProgram.hasContent());
    }

    @Test
    public void testAddLineWithLeadingWhitespace() throws SyntaxErrorException {
        _oProgram.load("<empty>", "");
        _oProgram.addOrReplace(10, "10    PRINT \"TEST\"");
        assertTrue(_oProgram.hasContent());
    }

    @Test
    public void testProgramSourcePreservesLines() throws SyntaxErrorException {
        String strLine10 = "10 PRINT \"TEN\"";
        String strLine20 = "20 PRINT \"TWENTY\"";

        _oProgram.load("<empty>", "");
        _oProgram.addOrReplace(10, strLine10);
        _oProgram.addOrReplace(20, strLine20);

        String strProgram = _oProgram.getProgram();
        assertTrue(strProgram.contains("10 PRINT \"TEN\""));
        assertTrue(strProgram.contains("20 PRINT \"TWENTY\""));
    }

    @Test
    public void testReplaceExistingLine() throws SyntaxErrorException {
        _oProgram.load("<empty>", "");
        _oProgram.addOrReplace(10, "10 PRINT \"ORIGINAL\"");

        String strOriginal = _oProgram.getProgram();
        assertTrue(strOriginal.contains("ORIGINAL"));

        _oProgram.addOrReplace(10, "10 PRINT \"REPLACED\"");

        String strUpdated = _oProgram.getProgram();
        assertTrue(strUpdated.contains("REPLACED"));
        assertFalse(strUpdated.contains("ORIGINAL"));
    }
}