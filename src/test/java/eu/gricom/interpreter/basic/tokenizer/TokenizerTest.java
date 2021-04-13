package eu.gricom.interpreter.basic.tokenizer;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.helper.Logger;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class TokenizerTest {
    private static final Logger LOGGER = new Logger("eu.gricom.interpreter.basic.tokenizer.TokenizerTest");

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
}
