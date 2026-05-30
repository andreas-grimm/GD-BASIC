package eu.gricom.basic.statements;

import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * FPutStatementTest.java
 *
 * Description: Unit tests for the FPutStatement class.
 * Tests writing character/data to file without adding newline.
 */
public class FPutStatementTest {

    @Test
    public void testFPutStatement_WithStringExpression_CreatesStatement() {
        int iTokenNumber = 10;
        int iFileId = 1;
        Expression oExpression = new StringValue("X");

        FPutStatement oStatement = new FPutStatement(iTokenNumber, iFileId, oExpression);

        assertNotNull(oStatement);
        assertEquals(iTokenNumber, oStatement.getTokenNumber());
    }

    @Test
    public void testFPutStatement_WithMultiCharacterString_CreatesStatement() {
        int iTokenNumber = 20;
        int iFileId = 2;
        Expression oExpression = new StringValue("Hello");

        FPutStatement oStatement = new FPutStatement(iTokenNumber, iFileId, oExpression);

        assertNotNull(oStatement);
        assertEquals(iTokenNumber, oStatement.getTokenNumber());
    }

    @Test
    public void testFPutStatement_WithEmptyString_CreatesStatement() {
        int iTokenNumber = 30;
        int iFileId = 3;
        Expression oExpression = new StringValue("");

        FPutStatement oStatement = new FPutStatement(iTokenNumber, iFileId, oExpression);

        assertNotNull(oStatement);
        assertEquals(iTokenNumber, oStatement.getTokenNumber());
    }

    @Test
    public void testFPutStatement_WithSpecialCharacters_CreatesStatement() {
        int iTokenNumber = 40;
        int iFileId = 4;
        Expression oExpression = new StringValue("!@#$%");

        FPutStatement oStatement = new FPutStatement(iTokenNumber, iFileId, oExpression);

        assertNotNull(oStatement);
        assertEquals(iTokenNumber, oStatement.getTokenNumber());
    }

    @Test
    public void testFPutStatement_WithNewlineCharacter_CreatesStatement() {
        int iTokenNumber = 50;
        int iFileId = 5;
        Expression oExpression = new StringValue("\n");

        FPutStatement oStatement = new FPutStatement(iTokenNumber, iFileId, oExpression);

        assertNotNull(oStatement);
        assertEquals(iTokenNumber, oStatement.getTokenNumber());
    }

    @Test
    public void testFPutStatement_WithTabCharacter_CreatesStatement() {
        int iTokenNumber = 60;
        int iFileId = 6;
        Expression oExpression = new StringValue("\t");

        FPutStatement oStatement = new FPutStatement(iTokenNumber, iFileId, oExpression);

        assertNotNull(oStatement);
        assertEquals(iTokenNumber, oStatement.getTokenNumber());
    }

    @Test
    public void testFPutStatement_WithLongString_CreatesStatement() {
        int iTokenNumber = 70;
        int iFileId = 7;
        String strLongContent = "X".repeat(10000);
        Expression oExpression = new StringValue(strLongContent);

        FPutStatement oStatement = new FPutStatement(iTokenNumber, iFileId, oExpression);

        assertNotNull(oStatement);
        assertEquals(iTokenNumber, oStatement.getTokenNumber());
    }

    @Test
    public void testFPutStatement_WithUnicodeString_CreatesStatement() {
        int iTokenNumber = 80;
        int iFileId = 8;
        Expression oExpression = new StringValue("αβγδε");

        FPutStatement oStatement = new FPutStatement(iTokenNumber, iFileId, oExpression);

        assertNotNull(oStatement);
        assertEquals(iTokenNumber, oStatement.getTokenNumber());
    }

    @Test
    public void testFPutStatement_WithNumericString_CreatesStatement() {
        int iTokenNumber = 90;
        int iFileId = 9;
        Expression oExpression = new StringValue("123456");

        FPutStatement oStatement = new FPutStatement(iTokenNumber, iFileId, oExpression);

        assertNotNull(oStatement);
        assertEquals(iTokenNumber, oStatement.getTokenNumber());
    }

    @Test
    public void testFPutStatement_WithPathString_CreatesStatement() {
        int iTokenNumber = 100;
        int iFileId = 10;
        Expression oExpression = new StringValue("/path/to/file.txt");

        FPutStatement oStatement = new FPutStatement(iTokenNumber, iFileId, oExpression);

        assertNotNull(oStatement);
        assertEquals(iTokenNumber, oStatement.getTokenNumber());
    }
}
