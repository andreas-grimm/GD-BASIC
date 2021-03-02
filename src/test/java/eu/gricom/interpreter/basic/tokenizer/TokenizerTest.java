package eu.gricom.interpreter.basic.tokenizer;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.helper.FileHandler;
import eu.gricom.interpreter.basic.helper.Logger;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class TokenizerTest {
    private static final Logger LOGGER = new Logger("eu.gricom.interpreter.basic.tokenizer.TokenizerTest");

    @Test
    public void testTokenizeForJasic() {
        // disable the logger
        try {
            LOGGER.setLogLevel("");

            String strReadText = FileHandler.readFile("src/test/resources/test_jasic_1.jas");
            Lexer oTokenizer = new JasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strReadText);

            assertEquals(aoTokens.size(), 35);
        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testTokenizeForBasicSimpleLine() {
        try {
            LOGGER.setLogLevel("");

            String strProgramLine = "10 PRINT A$";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            assertEquals(aoTokens.size(), 2);

            Token oToken = aoTokens.get(0);

            assertEquals(10, oToken.getLine());
            assertEquals("PRINT", oToken.getText());
            assertEquals(TokenType.PRINT, oToken.getType());

            oToken = aoTokens.get(1);

            assertEquals(10, oToken.getLine());
            assertEquals("A$", oToken.getText());
            assertEquals(TokenType.WORD, oToken.getType());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testTokenizeForBasicBrokenSequence() {
        LOGGER.setLogLevel("");

        String strProgramLine = "20 PRINT A$\n10 REM Error Test";
        Lexer oTokenizer = new BasicLexer();

        assertThrows(SyntaxErrorException.class, () -> {
            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);
            System.out.println(aoTokens.toString());
        });
    }

    @Test
    public void testTokenizeForBasicAssignmentWithParenthesis() {
        try {

            LOGGER.setLogLevel("");

            String strProgramLine = "10 A# = 4 * ( 2 + 1 )";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            assertEquals(aoTokens.size(), 9);

            Token oToken = aoTokens.get(0);

            assertEquals(10, oToken.getLine());
            assertEquals("A#", oToken.getText());
            assertEquals(TokenType.WORD, oToken.getType());

            oToken = aoTokens.get(1);

            assertEquals(10, oToken.getLine());
            assertEquals("=", oToken.getText());
            assertEquals(TokenType.ASSIGN_EQUAL, oToken.getType());

            oToken = aoTokens.get(2);

            assertEquals(10, oToken.getLine());
            assertEquals("4", oToken.getText());
            assertEquals(TokenType.NUMBER, oToken.getType());

            oToken = aoTokens.get(3);

            assertEquals(10, oToken.getLine());
            assertEquals("*", oToken.getText());
            assertEquals(TokenType.MULTIPLY, oToken.getType());

            oToken = aoTokens.get(4);

            assertEquals(10, oToken.getLine());
            assertEquals("(", oToken.getText());
            assertEquals(TokenType.LEFT_PAREN, oToken.getType());

            oToken = aoTokens.get(5);

            assertEquals(10, oToken.getLine());
            assertEquals("2", oToken.getText());
            assertEquals(TokenType.NUMBER, oToken.getType());

            oToken = aoTokens.get(6);

            assertEquals(10, oToken.getLine());
            assertEquals("+", oToken.getText());
            assertEquals(TokenType.PLUS, oToken.getType());

            oToken = aoTokens.get(7);

            assertEquals(10, oToken.getLine());
            assertEquals("1", oToken.getText());
            assertEquals(TokenType.NUMBER, oToken.getType());

            oToken = aoTokens.get(8);

            assertEquals(10, oToken.getLine());
            assertEquals(")", oToken.getText());
            assertEquals(TokenType.RIGHT_PAREN, oToken.getType());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTokenizeForBasicAssignmentWithOutParenthesis() {
        try {

            LOGGER.setLogLevel("");

            String strProgramLine = "10 A# = 4 * 2 + 1";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            assertEquals(aoTokens.size(), 7);

            Token oToken = aoTokens.get(0);

            assertEquals(10, oToken.getLine());
            assertEquals("A#", oToken.getText());
            assertEquals(TokenType.WORD, oToken.getType());

            oToken = aoTokens.get(1);

            assertEquals(10, oToken.getLine());
            assertEquals("=", oToken.getText());
            assertEquals(TokenType.ASSIGN_EQUAL, oToken.getType());

            oToken = aoTokens.get(2);

            assertEquals(10, oToken.getLine());
            assertEquals("4", oToken.getText());
            assertEquals(TokenType.NUMBER, oToken.getType());

            oToken = aoTokens.get(3);

            assertEquals(10, oToken.getLine());
            assertEquals("*", oToken.getText());
            assertEquals(TokenType.MULTIPLY, oToken.getType());

            oToken = aoTokens.get(4);

            assertEquals(10, oToken.getLine());
            assertEquals("2", oToken.getText());
            assertEquals(TokenType.NUMBER, oToken.getType());

            oToken = aoTokens.get(5);

            assertEquals(10, oToken.getLine());
            assertEquals("+", oToken.getText());
            assertEquals(TokenType.PLUS, oToken.getType());

            oToken = aoTokens.get(6);

            assertEquals(10, oToken.getLine());
            assertEquals("1", oToken.getText());
            assertEquals(TokenType.NUMBER, oToken.getType());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTokenizeForBasicAssignmentWithFunctionCallNoParam() {
        try {

            LOGGER.setLogLevel("");

            String strProgramLine = "10 A% = MEM()";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            assertEquals(aoTokens.size(), 3);

            Token oToken = aoTokens.get(0);

            assertEquals(10, oToken.getLine());
            assertEquals("A%", oToken.getText());
            assertEquals(TokenType.WORD, oToken.getType());

            oToken = aoTokens.get(1);

            assertEquals(10, oToken.getLine());
            assertEquals("=", oToken.getText());
            assertEquals(TokenType.ASSIGN_EQUAL, oToken.getType());

            oToken = aoTokens.get(2);

            assertEquals(10, oToken.getLine());
            assertEquals("MEM", oToken.getText());
            assertEquals(TokenType.MEM, oToken.getType());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTokenizeForBasicAssignmentWithFunctionCall() {
        try {

            LOGGER.setLogLevel("");

            String strProgramLine = "10 A% = ABS(-1)";
            Lexer oTokenizer = new BasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strProgramLine);

            assertEquals(aoTokens.size(), 6);

            Token oToken = aoTokens.get(0);

            assertEquals(10, oToken.getLine());
            assertEquals("A%", oToken.getText());
            assertEquals(TokenType.WORD, oToken.getType());

            oToken = aoTokens.get(1);

            assertEquals(10, oToken.getLine());
            assertEquals("=", oToken.getText());
            assertEquals(TokenType.ASSIGN_EQUAL, oToken.getType());

            oToken = aoTokens.get(2);

            assertEquals(10, oToken.getLine());
            assertEquals("ABS", oToken.getText());
            assertEquals(TokenType.ABS, oToken.getType());

            oToken = aoTokens.get(3);

            assertEquals(10, oToken.getLine());
            assertEquals("(", oToken.getText());
            assertEquals(TokenType.LEFT_PAREN, oToken.getType());

            oToken = aoTokens.get(4);

            assertEquals(10, oToken.getLine());
            assertEquals("-1", oToken.getText());
            assertEquals(TokenType.NUMBER, oToken.getType());

            oToken = aoTokens.get(5);

            assertEquals(10, oToken.getLine());
            assertEquals(")", oToken.getText());
            assertEquals(TokenType.RIGHT_PAREN, oToken.getType());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
        }
    }
}
