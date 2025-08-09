package eu.gricom.basic.statements;

/**
 * ColonStatement.java
 * <p>
 * Description:
 * <p>
 * The ColonStatement class provides the necessary information for the runtime to get all required information.
 * <p>
 * (c) = 2020,...,2025 by Andreas Grimm, Den Haag, The Netherlands
 */
public class ColonStatement implements Statement {
    private final int _iTokenNumber;

    /**
     * Constructs a ColonStatement with the specified token number.
     *
     * @param iTokenNumber the token number representing the statement's position in the BASIC program
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
     * Executes the colon statement.
     *
     * This method performs no action for colon statements, as they serve only as statement separators in BASIC programs.
     */
    public final void execute() {

    }

    /**
     * Returns the name of this statement.
     *
     * @return the string "COLON"
     */
    @Override
    public final String content() {

        return "COLON";
    }

    /**
     * Returns a JSON-formatted string representing the structure of the colon statement, including its token number.
     *
     * @return a JSON string with the statement name "COLON" and its token number.
     * @throws Exception if an error occurs during structure generation.
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"COLON\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
