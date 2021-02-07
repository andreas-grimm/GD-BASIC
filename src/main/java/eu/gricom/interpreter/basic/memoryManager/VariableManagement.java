package eu.gricom.interpreter.basic.memoryManager;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.variableTypes.BooleanValue;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
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
    private static Map<String, Value> _aoUntyped = new HashMap<>();
    private static Map<String, BooleanValue> _aoBooleans = new HashMap<>();
    private static Map<String, IntegerValue> _aoIntegers = new HashMap<>();
    private static Map<String, RealValue> _aoReals = new HashMap<>();
    private static Map<String, StringValue> _aoStrings = new HashMap<>();

    /**
     * Default Constructor.
     */
    public VariableManagement() {
    }

// section managing internal variables...
    /**
     * Put a key - value pair into the variable map structure.
     *
     * @param strName key part of the pair
     * @param oValue value part of the pair, here as an Value object
     */
    public final void putMap(final String strName, final Value oValue) {

        switch (strName.substring(strName.length() - 1)) {
            case "$":
                _aoStrings.put(strName, (StringValue) oValue);
                break;
            case "%":
            case "&":
                _aoIntegers.put(strName, (IntegerValue) oValue);
                break;
            case "!":
            case "#":
                _aoReals.put(strName, (RealValue) oValue);
                break;
            case "@":
                _aoBooleans.put(strName, (BooleanValue) oValue);
                break;
            default:
                _aoUntyped.put(strName, oValue);
        }
    }

    /**
     * Put a key - value pair into the variable map structure.
     *
     * @param strName - key part of the pair
     * @param dValue - value part of the pair, here as an double
     * @throws SyntaxErrorException variable is not marked as real
     */
    public final void putMap(final String strName, final double dValue) throws SyntaxErrorException {
        if (strName.endsWith("!") || strName.endsWith("#")) {
            RealValue oValue = new RealValue(dValue);
            _aoReals.put(strName, oValue);
            return;
        }

        throw new SyntaxErrorException("Syntax Error: Variable name [" + strName + "] does not end as a Real: '!' or '#'");
    }

    /**
     * Put a key - value pair into the variable map structure.
     *
     * @param strName key part of the pair
     * @param strValue value part of the pair, here as a string
     * @throws SyntaxErrorException variable is not marked as string
     */
    public final void putMap(final String strName, final String strValue) throws SyntaxErrorException {
        if (strName.endsWith("$")) {
            StringValue oValue = new StringValue(strValue);
            _aoStrings.put(strName, oValue);
            return;
        }

        throw new SyntaxErrorException("Syntax Error: Variable name [" + strName + "] does not end as a String: '$'");
    }

    /**
     * Put a key - value pair into the variable map structure.
     *
     * @param strName - key part of the pair
     * @param iValue - value part of the pair, here as an integer
     * @throws SyntaxErrorException variable is not marked as integer
     */
    public final void putMap(final String strName, final int iValue) throws SyntaxErrorException {
        if (strName.substring(strName.length() - 1).matches("%")
                || strName.substring(strName.length() - 1).matches("&")) {
            IntegerValue oValue = new IntegerValue(iValue);
            _aoIntegers.put(strName, oValue);
            return;
        }

        throw new SyntaxErrorException("Syntax Error: Variable name [" + strName + "] does not end as a Integer: '%' "
                + "or '&'");
    }

    /**
     * Put a key - value pair into the variable map structure.
     *
     * @param strName - key part of the pair
     * @param bValue - value part of the pair, here as a boolean
     * @throws SyntaxErrorException variable name is not marked as boolean
     */
    public final void putMap(final String strName, final boolean bValue) throws SyntaxErrorException {
        if (strName.endsWith("@")) {
            BooleanValue oValue = new BooleanValue(bValue);
            _aoBooleans.put(strName, oValue);
            return;
         }

        throw new SyntaxErrorException("Syntax Error: Variable name [" + strName + "] does not end as a Boolean: '@'");
    }

    /**
     * Get variable defined by a given key value.
     *
     * @param strKey - Key used for retrieval
     * @return Value object to be returned
     */
    public final Value getMap(final String strKey) {
        if (_aoUntyped.containsKey(strKey)) {
            return (_aoUntyped.get(strKey));
        }

        if (_aoStrings.containsKey(strKey)) {
            return (_aoStrings.get(strKey));
        }

        if (_aoIntegers.containsKey(strKey)) {
            return (_aoIntegers.get(strKey));
        }

        if (_aoReals.containsKey(strKey)) {
            return (_aoReals.get(strKey));
        }

        if (_aoBooleans.containsKey(strKey)) {
            return (_aoBooleans.get(strKey));
        }

        return (null);
    }

    /**
     * Verifies that the variables structure contains a given key.
     *
     * @param strKey Key to be verified
     * @return true, if key is in the data structure
     */
    public final boolean mapContainsKey(final String strKey) {
        if (_aoUntyped.containsKey(strKey)
                || _aoBooleans.containsKey(strKey)
                || _aoIntegers.containsKey(strKey)
                || _aoReals.containsKey(strKey)
                || _aoStrings.containsKey(strKey)) {
            return (true);
        }

        return (false);
    }
}
