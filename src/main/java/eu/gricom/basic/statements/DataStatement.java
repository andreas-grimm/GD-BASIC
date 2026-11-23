package eu.gricom.basic.statements;

import eu.gricom.basic.memorymanager.FiFoQueue;
import eu.gricom.basic.variabletypes.Value;
import java.util.List;

/**
 * DataStatement.java
 * <p>
 * Description: The DataStatement class implements the BASIC DATA command, which defines constant values that can be
 * retrieved sequentially by READ statements. During execution, the values are pushed into a FIFO queue for later
 * consumption by READ.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
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
    public DataStatement(final int iTokenNumber, final List<Value> voValues) {

        _iTokenNumber = iTokenNumber;
        _aoValues = voValues;
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
        StringBuilder oBuilder = new StringBuilder();

        for (Value oValue: _aoValues) {
            oBuilder.append(" <").append(oValue.toString()).append(">");
        }

        return "DATA" + oBuilder;
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
        StringBuilder oReturn = new StringBuilder("{\"DATA\": {");
        oReturn.append("\"TOKEN_NR\": \"").append(_iTokenNumber).append("\"{");
        for (Value oValue: _aoValues) {
            oReturn.append("\"").append(oValue.toString()).append("\"");
        }
        oReturn.append("}}}");
        return oReturn.toString();
    }
}
