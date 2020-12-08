package eu.gricom.interpreter.basic.statements;
/**
 * An if then statement jumps execution to another place in the program, but
 * only if an expression evaluates to something other than 0.
 */

import eu.gricom.interpreter.basic.helper.MemoryManagement;

public final class IfThenStatement implements Statement {

    private final Expression _oCondition;
    private final String _strLabel;
    private MemoryManagement _oMemoryManagement = new MemoryManagement();

    public IfThenStatement(Expression oCondition, String strLabel) {
        _oCondition = oCondition;
        _strLabel = strLabel;
    }

    public void execute() throws Exception {
        if (_oMemoryManagement.containsLabelKey(_strLabel)) {
            double value = _oCondition.evaluate().toNumber();
            if (value != 0) {
                _oMemoryManagement.setCurrentStatement(_oMemoryManagement.getLabelStatement(_strLabel));
            }
        }
    }

    @Override
    public String content() {
        return ("IF (" + _oCondition.content() + ") THEN " + _strLabel);
    }
}