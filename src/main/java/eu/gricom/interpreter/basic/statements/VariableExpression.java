package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.helper.Logger;
import eu.gricom.interpreter.basic.helper.MemoryManagement;
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
 * Created in 2003
 *
 */
public final class VariableExpression implements Expression {
    private Logger _oLogger = new Logger(this.getClass().getName());

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
     */
    public Value evaluate() {
        MemoryManagement oMemoryManager = new MemoryManagement();

        if (oMemoryManager.mapContainsKey(_strName)) {
            return (oMemoryManager.getMap(_strName));
        }
        return (new RealValue(0));
    }

    /**
     * Get the name of the variable.
     *
     * @return name of the vaiable as a string.
     */
    public String getName() {

        return (_strName);
    }

    /**
     * This method is used in testing and debugging. It returns the set values when the constructor has been called.
     *
     * @return - readable string with the name and the value of the assignment
     */
    public String content() {

        return (getName());
    }
}
