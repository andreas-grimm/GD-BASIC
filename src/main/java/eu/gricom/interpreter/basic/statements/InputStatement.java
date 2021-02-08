package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.memoryManager.VariableManagement;

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
    private final int _iLineNumber;

    /**
     * Default constructor.
     *
     * An "input" statement reads input from the user and stores it in a variable.
     *
     * @param strName - the name of the variable to be read.
     */
    public InputStatement(final String strName) {
        _iLineNumber = 0;
        _strName = strName;
    }

    /**
     * Default constructor.
     *
     * An "input" statement reads input from the user and stores it in a variable.
     *
     * @param iLineNumber the line number of this command
     * @param strName the name of the variable to be read.
     */
    public InputStatement(final int iLineNumber, final String strName) {
        _iLineNumber = iLineNumber;
        _strName = strName;
    }

    /**
     * Get Line Number.
     *
     * @return the command line number of the statement
     */
    @Override
    public final int getLineNumber() {
        return _iLineNumber;
    }

    /**
     * Execute.
     *
     * Execute the input statement.
     */
    public final void execute() throws RuntimeException {
        VariableManagement oVariableManager = new VariableManagement();
        BufferedReader oReader = new BufferedReader(new InputStreamReader(System.in));

        try {
            String strInput = oReader.readLine();

            // Store it as a number if possible, otherwise use a string.
            try {
                double iValue = Double.parseDouble(strInput);
                oVariableManager.putMap(_strName, iValue);
            } catch (NumberFormatException e) {
                oVariableManager.putMap(_strName, strInput);
            }
        } catch (IOException | SyntaxErrorException e) {
            throw new RuntimeException("Incorrect input detected...");
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
}
