package eu.gricom.basic.tokenizer;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DollarFunctionLexerTest.java
 * <p>
 * Description: A name with a $ at the end is a string function only when it is followed by "(" (STR$(2.5)). A name
 * like "str$ = 1" is a variable. Bug fixed in Phase 7: STR$( was read as an array access.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class DollarFunctionLexerTest {

    @Test
    public void testStringFunctionCallIsFunctionToken() throws Exception {
        List<Token> aoTokens = new BasicLexer().tokenize("10 x$ = STR$(2.5)");

        assertEquals(BasicTokenType.STR, aoTokens.get(2).getType());
        assertEquals("STR$", aoTokens.get(2).getText());
    }

    @Test
    public void testDollarNameWithoutCallStaysVariable() throws Exception {
        List<Token> aoTokens = new BasicLexer().tokenize("10 str$ = \"abc\"");

        assertEquals(BasicTokenType.WORD, aoTokens.get(0).getType());
        assertEquals("str$", aoTokens.get(0).getText());
    }
}
