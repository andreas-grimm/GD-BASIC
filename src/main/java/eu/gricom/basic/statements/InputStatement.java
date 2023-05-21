package eu.gricom.basic.statements;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.error.SyntaxErrorException;

import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * InputStatement.java
 * <p>
 * Description:
 * <p>
 * The InputStatement class has a single function: read the console and move the content into a named variable.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class InputStatement implements Statement {
    private final String _strName;
    private final int _iTokenNumber;

    /**
     * Default constructor.
     *
     * An "input" statement reads input from the user and stores it in a variable.
     *
     * @param strName - the name of the variable to be read.
     */
    public InputStatement(final String strName) {
        _iTokenNumber = 0;
        _strName = strName;
    }

    /**
     * Default constructor.
     *
     * An "input" statement reads input from the user and stores it in a variable.
     *
     * @param iTokenNumber the line number of this command
     * @param strName the name of the variable to be read.
     */
    public InputStatement(final int iTokenNumber, final String strName) {
        _iTokenNumber = iTokenNumber;
        _strName = strName;
    }

    /**
     * Get Line Number.
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
     * Execute the input statement.
     *
     * @throws RuntimeException if an incorrect input is detected
     */
    public final void execute() throws RuntimeException {
        BufferedReader oReader = new BufferedReader(new InputStreamReader(System.in));
        AssignStatement oAssignStatement;
        Value oValue;

        try {
            String strInput = oReader.readLine();

            // Store it as a number if possible, otherwise use a string.
            try {
                oValue = new RealValue(Double.parseDouble(strInput));
            } catch (NumberFormatException e) {
                oValue = new StringValue(strInput);
            }

            oAssignStatement = new AssignStatement(_iTokenNumber, _strName, oValue);
            oAssignStatement.execute();
        } catch (IOException | SyntaxErrorException eException) {
            throw new RuntimeException("Incorrect input detected: " + eException.getMessage());
        } catch (Exception eUnKnownException) {
            throw new RuntimeException("Unknown Exception: " + eUnKnownException.getMessage());
        }
    }

    /**
     * Content.
     *
     * Method for JUnit to return the content of the statement.
     *
     * @return - gives the name of the statement ("INPUT") and the variable name
     */
    @Override
    public final String content() {

        return "INPUT (" + _strName + ")";
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
        String strReturn = this.toString();
        return strReturn;
    }
}
