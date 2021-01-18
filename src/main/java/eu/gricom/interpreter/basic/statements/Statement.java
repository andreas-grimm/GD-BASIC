package eu.gricom.interpreter.basic.statements;

/**
 * Statement Interface.
 * <p>
 * Description:
 * <p>
 * The Statement interface defines the methods needed for the different statements to be executed
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public interface Statement {

    /**
     * Get Line Number.
     *
     * @return iLineNumber - the command line number of the statement
     */
    int getLineNumber();

    /**
     * Statements implement this to actually perform whatever behavior the
     * statement causes. "print" statements will display text here, "goto"
     * statements will change the current statement, etc.
     *
     * @throws Exception as any execution error found during execution
     */
    void execute() throws Exception;

    /**
     * Content.
     *
     * Method for JUnit to return the content of the statement.
     *
     * @return - gives the name of the statement ("INPUT") and the variable name
     */
    String content();
}
