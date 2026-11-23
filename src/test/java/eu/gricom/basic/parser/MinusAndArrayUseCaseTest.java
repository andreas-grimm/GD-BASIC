package eu.gricom.basic.parser;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.memorymanager.Program;
import eu.gricom.basic.runtimemanager.Execute;
import eu.gricom.basic.tokenizer.BasicLexer;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Use cases that prove two rules for the minus sign:
 * <ol>
 *   <li>The storage key "A-1" is only made for the array element A(1) (see ArrayAccessExpression). A programmer's
 *       "A-1" is always the subtraction A - 1, so it never finds the array element.</li>
 *   <li>A programmer's "A-1", "A -1", "A - 1" and "A-1" inside an index are read as a subtraction.</li>
 * </ol>
 * Every test uses its own variable names, because the variable store is static and is shared by all tests.
 * The interpreter is run in the same way as LineEditor runs a program.
 */
public class MinusAndArrayUseCaseTest {

    /** The output of a program and whether an error (other than END) stopped it. */
    private record Result(String output, boolean runtimeError) {
    }

    private static Result run(final String strBasic) throws Exception {
        Program oProgram = new Program();
        oProgram.load("use-case.bas", strBasic);
        oProgram.setTokens(new BasicLexer().tokenize(oProgram.getProgram()));
        BasicParser oParser = new BasicParser(oProgram.getTokens(), false);
        oProgram.setPreRunStatements(oParser.parsePreRun());
        oProgram.setStatements(oParser.parse());

        Execute oExecute = new Execute(oProgram);
        oExecute.loadEnvironment();

        ByteArrayOutputStream oCaptured = new ByteArrayOutputStream();
        PrintStream oOriginal = System.out;
        System.setOut(new PrintStream(oCaptured, true, StandardCharsets.UTF_8));
        try {
            oExecute.runProgram();
        } finally {
            System.setOut(oOriginal);
        }
        return new Result(oCaptured.toString(StandardCharsets.UTF_8).replace("\r\n", "\n"),
                oExecute.hasRuntimeError());
    }

    // ---------------------------------------------------------------- positive: a minus between two values is a subtraction

    @Test
    public void testUseCase01_NameMinusNumberWithoutSpaces() throws Exception {
        Result oResult = run("10 P = 3\n20 PRINT P-1\n30 END\n");
        assertEquals("2.0\n", oResult.output());
        assertFalse(oResult.runtimeError());
    }

    @Test
    public void testUseCase02_NameMinusNumberWithSpaces() throws Exception {
        Result oResult = run("10 P = 3\n20 PRINT P - 1\n30 END\n");
        assertEquals("2.0\n", oResult.output());
    }

    @Test
    public void testUseCase03_NameMinusNumberWithSpaceBeforeOnly() throws Exception {
        Result oResult = run("10 P = 3\n20 PRINT P -1\n30 END\n");
        assertEquals("2.0\n", oResult.output());
    }

    @Test
    public void testUseCase04_ScalarIsNotConfusedWithArrayElementOfSameName() throws Exception {
        // The array element Q(1) is stored under the key "Q-1". The scalar difference Q-1 must still be the subtraction.
        Result oResult = run("10 Q(1) = 7\n20 Q = 3\n30 PRINT Q-1\n40 PRINT Q(1)\n50 END\n");
        assertEquals("2.0\n7.0\n", oResult.output());
    }

    @Test
    public void testUseCase05_ArrayElementMinusNumberIsSubtraction() throws Exception {
        Result oResult = run("10 Q(1) = 7\n20 PRINT Q(1)-1\n30 PRINT Q(1) - 1\n40 END\n");
        assertEquals("6.0\n6.0\n", oResult.output());
    }

    @Test
    public void testUseCase06_IndexWithSubtractionInsideRoundBrackets() throws Exception {
        Result oResult = run("10 M(2) = 11\n20 I = 3\n30 PRINT M(I-1)\n40 PRINT M(I - 1)\n50 END\n");
        assertEquals("11.0\n11.0\n", oResult.output());
    }

