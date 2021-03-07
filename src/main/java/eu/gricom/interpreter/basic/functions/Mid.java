package eu.gricom.interpreter.basic.functions;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.statements.Expression;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import eu.gricom.interpreter.basic.variableTypes.LongValue;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
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
public final class Mid {

    /**
     * Private Constructor.
     */
    private Mid() {
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
    public static Value execute(final Expression oExpression,
                                final Expression oStartPosition,
                                final Expression oEndPosition) throws Exception {
        Value oValue = oExpression.evaluate();
        Value oStartValue = oStartPosition.evaluate();
        Value oEndValue = oEndPosition.evaluate();

        if (oValue instanceof StringValue
                && oStartValue instanceof IntegerValue
                && oEndValue instanceof IntegerValue) {
            String strValue = oValue.toString();
            int iStart = ((IntegerValue) oStartValue).toInt();
            int iEnd = ((IntegerValue) oEndValue).toInt();

            if (iStart < 0
                || iEnd < 0
                || iStart > iEnd
                || iEnd > oValue.toString().length()) {
                throw new RuntimeException("Requested parameter does not fit the String");
            }

            return new StringValue(strValue.substring(iStart, iEnd + 1));
        }

        throw new RuntimeException("Input value not String, parameter not integer");
    }
}
