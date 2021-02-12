package eu.gricom.interpreter.basic.tokenizer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
