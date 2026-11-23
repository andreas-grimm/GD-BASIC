package eu.gricom.basic.statements;

import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.helper.Trace;
import java.util.Locale;

/**
 * PragmaStatement.java
 * <p>
 * Description: The PragmaStatement class implements the @PRAGMA directive, which modifies interpreter or compiler
 * behaviour at runtime. Supported parameters include debug level, trace mode, and other execution settings that can
 * be toggled during program execution.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class PragmaStatement implements Statement {
    private final String _strParameter;
    private final String _strValue;
    private final int _iTokenNumber;

    /**
     * add a label destination in the memory management.
     *
     * @param strParameter to be changed - name of the label
     * @param strValue - new value
     * @param iTokenNumber - number of the token in which the PRAGMA statement is added
     */
    public PragmaStatement(final int iTokenNumber, final String strParameter, final String strValue) {
        _strParameter = strParameter;
        _strValue = strValue;
        _iTokenNumber = iTokenNumber;
    }

    private void changeLogLevel(final String strLogLevel) {
        Logger oLogger = new Logger(this.getClass().getName());

        String strLogLevelList = "trace|debug|info|warning|off";

        if (strLogLevelList.contains(strLogLevel.toLowerCase(Locale.ROOT))) {
            oLogger.setLogLevel(strLogLevel);
        }

        oLogger.debug("Log Level set to: " + strLogLevel + "...");
    }

    private void changeTraceLevel(final String strTraceLevel) {
        if (strTraceLevel.toLowerCase(Locale.ROOT).contains("on")) {
            Trace oTrace = new Trace(true);
        } else {
            Trace oTrace = new Trace(false);
        }
    }

    @Override
    public final int getTokenNumber() {
        return _iTokenNumber;
    }

    @Override
    public final void execute() {
        if (_strParameter.toLowerCase(Locale.ROOT).contains("log")) {
            changeLogLevel(_strValue);
        }

        if (_strParameter.toLowerCase(Locale.ROOT).contains("trace")) {
            changeTraceLevel(_strValue);
        }
    }

    @Override
    public final String content() throws Exception {
        return "Pragma Statement: Set " + _strParameter + " to " + _strValue;
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
        String strReturn = "{\"PRAGMA\": {";
        strReturn += "\"TOKEN_NR\": \"" + _iTokenNumber + "\",";
        strReturn += "\"PARAMETER\": \"" + _strParameter + "\",";
        strReturn += "\"VALUE\": \"" + _strValue + "\"";
        strReturn += "}}";
        return strReturn;
    }

    /**
     * Tells whether a pragma name is one the interpreter knows. A name is known when it contains "log" (for example
     * LOG_LEVEL or Logger) or "trace" (for example Trace), in any letter case. Other names are refused by the parser.
     *
     * @param strParameter the pragma name written in the program
     * @return true if the name is known
     */
    public static boolean isKnownParameter(final String strParameter) {
        String strLower = strParameter.toLowerCase(Locale.ROOT);
        return strLower.contains("log") || strLower.contains("trace");
    }

    /** @return the pragma name, for example LOG_LEVEL (used by the compiler) */
    public String getParameter() {
        return _strParameter;
    }

    /** @return the pragma value (used by the compiler) */
    public String getValue() {
        return _strValue;
    }
}
