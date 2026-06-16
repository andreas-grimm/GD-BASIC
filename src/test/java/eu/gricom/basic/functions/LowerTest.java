package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LowerTest {

    @Test
    public void testLowerBasicUppercaseString() {
        try {
            StringValue oValue = new StringValue("HELLO");
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), "hello");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerBasicLowercaseString() {
        try {
            StringValue oValue = new StringValue("hello");
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), "hello");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerMixedCaseString() {
        try {
            StringValue oValue = new StringValue("HeLLo WoRLd");
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), "hello world");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerEmptyString() {
        try {
            StringValue oValue = new StringValue("");
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerSingleCharacterUppercase() {
        try {
            StringValue oValue = new StringValue("A");
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), "a");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerSingleCharacterLowercase() {
        try {
            StringValue oValue = new StringValue("a");
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), "a");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerWithNumbers() {
        try {
            StringValue oValue = new StringValue("HELLO123WORLD");
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), "hello123world");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerWithSpecialCharacters() {
        try {
            StringValue oValue = new StringValue("HELLO!@#$%");
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), "hello!@#$%");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerWithSpaces() {
        try {
            StringValue oValue = new StringValue("HELLO   WORLD");
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), "hello   world");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerWithTabs() {
        try {
            StringValue oValue = new StringValue("HELLO\tWORLD");
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), "hello\tworld");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerWithNewlines() {
        try {
            StringValue oValue = new StringValue("HELLO\nWORLD");
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), "hello\nworld");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerWithPunctuation() {
        try {
            StringValue oValue = new StringValue("HELLO, WORLD!");
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), "hello, world!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerWithQuotes() {
        try {
            StringValue oValue = new StringValue("\"HELLO\"");
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), "\"hello\"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerWithApostrophe() {
        try {
            StringValue oValue = new StringValue("DON'T");
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), "don't");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerWithMultipleWords() {
        try {
            StringValue oValue = new StringValue("THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG");
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), "the quick brown fox jumps over the lazy dog");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerWithLeadingSpaces() {
        try {
            StringValue oValue = new StringValue("   HELLO");
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), "   hello");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerWithTrailingSpaces() {
        try {
            StringValue oValue = new StringValue("HELLO   ");
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), "hello   ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerWithSurroundingSpaces() {
        try {
            StringValue oValue = new StringValue("   HELLO   ");
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), "   hello   ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerWithNumericString() {
        try {
            StringValue oValue = new StringValue("123456789");
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), "123456789");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerWithAlphanumericMixed() {
        try {
            StringValue oValue = new StringValue("ABC123DEF456GHI");
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), "abc123def456ghi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerLongString() {
        try {
            String longStr = "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG. " +
                           "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG. " +
                           "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG.";
            StringValue oValue = new StringValue(longStr);
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), longStr.toLowerCase());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerWithAllSpecialCharacters() {
        try {
            StringValue oValue = new StringValue("!@#$%^&*()_+-=[]{}|;':\",./<>?");
            StringValue oResult = (StringValue) Lower.execute(oValue);
            assertEquals(oResult.toString(), "!@#$%^&*()_+-=[]{}|;':\",./<>?");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerReturnTypeIsStringValue() {
        try {
            StringValue oValue = new StringValue("HELLO");
            Object oResult = Lower.execute(oValue);
            assertEquals(oResult instanceof StringValue, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerWithIntegerThrowsException() {
        try {
            IntegerValue oValue = new IntegerValue(123);
            assertThrows(RuntimeException.class, () -> {
                Lower.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerWithRealThrowsException() {
        try {
            RealValue oValue = new RealValue(123.45);
            assertThrows(RuntimeException.class, () -> {
                Lower.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerConsecutiveExecution() {
        try {
            StringValue oValue1 = new StringValue("HELLO");
            StringValue oResult1 = (StringValue) Lower.execute(oValue1);
            assertEquals(oResult1.toString(), "hello");

            StringValue oValue2 = new StringValue("WORLD");
            StringValue oResult2 = (StringValue) Lower.execute(oValue2);
            assertEquals(oResult2.toString(), "world");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerIdempotence() {
        try {
            StringValue oValue = new StringValue("HELLO");
            StringValue oResult1 = (StringValue) Lower.execute(oValue);
            StringValue oResult2 = (StringValue) Lower.execute(oResult1);
            assertEquals(oResult1.toString(), oResult2.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
