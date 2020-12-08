package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.Basic;
import eu.gricom.interpreter.basic.helper.MemoryManagement;

import java.io.IOException;

public class InputStatement implements Statement {
    private final String _strName;
/**
 * An "input" statement reads input from the user and stores it in a
 * variable.
 */
    public InputStatement(String strName) {
        _strName = strName;
    }

    public void execute() {
        MemoryManagement oMemoryManager = new MemoryManagement();

        try {
            String strInput = Basic._oLineIn.readLine();

            // Store it as a number if possible, otherwise use a string.
            try {
                double iValue = Double.parseDouble(strInput);
                oMemoryManager.putMap(_strName, iValue);
            } catch (NumberFormatException e) {
                oMemoryManager.putMap(_strName, strInput);
            }
        } catch (IOException e1) {
            // HACK: Just ignore the problem.
        }
    }

    @Override
    public String content() {
        return ("INPUT (" + _strName + ")");
    }
}
