package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.memoryManager.FiFoQueue;
import eu.gricom.interpreter.basic.variableTypes.Value;
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
    private final int _iLineNumber;
    private List <Value> _aoValues;

    /**
     * Default constructor.
     *
     * An "DATA" statement prepares and loads the FiFo Queue.
     * @param iLineNumber - number of the command in the basic program
     * @param aoValues - List of all Values to be pushed into the queue
     */
    public DataStatement(final int iLineNumber, final List<Value> aoValues) {

        _iLineNumber = iLineNumber;
        _aoValues = aoValues;
    }

    /**
     * Get Line Number.
     *
     * @return iLineNumber - the command line number of the statement
     */
    @Override
    public final int getLineNumber() {
        return _iLineNumber;
    }

    /**
     * Execute.
     *
     * Push the array into the FIFO structure.
     */
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
}
