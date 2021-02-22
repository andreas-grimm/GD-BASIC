package eu.gricom.interpreter.basic.memoryManager;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.helper.Logger;
import eu.gricom.interpreter.basic.tokenizer.Normalizer;
import eu.gricom.interpreter.basic.variableTypes.BooleanValue;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
import eu.gricom.interpreter.basic.variableTypes.Value;
import eu.gricom.interpreter.basic.variableTypes.VariableType;

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
     * @param strKey key part of the pair
     * @param oValue value part of the pair, here as an Value object
     * @throws SyntaxErrorException if the parenthesis are not set correctly
     */
    public final void putMap(final String strKey, final Value oValue) throws SyntaxErrorException {
        VariableType eVariableType = VariableType.UNDEFINED;

        if (strKey.contains("$")) {
            eVariableType = VariableType.STRING;
        } else if (strKey.contains("%")) {
            eVariableType = VariableType.INTEGER;
        } else if (strKey.contains("&")) {
            eVariableType = VariableType.LONG;
        } else if (strKey.contains("#")) {
            eVariableType = VariableType.REAL;
        } else if (strKey.contains("!")) {
            eVariableType = VariableType.DOUBLE;
        } else if (strKey.contains("@")) {
            eVariableType = VariableType.BOOLEAN;
        }

        switch (eVariableType) {
            case STRING:
                _aoStrings.put(Normalizer.normalizeIndex(strKey), (StringValue) oValue);
                break;

            case INTEGER:
            case LONG:
                _aoIntegers.put(Normalizer.normalizeIndex(strKey), new IntegerValue((int) oValue.toReal()));
                break;
            case REAL:
            case DOUBLE:
                _aoReals.put(Normalizer.normalizeIndex(strKey), (RealValue) oValue);
                break;
            case BOOLEAN:
                _aoBooleans.put(Normalizer.normalizeIndex(strKey), (BooleanValue) oValue);
                break;
            default:
                _aoUntyped.put(Normalizer.normalizeIndex(strKey), oValue);
        }
    }

    /**
     * Put a key - value pair into the variable map structure.
     *
     * @param strKey - key part of the pair
     * @param dValue - value part of the pair, here as an double
     * @throws SyntaxErrorException variable is not marked as real
     * @throws RuntimeException incorrect format of the parenthesis
     */
    public final void putMap(final String strKey, final double dValue) throws SyntaxErrorException, RuntimeException {
        if (strKey.contains("!") || strKey.contains("#")) {
            RealValue oValue = new RealValue(dValue);
            _aoReals.put(Normalizer.normalizeIndex(strKey), oValue);
            return;
        }

        throw new SyntaxErrorException("Syntax Error: Variable name [" + strKey
                + "] does not end as a Real: '!' or " + "'#'");
    }

    /**
     * Put a key - value pair into the variable map structure.
     *
     * @param strKey key part of the pair
     * @param strValue value part of the pair, here as a string
     * @throws SyntaxErrorException variable is not marked as string
     * @throws RuntimeException incorrect format of the parenthesis
     */
    public final void putMap(final String strKey, final String strValue) throws SyntaxErrorException, RuntimeException {
        if (strKey.contains("$")) {
            StringValue oValue = new StringValue(strValue);
            _aoStrings.put(Normalizer.normalizeIndex(strKey), oValue);
            return;
        }

        throw new SyntaxErrorException("Syntax Error: Variable name [" + strKey + "] does not end as a String: '$'");
    }

    /**
     * Put a key - value pair into the variable map structure.
     *
     * @param strKey - key part of the pair
     * @param iValue - value part of the pair, here as an integer
     * @throws SyntaxErrorException variable is not marked as integer
     * @throws RuntimeException incorrect format of the parenthesis
     */
    public final void putMap(final String strKey, final int iValue) throws SyntaxErrorException, RuntimeException {
        if (strKey.contains("%") || strKey.contains("&")) {
            IntegerValue oValue = new IntegerValue(iValue);
            _aoIntegers.put(Normalizer.normalizeIndex(strKey), oValue);
            return;
        }

        throw new SyntaxErrorException("Syntax Error: Variable name [" + strKey + "] does not end as a Integer: '%' "
                + "or '&'");
    }

    /**
     * Get variable defined by a given key value.
     *
     * @param strKey - Key used for retrieval
     * @return Value object to be returned
     * @throws SyntaxErrorException if the parenthesis are not set correctly
     * @throws RuntimeException escalated exception
     */
    public final Value getMap(final String strKey) throws SyntaxErrorException, RuntimeException {
        Logger oLogger = new Logger(this.getClass().getName());
        boolean bProcess = false;

        String strWork = strKey;

        int iIndex = strKey.indexOf("[");
        if (iIndex > 0) {
            bProcess = true;
            strWork = strKey.substring(0, iIndex);
        }

        strWork = Normalizer.normalizeIndex(strWork);

        if (_aoUntyped.containsKey(strWork)) {
            oLogger.debug("-getMap-> retrieving key: <" + strWork + "> [untyped] ");
            return _aoUntyped.get(strWork);
        }

        if (_aoStrings.containsKey(strWork)) {
            oLogger.debug("-getMap-> retrieving key: <" + strWork + "> [string] " + _aoStrings.get(strWork));
            Value oString = _aoStrings.get(strWork);

            if (bProcess) {
                return ((StringValue) oString).process(strKey);
            }

            return _aoStrings.get(strWork);
        }

        if (_aoIntegers.containsKey(strWork)) {
            oLogger.debug("-getMap-> retrieving key: <" + strWork + "> [integer] ");
            return _aoIntegers.get(strWork);
        }

        if (_aoReals.containsKey(strWork)) {
            oLogger.debug("-getMap-> retrieving key: <" + strWork + "> [real] ");
            return _aoReals.get(strWork);
        }

        if (_aoBooleans.containsKey(strWork)) {
            oLogger.debug("-getMap-> retrieving key: <" + strWork + "> [boolean] ");
            return _aoBooleans.get(strWork);
        }

        return null;
    }

    /**
     * Verifies that the variables structure contains a given key.
     *
     * @param strKey Key to be verified
     * @return true, if key is in the data structure
     * @throws SyntaxErrorException if the parenthesis are not set correctly
     */
    public final boolean mapContainsKey(final String strKey) throws SyntaxErrorException {
        String strWork = strKey;

        int iIndex = strKey.indexOf("[");
        if (iIndex > 0) {
            strWork = strKey.substring(0, iIndex);
        }

        strWork = Normalizer.normalizeIndex(strWork);

        if (_aoUntyped.containsKey(strWork)
                || _aoBooleans.containsKey(strWork)
                || _aoIntegers.containsKey(strWork)
                || _aoReals.containsKey(strWork)
                || _aoStrings.containsKey(strWork)) {
            return true;
        }

        return false;
    }
}
