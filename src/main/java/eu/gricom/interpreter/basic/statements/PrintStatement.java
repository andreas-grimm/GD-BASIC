package eu.gricom.interpreter.basic.statements;

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

    /**
     * Default constructor.
     *
     * Receive the statement that is targeted to be printed.
     *
     * @param oExpression - input to the print statement
     */
    public PrintStatement(final Expression oExpression) {
        _oExpression = oExpression;
    }

    /**
     * Execute the transaction.
     *
     * @throws Exception any execution error found throws an exception
     */
    public void execute() throws Exception {
        System.out.println(_oExpression.evaluate().toString());
    }

    /**
     * This method is used in testing and debugging. It returns the set values when the constructor has been called.
     *
     * @return - readable string with the name and the value of the assignment
     */
    @Override
    public String content() {

        return ("PRINT (" + _oExpression.content() + ")");
    }
}
