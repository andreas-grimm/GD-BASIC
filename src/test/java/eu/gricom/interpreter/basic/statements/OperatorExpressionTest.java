package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.variableTypes.BooleanValue;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class OperatorExpressionTest {

    @Test
    public void testEvaluateReal() {
        RealValue oLeftValue = new RealValue(2);
        RealValue oRightValue = new RealValue(1);

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, "==", oLeftValue);
            BooleanValue oResultValue = (BooleanValue) oExpression1.evaluate();
            assertTrue(oResultValue.isTrue());
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, "!=", oRightValue);
            BooleanValue oResultValue = (BooleanValue) oExpression1.evaluate();
            assertTrue(oResultValue.isTrue());
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, "+", oRightValue);
            RealValue oResultValue = (RealValue) oExpression1.evaluate();
            assertEquals(oResultValue.toReal(), 3.0);
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, "-", oRightValue);
            RealValue oResultValue = (RealValue) oExpression1.evaluate();
            assertEquals(oResultValue.toReal(), 1.0);
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, "/", oRightValue);
            RealValue oResultValue = (RealValue) oExpression1.evaluate();
            assertEquals(oResultValue.toReal(), 2.0);
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, "*", oRightValue);
            RealValue oResultValue = (RealValue) oExpression1.evaluate();
            assertEquals(oResultValue.toReal(), 2.0);
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, "<", oRightValue);
            BooleanValue oResultValue = (BooleanValue) oExpression1.evaluate();
            assertFalse(oResultValue.isTrue());
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, "<=", oRightValue);
            BooleanValue oResultValue = (BooleanValue) oExpression1.evaluate();
            assertFalse(oResultValue.isTrue());
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, ">", oRightValue);
            BooleanValue oResultValue = (BooleanValue) oExpression1.evaluate();
            assertTrue(oResultValue.isTrue());
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, ">=", oRightValue);
            BooleanValue oResultValue = (BooleanValue) oExpression1.evaluate();
            assertTrue(oResultValue.isTrue());
        } catch (Exception eException) {
            fail();
        }
    }

    @Test
    public void testEvaluateInteger() {
        IntegerValue oLeftValue = new IntegerValue(2);
        IntegerValue oRightValue = new IntegerValue(1);

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, "==", oLeftValue);
            BooleanValue oResultValue = (BooleanValue) oExpression1.evaluate();
            assertTrue(oResultValue.isTrue());
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, "!=", oRightValue);
            BooleanValue oResultValue = (BooleanValue) oExpression1.evaluate();
            assertTrue(oResultValue.isTrue());
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, "+", oRightValue);
            IntegerValue oResultValue = (IntegerValue) oExpression1.evaluate();
            assertEquals(oResultValue.toInt(), 3);
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, "-", oRightValue);
            IntegerValue oResultValue = (IntegerValue) oExpression1.evaluate();
            assertEquals(oResultValue.toInt(), 1);
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, "/", oRightValue);
            IntegerValue oResultValue = (IntegerValue) oExpression1.evaluate();
            assertEquals(oResultValue.toInt(), 2);
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, "*", oRightValue);
            IntegerValue oResultValue = (IntegerValue) oExpression1.evaluate();
            assertEquals(oResultValue.toInt(), 2);
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, "<", oRightValue);
            BooleanValue oResultValue = (BooleanValue) oExpression1.evaluate();
            assertFalse(oResultValue.isTrue());
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, "<=", oRightValue);
            BooleanValue oResultValue = (BooleanValue) oExpression1.evaluate();
            assertFalse(oResultValue.isTrue());
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, ">", oRightValue);
            BooleanValue oResultValue = (BooleanValue) oExpression1.evaluate();
            assertTrue(oResultValue.isTrue());
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, ">=", oRightValue);
            BooleanValue oResultValue = (BooleanValue) oExpression1.evaluate();
            assertTrue(oResultValue.isTrue());
        } catch (Exception eException) {
            fail();
        }
    }
}
