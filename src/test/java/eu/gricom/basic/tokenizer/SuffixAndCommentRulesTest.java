package eu.gricom.basic.tokenizer;

import eu.gricom.basic.error.SyntaxErrorException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SuffixAndCommentRulesTest.java
 * <p>
 * Description: Tests for the Phase 1 rules of the BCD branch (feature/bcd-arithmetic):
 * <ul>
 *   <li>A7: a "!" directly after a name, outside brackets, is the boolean suffix ("A!=TRUE" is "A! = TRUE").</li>
 *   <li>A6: a "!" that starts a line is a comment; a "!" after a colon is a comment and gives a warning.</li>
 *   <li>A8: a decimal suffix "#" directly after a number is a syntax error.</li>
 * </ul>
 * Each rule has positive tests (correct reading) and negative tests (must fail or must not change).
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class SuffixAndCommentRulesTest {

    private final BasicLexer _oLexer = new BasicLexer();

    // ----- A7: boolean suffix and the not-equal operator (Normalizer) -----

    @Test
    public void testBooleanAssignmentWithoutSpaceIsSplit() {
        // Positive: "A!=TRUE" is the boolean assignment "A! = TRUE"
        assertEquals("A! = TRUE", Normalizer.normalize("A!=TRUE"));
    }

    @Test
    public void testBooleanSuffixWithSpaceIsUnchanged() {
        // Positive: "A! = TRUE" already has spaces and stays the same
        assertEquals("A! = TRUE", Normalizer.normalize("A! = TRUE"));
    }

    @Test
    public void testNotEqualWithSpacesIsOperator() {
        // Negative for the suffix rule: a space before "!" makes it the not-equal operator
        assertEquals("A != 2", Normalizer.normalize("A != 2"));
    }

    @Test
    public void testNotEqualAfterNumberIsOperator() {
        // Negative for the suffix rule: "1!=2" has no name in front of "!", so it stays not-equal
        assertEquals("1!=2", Normalizer.normalize("1!=2"));
    }

    @Test
    public void testNotEqualInsideBracketsIsOperator() {
        // Negative for the suffix rule: inside brackets "y!=0" is a comparison, not an assignment
        assertEquals("valid! ( y!=0 ) ", Normalizer.normalize("valid!(y!=0)"));
    }

    @Test
    public void testBooleanSuffixBeforeBracketKeepsSpacing() {
        // Positive: a boolean array name keeps the spacing of the existing bracket rules
        assertEquals("flags! ( item - 1 ) ", Normalizer.normalize("flags!(item-1)"));
    }

    // ----- A6: "!" as a comment (BasicLexer) -----

    @Test
    public void testExclamationAtStartOfLineIsComment() throws SyntaxErrorException {
        // Positive: "10 ! Comment" is line 10 with a comment (token type REM)
        List<Token> aoTokens = _oLexer.tokenize("10 ! Comment\n20 END");
        assertEquals(BasicTokenType.REM, aoTokens.get(0).getType());
        assertEquals(10, aoTokens.get(0).getLine());
        // the next line still produces its own tokens
        assertTrue(aoTokens.stream().anyMatch(t -> t.getType() == BasicTokenType.END && t.getLine() == 20));
    }

    @Test
    public void testExclamationAfterColonIsCommentWithWarning() throws SyntaxErrorException {
        // Positive: "PRINT 1 : ! Comment" keeps the PRINT and the colon, and the rest is a comment
        List<Token> aoTokens = _oLexer.tokenize("10 PRINT 1 : ! Comment");
        int iLast = aoTokens.size() - 1;
        assertEquals(BasicTokenType.REM, aoTokens.get(iLast).getType());
        assertEquals(BasicTokenType.COLON, aoTokens.get(iLast - 1).getType());
    }

    @Test
    public void testExclamationInsideExpressionFails() {
        // Negative (decision Q2): "!" inside an expression is a syntax error
        assertThrows(SyntaxErrorException.class, () -> _oLexer.tokenize("10 PRINT !1"));
    }

    @Test
    public void testNotEqualOperatorStillWorksInLexer() throws SyntaxErrorException {
        // Positive: "!=" as an operator is not caught by the "!" rule
        List<Token> aoTokens = _oLexer.tokenize("10 IF A != 2 THEN 20");
        assertTrue(aoTokens.stream().anyMatch(t -> t.getText().equals("!=")));
        assertFalse(aoTokens.stream().anyMatch(t -> t.getText().equals("!")));
    }

    // ----- A8: "#" after a number (BasicLexer) -----

    @Test
    public void testDecimalSuffixAfterNumberFails() {
        // Negative: "10#" is not a valid name or number
        assertThrows(SyntaxErrorException.class, () -> _oLexer.tokenize("10 X = 10#"));
    }

    @Test
    public void testDecimalSuffixAfterDecimalNumberFails() {
        // Negative: "1.5#" is not a valid name or number
        assertThrows(SyntaxErrorException.class, () -> _oLexer.tokenize("10 X = 1.5#"));
    }

    @Test
    public void testDecimalSuffixAfterNameIsAccepted() throws SyntaxErrorException {
        // Positive: "AMOUNT#" is a name, so the lexer accepts it (the BCD type is added in Phase 2)
        List<Token> aoTokens = _oLexer.tokenize("10 AMOUNT# = 5");
        assertTrue(aoTokens.stream().anyMatch(t -> t.getText().equals("AMOUNT#")));
    }
}
