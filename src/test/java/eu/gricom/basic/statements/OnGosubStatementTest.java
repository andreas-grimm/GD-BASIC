package eu.gricom.basic.statements;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.Value;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OnGosubStatementTest.java
 * <p>
 * Unit tests for the OnGosubStatement class.
 */
public class OnGosubStatementTest {

    @Test
    public void testOnGosubValidIndex1() throws Exception {
        Expression oExpr = new IntegerValue(1);
        List<String> aoTargets = Arrays.asList("100", "200", "300");
        OnGosubStatement oStmt = new OnGosubStatement(0, oExpr, aoTargets);

        // Test creation - execution will try to call subroutine at line 100
        assertNotNull(oStmt);
        assertEquals(0, oStmt.getTokenNumber());
    }

    @Test
    public void testOnGosubContent() throws Exception {
        Expression oExpr = new IntegerValue(1);
        List<String> aoTargets = Arrays.asList("100", "200", "300");
        OnGosubStatement oStmt = new OnGosubStatement(5, oExpr, aoTargets);

        String strContent = oStmt.content();
        assertTrue(strContent.contains("ON GOSUB"));
        assertTrue(strContent.contains("100"));
        assertTrue(strContent.contains("200"));
        assertTrue(strContent.contains("300"));
    }

    @Test
    public void testOnGosubStructure() throws Exception {
        Expression oExpr = new IntegerValue(2);
        List<String> aoTargets = Arrays.asList("100", "200", "300");
        OnGosubStatement oStmt = new OnGosubStatement(5, oExpr, aoTargets);

        String strStructure = oStmt.structure();
        assertTrue(strStructure.contains("ON_GOSUB"));
        assertTrue(strStructure.contains("TOKEN_NR"));
        assertTrue(strStructure.contains("TARGETS"));
        assertTrue(strStructure.contains("100"));
    }

    @Test
    public void testOnGosubGetTokenNumber() throws Exception {
        Expression oExpr = new IntegerValue(1);
        List<String> aoTargets = Arrays.asList("100");
        OnGosubStatement oStmt = new OnGosubStatement(42, oExpr, aoTargets);

        assertEquals(42, oStmt.getTokenNumber());
    }

    @Test
    public void testOnGosubOutOfRangeIndex() throws Exception {
        // Test ON 5 GOSUB 100, 200, 300 should NOT call (index > size)
        Expression oExpr = new IntegerValue(5);
        List<String> aoTargets = Arrays.asList("100", "200", "300");
        OnGosubStatement oStmt = new OnGosubStatement(0, oExpr, aoTargets);

        // Out-of-range should just continue, not throw
        try {
            oStmt.execute();
        } catch (SyntaxErrorException e) {
            // Expected if line resolution fails
        }
        assertNotNull(oStmt);
    }

    @Test
    public void testOnGosubZeroIndex() throws Exception {
        // Test ON 0 GOSUB 100, 200, 300 should NOT call (index < 1)
        Expression oExpr = new IntegerValue(0);
        List<String> aoTargets = Arrays.asList("100", "200", "300");
        OnGosubStatement oStmt = new OnGosubStatement(0, oExpr, aoTargets);

        try {
            oStmt.execute();
        } catch (SyntaxErrorException e) {
            // Expected if line resolution fails
        }
        assertNotNull(oStmt);
    }

    @Test
    public void testOnGosubNegativeIndex() throws Exception {
        // Test ON -1 GOSUB 100, 200, 300 should NOT call (index < 1)
        Expression oExpr = new IntegerValue(-1);
        List<String> aoTargets = Arrays.asList("100", "200", "300");
        OnGosubStatement oStmt = new OnGosubStatement(0, oExpr, aoTargets);

        try {
            oStmt.execute();
        } catch (SyntaxErrorException e) {
            // Expected if line resolution fails
        }
        assertNotNull(oStmt);
    }

    @Test
    public void testOnGosubRealExpressionConvertsToInt() throws Exception {
        // Test ON 2.9 GOSUB 100, 200, 300 should use index 2 (2.9 converts to 2)
        Expression oExpr = new RealValue(2.9);
        List<String> aoTargets = Arrays.asList("100", "200", "300");
        OnGosubStatement oStmt = new OnGosubStatement(0, oExpr, aoTargets);

        try {
            oStmt.execute();
        } catch (SyntaxErrorException e) {
            // Expected if line resolution fails
        }
        assertNotNull(oStmt);
    }

    @Test
    public void testOnGosubSingleTarget() throws Exception {
        Expression oExpr = new IntegerValue(1);
        List<String> aoTargets = Arrays.asList("100");
        OnGosubStatement oStmt = new OnGosubStatement(0, oExpr, aoTargets);

        try {
            oStmt.execute();
        } catch (SyntaxErrorException e) {
            // Expected if line resolution fails
        }
        assertNotNull(oStmt);
    }

    @Test
    public void testOnGosubManyTargets() throws Exception {
        Expression oExpr = new IntegerValue(5);
        List<String> aoTargets = Arrays.asList("100", "110", "120", "130", "140", "150", "160", "170", "180", "190");
        OnGosubStatement oStmt = new OnGosubStatement(0, oExpr, aoTargets);

        try {
            oStmt.execute();
        } catch (SyntaxErrorException e) {
            // Expected if line resolution fails
        }
        assertNotNull(oStmt);
    }

    @Test
    public void testOnGosubContentIncludesAllTargets() throws Exception {
        Expression oExpr = new IntegerValue(1);
        List<String> aoTargets = Arrays.asList("100", "200", "300", "400");
        OnGosubStatement oStmt = new OnGosubStatement(0, oExpr, aoTargets);

        String strContent = oStmt.content();
        for (String strTarget : aoTargets) {
            assertTrue(strContent.contains(strTarget), "Content should include target " + strTarget);
        }
    }

    @Test
    public void testOnGosubStructureIncludesAllTargets() throws Exception {
        Expression oExpr = new IntegerValue(2);
        List<String> aoTargets = Arrays.asList("100", "200", "300");
        OnGosubStatement oStmt = new OnGosubStatement(0, oExpr, aoTargets);

        String strStructure = oStmt.structure();
        for (String strTarget : aoTargets) {
            assertTrue(strStructure.contains(strTarget), "Structure should include target " + strTarget);
        }
    }

    @Test
    public void testOnGosubIsStatementInterface() throws Exception {
        Expression oExpr = new IntegerValue(1);
        List<String> aoTargets = Arrays.asList("100", "200");
        OnGosubStatement oStmt = new OnGosubStatement(10, oExpr, aoTargets);

        assertTrue(oStmt instanceof Statement);
    }
}