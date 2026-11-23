package eu.gricom.basic.parser;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.functions.Function;
import eu.gricom.basic.memorymanager.Program;
import eu.gricom.basic.statements.MkDirStatement;
import eu.gricom.basic.statements.PragmaStatement;
import eu.gricom.basic.statements.RmDirStatement;
import eu.gricom.basic.statements.Statement;
import eu.gricom.basic.tokenizer.BasicLexer;
import eu.gricom.basic.variabletypes.IntegerValue;
import eu.gricom.basic.variabletypes.RealValue;
import eu.gricom.basic.variabletypes.StringValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the parser's handling of MKDIR, RMDIR and @PRAGMA, and for the file ID check used by the file
 * functions.
 */
public class FileDirectoryAndPragmaParserTest {

    private static List<Statement> parse(final String strBasic) throws Exception {
        Program oProgram = new Program();
        oProgram.load("unit-test.bas", strBasic);
        oProgram.setTokens(new BasicLexer().tokenize(oProgram.getProgram()));
        BasicParser oParser = new BasicParser(oProgram.getTokens(), false);
        oParser.parsePreRun();
        return oParser.parse();
    }

    // ---------------------------------------------------------------- MKDIR and RMDIR

    @Test
    public void testMkdirParsesToMkDirStatement() throws Exception {
        List<Statement> aoStatements = parse("10 MKDIR \"newdir\"\n20 END\n");
        assertTrue(aoStatements.get(0) instanceof MkDirStatement);
    }

    @Test
    public void testRmdirWithoutForceParses() throws Exception {
        List<Statement> aoStatements = parse("10 RMDIR \"olddir\"\n20 END\n");
        assertTrue(aoStatements.get(0) instanceof RmDirStatement);
    }

    @Test
    public void testRmdirWithForceParses() throws Exception {
        List<Statement> aoStatements = parse("10 RMDIR \"olddir\", TRUE\n20 END\n");
        assertTrue(aoStatements.get(0) instanceof RmDirStatement);
    }

    @Test
    public void testMkdirWithoutQuotedNameIsRefused() {
        assertThrows(SyntaxErrorException.class, () -> parse("10 MKDIR 5\n20 END\n"));
    }

    @Test
    public void testRmdirWithoutQuotedNameIsRefused() {
        assertThrows(SyntaxErrorException.class, () -> parse("10 RMDIR 5\n20 END\n"));
    }

    // ---------------------------------------------------------------- @PRAGMA

    @Test
    public void testPragmaWithQuotedNameAndValueParses() throws Exception {
        List<Statement> aoStatements = parse("10 @PRAGMA( \"LOG_LEVEL\" = \"INFO\" )\n20 END\n");
        assertTrue(aoStatements.get(0) instanceof PragmaStatement);
    }

    @Test
    public void testPragmaTraceNameParses() throws Exception {
        List<Statement> aoStatements = parse("10 @PRAGMA(\"Trace\" = \"on\")\n20 END\n");
        assertTrue(aoStatements.get(0) instanceof PragmaStatement);
    }

    @Test
    public void testPragmaWithUnknownNameIsRefused() {
        SyntaxErrorException oException = assertThrows(SyntaxErrorException.class,
                () -> parse("10 @PRAGMA( \"NOPE\" = \"x\" )\n20 END\n"));
        assertTrue(oException.getMessage().contains("Unknown pragma: NOPE"));
    }

    @Test
    public void testPragmaWithUnquotedNameIsRefused() {
        assertThrows(SyntaxErrorException.class,
                () -> parse("10 @PRAGMA( bDartmouthFlag = true )\n20 END\n"));
    }

    @Test
    public void testPragmaKnownNamesAreRecognised() {
        assertTrue(PragmaStatement.isKnownParameter("LOG_LEVEL"));
        assertTrue(PragmaStatement.isKnownParameter("log_level"));
        assertTrue(PragmaStatement.isKnownParameter("Trace"));
        assertTrue(PragmaStatement.isKnownParameter("Logger"));
        assertFalse(PragmaStatement.isKnownParameter("bDartmouthFlag"));
    }

    // ---------------------------------------------------------------- file ID check

    @Test
    public void testIntegerFileIdIsAccepted() throws Exception {
        assertEquals(3, Function.fileId(new IntegerValue(3)));
    }

    @Test
    public void testWholeNumberLiteralFileIdIsAccepted() throws Exception {
        // a literal such as 2 is read as a real value (2.0)
        assertEquals(2, Function.fileId(new RealValue(2.0)));
    }

    @Test
    public void testFractionalFileIdIsRefused() {
        assertThrows(eu.gricom.basic.error.RuntimeException.class, () -> Function.fileId(new RealValue(2.5)));
    }

    @Test
    public void testStringFileIdIsRefused() {
        assertThrows(eu.gricom.basic.error.RuntimeException.class, () -> Function.fileId(new StringValue("2")));
    }

    // ---------------------------------------------------------------- @PRAGMA brackets are required

    @Test
    public void testPragmaWithoutOpeningBracketIsRefused() {
        SyntaxErrorException oException = assertThrows(SyntaxErrorException.class,
                () -> parse("10 @PRAGMA \"Trace\" = \"on\"\n20 END\n"));
        assertTrue(oException.getMessage().contains("expected LEFT_PAREN"));
    }

    @Test
    public void testPragmaWithoutClosingBracketIsRefused() {
        assertThrows(SyntaxErrorException.class, () -> parse("10 @PRAGMA( \"Trace\" = \"on\"\n20 END\n"));
    }

    @Test
    public void testPragmaWithBracketsAndSpacesIsAccepted() throws Exception {
        List<Statement> aoStatements = parse("10 @PRAGMA( \"Trace\" = \"on\" )\n20 END\n");
        assertTrue(aoStatements.get(0) instanceof PragmaStatement);
    }
}
