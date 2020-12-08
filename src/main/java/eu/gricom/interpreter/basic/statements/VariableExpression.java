package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.helper.Logger;
import eu.gricom.interpreter.basic.helper.MemoryManagement;

/**
 * A variable expression evaluates to the current value stored in that
 * variable.
 */
public final class VariableExpression implements Expression {
    private Logger _oLogger = new Logger(this.getClass().getName());

    private final String _strName;

    public VariableExpression(String strName) {
        _strName = strName;
    }

    public Value evaluate() {
        MemoryManagement oMemoryManager = new MemoryManagement();

        if (oMemoryManager.mapContainsKey(_strName)) {
            return oMemoryManager.getMap(_strName);
        }
        return new NumberValue(0);
    }

    public final String getName() {return (_strName); }
    public final String content() {return (getName()); }
}
