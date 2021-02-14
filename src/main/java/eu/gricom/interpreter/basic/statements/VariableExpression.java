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
    public Value evaluate() throws RuntimeException {
        VariableManagement oVariableManager = new VariableManagement();

        if (oVariableManager.mapContainsKey(_strName)) {
            return oVariableManager.getMap(_strName);
        }

        return new RealValue(0);
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
