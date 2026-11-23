package eu.gricom.basic.lineeditor;

import eu.gricom.basic.compiler.BasicCompiler;
import eu.gricom.basic.compiler.CompileException;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.helper.Printer;
import eu.gricom.basic.macromanager.MacroProcessor;
import eu.gricom.basic.memorymanager.Program;
import eu.gricom.basic.parser.BasicParser;
import eu.gricom.basic.runtimemanager.Execute;
import eu.gricom.basic.statements.Statement;
import eu.gricom.basic.tokenizer.BasicLexer;
import eu.gricom.basic.tokenizer.Lexer;
import eu.gricom.basic.tokenizer.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LineEditor {
    /** The file name used by SAVEJAVA when no name is given and no -o target is set. */
    private static final String DEFAULT_JAVA_FILE = "BasicProgram.java";

    private Program _oProgram;
    private BufferedReader _oReader;
    private boolean _bDartmouthFlag = false;
    private String _strTargetName = null;
    private static final Logger _oLogger = new Logger(LineEditor.class.getName());


    public LineEditor(Program oProgram, boolean bDartmouthFlag) {
        _oProgram = oProgram;
        _bDartmouthFlag = bDartmouthFlag;
        _oReader = eu.gricom.basic.helper.ConsoleInput.getReader();
    }

    /**
     * Sets the default build target given on the command line (-o). COMPILE uses it when no name is typed.
     *
     * @param strTargetName the build target name from -o, or null when it was not given
     */
    public void setCompileTargets(String strTargetName) {
        _strTargetName = strTargetName;
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
        } catch (IOException oException) {
            _oLogger.error("Catastrophic error: " + oException.getMessage());
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

                // Tokenise. At the end of the tokenisation, I have the program transferred into a list of tokens and parameters
                    _oLogger.info("Starting tokenisation...");

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

                } catch (SyntaxErrorException oException) {
                    Printer.println("Syntax Error: " + oException.getMessage());
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
                    case "COMPILE":
                        compileProgram(strLine);
                        break;
                    case "SAVEJAVA":
                        saveJava(strLine);
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
        } catch (SyntaxErrorException oException) {
            _oLogger.error(oException.getMessage());
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
        } catch (NumberFormatException oException) {
            Printer.println("DELETE: invalid line number(s)");
        } catch (SyntaxErrorException oException) {
            Printer.println("DELETE error: " + oException.getMessage());
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
        } catch (eu.gricom.basic.error.FileNotFoundException oException) {
            Printer.println("LOAD error: " + oException.getMessage());
        } catch (eu.gricom.basic.error.EmptyProgramException oException) {
            Printer.println("LOAD error: " + oException.getMessage());
        } catch (SyntaxErrorException oException) {
            Printer.println("LOAD error: " + oException.getMessage());
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
        } catch (eu.gricom.basic.error.FileAlreadyExistsException oException) {
            Printer.println("SAVE error: " + oException.getMessage());
        }
    }

    /**
     * Print help information from help.txt resource file.
     */
    /**
     * Returns the name typed after a command, or null when no name was typed.
     * For example "COMPILE hello" gives "hello", and "COMPILE" gives null.
     *
     * @param strLine the command line as entered
     * @return the name, or null
     */
    static String targetNameFromCommand(String strLine) {
        String[] astrWords = strLine.trim().split("\\s+");
        return astrWords.length > 1 ? astrWords[1] : null;
    }

    /**
     * Removes a trailing ".jar" from a target name, so "hello.jar" and "hello" both give "hello".
     *
     * @param strName the target name as typed
     * @return the base name, or null when nothing is left (for example "" or ".jar")
     */
    static String baseNameOf(String strName) {
        if (strName == null) {
            return null;
        }
        String strBase = strName.endsWith(".jar") ? strName.substring(0, strName.length() - ".jar".length()) : strName;
        return strBase.isEmpty() ? null : strBase;
    }

    /**
     * COMPILE [name]: writes the Java source file (name.java), compiles it, and builds the runnable jar (name.jar).
     * The name is typed after the command; without a name the -o target is used. Given "hello" or "hello.jar",
     * both hello.java and hello.jar are written.
     *
     * @param strLine the command line as entered
     */
    private void compileProgram(String strLine) {
        String strGiven = targetNameFromCommand(strLine);
        String strBase = baseNameOf(strGiven != null ? strGiven : _strTargetName);
        if (strBase == null) {
            Printer.println("COMPILE: give a target name, for example COMPILE hello (or start with -o hello).");
            return;
        }
        List<Statement> voStatements = parseProgram();
        if (voStatements == null) {
            return;
        }
        Path oJavaFile = Paths.get(strBase + ".java");
        Path oJarFile = Paths.get(strBase + ".jar");
        try {
            BasicCompiler.compileToJar(_oProgram.getPreRunStatements(), voStatements, oJavaFile, oJarFile);
            Printer.println("Java source written to " + oJavaFile);
            Printer.println("Build target created: " + oJarFile + " (start it with: java -jar " + oJarFile + ")");
        } catch (CompileException oException) {
            Printer.println("COMPILE error: " + oException.getMessage());
        } catch (IOException oException) {
            Printer.println("COMPILE error: " + oException.getMessage());
        }
    }

    /**
     * SAVEJAVA [file.java]: writes the generated Java source to a file without compiling it. Without a file name,
     * the Java file is named after the -o target (or BasicProgram.java when -o is not given).
     *
     * @param strLine the command line as entered
     */
    private void saveJava(String strLine) {
        List<Statement> voStatements = parseProgram();
        if (voStatements == null) {
            return;
        }
        String strFile = targetNameFromCommand(strLine);
        if (strFile == null) {
            String strBase = baseNameOf(_strTargetName);
            strFile = strBase != null ? strBase + ".java" : DEFAULT_JAVA_FILE;
        }
        try {
            BasicCompiler.saveJava(_oProgram.getPreRunStatements(), voStatements, Paths.get(strFile));
            Printer.println("Java source saved to " + strFile);
        } catch (CompileException oException) {
            Printer.println("SAVEJAVA error: " + oException.getMessage());
        } catch (IOException oException) {
            Printer.println("SAVEJAVA error: " + oException.getMessage());
        }
    }

    /** Parses the loaded program the same way RUN does. Returns null (and prints the reason) on failure. */
    private List<Statement> parseProgram() {
        if (!_oProgram.hasContent()) {
            Printer.println("No program loaded. Use LOAD command or enter program lines.");
            return null;
        }
        try {
            BasicParser oParser = new BasicParser(_oProgram.getTokens(), _bDartmouthFlag);
            _oProgram.setPreRunStatements(oParser.parsePreRun());
            _oProgram.setStatements(oParser.parse());
            return _oProgram.getStatements();
        } catch (SyntaxErrorException oException) {
            Printer.println(oException.getMessage());
            return null;
        }
    }

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
        } catch (Exception oException) {
            Printer.println("Error reading help file: " + oException.getMessage());
        }
    }

    private boolean isInteger(String strToBeTested) {
        try {
            Integer.parseInt(strToBeTested);
            return true;
        } catch (NumberFormatException oException) {
            return false;
        }
    }
}
