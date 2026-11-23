package eu.gricom.basic.compiler;

import eu.gricom.basic.memorymanager.Program;
import eu.gricom.basic.parser.BasicParser;
import eu.gricom.basic.statements.Statement;
import eu.gricom.basic.tokenizer.BasicLexer;
import eu.gricom.basic.memorymanager.FileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the BASIC-to-Java compiler.
 *
 * <p>Positive tests compile a small BASIC program, run the generated class, and compare the printed text with the
 * output that the interpreter gives for the same program. Negative tests check that unsupported statements are
 * refused with a clear message.</p>
 */
public class JavaCodeGeneratorTest {

    private static final String CLASS_NAME = "UnitProgram";

    /**
     * FileManager keeps the current directory in static fields, so a directory set by another test would leak into
     * these tests. Each test therefore starts with the empty (default) directory.
     */
    @BeforeEach
    public void resetFileManagerDirectory() {
        new FileManager().setCurrentDirectory("");
    }

    /** Parses BASIC source text the same way the editor does (tokenise, then parse). */
    private static List<Statement> parse(final String strBasic) throws Exception {
        Program oProgram = new Program();
        oProgram.load("unit-test.bas", strBasic);
        oProgram.setTokens(new BasicLexer().tokenize(oProgram.getProgram()));
        BasicParser oParser = new BasicParser(oProgram.getTokens(), false);
        oParser.parsePreRun();
        return oParser.parse();
    }

    /** Compiles the program to Java, compiles the Java, runs it, and returns what it printed. */
    private static String compileAndRun(final String strBasic) throws Exception {
        return compileAndRun(strBasic, "");
    }

    /**
     * Same as compileAndRun, but the program reads from standard input: the given text is what INPUT reads.
     */
    private static String compileAndRun(final String strBasic, final String strStandardInput) throws Exception {
        List<Statement> aoStatements = parse(strBasic);
        Path oDirectory = Files.createTempDirectory("compiler-unit-test");
        Path oJavaFile = oDirectory.resolve(CLASS_NAME + ".java");
        BasicCompiler.saveJava(null, aoStatements, oJavaFile);
        JavaSourceCompiler.compile(oJavaFile, oDirectory, BasicCompiler.runtimeLocation().toString());

        try (URLClassLoader oLoader = new URLClassLoader(new URL[] {oDirectory.toUri().toURL()},
                Thread.currentThread().getContextClassLoader())) {
            Class<?> oClass = oLoader.loadClass(CLASS_NAME);
            Method oMain = oClass.getMethod("main", String[].class);
            ByteArrayOutputStream oCaptured = new ByteArrayOutputStream();
            PrintStream oOriginal = System.out;
            java.io.InputStream oOriginalIn = System.in;
            System.setOut(new PrintStream(oCaptured, true, StandardCharsets.UTF_8));
            System.setIn(new java.io.ByteArrayInputStream(strStandardInput.getBytes(StandardCharsets.UTF_8)));
            try {
                oMain.invoke(null, (Object) new String[0]);
            } finally {
                System.setOut(oOriginal);
                System.setIn(oOriginalIn);
            }
            return oCaptured.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
        }
    }

    // ---------------------------------------------------------------- positive tests

    @Test
    public void testPrintText() throws Exception {
        assertEquals("HELLO\n", compileAndRun("10 PRINT \"HELLO\"\n20 END\n"));
    }

    @Test
    public void testForLoopCountsUpAndStops() throws Exception {
        assertEquals("1\n2\n3\n", compileAndRun("10 FOR I% = 1 TO 3\n20 PRINT I%\n30 NEXT\n40 END\n"));
    }

    @Test
    public void testForLoopWithStartAboveEndRunsOnce() throws Exception {
        // Same as the interpreter: the first pass runs before the end value is checked
        assertEquals("5\n", compileAndRun("10 FOR Z% = 5 TO 1\n20 PRINT Z%\n30 NEXT\n40 END\n"));
    }

    @Test
    public void testGosubAndReturn() throws Exception {
        assertEquals("SUB\nBACK\n",
                compileAndRun("10 GOSUB 100\n20 PRINT \"BACK\"\n30 END\n100 PRINT \"SUB\"\n110 RETURN\n"));
    }

    @Test
    public void testWhileLoop() throws Exception {
        assertEquals("1\n2\n3\n", compileAndRun(
                "10 N% = 0\n20 WHILE N% < 3\n30 N% = N% + 1\n40 PRINT N%\n50 END-WHILE\n60 END\n"));
    }

