package eu.gricom.basic.statements;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.memorymanager.LineNumberXRef;
import eu.gricom.basic.memorymanager.ProgramPointer;
import eu.gricom.basic.variabletypes.Value;

import java.util.List;

/**
 * OnGotoStatement.java
 * <p>
 * Description: The OnGotoStatement class implements the BASIC ON GOTO command for computed jumps. It evaluates
 * an expression to an integer index, then uses that index to select one of multiple target line numbers.
 * If the index is out of range (&lt; 1 or &gt; number of targets), execution continues normally.
 * <p>
 * Syntax: ON expression GOTO line1, line2, line3, ...
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class OnGotoStatement implements Statement {
    private final Logger _oLogger = new Logger(this.getClass().getName());
    private final Expression _oSelectExpression;
    private final List<String> _aoTargetLineNumbers;
    private final int _iTokenNumber;
    private final ProgramPointer _oProgramPointer = new ProgramPointer();
    private final LineNumberXRef _oLineNumberObject = new LineNumberXRef();

    /**
     * Default constructor.
     *
     * @param iTokenNumber - number of the line of the command
     * @param oSelectExpression - expression that evaluates to the target index
     * @param aoTargetLineNumbers - list of target line numbers as strings
     */
    public OnGotoStatement(final int iTokenNumber, final Expression oSelectExpression,
                           final List<String> vstrTargetLineNumbers) {
        _iTokenNumber = iTokenNumber;
        _oSelectExpression = oSelectExpression;
        _aoTargetLineNumbers = vstrTargetLineNumbers;
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
     * @throws SyntaxErrorException for invalid targets or out-of-range indices
     */
    public void execute() throws SyntaxErrorException {
        try {
            Value oValue = _oSelectExpression.evaluate();
            int iIndex = (int) oValue.toReal();

            _oLogger.debug("-execute-> ON GOTO: expression evaluated to index " + iIndex);

            // 1-based indexing: index 1 refers to first target, 2 to second, etc.
            if (iIndex < 1 || iIndex > _aoTargetLineNumbers.size()) {
                _oLogger.debug("-execute-> ON GOTO: index " + iIndex + " out of range [1.."
                        + _aoTargetLineNumbers.size() + "], continuing normally");
                return;
            }

            String strTarget = _aoTargetLineNumbers.get(iIndex - 1);
            // Statement index 0 is a valid target, so a missing line is detected by the exception, not a 0 value.
            int iStatementNo;
            try {
                iStatementNo = _oLineNumberObject.getStatementFromLineNumber(Integer.parseInt(strTarget));
            } catch (RuntimeException oException) {
                throw new SyntaxErrorException("ON GOTO: Target line " + strTarget + " not found");
            }

            _oProgramPointer.setCurrentStatement(iStatementNo);
            _oLogger.debug("-execute-> ON GOTO: jumped to line " + strTarget);
            return;
        } catch (SyntaxErrorException oException) {
            throw oException;
        } catch (Exception oException) {
            throw new SyntaxErrorException("ON GOTO: " + oException.getMessage());
        }
    }

    /**
     * Content debugging information.
     *
     * @return readable string with statement details
     */
    @Override
    public String content() throws RuntimeException {
        StringBuilder oSb = new StringBuilder();
        oSb.append("ON GOTO [").append(_oSelectExpression.content()).append("]: Targets: ");
        for (int iIndex = 0; iIndex < _aoTargetLineNumbers.size(); iIndex++) {
            if (iIndex > 0) {
                oSb.append(", ");
            }
            oSb.append(_aoTargetLineNumbers.get(iIndex));
        }
        return oSb.toString();
    }

    /**
     * Structure.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement and a list of the parameters
     */
    @Override
    public String structure() throws Exception {
        StringBuilder oSb = new StringBuilder();
        oSb.append("{\"ON_GOTO\": {");
        oSb.append("\"TOKEN_NR\": \"").append(_iTokenNumber).append("\",");
        oSb.append("\"EXPRESSION\": ").append(_oSelectExpression.content()).append(",");
        oSb.append("\"TARGETS\": [");
        for (int iIndex = 0; iIndex < _aoTargetLineNumbers.size(); iIndex++) {
            if (iIndex > 0) {
                oSb.append(", ");
            }
            oSb.append("\"").append(_aoTargetLineNumbers.get(iIndex)).append("\"");
        }
        oSb.append("]");
        oSb.append("}}");
        return oSb.toString();
    }

    /** @return the expression that selects the target (used by the compiler) */
    public Expression getSelectExpression() {
        return _oSelectExpression;
    }

    /** @return the target line numbers as text (used by the compiler) */
    public List<String> getTargetLineNumbers() {
        return _aoTargetLineNumbers;
    }
}
