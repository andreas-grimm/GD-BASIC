package eu.gricom.basic.math;

import eu.gricom.basic.error.DivideByZeroException;
import eu.gricom.basic.error.OverflowException;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.variableTypes.BcdValue;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * BcdMathTest.java
 * <p>
 * Description: Unit tests for the BCD maths functions (Phase 4, decision D12).
 * <p>
 * Reference values: generated with mpmath 1.3.0 (arbitrary precision, 80 digits), rounded to 40 significant digits
 * with ROUND_HALF_UP (Python decimal module). The generator script is kept in the work folder, see the
 * documentation section "BCD maths functions". Each expected value below is the 40-digit result that BcdValue
 * produces from the unrounded BcdMath result.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class BcdMathTest {

    private static final BigDecimal ONE = BigDecimal.ONE;

    /** Rounds a BcdMath result the same way a BCD variable does. */
    private static String stored(final BigDecimal oValue) throws Exception {
        return new BcdValue(oValue).toString();
    }

    // ----- positive: constants -----

    @Test
    public void testPi() throws Exception {
        assertEquals("3.141592653589793238462643383279502884197", stored(BcdMath.pi()));
    }

    // ----- positive: EXP and LN -----

    @Test
    public void testExpOne() throws Exception {
        assertEquals("2.718281828459045235360287471352662497757", stored(BcdMath.exp(ONE)));
    }

    @Test
    public void testExpFractional() throws Exception {
        assertEquals("12.18249396070347343807017595116796618318", stored(BcdMath.exp(new BigDecimal("2.5"))));
    }

    @Test
    public void testExpNegative() throws Exception {
        assertEquals("0.0497870683678639429793424156500617766317", stored(BcdMath.exp(new BigDecimal("-3"))));
    }

    @Test
    public void testExpSmallFraction() throws Exception {
        assertEquals("1.105170918075647624811707826490246668225", stored(BcdMath.exp(new BigDecimal("0.1"))));
    }

    @Test
    public void testLnTwo() throws Exception {
        assertEquals("0.6931471805599453094172321214581765680755", stored(BcdMath.ln(new BigDecimal("2"))));
    }

    @Test
    public void testLnTen() throws Exception {
        assertEquals("2.302585092994045684017991454684364207601", stored(BcdMath.ln(BigDecimal.TEN)));
    }

    @Test
    public void testLnBelowOne() throws Exception {
        assertEquals("-0.6931471805599453094172321214581765680755", stored(BcdMath.ln(new BigDecimal("0.5"))));
    }

    @Test
    public void testLnLargeValue() throws Exception {
        assertEquals("7.118476228297786292508792536387081841341", stored(BcdMath.ln(new BigDecimal("1234.5678"))));
    }

    // ----- positive: LOG10 and SQR -----

    @Test
    public void testLog10Two() throws Exception {
        assertEquals("0.3010299956639811952137388947244930267682", stored(BcdMath.log10(new BigDecimal("2"))));
    }

    @Test
    public void testLog10ExactPowerOfTen() throws Exception {
        assertEquals("3", stored(BcdMath.log10(new BigDecimal("1000"))));
    }

    @Test
    public void testLog10Fraction() throws Exception {
        assertEquals("-0.6020599913279623904274777894489860535364", stored(BcdMath.log10(new BigDecimal("0.25"))));
    }

    @Test
    public void testSqrTwo() throws Exception {
        assertEquals("1.41421356237309504880168872420969807857", stored(BcdMath.sqrt(new BigDecimal("2"))));
    }

    @Test
    public void testSqrTen() throws Exception {
        assertEquals("3.16227766016837933199889354443271853372", stored(BcdMath.sqrt(BigDecimal.TEN)));
    }

    // ----- positive: SIN, COS, TAN, ATN -----

    @Test
    public void testSinOne() throws Exception {
        assertEquals("0.8414709848078965066525023216302989996226", stored(BcdMath.sin(ONE)));
    }

    @Test
    public void testSinLargeArgumentIsReduced() throws Exception {
        assertEquals("-0.5440211108893698134047476618513772816836", stored(BcdMath.sin(BigDecimal.TEN)));
    }

    @Test
    public void testSinNegativeArgument() throws Exception {
        assertEquals("-0.6569865987187890903969990915936351779369",
                stored(BcdMath.sin(new BigDecimal("-7"))));
    }

    @Test
    public void testSinOfPiIsNearZero() throws Exception {
        // sin(PI) is zero in exact arithmetic. The result must be far smaller than any 40-digit value of size 1.
        BigDecimal oResult = BcdMath.sin(BcdMath.pi());
        assertTrue(oResult.abs().compareTo(new BigDecimal("1E-40")) < 0, "sin(PI) = " + oResult);
    }

    @Test
    public void testCosOne() throws Exception {
        assertEquals("0.5403023058681397174009366074429766037323", stored(BcdMath.cos(ONE)));
    }

    @Test
    public void testCosNegativeResult() throws Exception {
        assertEquals("-0.416146836547142386997568229500762189766", stored(BcdMath.cos(new BigDecimal("2"))));
    }

    @Test
    public void testCosLargeArgument() throws Exception {
        assertEquals("0.8623188722876839341019385139508425355101", stored(BcdMath.cos(new BigDecimal("100"))));
    }

    @Test
    public void testTanOne() throws Exception {
        assertEquals("1.557407724654902230506974807458360173087", stored(BcdMath.tan(ONE)));
    }

    @Test
    public void testTanHalf() throws Exception {
        assertEquals("0.5463024898437905132551794657802853832976", stored(BcdMath.tan(new BigDecimal("0.5"))));
    }

    @Test
    public void testAtanOne() throws Exception {
        assertEquals("0.7853981633974483096156608458198757210493", stored(BcdMath.atan(ONE)));
    }

    @Test
    public void testAtanSmall() throws Exception {
        assertEquals("0.4636476090008061162142562314612144020285", stored(BcdMath.atan(new BigDecimal("0.5"))));
    }

    @Test
    public void testAtanLarge() throws Exception {
        assertEquals("1.471127674303734591852875571761730851855", stored(BcdMath.atan(BigDecimal.TEN)));
    }

    @Test
    public void testAtanNegative() throws Exception {
        assertEquals("-1.249045772398254425829917077281090123078", stored(BcdMath.atan(new BigDecimal("-3"))));
    }

    // ----- positive: fractional power -----

    @Test
    public void testFractionalPowerSquareRoot() throws Exception {
        assertEquals("1.41421356237309504880168872420969807857",
                stored(BcdMath.power(new BigDecimal("2"), new BigDecimal("0.5"))));
    }

    @Test
    public void testFractionalPowerOneAndHalf() throws Exception {
        assertEquals("31.6227766016837933199889354443271853372",
                stored(BcdMath.power(BigDecimal.TEN, new BigDecimal("1.5"))));
    }

    @Test
    public void testFractionalPowerExact() throws Exception {
        assertEquals("3", stored(BcdMath.power(new BigDecimal("9"), new BigDecimal("0.5"))));
    }

    // ----- negative -----

    @Test
    public void testLnOfZeroIsReported() {
        assertThrows(SyntaxErrorException.class, () -> BcdMath.ln(BigDecimal.ZERO));
    }

    @Test
    public void testLnOfNegativeIsReported() {
        assertThrows(SyntaxErrorException.class, () -> BcdMath.ln(new BigDecimal("-1")));
    }

    @Test
    public void testLog10OfZeroIsReported() {
        assertThrows(SyntaxErrorException.class, () -> BcdMath.log10(BigDecimal.ZERO));
    }

    @Test
    public void testSqrOfNegativeIsReported() {
        assertThrows(SyntaxErrorException.class, () -> BcdMath.sqrt(new BigDecimal("-4")));
    }

    @Test
    public void testExpAboveRangeOverflows() {
        assertThrows(OverflowException.class, () -> BcdMath.exp(new BigDecimal("1001")));
    }

    @Test
    public void testFractionalPowerOfNegativeIsReported() {
        assertThrows(SyntaxErrorException.class,
                () -> BcdMath.power(new BigDecimal("-2"), new BigDecimal("0.5")));
    }

    @Test
    public void testZeroToNegativePowerIsReported() {
        assertThrows(DivideByZeroException.class,
                () -> BcdMath.power(BigDecimal.ZERO, new BigDecimal("-0.5")));
    }

    @Test
    public void testFractionalPowerOverflows() {
        assertThrows(OverflowException.class,
                () -> new BcdValue(BcdMath.power(BigDecimal.TEN, new BigDecimal("50.5"))));
    }
}
