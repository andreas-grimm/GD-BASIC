package eu.gricom.basic.parser;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.helper.FileHandler;
import eu.gricom.basic.memoryManager.Program;
import eu.gricom.basic.runtimeManager.Execute;
import eu.gricom.basic.statements.Expression;
import eu.gricom.basic.statements.PrintStatement;
import eu.gricom.basic.statements.Statement;
import eu.gricom.basic.tokenizer.BasicLexer;
import eu.gricom.basic.tokenizer.Lexer;
import eu.gricom.basic.tokenizer.Token;
import eu.gricom.basic.variableTypes.Value;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("SpellCheckingInspection")
public class BasicParserTest {


    /**
     * Verify maths - using precedence rules of calcualtion (* before +)...
     *
     * Note: This unit test only works iff the last statement in the test program is a PRINT command on a real value
     * e.g. a#
     *
     * @throws Exception
     */
    @Test
    public void testDartmouthMathParsing() throws Exception {
        Lexer oTokenizer = new BasicLexer();

        String strReadText = FileHandler.readFile("src/test/basic/test_basic_Q1_unittest_2.bas");
        Program oProgram = new Program();
        oProgram.load(strReadText);

        oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        BasicParser oTestParser = new BasicParser(oProgram.getTokens(), true);
        oProgram.setStatements(oTestParser.parse());

        Execute oRun = new Execute(oProgram);
        oRun.runProgram();

        PrintStatement oLastStatement = (PrintStatement) oRun.getFinalStatement();
        Expression oExpression = oLastStatement.getExpression();
        Value oValue = oExpression.evaluate();

        double dValue = oValue.toReal();
        System.out.println(dValue);

        assertTrue(dValue == 57.6650390625);
    }

    /**
     * Verify maths - using precedence rules of calcualtion (* before +)...
     *
     * Note: This unit test only works iff the last statement in the test program is a PRINT command on a real value
     * e.g. a#
     *
     * @throws Exception
     */
    @Test
    public void testPrecedenceMathParsing() throws Exception {
        Lexer oTokenizer = new BasicLexer();

        String strReadText = FileHandler.readFile("src/test/basic/test_basic_Q1_unittest_2.bas");
        Program oProgram = new Program();
        oProgram.load(strReadText);

        oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        BasicParser oTestParser = new BasicParser(oProgram.getTokens(), false);
        oProgram.setStatements(oTestParser.parse());

        Execute oRun = new Execute(oProgram);
        oRun.runProgram();

        PrintStatement oLastStatement = (PrintStatement) oRun.getFinalStatement();
        Expression oExpression = oLastStatement.getExpression();
        Value oValue = oExpression.evaluate();

        double dValue = oValue.toReal();
        System.out.println(dValue);

        assertTrue(dValue == 1.005859375);
    }

    /**
     * Verify maths - using precedence rules of calcualtion (* before +)...
     *
     * Note: This unit test only works iff the last statement in the test program is a PRINT command on a real value
     * e.g. a#
     *
     * @throws Exception
     */
    @Test
    public void testAddMathParsing() throws Exception {
        Lexer oTokenizer = new BasicLexer();

        String strReadText = FileHandler.readFile("src/test/basic/test_basic_Q1_unittest_3_add.bas");
        Program oProgram = new Program();
        oProgram.load(strReadText);

        oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        BasicParser oTestParser = new BasicParser(oProgram.getTokens(), false);
        oProgram.setStatements(oTestParser.parse());

        Execute oRun = new Execute(oProgram);
        oRun.runProgram();

        PrintStatement oLastStatement = (PrintStatement) oRun.getFinalStatement();
        Expression oExpression = oLastStatement.getExpression();
        Value oValue = oExpression.evaluate();

        double dValue = oValue.toReal();
        System.out.println(dValue);

        assertTrue(dValue == 3);
    }

    /**
     * Verify maths - using precedence rules of calcualtion (* before +)...
     *
     * Note: This unit test only works iff the last statement in the test program is a PRINT command on a real value
     * e.g. a#
     *
     * @throws Exception
     */
    @Test
    public void testDivideMathParsing() throws Exception {
        Lexer oTokenizer = new BasicLexer();

        String strReadText = FileHandler.readFile("src/test/basic/test_basic_Q1_unittest_4_divide.bas");
        Program oProgram = new Program();
        oProgram.load(strReadText);

        oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        BasicParser oTestParser = new BasicParser(oProgram.getTokens(), false);
        oProgram.setStatements(oTestParser.parse());

        Execute oRun = new Execute(oProgram);
        oRun.runProgram();

        PrintStatement oLastStatement = (PrintStatement) oRun.getFinalStatement();
        Expression oExpression = oLastStatement.getExpression();
        Value oValue = oExpression.evaluate();

        double dValue = oValue.toReal();
        System.out.println(dValue);

        assertTrue(dValue == 0.75);
    }

    /**
     * Verify maths - using precedence rules of calcualtion (* before +)...
     *
     * Note: This unit test only works iff the last statement in the test program is a PRINT command on a real value
     * e.g. a#
     *
     * @throws Exception
     */
    @Test
    public void testMultiplyMathParsing() throws Exception {
        Lexer oTokenizer = new BasicLexer();

        String strReadText = FileHandler.readFile("src/test/basic/test_basic_Q1_unittest_5_multiply.bas");
        Program oProgram = new Program();
        oProgram.load(strReadText);

        oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        BasicParser oTestParser = new BasicParser(oProgram.getTokens(), false);
        oProgram.setStatements(oTestParser.parse());

        Execute oRun = new Execute(oProgram);
        oRun.runProgram();

        PrintStatement oLastStatement = (PrintStatement) oRun.getFinalStatement();
        Expression oExpression = oLastStatement.getExpression();
        Value oValue = oExpression.evaluate();

        double dValue = oValue.toReal();
        System.out.println(dValue);

        assertTrue(dValue == 6);
    }

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
}
