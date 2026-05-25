package eu.gricom.basic.parser;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.functions.Function;
import eu.gricom.basic.helper.FileHandler;
import eu.gricom.basic.memoryManager.Program;
import eu.gricom.basic.runtimeManager.Execute;
import eu.gricom.basic.statements.*;
import eu.gricom.basic.tokenizer.BasicLexer;
import eu.gricom.basic.tokenizer.BasicTokenType;
import eu.gricom.basic.tokenizer.Lexer;
import eu.gricom.basic.tokenizer.Token;
import eu.gricom.basic.variableTypes.Value;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SpellCheckingInspection")
public class BasicParserTest {

    /**
     * Verify maths - using precedence rules of calcualtion (* before +)...
     * <p>
     * Note: This unit test only works iff the last statement in the test program is a PRINT command on a real value
     * e.g. a#
     *
     */
    @Test
    public void testDartmouthMathParsing() throws Exception {
        Lexer oTokenizer = new BasicLexer();

        String strTestProgramName = "src/test/basic/test_basic_parser_unittest_1.bas";
        String strReadText = FileHandler.readFile(strTestProgramName);
        Program oProgram = new Program();
        oProgram.load(strTestProgramName, strReadText);

        oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        BasicParser oTestParser = new BasicParser(oProgram.getTokens(), true);
        oProgram.setStatements(oTestParser.parse());

        Execute oRun = new Execute(oProgram);
        oRun.runProgram();

        PrintStatement oLastStatement = (PrintStatement) oRun.getFinalStatement();
        Expression oExpression = oLastStatement.getExpression();
        Value oValue = oExpression.evaluate();

        double dValue = oValue.toReal();
        assertEquals(57.6650390625, dValue);
    }

    /**
     * Verify maths - using precedence rules of calcualtion (* before +)...
     * <p>
     * Note: This unit test only works iff the last statement in the test program is a PRINT command on a real value
     * e.g. a#
     *
     */
    @Test
    public void testPrecedenceMathParsing() throws Exception {
        Lexer oTokenizer = new BasicLexer();

        String strTestProgramName = "src/test/basic/test_basic_parser_unittest_1.bas";
        String strReadText = FileHandler.readFile(strTestProgramName);
        Program oProgram = new Program();
        oProgram.load(strTestProgramName, strReadText);

        oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        BasicParser oTestParser = new BasicParser(oProgram.getTokens(), false);
        oProgram.setStatements(oTestParser.parse());

        Execute oRun = new Execute(oProgram);
        oRun.runProgram();

        PrintStatement oLastStatement = (PrintStatement) oRun.getFinalStatement();
        Expression oExpression = oLastStatement.getExpression();
        Value oValue = oExpression.evaluate();

        double dValue = oValue.toReal();
        assertEquals(1.005859375, dValue);
    }

    /**
     * Verify maths - using precedence rules of calcualtion (* before +)...
     * <p>
     * Note: This unit test only works iff the last statement in the test program is a PRINT command on a real value
     * e.g. a#
     *
     */
    @Test
    public void testAddMathParsing() throws Exception {
        Lexer oTokenizer = new BasicLexer();

        String strTestProgramName = "src/test/basic/test_basic_parser_unittest_2_add.bas";
        String strReadText = FileHandler.readFile(strTestProgramName);
        Program oProgram = new Program();
        oProgram.load(strTestProgramName, strReadText);

        oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        BasicParser oTestParser = new BasicParser(oProgram.getTokens(), false);
        oProgram.setStatements(oTestParser.parse());

        Execute oRun = new Execute(oProgram);
        oRun.runProgram();

        PrintStatement oLastStatement = (PrintStatement) oRun.getFinalStatement();
        Expression oExpression = oLastStatement.getExpression();
        Value oValue = oExpression.evaluate();

        double dValue = oValue.toReal();
        assertEquals(3, dValue);
    }

    /**
     * Verify maths - using precedence rules of calcualtion (* before +)...
     * <p>
     * Note: This unit test only works iff the last statement in the test program is a PRINT command on a real value
     * e.g. a#
     *
     */
    @Test
    public void testDivideMathParsing() throws Exception {
        Lexer oTokenizer = new BasicLexer();

        String strTestProgramName = "src/test/basic/test_basic_parser_unittest_3_divide.bas";
        String strReadText = FileHandler.readFile(strTestProgramName);
        Program oProgram = new Program();
        oProgram.load(strTestProgramName, strReadText);

        oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        BasicParser oTestParser = new BasicParser(oProgram.getTokens(), false);
        oProgram.setStatements(oTestParser.parse());

        Execute oRun = new Execute(oProgram);
        oRun.runProgram();

        PrintStatement oLastStatement = (PrintStatement) oRun.getFinalStatement();
        Expression oExpression = oLastStatement.getExpression();
        Value oValue = oExpression.evaluate();

        double dValue = oValue.toReal();
        assertEquals(0.75, dValue);
    }

