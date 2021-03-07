package eu.gricom.interpreter.basic.functions;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.statements.Expression;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.Value;

/**
 * EXP Function.
 *
 * Description:
 *
 * The LOG function returns the logarithm naturalis (to the basis 'e') of the input value. This parameter has to be
 * numeric.
 *
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public final class Log {

    /**
     * Private Constructor.
     */
    private Log() {
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

        if (oValue instanceof RealValue) {
            return new RealValue(Math.log(oValue.toReal()));
        }

        throw new RuntimeException("Input value not of type Real: " + oValue);
    }

}
