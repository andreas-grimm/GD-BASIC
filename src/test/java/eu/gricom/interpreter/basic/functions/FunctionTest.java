package eu.gricom.interpreter.basic.functions;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.statements.Expression;
import eu.gricom.interpreter.basic.tokenizer.Token;
import eu.gricom.interpreter.basic.tokenizer.TokenType;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import eu.gricom.interpreter.basic.variableTypes.LongValue;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
import eu.gricom.interpreter.basic.variableTypes.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FunctionTest {

    @Test
    public void testFunctionForInteger() {

        try {
            IntegerValue oValue = new IntegerValue(-1);
            Function oFunction = new Function(new Token("ABS", TokenType.ABS, 10), oValue);

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
            Function oFunction = new Function(new Token("ABS", TokenType.ABS, 10), oValue);

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
            Function oFunction = new Function(new Token("ABS", TokenType.ABS, 10), oValue);

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
            Function oFunction = new Function(new Token("ABS", TokenType.ABS, 10), oValue);

            assertThrows(RuntimeException.class, () -> {
                oFunction.evaluate();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
