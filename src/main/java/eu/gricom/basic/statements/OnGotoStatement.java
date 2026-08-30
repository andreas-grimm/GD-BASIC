package eu.gricom.basic.statements;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.memoryManager.LineNumberXRef;
import eu.gricom.basic.memoryManager.ProgramPointer;
import eu.gricom.basic.variableTypes.Value;

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
                           final List<String> aoTargetLineNumbers) {
        _iTokenNumber = iTokenNumber;
        _oSelectExpression = oSelectExpression;
        _aoTargetLineNumbers = aoTargetLineNumbers;
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
                _oLogger.debug("-execute-> ON GOTO: index " + iIndex + " out of range [1.." +
                        _aoTargetLineNumbers.size() + "], continuing normally");
                return;
            }

            String strTarget = _aoTargetLineNumbers.get(iIndex - 1);
            int iTokenNo = _oLineNumberObject.getStatementFromLineNumber(Integer.parseInt(strTarget));

            if (iTokenNo != 0) {
                _oProgramPointer.setCurrentStatement(iTokenNo);
                _oLogger.debug("-execute-> ON GOTO: jumped to line " + strTarget);
                return;
            }

            throw new SyntaxErrorException("ON GOTO: Target line " + strTarget + " not found");
        } catch (SyntaxErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new SyntaxErrorException("ON GOTO: " + e.getMessage());
        }
    }

    /**
     * Content debugging information.
     *
     * @return readable string with statement details
     */
    @Override
    public String content() throws RuntimeException {
        StringBuilder sb = new StringBuilder();
        sb.append("ON GOTO [").append(_oSelectExpression.content()).append("]: Targets: ");
        for (int i = 0; i < _aoTargetLineNumbers.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(_aoTargetLineNumbers.get(i));
        }
        return sb.toString();
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
        StringBuilder sb = new StringBuilder();
        sb.append("{\"ON_GOTO\": {");
        sb.append("\"TOKEN_NR\": \"").append(_iTokenNumber).append("\",");
        sb.append("\"EXPRESSION\": ").append(_oSelectExpression.content()).append(",");
        sb.append("\"TARGETS\": [");
        for (int i = 0; i < _aoTargetLineNumbers.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append("\"").append(_aoTargetLineNumbers.get(i)).append("\"");
        }
        sb.append("]");
        sb.append("}}");
        return sb.toString();
    }
}