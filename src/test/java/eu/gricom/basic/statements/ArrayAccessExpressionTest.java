package eu.gricom.basic.statements;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.memoryManager.VariableManagement;
import eu.gricom.basic.tokenizer.BasicTokenType;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.RealValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * ArrayAccessExpressionTest.java
 * <p>
 * Description: Unit tests for ArrayAccessExpression. Each test uses a unique variable name prefix
 * to avoid interference caused by VariableManagement static maps shared across all test instances.
 */
public class ArrayAccessExpressionTest {

    /**
     * Verify that a literal integer expression resolves to the correct array element.
     * Index: RealValue(2) → stored key "t1arr%-2"
     */
    @Test
    public void testLiteralIndex() throws Exception {
        VariableManagement oVarMgr = new VariableManagement();
        oVarMgr.putMap("t1arr%-2", new IntegerValue(99));

        ArrayAccessExpression oExpr = new ArrayAccessExpression("t1arr%", List.of(new RealValue(2.0)));

        assertEquals(99, (int) oExpr.evaluate().toReal());
    }

    /**
     * Verify that a simple variable as the index resolves to the correct array element.
     * t2i% = 3 → stored key "t2arr%-3"
     */
    @Test
    public void testVariableIndex() throws Exception {
        VariableManagement oVarMgr = new VariableManagement();
        oVarMgr.putMap("t2i%", new IntegerValue(3));
        oVarMgr.putMap("t2arr%-3", new IntegerValue(77));

        Expression oIdx = new VariableExpression("t2i%");
        ArrayAccessExpression oExpr = new ArrayAccessExpression("t2arr%", List.of(oIdx));

        assertEquals(77, (int) oExpr.evaluate().toReal());
    }

    /**
     * Verify that an addition expression as the index resolves to the correct array element.
     * t3i% = 4, index = t3i% + 1 = 5 → stored key "t3arr%-5"
     */
    @Test
    public void testAdditionIndex() throws Exception {
        VariableManagement oVarMgr = new VariableManagement();
        oVarMgr.putMap("t3i%", new IntegerValue(4));
        oVarMgr.putMap("t3arr%-5", new IntegerValue(55));

        Expression oIdx = new OperatorExpression(
                new VariableExpression("t3i%"), BasicTokenType.PLUS, new RealValue(1.0));
        ArrayAccessExpression oExpr = new ArrayAccessExpression("t3arr%", List.of(oIdx));

        assertEquals(55, (int) oExpr.evaluate().toReal());
    }

    /**
     * Verify that a subtraction expression as the index resolves to the correct array element.
     * t4i% = 4, index = t4i% - 1 = 3 → stored key "t4arr%-3"
     */
    @Test
    public void testSubtractionIndex() throws Exception {
        VariableManagement oVarMgr = new VariableManagement();
        oVarMgr.putMap("t4i%", new IntegerValue(4));
        oVarMgr.putMap("t4arr%-3", new IntegerValue(44));

        Expression oIdx = new OperatorExpression(
                new VariableExpression("t4i%"), BasicTokenType.MINUS, new RealValue(1.0));
        ArrayAccessExpression oExpr = new ArrayAccessExpression("t4arr%", List.of(oIdx));

        assertEquals(44, (int) oExpr.evaluate().toReal());
    }

    /**
     * Verify that a multiplication expression as the index resolves to the correct array element.
     * t5i% = 3, index = t5i% * 2 = 6 → stored key "t5arr%-6"
     */
    @Test
    public void testMultiplicationIndex() throws Exception {
        VariableManagement oVarMgr = new VariableManagement();
        oVarMgr.putMap("t5i%", new IntegerValue(3));
        oVarMgr.putMap("t5arr%-6", new IntegerValue(33));

        Expression oIdx = new OperatorExpression(
                new VariableExpression("t5i%"), BasicTokenType.MULTIPLY, new RealValue(2.0));
        ArrayAccessExpression oExpr = new ArrayAccessExpression("t5arr%", List.of(oIdx));

        assertEquals(33, (int) oExpr.evaluate().toReal());
    }

    /**
     * Verify that two literal indices resolve to the correct multi-dimensional array element.
     * Indices: (1, 2) → stored key "t6m%-1,2"
     */
    @Test
    public void testMultiDimensionalLiteral() throws Exception {
        VariableManagement oVarMgr = new VariableManagement();
        oVarMgr.putMap("t6m%-1,2", new IntegerValue(22));

        ArrayAccessExpression oExpr = new ArrayAccessExpression("t6m%",
                List.of(new RealValue(1.0), new RealValue(2.0)));

        assertEquals(22, (int) oExpr.evaluate().toReal());
    }

    /**
     * Verify that expression indices resolve to the correct multi-dimensional array element.
     * t7i% = 0, t7j% = 1
     * index 0: t7i% + 1 = 1  index 1: t7j% * 2 = 2 → stored key "t7m%-1,2"
     */
    @Test
    public void testMultiDimensionalExpr() throws Exception {
        VariableManagement oVarMgr = new VariableManagement();
        oVarMgr.putMap("t7i%", new IntegerValue(0));
        oVarMgr.putMap("t7j%", new IntegerValue(1));
        oVarMgr.putMap("t7m%-1,2", new IntegerValue(11));

        Expression oIdx0 = new OperatorExpression(
                new VariableExpression("t7i%"), BasicTokenType.PLUS, new RealValue(1.0));
        Expression oIdx1 = new OperatorExpression(
                new VariableExpression("t7j%"), BasicTokenType.MULTIPLY, new RealValue(2.0));
        ArrayAccessExpression oExpr = new ArrayAccessExpression("t7m%", List.of(oIdx0, oIdx1));

        assertEquals(11, (int) oExpr.evaluate().toReal());
    }

    /**
     * Verify that accessing a non-existent array element throws SyntaxErrorException.
     */
    @Test
    public void testMissingElementThrows() {
        ArrayAccessExpression oExpr = new ArrayAccessExpression("t8arr%", List.of(new RealValue(99.0)));

        assertThrows(SyntaxErrorException.class, oExpr::evaluate);
    }
}