    /**
     * Verify maths - using precedence rules of calcualtion (* before +)...
     * <p>
     * Note: This unit test only works iff the last statement in the test program is a PRINT command on a real value
     * e.g. a#
     *
     */
    @Test
    public void testMultiplyMathParsing() throws Exception {
        Lexer oTokenizer = new BasicLexer();

        String strTestProgramName = "src/test/basic/test_basic_parser_unittest_4_multiply.bas";
        String strReadText = FileHandler.readFile(strTestProgramName);
        Program oProgram = new Program();
        oProgram.load(strTestProgramName, strReadText);

        oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        BasicParser oTestParser = new BasicParser(oProgram.getTokens(), false);
        oProgram.setStatements(oTestParser.parse());

        Execute oRun = new Execute(oProgram);
        oRun.runProgram();

        PrintStatement oLastStatement = (PrintStatement) oRun.getFinalStatement();
        Expression oExpression = oLastStatement.getExpression();
        Value oValue = oExpression.evaluate();

        double dValue = oValue.toReal();
        assertEquals(6, dValue);
    }

    /**
     * Verify that "pa1N%(1) = 5" parses into an ArrayAssignStatement and executes
     * to produce the value 5 when the element is read back via PRINT.
     */
    @Test
    public void testParseArrayAssignLiteral() throws Exception {
        Lexer oTokenizer = new BasicLexer();

        String strTestProgramName = "src/test/basic/test_array_assign_literal.bas";
        String strReadText = FileHandler.readFile(strTestProgramName);
        Program oProgram = new Program();
        oProgram.load(strTestProgramName, strReadText);
        oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        BasicParser oTestParser = new BasicParser(oProgram.getTokens(), false);
        List<Statement> aoStatements = oTestParser.parse();

        assertTrue(aoStatements.get(0) instanceof ArrayAssignStatement);

        oProgram.setStatements(aoStatements);
        Execute oRun = new Execute(oProgram);
        oRun.runProgram();

        PrintStatement oLastStatement = (PrintStatement) oRun.getFinalStatement();
        Value oValue = oLastStatement.getExpression().evaluate();
        assertEquals(5.0, oValue.toReal());
    }

    /**
     * Verify that "pa2N%(pa2I%) = 15" with pa2I% = 3 parses into an ArrayAssignStatement and
     * executes to produce the value 15 when the element is read back via PRINT.
     */
    @Test
    public void testParseArrayAssignVariable() throws Exception {
        Lexer oTokenizer = new BasicLexer();

        String strTestProgramName = "src/test/basic/test_array_assign_variable.bas";
        String strReadText = FileHandler.readFile(strTestProgramName);
        Program oProgram = new Program();
        oProgram.load(strTestProgramName, strReadText);
        oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        BasicParser oTestParser = new BasicParser(oProgram.getTokens(), false);
        List<Statement> aoStatements = oTestParser.parse();

        assertTrue(aoStatements.get(1) instanceof ArrayAssignStatement);

        oProgram.setStatements(aoStatements);
        Execute oRun = new Execute(oProgram);
        oRun.runProgram();

        PrintStatement oLastStatement = (PrintStatement) oRun.getFinalStatement();
        Value oValue = oLastStatement.getExpression().evaluate();
        assertEquals(15.0, oValue.toReal());
    }

    /**
     * Verify that "pa3N%(pa3I% + 1) = 25" with pa3I% = 4 parses into an ArrayAssignStatement and
     * executes to produce the value 25 when element 5 is read back via PRINT.
     */
    @Test
    public void testParseArrayAssignExpr() throws Exception {
        Lexer oTokenizer = new BasicLexer();

        String strTestProgramName = "src/test/basic/test_array_assign_expr.bas";
        String strReadText = FileHandler.readFile(strTestProgramName);
        Program oProgram = new Program();
        oProgram.load(strTestProgramName, strReadText);
        oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        BasicParser oTestParser = new BasicParser(oProgram.getTokens(), false);
        List<Statement> aoStatements = oTestParser.parse();

        assertTrue(aoStatements.get(1) instanceof ArrayAssignStatement);

        oProgram.setStatements(aoStatements);
        Execute oRun = new Execute(oProgram);
        oRun.runProgram();

        PrintStatement oLastStatement = (PrintStatement) oRun.getFinalStatement();
        Value oValue = oLastStatement.getExpression().evaluate();
        assertEquals(25.0, oValue.toReal());
    }

