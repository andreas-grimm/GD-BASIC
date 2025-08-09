package eu.gricom.basic.statements;

import eu.gricom.basic.variableTypes.Value;

/**
 * Base interface for an expression. An expression is like a statement
 * except that it also returns a value when executed. Expressions do not
 * appear at the top level in Jasic programs, but are used in many
 * statements. For example, the value printed by a "print" statement is an
 * expression. Unlike statements, expressions can nest.
 */

public interface Expression {
    /**
 * Evaluates the expression and returns its resulting value.
 *
 * @return the computed value of the expression
 * @throws Exception if an error occurs during evaluation
 */
    Value evaluate() throws Exception;

    /**
 * Returns a human-readable representation of the expression's content.
 *
 * @return a string describing the content of the expression
 */
    String content();

    /**
 * Returns a structural description of the expression for compiler use.
 * <p>
 * The returned string typically includes the statement name and its parameters.
 *
 * @return a string representing the structure of the expression, such as the statement name and its parameters
 * @throws Exception if an error occurs while generating the structure
 */
    String structure() throws Exception;
}
