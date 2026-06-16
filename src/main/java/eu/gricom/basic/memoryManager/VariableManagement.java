package eu.gricom.basic.memoryManager;

import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.tokenizer.Normalizer;
import eu.gricom.basic.variableTypes.BooleanValue;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;
import eu.gricom.basic.variableTypes.VariableType;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.error.SyntaxErrorException;

import java.util.HashMap;
import java.util.Map;

/**
 * VariableManagement.java
 * <p>
 * Description: The VariableManagement class provides centralised storage and retrieval for all variables used during
 * BASIC program execution. It maintains separate hash maps for different variable types (strings, integers, reals,
 * booleans, and untyped values) and handles variable type inference based on BASIC naming conventions such as $ for
 * strings and % for integers.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class VariableManagement {
    private final static Map<String, BooleanValue> _moBooleans = new HashMap<>();
    private final static Map<String, IntegerValue> _moIntegers = new HashMap<>();
    private final static Map<String, RealValue> _moReals = new HashMap<>();
    private final static Map<String, StringValue> _moStrings = new HashMap<>();

    /**
     * Default Constructor.
     */
    public VariableManagement() {
    }

    /**
     * Helper method to determine if a variable name has any type suffix.
     *
     * @param strKey variable name to check
     * @return true if variable has type suffix ($, %, &, !, @), false otherwise
     */
    private boolean _hasTypeSuffix(final String strKey) {
        return strKey.contains("$") || strKey.contains("%") ||
               strKey.contains("&") || strKey.contains("!") ||
               strKey.contains("@");
    }

// section managing internal variables...
    /**
     * Put a key - value pair into the variable map structure.
     *
     * @param strKey key part of the pair
     * @param oValue value part of the pair, here as a Value object
     * @throws SyntaxErrorException if the parenthesis is not set correctly
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
            throw new SyntaxErrorException(
                "Syntax Error: Variable name [" + strKey +
                "] uses unsupported '#' suffix. Use untyped (no suffix) or '!' for real numbers.");
        } else if (strKey.contains("!")) {
            eVariableType = VariableType.DOUBLE;
        } else if (strKey.contains("@")) {
            eVariableType = VariableType.BOOLEAN;
        }

        switch (eVariableType) {
            case STRING:
                _moStrings.put(Normalizer.normalizeIndex(strKey), (StringValue) oValue);
                break;

            case INTEGER:
            case LONG:
                _moIntegers.put(Normalizer.normalizeIndex(strKey), new IntegerValue((int) oValue.toReal()));
                break;
            case REAL:
            case DOUBLE:
                _moReals.put(Normalizer.normalizeIndex(strKey), (RealValue) oValue);
                break;
            case BOOLEAN:
                _moBooleans.put(Normalizer.normalizeIndex(strKey), (BooleanValue) oValue);
                break;
            case UNDEFINED:
                // Untyped variables default to REAL type
                if (oValue instanceof RealValue) {
                    _moReals.put(Normalizer.normalizeIndex(strKey), (RealValue) oValue);
                } else {
                    // Convert untyped to real
                    _moReals.put(Normalizer.normalizeIndex(strKey),
                        new RealValue(oValue.toReal()));
                }
                break;
            default:
                // Should not reach here with current suffix logic
                _moReals.put(Normalizer.normalizeIndex(strKey),
                    new RealValue(oValue.toReal()));
                break;
        }
    }

    /**
     * Put a key - value pair into the variable map structure.
     *
     * @param strKey - key part of the pair
     * @param dValue - value part of the pair, here as an double
     * @throws SyntaxErrorException variable cannot store double value
     */
    public final void putMap(final String strKey, final double dValue) throws SyntaxErrorException {
        // Reject # suffix explicitly
        if (strKey.contains("#")) {
            throw new SyntaxErrorException("Syntax Error: Variable name [" + strKey +
                    "] uses unsupported '#' suffix. Use untyped (no suffix) or '!' for real numbers.");
        }

        // Explicit ! suffix or untyped variables both map to REAL
        if (strKey.contains("!") || !_hasTypeSuffix(strKey)) {
            RealValue oValue = new RealValue(dValue);
            _moReals.put(Normalizer.normalizeIndex(strKey), oValue);
            return;
        }

        throw new SyntaxErrorException("Syntax Error: Variable name [" + strKey
                + "] cannot store double value. Use untyped (no suffix) or '!' for real numbers.");
    }

    /**
     * Put a key - value pair into the variable map structure.
     *
     * @param strKey key part of the pair
     * @param strValue value part of the pair, here as a string
     * @throws SyntaxErrorException variable is not marked as string
     */
    public final void putMap(final String strKey, final String strValue) throws SyntaxErrorException {
        if (strKey.contains("$")) {
            StringValue oValue = new StringValue(strValue);
            _moStrings.put(Normalizer.normalizeIndex(strKey), oValue);
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
     */
    public final void putMap(final String strKey, final int iValue) throws SyntaxErrorException {
        if (strKey.contains("%") || strKey.contains("&")) {
            IntegerValue oValue = new IntegerValue(iValue);
            _moIntegers.put(Normalizer.normalizeIndex(strKey), oValue);
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
     * @throws SyntaxErrorException if the parenthesis is not set correctly
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

        if (_moStrings.containsKey(strWork)) {
            oLogger.debug("-getMap-> retrieving key: <" + strWork + "> [string] " + _moStrings.get(strWork));
            Value oString = _moStrings.get(strWork);

            if (bProcess) {
                return ((StringValue) oString).process(strKey);
            }

            return _moStrings.get(strWork);
        }

        if (_moIntegers.containsKey(strWork)) {
            oLogger.debug("-getMap-> retrieving key: <" + strWork + "> [integer] ");
            return _moIntegers.get(strWork);
        }

        if (_moReals.containsKey(strWork)) {
            oLogger.debug("-getMap-> retrieving key: <" + strWork + "> [real] ");
            return _moReals.get(strWork);
        }

        if (_moBooleans.containsKey(strWork)) {
            oLogger.debug("-getMap-> retrieving key: <" + strWork + "> [boolean] ");
            return _moBooleans.get(strWork);
        }

        return null;
    }

    /**
     * Verifies that the variables structure contains a given key.
     *
     * @param strKey Key to be verified
     * @return true, if key is in the data structure
     * @throws SyntaxErrorException if the parenthesis is not set correctly
     */
    public final boolean mapContainsKey(final String strKey) throws SyntaxErrorException {
        String strWork = strKey;

        int iIndex = strKey.indexOf("[");
        if (iIndex > 0) {
            strWork = strKey.substring(0, iIndex);
        }

        strWork = Normalizer.normalizeIndex(strWork);

        if (_moBooleans.containsKey(strWork)
                || _moIntegers.containsKey(strWork)
                || _moReals.containsKey(strWork)
                || _moStrings.containsKey(strWork)) {
            return true;
        }

        return false;
    }
}
