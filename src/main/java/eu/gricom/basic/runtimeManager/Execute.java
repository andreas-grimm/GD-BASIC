package eu.gricom.basic.runtimeManager;

import eu.gricom.basic.helper.Trace;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.memoryManager.LineNumberXRef;
import eu.gricom.basic.memoryManager.Program;
import eu.gricom.basic.memoryManager.ProgramPointer;
import eu.gricom.basic.statements.Statement;
import java.util.List;

public class Execute {
    private final transient Logger _oLogger = new Logger(this.getClass().getName());
    private final transient LineNumberXRef _oLineNumbers = new LineNumberXRef();
    private final ProgramPointer _oProgramPointer = new ProgramPointer();
    private final List<Statement> _aoPreRunStatements;
    private final List<Statement> _aoStatements;
    private final Trace _oTrace = new Trace(false);

    private Statement _oStatement = null;

    /**
     * Initializes the Execute instance with pre-run and main program statements from the given Program.
     *
     * @param oProgram the Program containing pre-run and main statements to be executed
     */
    public Execute(Program oProgram) {
        _aoPreRunStatements = oProgram.getPreRunStatements();
        _aoStatements = oProgram.getStatements();
    }

    /**
     * Executes all pre-run initialization statements for the BASIC program.
     *
     * If no pre-run statements are available, logs an error and terminates the program.
     */
    public void loadEnvironment() {
        _oLogger.info("Pre-load environment...");
        try {
            if (_aoPreRunStatements != null) {
                _oProgramPointer.setCurrentStatement(0);
                while (_oProgramPointer.getCurrentStatement() < _aoPreRunStatements.size()) {
                    // as long as we have not reached the end of the code
                    int iThisStatement = _oProgramPointer.getCurrentStatement();

                    _oProgramPointer.calcNextStatement();
                    _oLogger.debug("PreRun Statement # <" + iThisStatement + ">: [" + _aoPreRunStatements.get(
                            iThisStatement).content() + "]");

                    _aoPreRunStatements.get(iThisStatement).execute();
                }
            } else {
                _oLogger.error("Parsing delivered empty program");
                System.exit(- 1);
            }
        } catch (Exception eException) {
            eException.printStackTrace();
        }
    }

    /**
     * Executes the main statements of the BASIC program sequentially.
     *
     * Iterates through all main program statements, updating the program pointer and executing each statement in order. If no main statements are present, logs an error and terminates the program.
     */
    public void runProgram() {

        _oLogger.info("Starting execution...");
        try {
            if (_aoStatements != null) {
                int iSourceCodeLineNumber = -1;
                _oProgramPointer.setCurrentStatement(0);

                while (_oProgramPointer.getCurrentStatement() < _aoStatements.size()) {
                    // as long as we have not reached the end of the code
                    int iThisStatement = _oProgramPointer.getCurrentStatement();

                    iSourceCodeLineNumber =
                            _oLineNumbers.getLineNumberFromToken(_aoStatements.get(iThisStatement).getTokenNumber());

                    _oProgramPointer.calcNextStatement();

                    _oLogger.debug(
                            "Basic Source Code Line [" + iSourceCodeLineNumber + "] Statement [ "
                                    + _aoStatements.get(iThisStatement).getTokenNumber() + "]: "
                                    + _aoStatements.get(iThisStatement).content());

                    _oTrace.trace(iSourceCodeLineNumber);

                    _oStatement = _aoStatements.get(iThisStatement);
                    _aoStatements.get(iThisStatement).execute();
                }
            } else {
                _oLogger.error("Parsing delivered empty program");
                System.exit(-1);
            }
        } catch (Exception eException) {
            eException.printStackTrace();
        }
    }

    /**
     * Returns the last executed statement of the program.
     *
     * @return the most recently executed Statement, or null if no statement has been executed yet
     */
    public Statement getFinalStatement() {
        return _oStatement;
    }
}