package eu.gricom.basic.statements;

/**
 * DimStatement.java
 * <p>
 * Description:
 * <p>
 * The DimStatement class defines all kinds of arrays.
 * <p>
 * (c) = 2020,...,2025 by Andreas Grimm, Den Haag, The Netherlands
 */
public class DimStatement implements Statement {
    private final int _iTokenNumber;

    /**
     * Constructs a DimStatement with the specified token number.
     *
     * @param iTokenNumber the token number of the DIM statement in the BASIC program
     */
    public DimStatement(final int iTokenNumber) {
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
     * Terminates the running program immediately.
     */
    public final void execute() {
        System.exit(0);
    }

    /**
     * Returns the string "END" as the content representation of this statement.
     *
     * Primarily used in JUnit tests to identify the statement type.
     *
     * @return the string "END"
     */
    @Override
    public final String content() {

        return "END";
    }

    /****
     * Returns a JSON-formatted string representing the structure of the DIM statement, including its token number.
     *
     * @return a JSON string in the format {"DIM": {"TOKEN_NR": "<token_number>"}}
     * @throws Exception if an error occurs in implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"DIM\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
