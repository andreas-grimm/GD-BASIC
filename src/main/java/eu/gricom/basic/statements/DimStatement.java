package eu.gricom.basic.statements;

import eu.gricom.basic.helper.Logger;

/**
 * DimStatement.java
 * <p>
 * Description: The DimStatement class represents the BASIC DIM command. Currently, array support is not implemented
 * in this interpreter. When a DIM statement is encountered, a warning is logged and the statement is skipped.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class DimStatement implements Statement {
    private final int _iTokenNumber;
    private final Logger _oLogger = new Logger(this.getClass().getName());

    /**
     * Default constructor.
     * <p>
     * A "DIM" statement initializes an array of any type.
     * @param iTokenNumber - number of the command in the basic program
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
     * Execute.
     * <p>
     * Log a warning that DIM is not supported and skip execution.
     */
    public final void execute() {
        _oLogger.warning("DIM statement is not supported in this BASIC interpreter");
    }

    /**
     * Content.
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return - gives the name of the statement ("DIM")
     */
    @Override
    public final String content() {
        return "DIM";
    }

    /**
     * Structure.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("DIM")
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"DIM\": {";
        strReturn += "\"TOKEN_NR\": \"" + _iTokenNumber + "\"";
        strReturn += "}}";
        return strReturn;
    }
}
