package eu.gricom.basic.lineEditor;

import eu.gricom.basic.error.EndOfProgramException;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.helper.EnvParam;
import eu.gricom.basic.helper.FileHandler;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.helper.Printer;
import eu.gricom.basic.macroManager.MacroProcessor;
import eu.gricom.basic.memoryManager.Program;
import eu.gricom.basic.parser.BasicParser;
import eu.gricom.basic.runtimeManager.Execute;
import eu.gricom.basic.tokenizer.BasicLexer;
import eu.gricom.basic.tokenizer.Lexer;
import eu.gricom.basic.tokenizer.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
                int iLineNumber = Integer.parseInt(strFirstWord);

                try {
                    _oProgram.addOrReplace(iLineNumber, strLine);

                    MacroProcessor oMacroProcessor = new MacroProcessor();

                    _oProgram.setProgram(oMacroProcessor.process(_oProgram.getProgram()));

                // Tokenize. At the end of the tokenization, I have the program transferred into a list of tokens and parameters
                    _oLogger.info("Starting tokenization...");

                    Lexer oTokenizer = new BasicLexer();

                    _oProgram.setTokens(oTokenizer.tokenize(_oProgram.getProgram()));

                    int iCounter = 0;
                    for (Token oToken: _oProgram.getTokens()) {
                        if (oToken.getType().toString().contains("LINE")) {
                            _oLogger.debug("[" + oToken.getLine() + "] Token # <" + iCounter + ">: [" + oToken.getType() + "]: []");
                        } else {
                            _oLogger.debug("[" + oToken.getLine() + "] Token # <" + iCounter + ">: [" + oToken.getType() + "]: ["
                                    + oToken.getText() + "]");
                        }
                        iCounter++;
                    }

                } catch (SyntaxErrorException eSyntaxErrorException) {
                    Printer.println("Syntax Error: " + eSyntaxErrorException.getMessage());
                }
            } else {
// let's interpret the entered command:
                strFirstWord = strFirstWord.toUpperCase();
                switch (strFirstWord) {
                    case "BYE":
                    case "EXIT":
                    case "QUIT":
                        bExit = true;
                        break;
                    case "LIST":
                        Printer.print(_oProgram.getProgram());
                        break;
                    case "RUN":
                        run();
                        break;
                    case "DELETE":
                        deleteLines(strLine);
                        break;
                    case "LOAD":
                        load(strLine);
                        break;
                    case "SAVE":
                        save(strLine);
                        break;
                    case "HELP":
                        printHelp();
                        break;
                    default:
                        Printer.println("Syntax error");
                }
            }
        }
        return bExit;
    }

    private void run() {
        if (!_oProgram.hasContent()) {
            Printer.println("RUN: No program loaded. Use LOAD command or enter program lines.");
            return;
        }

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

    /**
     * Parse and execute the DELETE command.
     *
     * Syntax: DELETE lineNumber
     *         DELETE lineBegin lineEnd
     *         DELETE lineBegin,lineEnd
     *         DELETE lineBegin, lineEnd
     *
     * @param strLine The complete command line including "DELETE" and parameters
     */
    private void deleteLines(String strLine) {
        String strRemainder = strLine.substring(6).trim();

        if (strRemainder.isEmpty()) {
            Printer.println("DELETE: missing line number(s)");
            return;
        }

        String[] astrNumbers = strRemainder.replaceAll(",", " ").split("\\s+");

        if (astrNumbers.length == 0) {
            Printer.println("DELETE: invalid syntax");
            return;
        }

        try {
            if (astrNumbers.length == 1) {
                int iLineNumber = Integer.parseInt(astrNumbers[0]);
                _oProgram.deleteLines(iLineNumber, iLineNumber);
                Printer.println("Deleted line " + iLineNumber);
            } else if (astrNumbers.length >= 2) {
                int iBegin = Integer.parseInt(astrNumbers[0]);
                int iEnd = Integer.parseInt(astrNumbers[1]);
                int iMin = Math.min(iBegin, iEnd);
                int iMax = Math.max(iBegin, iEnd);
                _oProgram.deleteLines(iMin, iMax);
                Printer.println("Deleted lines " + iMin + " to " + iMax);
            }
        } catch (NumberFormatException e) {
            Printer.println("DELETE: invalid line number(s)");
        } catch (SyntaxErrorException e) {
            Printer.println("DELETE error: " + e.getMessage());
        }
    }

    /**
     * Parse and execute the LOAD command.
     *
     * Syntax: LOAD filename
     *
     * @param strLine The complete command line including "LOAD" and the filename
     */
    private void load(String strLine) {
        String strRemainder = strLine.substring(4).trim();

        if (strRemainder.isEmpty()) {
            Printer.println("LOAD: missing filename");
            return;
        }

        try {
            _oProgram.loadProgram(strRemainder);
            Printer.println("Program loaded from " + strRemainder);
        } catch (eu.gricom.basic.error.FileNotFoundException e) {
            Printer.println("LOAD error: " + e.getMessage());
        } catch (eu.gricom.basic.error.EmptyProgramException e) {
            Printer.println("LOAD error: " + e.getMessage());
        } catch (SyntaxErrorException e) {
            Printer.println("LOAD error: " + e.getMessage());
        }
    }

    /**
     * Parse and execute the SAVE command.
     *
     * Syntax: SAVE filename
     *
     * @param strLine The complete command line including "SAVE" and the filename
     */
    private void save(String strLine) {
        String strRemainder = strLine.substring(4).trim();

        if (strRemainder.isEmpty()) {
            Printer.println("SAVE: missing filename");
            return;
        }

        try {
            _oProgram.save(strRemainder);
            Printer.println("Program saved to " + strRemainder);
        } catch (eu.gricom.basic.error.FileAlreadyExistsException e) {
            Printer.println("SAVE error: " + e.getMessage());
        }
    }

    /**
     * Print help information from help.txt resource file.
     */
    private void printHelp() {
        try {
            ClassLoader oClassLoader = Thread.currentThread().getContextClassLoader();
            InputStream oInputStream = oClassLoader.getResourceAsStream("help.txt");

            if (oInputStream == null) {
                Printer.println("Help file not found");
                return;
            }

            BufferedReader oReader = new BufferedReader(new InputStreamReader(oInputStream));
            StringBuilder oBuilder = new StringBuilder();
            String strLine;

            while ((strLine = oReader.readLine()) != null) {
                oBuilder.append(strLine).append("\n");
            }

            oReader.close();

            if (oBuilder.length() > 0) {
                Printer.print(oBuilder.toString());
            } else {
                Printer.println("Help file is empty");
            }
        } catch (Exception e) {
            Printer.println("Error reading help file: " + e.getMessage());
        }
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
