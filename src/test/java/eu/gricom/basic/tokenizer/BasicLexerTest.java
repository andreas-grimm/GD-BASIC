package eu.gricom.basic.tokenizer;

import eu.gricom.basic.error.SyntaxErrorException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BasicLexerTest.java
 * <p>
 * Description: Unit test for BasicLexer class.
 */
public class BasicLexerTest {

    private final BasicLexer lexer = new BasicLexer();

    @Test
    public void testIsBoolean() throws Exception {
        Method method = BasicLexer.class.getDeclaredMethod("isBoolean", String.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(lexer, "TRUE"));
        assertTrue((Boolean) method.invoke(lexer, "FALSE"));
        assertTrue((Boolean) method.invoke(lexer, "true"));
        assertTrue((Boolean) method.invoke(lexer, "false"));
        assertFalse((Boolean) method.invoke(lexer, "NOT_BOOLEAN"));
    }

    @Test
    public void testIsString() throws Exception {
        Method method = BasicLexer.class.getDeclaredMethod("isString", String.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(lexer, "\"String\""));
        assertTrue((Boolean) method.invoke(lexer, "\""));
        assertFalse((Boolean) method.invoke(lexer, "String\""));
        assertFalse((Boolean) method.invoke(lexer, "String"));
    }

    @Test
    public void testIsNumber() throws Exception {
        Method method = BasicLexer.class.getDeclaredMethod("isNumber", String.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(lexer, "123"));
        assertTrue((Boolean) method.invoke(lexer, "123.45"));
        assertTrue((Boolean) method.invoke(lexer, "-123"));
        assertTrue((Boolean) method.invoke(lexer, ".45"));
        assertFalse((Boolean) method.invoke(lexer, "abc"));
        assertFalse((Boolean) method.invoke(lexer, "12.34.56"));
        assertFalse((Boolean) method.invoke(lexer, (String) null));
    }

    @Test
    public void testTokenizeSimple() throws SyntaxErrorException {
        String source = "10 PRINT \"HELLO\"\n20 END";
        List<Token> tokens = lexer.tokenize(source);

        assertEquals(3, tokens.size());

        assertEquals(10, tokens.get(0).getLine());
        assertEquals(BasicTokenType.PRINT, tokens.get(0).getType());

        assertEquals(10, tokens.get(1).getLine());
        assertEquals(BasicTokenType.STRING, tokens.get(1).getType());
        assertEquals("HELLO", tokens.get(1).getText());

        assertEquals(20, tokens.get(2).getLine());
        assertEquals(BasicTokenType.END, tokens.get(2).getType());
    }

    @Test
    public void testTokenizeLineNumberSequence() {
        String source = "20 PRINT \"HELLO\"\n10 END";
        assertThrows(SyntaxErrorException.class, () -> lexer.tokenize(source));
    }

    @Test
    public void testTokenizeEmptyLine() throws SyntaxErrorException {
        String source = "10";
        List<Token> tokens = lexer.tokenize(source);
        assertEquals(1, tokens.size());
        assertEquals(BasicTokenType.LINE, tokens.get(0).getType());
        assertEquals(10, tokens.get(0).getLine());
    }

    @Test
    public void testTokenizeComments() throws SyntaxErrorException {
        String source = "10 REM This is a comment\n20 ' Another comment";
        List<Token> tokens = lexer.tokenize(source);
        
        assertEquals(2, tokens.size());
        assertEquals(BasicTokenType.REM, tokens.get(0).getType());
        assertEquals(BasicTokenType.COMMENT, tokens.get(1).getType());
    }

    @Test
    public void testTokenizeMultiWordString() throws SyntaxErrorException {
        String source = "10 PRINT \"HELLO WORLD\"";
        List<Token> tokens = lexer.tokenize(source);
        
        assertEquals(2, tokens.size());
        assertEquals(BasicTokenType.STRING, tokens.get(1).getType());
        assertEquals("HELLO WORLD", tokens.get(1).getText());
    }

    @Test
    public void testTokenizeUnclosedString() {
        String source = "10 PRINT \"HELLO\n20 END";
        
        // This fails because oToken is reset to null at the start of each line,
        // but bIsStringRunning remains true.
        assertThrows(SyntaxErrorException.class, () -> lexer.tokenize(source));
    }

    @Test
    public void testTokenizeBoolean() throws SyntaxErrorException {
        String source = "10 LET A = TRUE";
        List<Token> tokens = lexer.tokenize(source);

        // 10 LET A = TRUE -> tokens: LET, A, =, TRUE
        assertEquals(4, tokens.size());
        assertEquals(BasicTokenType.BOOLEAN, tokens.get(3).getType());
        assertEquals("TRUE", tokens.get(3).getText());
    }

    @Test
    public void testTokenizeMixed() throws SyntaxErrorException {
        String source = "10 IF A = 5 THEN GOTO 100";
        List<Token> tokens = lexer.tokenize(source);

        // 10 IF A = 5 THEN GOTO 100
        // Normalizer should add spaces around =.
        // Tokens: IF, A, =, 5, THEN, GOTO, 100
        assertEquals(7, tokens.size());
        assertEquals(BasicTokenType.IF, tokens.get(0).getType());
        assertEquals(BasicTokenType.WORD, tokens.get(1).getType());
        assertEquals(BasicTokenType.ASSIGN_EQUAL, tokens.get(2).getType());
        assertEquals(BasicTokenType.NUMBER, tokens.get(3).getType());
        assertEquals(BasicTokenType.THEN, tokens.get(4).getType());
        assertEquals(BasicTokenType.GOTO, tokens.get(5).getType());
        assertEquals(BasicTokenType.NUMBER, tokens.get(6).getType());
    }

    @Test
    public void testTokenizeLineOnlyNumber() throws SyntaxErrorException {
        String source = "10 ";
        List<Token> tokens = lexer.tokenize(source);
        assertEquals(1, tokens.size());
        assertEquals(BasicTokenType.LINE, tokens.get(0).getType());
    }
}
