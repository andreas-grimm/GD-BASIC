package eu.gricom.basic.statements;

import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * FRenameStatementTest.java
 *
 * Description: Unit tests for the FRenameStatement class.
 * Tests renaming a file identified by its file ID.
 */
public class FRenameStatementTest {

    @Test
    public void testFRenameStatement_WithSimpleNewName_CreatesStatement() {
        int iTokenNumber = 10;
        int iFileId = 1;
        StringValue oNewFileName = new StringValue("newfile.txt");

        FRenameStatement oStatement = new FRenameStatement(iTokenNumber, iFileId, oNewFileName);

        assertNotNull(oStatement);
        assertEquals(iTokenNumber, oStatement.getTokenNumber());
    }

    @Test
    public void testFRenameStatement_WithPathNewName_CreatesStatement() {
        int iTokenNumber = 20;
        int iFileId = 2;
        StringValue oNewFileName = new StringValue("/path/to/newfile.txt");

        FRenameStatement oStatement = new FRenameStatement(iTokenNumber, iFileId, oNewFileName);

        assertNotNull(oStatement);
        assertEquals(iTokenNumber, oStatement.getTokenNumber());
    }

    @Test
    public void testFRenameStatement_WithDifferentExtension_CreatesStatement() {
        int iTokenNumber = 30;
        int iFileId = 3;
        StringValue oNewFileName = new StringValue("oldname.bas");

        FRenameStatement oStatement = new FRenameStatement(iTokenNumber, iFileId, oNewFileName);

        assertNotNull(oStatement);
        assertEquals(iTokenNumber, oStatement.getTokenNumber());
    }

    @Test
    public void testFRenameStatement_WithNoExtension_CreatesStatement() {
        int iTokenNumber = 40;
        int iFileId = 4;
        StringValue oNewFileName = new StringValue("newfilename");

        FRenameStatement oStatement = new FRenameStatement(iTokenNumber, iFileId, oNewFileName);

        assertNotNull(oStatement);
        assertEquals(iTokenNumber, oStatement.getTokenNumber());
    }

    @Test
    public void testFRenameStatement_WithUpperCaseName_CreatesStatement() {
        int iTokenNumber = 50;
        int iFileId = 5;
        StringValue oNewFileName = new StringValue("NEWFILE.TXT");

        FRenameStatement oStatement = new FRenameStatement(iTokenNumber, iFileId, oNewFileName);

        assertNotNull(oStatement);
        assertEquals(iTokenNumber, oStatement.getTokenNumber());
    }

    @Test
    public void testFRenameStatement_WithMixedCaseName_CreatesStatement() {
        int iTokenNumber = 60;
        int iFileId = 6;
        StringValue oNewFileName = new StringValue("NewFile.Txt");

        FRenameStatement oStatement = new FRenameStatement(iTokenNumber, iFileId, oNewFileName);

        assertNotNull(oStatement);
        assertEquals(iTokenNumber, oStatement.getTokenNumber());
    }

    @Test
    public void testFRenameStatement_WithSpecialCharactersInName_CreatesStatement() {
        int iTokenNumber = 70;
        int iFileId = 7;
        StringValue oNewFileName = new StringValue("newfile-2024.txt");

        FRenameStatement oStatement = new FRenameStatement(iTokenNumber, iFileId, oNewFileName);

        assertNotNull(oStatement);
        assertEquals(iTokenNumber, oStatement.getTokenNumber());
    }

    @Test
    public void testFRenameStatement_WithUnderscoreName_CreatesStatement() {
        int iTokenNumber = 80;
        int iFileId = 8;
        StringValue oNewFileName = new StringValue("new_file_name.txt");

        FRenameStatement oStatement = new FRenameStatement(iTokenNumber, iFileId, oNewFileName);

        assertNotNull(oStatement);
        assertEquals(iTokenNumber, oStatement.getTokenNumber());
    }

    @Test
    public void testFRenameStatement_WithDotPrefixedName_CreatesStatement() {
        int iTokenNumber = 90;
        int iFileId = 9;
        StringValue oNewFileName = new StringValue(".hiddenfile");

        FRenameStatement oStatement = new FRenameStatement(iTokenNumber, iFileId, oNewFileName);

        assertNotNull(oStatement);
        assertEquals(iTokenNumber, oStatement.getTokenNumber());
    }

    @Test
    public void testFRenameStatement_WithLongFileName_CreatesStatement() {
        int iTokenNumber = 100;
        int iFileId = 10;
        String strLongName = "verylongfilename" + "X".repeat(200) + ".txt";
        StringValue oNewFileName = new StringValue(strLongName);

        FRenameStatement oStatement = new FRenameStatement(iTokenNumber, iFileId, oNewFileName);

        assertNotNull(oStatement);
        assertEquals(iTokenNumber, oStatement.getTokenNumber());
    }
}
