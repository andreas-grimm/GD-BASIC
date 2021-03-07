package eu.gricom.interpreter.basic.functions;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.statements.Expression;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import eu.gricom.interpreter.basic.variableTypes.LongValue;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.Value;

/**
 * ABS Function.
 *
 * Description:
 *
 * The ABS function delivers the absolute value of the parameter. This parameter has to be numeric.
 *
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public final class Abs {

    /**
     * Private Constructor.
     */
    private Abs() {
    }

    /**
     * Functions implemented here are similar to Statements with the difference
     * that they actually return a result to the caller of type Value. The method execute
     * triggers the function.
     *
     * @param oExpression input value
     * @return Value the return message of the function
     * @throws Exception as any execution error found during execution
     */
    public static Value execute(final Expression oExpression) throws Exception {
        Value oValue = oExpression.evaluate();

        if (oValue instanceof IntegerValue) {
            if (((IntegerValue) oValue).toInt() < 0) {
                return oValue.multiply(new IntegerValue(-1));
            } else {
                return oValue;
            }
        } else
        if (oValue instanceof LongValue) {
            if (((LongValue) oValue).toLong() < 0) {
                return oValue.multiply(new LongValue(-1));
            } else {
                return oValue;
            }
        } else
        if (oValue instanceof RealValue) {
            if (oValue.toReal() < 0) {
                return oValue.multiply(new RealValue(-1));
            } else {
                return oValue;
            }
        }

        throw new RuntimeException("Input value not numeric: " + oValue);
    }
}
