package eu.gricom.basic.statements;

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
     * Get Token Number - get the number of the corresponding token to this statement.
     *
     * @return the command line number of the statement
     */
    int getTokenNumber();

    /**
     * Statements implement this to actually perform whatever behavior the
     * statement causes. "print" statements will display text here, "goto"
     * statements will change the current statement, etc.
     *
     * @throws Exception as any execution error found during execution
     */
    void execute() throws Exception;

    /**
 * Returns a string representation of the statement's content, including the statement name and relevant variable names.
 *
 * Intended for use in unit testing to verify the statement's content.
 *
 * @return a string containing the statement name and associated variable names
 * @throws Exception if an error occurs while generating the content
 */
    String content() throws Exception;

    /**
 * Returns a string describing the structure of the statement, including its name and parameter list.
 *
 * Intended for use by the compiler to analyze the program's structure.
 *
 * @return a string containing the statement name and its parameters
 * @throws Exception if an error occurs while generating the structure description
 */
    String structure() throws Exception;
}
