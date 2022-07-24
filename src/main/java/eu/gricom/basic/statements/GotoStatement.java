package eu.gricom.basic.statements;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.memoryManager.LineNumberXRef;
import eu.gricom.basic.memoryManager.ProgramPointer;

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
    private final String _strTarget;
    private final int _iTokenNumber;
    private final ProgramPointer _oProgramPointer = new ProgramPointer();
    private final LabelStatement _oLabelStatement = new LabelStatement();
    private final LineNumberXRef _oLineNumberObject = new LineNumberXRef();

    /**
     * Default constructor.
     *
     * @param strTarget - target of the jump - defined by a label
     */
    public GotoStatement(final String strTarget) {
        _iTokenNumber = 0;
        _strTarget = strTarget;
    }

    /**
     * Default constructor.
     * @param iTokenNumber - number of the line of the command
     * @param strTarget - target of the jump - defined by a label
     */
    public GotoStatement(final int iTokenNumber, final String strTarget) {
        _iTokenNumber = iTokenNumber;
        _strTarget = strTarget;
    }

    /**
     * Get Token Number.
     *
     * @return the command line number of the statement
     */
    @Override
    public int getTokenNumber() {
        return _iTokenNumber;
    }

    /**
     * Execute the transaction.
     *
     * @throws SyntaxErrorException for unknown or incorrect formatted targets
     */
    public void execute() throws SyntaxErrorException {
        // This part of the method is executed if the BASIC interpreter uses labels (e.g. we are using JASIC)
        if (_oLabelStatement.containsLabelKey(_strTarget)) {
            _oLogger.debug("-execute-> jump to [" + _strTarget + "]");
            _oProgramPointer.setCurrentStatement(_oLabelStatement.getLabelStatement(_strTarget));
            _oLogger.debug("-execute-> jump to [" + _oLabelStatement.getLabelStatement(_strTarget) + "]");
            return;
        }

        // here we are using line numbers to jump to the destination. This is only done for BASIC programs.
        try {
            int iTokenNo = _oLineNumberObject.getStatementFromLineNumber(Integer.parseInt(_strTarget));

            if (iTokenNo != 0) {
                _oProgramPointer.setCurrentStatement(iTokenNo);
                return;
            }

            throw new SyntaxErrorException("GOTO [unknown]: Target: " + _strTarget);
        } catch (NumberFormatException eNumberException) {
            throw new SyntaxErrorException("GOTO [incorrect format]: Target: " + _strTarget);
        } catch (RuntimeException e) {
            throw new SyntaxErrorException("GOTO [incorrect format]: Target: " + _strTarget);
        }
    }

    /**
     * This method is used in testing and debugging. It returns the set values when the constructor has been called.
     *
     * @return - readable string with the name and the value of the assignment
     */
    @Override
    public String content() throws RuntimeException {
        if (_oLabelStatement.containsLabelKey(_strTarget)) {
            return "GOTO [" + _strTarget + "]: Destination: " + _oLabelStatement.getLabelStatement(_strTarget);
        }

        return "GOTO [" + _strTarget + "]: Destination: " + _oLineNumberObject.getStatementFromLineNumber(Integer.parseInt(_strTarget));
    }
}
