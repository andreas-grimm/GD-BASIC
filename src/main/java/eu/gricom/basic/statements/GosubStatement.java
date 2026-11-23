package eu.gricom.basic.statements;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.memorymanager.LineNumberXRef;
import eu.gricom.basic.memorymanager.ProgramPointer;
import eu.gricom.basic.memorymanager.Stack;
import eu.gricom.basic.variabletypes.IntegerValue;

/**
 * GosubStatement.java
 * <p>
 * Description: The GosubStatement class implements the BASIC GOSUB command for subroutine calls. It pushes the current
 * program position onto the stack as a return address, then transfers control to the specified line number or label.
 * Execution resumes after the GOSUB when a RETURN statement is encountered.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
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
        // Statement index 0 is a valid target (the first statement of the program), so a missing line is detected
        // by the exception thrown by getStatementFromLineNumber(), not by a 0 return value.
        int iStatementNo;
        try {
            iStatementNo = _oLineNumberObject.getStatementFromLineNumber(Integer.parseInt(_strTarget));
        } catch (RuntimeException oException) {
            throw new SyntaxErrorException("GOSUB [unknown]: Target: " + _strTarget);
        }

        _oStack.push(new IntegerValue(_oLineNumberObject.getStatementFromToken(_iTokenNumber)));
        _oProgramPointer.setCurrentStatement(iStatementNo);
    }

    /**
     * This method is used in testing and debugging. It returns the set values when the constructor has been called.
     *
     * @return - readable string with the name and the value of the assignment
     */
    @Override
    public String content() throws RuntimeException {
        if (_oLabelStatement.containsLabelKey(_strTarget)) {
            return "GOSUB [" + _strTarget + "]: Destination: " + _oLabelStatement.getLabelStatement(_strTarget);
        }

       return "GOSUB [" + _strTarget + "]: Destination: " + _oLineNumberObject.getStatementFromLineNumber(Integer.parseInt(_strTarget));
    }

    /**
     * Structure.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("INPUT") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"GOSUB\": {";
        strReturn += "\"TOKEN_NR\": \"" + _iTokenNumber + "\",";
        strReturn += "\"TARGET\": \"" + _strTarget + "\"";
        strReturn += "}}";
        return strReturn;
    }

    /** @return the target line number as text (used by the compiler) */
    public String getTarget() {
        return _strTarget;
    }
}