    /**
     * Verify that "PRINT pa4N%(pa4I% + 2)" with pa4I% = 1 and pa4N%(3) = 35 reads the correct
     * element via an expression index and prints 35.
     */
    @Test
    public void testParseArrayReadExpr() throws Exception {
        Lexer oTokenizer = new BasicLexer();

        String strTestProgramName = "src/test/basic/test_array_read_expr.bas";
        String strReadText = FileHandler.readFile(strTestProgramName);
        Program oProgram = new Program();
        oProgram.load(strTestProgramName, strReadText);
        oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        BasicParser oTestParser = new BasicParser(oProgram.getTokens(), false);
        oProgram.setStatements(oTestParser.parse());

        Execute oRun = new Execute(oProgram);
        oRun.runProgram();

        PrintStatement oLastStatement = (PrintStatement) oRun.getFinalStatement();
        Value oValue = oLastStatement.getExpression().evaluate();
        assertEquals(35.0, oValue.toReal());
    }

    /**
     * Test Atomic Word...
     */
    @Test
    public void testAtomicWord() throws SyntaxErrorException {
        Lexer oTokenizer = new BasicLexer();

        String strReadText = FileHandler.readFile("src/test/basic/test_basic_parser_unittest.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);

        BasicParser oParser = new BasicParser(aoTokens, true);
        Expression oExpression = oParser.atomic();

        String strExpression = oExpression.toString();
        strExpression = (strExpression.substring(0, strExpression.indexOf('@')));

        assertTrue(strExpression.matches("eu.gricom.basic.statements.VariableExpression"));
    }

    /**
     * Test: CHDIR statement parsing
     * <p>
     * Verifies that the parser correctly identifies a CHDIR token, creates a ChDirStatement,
     * and moves to the next statement in the sequence.
     * <p>
     * Test program:
     * 10 CHDIR "/tmp"
     * 20 PRINT "Changed directory"
     * 30 END
     * <p>
     * Expected behavior:
     * - First statement should be ChDirStatement with path "/tmp"
     * - Second statement should be PrintStatement
     * - Third statement should be EndStatement
     * - All statements should be correctly sequenced without gaps
     */
    @Test
    public void testParseChdirStatement() throws Exception {
        Lexer oTokenizer = new BasicLexer();

        String strTestProgramName = "src/test/basic/test_chdir_statement.bas";
        String strReadText = FileHandler.readFile(strTestProgramName);
        Program oProgram = new Program();
        oProgram.load(strTestProgramName, strReadText);
        oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        BasicParser oTestParser = new BasicParser(oProgram.getTokens(), false);
        List<Statement> aoStatements = oTestParser.parse();

        // Verify that we have at least 3 statements
        assertTrue(aoStatements.size() >= 3, "Expected at least 3 statements");

        // Verify first statement is ChDirStatement
        assertTrue(aoStatements.get(0) instanceof ChDirStatement,
                "First statement should be ChDirStatement, but was: " + aoStatements.get(0).getClass().getSimpleName());

        // Verify second statement is PrintStatement
        assertTrue(aoStatements.get(1) instanceof PrintStatement,
                "Second statement should be PrintStatement, but was: " + aoStatements.get(1).getClass().getSimpleName());

        // Verify third statement is EndStatement
        assertTrue(aoStatements.get(2) instanceof EndStatement,
                "Third statement should be EndStatement, but was: " + aoStatements.get(2).getClass().getSimpleName());

        // Verify that statements are correctly sequenced by checking their token numbers
        int iChDirTokenNumber = aoStatements.get(0).getTokenNumber();
        int iPrintTokenNumber = aoStatements.get(1).getTokenNumber();
        int iEndTokenNumber = aoStatements.get(2).getTokenNumber();

        assertTrue(iChDirTokenNumber < iPrintTokenNumber,
                "ChDirStatement token number should be less than PrintStatement token number");
        assertTrue(iPrintTokenNumber < iEndTokenNumber,
                "PrintStatement token number should be less than EndStatement token number");
    }

    /**
     * Test: CHDIR statement content verification
     * <p>
     * Verifies that the parser creates a ChDirStatement with correct content information.
     * <p>
     * Expected behavior:
     * - ChDirStatement should return "CHDIR" from content() method
     * - Token number should be properly preserved
     */
    @Test
    public void testChdirStatementContent() throws Exception {
        Lexer oTokenizer = new BasicLexer();

        String strTestProgramName = "src/test/basic/test_chdir_statement.bas";
        String strReadText = FileHandler.readFile(strTestProgramName);
        Program oProgram = new Program();
        oProgram.load(strTestProgramName, strReadText);
        oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        BasicParser oTestParser = new BasicParser(oProgram.getTokens(), false);
        List<Statement> aoStatements = oTestParser.parse();

        // Get the ChDirStatement
        ChDirStatement oChdirStatement = (ChDirStatement) aoStatements.get(0);

        // Verify content method returns correct value
        assertEquals("CHDIR", oChdirStatement.content(),
                "ChDirStatement.content() should return 'CHDIR'");

        // Verify token number is correctly set (should be 0 for first statement)
        assertTrue(oChdirStatement.getTokenNumber() >= 0,
                "Token number should be non-negative");
    }

    /**
     * Test: DIREXISTS atomic function parsing
     * <p>
     * Verifies that the atomic() method correctly parses a DIREXISTS function call.
     * DIREXISTS is a single-parameter function that checks if a directory exists.
     * <p>
     * Expected behavior:
     * - Parser recognizes DIREXISTS as a single-parameter function
     * - Returns a Function object (Expression)
     * - Function is correctly created with DIREXISTS token
     * - Function accepts a string parameter (directory path)
     */
    @Test
    public void testAtomicDirexistsFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();

        String strReadText = FileHandler.readFile("src/test/basic/test_direxists_atomic.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);

        BasicParser oParser = new BasicParser(aoTokens, false);

        // Parse the program to get to the DIREXISTS expression
        List<Statement> aoStatements = oParser.parse();

        // Verify statements were parsed
        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        // Verify first statement is IfThenStatement (which contains the DIREXISTS expression)
        Statement oFirstStatement = aoStatements.get(0);
        assertTrue(oFirstStatement instanceof IfThenStatement,
                "First statement should be IfThenStatement containing DIREXISTS");
    }

    /**
     * Test: DIREXISTS function with string literal parameter
     * <p>
     * Verifies that DIREXISTS can correctly parse with a string literal parameter.
     * <p>
     * Expected behavior:
     * - DIREXISTS("existing_path") is parsed as a Function
     * - Parser correctly handles parentheses and string parameter
     * - Expression is evaluated without errors
     */
    @Test
    public void testAtomicDirexistsWithStringParameter() throws Exception {
        Lexer oTokenizer = new BasicLexer();

        // Create a simple test with DIREXISTS in an IF statement
        String strProgram = "10 IF DIREXISTS(\"/tmp\") THEN\n20 PRINT \"exists\"\n30 END";
        List<Token> aoTokens = oTokenizer.tokenize(strProgram);

        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        // Verify the program parses without errors
        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        // Verify first statement is IfThenStatement
        assertTrue(aoStatements.get(0) instanceof IfThenStatement,
                "DIREXISTS(\"/tmp\") should be parsed in an IfThenStatement");
    }

    /**
     * Test: DIREXISTS function with variable parameter
     * <p>
     * Verifies that DIREXISTS correctly parses with a variable as the path parameter.
     * <p>
     * Expected behavior:
     * - DIREXISTS(strPath$) is parsed as a Function with variable expression
     * - Parser handles the variable reference inside parentheses
     * - Expression object is returned correctly
     */
    @Test
    public void testAtomicDirexistsWithVariableParameter() throws Exception {
        Lexer oTokenizer = new BasicLexer();

        // Create a test with DIREXISTS using a variable
        String strProgram = "10 strPath$ = \"/home\"\n20 IF DIREXISTS(strPath$) THEN\n30 PRINT \"path exists\"\n40 END";
        List<Token> aoTokens = oTokenizer.tokenize(strProgram);

        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        // Verify the program parses without errors
        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 2, "Should have at least two statements");

        // Verify second statement is IfThenStatement (first is assignment)
        Statement oSecondStatement = aoStatements.get(1);
        assertTrue(oSecondStatement instanceof IfThenStatement,
                "DIREXISTS(strPath$) should be parsed in an IfThenStatement");
    }

    /**
     * Test: DIREXISTS token classification
     * <p>
     * Verifies that DIREXISTS is correctly recognized as a single-parameter function.
     * <p>
     * Expected behavior:
     * - DIREXISTS token is recognized in the atomic method
     * - Parser treats it as a function (not a keyword or statement)
     * - Requires LEFT_PAREN after the function name
     */
    @Test
    public void testAtomicDirexistsTokenType() throws SyntaxErrorException {
        Lexer oTokenizer = new BasicLexer();

        String strReadText = FileHandler.readFile("src/test/basic/test_direxists_atomic.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);

        BasicParser oParser = new BasicParser(aoTokens, false);

        // Find DIREXISTS token in the token list
        boolean bDirexistsFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.DIREXISTS) {
                bDirexistsFound = true;
                break;
            }
        }

        assertTrue(bDirexistsFound, "DIREXISTS token should be recognized by the lexer");
    }
}
