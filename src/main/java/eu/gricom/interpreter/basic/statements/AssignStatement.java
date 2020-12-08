package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.helper.MemoryManagement;

/**
 * An assignment statement evaluates an expression and stores the result in
 * a variable.
 */
public final class AssignStatement implements Statement {
    private final String _strName;
    private final Expression _oValue;
    private MemoryManagement _oMemoryManagement = new MemoryManagement();

    public AssignStatement(String strName, Expression oValue) {
        _strName = strName;
        _oValue = oValue;
    }

    @Override
    public void execute() throws Exception {
        _oMemoryManagement.putMap(_strName, _oValue.evaluate().toNumber());
    }

    @Override
    public String content() {
        return ("ASSIGN [" + _strName + ":= " + _oValue.content() + "]");
    }
}
