package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UpperTest {

    @Test
    public void testUpperBasicLowercaseString() {
        try {
            StringValue oValue = new StringValue("hello");
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), "HELLO");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperBasicUppercaseString() {
        try {
            StringValue oValue = new StringValue("HELLO");
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), "HELLO");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperMixedCaseString() {
        try {
            StringValue oValue = new StringValue("HeLLo WoRLd");
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), "HELLO WORLD");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperEmptyString() {
        try {
            StringValue oValue = new StringValue("");
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperSingleCharacterLowercase() {
        try {
            StringValue oValue = new StringValue("a");
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), "A");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperSingleCharacterUppercase() {
        try {
            StringValue oValue = new StringValue("A");
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), "A");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperWithNumbers() {
        try {
            StringValue oValue = new StringValue("hello123world");
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), "HELLO123WORLD");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperWithSpecialCharacters() {
        try {
            StringValue oValue = new StringValue("hello!@#$%");
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), "HELLO!@#$%");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperWithSpaces() {
        try {
            StringValue oValue = new StringValue("hello   world");
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), "HELLO   WORLD");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperWithTabs() {
        try {
            StringValue oValue = new StringValue("hello\tworld");
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), "HELLO\tWORLD");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperWithNewlines() {
        try {
            StringValue oValue = new StringValue("hello\nworld");
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), "HELLO\nWORLD");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperWithPunctuation() {
        try {
            StringValue oValue = new StringValue("hello, world!");
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), "HELLO, WORLD!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperWithQuotes() {
        try {
            StringValue oValue = new StringValue("\"hello\"");
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), "\"HELLO\"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperWithApostrophe() {
        try {
            StringValue oValue = new StringValue("don't");
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), "DON'T");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperWithMultipleWords() {
        try {
            StringValue oValue = new StringValue("the quick brown fox jumps over the lazy dog");
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperWithLeadingSpaces() {
        try {
            StringValue oValue = new StringValue("   hello");
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), "   HELLO");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperWithTrailingSpaces() {
        try {
            StringValue oValue = new StringValue("hello   ");
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), "HELLO   ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperWithSurroundingSpaces() {
        try {
            StringValue oValue = new StringValue("   hello   ");
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), "   HELLO   ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperWithNumericString() {
        try {
            StringValue oValue = new StringValue("123456789");
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), "123456789");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperWithAlphanumericMixed() {
        try {
            StringValue oValue = new StringValue("abc123def456ghi");
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), "ABC123DEF456GHI");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperLongString() {
        try {
            String longStr = "the quick brown fox jumps over the lazy dog. " +
                           "the quick brown fox jumps over the lazy dog. " +
                           "the quick brown fox jumps over the lazy dog.";
            StringValue oValue = new StringValue(longStr);
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), longStr.toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperWithAllSpecialCharacters() {
        try {
            StringValue oValue = new StringValue("!@#$%^&*()_+-=[]{}|;':\",./<>?");
            StringValue oResult = (StringValue) Upper.execute(oValue);
            assertEquals(oResult.toString(), "!@#$%^&*()_+-=[]{}|;':\",./<>?");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperReturnTypeIsStringValue() {
        try {
            StringValue oValue = new StringValue("hello");
            Object oResult = Upper.execute(oValue);
            assertEquals(oResult instanceof StringValue, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperWithIntegerThrowsException() {
        try {
            IntegerValue oValue = new IntegerValue(123);
            assertThrows(RuntimeException.class, () -> {
                Upper.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperWithRealThrowsException() {
        try {
            RealValue oValue = new RealValue(123.45);
            assertThrows(RuntimeException.class, () -> {
                Upper.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperConsecutiveExecution() {
        try {
            StringValue oValue1 = new StringValue("hello");
            StringValue oResult1 = (StringValue) Upper.execute(oValue1);
            assertEquals(oResult1.toString(), "HELLO");

            StringValue oValue2 = new StringValue("world");
            StringValue oResult2 = (StringValue) Upper.execute(oValue2);
            assertEquals(oResult2.toString(), "WORLD");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperIdempotence() {
        try {
            StringValue oValue = new StringValue("hello");
            StringValue oResult1 = (StringValue) Upper.execute(oValue);
            StringValue oResult2 = (StringValue) Upper.execute(oResult1);
            assertEquals(oResult1.toString(), oResult2.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperAndLowerRoundTrip() {
        try {
            String original = "Hello World";
            StringValue oValue = new StringValue(original);

            StringValue oUpper = (StringValue) Upper.execute(oValue);
            assertEquals(oUpper.toString(), "HELLO WORLD");

            StringValue oLower = (StringValue) Lower.execute(oUpper);
            assertEquals(oLower.toString(), "hello world");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
