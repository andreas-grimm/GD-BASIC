package eu.gricom.interpreter.basic.functions;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.statements.Expression;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import eu.gricom.interpreter.basic.variableTypes.LongValue;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
import eu.gricom.interpreter.basic.variableTypes.Value;

/**
 * LEN Function.
 *
 * Description:
 *
 * The LEN function delivers the lenght of the parameter value. This parameter has to be of type String.
 *
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public final class Len {

    /**
     * Private Constructor.
     */
    private Len() {
    }

    /**
     * Functions implemented here are similar to Statements with the difference
     * that they actually return a result to the caller of type Value. The method execute
     * triggers the function.
     *
     * @param oValue input value
     * @return Value the return message of the function
     * @throws Exception as any execution error found during execution
     */
    public static Value execute(final Value oValue) throws Exception {
        if (oValue instanceof StringValue) {
                return new IntegerValue(oValue.toString().length());

        }

        throw new RuntimeException("Input value not of type String: " + oValue);
    }
}
