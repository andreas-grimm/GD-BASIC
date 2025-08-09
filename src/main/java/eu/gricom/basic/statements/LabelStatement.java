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
     * add a label destination in the memory management.
     *
     * @param strLabel - name of the label
     * @param iStatementNumber - statement number
     */
    public final void putLabelStatement(final String strLabel, final int iStatementNumber) {
        _iStatementNumber = iStatementNumber;
        _strLabel = strLabel;
        _aoLabels.put(strLabel, iStatementNumber);
    }

    /**
     * get the statement number of the label statement searched.
     *
     * @param strLabel - label name
     * @return - statement number
     */
    public final int getLabelStatement(final String strLabel) {
        return _aoLabels.get(strLabel);
    }

    /**
     * verify that the Label is stored.
     *
     * @param strKey - Label name
     * @return - true if label is in the memory management
     */
    public final boolean containsLabelKey(final String strKey) {

        return _aoLabels.containsKey(strKey);
    }

    /**
     * Get Token Number - get the number of the corresponding token to this statement.
     *
     * @return the command line number of the statement
     */
    @Override
    public int getTokenNumber() {
        return 0;
    }

    /**
     * Statemen objects implement this class to actually perform whatever
     * behavior the statement causes. "Print" statements will display text
     * here, "goto" statements will change the current statement, etc.
     *
     * @throws Exception as any execution error found during execution
     */
    @Override
    public void execute() throws Exception {

    }

    /**
     * Content.
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return gives the name of the statement ("INPUT") and the variable name
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String content() throws Exception {
        return null;
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
        String strReturn = "{\"LABEL\": {";
        strReturn += "\"LABEL\": \""+ _strLabel +"\",";
        strReturn += "\"STATEMENT\": \""+ _iStatementNumber +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
