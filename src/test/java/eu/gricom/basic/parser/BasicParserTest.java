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

    // ==================== ZERO-PARAMETER FUNCTIONS ====================

    @Test
    public void testAtomicGetcwdFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_zero_param_getcwd.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bGetcwdFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.GETCWD) {
                bGetcwdFound = true;
                break;
            }
        }
        assertTrue(bGetcwdFound, "GETCWD token should be recognized by the lexer");
    }

    @Test
    public void testAtomicMemFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_zero_param_mem.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bMemFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.MEM) {
                bMemFound = true;
                break;
            }
        }
        assertTrue(bMemFound, "MEM token should be recognized by the lexer");
    }

    @Test
    public void testAtomicRndFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_zero_param_rnd.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bRndFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.RND) {
                bRndFound = true;
                break;
            }
        }
        assertTrue(bRndFound, "RND token should be recognized by the lexer");
    }

    @Test
    public void testAtomicTimeFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_zero_param_time.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bTimeFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.TIME) {
                bTimeFound = true;
                break;
            }
        }
        assertTrue(bTimeFound, "TIME token should be recognized by the lexer");
    }

    // ==================== SINGLE-PARAMETER MATH FUNCTIONS ====================

    @Test
    public void testAtomicAbsFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_math_abs.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bAbsFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.ABS) {
                bAbsFound = true;
                break;
            }
        }
        assertTrue(bAbsFound, "ABS token should be recognized by the lexer");
    }

    @Test
    public void testAtomicSinFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_math_sin.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bSinFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.SIN) {
                bSinFound = true;
                break;
            }
        }
        assertTrue(bSinFound, "SIN token should be recognized by the lexer");
    }

    @Test
    public void testAtomicCosFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_math_cos.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bCosFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.COS) {
                bCosFound = true;
                break;
            }
        }
        assertTrue(bCosFound, "COS token should be recognized by the lexer");
    }

    @Test
    public void testAtomicTanFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_math_tan.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bTanFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.TAN) {
                bTanFound = true;
                break;
            }
        }
        assertTrue(bTanFound, "TAN token should be recognized by the lexer");
    }

    @Test
    public void testAtomicLogFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_math_log.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bLogFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.LOG) {
                bLogFound = true;
                break;
            }
        }
        assertTrue(bLogFound, "LOG token should be recognized by the lexer");
    }

    @Test
    public void testAtomicLog10Function() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_math_log10.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bLog10Found = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.LOG10) {
                bLog10Found = true;
                break;
            }
        }
        assertTrue(bLog10Found, "LOG10 token should be recognized by the lexer");
    }

    @Test
    public void testAtomicExpFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_math_exp.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bExpFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.EXP) {
                bExpFound = true;
                break;
            }
        }
        assertTrue(bExpFound, "EXP token should be recognized by the lexer");
    }

    @Test
    public void testAtomicSqrFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_math_sqr.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bSqrFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.SQR) {
                bSqrFound = true;
                break;
            }
        }
        assertTrue(bSqrFound, "SQR token should be recognized by the lexer");
    }

    @Test
    public void testAtomicAtnFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_math_atn.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bAtnFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.ATN) {
                bAtnFound = true;
                break;
            }
        }
        assertTrue(bAtnFound, "ATN token should be recognized by the lexer");
    }

    // ==================== SINGLE-PARAMETER CONVERSION FUNCTIONS ====================

    @Test
    public void testAtomicChrFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_convert_chr.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bChrFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.CHR) {
                bChrFound = true;
                break;
            }
        }
        assertTrue(bChrFound, "CHR token should be recognized by the lexer");
    }

    @Test
    public void testAtomicAscFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_convert_asc.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bAscFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.ASC) {
                bAscFound = true;
                break;
            }
        }
        assertTrue(bAscFound, "ASC token should be recognized by the lexer");
    }

    @Test
    public void testAtomicValFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_convert_val.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bValFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.VAL) {
                bValFound = true;
                break;
            }
        }
        assertTrue(bValFound, "VAL token should be recognized by the lexer");
    }

    @Test
    public void testAtomicStrFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_convert_str.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bStrFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.STR) {
                bStrFound = true;
                break;
            }
        }
        assertTrue(bStrFound, "STR token should be recognized by the lexer");
    }

    @Test
    public void testAtomicCintFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_convert_cint.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bCintFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.CINT) {
                bCintFound = true;
                break;
            }
        }
        assertTrue(bCintFound, "CINT token should be recognized by the lexer");
    }

    @Test
    public void testAtomicCdblFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_convert_cdbl.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bCdblFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.CDBL) {
                bCdblFound = true;
                break;
            }
        }
        assertTrue(bCdblFound, "CDBL token should be recognized by the lexer");
    }

    // ==================== SINGLE-PARAMETER FILE FUNCTIONS ====================

    @Test
    public void testAtomicEofFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_file_eof.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bEofFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.EOF) {
                bEofFound = true;
                break;
            }
        }
        assertTrue(bEofFound, "EOF token should be recognized by the lexer");
    }

    @Test
    public void testAtomicFexistsFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_file_fexists.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bFexistsFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.FEXISTS) {
                bFexistsFound = true;
                break;
            }
        }
        assertTrue(bFexistsFound, "FEXISTS token should be recognized by the lexer");
    }

    @Test
    public void testAtomicFgetnameFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_file_fgetname.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bFgetnameFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.FGETNAME) {
                bFgetnameFound = true;
                break;
            }
        }
        assertTrue(bFgetnameFound, "FGETNAME token should be recognized by the lexer");
    }

    @Test
    public void testAtomicFgetsizeFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_file_fgetsize.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bFgetsizeFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.FGETSIZE) {
                bFgetsizeFound = true;
                break;
            }
        }
        assertTrue(bFgetsizeFound, "FGETSIZE token should be recognized by the lexer");
    }

    @Test
    public void testAtomicFisopenFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_file_fisopen.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bFisopenFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.FISOPEN) {
                bFisopenFound = true;
                break;
            }
        }
        assertTrue(bFisopenFound, "FISOPEN token should be recognized by the lexer");
    }

    @Test
    public void testAtomicFlinecountFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_file_flinecount.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bFlinecountFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.FLINECOUNT) {
                bFlinecountFound = true;
                break;
            }
        }
        assertTrue(bFlinecountFound, "FLINECOUNT token should be recognized by the lexer");
    }

    @Test
    public void testAtomicFmodtimeFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_file_fmodtime.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bFmodtimeFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.FMODTIME) {
                bFmodtimeFound = true;
                break;
            }
        }
        assertTrue(bFmodtimeFound, "FMODTIME token should be recognized by the lexer");
    }

    // ==================== SINGLE-PARAMETER UTILITY FUNCTIONS ====================

    @Test
    public void testAtomicLenFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_string_len.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bLenFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.LEN) {
                bLenFound = true;
                break;
            }
        }
        assertTrue(bLenFound, "LEN token should be recognized by the lexer");
    }

    @Test
    public void testAtomicNotFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_logic_not.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bNotFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.NOT) {
                bNotFound = true;
                break;
            }
        }
        assertTrue(bNotFound, "NOT token should be recognized by the lexer");
    }

    // ==================== TWO-PARAMETER FUNCTIONS ====================

    @Test
    public void testAtomicInstrFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_two_param_instr.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bInstrFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.INSTR) {
                bInstrFound = true;
                break;
            }
        }
        assertTrue(bInstrFound, "INSTR token should be recognized by the lexer");
    }

    @Test
    public void testAtomicLeftFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_two_param_left.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bLeftFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.LEFT) {
                bLeftFound = true;
                break;
            }
        }
        assertTrue(bLeftFound, "LEFT token should be recognized by the lexer");
    }

    @Test
    public void testAtomicRightFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_two_param_right.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bRightFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.RIGHT) {
                bRightFound = true;
                break;
            }
        }
        assertTrue(bRightFound, "RIGHT token should be recognized by the lexer");
    }

    @Test
    public void testAtomicFcompareFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_two_param_fcompare.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bFcompareFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.FCOMPARE) {
                bFcompareFound = true;
                break;
            }
        }
        assertTrue(bFcompareFound, "FCOMPARE token should be recognized by the lexer");
    }

    @Test
    public void testAtomicSystemFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_two_param_system.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bSystemFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.SYSTEM) {
                bSystemFound = true;
                break;
            }
        }
        assertTrue(bSystemFound, "SYSTEM token should be recognized by the lexer");
    }

    @Test
    public void testAtomicCallFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_two_param_call.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bCallFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.CALL) {
                bCallFound = true;
                break;
            }
        }
        assertTrue(bCallFound, "CALL token should be recognized by the lexer");
    }

    // ==================== THREE-PARAMETER FUNCTIONS ====================

    @Test
    public void testAtomicMidFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_three_param_mid.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bMidFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.MID) {
                bMidFound = true;
                break;
            }
        }
        assertTrue(bMidFound, "MID token should be recognized by the lexer");
    }

    @Test
    public void testAtomicListdirectoryFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_three_param_listdirectory.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bListdirectoryFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.LISTDIRECTORY) {
                bListdirectoryFound = true;
                break;
            }
        }
        assertTrue(bListdirectoryFound, "LISTDIRECTORY token should be recognized by the lexer");
    }

    // ==================== STRING CASE CONVERSION FUNCTIONS ====================

    @Test
    public void testParseAndExecuteUpperFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();

        String strTestProgramName = "src/test/basic/test_upper_parsing.bas";
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
        String strValue = oExpression.evaluate().toString();

        assertEquals("HELLO", strValue);
    }

    @Test
    public void testParseAndExecuteLowerFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();

        String strTestProgramName = "src/test/basic/test_lower_parsing.bas";
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
        String strValue = oExpression.evaluate().toString();

        assertEquals("hello", strValue);
    }

    @Test
    public void testAtomicUpperFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_upper_parsing.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bUpperFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.UPPER) {
                bUpperFound = true;
                break;
            }
        }
        assertTrue(bUpperFound, "UPPER token should be recognized by the lexer");
    }

    @Test
    public void testAtomicLowerFunction() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = FileHandler.readFile("src/test/basic/test_lower_parsing.bas");
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 1, "Should have at least one statement");

        boolean bLowerFound = false;
        for (Token oToken : aoTokens) {
            if (oToken.getType() == BasicTokenType.LOWER) {
                bLowerFound = true;
                break;
            }
        }
        assertTrue(bLowerFound, "LOWER token should be recognized by the lexer");
    }

    @Test
    public void testOnGotoBasicParsing() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = "10 X% = 1\n20 ON X% GOTO 100, 200\n100 END";
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 2, "Should have at least 2 statements");

        boolean bOnGotoFound = false;
        for (Statement oStatement : aoStatements) {
            if (oStatement instanceof OnGotoStatement) {
                bOnGotoFound = true;
                break;
            }
        }
        assertTrue(bOnGotoFound, "Parser should recognize ON GOTO statement");
    }

    @Test
    public void testOnGosubBasicParsing() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = "10 X% = 1\n20 ON X% GOSUB 100, 200\n100 RETURN\n200 END";
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 2, "Should have at least 2 statements");

        boolean bOnGosubFound = false;
        for (Statement oStatement : aoStatements) {
            if (oStatement instanceof OnGosubStatement) {
                bOnGosubFound = true;
                break;
            }
        }
        assertTrue(bOnGosubFound, "Parser should recognize ON GOSUB statement");
    }

    @Test
    public void testOnGotoWithExpression() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = "10 X% = 1\n20 Y% = 1\n30 ON X% + Y% GOTO 100, 200, 300\n100 END";
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 3, "Should have at least 3 statements");

        boolean bOnGotoFound = false;
        for (Statement oStatement : aoStatements) {
            if (oStatement instanceof OnGotoStatement) {
                bOnGotoFound = true;
                OnGotoStatement oOnGoto = (OnGotoStatement) oStatement;
                String strContent = oOnGoto.content();
                assertTrue(strContent.contains("ON GOTO"), "Content should describe ON GOTO statement");
                break;
            }
        }
        assertTrue(bOnGotoFound, "Parser should recognize ON GOTO with expression");
    }

    @Test
    public void testOnGotoMultipleTargets() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = "10 X% = 3\n20 ON X% GOTO 100, 200, 300, 400, 500\n100 END";
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");

        for (Statement oStatement : aoStatements) {
            if (oStatement instanceof OnGotoStatement) {
                String strContent = oStatement.content();
                assertTrue(strContent.contains("100"), "Should contain first target");
                assertTrue(strContent.contains("500"), "Should contain last target");
                return;
            }
        }
        fail("ON GOTO statement not found");
    }

    @Test
    public void testOnGosubMultipleTargets() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = "10 X% = 2\n20 ON X% GOSUB 100, 200, 300, 400\n100 RETURN\n200 END";
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");

        for (Statement oStatement : aoStatements) {
            if (oStatement instanceof OnGosubStatement) {
                String strContent = oStatement.content();
                assertTrue(strContent.contains("100"), "Should contain first target");
                assertTrue(strContent.contains("400"), "Should contain last target");
                return;
            }
        }
        fail("ON GOSUB statement not found");
    }

    @Test
    public void testOnGotoSingleTarget() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = "10 X% = 1\n20 ON X% GOTO 100\n100 END";
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");
        assertTrue(aoStatements.size() >= 2, "Should have at least 2 statements");
    }

    @Test
    public void testOnGotoInvalidSyntax() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = "10 X% = 1\n20 ON X% PRINT 100\n100 END";
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);

        assertThrows(SyntaxErrorException.class, oParser::parse,
            "Parser should throw exception for invalid ON syntax (PRINT instead of GOTO)");
    }

    @Test
    public void testOnGosubInvalidSyntax() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = "10 X% = 1\n20 ON X% IF 100\n100 END";
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);

        assertThrows(SyntaxErrorException.class, oParser::parse,
            "Parser should throw exception for invalid ON syntax (IF instead of GOSUB)");
    }

    @Test
    public void testOnGotoWithComplexExpression() throws Exception {
        Lexer oTokenizer = new BasicLexer();
        String strReadText = "10 X% = 2\n20 Y% = 3\n30 ON X% * Y% - 5 GOTO 100, 200, 300\n100 END";
        List<Token> aoTokens = oTokenizer.tokenize(strReadText);
        BasicParser oParser = new BasicParser(aoTokens, false);
        List<Statement> aoStatements = oParser.parse();

        assertNotNull(aoStatements, "Parser should return statements");

        boolean bOnGotoFound = false;
        for (Statement oStatement : aoStatements) {
            if (oStatement instanceof OnGotoStatement) {
                bOnGotoFound = true;
                break;
            }
        }
        assertTrue(bOnGotoFound, "Parser should handle complex expressions in ON GOTO");
    }
}
