package eu.gricom.interpreter.basic.statements;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LabelStatementTest {
    private LabelStatement oLabelStatement = new LabelStatement();

    @Test
    @Order(1)
    public void testPutLabelStatement() {
        oLabelStatement.putLabelStatement("TestCase", 5);
    }

    @Test
    @Order(2)
    public void testContainsKey() {
        boolean bResult;
        oLabelStatement.putLabelStatement("TestCase", 5);

        LabelStatement oNewLabelStatement = new LabelStatement();

        bResult = oNewLabelStatement.containsLabelKey("TestCase");
        assertTrue(bResult);

        bResult = oNewLabelStatement.containsLabelKey("TestCase2");
        assertFalse(bResult);
    }

    @Test
    @Order(3)
    public void testGetLabelStatement() {
        LabelStatement oNewLabelStatement = new LabelStatement();
        oLabelStatement.putLabelStatement("TestCase", 5);

        int iResult = oLabelStatement.getLabelStatement("TestCase");
        assertEquals(iResult, 5);
    }
}
