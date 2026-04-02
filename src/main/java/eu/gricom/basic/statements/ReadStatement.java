package eu.gricom.basic.statements;

import eu.gricom.basic.error.EmptyStackException;
import eu.gricom.basic.error.OutOfDataException;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.memoryManager.FiFoQueue;
import eu.gricom.basic.variableTypes.Value;
import java.util.List;

/**
 * ReadStatement.java
 * <p>
 * Description: The ReadStatement class implements the BASIC READ command, which retrieves values from the FIFO queue
 * (populated by DATA statements) and assigns them to the specified variables. It supports reading multiple values in
 * a single statement.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class ReadStatement implements Statement {
    private final int _iTokenNumber;
    private final List<String> _astrNames;

    /**
     * Default constructor.
     * <p>
     * An "input" statement reads input from the user and stores it in a variable.
     *
     * @param iTokenNumber the token number of this command
     * @param astrNames the names of the variable to be read.
     */
    public ReadStatement(final int iTokenNumber, final List<String> astrNames) {
        _iTokenNumber = iTokenNumber;
        _astrNames = astrNames;
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
     * <p>
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
                oAssignStatement = new AssignStatement(_iTokenNumber, strName, oInput);
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
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return - gives the name of the statement ("INPUT") and the variable name
     */
    @Override
    public final String content() {

        StringBuilder strReturn = new StringBuilder();

        for (String strName: _astrNames) {
            strReturn.append(strName).append(" ");
        }

        return "READ ( " + strReturn + ")";
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
        StringBuilder strReturn = new StringBuilder("{\"READ\": {");
        strReturn.append("\"TOKEN_NR\": \"").append(_iTokenNumber).append("\",");
        for (String strName: _astrNames) {
            strReturn.append("\"NAME\": \"").append(strName).append("\",");
        }
        strReturn.deleteCharAt(strReturn.length() - 1);
        strReturn.append("}}");
        return strReturn.toString();
    }
}
