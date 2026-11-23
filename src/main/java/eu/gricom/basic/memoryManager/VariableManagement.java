package eu.gricom.basic.memorymanager;

import eu.gricom.basic.error.OverflowException;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.tokenizer.Normalizer;
import eu.gricom.basic.variabletypes.BcdValue;
import eu.gricom.basic.variabletypes.BooleanValue;
import eu.gricom.basic.variabletypes.IntegerValue;
import eu.gricom.basic.variabletypes.RealValue;
import eu.gricom.basic.variabletypes.StringValue;
import eu.gricom.basic.variabletypes.Value;
import eu.gricom.basic.variabletypes.VariableType;
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
    private static final Map<String, BooleanValue> _moBooleans = new HashMap<>();
    private static final Map<String, BcdValue> _moBcds = new HashMap<>();
    private static final Map<String, IntegerValue> _moIntegers = new HashMap<>();
    private static final Map<String, RealValue> _moReals = new HashMap<>();
    private static final Map<String, StringValue> _moStrings = new HashMap<>();

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
    private boolean hasTypeSuffix(final String strKey) {
        return strKey.contains("$") || strKey.contains("%")
               || strKey.contains("&") || strKey.contains("!")
               || strKey.contains("#") || strKey.contains("@");
    }

// section managing internal variables...
    /**
     * Put a key - value pair into the variable map structure.
     *
     * @param strKey key part of the pair
     * @param oValue value part of the pair, here as a Value object
     * @throws SyntaxErrorException if the parenthesis is not set correctly
     */
    public final void putMap(final String strKey, final Value oValue) throws SyntaxErrorException, OverflowException {
        rejectBcdArray(strKey);
        // Untyped variables are DOUBLE (decision D5: the DOUBLE type remains for variables without a suffix)
        VariableType eVariableType = VariableType.DOUBLE;

        if (strKey.contains("$")) {
            eVariableType = VariableType.STRING;
        } else if (strKey.contains("%")) {
            eVariableType = VariableType.INTEGER;
        } else if (strKey.contains("&")) {
            eVariableType = VariableType.LONG;
        } else if (strKey.contains("#")) {
            eVariableType = VariableType.BCD;
        } else if (strKey.contains("!")) {
            eVariableType = VariableType.BOOLEAN;
        } else if (strKey.contains("@")) {
            throw retiredSuffixError(strKey);
        }

        switch (eVariableType) {
            case STRING:
                // Phase 7: a clear error instead of a ClassCastException when a number is put in a string variable
                if (!(oValue instanceof StringValue)) {
                    throw new SyntaxErrorException("Type mismatch: the string variable " + strKey
                            + " cannot receive the value " + oValue.content());
                }
                _moStrings.put(Normalizer.normalizeIndex(strKey), (StringValue) oValue);
                break;

            case INTEGER:
            case LONG:
                _moIntegers.put(Normalizer.normalizeIndex(strKey), new IntegerValue((int) oValue.toReal()));
                break;
            case BCD:
                // A name with the '#' suffix holds a decimal number (BCD). INTEGER, LONG and REAL values convert.
                _moBcds.put(Normalizer.normalizeIndex(strKey), BcdValue.of(oValue));
                break;
            case BOOLEAN:
                // A name with the '!' suffix holds TRUE or FALSE. A number is converted (decision A11):
                // a number equal to 0 gives TRUE, any other number gives FALSE. Text is rejected.
                if (oValue instanceof BooleanValue) {
                    _moBooleans.put(Normalizer.normalizeIndex(strKey), (BooleanValue) oValue);
                } else if (oValue instanceof StringValue) {
                    throw typeMismatchError(strKey, "TRUE or FALSE", oValue);
                } else {
                    _moBooleans.put(Normalizer.normalizeIndex(strKey),
                        new BooleanValue(oValue.toReal() == 0.0));
                }
                break;
            default:
                // DOUBLE (untyped variables): numbers only
                if (oValue instanceof BooleanValue) {
                    throw typeMismatchError(strKey, "a number", oValue);
                }
                if (oValue instanceof RealValue) {
                    _moReals.put(Normalizer.normalizeIndex(strKey), (RealValue) oValue);
                } else {
                    _moReals.put(Normalizer.normalizeIndex(strKey), new RealValue(oValue.toReal()));
                }
                break;
        }
    }

    /**
     * Rejects array access with the '#' (BCD) suffix, such as N#(I#). BCD arrays are not implemented yet, so the access
     * is reported as an error instead of silently using one shared scalar value.
     *
     * @param strKey the variable name, including any subscript. An array key has the form NAME-index, for example F#-0
     * @throws SyntaxErrorException if the name has the '#' suffix and a subscript
     */
    private static void rejectBcdArray(final String strKey) throws SyntaxErrorException {
        if (strKey.contains("#") && (strKey.contains("-") || strKey.contains("(") || strKey.contains("["))) {
            throw new SyntaxErrorException("Syntax Error: arrays with the '#' (BCD) suffix are not supported yet: "
                    + strKey);
        }
    }

    /**
     * Error for the retired suffix '@' (decision D2). Only '!' marks true-or-false variables.
     *
     * @param strKey the variable name
     * @return the syntax error to throw
     */
    private static SyntaxErrorException retiredSuffixError(final String strKey) {
        return new SyntaxErrorException("Syntax Error: Variable name [" + strKey + "] uses '@', which is not a "
                + "variable suffix. Use '!' for true or false.");
    }

    /**
     * Error for a value of the wrong type for a variable name (decision A11).
     *
     * @param strKey the variable name
     * @param strExpected the description of the expected value
     * @param oValue the value that was offered
     * @return the syntax error to throw
     */
    private static SyntaxErrorException typeMismatchError(final String strKey, final String strExpected,
                                                           final Value oValue) {
        String strGot = oValue instanceof BooleanValue ? "TRUE or FALSE"
                : oValue instanceof StringValue ? "text" : "a number";
        return new SyntaxErrorException("Type mismatch: [" + strKey + "] expects " + strExpected
                + ", but got " + strGot);
    }

    /**
     * Put a key - value pair into the variable map structure.
     *
     * @param strKey - key part of the pair
     * @param dValue - value part of the pair, here as an double
     * @throws SyntaxErrorException variable cannot store double value
     */
    public final void putMap(final String strKey, final double fValue) throws SyntaxErrorException, OverflowException {
        rejectBcdArray(strKey);
        if (strKey.contains("#")) {
            _moBcds.put(Normalizer.normalizeIndex(strKey), new BcdValue(fValue));
            return;
        }

        // A number is converted for a true-or-false variable: 0 gives TRUE, any other number gives FALSE (A11)
        if (strKey.contains("!")) {
            _moBooleans.put(Normalizer.normalizeIndex(strKey), new BooleanValue(fValue == 0.0));
            return;
        }

        if (strKey.contains("@")) {
            throw retiredSuffixError(strKey);
        }

        // Untyped variables are DOUBLE
        if (!hasTypeSuffix(strKey)) {
            RealValue oValue = new RealValue(fValue);
            _moReals.put(Normalizer.normalizeIndex(strKey), oValue);
            return;
        }

        throw new SyntaxErrorException("Syntax Error: Variable name [" + strKey
                + "] cannot store double value.");
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
    /**
     * resolveIndexVariables replaces the variables inside the square brackets of a string index by their values,
     * e.g. "word$[i%]" becomes "word$[3]" when i% is 3. Numbers are kept as they are. (Phase 7 fix.)
     *
     * @param strKey the key with the brackets, for example word$[i%] or A$[1,i%]
     * @return the key with the index values filled in
     * @throws SyntaxErrorException when an index variable does not exist
     * @throws RuntimeException when the index variable is not numeric
     */
    private String resolveIndexVariables(final String strKey) throws SyntaxErrorException, RuntimeException {
        int iStart = strKey.indexOf("[");
        int iEnd = strKey.indexOf("]");
        if (iStart < 0 || iEnd < iStart) {
            return strKey;
        }

        String[] astrParts = strKey.substring(iStart + 1, iEnd).split(",");
        StringBuilder oBuilder = new StringBuilder(strKey.substring(0, iStart + 1));
        for (int iPart = 0; iPart < astrParts.length; iPart++) {
            if (iPart > 0) {
                oBuilder.append(",");
            }
            String strPart = astrParts[iPart].trim();
            if (strPart.matches("-?\\d+")) {
                oBuilder.append(strPart);
            } else {
                Value oIndex = getMap(strPart);
                if (!(oIndex instanceof IntegerValue || oIndex instanceof RealValue)) {
                    throw new RuntimeException("String index is not a number: " + strPart);
                }
                oBuilder.append((int) oIndex.toReal());
            }
        }
        oBuilder.append(strKey.substring(iEnd));
        return oBuilder.toString();
    }

    public final Value getMap(final String strKey) throws SyntaxErrorException, RuntimeException {
        Logger oLogger = new Logger(this.getClass().getName());
        boolean bProcess = false;

        rejectBcdArray(strKey);

        String strWork = strKey;

        int iIndex = strKey.indexOf("[");
        if (iIndex > 0) {
            bProcess = true;
            strWork = strKey.substring(0, iIndex);
        }

        strWork = Normalizer.normalizeIndex(strWork);

        // Phase 7 fix: an index such as word$[i%] uses a variable. The variables are replaced by their values first.
        String strIndexedKey = bProcess ? resolveIndexVariables(strKey) : strKey;

        if (_moStrings.containsKey(strWork)) {
            oLogger.debug("-getMap-> retrieving key: <" + strWork + "> [string] " + _moStrings.get(strWork));
            Value oString = _moStrings.get(strWork);

            if (bProcess) {
                return ((StringValue) oString).process(strIndexedKey);
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

        if (_moBcds.containsKey(strWork)) {
            oLogger.debug("-getMap-> retrieving key: <" + strWork + "> [bcd] ");
            return _moBcds.get(strWork);
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
                || _moBcds.containsKey(strWork)
                || _moIntegers.containsKey(strWork)
                || _moReals.containsKey(strWork)
                || _moStrings.containsKey(strWork)) {
            return true;
        }

        return false;
    }
}
