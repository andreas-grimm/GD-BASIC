package eu.gricom.basic.statements;

import java.util.HashMap;
import java.util.Map;

/**
 * Label.java
 * <p>
 * Description:
 * <p>
 * The Label structure contained a list of the different labels found in the source code. This structure is now part
 * of the memory management function and has been deprecated.
 * <p>
 * (c) = 2020,...,2025 by Andreas Grimm, Den Haag, The Netherlands
 */
public class LabelStatement implements Statement {
    private static final Map<String, Integer> _aoLabels = new HashMap<>();
    private int _iStatementNumber;
    private String _strLabel;

// section managing the labels found...

    /**
     * Associates the specified label with a statement number in the static label map.
     *
     * Updates the current instance to reflect the provided label and statement number.
     *
     * @param strLabel the name of the label to add or update
     * @param iStatementNumber the statement number to associate with the label
     */
    public final void putLabelStatement(final String strLabel, final int iStatementNumber) {
        _iStatementNumber = iStatementNumber;
        _strLabel = strLabel;
        _aoLabels.put(strLabel, iStatementNumber);
    }

    /**
     * Retrieves the statement number associated with the specified label.
     *
     * @param strLabel the name of the label to look up
     * @return the statement number mapped to the label, or {@code null} if the label does not exist
     */
    public final int getLabelStatement(final String strLabel) {
        return _aoLabels.get(strLabel);
    }

    /**
     * Checks whether a label with the specified name exists in the label map.
     *
     * @param strKey the name of the label to check
     * @return true if the label exists; false otherwise
     */
    public final boolean containsLabelKey(final String strKey) {

        return _aoLabels.containsKey(strKey);
    }

    /**
     * Returns the token number associated with this statement.
     *
     * @return always returns 0, indicating no specific token number is assigned.
     */
    @Override
    public int getTokenNumber() {
        return 0;
    }

    /**
     * Executes the behavior associated with this label statement.
     *
     * This implementation does nothing, as label statements do not have executable behavior.
     */
    @Override
    public void execute() throws Exception {

    }

    /**
     * Returns the content of the label statement.
     *
     * @return always returns {@code null} as label statements do not have content.
     */
    @Override
    public String content() throws Exception {
        return null;
    }

    /**
     * Returns a JSON-formatted string representing the label and its associated statement number.
     *
     * @return a JSON string with the label name and statement number.
     * @throws Exception if an error occurs during string construction.
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"LABEL\": {";
        strReturn += "\"LABEL\": \""+ _strLabel +"\",";
        strReturn += "\"STATEMENT\": \""+ _iStatementNumber +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
