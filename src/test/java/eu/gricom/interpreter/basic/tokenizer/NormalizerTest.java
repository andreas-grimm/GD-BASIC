package eu.gricom.interpreter.basic.tokenizer;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NormalizerTest {

    @Test
    public void testNormalString() {
        String strTest = "This is a normal String";
        String strResult = Normalizer.normalize(strTest);

        assertEquals(strTest, strResult);
    }

    @Test
    public void testNormalStringWithQuotes() {
        String strTest = "This is a normal String with \"Quotes\"";
        String strResult = Normalizer.normalize(strTest);

        assertEquals(strTest, strResult);
    }

    @Test
    public void testNormalStringWithComma() {
        String strTest = "This is a normal String with Comma:,";
        String strTarget = "This is a normal String with Comma: ,";
        String strResult = Normalizer.normalize(strTest);

        assertEquals(strTarget, strResult);
    }

    @Test
    public void testNormalStringWithCommaInQuotes() {
        String strTest = "This is a normal String with Comma in \"Quotes:,\"";
        String strResult = Normalizer.normalize(strTest);

        assertEquals(strTest, strResult);
    }

    @Test
    public void testNormalStringWithEverything() {
        String strTest = "This is a normal String with Comma in \"Quotes:,\" and out of Quotes:,";
        String strTarget = "This is a normal String with Comma in \"Quotes:,\" and out of Quotes: ,";
        String strResult = Normalizer.normalize(strTest);

        assertEquals(strTarget, strResult);
    }

    @Test
    public void testIndexString() {
        String strTest = "(1, 1, 1, 1,1)";
        String strTarget = "-1,1,1,1,1";
        String strResult = null;
        try {
            strResult = Normalizer.normalizeIndex(strTest);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        assertEquals(strTarget, strResult);
    }

    @Test
    public void testIndexStringWithoutParenthesis() {
        String strTest = "1, 1, 1, 1,1";
        String strResult = null;
        try {
            strResult = Normalizer.normalizeIndex(strTest);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        assertEquals(strTest, strResult);
    }

    @Test
    public void testIndexStringWrongParenthesisLeft() {
        String strTest = "1, 1, 1, 1,1)";

        assertThrows(RuntimeException.class, () -> {
            Normalizer.normalizeIndex(strTest);
        });
    }

    @Test
    public void testIndexStringWrongParenthesisRight() {
        String strTest = "(1, 1, 1, 1,1";

        assertThrows(RuntimeException.class, () -> {
            Normalizer.normalizeIndex(strTest);
        });
    }
}
