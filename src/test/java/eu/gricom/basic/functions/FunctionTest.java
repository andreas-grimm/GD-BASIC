package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.tokenizer.BasicTokenType;
import eu.gricom.basic.tokenizer.Token;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.LongValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FunctionTest {

    private static final String ABS_TOKEN_NAME = new String("ABS");

    @Test
    public void testFunctionForInteger() {

        try {
            IntegerValue oValue = new IntegerValue(-1);
            Function oFunction = new Function(new Token(ABS_TOKEN_NAME, BasicTokenType.ABS, 10), oValue);

            IntegerValue oResult = (IntegerValue) oFunction.evaluate();

            assertEquals(oResult.toInt(), 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFunctionForReal() {

        try {
            RealValue oValue = new RealValue(-1.0);
            Function oFunction = new Function(new Token(ABS_TOKEN_NAME, BasicTokenType.ABS, 10), oValue);

            RealValue oResult = (RealValue) oFunction.evaluate();

            assertEquals(oResult.toReal(), 1.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFunctionForLong() {

        try {
            LongValue oValue = new LongValue(-1);
            Function oFunction = new Function(new Token(ABS_TOKEN_NAME, BasicTokenType.ABS, 10), oValue);

            LongValue oResult = (LongValue) oFunction.evaluate();

            assertEquals(oResult.toLong(), 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFunctionWithException() {

        try {
            StringValue oValue = new StringValue("-1");
            Function oFunction = new Function(new Token(ABS_TOKEN_NAME, BasicTokenType.ABS, 10), oValue);

            assertThrows(RuntimeException.class, () -> {
                oFunction.evaluate();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperFunctionBasic() {
        try {
            StringValue oValue = new StringValue("hello");
            Function oFunction = new Function(new Token("UPPER", BasicTokenType.UPPER, 10), oValue);

            StringValue oResult = (StringValue) oFunction.evaluate();

            assertEquals(oResult.toString(), "HELLO");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperFunctionMixedCase() {
        try {
            StringValue oValue = new StringValue("HeLLo WoRLd");
            Function oFunction = new Function(new Token("UPPER", BasicTokenType.UPPER, 10), oValue);

            StringValue oResult = (StringValue) oFunction.evaluate();

            assertEquals(oResult.toString(), "HELLO WORLD");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperFunctionWithNumbers() {
        try {
            StringValue oValue = new StringValue("Test123ABC");
            Function oFunction = new Function(new Token("UPPER", BasicTokenType.UPPER, 10), oValue);

            StringValue oResult = (StringValue) oFunction.evaluate();

            assertEquals(oResult.toString(), "TEST123ABC");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperFunctionEmpty() {
        try {
            StringValue oValue = new StringValue("");
            Function oFunction = new Function(new Token("UPPER", BasicTokenType.UPPER, 10), oValue);

            StringValue oResult = (StringValue) oFunction.evaluate();

            assertEquals(oResult.toString(), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperFunctionWithSpecialChars() {
        try {
            StringValue oValue = new StringValue("hello!@#$%world");
            Function oFunction = new Function(new Token("UPPER", BasicTokenType.UPPER, 10), oValue);

            StringValue oResult = (StringValue) oFunction.evaluate();

            assertEquals(oResult.toString(), "HELLO!@#$%WORLD");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperFunctionReturnType() {
        try {
            StringValue oValue = new StringValue("test");
            Function oFunction = new Function(new Token("UPPER", BasicTokenType.UPPER, 10), oValue);

            Object oResult = oFunction.evaluate();

            assertEquals(oResult instanceof StringValue, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperFunctionWithIntegerThrowsException() {
        try {
            IntegerValue oValue = new IntegerValue(123);
            Function oFunction = new Function(new Token("UPPER", BasicTokenType.UPPER, 10), oValue);

            assertThrows(RuntimeException.class, () -> {
                oFunction.evaluate();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerFunctionBasic() {
        try {
            StringValue oValue = new StringValue("HELLO");
            Function oFunction = new Function(new Token("LOWER", BasicTokenType.LOWER, 10), oValue);

            StringValue oResult = (StringValue) oFunction.evaluate();

            assertEquals(oResult.toString(), "hello");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerFunctionMixedCase() {
        try {
            StringValue oValue = new StringValue("HeLLo WoRLd");
            Function oFunction = new Function(new Token("LOWER", BasicTokenType.LOWER, 10), oValue);

            StringValue oResult = (StringValue) oFunction.evaluate();

            assertEquals(oResult.toString(), "hello world");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerFunctionWithNumbers() {
        try {
            StringValue oValue = new StringValue("TEST123abc");
            Function oFunction = new Function(new Token("LOWER", BasicTokenType.LOWER, 10), oValue);

            StringValue oResult = (StringValue) oFunction.evaluate();

            assertEquals(oResult.toString(), "test123abc");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerFunctionEmpty() {
        try {
            StringValue oValue = new StringValue("");
            Function oFunction = new Function(new Token("LOWER", BasicTokenType.LOWER, 10), oValue);

            StringValue oResult = (StringValue) oFunction.evaluate();

            assertEquals(oResult.toString(), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerFunctionWithSpecialChars() {
        try {
            StringValue oValue = new StringValue("HELLO!@#$%WORLD");
            Function oFunction = new Function(new Token("LOWER", BasicTokenType.LOWER, 10), oValue);

            StringValue oResult = (StringValue) oFunction.evaluate();

            assertEquals(oResult.toString(), "hello!@#$%world");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerFunctionReturnType() {
        try {
            StringValue oValue = new StringValue("TEST");
            Function oFunction = new Function(new Token("LOWER", BasicTokenType.LOWER, 10), oValue);

            Object oResult = oFunction.evaluate();

            assertEquals(oResult instanceof StringValue, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerFunctionWithIntegerThrowsException() {
        try {
            IntegerValue oValue = new IntegerValue(123);
            Function oFunction = new Function(new Token("LOWER", BasicTokenType.LOWER, 10), oValue);

            assertThrows(RuntimeException.class, () -> {
                oFunction.evaluate();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperAndLowerRoundTrip() {
        try {
            StringValue original = new StringValue("Hello");

            Function oUpper = new Function(new Token("UPPER", BasicTokenType.UPPER, 10), original);
            StringValue upper = (StringValue) oUpper.evaluate();

            Function oLower = new Function(new Token("LOWER", BasicTokenType.LOWER, 10), upper);
            StringValue lower = (StringValue) oLower.evaluate();

            assertEquals(upper.toString(), "HELLO");
            assertEquals(lower.toString(), "hello");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
