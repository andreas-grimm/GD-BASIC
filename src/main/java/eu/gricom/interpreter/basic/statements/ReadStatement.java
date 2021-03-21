package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.error.EmptyStackException;
import eu.gricom.interpreter.basic.error.OutOfDataException;
import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.memoryManager.FiFoQueue;
import eu.gricom.interpreter.basic.variableTypes.Value;
import java.util.List;

/**
 * ReadStatement.java
 * <p>
 * Description:
 * <p>
 * The ReadStatement class has a single function: read the FiFo queue and move the content into a set of named
 * variables.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class ReadStatement implements Statement {
    private final int _iLineNumber;
    private List<String> _astrNames;

    /**
     * Default constructor.
     *
     * An "input" statement reads input from the user and stores it in a variable.
     *
     * @param iLineNumber the line number of this command
     * @param astrNames the names of the variable to be read.
     */
    public ReadStatement(final int iLineNumber, final List<String> astrNames) {
        _iLineNumber = iLineNumber;
        _astrNames = astrNames;
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
     *
     * @throws RuntimeException if an incorrect input is detected
     * @throws OutOfDataException if the FiFo queue is empty
     */
    public final void execute() throws OutOfDataException, SyntaxErrorException {
        AssignStatement oAssignStatement;
        FiFoQueue oFiFO = new FiFoQueue();

        for (String strName: _astrNames) {
            try {
                Value oInput = oFiFO.pop();
                oAssignStatement = new AssignStatement(_iLineNumber, strName, oInput);
                oAssignStatement.execute();
            } catch (EmptyStackException eOutOfDataException) {
                throw new OutOfDataException(eOutOfDataException.getMessage());
            } catch (Exception eUnknownException) {
                throw new SyntaxErrorException(eUnknownException.getMessage());
            }
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

        String strReturn = new String();

        for (String strName: _astrNames) {
            strReturn += strName + " ";
        }

        return "READ ( " + strReturn + ")";
    }
}
