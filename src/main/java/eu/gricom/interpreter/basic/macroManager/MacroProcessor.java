package eu.gricom.interpreter.basic.macroManager;

import eu.gricom.interpreter.basic.helper.Logger;
import eu.gricom.interpreter.basic.memoryManager.Program;

public class MacroProcessor {

    private final transient Logger _oLogger = new Logger(this.getClass().getName());

    /**
     * Constructs a new Program instance. The instance stores the global state of
     * the program such as the values of all of the variables and the
     * current statement.
     */
    public MacroProcessor() {
        _oLogger.info("Processing Macros into the program code...");
    }

    /**
     * Process Function.
     * This function converts the macros into code that is added to the code.
     *
     * @param strProgram - the program to be processed to find and replace macros.
     * @return - the generated code after the macros have benn processed
     */
    public String process (String strProgram) {
        return strProgram;
    }
}
