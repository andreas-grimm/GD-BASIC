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
 * (c) = 2004,...,2016 by Andreas Grimm, Den Haag, The Netherlands
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
     * Transfers program execution to the statement identified by the target label or line number.
     *
     * If the target is a recognized label, execution jumps to the corresponding statement. If the target is a line number, execution jumps to the statement at that line. Throws a {@code SyntaxErrorException} if the target is unknown or incorrectly formatted.
     *
     * @throws SyntaxErrorException if the target label or line number does not exist or is incorrectly formatted
     */
    public void execute() throws SyntaxErrorException {
        // This part of the method is executed if the BASIC interpreter uses labels (e.g., we are using JASIC)
        if (_oLabelStatement.containsLabelKey(_strTarget)) {
            _oLogger.debug("-execute-> jump to [" + _strTarget + "]");
            _oProgramPointer.setCurrentStatement(_oLabelStatement.getLabelStatement(_strTarget));
            _oLogger.debug("-execute-> jump to [" + _oLabelStatement.getLabelStatement(_strTarget) + "]");
            return;
        }

        // Here we are using line numbers to jump to the destination. This is only done for BASIC programs.
        try {
            int iTokenNo = _oLineNumberObject.getStatementFromLineNumber(Integer.parseInt(_strTarget));

            if (iTokenNo != 0) {
                _oProgramPointer.setCurrentStatement(iTokenNo);
                return;
            }

            throw new SyntaxErrorException("GOTO [unknown]: Target: " + _strTarget);
        } catch (RuntimeException e) {
            throw new SyntaxErrorException("GOTO [incorrect format]: Target: " + _strTarget);
        }
    }

    /**
     * Returns a string describing the GOTO statement's target and its resolved destination.
     *
     * The returned string indicates whether the target is a label or a line number and shows the corresponding destination statement.
     *
     * @return a string representation of the GOTO statement's target and destination
     * @throws RuntimeException if the target cannot be resolved to a valid label or line number
     */
    @Override
    public String content() throws RuntimeException {
        if (_oLabelStatement.containsLabelKey(_strTarget)) {
            return "GOTO [" + _strTarget + "]: Destination: " + _oLabelStatement.getLabelStatement(_strTarget);
        }

        return "GOTO [" + _strTarget + "]: Destination: " + _oLineNumberObject.getStatementFromLineNumber(Integer.parseInt(_strTarget));
    }

    /**
     * Returns a JSON-like string representing the structure of this GOTO statement for compiler use.
     *
     * @return a string containing the statement type ("GOTO"), token number, and target label or line number.
     * @throws Exception if an error occurs while generating the structure representation.
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"GOTO\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\",";
        strReturn += "\"TARGET\": \""+ _strTarget +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
