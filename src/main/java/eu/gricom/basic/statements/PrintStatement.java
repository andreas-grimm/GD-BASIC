package eu.gricom.basic.statements;

import java.util.List;

/**
 * PrintStatement.java
 * <p>
 * Description:
 * <p>
 * A "print" statement evaluates an expression, converts the result to a string, and displays it to the user.
 * <p>
 * (c) = 2004,...,2016 by Andreas Grimm, Den Haag, The Netherlands
 * <p>
 * Created in 2003
 *
 */
public final class PrintStatement implements Statement {
    private final Expression _oExpression;
    private final int _iTokenNumber;
    private final List<Expression> _aoExpression;
    private final boolean _bCRLF;

    /**
     * Default constructor.
     * <p>
     * Receive the statement targeted to be printed.
     *
     * @param oExpression - input to the print statement
     */
    public PrintStatement(final Expression oExpression) {
        _iTokenNumber = 0;
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
     * @param aoExpression - list of inputs to the print statement
     * @param bCRLF - if true, the line to be printed ends with a CR
     */
    public PrintStatement(final int iTokenNumber, final List<Expression> aoExpression, final boolean bCRLF) {
        _iTokenNumber = iTokenNumber;
        _oExpression = null;
        _aoExpression = aoExpression;
        _bCRLF = bCRLF;
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
     * Evaluates and prints the result of one or more expressions.
     *
     * If a single expression is present, prints its evaluated string representation followed by a newline. If a list of expressions is present, prints each evaluated result consecutively, followed by a newline if the CRLF flag is set.
     *
     * @throws Exception if evaluating any expression fails
     */
    public void execute() throws Exception {
        // the simple output of the expression is only used for the JASIC version
        if (_oExpression != null) {
            System.out.println(_oExpression.evaluate().toString());
        }

        // the BASIC version uses this more complex version
        if (_aoExpression != null) {
            for (Expression oExpression : _aoExpression) {
                System.out.print(oExpression.evaluate());
            }

            if (_bCRLF) {
                System.out.println();
            }
        }
    }

    /**
     * Returns a string representation of the print statement and its expressions for debugging purposes.
     *
     * @return a readable string showing the print statement and the content of its expressions
     */
    @Override
    public String content() {
        if (_aoExpression != null) {
            StringBuilder strContent = new StringBuilder();

            for (Expression oExpression : _aoExpression) {
                strContent.append("<").append(oExpression.content()).append(">");
            }

            return "PRINT (" + strContent + ")";
        }

        if (_oExpression != null) {
            return "PRINT (" + _oExpression.content() + ")";
        }

        return "PRINT ()";
    }

    /**
     * Returns a JSON-like string describing the structure of this print statement, including token number, each expression's structure, and the CRLF flag.
     *
     * @return a JSON-like string representing the print statement's structure
     * @throws Exception if an error occurs while obtaining the structure of any expression
     */
    @Override
    public String structure() throws Exception {
        assert _aoExpression != null;
        StringBuilder strReturn = new StringBuilder("{\"PRINT\": {");
        strReturn.append("\"TOKEN_NR\": \"").append(_iTokenNumber).append("\",");
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

    /**
     * Returns the first expression from the list of expressions associated with this print statement.
     *
     * @return the first {@code Expression} in the expression list
     * @throws AssertionError if the expression list is {@code null}
     */
    public Expression getExpression() {
        assert _aoExpression != null;
        return _aoExpression.getFirst();
    }
}
