package eu.gricom.interpreter.basic.variableTypes;

import eu.gricom.interpreter.basic.error.DivideByZeroException;
import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RealValueTest {

    @Test
    public void testToString() {RealValue oNumberValue = new RealValue(999);

        String strResult = oNumberValue.toString();
        assertTrue(strResult.matches("999.0"));
    }

    @Test
    public void testToReal() {
        RealValue oRealValue = new RealValue(999);

        double dResult = oRealValue.toReal();
        assertEquals(dResult, 999);
    }

    @Test
    public void testEvaluate() {
        RealValue oNumberValue = new RealValue(999);
        RealValue oNewValue = (RealValue) oNumberValue.evaluate();

        assertEquals(oNumberValue, oNewValue);
    }

    @Test
    public void testPlus() {
        try {
            RealValue oFirstValue = new RealValue(1);
            RealValue oSecondValue = new RealValue(2);

            RealValue oResultValue = (RealValue) oFirstValue.plus(oSecondValue);
            assertTrue(oResultValue.toReal() == 3.0);

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMinus() {
        try {
            RealValue oFirstValue = new RealValue(1);
            RealValue oSecondValue = new RealValue(2);

            RealValue oResultValue = (RealValue) oFirstValue.minus(oSecondValue);
            assertTrue(oResultValue.toReal() == -1.0);

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMultiply() {
        try {
            RealValue oFirstValue = new RealValue(1);
            RealValue oSecondValue = new RealValue(2);

            RealValue oResultValue = (RealValue) oFirstValue.multiply(oSecondValue);
            assertTrue(oResultValue.toReal() == 2.0);

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDivide() {
        try {
            RealValue oFirstValue = new RealValue(1);
            RealValue oSecondValue = new RealValue(2);

            RealValue oResultValue = (RealValue) oFirstValue.divide(oSecondValue);
            assertTrue(oResultValue.toReal() == 0.5);

            oSecondValue = new RealValue(0);
            oResultValue = (RealValue) oFirstValue.divide(oSecondValue);

            //this command should never been reached...
            assertTrue(false);
        } catch (SyntaxErrorException | DivideByZeroException e) {
            e.printStackTrace();
            assertTrue(true);
        }
    }
}
