package eu.gricom.basic.statements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the FCloseStatement class.
 * Tests are based on the Statement interface specification and FCloseStatement's public API.
 */
public class FCloseStatementTest {

    // -------------------------------------------------------------------------
    // getTokenNumber()
    // -------------------------------------------------------------------------

    @Test
    public void testGetTokenNumber_ReturnsConstructorValue() {
        FCloseStatement oStatement = new FCloseStatement(42, 1, false);

        assertEquals(42, oStatement.getTokenNumber());
    }

    @Test
    public void testGetTokenNumber_WithZero_ReturnsZero() {
        FCloseStatement oStatement = new FCloseStatement(0, 1, false);

        assertEquals(0, oStatement.getTokenNumber());
    }

    @Test
    public void testGetTokenNumber_WithNegative_ReturnsNegative() {
        FCloseStatement oStatement = new FCloseStatement(-1, 1, false);

        assertEquals(-1, oStatement.getTokenNumber());
    }

    // -------------------------------------------------------------------------
    // content()
    // -------------------------------------------------------------------------

    @Test
    public void testContent_ReturnsFCLOSE() throws Exception {
        FCloseStatement oStatement = new FCloseStatement(1, 1, false);

        assertEquals("FCLOSE", oStatement.content());
    }

    @Test
    public void testContent_WithDeleteTrue_ReturnsFCLOSE() throws Exception {
        FCloseStatement oStatement = new FCloseStatement(1, 1, true);

        assertEquals("FCLOSE", oStatement.content());
    }

    // -------------------------------------------------------------------------
    // structure()
    // -------------------------------------------------------------------------

    @Test
    public void testStructure_ContainsTokenNumber() throws Exception {
        FCloseStatement oStatement = new FCloseStatement(100, 1, false);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"TOKEN_NR\": \"100\""));
    }

    @Test
    public void testStructure_ContainsFileId() throws Exception {
        FCloseStatement oStatement = new FCloseStatement(1, 7, false);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FILE_ID\": \"7\""));
    }

    @Test
    public void testStructure_WithDeleteFalse_ContainsFALSE() throws Exception {
        FCloseStatement oStatement = new FCloseStatement(1, 1, false);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FILE_DELETE\": \"FALSE\""));
    }

    @Test
    public void testStructure_WithDeleteTrue_ContainsTRUE() throws Exception {
        FCloseStatement oStatement = new FCloseStatement(1, 1, true);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("\"FILE_DELETE\": \"TRUE\""));
    }

    @Test
    public void testStructure_StartsWithFcloseKey() throws Exception {
        FCloseStatement oStatement = new FCloseStatement(1, 1, false);

        String strStructure = oStatement.structure();

        assertTrue(strStructure.contains("{\"FCLOSE\": {"));
    }

    // -------------------------------------------------------------------------
    // execute()
    // -------------------------------------------------------------------------

    @Test
    public void testExecute_WithNonExistentFileId_DoesNotThrow() throws Exception {
        FCloseStatement oStatement = new FCloseStatement(1, 999, false);

        oStatement.execute();
    }

    @Test
    public void testExecute_WithDeleteFalse_DoesNotThrow() throws Exception {
        FCloseStatement oStatement = new FCloseStatement(1, 1, false);

        oStatement.execute();
    }

    @Test
    public void testExecute_WithDeleteTrue_DoesNotThrow() throws Exception {
        FCloseStatement oStatement = new FCloseStatement(1, 2, true);

        oStatement.execute();
    }
}