    @Test
    public void testUseCase07_MultiplicationWithoutSpaces() throws Exception {
        Result oResult = run("10 P = 3\n20 PRINT 2*P\n30 END\n");
        assertEquals("6.0\n", oResult.output());
    }

    @Test
    public void testUseCase08_UnaryMinusOnName() throws Exception {
        Result oResult = run("10 P = 3\n20 PRINT -P\n30 END\n");
        assertEquals("-3.0\n", oResult.output());
    }

    @Test
    public void testUseCase09_AssignmentOfUnaryMinus() throws Exception {
        Result oResult = run("10 P = 3\n20 X = -P\n30 PRINT X\n40 END\n");
        assertEquals("-3.0\n", oResult.output());
    }

    @Test
    public void testUseCase10_NameMinusNameWithoutSpaces() throws Exception {
        Result oResult = run("10 P = 10\n20 R = 4\n30 PRINT P-R\n40 END\n");
        assertEquals("6.0\n", oResult.output());
    }

    // ---------------------------------------------------------------- negative: a name with a minus is never a variable

    @Test
    public void testUseCase11_NameMinusOneDoesNotFindArrayElementWithoutScalar() throws Exception {
        // Z(1) exists, but the scalar Z does not. "Z-1" must be the subtraction, so the program stops with
        // "Unknown variable <Z>". It must not print 7, the value of the element Z(1).
        Result oResult = run("10 Z(1) = 7\n20 PRINT Z-1\n30 END\n");
        assertTrue(oResult.runtimeError());
        assertFalse(oResult.output().contains("7"));
    }

    @Test
    public void testUseCase12_AssignmentToNameMinusOneIsRefused() {
        // "R-1 = 5" is a subtraction on the left side of "=", so the parser refuses it before the program runs.
        assertThrows(SyntaxErrorException.class, () -> run("10 R-1 = 5\n20 END\n"));
    }

    @Test
    public void testUseCase13_AssignmentToNameMinusOneWithSpacesIsRefused() {
        assertThrows(SyntaxErrorException.class, () -> run("10 R - 1 = 5\n20 END\n"));
    }

    @Test
    public void testUseCase14_StringIndexWithSubtractionIsRefused() throws Exception {
        // Inside [ ] the text is kept together, so I-1 is one index text and is not a number: the program stops.
        Result oResult = run("10 W$ = \"ABCDE\"\n20 I = 3\n30 PRINT W$[I-1]\n40 END\n");
        assertTrue(oResult.runtimeError());
    }

    // ---------------------------------------------------------------- percent suffix: A%-1 is A% - 1

    @Test
    public void testUseCase15_PercentSuffixNameMinusOneIsSubtraction() throws Exception {
        // The literal 3 is read as a real, so the result prints as 2.0 (the existing number format)
        Result oResult = run("10 D% = 3\n20 PRINT D%-1\n30 END\n");
        assertEquals("2.0\n", oResult.output());
    }

    @Test
    public void testUseCase16_PercentSuffixArrayElementMinusOneIsSubtraction() throws Exception {
        // C%(1) is stored under the key "C%-1". A programmer's C%(1)-1 is still the element minus one.
        Result oResult = run("10 C%(1) = 7\n20 PRINT C%(1)-1\n30 END\n");
        assertEquals("6.0\n", oResult.output());
    }

    @Test
    public void testUseCase17_PercentNameMinusOneDoesNotFindArrayElement() throws Exception {
        // Only the element C%(1) exists. "C%-1" is the subtraction C% - 1, so the program stops with
        // "Unknown variable <C%>" and does not print the element's value.
        Result oResult = run("10 C%(1) = 7\n20 PRINT C%-1\n30 END\n");
        assertTrue(oResult.runtimeError());
        assertFalse(oResult.output().contains("7"));
    }
}
