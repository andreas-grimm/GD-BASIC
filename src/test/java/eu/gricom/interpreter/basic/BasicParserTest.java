package eu.gricom.interpreter.basic;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.helper.FileHandler;
import eu.gricom.interpreter.basic.statements.Expression;
import eu.gricom.interpreter.basic.statements.OperatorExpression;
import eu.gricom.interpreter.basic.statements.VariableExpression;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertSame;

@SuppressWarnings("SpellCheckingInspection")
public class BasicParserTest {

    @Test
    public void testAtomicWord() throws SyntaxErrorException {
        String strReadText = FileHandler.readFile("src/test/resources/testfile.bas");
        List<Token> aoTokens = Tokenizer.tokenize(strReadText);

        BasicParser oParser = new BasicParser(aoTokens);
        Expression oExpression = oParser.atomic();

        String strExpression = oExpression.toString();
        strExpression = (strExpression.substring(0,strExpression.indexOf('@')));

        assertTrue(strExpression.matches("eu.gricom.interpreter.basic.statements.VariableExpression"));
    }

    @Test
    public void testAssignment() throws SyntaxErrorException {
        String strReadText = FileHandler.readFile("src/test/resources/testfile.bas");
        List<Token> aoTokens = Tokenizer.tokenize(strReadText);

        BasicParser oParser = new BasicParser(aoTokens);
        Expression oExpression = oParser.atomic();

        String strExpression = oExpression.toString();
        strExpression = (strExpression.substring(0,strExpression.indexOf('@')));

        assertTrue(strExpression.matches("eu.gricom.interpreter.basic.statements.VariableExpression"));

        // running the atomic method
        while ((oParser.matchNextToken(TokenType.OPERATOR)) || (oParser.matchNextToken(TokenType.EQUALS))) {
            char strOperator = oParser.lastToken(1).getText().charAt(0);
            Expression oRight = oParser.atomic();
            oExpression = new OperatorExpression(oExpression, (String.valueOf(strOperator)), oRight);
        }

        strExpression = oExpression.toString();

        strExpression = (strExpression.substring(0,strExpression.indexOf('@')));
        assertTrue(strExpression.matches("eu.gricom.interpreter.basic.statements.OperatorExpression"));

        OperatorExpression oOperatorExpression = (OperatorExpression)oExpression;
        VariableExpression oVariableExpression = (VariableExpression) oOperatorExpression.getLeft();

        // checking that the line is processed correctly...
        assertTrue(oVariableExpression.getName().matches("count"));
        assertTrue(oOperatorExpression.getOperator().matches("="));
        assertTrue(oOperatorExpression.getRight().toString().matches("5.0"));
    }

    @Test
    public void testMatchNextTokenType() {
        String strReadText = FileHandler.readFile("src/test/resources/testfile.bas");
        List<Token> aoTokens = Tokenizer.tokenize(strReadText);

        BasicParser oParser = new BasicParser(aoTokens);
        oParser.setPosition(33);

        assertTrue(oParser.matchNextToken(TokenType.LABEL));
    }

    @Test
    public void testMatchNextTwoTokenType() {
        String strReadText = FileHandler.readFile("src/test/resources/testfile.bas");
        List<Token> aoTokens = Tokenizer.tokenize(strReadText);

        BasicParser oParser = new BasicParser(aoTokens);
        oParser.setPosition(5);

        assertTrue(oParser.matchNextTwoToken(TokenType.WORD, TokenType.EQUALS));
    }

    @Test
    public void testMatchNextTokenName() {
        String strReadText = FileHandler.readFile("src/test/resources/testfile.bas");
        List<Token> aoTokens = Tokenizer.tokenize(strReadText);

        BasicParser oParser = new BasicParser(aoTokens);
        oParser.setPosition(0);

        assertTrue(oParser.matchNextToken("count"));
        assertFalse(oParser.matchNextToken("="));

        oParser.setPosition(0);
        assertFalse(oParser.matchNextToken("not here"));
    }

    @Test
    public void testConsumeTokenType() {
        String strReadText = FileHandler.readFile("src/test/resources/testfile.bas");
        List<Token> aoTokens = Tokenizer.tokenize(strReadText);

        BasicParser oParser = new BasicParser(aoTokens);
        oParser.setPosition(33);

        Token oToken = oParser.consumeToken(TokenType.LABEL);
        assertSame(oToken.getType(), TokenType.LABEL);
    }

    @Test
    public void testConsumeTokenName() {
        String strReadText = FileHandler.readFile("src/test/resources/testfile.bas");
        List<Token> aoTokens = Tokenizer.tokenize(strReadText);

        BasicParser oParser = new BasicParser(aoTokens);
        oParser.setPosition(24);

        Token oToken = oParser.consumeToken("count");
    }

    @Test
    public void testLastToken() {
        String strReadText = FileHandler.readFile("src/test/resources/testfile.bas");
        List<Token> aoTokens = Tokenizer.tokenize(strReadText);

        BasicParser oParser = new BasicParser(aoTokens);
        oParser.setPosition(34);

        Token oToken;

        oToken = oParser.lastToken(1);
        assertTrue(oToken.getType().toString().matches("LABEL"));
    }

    @Test
    public void testGetToken() {
        String strReadText = FileHandler.readFile("src/test/resources/testfile.bas");
        List<Token> aoTokens = Tokenizer.tokenize(strReadText);

        BasicParser oParser = new BasicParser(aoTokens);

        Token oToken;

        oToken = oParser.getToken(33);
        assertTrue(oToken.getType().toString().matches("LABEL"));

        oToken = oParser.getToken(34);
        assertTrue(oToken.getType().toString().matches("LINE"));
    }
}
