package eu.gricom.basic.tokenizer;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.helper.Logger;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class TokenizerTest {
    private static final Logger LOGGER = new Logger("TokenizerTest");

    @Test
    public void testTokenizeSimpleLine() {
        try {
            LOGGER.setLogLevel("");

            String strProgramLine = "10 PRINT A$";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            assertEquals(aoTokens.size(), 2);

            Token oToken = aoTokens.get(0);

            assertEquals(10, oToken.getLine());
            assertEquals("PRINT", oToken.getText());
            assertEquals(BasicTokenType.PRINT, oToken.getType());

            oToken = aoTokens.get(1);

            assertEquals(10, oToken.getLine());
            assertEquals("A$", oToken.getText());
            assertEquals(BasicTokenType.WORD, oToken.getType());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testTokenizeBrokenSequence() {
        LOGGER.setLogLevel("");

        String strProgramLine = "20 PRINT A$\n10 REM Error Test";
        Lexer oTokenizer = new BasicLexer();

        assertThrows(SyntaxErrorException.class, () -> {
            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);
            System.out.println(aoTokens.toString());
        });
    }

    @Test
    public void testTokenizeAssignmentWithParenthesis() {
        try {

            LOGGER.setLogLevel("");

            String strProgramLine = "10 A# = 4 * ( 2 + 1 )";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            assertEquals(aoTokens.size(), 9);

            Token oToken = aoTokens.get(0);

            assertEquals(10, oToken.getLine());
            assertEquals("A#", oToken.getText());
            assertEquals(BasicTokenType.WORD, oToken.getType());

            oToken = aoTokens.get(1);

            assertEquals(10, oToken.getLine());
            assertEquals("=", oToken.getText());
            assertEquals(BasicTokenType.ASSIGN_EQUAL, oToken.getType());

            oToken = aoTokens.get(2);

            assertEquals(10, oToken.getLine());
            assertEquals("4", oToken.getText());
            assertEquals(BasicTokenType.NUMBER, oToken.getType());

            oToken = aoTokens.get(3);

            assertEquals(10, oToken.getLine());
            assertEquals("*", oToken.getText());
            assertEquals(BasicTokenType.MULTIPLY, oToken.getType());

            oToken = aoTokens.get(4);

            assertEquals(10, oToken.getLine());
            assertEquals("(", oToken.getText());
            assertEquals(BasicTokenType.LEFT_PAREN, oToken.getType());

            oToken = aoTokens.get(5);

            assertEquals(10, oToken.getLine());
            assertEquals("2", oToken.getText());
            assertEquals(BasicTokenType.NUMBER, oToken.getType());

            oToken = aoTokens.get(6);

            assertEquals(10, oToken.getLine());
            assertEquals("+", oToken.getText());
            assertEquals(BasicTokenType.PLUS, oToken.getType());

            oToken = aoTokens.get(7);

            assertEquals(10, oToken.getLine());
            assertEquals("1", oToken.getText());
            assertEquals(BasicTokenType.NUMBER, oToken.getType());

            oToken = aoTokens.get(8);

            assertEquals(10, oToken.getLine());
            assertEquals(")", oToken.getText());
            assertEquals(BasicTokenType.RIGHT_PAREN, oToken.getType());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTokenizeAssignmentWithOutParenthesis() {
        try {

            LOGGER.setLogLevel("");

            String strProgramLine = "10 A# = 4 * 2 + 1";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            assertEquals(aoTokens.size(), 7);

            Token oToken = aoTokens.get(0);

            assertEquals(10, oToken.getLine());
            assertEquals("A#", oToken.getText());
            assertEquals(BasicTokenType.WORD, oToken.getType());

            oToken = aoTokens.get(1);

            assertEquals(10, oToken.getLine());
            assertEquals("=", oToken.getText());
            assertEquals(BasicTokenType.ASSIGN_EQUAL, oToken.getType());

            oToken = aoTokens.get(2);

            assertEquals(10, oToken.getLine());
            assertEquals("4", oToken.getText());
            assertEquals(BasicTokenType.NUMBER, oToken.getType());

            oToken = aoTokens.get(3);

            assertEquals(10, oToken.getLine());
            assertEquals("*", oToken.getText());
            assertEquals(BasicTokenType.MULTIPLY, oToken.getType());

            oToken = aoTokens.get(4);

            assertEquals(10, oToken.getLine());
            assertEquals("2", oToken.getText());
            assertEquals(BasicTokenType.NUMBER, oToken.getType());

            oToken = aoTokens.get(5);

            assertEquals(10, oToken.getLine());
            assertEquals("+", oToken.getText());
            assertEquals(BasicTokenType.PLUS, oToken.getType());

            oToken = aoTokens.get(6);

            assertEquals(10, oToken.getLine());
            assertEquals("1", oToken.getText());
            assertEquals(BasicTokenType.NUMBER, oToken.getType());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTokenizeAssignmentWithFunctionCallNoParam() {
        try {

            LOGGER.setLogLevel("");

            String strProgramLine = "10 A% = MEM()";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            assertEquals(aoTokens.size(), 3);

            Token oToken = aoTokens.get(0);

            assertEquals(10, oToken.getLine());
            assertEquals("A%", oToken.getText());
            assertEquals(BasicTokenType.WORD, oToken.getType());

            oToken = aoTokens.get(1);

            assertEquals(10, oToken.getLine());
            assertEquals("=", oToken.getText());
            assertEquals(BasicTokenType.ASSIGN_EQUAL, oToken.getType());

            oToken = aoTokens.get(2);

            assertEquals(10, oToken.getLine());
            assertEquals("MEM", oToken.getText());
            assertEquals(BasicTokenType.MEM, oToken.getType());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTokenizeAssignmentWithFunctionCall() {
        try {

            LOGGER.setLogLevel("");

            String strProgramLine = "10 A% = ABS(-1)";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            assertEquals(aoTokens.size(), 6);

            Token oToken = aoTokens.get(0);

            assertEquals(10, oToken.getLine());
            assertEquals("A%", oToken.getText());
            assertEquals(BasicTokenType.WORD, oToken.getType());

            oToken = aoTokens.get(1);

            assertEquals(10, oToken.getLine());
            assertEquals("=", oToken.getText());
            assertEquals(BasicTokenType.ASSIGN_EQUAL, oToken.getType());

            oToken = aoTokens.get(2);

            assertEquals(10, oToken.getLine());
            assertEquals("ABS", oToken.getText());
            assertEquals(BasicTokenType.ABS, oToken.getType());

            oToken = aoTokens.get(3);

            assertEquals(10, oToken.getLine());
            assertEquals("(", oToken.getText());
            assertEquals(BasicTokenType.LEFT_PAREN, oToken.getType());

            oToken = aoTokens.get(4);

            assertEquals(10, oToken.getLine());
            assertEquals("-1", oToken.getText());
            assertEquals(BasicTokenType.NUMBER, oToken.getType());

            oToken = aoTokens.get(5);

            assertEquals(10, oToken.getLine());
            assertEquals(")", oToken.getText());
            assertEquals(BasicTokenType.RIGHT_PAREN, oToken.getType());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
        }
    }

    // ============================================================================
    // NEW TESTS: File I/O operations
    // ============================================================================

    @Test
    public void testTokenizeFileOpenOperation() {
        try {
            LOGGER.setLogLevel("");

            String strProgramLine = "10 F% = FOPEN(\"test.txt\")";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            assertEquals(6, aoTokens.size());

            Token oToken = aoTokens.get(2);
            assertEquals("FOPEN", oToken.getText());
            assertEquals(BasicTokenType.FOPEN, oToken.getType());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testTokenizeFileCloseOperation() {
        try {
            LOGGER.setLogLevel("");

            String strProgramLine = "20 FCLOSE(F%)";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            Token oToken = aoTokens.get(0);
            assertEquals("FCLOSE", oToken.getText());
            assertEquals(BasicTokenType.FCLOSE, oToken.getType());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testTokenizeFileInputOperation() {
        try {
            LOGGER.setLogLevel("");

            String strProgramLine = "30 FINPUT(F%, S$)";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            Token oToken = aoTokens.get(0);
            assertEquals("FINPUT", oToken.getText());
            assertEquals(BasicTokenType.FINPUT, oToken.getType());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testTokenizeFileExistsOperation() {
        try {
            LOGGER.setLogLevel("");

            String strProgramLine = "40 IF FEXISTS(\"file.txt\") THEN";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            Token oToken = aoTokens.get(1);
            assertEquals("FEXISTS", oToken.getText());
            assertEquals(BasicTokenType.FEXISTS, oToken.getType());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testTokenizeFileGetSizeOperation() {
        try {
            LOGGER.setLogLevel("");

            String strProgramLine = "50 SIZE% = FGETSIZE(F%)";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            Token oToken = aoTokens.get(2);
            assertEquals("FGETSIZE", oToken.getText());
            assertEquals(BasicTokenType.FGETSIZE, oToken.getType());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testTokenizeFileCompareOperation() {
        try {
            LOGGER.setLogLevel("");

            String strProgramLine = "60 RESULT% = FCOMPARE(\"file1.txt\", \"file2.txt\")";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            Token oToken = aoTokens.get(2);
            assertEquals("FCOMPARE", oToken.getText());
            assertEquals(BasicTokenType.FCOMPARE, oToken.getType());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testTokenizeFileCopyOperation() {
        try {
            LOGGER.setLogLevel("");

            String strProgramLine = "70 FCOPY(\"source.txt\", \"dest.txt\")";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            Token oToken = aoTokens.get(0);
            assertEquals("FCOPY", oToken.getText());
            assertEquals(BasicTokenType.FCOPY, oToken.getType());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testTokenizeFileRenameOperation() {
        try {
            LOGGER.setLogLevel("");

            String strProgramLine = "80 FRENAME(\"old.txt\", \"new.txt\")";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            Token oToken = aoTokens.get(0);
            assertEquals("FRENAME", oToken.getText());
            assertEquals(BasicTokenType.FRENAME, oToken.getType());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    // ============================================================================
    // NEW TESTS: Directory operations
    // ============================================================================

    @Test
    public void testTokenizeChangeDirectoryOperation() {
        try {
            LOGGER.setLogLevel("");

            String strProgramLine = "90 CHDIR(\"/path/to/dir\")";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            Token oToken = aoTokens.get(0);
            assertEquals("CHDIR", oToken.getText());
            assertEquals(BasicTokenType.CHDIR, oToken.getType());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testTokenizeDirectoryExistsOperation() {
        try {
            LOGGER.setLogLevel("");

            String strProgramLine = "100 IF DIREXISTS(\"/tmp\") THEN";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            Token oToken = aoTokens.get(1);
            assertEquals("DIREXISTS", oToken.getText());
            assertEquals(BasicTokenType.DIREXISTS, oToken.getType());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testTokenizeGetCurrentWorkingDirectoryOperation() {
        try {
            LOGGER.setLogLevel("");

            String strProgramLine = "110 PATH$ = GETCWD()";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            Token oToken = aoTokens.get(2);
            assertEquals("GETCWD", oToken.getText());
            assertEquals(BasicTokenType.GETCWD, oToken.getType());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testTokenizeListDirectoryOperation() {
        try {
            LOGGER.setLogLevel("");

            String strProgramLine = "120 FILES$ = LISTDIR(\"/data\")";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            Token oToken = aoTokens.get(2);
            assertEquals("LISTDIR", oToken.getText());
            assertEquals(BasicTokenType.LISTDIRECTORY, oToken.getType());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testTokenizeRemoveDirectoryOperation() {
        try {
            LOGGER.setLogLevel("");

            String strProgramLine = "130 RMDIR(\"empty_dir\")";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            Token oToken = aoTokens.get(0);
            assertEquals("RMDIR", oToken.getText());
            assertEquals(BasicTokenType.RMDIR, oToken.getType());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testTokenizeMultipleFileOperations() {
        try {
            LOGGER.setLogLevel("");

            String strProgramLine = "140 FPUT(F1%, \"data\") : FGET(F2%, S$)";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            // Find FPUT token
            boolean foundFput = false;
            boolean foundFget = false;

            for (Token token : aoTokens) {
                if (token.getText().equals("FPUT") && token.getType() == BasicTokenType.FPUT) {
                    foundFput = true;
                }
                if (token.getText().equals("FGET") && token.getType() == BasicTokenType.FGET) {
                    foundFget = true;
                }
            }

            assertEquals(true, foundFput, "FPUT token not found");
            assertEquals(true, foundFget, "FGET token not found");

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testTokenizeFileOperationsCaseInsensitive() {
        try {
            LOGGER.setLogLevel("");

            String strProgramLine = "150 fopen(\"test.txt\") : FOPEN(\"test.txt\") : FopeN(\"test.txt\")";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            int fopenCount = 0;
            for (Token token : aoTokens) {
                if (token.getType() == BasicTokenType.FOPEN) {
                    fopenCount++;
                }
            }

            assertEquals(3, fopenCount, "All case variations of FOPEN should be recognized");

        } catch (SyntaxErrorException e) {
            fail();
        }
    }
}
