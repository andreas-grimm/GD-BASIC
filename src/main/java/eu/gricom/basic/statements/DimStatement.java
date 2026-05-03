package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.VariableManagement;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.error.SyntaxErrorException;

/**
 * DimStatement.java
 * <p>
 * Description: The DimStatement class implements the BASIC DIM command, which declares and allocates arrays of any
 * type. It reserves memory for the specified number of elements and initialises the array for use in the program.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class DimStatement implements Statement {
    private final int _iTokenNumber;
    private final String _strArrayName;
    private final int _iSize;

    /**
     * Default constructor.
     * <p>
     * A "DIM" statement initializes an array of any type.
     * @param iTokenNumber - number of the command in the basic program
     */
    public DimStatement(final int iTokenNumber) {
        _iTokenNumber = iTokenNumber;
        _strArrayName = "";
        _iSize = 0;
    }

    /**
     * Constructor with array name and size.
     * <p>
     * A "DIM" statement initializes an array of specified size.
     * @param iTokenNumber - number of the command in the basic program
     * @param strArrayName - name of the array (e.g. "F#")
     * @param iSize - size of the array
     */
    public DimStatement(final int iTokenNumber, final String strArrayName, final int iSize) {
        _iTokenNumber = iTokenNumber;
        _strArrayName = strArrayName;
        _iSize = iSize;
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
     * Initialize array elements with 0.
     */
    public final void execute() throws SyntaxErrorException {
        if (_strArrayName.isEmpty() || _iSize <= 0) {
            return;
        }

        VariableManagement oVariableManager = new VariableManagement();

        // Initialize all array elements to 0
        for (int i = 0; i < _iSize; i++) {
            String strKey = _strArrayName + "-" + i;
            oVariableManager.putMap(strKey, new RealValue(0.0));
        }
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
     * @return gives the name of the statement ("DIM") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"DIM\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\"";
        strReturn += ", \"ARRAY_NAME\": \"" + _strArrayName + "\"";
        strReturn += ", \"SIZE\": \"" + _iSize + "\"";
        strReturn += "}}";
        return strReturn;
    }
}