    @Test
    public void testDoUntilLoop() throws Exception {
        assertEquals("2\n4\n", compileAndRun(
                "10 M% = 0\n20 DO\n30 M% = M% + 2\n40 PRINT M%\n50 UNTIL M% >= 4\n60 END\n"));
    }

    @Test
    public void testSingleLineIfJumpsWhenTrue() throws Exception {
        assertEquals("YES\n", compileAndRun(
                "10 X% = 5\n20 IF X% > 3 THEN 40\n30 PRINT \"NO\"\n40 PRINT \"YES\"\n50 END\n"));
    }

    @Test
    public void testBlockIfElseTakesElsePart() throws Exception {
        assertEquals("OTHER\n", compileAndRun(
                "10 X% = 1\n20 IF X% > 5 THEN\n30 PRINT \"BIG\"\n40 ELSE\n50 PRINT \"OTHER\"\n60 END-IF\n70 END\n"));
    }

    @Test
    public void testOnGotoSelectsTarget() throws Exception {
        assertEquals("SECOND\n", compileAndRun(
                "10 I% = 2\n20 ON I% GOTO 40, 50\n30 PRINT \"NONE\"\n35 END\n40 PRINT \"FIRST\"\n45 END\n"
                        + "50 PRINT \"SECOND\"\n60 END\n"));
    }

    @Test
    public void testOnGotoOutOfRangeContinues() throws Exception {
        assertEquals("NONE\n", compileAndRun(
                "10 I% = 5\n20 ON I% GOTO 40, 50\n30 PRINT \"NONE\"\n35 END\n40 PRINT \"FIRST\"\n45 END\n"
                        + "50 PRINT \"SECOND\"\n60 END\n"));
    }

    @Test
    public void testArithmeticAndFunctions() throws Exception {
        assertEquals("4.0\n1024.0\n", compileAndRun("10 PRINT SQR(16)\n20 PRINT 2 ^ 10\n30 END\n"));
    }

    @Test
    public void testDecimalBcdValue() throws Exception {
        assertEquals("0.3\n", compileAndRun(
                "10 D# = 0.1\n20 E# = D# + 0.2\n30 PRINT STR$(E#)\n40 END\n"));
    }

    @Test
    public void testShiftOperatorOnIntegers() throws Exception {
        assertEquals("8\n", compileAndRun("10 A% = 1\n20 PRINT A% << 3\n30 END\n"));
    }

    @Test
    public void testGeneratedSourceHasOneSwitchCase() throws Exception {
        String strSource = new JavaCodeGenerator(parse("10 PRINT \"A\"\n20 END\n"), CLASS_NAME).generate();
        assertTrue(strSource.contains("public final class " + CLASS_NAME));
        assertTrue(strSource.contains("case 0: {"));
        assertTrue(strSource.contains("case 1: {"));
        assertTrue(strSource.contains("System.out.print(new StringValue(\"A\"));"));
    }

    @Test
    public void testClassNameFromJavaFile() throws Exception {
        assertEquals("Prog1", BasicCompiler.classNameOf(Path.of("Prog1.java")));
    }

    // ---------------------------------------------------------------- negative tests

    @Test
    public void testReadIsRejectedWithClearMessage() throws Exception {
        CompileException oException = assertThrows(CompileException.class,
                () -> new JavaCodeGenerator(parse("10 READ A\n20 END\n"), CLASS_NAME).generate());
        assertTrue(oException.getMessage().contains("not supported by the compiler yet"));
        assertTrue(oException.getMessage().contains("BASIC line 10"));
    }

    @Test
    public void testGotoToMissingLineIsRejected() throws Exception {
        CompileException oException = assertThrows(CompileException.class,
                () -> new JavaCodeGenerator(parse("10 GOTO 999\n20 END\n"), CLASS_NAME).generate());
        assertTrue(oException.getMessage().contains("does not exist"));
    }

    @Test
    public void testArraysAreRejected() throws Exception {
        CompileException oException = assertThrows(CompileException.class,
                () -> new JavaCodeGenerator(parse("10 DIM A(5)\n20 END\n"), CLASS_NAME).generate());
        assertTrue(oException.getMessage().contains("not supported by the compiler yet"));
    }

    @Test
    public void testEmptyProgramIsRejected() {
        assertThrows(CompileException.class, () -> new JavaCodeGenerator(List.of(), CLASS_NAME).generate());
    }

