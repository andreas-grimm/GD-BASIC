package eu.gricom.basic.tokenizer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * ReservedWordsTest.java
 * <p>
 * Description: Unit test for ReservedWords class. Tests that reserved words and their corresponding
 * token types are correctly mapped and retrieved.
 */
public class ReservedWordsTest {

    /**
     * Test getIndex() method with various reserved words and symbols.
     */
    @Test
    public void testGetIndex() {
        // Test pragma (first reserved word)
        assertEquals(0, ReservedWords.getIndex("@PRAGMA"));

        // Test basic keywords (case-insensitive)
        assertEquals(1, ReservedWords.getIndex("ABS"));
        assertEquals(1, ReservedWords.getIndex("abs"));
        assertEquals(1, ReservedWords.getIndex("AbS"));

        // Test statement keywords
        assertEquals(53, ReservedWords.getIndex("GOTO"));
        assertEquals(70, ReservedWords.getIndex("ON"));
        assertEquals(91, ReservedWords.getIndex("THEN"));

        // Test operators and symbols
        assertEquals(100, ReservedWords.getIndex("+"));   // "\\+" in reserved words list
        assertEquals(101, ReservedWords.getIndex("-"));   // "\\-" in reserved words list

        // Test invalid reserved word
        assertEquals(-1, ReservedWords.getIndex("NON_EXISTENT_WORD"));
    }

    /**
     * Test getTokenIndex() method with valid token type names.
     */
    @Test
    public void testGetTokenIndex() {
        // Test valid token types
        assertEquals(0, ReservedWords.getTokenIndex("PRAGMA"));
        assertEquals(1, ReservedWords.getTokenIndex("ABS"));
        assertEquals(53, ReservedWords.getTokenIndex("GOTO"));
        assertEquals(70, ReservedWords.getTokenIndex("ON"));

        // Test case insensitivity
        assertEquals(0, ReservedWords.getTokenIndex("pragma"));
        assertEquals(1, ReservedWords.getTokenIndex("abs"));

        // Test invalid token type
        assertEquals(-1, ReservedWords.getTokenIndex("NON_EXISTENT_TOKEN"));
    }

    /**
     * Test getTokenType() method returns correct BasicTokenType for indices.
     */
    @Test
    public void testGetTokenType() {
        // Test known indices and their token types
        assertEquals(BasicTokenType.PRAGMA, ReservedWords.getTokenType(0));
        assertEquals(BasicTokenType.ABS, ReservedWords.getTokenType(1));
        assertEquals(BasicTokenType.GOTO, ReservedWords.getTokenType(53));
        assertEquals(BasicTokenType.ON, ReservedWords.getTokenType(70));

        // Verify we can get all token types (122 total in current implementation)
        for (int i = 0; i < 122; i++) {
            assertNotNull(ReservedWords.getTokenType(i),
                "Token type at index " + i + " should not be null");
        }
    }

    /**
     * Test getReservedWord() method returns correct word strings for indices.
     */
    @Test
    public void testGetReservedWord() {
        // Test known indices and their reserved words
        assertEquals("@PRAGMA", ReservedWords.getReservedWord(0));
        assertEquals("ABS", ReservedWords.getReservedWord(1));
        assertEquals("GOTO", ReservedWords.getReservedWord(53));
        assertEquals("ON", ReservedWords.getReservedWord(70));

        // Verify we can get all reserved words (122 total in current implementation)
        for (int i = 0; i < 122; i++) {
            assertNotNull(ReservedWords.getReservedWord(i),
                "Reserved word at index " + i + " should not be null");
        }
    }

    /**
     * Test round-trip consistency: word -> index -> token type -> consistent mapping.
     */
    @Test
    public void testRoundTripConsistency() {
        // Test that words resolve to tokens consistently
        String[] testWords = {"ABS", "GOTO", "ON", "PRINT", "IF"};
        for (String word : testWords) {
            int index = ReservedWords.getIndex(word);
            assertEquals(word, ReservedWords.getReservedWord(index),
                "Word should round-trip through index");

            BasicTokenType tokenType = ReservedWords.getTokenType(index);
            assertNotNull(tokenType,
                "Token type should exist for word: " + word);
        }
    }
}
