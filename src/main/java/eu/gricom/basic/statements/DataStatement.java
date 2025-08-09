package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.FiFoQueue;
import eu.gricom.basic.variableTypes.Value;
import java.util.List;

/**
 * DataStatement.java
 * <p>
 * Description:
 * <p>
 * The Data Statement class provides the input values for the READ statement.
 * <p>
 * (c) = 2021,...,2025 by Andreas Grimm, Den Haag, The Netherlands
 */
public class DataStatement implements Statement {
    private final int _iTokenNumber;
    private final List<Value> _aoValues;

    /**
     * Default constructor.
     * <p>
     * A "DATA" statement prepares and loads the FiFo Queue.
     * @param iTokenNumber - number of the command in the basic program
     * @param aoValues - List of all Values to be pushed into the queue
     */
    public DataStatement(final int iTokenNumber, final List<Value> aoValues) {

        _iTokenNumber = iTokenNumber;
        _aoValues = aoValues;
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
     * Push the array into the FIFO structure.
     */
    //TODO: This needs fixing: the FiFo Queue will forget the values after the end of the call. Look at READ statement
    public final void execute() {
        FiFoQueue oQueue = new FiFoQueue();

        for (Value oValue: _aoValues) {
            oQueue.push(oValue);
        }
    }

    /**
     * Content.
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return - gives the name of the statement ("END")
     */
    @Override
    public final String content() {
        StringBuilder strValue = new StringBuilder();

        for (Value oValue: _aoValues) {
            strValue.append(" <").append(oValue.toString()).append(">");
        }

        return "DATA" + strValue;
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
        StringBuilder strReturn = new StringBuilder("{\"DATA\": {");
        strReturn.append("\"TOKEN_NR\": \"").append(_iTokenNumber).append("\"{");
        for (Value oValue: _aoValues) {
            strReturn.append("\"").append(oValue.toString()).append("\"");
        }
        strReturn.append("}}}");
        return strReturn.toString();
    }
}
