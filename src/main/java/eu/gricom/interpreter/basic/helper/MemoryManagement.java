package eu.gricom.interpreter.basic.helper;

import eu.gricom.interpreter.basic.statements.NumberValue;
import eu.gricom.interpreter.basic.statements.StringValue;
import eu.gricom.interpreter.basic.statements.Value;

import java.util.HashMap;
import java.util.Map;

public class MemoryManagement {
    private static int _iCurrentStatement = 0;
    private static Map<String, Value> _mapVariables = new HashMap<String, Value>();
    private static Map<String, Integer> _mapLabels = new HashMap<String, Integer>();

    public MemoryManagement() {
    }
// section managing the current statement area
    public final void setCurrentStatement(int iCurrentStatement) {
        _iCurrentStatement = iCurrentStatement;
    }

    public final int getCurrentStatement() {
        return(_iCurrentStatement);
    }

    public final void nextStatement() {
        _iCurrentStatement++;
    }

// section managing the labels found...
    public void putLabelStatement(String strText, int iSize) {
        _mapLabels.put(strText, iSize);
    }

    public final int getLabelStatement(String strLabel) {
        return (_mapLabels.get(strLabel));
    }

    public final boolean containsLabelKey(String strKey) {
        return (_mapLabels.containsKey(strKey));
    }

// section managing internal variables...
    public final void putMap (String strName, double iValue) {
        NumberValue oValue = new NumberValue(iValue);
        _mapVariables.put(strName, oValue);
    }

    public final void putMap (String strName, String strValue) {
        StringValue oValue = new StringValue(strValue);
        _mapVariables.put(strName, oValue);
    }

    public final Value getMap (String strKey) {
        return (_mapVariables.get(strKey));
    }

    public boolean mapContainsKey(String strKey) {
        return (_mapVariables.containsKey(strKey));
    }
}
