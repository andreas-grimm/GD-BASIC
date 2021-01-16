package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
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
    private final int _iLineNumber;
    private final ProgramPointer _oProgramPointer = new ProgramPointer();
    private final LabelStatement _oLabelStatement = new LabelStatement();
    private final LineNumberStatement _oLineNumber = new LineNumberStatement();

    /**
     * Default constructor.
     *
     * @param strLabel - target of the jump - defined by a label
     */
    public GotoStatement(final String strLabel) {
        _iLineNumber = 0;
        _strLabel = strLabel;
    }

    /**
     * Default constructor.
     * @param iLineNumber - number of the line of the command
     * @param strLabel - target of the jump - defined by a label
     */
    public GotoStatement(final int iLineNumber, final String strLabel) {
        _iLineNumber = iLineNumber;
        _strLabel = strLabel;
    }

    /**
     * Get Line Number.
     *
     * @return iLineNumber - the command line number of the statement
     */
    @Override
    public int getLineNumber() {
        return (_iLineNumber);
    }

    /**
     * Execute the transaction.
     */
    public void execute() throws SyntaxErrorException {
        // This part of the method is executed if the BASIC interpreter uses labels (e.g. we are using JASIC)
        if (_oLabelStatement.containsLabelKey(_strLabel)) {
            _oLogger.debug("-execute-> jump to [" + _strLabel + "]");
            _oProgramPointer.setCurrentStatement(_oLabelStatement.getLabelStatement(_strLabel));
            _oLogger.debug("-execute-> jump to [" + _oLabelStatement.getLabelStatement(_strLabel) + "]");
            return;
        }

        // here we are using line numbers to jump to
        try {
            int iTokenNo = _oLineNumber.getStatement(Integer.parseInt(_strLabel));
            _oLogger.debug("-execute-> jump to Token [" + iTokenNo + "]");

            if (iTokenNo != 0) {
                _oProgramPointer.setCurrentStatement(iTokenNo);
                return;
            }

            throw (new SyntaxErrorException("GOTO [unknown]: Target: " + _strLabel));
        } catch (NumberFormatException eNumberException) {
            throw (new SyntaxErrorException("GOTO [incorrect format]: Target: " + _strLabel));
        }
    }

    /**
     * This method is used in testing and debugging. It returns the set values when the constructor has been called.
     *
     * @return - readable string with the name and the value of the assignment
     */
    @Override
    public String content() {
        if (_oLabelStatement.containsLabelKey(_strLabel)) {
            return ("GOTO [" + _strLabel + "]: Destination: " + _oLabelStatement.getLabelStatement(_strLabel));
        }

        return ("GOTO [" + _strLabel + "]: Destination: " + _oLineNumber.getStatement(Integer.parseInt(_strLabel)));
    }
}
