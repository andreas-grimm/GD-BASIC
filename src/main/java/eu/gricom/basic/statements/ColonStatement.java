package eu.gricom.basic.statements;

/**
 * ColonStatement.java
 * <p>
 * Description: The ColonStatement class represents the colon separator that allows multiple BASIC statements on a
 * single line. When executed, it acts as a no-operation placeholder that allows program flow to continue to the next
 * statement.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class ColonStatement implements Statement {
    private final int _iTokenNumber;

    /**
     * Default constructor.
     * <p>
     * A "DIM" statement initializes an array of any type.
     * @param iTokenNumber - number of the command in the basic program
     */
    public ColonStatement(final int iTokenNumber) {
        _iTokenNumber = iTokenNumber;
    }

    /**
     * Get Token Number.
     *
     * @return the command line number of the statement
     */
    @Override
    public final int getTokenNumber() {
        return _iTokenNumber;
    }

    /**
     * Execute.
     * <p>
     * Terminate the running program.
     */
    public final void execute() {

    }

    /**
     * Content.
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return - gives the name of the statement ("END")
     */
    @Override
    public final String content() {

        return "COLON";
    }

    /**
     * Structure.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("INPUT") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"COLON\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
