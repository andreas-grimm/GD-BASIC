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
}
