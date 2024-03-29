package eu.gricom.interpreter.basic.runtimeManager;

import eu.gricom.interpreter.basic.helper.Logger;
import eu.gricom.interpreter.basic.helper.Trace;
import eu.gricom.interpreter.basic.memoryManager.LineNumberXRef;
import eu.gricom.interpreter.basic.memoryManager.ProgramPointer;
import eu.gricom.interpreter.basic.statements.Statement;
import java.util.List;

public class Execute {
    private final transient Logger _oLogger = new Logger(this.getClass().getName());
    private final transient LineNumberXRef _oLineNumbers = new LineNumberXRef();
    private final ProgramPointer _oProgramPointer = new ProgramPointer();
    private final List<Statement> _aoPreRunStatements;
    private final List<Statement> _aoStatements;
    private final Trace _oTrace = new Trace(false);

    public Execute(List<Statement> aoPreRunStatements, List<Statement> aoStatements) {
        _aoPreRunStatements = aoPreRunStatements;
        _aoStatements = aoStatements;
    }

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
}