package eu.gricom.basic.parser;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.helper.FileHandler;
import eu.gricom.basic.statements.Expression;
import eu.gricom.basic.tokenizer.BasicLexer;
import eu.gricom.basic.tokenizer.Lexer;
import eu.gricom.basic.tokenizer.Token;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("SpellCheckingInspection")
public class BasicParserTest {

    @Test
    public void testAtomicWord() throws SyntaxErrorException {
        Lexer oTokenizer = new BasicLexer();

        String strReadText = FileHandler.readFile("src/test/basic/test_basic_Q1_unittest.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);

        BasicParser oParser = new BasicParser(aoTokens, true);
        Expression oExpression = oParser.atomic();

        String strExpression = oExpression.toString();
        strExpression = (strExpression.substring(0, strExpression.indexOf('@')));

        assertTrue(strExpression.matches("eu.gricom.basic.statements.VariableExpression"));
    }

    /*
    @Test
    public void testAssignment() throws SyntaxErrorException {
        Lexer oTokenizer = new BasicLexer();

        String strReadText = FileHandler.readFile("src/test/resources/test_basic_Q1_unittest.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);

        BasicParser oParser = new BasicParser(aoTokens);
        Expression oExpression = oParser.atomic();

        String strExpression = oExpression.toString();
        strExpression = (strExpression.substring(0,strExpression.indexOf('@')));

        assertTrue(strExpression.matches("VariableExpression"));

        // running the atomic method
        while ((oParser.matchNextToken(BasicTokenType.OPERATOR)) || (oParser.matchNextToken(BasicTokenType.EQUALS))) {
            char strOperator = oParser.lastToken(1).getText().charAt(0);
            Expression oRight = oParser.atomic();
            oExpression = new OperatorExpression(oExpression, (String.valueOf(strOperator)), oRight);
        }

        strExpression = oExpression.toString();

        strExpression = (strExpression.substring(0,strExpression.indexOf('@')));
        assertTrue(strExpression.matches("OperatorExpression"));

        OperatorExpression oOperatorExpression = (OperatorExpression)oExpression;
        VariableExpression oVariableExpression = (VariableExpression) oOperatorExpression.getLeft();

        // checking that the line is processed correctly...
        assertTrue(oVariableExpression.getName().matches("count"));
        assertTrue(oOperatorExpression.getOperator().matches("="));
        assertTrue(oOperatorExpression.getRight().toString().matches("5.0"));
    }

    @Test
    public void testMatchNextTokenType() {
        Lexer oTokenizer = new JasicLexer();

        String strReadText = FileHandler.readFile("src/test/resources/test_jasic_1.jas");

        try {
            List<Token> aoTokens = oTokenizer.tokenize(strReadText);

            JasicParser oParser = new JasicParser(aoTokens);
            oParser.setPosition(33);

            assertTrue(oParser.matchNextToken(BasicTokenType.LABEL));
        } catch (SyntaxErrorException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testMatchNextTwoTokenType() {
        Lexer oTokenizer = new JasicLexer();

        String strReadText = FileHandler.readFile("src/test/resources/test_jasic_1.jas");

        try {
            List<Token> aoTokens = oTokenizer.tokenize(strReadText);

            JasicParser oParser = new JasicParser(aoTokens);
            oParser.setPosition(5);

            assertTrue(oParser.matchNextTwoToken(BasicTokenType.WORD, BasicTokenType.EQUALS));
        } catch (SyntaxErrorException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testMatchNextTokenName() {
        Lexer oTokenizer = new JasicLexer();

        String strReadText = FileHandler.readFile("src/test/resources/test_jasic_1.jas");
        try {
            List<Token> aoTokens = oTokenizer.tokenize(strReadText);

            JasicParser oParser = new JasicParser(aoTokens);
            oParser.setPosition(0);

            assertTrue(oParser.matchNextToken("count"));
            assertFalse(oParser.matchNextToken("="));

            oParser.setPosition(0);
            assertFalse(oParser.matchNextToken("not here"));
        } catch (SyntaxErrorException e) {
            assertTrue(false);
        }

    }

    @Test
    public void testConsumeTokenType() {
        Lexer oTokenizer = new JasicLexer();

        String strReadText = FileHandler.readFile("src/test/resources/test_jasic_1.jas");

        try {
            List<Token> aoTokens = oTokenizer.tokenize(strReadText);

            JasicParser oParser = new JasicParser(aoTokens);
            oParser.setPosition(33);

            Token oToken = oParser.consumeToken(BasicTokenType.LABEL);
            assertSame(oToken.getType(), BasicTokenType.LABEL);
        } catch (SyntaxErrorException e) {
            assertTrue(false);
        }

    }

    @Test
    public void testConsumeTokenName() {
        Lexer oTokenizer = new JasicLexer();

        String strReadText = FileHandler.readFile("src/test/resources/test_jasic_1.jas");

        try {
            List<Token> aoTokens = oTokenizer.tokenize(strReadText);

            JasicParser oParser = new JasicParser(aoTokens);
            oParser.setPosition(24);

            Token oToken = oParser.consumeToken("count");
        } catch (SyntaxErrorException e) {
            assertTrue(false);
        }

    }

    @Test
    public void testLastToken() {
        Lexer oTokenizer = new JasicLexer();

        String strReadText = FileHandler.readFile("src/test/resources/test_jasic_1.jas");

        try {
            List<Token> aoTokens = oTokenizer.tokenize(strReadText);

            JasicParser oParser = new JasicParser(aoTokens);
            oParser.setPosition(34);

            Token oToken;

            oToken = oParser.lastToken(1);
            assertTrue(oToken.getType().toString().matches("LABEL"));
        } catch (SyntaxErrorException e) {
            assertTrue(false);
        }

    }

    @Test
    public void testGetToken() {
        Lexer oTokenizer = new JasicLexer();

        String strReadText = FileHandler.readFile("src/test/resources/test_jasic_1.jas");

        try {
            List<Token> aoTokens = oTokenizer.tokenize(strReadText);

            JasicParser oParser = new JasicParser(aoTokens);

            Token oToken;

            oToken = oParser.getToken(33);
            assertTrue(oToken.getType().toString().matches("LABEL"));

            oToken = oParser.getToken(34);
            assertTrue(oToken.getType().toString().matches("LINE"));
        } catch (SyntaxErrorException e) {
            assertTrue(false);
        }

    }

 */
}
