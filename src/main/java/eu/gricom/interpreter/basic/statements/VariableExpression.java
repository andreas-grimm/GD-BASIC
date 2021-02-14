package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.memoryManager.VariableManagement;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.Value;

 /**
 * VariableExpression.java
 * <p>
 * Description:
 * <p>
 * A variable expression evaluates to the current value stored in that variable.
 * <p>
 * (c) = 2004,..,2016 by Andreas Grimm, Den Haag, The Netherlands
 * <p>
 * Created in 2020
 *
 */
public final class VariableExpression implements Expression {

    private final String _strName;

    /**
     * Default constructor.
     *
     * @param strName name of the variable.
     */
    public VariableExpression(final String strName) {

        _strName = strName;
    }

    /**
     * Return the content of the variable in the Memory Management component.
     *
     * @return returns the value of the variable - or if the variable does not exists - returns a 0 as a numerical value
     * @throws RuntimeException for any errors occuring in the execution of the evaluation. Currently this happens if
     * the index in an array subscription is larger than the array.
     */
    public Value evaluate() throws Exception {
        VariableManagement oVariableManager = new VariableManagement();
        String strKey = _strName;

        int iIndexStart = strKey.indexOf("(");
        int iIndexEnd = strKey.indexOf(")");

        if (iIndexStart > 0 && iIndexEnd > 0) {
            String strInner = strKey.substring(iIndexStart +1, iIndexEnd);

            if (oVariableManager.mapContainsKey(strInner)) {
                Expression oExpression = new VariableExpression(strInner);

                strKey = strKey.substring(0, iIndexStart + 1)
                        + oExpression.evaluate().toString()
                        + strKey.substring(iIndexEnd);

            }
        }

        if (oVariableManager.mapContainsKey(strKey)) {
            return oVariableManager.getMap(strKey);
        }

        throw new RuntimeException("Unknown variable " + strKey);
    }

    /**
     * Get the name of the variable.
     *
     * @return name of the vaiable as a string.
     */
    public String getName() {

        return _strName;
    }

    /**
     * This method is used in testing and debugging. It returns the set values when the constructor has been called.
     *
     * @return - readable string with the name and the value of the assignment
     */
    public String content() {

        return getName();
    }
}
