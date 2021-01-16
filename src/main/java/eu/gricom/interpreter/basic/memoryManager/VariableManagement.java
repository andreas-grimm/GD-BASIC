package eu.gricom.interpreter.basic.memoryManager;

import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
import eu.gricom.interpreter.basic.variableTypes.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * VariableManagement.java
 * <p>
 * Description:
 * This structure holds all variables used in the Basic program.
 * <p>
 * The Program Pointer contains the current position of the program.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class VariableManagement {
    private static Map<String, Value> _aoVariables = new HashMap<>();

    /**
     * Default Constructor.
     */
    public VariableManagement() {
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
