package eu.gricom.basic.tokenizer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * ReservedWordsTest.java
 * <p>
 * Description: Unit test for ReservedWords class.
 */
public class ReservedWordsTest {

    @Test
    public void testGetIndex() {
        // Test valid reserved words (case insensitive as per ReservedWords implementation)
        assertEquals(0, ReservedWords.getIndex("@PRAGMA"));
        assertEquals(1, ReservedWords.getIndex("ABS"));
        assertEquals(1, ReservedWords.getIndex("abs"));
        assertEquals(36, ReservedWords.getIndex("GOTO"));
        
        // Test symbols that are escaped in the regex but passed as literal strings
        // ReservedWords uses matches(strReserveWord), where strReserveWord might be "\\+"
        // "+" matches "\\+" regex, and its index is 78
        assertEquals(78, ReservedWords.getIndex("+"));
        
        // Test invalid reserved word
        assertEquals(-1, ReservedWords.getIndex("NON_EXISTENT_WORD"));
    }

    @Test
    public void testGetTokenIndex() {
        // Test valid token types
        assertEquals(0, ReservedWords.getTokenIndex("PRAGMA"));
        assertEquals(1, ReservedWords.getTokenIndex("ABS"));
        assertEquals(36, ReservedWords.getTokenIndex("GOTO"));
        
        // Test invalid token type
        assertEquals(-1, ReservedWords.getTokenIndex("NON_EXISTENT_TOKEN"));
    }

    @Test
    public void testGetTokenType() {
        // Test some known indices
        assertEquals(BasicTokenType.PRAGMA, ReservedWords.getTokenType(0));
        assertEquals(BasicTokenType.ABS, ReservedWords.getTokenType(1));
        assertEquals(BasicTokenType.GOTO, ReservedWords.getTokenType(36));
        
        // Verify we can get all token types
        for (int i = 0; i < 81; i++) {
            assertNotNull(ReservedWords.getTokenType(i));
        }
    }

    @Test
    public void testGetReservedWord() {
        // Test some known indices
        assertEquals("@PRAGMA", ReservedWords.getReservedWord(0));
        assertEquals("ABS", ReservedWords.getReservedWord(1));
        assertEquals("GOTO", ReservedWords.getReservedWord(36));
        
        // Verify we can get all reserved words
        for (int i = 0; i < 78; i++) {
            assertNotNull(ReservedWords.getReservedWord(i));
        }
    }
}
