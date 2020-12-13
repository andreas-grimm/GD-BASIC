package eu.gricom.interpreter.basic.helper;

import eu.gricom.interpreter.basic.statements.GotoStatement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LabelsTest {

    @Test
    public void testEvaluate() {
        MemoryManagement oMemoryManagement = new MemoryManagement();
        oMemoryManagement.putLabelStatement("TestCase", 5);

        try {
            GotoStatement oStatement = new GotoStatement("TestCase");

            oStatement.execute();

            int iNewLabel = oMemoryManagement.getCurrentStatement();

            assertTrue(iNewLabel == 5);
        } catch (Exception eException) {
            assertTrue(false);
        }
    }
}
