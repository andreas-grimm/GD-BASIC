package eu.gricom.basic.variableTypes;

import eu.gricom.basic.error.OverflowException;
import eu.gricom.basic.error.SyntaxErrorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * BcdOutputFormatTest.java
 * <p>
 * Description: Tests the printed form of BCD values (Phase 5, decision D10): plain decimal notation, no exponent, no
 * trailing zeros after the decimal point. Printing is done by BcdValue.toString, which PRINT and STR$ both use.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class BcdOutputFormatTest {

    @Test
    public void testZeroPrintsAsZero() throws Exception {
        assertEquals("0", new BcdValue("0.000").toString());
    }

    @Test
    public void testNegativeKeepsSignAndDropsZeros() throws Exception {
        assertEquals("-12.5", new BcdValue("-12.50").toString());
    }

    @Test
    public void testIntegerZerosAreKept() throws Exception {
        // Zeros before the decimal point are part of the number, so they stay
        assertEquals("1000", new BcdValue("1000").toString());
        assertEquals("1000", new BcdValue("1E3").toString());
    }

    @Test
    public void testNoExponentNotationForLargeValues() throws Exception {
        String strThirtyNineZeros = "1" + "0".repeat(39);
        String strPrinted = new BcdValue(strThirtyNineZeros).toString();
        assertEquals(strThirtyNineZeros, strPrinted);
        assertFalse(strPrinted.contains("E"));
    }

    @Test
    public void testRoundingThatCarriesIntoWholePartPrintsPlain() throws Exception {
        // 0.99...9 (45 nines) rounds up to 1 at 40 significant digits, and prints as "1", not "1.000..."
        assertEquals("1", new BcdValue("0." + "9".repeat(45)).toString());
    }

    @Test
    public void testSmallValueKeepsSignificantDigitsWithoutExponent() throws Exception {
        assertEquals("0.00000012", new BcdValue("1.2E-7").toString());
    }

    @Test
    public void testOutputDoesNotChangeTheStoredValue() throws Exception {
        BcdValue oValue = new BcdValue("2.50");
        oValue.toString();
        assertEquals(0, oValue.compareTo(new BcdValue("2.5")));
    }

    @Test
    public void testFortyOneDigitOutputIsRejectedAtCreation() {
        assertThrows(OverflowException.class, () -> new BcdValue("1" + "0".repeat(40)));
    }

    @Test
    public void testTextThatIsNotNumberIsRejected() {
        assertThrows(SyntaxErrorException.class, () -> new BcdValue("1,5"));
    }
}
