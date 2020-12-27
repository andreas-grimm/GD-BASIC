package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.helper.Logger;
import eu.gricom.interpreter.basic.memoryManager.ProgramPointer;

/**
 * GoToStatement.java
 * <p>
 * Description:
 * <p>
 * A "goto" statement jumps execution to another place in the program.
 * <p>
 * (c) = 2004,..,2016 by Andreas Grimm, Den Haag, The Netherlands
 * <p>
 * Created in 2003
 *
 */
public final class GotoStatement implements Statement {
    private final Logger _oLogger = new Logger(this.getClass().getName());
    private final String _strLabel;
    private final ProgramPointer _oProgramPointer = new ProgramPointer();
    private final LabelStatement _oLabelStatement = new LabelStatement();

    /**
     * Default constructor.
     *
     * @param strLabel - target of the jump - defined by a label
     */
    public GotoStatement(final String strLabel) {
        _strLabel = strLabel;
    }

    /**
     * Execute the transaction.
     */
    public void execute() {
        if (_oLabelStatement.containsLabelKey(_strLabel)) {
            _oProgramPointer.setCurrentStatement(_oLabelStatement.getLabelStatement(_strLabel));
        } else {
            // TODO This should be a syntax error thrown...
            _oLogger.error("GOTO [unknown]");
        }
    }

    /**
     * This method is used in testing and debugging. It returns the set values when the constructor has been called.
     *
     * @return - readable string with the name and the value of the assignment
     */
    @Override
    public String content() {
        return ("GOTO [" + _strLabel + "]: Destination: " + _oLabelStatement.getLabelStatement(_strLabel));
    }
}
