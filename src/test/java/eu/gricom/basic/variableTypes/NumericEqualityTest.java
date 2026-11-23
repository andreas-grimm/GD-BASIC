package eu.gricom.basic.variabletypes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * NumericEqualityTest.java
 * <p>
 * Description: Equality (==) and not-equal (!=) between numeric types. Untyped number literals are REAL, so
 * "IF count% != 10" compares an INTEGER with a REAL. These comparisons are numeric (bug fixed in Phase 6); before the
 * fix they raised "is not an integer". Ordering operators already accepted mixed numeric types.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class NumericEqualityTest {

    @Test
    public void testIntegerEqualsRealLiteral() throws Exception {
        BooleanValue oResult = (BooleanValue) new IntegerValue(10).equals(new RealValue(10.0));
        assertTrue(oResult.toBoolean());
    }

    @Test
    public void testIntegerNotEqualsRealLiteralWhenDifferent() throws Exception {
        BooleanValue oResult = (BooleanValue) new IntegerValue(10).notEqual(new RealValue(11.0));
        assertTrue(oResult.toBoolean());
    }

    @Test
    public void testIntegerNotEqualsRealLiteralWhenSame() throws Exception {
        BooleanValue oResult = (BooleanValue) new IntegerValue(10).notEqual(new RealValue(10.0));
        assertFalse(oResult.toBoolean());
    }

    @Test
    public void testRealEqualsIntegerNumerically() throws Exception {
        BooleanValue oResult = (BooleanValue) new RealValue(3.0).equals(new IntegerValue(3));
        assertTrue(oResult.toBoolean());
    }

    @Test
    public void testRealWithFractionIsNotEqualToInteger() throws Exception {
        BooleanValue oResult = (BooleanValue) new RealValue(3.5).equals(new IntegerValue(3));
        assertFalse(oResult.toBoolean());
    }

    @Test
    public void testLongEqualsRealAndInteger() throws Exception {
        assertTrue(((BooleanValue) new LongValue(7L).equals(new RealValue(7.0))).toBoolean());
        assertTrue(((BooleanValue) new LongValue(7L).equals(new IntegerValue(7))).toBoolean());
        assertFalse(((BooleanValue) new LongValue(7L).notEqual(new RealValue(7.0))).toBoolean());
    }

    @Test
    public void testIntegerEqualsStringStillFails() {
        org.junit.jupiter.api.Assertions.assertThrows(eu.gricom.basic.error.SyntaxErrorException.class,
                () -> new IntegerValue(1).equals(new StringValue("1")));
    }

}
