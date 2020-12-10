package eu.gricom.interpreter.basic.statements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class OperatorExpressionTest {

    @Test
    public void testEvaluate() {
        NumberValue oLeftValue = new NumberValue(2);
        NumberValue oRightValue = new NumberValue(1);

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, '=', oLeftValue);
            NumberValue oResultValue = (NumberValue) oExpression1.evaluate();
            assertTrue(oResultValue.toNumber() == 1.0);
        } catch (Exception eException) {
            assertTrue(false);
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, '+', oRightValue);
            NumberValue oResultValue = (NumberValue) oExpression1.evaluate();
            assertTrue(oResultValue.toNumber() == 3.0);
        } catch (Exception eException) {
            assertTrue(false);
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, '-', oRightValue);
            NumberValue oResultValue = (NumberValue) oExpression1.evaluate();
            assertTrue(oResultValue.toNumber() == 1.0);
        } catch (Exception eException) {
            assertTrue(false);
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, '/', oRightValue);
            NumberValue oResultValue = (NumberValue) oExpression1.evaluate();
            assertTrue(oResultValue.toNumber() == 2.0);
        } catch (Exception eException) {
            assertTrue(false);
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, '<', oRightValue);
            NumberValue oResultValue = (NumberValue) oExpression1.evaluate();
            assertFalse(oResultValue.toNumber() == 1.0);
        } catch (Exception eException) {
            assertTrue(false);
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, '>', oRightValue);
            NumberValue oResultValue = (NumberValue) oExpression1.evaluate();
            assertTrue(oResultValue.toNumber() == 1.0);
        } catch (Exception eException) {
            assertTrue(false);
        }
    }
}
