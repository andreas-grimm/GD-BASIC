package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.memoryManager.ProgramPointer;

/**
 */
/**
 * IfThenStatement.java
 * <p>
 * Description:
 * <p>
 * An if then statement jumps execution to another place in the program, but only if an expression evaluates to
 * something other than 0.
 * <p>
 * (c) = 2004,..,2016 by Andreas Grimm, Den Haag, The Netherlands
 * <p>
 * Created in 2003
 *
 */
public final class IfThenStatement implements Statement {

    private final Expression _oCondition;
    private final String _strLabel;
    private ProgramPointer _oProgramPointer = new ProgramPointer();
    private LabelStatement _oLabelStatement = new LabelStatement();

    /**
     * Default constructor.
     *
     * @param oCondition - condition to be tested.
     * @param strLabel - destination for the jump after successful completion of the condition.
     */
    public IfThenStatement(final Expression oCondition, final String strLabel) {
        _oCondition = oCondition;
        _strLabel = strLabel;
    }

    /**
     * Execute the If statement.
     *
     * @throws Exception - exposes any exception coming from the memory management
     */
    public void execute() throws Exception {
        if (_oLabelStatement.containsLabelKey(_strLabel)) {
            double value = _oCondition.evaluate().toReal();
            if (value != 0) {
                _oProgramPointer.setCurrentStatement(_oLabelStatement.getLabelStatement(_strLabel));
            }
        }
    }

    /**
     * This method is used in testing and debugging. It returns the set values when the constructor has been called.
     *
     * @return - readable string with the name and the value of the assignment
     */
    @Override
    public String content() {

        return ("IF (" + _oCondition.content() + ") THEN " + _strLabel);
    }
}
