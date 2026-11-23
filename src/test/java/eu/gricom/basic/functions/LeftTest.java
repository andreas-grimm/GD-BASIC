package eu.gricom.basic.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

public class LeftTest {
    @Test
    public void testLeft() {
        try {
            StringValue oValue = new StringValue("abcd");
            IntegerValue oLength = new IntegerValue(2);

            StringValue oResult = (StringValue) Left.execute(oValue, oLength);

            assertEquals("ab", oResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Positive: a length equal to the string length returns the whole string.
    @Test
    public void testLeftLengthEqualToStringLength() throws Exception {
        assertEquals("abc", Left.execute(new StringValue("abc"), new IntegerValue(3)).toString());
        assertEquals("a", Left.execute(new StringValue("a"), new IntegerValue(1)).toString());
    }

    // Positive: a length of zero returns an empty string.
    @Test
    public void testLeftLengthZero() throws Exception {
        assertEquals("", Left.execute(new StringValue("abc"), new IntegerValue(0)).toString());
    }

    // Positive: a length larger than the string returns the whole string.
    @Test
    public void testLeftLengthLargerThanString() throws Exception {
        assertEquals("abc", Left.execute(new StringValue("abc"), new IntegerValue(4)).toString());
    }

    @Test
    public void testLeftEmptyParam() {

        try {
            StringValue oValue = new StringValue("");
            IntegerValue oLength = new IntegerValue(2);

            // Length 2 is greater than the length 0 of the string: the whole (empty) string is returned.
            assertEquals("", Left.execute(oValue, oLength).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLeftWithException() {
        try {
            RealValue oValue = new RealValue(-1);
            IntegerValue oLength = new IntegerValue(2);

            assertThrows(RuntimeException.class, () -> {
                Left.execute(oValue, oLength);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
