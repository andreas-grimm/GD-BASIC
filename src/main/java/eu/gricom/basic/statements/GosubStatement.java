package eu.gricom.basic.statements;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.memoryManager.LineNumberXRef;
import eu.gricom.basic.memoryManager.ProgramPointer;
import eu.gricom.basic.memoryManager.Stack;
import eu.gricom.basic.variableTypes.IntegerValue;

/**
 * GoSubStatement.java
 * <p>
 * Description:
 * <p>
 * A "GOSUB" statement jumps execution to another place in the program and returns to this location after reaching the
 * return command.
 * <p>
 * (c) = 2004,...,2021 by Andreas Grimm, Den Haag, The Netherlands
 * <p>
 * Created in 2021
 */
public final class GosubStatement implements Statement {
    private final String _strTarget;
    private final int _iTokenNumber;
    private final ProgramPointer _oProgramPointer = new ProgramPointer();
    private final LabelStatement _oLabelStatement = new LabelStatement();
    private final LineNumberXRef _oLineNumberObject = new LineNumberXRef();
    private final Stack _oStack = new Stack();

    /**
     * Default constructor.
     *
     * @param iTokenNumber - number of the line of the command
     * @param strTarget - target of the jump - defined by a label
     */
    public GosubStatement(final int iTokenNumber, final String strTarget) {
        _iTokenNumber = iTokenNumber;
        _strTarget = strTarget;
    }

    /**
     * Get Line Number.
     *
     * @return iLineNumber - the command line number of the statement
     */
    @Override
    public int getTokenNumber() {
        return _iTokenNumber;
    }

    /**
     * Execute the transaction.
     *
     * @throws SyntaxErrorException when the target of the GoSub command is unknown
     * @throws RuntimeException escalated from the LineNumberXRef class
     */
    public void execute() throws SyntaxErrorException, RuntimeException {
        // This class is only used for the BASIC version...
        // here we are using line numbers to jump to the destination. This is only done for BASIC programs.
        int iTokenNo = _oLineNumberObject.getStatementFromLineNumber(Integer.parseInt(_strTarget));

        if (iTokenNo != 0) {
            _oStack.push(new IntegerValue(_oLineNumberObject.getStatementFromToken(_iTokenNumber)));
            _oProgramPointer.setCurrentStatement(iTokenNo);
            return;
        }

        throw new SyntaxErrorException("GOSUB [unknown]: Target: " + _strTarget);
    }

    /**
     * Returns a string representation of the GOSUB statement for debugging or testing purposes.
     *
     * The returned string includes the target label or line number and its corresponding destination statement index.
     *
     * @return a readable string describing the GOSUB statement's target and destination
     * @throws RuntimeException if label or line number resolution fails
     */
    @Override
    public String content() throws RuntimeException {
        if (_oLabelStatement.containsLabelKey(_strTarget)) {
            return "GOSUB [" + _strTarget + "]: Destination: " + _oLabelStatement.getLabelStatement(_strTarget);
        }

       return "GOSUB [" + _strTarget + "]: Destination: " + _oLineNumberObject.getStatementFromLineNumber(Integer.parseInt(_strTarget));
    }

    /**
     * Returns a JSON-like string representing the structure of this GOSUB statement, including its token number and target.
     *
     * @return a string describing the statement's structure for use by the compiler.
     * @throws Exception if an error occurs in underlying implementation classes.
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"GOSUB\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\",";
        strReturn += "\"TARGET\": \""+ _strTarget +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