    @Test
    public void testJavaFileWithoutJavaExtensionIsRejected() {
        assertThrows(CompileException.class, () -> BasicCompiler.classNameOf(Path.of("prog.bas")));
    }

    @Test
    public void testJavaFileWithInvalidClassNameIsRejected() {
        assertThrows(CompileException.class, () -> BasicCompiler.classNameOf(Path.of("my-program.java")));
    }

    @Test
    public void testGeneratedSourceIsNotEmptyForValidProgram() throws Exception {
        String strSource = new JavaCodeGenerator(parse("10 END\n"), CLASS_NAME).generate();
        assertFalse(strSource.isBlank());
    }

    // ---------------------------------------------------------------- unary minus, operators, I/O (added in 0.4.0)

    @Test
    public void testUnaryMinusOnIntegerPrintsNegativeValue() throws Exception {
        assertEquals("-5\n", compileAndRun("10 A% = 5\n20 PRINT -A%\n30 END\n"));
    }

    @Test
    public void testOperatorsWithoutSpacesAreReadAsOperators() throws Exception {
        assertEquals("-2.0\n-5.0\n", compileAndRun(
                "10 A% = 5\n20 PRINT A%-7\n30 X=-A%\n40 PRINT X\n50 END\n"));
    }

    @Test
    public void testUnaryMinusOnStringFailsWhenRun() throws Exception {
        java.lang.reflect.InvocationTargetException oException = assertThrows(
                java.lang.reflect.InvocationTargetException.class,
                () -> compileAndRun("10 S$ = \"x\"\n20 PRINT -S$\n30 END\n"));
        assertTrue(oException.getCause().getMessage().contains("Cannot apply unary minus to a string"));
    }

    @Test
    public void testInputReadsFromStandardInput() throws Exception {
        assertEquals("Hello World\n-21.0\n", compileAndRun(
                "10 INPUT N$\n20 PRINT \"Hello \"; N$\n30 INPUT A\n40 PRINT -A\n50 END\n", "World\n21\n"));
    }

    @Test
    public void testSystemFunctionRunsCommand() throws Exception {
        assertTrue(compileAndRun("10 A$ = SYSTEM(\"RUN\", \"echo unit\")\n20 PRINT A$\n30 END\n")
                .contains("unit"));
    }

    @Test
    public void testFexistsIsFalseForMissingFile() throws Exception {
        assertEquals("False\n", compileAndRun(
                "10 PRINT FEXISTS(\"no_such_file_for_unit_test.txt\")\n20 END\n"));
    }

    @Test
    public void testFileWriteReadAndDelete() throws Exception {
        assertEquals("hello\nFalse\n", compileAndRun(
                "10 FOPEN 41 \"unit_compiler_io.txt\" \"write\"\n20 FPRINT 41 \"hello\"\n30 FCLOSE 41 \"\"\n"
                        + "40 FOPEN 42 \"unit_compiler_io.txt\" \"read\"\n50 FINPUT 42, L$\n60 PRINT L$\n"
                        + "70 FCLOSE 42 \"DELETE\"\n80 PRINT FEXISTS(\"unit_compiler_io.txt\")\n90 END\n"));
    }

    @Test
    public void testDirectoryCreateAndRemove() throws Exception {
        assertEquals("True\nFalse\n", compileAndRun(
                "10 MKDIR \"unit_compiler_dir\"\n20 PRINT DIREXISTS(\"unit_compiler_dir\")\n"
                        + "30 RMDIR \"unit_compiler_dir\"\n40 PRINT DIREXISTS(\"unit_compiler_dir\")\n50 END\n"));
    }

    @Test
    public void testFileIdFromStringFailsWhenRun() throws Exception {
        java.lang.reflect.InvocationTargetException oException = assertThrows(
                java.lang.reflect.InvocationTargetException.class,
                () -> compileAndRun("10 PRINT FISOPEN(\"2\")\n20 END\n"));
        assertTrue(oException.getCause().getMessage().contains("File ID is not an integer"));
    }

    @Test
    public void testFileIdLiteralWorksInCompiledProgram() throws Exception {
        // a file ID written as a literal (43) works, and the file is removed again
        assertEquals("True\n", compileAndRun(
                "10 FOPEN 43 \"unit_fileid_literal.txt\" \"write\"\n20 PRINT FISOPEN(43)\n"
                        + "30 FCLOSE 43 \"DELETE\"\n40 END\n"));
    }
}
