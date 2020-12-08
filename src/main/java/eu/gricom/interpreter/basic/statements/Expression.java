package eu.gricom.interpreter.basic.statements;

/**
 * Base interface for an expression. An expression is like a statement
 * except that it also returns a value when executed. Expressions do not
 * appear at the top level in Jasic programs, but are used in many
 * statements. For example, the value printed by a "print" statement is an
 * expression. Unlike statements, expressions can nest.
 */

public interface Expression {
    /**
     * Expression classes implement this to evaluate the expression and
     * return the value.
     *
     * @return The value of the calculated expression.
     */
    Value evaluate() throws Exception;

    /**
     * Get the content in human readable form
     *
     * @return Content in readable form
     */
    String content();
}
