package eu.gricom.basic.functions;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.variabletypes.BcdValue;
import eu.gricom.basic.variabletypes.IntegerValue;
import eu.gricom.basic.variabletypes.RealValue;
import eu.gricom.basic.variabletypes.StringValue;
import eu.gricom.basic.variabletypes.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * BcdFunctionsTest.java
 * <p>
 * Description: Tests that the BASIC maths functions take BCD ('#') arguments and return BCD results (decision D12),
 * and that CINT and CDBL convert between BCD and INTEGER or REAL. Each function is called through its execute method,
 * as the Function class does.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class BcdFunctionsTest {

    @Test
    public void testAbsOfBcdIsBcd() throws Exception {
        Value oResult = Abs.execute(new BcdValue("-2.5"));
        assertTrue(oResult instanceof BcdValue);
        assertEquals("2.5", oResult.toString());
    }

    @Test
    public void testSqrOfBcdIsBcd() throws Exception {
        Value oResult = Sqr.execute(new BcdValue("2"));
        assertTrue(oResult instanceof BcdValue);
        assertEquals("1.41421356237309504880168872420969807857", oResult.toString());
    }

    @Test
    public void testSqrOfNegativeBcdIsReported() throws Exception {
        assertThrows(SyntaxErrorException.class, () -> Sqr.execute(new BcdValue("-4")));
    }

    @Test
    public void testExpOfBcdIsBcd() throws Exception {
        assertEquals("2.718281828459045235360287471352662497757", Exp.execute(new BcdValue("1")).toString());
    }

    @Test
    public void testLogOfBcdIsBcd() throws Exception {
        assertEquals("2.302585092994045684017991454684364207601", Log.execute(new BcdValue("10")).toString());
    }

    @Test
    public void testLog10OfBcdIsBcd() throws Exception {
        assertEquals("3", Log10.execute(new BcdValue("1000")).toString());
    }

    @Test
    public void testSinOfBcdIsBcd() throws Exception {
        assertEquals("0.8414709848078965066525023216302989996226", Sin.execute(new BcdValue("1")).toString());
    }

    @Test
    public void testCosOfBcdIsBcd() throws Exception {
        assertEquals("0.5403023058681397174009366074429766037323", Cos.execute(new BcdValue("1")).toString());
    }

    @Test
    public void testTanOfBcdIsBcd() throws Exception {
        assertEquals("1.557407724654902230506974807458360173087", Tan.execute(new BcdValue("1")).toString());
    }

    @Test
    public void testAtnOfBcdIsBcd() throws Exception {
        assertEquals("0.7853981633974483096156608458198757210493", Atn.execute(new BcdValue("1")).toString());
    }

    @Test
    public void testCintOfBcdTruncatesToInteger() throws Exception {
        Value oResult = Cint.execute(new BcdValue("3.9"));
        assertTrue(oResult instanceof IntegerValue);
        assertEquals(3, ((IntegerValue) oResult).toInt());
    }

    @Test
    public void testCdblOfBcdIsReal() throws Exception {
        Value oResult = Cdbl.execute(new BcdValue("0.25"));
        assertTrue(oResult instanceof RealValue);
        assertEquals(0.25, oResult.toReal(), 0.0000001);
    }

    @Test
    public void testStrOfBcdIsPlainTextWithoutTrailingZeros() throws Exception {
        Value oResult = Str.execute(new BcdValue("2.50"));
        assertTrue(oResult instanceof StringValue);
        assertEquals("2.5", oResult.toString());
    }

    @Test
    public void testRealArgumentStillUsesDoublePath() throws Exception {
        // Unchanged behaviour for untyped variables: SQR of a REAL is a REAL
        Value oResult = Sqr.execute(new RealValue(4.0));
        assertTrue(oResult instanceof RealValue);
        assertEquals(2.0, oResult.toReal(), 0.0000001);
    }
}
