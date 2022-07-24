package eu.gricom.basic.statements;

import java.util.List;

/**
 * PrintStatement.java
 * <p>
 * Description:
 * <p>
 * A "print" statement evaluates an expression, converts the result to a string, and displays it to the user.
 * <p>
 * (c) = 2004,..,2016 by Andreas Grimm, Den Haag, The Netherlands
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
     *
     * Receive the statement that is targeted to be printed.
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
     *
     * Receive the statement that is targeted to be printed.
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
     * Execute the transaction.
     *
     * @throws Exception any execution error found throws an exception
     */
    public void execute() throws Exception {
        // the simple output of the expression is only used for the JASIC version
        if (_oExpression != null) {
            System.out.println(_oExpression.evaluate().toString());
        }

        // this more complex version is used by the BASIC version
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
     * This method is used in testing and debugging. It returns the set values when the constructor has been called.
     *
     * @return - readable string with the name and the value of the assignment
     */
    @Override
    public String content() {
        if (_aoExpression != null) {
            String strContent = new String();

            for (Expression oExpression : _aoExpression) {
                strContent += "<" + oExpression.content() + ">";
            }

            return "PRINT (" + strContent + ")";
        }

        if (_oExpression != null) {
            return "PRINT (" + _oExpression.content() + ")";
        }

        return "PRINT ()";
    }
}
