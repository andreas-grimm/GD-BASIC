package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.FiFoQueue;
import eu.gricom.basic.variableTypes.Value;
import java.util.List;

/**
 * DataStatement.java
 * <p>
 * Description:
 * <p>
 * The DimStatement class defines all kind of arrays.
 * <p>
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class DataStatement implements Statement {
    private final int _iTokenNumber;
    private final List<Value> _aoValues;

    /**
     * Default constructor.
     *
     * An "DATA" statement prepares and loads the FiFo Queue.
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
     *
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
     *
     * Method for JUnit to return the content of the statement.
     *
     * @return - gives the name of the statement ("END")
     */
    @Override
    public final String content() {
        String strValue = new String();

        for (Value oValue: _aoValues) {
            strValue += " <" + oValue.toString() + ">";
        }

        return "DATA" + strValue;
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
