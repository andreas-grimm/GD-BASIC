package eu.gricom.basic.statements;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DimStatementTest.java
 * <p>
 * Description: Unit test for DimStatement class.
 */
public class DimStatementTest {

    @Test
    public void testGetTokenNumber() {
        DimStatement dimStatement = new DimStatement(10);
        assertEquals(10, dimStatement.getTokenNumber());
    }

    @Test
    public void testContent() {
        DimStatement dimStatement = new DimStatement(10);
        assertEquals("DIM", dimStatement.content());
    }

    @Test
    public void testStructure() throws Exception {
        DimStatement dimStatement = new DimStatement(10);
        String expected = "{\"DIM\": {\"TOKEN_NR\": \"10\", \"ARRAY_NAME\": \"\", \"SIZE\": \"0\"}}";
        assertEquals(expected, dimStatement.structure());
    }

    // Note: execute() calls System.exit(0), which would terminate the test runner.
    // It is not normally possible to unit test this without a security manager or similar,
    // which is deprecated in modern Java. For this reason, we avoid calling execute() 
    // in this test to prevent the test environment from shutting down.
}
