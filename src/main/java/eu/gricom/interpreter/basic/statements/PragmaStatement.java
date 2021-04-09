package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.helper.Logger;
import eu.gricom.interpreter.basic.helper.Trace;
import java.util.Locale;

/**
 * PragmaStatement.java
 * <p>
 * Description:
 * <p>
 * The Pragma Statement changes the behaviour of the interpreter / compiler during execution from the moment it is
 * found. Currently the following paramter can be modified:
 * Debug Level
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
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
        return new String("Pragma Statement: Set " + _strParameter + " to " + _strValue);
    }
}
