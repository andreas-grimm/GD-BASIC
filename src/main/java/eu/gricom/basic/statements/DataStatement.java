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
     * Constructs a DataStatement with the specified token number and list of values.
     *
     * @param iTokenNumber the line number of the DATA statement in the BASIC program
     * @param aoValues the list of values to be associated with this DATA statement
     */
    public DataStatement(final int iTokenNumber, final List<Value> aoValues) {

        _iTokenNumber = iTokenNumber;
        _aoValues = aoValues;
    }

    /**
     * Returns the token number associated with this DATA statement.
     *
     * @return the line number where this statement appears in the source code
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
    /**
     * Loads the DATA statement's values into a new FIFO queue for use by subsequent READ statements.
     *
     * Note: The current implementation does not persist the queue beyond this method call, which may prevent READ statements from accessing the loaded values.
     */
    public final void execute() {
        FiFoQueue oQueue = new FiFoQueue();

        for (Value oValue: _aoValues) {
            oQueue.push(oValue);
        }
    }

    /**
     * Returns a string representation of the DATA statement and its values.
     * <p>
     * The result starts with "DATA" followed by each value enclosed in angle brackets, suitable for use in JUnit tests.
     *
     * @return a string representing the DATA statement and its contained values
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
     * Returns a JSON-like string describing the structure of the DATA statement, including its token number and values.
     *
     * @return a string representation of the DATA statement's structure for use by the compiler.
     * @throws Exception if an error occurs while generating the structure.
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
