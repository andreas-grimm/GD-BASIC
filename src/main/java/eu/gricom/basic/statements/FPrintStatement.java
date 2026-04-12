package eu.gricom.basic.statements;

import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.memoryManager.FileManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

/**
 * FPrintStatement.java
 * <p>
 * Description: The FPrintStatement class implements the BASIC FPRINT command. It evaluates one or more expressions,
 * converts the results to string format, and outputs them to a previously opened file. It supports, like the PRINT
 * command multiple expressions separated by semicolons or commas, with optional line termination suppression.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class FPrintStatement implements Statement {
    private final Expression _oExpression;
    private final int _iTokenNumber;
    private final List<Expression> _aoExpression;
    private final boolean _bCRLF;
    private final int _iFileId;
     /**
     * Default constructor.
     * <p>
     * Receive the statement targeted to be printed.
     *
     * @param iFileId - the file id to be used for assigning the output
     * @param oExpression - input to the print statement
     */
    public FPrintStatement(final int iFileId, final Expression oExpression) {
        _iTokenNumber = 0;
        _iFileId = iFileId;
        _oExpression = oExpression;
        _aoExpression = null;
        _bCRLF = true;
    }

    /**
     * Default constructor.
     * <p>
     * Receive the statement targeted to be printed.
     *
     * @param iTokenNumber  - number of the token in the BASIC program
     * @param iFileId - the file id to be used for assigning the output
     * @param aoExpression - list of inputs to the print statement
     * @param bCRLF - if true, the line to be printed ends with a CR
     */
    public FPrintStatement(final int iTokenNumber, final int iFileId, final List<Expression> aoExpression, final boolean bCRLF) {
        _iTokenNumber = iTokenNumber;
        _oExpression = null;
        _aoExpression = aoExpression;
        _bCRLF = bCRLF;
        _iFileId = iFileId;
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
     * @throws Exception any execution error found throws an exception
     */
    public void execute() throws Exception {
        // the simple output of the expression is only used for the JASIC version
        if (_oExpression != null) {
            System.out.println(_oExpression.evaluate().toString());
        }

        // the BASIC version uses this more complex version
        if (_aoExpression != null) {
            FileManager oFileManager = new FileManager();
            for (Expression oExpression : _aoExpression) {
                try {
                    oFileManager.write(_iFileId, oExpression.evaluate().toString());
                } catch (IOException eIoException) {
                    Logger oLogger = new Logger("eu.gricom.basic.statements.FPrintStatement");
                    oLogger.error(eIoException.getMessage() + "oExpression.evaluate()");
                }
            }
            if (_bCRLF) {
                try {
                    oFileManager.write(_iFileId, "\n");
                } catch (IOException eIoException) {
                    Logger oLogger = new Logger("eu.gricom.basic.statements.FPrintStatement");
                    oLogger.error(eIoException.getMessage() + " CRLF write failed");
                }
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
        if (_aoExpression != null) {
            StringBuilder strContent = new StringBuilder();

            for (Expression oExpression : _aoExpression) {
                strContent.append("<").append(_iFileId).append(oExpression.content()).append(">");
            }

            return "FPRINT (" + strContent + ")";
        }

        if (_oExpression != null) {
            return "FPRINT (" + _iFileId + "," + _oExpression.content() + ")";
        }

        return "FPRINT (" + _iFileId + ")";
    }

    /**
     * Structure.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("FPRINT") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        assert _aoExpression != null;
        StringBuilder strReturn = new StringBuilder("{\"FPRINT\": {");
        strReturn.append("\"TOKEN_NR\": \"").append(_iTokenNumber).append("\",");
        strReturn.append("\"FILE_ID\": \"").append(_iFileId).append("\",");
        for (Expression oExpression : _aoExpression) {
            strReturn.append("\"EXPRESSION\": ").append(oExpression.structure()).append(",");
        }
        if (_bCRLF) {
            strReturn.append("\"CRLF\": \"TRUE\"");
        } else {
            strReturn.append("\"CRLF\": \"FALSE\"");
        }
        strReturn.append("}}");
        return strReturn.toString();
    }

    public Expression getExpression() {
        assert _aoExpression != null;
        return _aoExpression.getFirst();
    }
}
