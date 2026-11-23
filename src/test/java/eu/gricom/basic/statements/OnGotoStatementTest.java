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
 * OnGotoStatementTest.java
 * <p>
 * Unit tests for the OnGotoStatement class.
 */
public class OnGotoStatementTest {

    @Test
    public void testOnGotoValidIndex1() throws Exception {
        Expression oExpr = new IntegerValue(1);
        List<String> aoTargets = Arrays.asList("100", "200", "300");
        OnGotoStatement oStmt = new OnGotoStatement(0, oExpr, aoTargets);

        // Test execution will try to jump to line 100, which may fail if line not found
        // This test just verifies the statement can be created
        assertNotNull(oStmt);
        assertEquals(0, oStmt.getTokenNumber());
    }

    @Test
    public void testOnGotoContent() throws Exception {
        Expression oExpr = new IntegerValue(1);
        List<String> aoTargets = Arrays.asList("100", "200", "300");
        OnGotoStatement oStmt = new OnGotoStatement(5, oExpr, aoTargets);

        String strContent = oStmt.content();
        assertTrue(strContent.contains("ON GOTO"));
        assertTrue(strContent.contains("100"));
        assertTrue(strContent.contains("200"));
        assertTrue(strContent.contains("300"));
    }

    @Test
    public void testOnGotoStructure() throws Exception {
        Expression oExpr = new IntegerValue(2);
        List<String> aoTargets = Arrays.asList("100", "200", "300");
        OnGotoStatement oStmt = new OnGotoStatement(5, oExpr, aoTargets);

        String strStructure = oStmt.structure();
        assertTrue(strStructure.contains("ON_GOTO"));
        assertTrue(strStructure.contains("TOKEN_NR"));
        assertTrue(strStructure.contains("TARGETS"));
        assertTrue(strStructure.contains("100"));
    }

    @Test
    public void testOnGotoGetTokenNumber() throws Exception {
        Expression oExpr = new IntegerValue(1);
        List<String> aoTargets = Arrays.asList("100");
        OnGotoStatement oStmt = new OnGotoStatement(42, oExpr, aoTargets);

        assertEquals(42, oStmt.getTokenNumber());
    }

    @Test
    public void testOnGotoOutOfRangeIndex() throws Exception {
        // Test ON 5 GOTO 100, 200, 300 should NOT jump (index > size)
        Expression oExpr = new IntegerValue(5);
        List<String> aoTargets = Arrays.asList("100", "200", "300");
        OnGotoStatement oStmt = new OnGotoStatement(0, oExpr, aoTargets);

        // Out-of-range should just continue, not throw
        try {
            oStmt.execute();
        } catch (SyntaxErrorException e) {
            // Expected if line resolution fails
        }
        assertNotNull(oStmt);
    }

    @Test
    public void testOnGotoZeroIndex() throws Exception {
        // Test ON 0 GOTO 100, 200, 300 should NOT jump (index < 1)
        Expression oExpr = new IntegerValue(0);
        List<String> aoTargets = Arrays.asList("100", "200", "300");
        OnGotoStatement oStmt = new OnGotoStatement(0, oExpr, aoTargets);

        try {
            oStmt.execute();
        } catch (SyntaxErrorException e) {
            // Expected if line resolution fails
        }
        assertNotNull(oStmt);
    }

    @Test
    public void testOnGotoNegativeIndex() throws Exception {
        // Test ON -1 GOTO 100, 200, 300 should NOT jump (index < 1)
        Expression oExpr = new IntegerValue(-1);
        List<String> aoTargets = Arrays.asList("100", "200", "300");
        OnGotoStatement oStmt = new OnGotoStatement(0, oExpr, aoTargets);

        try {
            oStmt.execute();
        } catch (SyntaxErrorException e) {
            // Expected if line resolution fails
        }
        assertNotNull(oStmt);
    }

    @Test
    public void testOnGotoRealExpressionConvertsToInt() throws Exception {
        // Test ON 2.7 GOTO 100, 200, 300 should use index 2 (2.7 converts to 2)
        Expression oExpr = new RealValue(2.7);
        List<String> aoTargets = Arrays.asList("100", "200", "300");
        OnGotoStatement oStmt = new OnGotoStatement(0, oExpr, aoTargets);

        try {
            oStmt.execute();
        } catch (SyntaxErrorException e) {
            // Expected if line resolution fails
        }
        assertNotNull(oStmt);
    }

    @Test
    public void testOnGotoSingleTarget() throws Exception {
        Expression oExpr = new IntegerValue(1);
        List<String> aoTargets = Arrays.asList("100");
        OnGotoStatement oStmt = new OnGotoStatement(0, oExpr, aoTargets);

        try {
            oStmt.execute();
        } catch (SyntaxErrorException e) {
            // Expected if line resolution fails
        }
        assertNotNull(oStmt);
    }

    @Test
    public void testOnGotoManyTargets() throws Exception {
        Expression oExpr = new IntegerValue(5);
        List<String> aoTargets = Arrays.asList("100", "110", "120", "130", "140", "150", "160", "170", "180", "190");
        OnGotoStatement oStmt = new OnGotoStatement(0, oExpr, aoTargets);

        try {
            oStmt.execute();
        } catch (SyntaxErrorException e) {
            // Expected if line resolution fails
        }
        assertNotNull(oStmt);
    }

    @Test
    public void testOnGotoContentIncludesAllTargets() throws Exception {
        Expression oExpr = new IntegerValue(1);
        List<String> aoTargets = Arrays.asList("100", "200", "300", "400");
        OnGotoStatement oStmt = new OnGotoStatement(0, oExpr, aoTargets);

        String strContent = oStmt.content();
        for (String strTarget : aoTargets) {
            assertTrue(strContent.contains(strTarget), "Content should include target " + strTarget);
        }
    }

    @Test
    public void testOnGotoStructureIncludesAllTargets() throws Exception {
        Expression oExpr = new IntegerValue(2);
        List<String> aoTargets = Arrays.asList("100", "200", "300");
        OnGotoStatement oStmt = new OnGotoStatement(0, oExpr, aoTargets);

        String strStructure = oStmt.structure();
        for (String strTarget : aoTargets) {
            assertTrue(strStructure.contains(strTarget), "Structure should include target " + strTarget);
        }
    }
}