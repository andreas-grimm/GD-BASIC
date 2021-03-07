package eu.gricom.interpreter.basic.functions;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.statements.Expression;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
import eu.gricom.interpreter.basic.variableTypes.Value;

/**
 * INSTR Function.
 *
 * Description:
 *
 * The INSTR function delivers the absolute value of the parameter. This parameter has to be numeric.
 *
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public final class Instr {

    /**
     * Private Constructor.
     */
    private Instr() {
    }

    /**
     * Functions implemented here are similar to Statements with the difference
     * that they actually return a result to the caller of type Value. The method execute
     * triggers the function.
     *
     * @param oExpression input value
     * @param oSearch search string
     * @return Value the return message of the function
     * @throws Exception as any execution error found during execution
     */
    public static Value execute(final Expression oExpression, final Expression oSearch) throws Exception {
        Value oValue = oExpression.evaluate();
        Value oSearchValue = oSearch.evaluate();

        if (oValue instanceof StringValue
                && oSearchValue instanceof StringValue) {

            if (oValue.toString().length() < 1 ||
                    oSearchValue.toString().length() < 1) {
                throw new RuntimeException("Input string or search string is empty");
            }

            return new IntegerValue(oValue.toString().indexOf(oSearchValue.toString()));
        }

        throw new RuntimeException("Input values are not String");
    }
}
