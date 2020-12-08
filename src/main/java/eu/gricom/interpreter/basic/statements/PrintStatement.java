package eu.gricom.interpreter.basic.statements;

/**
 * A "print" statement evaluates an expression, converts the result to a
 * string, and displays it to the user.
 */
public final class PrintStatement implements Statement {
    private final Expression _oExpression;

    public PrintStatement(Expression oExpression) {
        _oExpression = oExpression;
    }

    public void execute() throws Exception {
        System.out.println(_oExpression.evaluate().toString());
    }

    @Override
    public String content() {
        return ("PRINT (" + _oExpression.content() + ")");
    }
}
