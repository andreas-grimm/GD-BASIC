package eu.gricom.basic.regression;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.functions.Chr;
import eu.gricom.basic.functions.Instr;
import eu.gricom.basic.functions.Mid;
import eu.gricom.basic.tokenizer.BasicTokenType;
import eu.gricom.basic.tokenizer.ReservedWords;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Phase7RegressionTest.java
 * <p>
 * Description: Unit tests for the bugs fixed in Phase 7 that can be tested without files or a running program.
 * <ul>
 *   <li>REAL op INTEGER gives a REAL result (earlier: a syntax error).</li>
 *   <li>CHR$ and MID$ accept whole-number REAL values (number literals are REAL).</li>
 *   <li>INSTR returns a one-based position, 0 when not found (as documented).</li>
 *   <li>WEND is a second spelling of END-WHILE.</li>
 * </ul>
 * The file handling, lexer and parser fixes are covered by the system programs in test/system.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class Phase7RegressionTest {

    @Test
    public void testRealPlusIntegerGivesReal() throws Exception {
        Value oResult = new RealValue(5).plus(new IntegerValue(2));

        assertTrue(oResult instanceof RealValue);
        assertEquals(7.0, oResult.toReal(), 0.0);
    }

    @Test
    public void testIntegerTimesRealGivesReal() throws Exception {
        Value oResult = new IntegerValue(5).multiply(new RealValue(2.5));

        assertTrue(oResult instanceof RealValue);
        assertEquals(12.5, oResult.toReal(), 0.0);
    }

    @Test
    public void testRealPlusStringIsRejected() {
        assertThrows(SyntaxErrorException.class, () -> new RealValue(5).plus(new StringValue("2")));
    }

    @Test
    public void testChrAcceptsWholeNumberReal() throws Exception {
        assertEquals("A", ((StringValue) Chr.execute(new RealValue(65))).toString());
    }

    @Test
    public void testChrWithIntegerStillWorks() throws Exception {
        assertEquals("a", ((StringValue) Chr.execute(new IntegerValue(97))).toString());
    }

    @Test
    public void testChrRejectsFractionalReal() {
        assertThrows(RuntimeException.class, () -> Chr.execute(new RealValue(65.5)));
    }

    @Test
    public void testMidAcceptsWholeNumberRealPositions() throws Exception {
        // start 1, end 3 (the function's current meaning): "bcd" from "abcde"
        Value oResult = Mid.execute(new StringValue("abcde"), new RealValue(1), new RealValue(3));

        assertEquals("bcd", oResult.toString());
    }

    @Test
    public void testMidRejectsFractionalPosition() {
        assertThrows(RuntimeException.class,
                () -> Mid.execute(new StringValue("abcde"), new RealValue(1.5), new IntegerValue(3)));
    }

    @Test
    public void testInstrIsOneBased() throws Exception {
        assertEquals(1, ((IntegerValue) Instr.execute(new StringValue("abc"), new StringValue("a"))).toInt());
        assertEquals(0, ((IntegerValue) Instr.execute(new StringValue("abc"), new StringValue("z"))).toInt());
    }

    @Test
    public void testWendIsEndWhile() {
        assertEquals(BasicTokenType.ENDWHILE, ReservedWords.getTokenType(ReservedWords.getIndex("WEND")));
        assertEquals(BasicTokenType.ENDWHILE, ReservedWords.getTokenType(ReservedWords.getIndex("END-WHILE")));
    }
}
