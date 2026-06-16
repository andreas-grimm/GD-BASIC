package eu.gricom.basic.statements;

import eu.gricom.basic.helper.Logger;

import java.util.Collections;
import java.util.List;

/**
 * FPutStatement.java
 * <p>
 * Description: The FPutStatement class implements the BASIC FPUT command. It evaluates an expression,
 * converts the result to string format, and outputs it to a previously opened file WITHOUT adding a newline.
 * This is a wrapper around FPrintStatement with the bCRLF parameter set to false, providing a simpler
 * interface for writing single characters or strings without line termination.
 * <p>
 * Usage:
 * - FPUT fileID, expression
 * <p>
 * Execution Flow:
 * 1. Receive file ID and expression to write
 * 2. Create a single-element list containing the expression
 * 3. Call FPrintStatement with bCRLF = false (no newline added)
 * 4. The expression is evaluated and written to the file without line termination
 * <p>
 * Difference from FPRINT:
 * - FPRINT adds a newline at the end (bCRLF = true by default)
 * - FPUT does not add a newline at the end (bCRLF = false)
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FPutStatement implements Statement {
    private final int _iTokenNumber;
    private final int _iFileId;
    private final Expression _oExpression;
    private final FPrintStatement _oFPrintStatement;

    /**
     * Default constructor.
     * <p>
     * @param iTokenNumber the line number of the command in the BASIC program
     * @param iFileId the file ID to write to
     * @param oExpression the expression to evaluate and write
     */
    public FPutStatement(final int iTokenNumber, final int iFileId, final Expression oExpression) {
        _iTokenNumber = iTokenNumber;
        _iFileId = iFileId;
        _oExpression = oExpression;

        // Create a single-element list containing the expression
        List<Expression> aoExpression = Collections.singletonList(oExpression);

        // Create the internal FPrintStatement with bCRLF set to false (no newline)
        _oFPrintStatement = new FPrintStatement(_iTokenNumber, _iFileId, aoExpression, false);
    }

    /**
     * Get Token Number - get the number of the corresponding token to this statement.
     *
     * @return the command line number of the statement
     */
    @Override
    public int getTokenNumber() {
        return _iTokenNumber;
    }

    /**
     * execute writes the expression to the file without adding a newline.
     * Delegates to the internal FPrintStatement with bCRLF = false.
     *
     * @throws Exception as any execution error found during execution
     */
    @Override
    public void execute() throws Exception {
        try {
            // Delegate to FPrintStatement with bCRLF = false
            _oFPrintStatement.execute();
        } catch (Exception e) {
            Logger oLogger = new Logger("eu.gricom.basic.statements.FPutStatement");
            oLogger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Content.
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return gives the name of the statement ("FPUT") and the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String content() throws Exception {
        return "FPUT (" + _iFileId + "," + _oExpression.content() + ")";
    }

    /**
     * Structure.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("FPUT") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        StringBuilder strReturn = new StringBuilder("{\"FPUT\": {");
        strReturn.append("\"TOKEN_NR\": \"").append(_iTokenNumber).append("\",");
        strReturn.append("\"FILE_ID\": \"").append(_iFileId).append("\",");
        strReturn.append("\"EXPRESSION\": ").append(_oExpression.structure());
        strReturn.append("}}");
        return strReturn.toString();
    }
}
