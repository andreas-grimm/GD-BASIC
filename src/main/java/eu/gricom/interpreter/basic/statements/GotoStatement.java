package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.helper.Logger;
import eu.gricom.interpreter.basic.helper.MemoryManagement;
import eu.gricom.interpreter.basic.helper.Labels;

/**
 * A "goto" statement jumps execution to another place in the program.
 */
public final class GotoStatement implements Statement {
    private Logger _oLogger = new Logger(this.getClass().getName());
    private final String _strLabel;
    private MemoryManagement oMemoryManager = new MemoryManagement();
    private Labels oLabels = new Labels();

    public GotoStatement(String strLabel) {
        _strLabel = strLabel;
    }

    public void execute() {
        if (oMemoryManager.containsLabelKey(_strLabel)) {
            oMemoryManager.setCurrentStatement(oMemoryManager.getLabelStatement(_strLabel));
        } else {
            _oLogger.error("GOTO [unknown]" );
        }
    }

    @Override
    public String content() {
        return ("GOTO [" + _strLabel + "]: Destination: " + oMemoryManager.getLabelStatement(_strLabel));
    }
}