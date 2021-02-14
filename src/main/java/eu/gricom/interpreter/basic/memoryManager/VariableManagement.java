package eu.gricom.interpreter.basic.memoryManager;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.helper.Logger;
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
     * @param strName key part of the pair
     * @param oValue value part of the pair, here as an Value object
     */
    public final void putMap(final String strName, final Value oValue) throws RuntimeException {
        Value oWorkValue = oValue;
        String strWorkName = strName;

        VariableType eVariableType = VariableType.UNDEFINED;

        if (strWorkName.contains("$")) {
            eVariableType = VariableType.STRING;
        } else if (strWorkName.contains("%")) {
            eVariableType = VariableType.INTEGER;
        } else if (strWorkName.contains("&")) {
            eVariableType = VariableType.LONG;
        } else if (strWorkName.contains("#")) {
            eVariableType = VariableType.REAL;
        } else if (strWorkName.contains("!")) {
            eVariableType = VariableType.DOUBLE;
        } else if (strWorkName.contains("@")) {
            eVariableType = VariableType.BOOLEAN;
        }

        switch (eVariableType) {
            case STRING:
                if (strWorkName.contains("(")) {
                    strWorkName = strWorkName.substring(0, strWorkName.indexOf("("));
                    oWorkValue = new StringValue(strWorkName, oValue.toString());
                }
                _aoStrings.put(strWorkName, (StringValue) oWorkValue);
                break;
            case INTEGER:
            case LONG:
                _aoIntegers.put(strName, (IntegerValue) oValue);
                break;
            case REAL:
            case DOUBLE:
                _aoReals.put(strName, (RealValue) oValue);
                break;
            case BOOLEAN:
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
        if (strName.contains("!") || strName.contains("#")) {
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
     * @throws RuntimeException incorrect format of the parenthesis
     */
    public final void putMap(final String strName, final String strValue) throws SyntaxErrorException, RuntimeException {
        if (strName.contains("$")) {
            if (strName.contains("(")) {
                StringValue oValue = new StringValue(strName, strValue);
                _aoStrings.put(strName, oValue);
            } else {
                StringValue oValue = new StringValue(strValue);
                _aoStrings.put(strName, oValue);
            }

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
        if (strName.contains("%") || strName.contains("&")) {
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
        if (strName.contains("@")) {
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
    public final Value getMap(final String strKey) throws RuntimeException {
        Logger oLogger = new Logger(this.getClass().getName());
        boolean bProcess = false;

        String strWork = strKey;

        int iIndex = strKey.indexOf("[");
        if (iIndex > 0) {
            bProcess = true;
            strWork = strKey.substring(0, iIndex);
        }

        iIndex = strKey.indexOf("(");
        if (iIndex > 0) {
            bProcess = true;
            strWork = strKey.substring(0, iIndex);
        }

        if (_aoUntyped.containsKey(strWork)) {
            oLogger.debug("-getMap-> retreiving key: <" + strWork + "> [untyped] ");
            return _aoUntyped.get(strWork);
        }

        if (_aoStrings.containsKey(strWork)) {
            oLogger.debug("-getMap-> retreiving key: <" + strWork + "> [string] " + _aoStrings.get(strWork));
            StringValue oString = _aoStrings.get(strWork);

            if (bProcess) {
                return oString.process(strKey);
            }

            return _aoStrings.get(strWork);
        }

        if (_aoIntegers.containsKey(strWork)) {
            oLogger.debug("-getMap-> retreiving key: <" + strWork + "> [integer] ");
            return _aoIntegers.get(strWork);
        }

        if (_aoReals.containsKey(strWork)) {
            oLogger.debug("-getMap-> retreiving key: <" + strWork + "> [real] ");
            return _aoReals.get(strWork);
        }

        if (_aoBooleans.containsKey(strWork)) {
            oLogger.debug("-getMap-> retreiving key: <" + strWork + "> [boolean] ");
            return _aoBooleans.get(strWork);
        }

        return null;
    }

    /**
     * Verifies that the variables structure contains a given key.
     *
     * @param strKey Key to be verified
     * @return true, if key is in the data structure
     */
    public final boolean mapContainsKey(final String strKey) {
        String strWork = strKey;

        int iIndex = strKey.indexOf("[");
        if (iIndex > 0) {
            strWork = strKey.substring(0, iIndex);
        }

        iIndex = strKey.indexOf("(");
        if (iIndex > 0) {
            strWork = strKey.substring(0, iIndex);
        }

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
