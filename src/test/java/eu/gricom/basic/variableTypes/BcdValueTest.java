package eu.gricom.basic.variabletypes;

import eu.gricom.basic.error.OverflowException;
import eu.gricom.basic.error.SyntaxErrorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * BcdValueTest.java
 * <p>
 * Description: Unit tests for the BCD value core (Phase 2). Positive tests prove correct storing, rounding and output.
 * Negative tests prove that overflow and malformed input are rejected.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class BcdValueTest {

    // 40 integer digits. The last 0 is an integer digit, so it is kept: only zeros after the decimal point are removed.
    private static final String FORTY_DIGITS = "1234567890123456789012345678901234567890";

    // ----- positive -----

    @Test
    public void testPlainDecimalIsKept() throws Exception {
        assertEquals("1234.56", new BcdValue("1234.56").toString());
    }

    @Test
    public void testTrailingZerosAreRemoved() throws Exception {
        assertEquals("2.5", new BcdValue("2.50").toString());
        assertEquals("100", new BcdValue("100.000").toString());
    }

    @Test
    public void testWholeNumberFromLong() throws Exception {
        assertEquals("123", new BcdValue(123L).toString());
    }

    @Test
    public void testDoubleUsesShortestDecimalForm() throws Exception {
        assertEquals("0.1", new BcdValue(0.1).toString());
    }

    @Test
    public void testFortyIntegerDigitsAccepted() throws Exception {
        String strForty = "1" + "0".repeat(39);
        assertEquals(strForty, new BcdValue(strForty).toString());
    }

    @Test
    public void testFortySignificantDigitsKept() throws Exception {
        assertEquals(FORTY_DIGITS, new BcdValue(FORTY_DIGITS).toString());
    }

    @Test
    public void testRoundingHalfUpAtFortyDigits() throws Exception {
        // 40 integer digits plus ".5": the 41st digit is 5, so HALF_UP rounds the last digit up (HALF_EVEN would keep 0)
        assertEquals("1234567890123456789012345678901234567891",
                new BcdValue(FORTY_DIGITS + ".5").toString());
    }

    @Test
    public void testRoundingBelowHalfRoundsDown() throws Exception {
        assertEquals(FORTY_DIGITS, new BcdValue(FORTY_DIGITS + ".4").toString());
    }

    @Test
    public void testFractionRoundedToFortySignificantDigits() throws Exception {
        // 41 significant digits in total: the last one is rounded away
        // 41 significant digits: the last "1" is rounded away, leaving 40 digits, and the resulting trailing 0 is removed
        String strInput = "0.12345678901234567890123456789012345678901";
        assertEquals("0.123456789012345678901234567890123456789", new BcdValue(strInput).toString());
        assertTrue(new BcdValue(strInput).getBigDecimal().precision() <= 40);
    }

    @Test
    public void testSmallNumberKeepsNaturalLength() throws Exception {
        assertEquals("0.000000000000000000000000000000000000001",
                new BcdValue("0.000000000000000000000000000000000000001").toString());
    }

    @Test
    public void testComparisonIgnoresTrailingZeros() throws Exception {
        assertEquals(0, new BcdValue("2.0").compareTo(new BcdValue("2.00")));
    }

    @Test
    public void testComparisonOrder() throws Exception {
        assertTrue(new BcdValue("2").compareTo(new BcdValue("3")) < 0);
        assertTrue(new BcdValue("3").compareTo(new BcdValue("2")) > 0);
    }

    @Test
    public void testToRealIsApproximate() throws Exception {
        assertEquals(1234.56, new BcdValue("1234.56").toReal(), 0.000001);
    }

    // ----- negative -----

    @Test
    public void testFortyOneIntegerDigitsOverflows() {
        String strFortyOne = "1" + "0".repeat(40);
        assertThrows(OverflowException.class, () -> new BcdValue(strFortyOne));
    }

    @Test
    public void testFortyOneIntegerDigitsWithFractionOverflows() {
        assertThrows(OverflowException.class,
                () -> new BcdValue("12345678901234567890123456789012345678901.5"));
    }

    @Test
    public void testRoundingCannotHideOverflow() {
        // 40 nines with ".6" round up to 10^40, which has 41 digits before the decimal point
        assertThrows(OverflowException.class,
                () -> new BcdValue("9999999999999999999999999999999999999999.6"));
    }

    @Test
    public void testLargeDoubleOverflows() {
        assertThrows(OverflowException.class, () -> new BcdValue(1e50));
    }

    @Test
    public void testInfiniteDoubleOverflows() {
        assertThrows(OverflowException.class, () -> new BcdValue(Double.POSITIVE_INFINITY));
    }

    @Test
    public void testNaNDoubleIsSyntaxError() {
        assertThrows(SyntaxErrorException.class, () -> new BcdValue(Double.NaN));
    }

    @Test
    public void testTextIsNotANumber() {
        assertThrows(SyntaxErrorException.class, () -> new BcdValue("abc"));
    }

    @Test
    public void testEmptyTextIsNotANumber() {
        assertThrows(SyntaxErrorException.class, () -> new BcdValue(""));
    }
}
