package eu.gricom.interpreter.basic.helper;

import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
import eu.gricom.interpreter.basic.variableTypes.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * MemoryManagement.java
 * <p>
 * Description:
 * <p>
 * This class is an intermediate structure to bundle all variables, labels, and program position. It will be resolved
 * in one of the next versions. Additional functionaly will contain stack and heap processing.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class MemoryManagement {
    private static int _iCurrentStatement = 0;
    private static Map<String, Value> _aoVariables = new HashMap<>();
    private static Map<String, Integer> _aoLabels = new HashMap<>();

    /**
     * Default Constructor.
     */
    public MemoryManagement() {
    }
// section managing the current statement area

    /**
     * set the current statement number.
     *
     * @param iCurrentStatement - number of the current statement
     */
    public final void setCurrentStatement(final int iCurrentStatement) {

        _iCurrentStatement = iCurrentStatement;
    }

    /**
     * get the current statement number.
     *
     * @return - current statement number
     */
    public final int getCurrentStatement() {

        return (_iCurrentStatement);
    }

    /**
     * calculate the next statement number.
     */
    public final void nextStatement() {
        _iCurrentStatement++;
    }

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
        return (_aoLabels.get(strLabel));
    }

    /**
     * verify that the Label is stored.
     *
     * @param strKey - Label name
     * @return - true if label is in the memory management
     */
    public final boolean containsLabelKey(final String strKey) {

        return (_aoLabels.containsKey(strKey));
    }

// section managing internal variables...
    /**
     * Put a key - value pair into the variable map structure.
     *
     * @param strName - key part of the pair
     * @param dValue - value part of the pair, here as an double
     */
    public final void putMap(final String strName, final double dValue) {
        RealValue oValue = new RealValue(dValue);
        _aoVariables.put(strName, oValue);
    }

    /**
     * Put a key - value pair into the variable map structure.
     *
     * @param strName - key part of the pair
     * @param strValue - value part of the pair, here as a string
     */
    public final void putMap(final String strName, final String strValue) {
        StringValue oValue = new StringValue(strValue);
        _aoVariables.put(strName, oValue);
    }

    /**
     * Get variable defined by a given key value.
     *
     * @param strKey - Key used for retrieval
     * @return Value object to be returned
     */
    public final Value getMap(final String strKey) {

        return (_aoVariables.get(strKey));
    }

    /**
     * Verifies that the variables structure contains a given key.
     *
     * @param strKey - Key to be verified
     * @return true, if key is in the data structure
     */
    public final boolean mapContainsKey(final String strKey) {

        return (_aoVariables.containsKey(strKey));
    }
}
