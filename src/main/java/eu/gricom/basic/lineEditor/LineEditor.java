package eu.gricom.basic.lineEditor;

import eu.gricom.basic.error.EndOfProgramException;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.helper.EnvParam;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.helper.Printer;
import eu.gricom.basic.memoryManager.Program;
import eu.gricom.basic.parser.BasicParser;
import eu.gricom.basic.runtimeManager.Execute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LineEditor {
    private Program _oProgram;
    private BufferedReader _oReader;
    private boolean _bDartmouthFlag = false;
    private static final Logger _oLogger = new Logger(LineEditor.class.getName());


    public LineEditor(Program oProgram, boolean bDartmouthFlag) {
        _oProgram = oProgram;
        _bDartmouthFlag = bDartmouthFlag;
        _oReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void execute() {
        boolean bExit = false;
        Printer.println("Welcome");

        try {
            while (!bExit) {
                Printer.print(">");
                String strEnteredLine = _oReader.readLine();
                _oLogger.debug("Entered line: [" + strEnteredLine + "]");
                bExit = processLine(strEnteredLine);
            }
        } catch (IOException eException) {
            _oLogger.error("Catastrophic error: " + eException.getMessage());
            System.exit(-1);
        }
        Printer.println("Good bye.");
    }

    private boolean processLine(String strLine) {
        boolean bExit = false;
        String strFirstWord;

        if (strLine.length() >= 1) {
            //not an empty line, we need to process:

            // let's check whether we talk about one or more words...
            if (strLine.indexOf(" ") > 0) {
                strFirstWord = strLine.substring(0, strLine.indexOf(" "));
            } else {
                strFirstWord = strLine;
            }

            //verify whether this is supposed to be a BASIC program line or an editor command.
            if (isInteger(strFirstWord)) {
// take the line number, loop through the program and add or replace the line in the program
            } else {
// let's interpret the entered command:
                strFirstWord = strFirstWord.toUpperCase();
                switch (strFirstWord) {
                    case "BYE":
                    case "EXIT":
                        bExit = true;
                        break;
                    case "LIST":
                        break;
                    case "RUN":
                        run();
                        break;
                    default:
                        Printer.println("Syntax error");
                }
            }
        }
        return bExit;
    }

    private void run() {
        boolean bSuccessfulCompleted = false;
        // Parse.
        _oLogger.info("Starting parsing...");
        try {
            BasicParser oParser = new BasicParser(_oProgram.getTokens(), _bDartmouthFlag);
            _oProgram.setPreRunStatements(oParser.parsePreRun());
            _oProgram.setStatements(oParser.parse());
        } catch (SyntaxErrorException eSyntaxError) {
            _oLogger.error(eSyntaxError.getMessage());
        }

        // Run.
        Execute oRun = new Execute(_oProgram);

        // load the environment for the execution
        oRun.loadEnvironment();

        // run the program
        oRun.runProgram();
    }

    private void list() {

    }

    private boolean isInteger(String strToBeTested) {
        try {
            Integer.parseInt(strToBeTested);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
