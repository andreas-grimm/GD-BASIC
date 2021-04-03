package eu.gricom.interpreter.basic.functions;

import eu.gricom.interpreter.basic.error.UndefinedUserFunctionException;
import eu.gricom.interpreter.basic.statements.Expression;
import eu.gricom.interpreter.basic.variableTypes.Value;
import java.util.List;


/**
 * FnFunction.java
 *
 * Description:
 *
 * FN Function execute: Uses the name of the function and a list of parameter to find the prior defined function,
 * replace the parameter, and evaluate the result.
 *
 * (c) = 2021 by Andreas Grimm, Den Haag, The Netherlands
 *
 * Created in 2021
 */
public class FnFunction implements Expression {
    private final String _strFunctionId;
    private final List<Expression> _aoFunctionParameter;


    /**
     * Default Constructor.
     *
     * @param strFunctionId name of the function to be executed
     * @param aoFunctionParameter list of parameters of the function
     */
    public FnFunction(final String strFunctionId,
                      final List<Expression> aoFunctionParameter) {
        _strFunctionId = strFunctionId;
        _aoFunctionParameter = aoFunctionParameter;
    }

    /**
     * This method is used to trigger the function selected.
     *
     * @return - value containing the result of the function
     * @throws Exception - when the processing is running into a problem
     */
    public final Value evaluate() throws Exception {
        throw new UndefinedUserFunctionException("Unknown Function Called: " + _strFunctionId);
    }

    /**
     * This method is used in testing and debugging. It returns the set values when the constructor has been called.
     *
     * @return - readable string with the name and the value of the assignment
     */
    public final String content() {
        String strReturn = "FN Function: " + _strFunctionId + " Parameter: <";


        strReturn += ">";

        return strReturn;
    }
}
