package eu.gricom.basic.statements;

/**
 * Statement.java
 * <p>
 * Description: The Statement interface defines the contract that all BASIC statement implementations must follow. It
 * declares methods for execution, code generation, and accessing the token number for error reporting and navigation.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
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
     * Content.
     *
     * Method for JUnit to return the content of the statement.
     *
     * @return gives the name of the statement ("INPUT") and the variable name
     * @throws Exception based on errors in the implementation classes
     */
    String content() throws Exception;

    /**
     * Structure.
     *
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("INPUT") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    String structure() throws Exception;
}
