package eu.gricom.interpreter.basic.statements;

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
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class LabelStatement {
    private static Map<String, Integer> _aoLabels = new HashMap<>();
// section managing the labels found...

    /**
     * add a label destination in the memory management.
     *
     * @param strLabel - name of the label
     * @param iStatementNumber - statement number
     */
    public final void putLabelStatement(final String strLabel, final int iStatementNumber) {

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
}
