package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.variableTypes.BooleanValue;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class OperatorExpressionTest {

    @Test
    public void testEvaluate() {
        RealValue oLeftValue = new RealValue(2);
        RealValue oRightValue = new RealValue(1);

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, '=', oLeftValue);
            BooleanValue oResultValue = (BooleanValue) oExpression1.evaluate();
            assertEquals(oResultValue.isTrue(), true);
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, '+', oRightValue);
            RealValue oResultValue = (RealValue) oExpression1.evaluate();
            assertEquals(oResultValue.toReal(), 3.0);
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, '-', oRightValue);
            RealValue oResultValue = (RealValue) oExpression1.evaluate();
            assertEquals(oResultValue.toReal(), 1.0);
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, '/', oRightValue);
            RealValue oResultValue = (RealValue) oExpression1.evaluate();
            assertEquals(oResultValue.toReal(), 2.0);
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, '<', oRightValue);
            BooleanValue oResultValue = (BooleanValue) oExpression1.evaluate();
            assertEquals(oResultValue.isTrue(), false);
        } catch (Exception eException) {
            fail();
        }

        try {
            OperatorExpression oExpression1 = new OperatorExpression(oLeftValue, '>', oRightValue);
            BooleanValue oResultValue = (BooleanValue) oExpression1.evaluate();
            assertEquals(oResultValue.isTrue(), true);
        } catch (Exception eException) {
            fail();
        }
    }
}
