package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.VariableManagement;
import eu.gricom.basic.tokenizer.BasicTokenType;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.RealValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ArrayAssignStatementTest.java
 * <p>
 * Description: Unit tests for ArrayAssignStatement. Each test uses a unique variable name prefix
 * to avoid interference caused by VariableManagement static maps shared across all test instances.
 */
public class ArrayAssignStatementTest {

    /**
     * Verify assignment to an array element with a literal index.
     * s1N%(1) = 10 → key "s1N%-1" must hold 10.
     */
    @Test
    public void testLiteralIndex() throws Exception {
        ArrayAssignStatement oStmt = new ArrayAssignStatement(
                0, "s1N%", List.of(new RealValue(1.0)), new RealValue(10.0));
        oStmt.execute();

        VariableManagement oVarMgr = new VariableManagement();
        assertEquals(10, (int) oVarMgr.getMap("s1N%-1").toReal());
    }

    /**
     * Verify assignment to an array element with a simple variable index.
     * s2i% = 3, s2N%(s2i%) = 20 → key "s2N%-3" must hold 20.
     */
    @Test
    public void testVariableIndex() throws Exception {
        VariableManagement oVarMgr = new VariableManagement();
        oVarMgr.putMap("s2i%", new IntegerValue(3));

        ArrayAssignStatement oStmt = new ArrayAssignStatement(
                0, "s2N%", List.of(new VariableExpression("s2i%")), new RealValue(20.0));
        oStmt.execute();

        assertEquals(20, (int) oVarMgr.getMap("s2N%-3").toReal());
    }

    /**
     * Verify assignment to an array element with an addition expression index.
     * s3i% = 4, s3N%(s3i% + 1) = 30 → key "s3N%-5" must hold 30.
     */
    @Test
    public void testAdditionIndex() throws Exception {
        VariableManagement oVarMgr = new VariableManagement();
        oVarMgr.putMap("s3i%", new IntegerValue(4));

        Expression oIdx = new OperatorExpression(
                new VariableExpression("s3i%"), BasicTokenType.PLUS, new RealValue(1.0));
        ArrayAssignStatement oStmt = new ArrayAssignStatement(
                0, "s3N%", List.of(oIdx), new RealValue(30.0));
        oStmt.execute();

        assertEquals(30, (int) oVarMgr.getMap("s3N%-5").toReal());
    }

    /**
     * Verify assignment to an array element with a subtraction expression index.
     * s4i% = 2, s4N%(s4i% - 1) = 40 → key "s4N%-1" must hold 40.
     */
    @Test
    public void testSubtractionIndex() throws Exception {
        VariableManagement oVarMgr = new VariableManagement();
        oVarMgr.putMap("s4i%", new IntegerValue(2));

        Expression oIdx = new OperatorExpression(
                new VariableExpression("s4i%"), BasicTokenType.MINUS, new RealValue(1.0));
        ArrayAssignStatement oStmt = new ArrayAssignStatement(
                0, "s4N%", List.of(oIdx), new RealValue(40.0));
        oStmt.execute();

        assertEquals(40, (int) oVarMgr.getMap("s4N%-1").toReal());
    }

    /**
     * Verify assignment to an array element with a multiplication expression index.
     * s5i% = 3, s5N%(s5i% * 2) = 50 → key "s5N%-6" must hold 50.
     */
    @Test
    public void testMultiplicationIndex() throws Exception {
        VariableManagement oVarMgr = new VariableManagement();
        oVarMgr.putMap("s5i%", new IntegerValue(3));

        Expression oIdx = new OperatorExpression(
                new VariableExpression("s5i%"), BasicTokenType.MULTIPLY, new RealValue(2.0));
        ArrayAssignStatement oStmt = new ArrayAssignStatement(
                0, "s5N%", List.of(oIdx), new RealValue(50.0));
        oStmt.execute();

        assertEquals(50, (int) oVarMgr.getMap("s5N%-6").toReal());
    }

    /**
     * Verify assignment to a multi-dimensional array element with literal indices.
     * s6M%(1, 2) = 99 → key "s6M%-1,2" must hold 99.
     */
    @Test
    public void testMultiDimensionalLiteral() throws Exception {
        ArrayAssignStatement oStmt = new ArrayAssignStatement(
                0, "s6M%", List.of(new RealValue(1.0), new RealValue(2.0)), new RealValue(99.0));
        oStmt.execute();

        VariableManagement oVarMgr = new VariableManagement();
        assertEquals(99, (int) oVarMgr.getMap("s6M%-1,2").toReal());
    }

    /**
     * Verify assignment to a multi-dimensional array element with expression indices.
     * s7i% = 0, s7j% = 2
     * s7M%(s7i% + 1, s7j%) = 88 → key "s7M%-1,2" must hold 88.
     */
    @Test
    public void testMultiDimensionalExpr() throws Exception {
        VariableManagement oVarMgr = new VariableManagement();
        oVarMgr.putMap("s7i%", new IntegerValue(0));
        oVarMgr.putMap("s7j%", new IntegerValue(2));

        Expression oIdx0 = new OperatorExpression(
                new VariableExpression("s7i%"), BasicTokenType.PLUS, new RealValue(1.0));
        Expression oIdx1 = new VariableExpression("s7j%");
        ArrayAssignStatement oStmt = new ArrayAssignStatement(
                0, "s7M%", List.of(oIdx0, oIdx1), new RealValue(88.0));
        oStmt.execute();

        assertEquals(88, (int) oVarMgr.getMap("s7M%-1,2").toReal());
    }

    /**
     * Verify that writing to the same index twice keeps the second value.
     * s8N%(1) = 100, then s8N%(1) = 200 → key "s8N%-1" must hold 200.
     */
    @Test
    public void testOverwriteExisting() throws Exception {
        new ArrayAssignStatement(0, "s8N%", List.of(new RealValue(1.0)), new RealValue(100.0)).execute();
        new ArrayAssignStatement(0, "s8N%", List.of(new RealValue(1.0)), new RealValue(200.0)).execute();

        VariableManagement oVarMgr = new VariableManagement();
        assertEquals(200, (int) oVarMgr.getMap("s8N%-1").toReal());
    }
}
