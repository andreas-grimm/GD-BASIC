package eu.gricom.basic.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

public class RightTest {
    @Test
    public void testRight() {
        try {
            StringValue oValue = new StringValue("abcde");
            IntegerValue oLength = new IntegerValue(2);

            StringValue oResult = (StringValue) Right.execute(oValue, oLength);

            assertEquals("de", oResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Positive: a length equal to the string length returns the whole string.
    @Test
    public void testRightLengthEqualToStringLength() throws Exception {
        assertEquals("abc", Right.execute(new StringValue("abc"), new IntegerValue(3)).toString());
        assertEquals("a", Right.execute(new StringValue("a"), new IntegerValue(1)).toString());
    }

    // Positive: a length of zero returns an empty string.
    @Test
    public void testRightLengthZero() throws Exception {
        assertEquals("", Right.execute(new StringValue("abc"), new IntegerValue(0)).toString());
    }

    // Positive: a length larger than the string returns the whole string.
    @Test
    public void testRightLengthLargerThanString() throws Exception {
        assertEquals("abc", Right.execute(new StringValue("abc"), new IntegerValue(4)).toString());
    }

    @Test
    public void testRightEmptyParam() {

        try {
            StringValue oValue = new StringValue("");
            IntegerValue oLength = new IntegerValue(2);

            // Length 2 is greater than the length 0 of the string: the whole (empty) string is returned.
            assertEquals("", Right.execute(oValue, oLength).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRightWithException() {
        try {
            RealValue oValue = new RealValue(-1);
            IntegerValue oLength = new IntegerValue(2);

            assertThrows(RuntimeException.class, () -> {
                Right.execute(oValue, oLength);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
