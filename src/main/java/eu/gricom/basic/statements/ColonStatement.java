package eu.gricom.basic.statements;

/**
 * ColonStatement.java
 * <p>
 * Description:
 * <p>
 * The ColonStatement class provides the necessary information for the runtime to get all required information.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class ColonStatement implements Statement {
    private final int _iTokenNumber;

    /**
     * Default constructor.
     *
     * An "DIM" statement initializes an array of any type.
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
     *
     * Terminate the running program.
     */
    public final void execute() {

    }

    /**
     * Content.
     *
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
     *
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("INPUT") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"COLON\": {\n";
        strReturn += "\t\"TOKEN_NR\": \""+ _iTokenNumber +"\"\n";
        strReturn += "\t}\n";
        strReturn += "}\n";
        return strReturn;
    }
}
