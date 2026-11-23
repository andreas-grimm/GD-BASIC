package eu.gricom.basic.tokenizer;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for OperatorSpacer (spacing around operators written without spaces) and for the tokens that the
 * BasicLexer produces from those lines.
 */
public class OperatorSpacerTest {

    // ---------------------------------------------------------------- positive: spacing

    @Test
    public void testBinaryMinusIsSpaced() {
        assertEquals("A - B", OperatorSpacer.space("A-B"));
    }

    @Test
    public void testMultiplyWithoutSpacesIsSpaced() {
        assertEquals("2 * A", OperatorSpacer.space("2*A"));
    }

    @Test
    public void testAssignmentWithUnaryMinusIsSpaced() {
        assertEquals("X = - A", OperatorSpacer.space("X=-A"));
    }

    @Test
    public void testUnaryMinusAfterKeywordIsSeparatedFromName() {
        assertEquals("PRINT - A", OperatorSpacer.space("PRINT -A"));
    }

    @Test
    public void testUnaryMinusInFrontOfDigitStaysNegativeNumber() {
        assertEquals("PRINT -5", OperatorSpacer.space("PRINT -5"));
        assertEquals("X = -5", OperatorSpacer.space("X=-5"));
    }

    @Test
    public void testBinaryMinusAfterDigitIsSpaced() {
        assertEquals("1 - A", OperatorSpacer.space("1-A"));
    }

    @Test
    public void testMinusAfterTypeSuffixIsBinary() {
        assertEquals("A% - B", OperatorSpacer.space("A%-B"));
    }

    @Test
    public void testMinusAfterClosingBracketIsBinary() {
        assertEquals("(A) - B", OperatorSpacer.space("(A)-B"));
    }

    @Test
    public void testComparisonOperatorsAreSpaced() {
        assertEquals("A <= B", OperatorSpacer.space("A<=B"));
        assertEquals("A >= B", OperatorSpacer.space("A>=B"));
        assertEquals("A != B", OperatorSpacer.space("A!=B"));
        assertEquals("A == B", OperatorSpacer.space("A==B"));
    }

    @Test
    public void testShiftOperatorsAreSpaced() {
        assertEquals("A << 2", OperatorSpacer.space("A<<2"));
    }

    @Test
    public void testUnaryMinusAfterOpeningBracketIsSeparated() {
        assertEquals("(- A)", OperatorSpacer.space("(-A)"));
    }

    @Test
    public void testHyphenatedKeywordsAreKept() {
        assertEquals("IF X THEN END-IF", OperatorSpacer.space("IF X THEN END-IF"));
        assertEquals("END-WHILE", OperatorSpacer.space("END-WHILE"));
    }

    // ---------------------------------------------------------------- negative: text that must not change

    @Test
    public void testPercentSuffixNameMinusIsSubtraction() {
        // A% is a name with a suffix; the minus after it is a subtraction, so A%-1 is read as A% - 1
        assertEquals("PRINT D% - 1", OperatorSpacer.space("PRINT D%-1"));
    }

    @Test
    public void testPercentSuffixArrayElementMinusIsSubtraction() {
        assertEquals("PRINT C%(1) - 1", OperatorSpacer.space("PRINT C%(1)-1"));
    }

    @Test
    public void testTextInsideSquareBracketsIsNotChanged() {
        // a string index such as W$[I-1] stays together, so the interpreter reports the error for a text index
        assertEquals("PRINT W$[I-1]", OperatorSpacer.space("PRINT W$[I-1]"));
    }

    @Test
    public void testArrayElementIndexIsSpacedButSquareBracketsAreKept() {
        assertEquals("PRINT A(I - 1) + W$[I-1]", OperatorSpacer.space("PRINT A(I-1)+W$[I-1]"));
    }

    @Test
    public void testTextInsideStringsIsNotChanged() {
        assertEquals("PRINT \"A-B*C=D\"", OperatorSpacer.space("PRINT \"A-B*C=D\""));
    }

    @Test
    public void testRemCommentIsNotChanged() {
        assertEquals("REM A-B*C", OperatorSpacer.space("REM A-B*C"));
    }

    @Test
    public void testApostropheCommentIsNotChanged() {
        assertEquals("'A-B", OperatorSpacer.space("'A-B"));
    }

    @Test
    public void testDimLineIsNotChanged() {
        assertEquals("DIM A(N-1)", OperatorSpacer.space("DIM A(N-1)"));
    }

    @Test
    public void testAlreadySpacedLineIsUnchanged() {
        assertEquals("A - B", OperatorSpacer.space("A - B"));
    }

    @Test
    public void testEmptyLineIsUnchanged() {
        assertEquals("", OperatorSpacer.space(""));
    }

    // ---------------------------------------------------------------- lexer: the tokens that come out

    private static String tokens(final String strLine) throws Exception {
        List<Token> aoTokens = new BasicLexer().tokenize("20 " + strLine + "\n30 END\n");
        return aoTokens.stream()
                .filter(oToken -> oToken.getLine() == 20)
                .map(oToken -> oToken.getType() + ":" + oToken.getText())
                .collect(Collectors.joining(" "));
    }

    @Test
    public void testLexerReadsUnaryMinusOnNameAsMinusAndName() throws Exception {
        assertEquals("PRINT:PRINT MINUS:- WORD:A", tokens("PRINT -A"));
    }

    @Test
    public void testLexerReadsBinaryMinusWithoutSpacesAsOperator() throws Exception {
        assertEquals("WORD:A MINUS:- WORD:B", tokens("A-B"));
    }

    @Test
    public void testLexerReadsMultiplicationWithoutSpacesAsOperator() throws Exception {
        assertEquals("NUMBER:2 MULTIPLY:* WORD:A", tokens("2*A"));
    }

    @Test
    public void testLexerKeepsNegativeNumberLiteral() throws Exception {
        assertEquals("ASSIGN_EQUAL:= NUMBER:-5", tokens("X = -5").replace("WORD:X ", ""));
    }
}
