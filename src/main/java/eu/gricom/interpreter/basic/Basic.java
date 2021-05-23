package eu.gricom.interpreter.basic;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.functions.Mem;
import eu.gricom.interpreter.basic.helper.FileHandler;
import eu.gricom.interpreter.basic.helper.Logger;
import eu.gricom.interpreter.basic.helper.Printer;
import eu.gricom.interpreter.basic.helper.Trace;
import eu.gricom.interpreter.basic.macroManager.MacroProcessor;
import eu.gricom.interpreter.basic.memoryManager.Program;
import eu.gricom.interpreter.basic.memoryManager.ProgramPointer;
import eu.gricom.interpreter.basic.parser.BasicParser;
import eu.gricom.interpreter.basic.memoryManager.LineNumberXRef;
import eu.gricom.interpreter.basic.tokenizer.BasicLexer;
import eu.gricom.interpreter.basic.tokenizer.Lexer;
import eu.gricom.interpreter.basic.tokenizer.Token;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.List;
import java.util.Locale;

/**
 * Basic.java
 * <p>
 * Description:
 * <p>
 * This is the main class of the Basic interpreter. It manages the execution flow of the program: read, tokenize,
 * parse, and interpret.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
@SuppressWarnings("SpellCheckingInspection")
public class Basic {
    private Program _oProgram = new Program();
    private final transient Logger _oLogger = new Logger(this.getClass().getName());
    private final transient LineNumberXRef _oLineNumbers = new LineNumberXRef();

    /**
     * Constructs a new Basic instance. The instance stores the global state of
     * the interpreter such as the values of all of the variables and the
     * current statement.
     */
    public Basic() {
    }

    /**
     * Interpret.
     * This is where the magic happens. This runs the code through the parsing pipeline to generate the AST. Then it
     * executes each statement. It keeps track of the current line in a member variable that the statement objects
     * have access to. This lets "goto" and "if then" do flow control by simply setting the index of the current statement.
     *
     * In an interpreter that didn't mix the interpretation logic in with the AST node classes, this would be doing a lot more work.
     *
     * @param oProgram The program object, containing the source code of a .bas script to interpret.
     */
    public final void interpret(final Program oProgram) {
        ProgramPointer oProgramPointer = new ProgramPointer();

        Trace oTrace = new Trace(false);

        // Find and process Macros.
        _oLogger.info("Processing macros...");
        _oProgram = oProgram;
        MacroProcessor oMacroProcessor = new MacroProcessor();

        try {
            _oProgram.setProgram(oMacroProcessor.process(oProgram.getProgram()));
        } catch (SyntaxErrorException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        // Tokenize. At the end of the tokenization I have the program transferred into a list of tokens and parameters
        _oLogger.info("Starting tokenization...");

        Lexer oTokenizer = new BasicLexer();

        try {
            _oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        } catch (SyntaxErrorException e) {
            // This syntax error has to generated due to the use of the macro. Original code errors in the lexer are
            // discovered in the previous step.
            System.out.println(e.getMessage());
            System.exit(1);
        }

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

        // Parse.
        _oLogger.info("Starting parsing...");
        try {
            BasicParser oParser = new BasicParser(oProgram.getTokens());
            _oProgram.setPreRunStatements(oParser.parsePreRun());
            _oProgram.setStatements(oParser.parse());
        } catch (SyntaxErrorException eSyntaxError) {
            eSyntaxError.printStackTrace();
        }

        // Run.
        _oLogger.info("Pre-load environment...");
        try {
            if (_oProgram.getPreRunStatements() != null) {
                oProgramPointer.setCurrentStatement(0);
                while (oProgramPointer.getCurrentStatement() < _oProgram.getPreRunStatements().size()) {
                    // as long as we have not reached the end of the code
                    int iThisStatement = oProgramPointer.getCurrentStatement();

                    oProgramPointer.calcNextStatement();
                    _oLogger.debug("PreRun Statement # <" + iThisStatement + ">: ["
                                           + _oProgram.getPreRunStatements().get(iThisStatement).content() + "]");

                    _oProgram.getPreRunStatements().get(iThisStatement).execute();
                }
            } else {
                _oLogger.error("Parsing delivered empty program");
                System.exit(-1);
            }
        } catch (Exception eException) {
            eException.printStackTrace();
        }

        _oLogger.info("Starting execution...");
        try {
            if (_oProgram.getStatements() != null) {
                int iSourceCodeLineNumber;
                oProgramPointer.setCurrentStatement(0);

                while (oProgramPointer.getCurrentStatement() < _oProgram.getStatements().size()) {
                    // as long as we have not reached the end of the code
                    int iThisStatement = oProgramPointer.getCurrentStatement();

                    iSourceCodeLineNumber = _oLineNumbers.getLineNumberFromToken(_oProgram.getStatements().get(iThisStatement).getTokenNumber());

                    oProgramPointer.calcNextStatement();

                    _oLogger.debug(
                            "Basic Source Code Line [" + iSourceCodeLineNumber + "] Statement [ "
                                    + _oProgram.getStatements().get(iThisStatement).getTokenNumber() + "]: "
                                    + _oProgram.getStatements().get(iThisStatement).content());
                    oTrace.trace(iSourceCodeLineNumber);

                    _oProgram.getStatements().get(iThisStatement).execute();
                }
            } else {
                _oLogger.error("Parsing delivered empty program");
                System.exit(-1);
            }
        } catch (Exception eException) {
            eException.printStackTrace();
        }

        System.exit(0);
    }

    // Utility stuff -----------------------------------------------------------

    /**
     * Runs the interpreter as a command-line app. Takes one argument: a path
     * to a script file to load and run. The script should contain one
     * statement per line.
     *
     * @param args Command-line arguments.
     */
    public static void main(final String[] args) {
        Logger oLogger = new Logger("main");
        Program oProgram = new Program();
        oLogger.setLogLevel("");

        CommandLine oCommandLine = null;

        // create Options object
        Options options = new Options();

        try {
            options.addOption("h", false, "help (This screen)");
            options.addOption("i", true, "define input file");
            options.addOption("q", false, "quiet mode");
            options.addOption("v", true, "verbose level: (0 - 3)");

            CommandLineParser parser = new DefaultParser();
            oCommandLine = parser.parse(options, args);
        } catch (ParseException eParseException) {
            System.out.println(eParseException.getMessage());
            System.exit(-1);
        }

        if (oCommandLine != null
                && !oCommandLine.hasOption("q")) {

            long lMaxMemory = Runtime.getRuntime().maxMemory() / 1024;
            long lFreeMem = Mem.execute().toInt() / 1024;
            Printer.println();
            Printer.println("  #####  ######      ######     #     #####  ###  #####     ");
            Printer.println(" #     # #     #     #     #   # #   #     #  #  #     #    GriCom Basic Interpreter Version 0.1.0");
            Printer.println(" #       #     #     #     #  #   #  #        #  #          (c) Copyright A.Grimm 2020");
            Printer.println(" #  #### #     # ### ######  #     #  #####   #  #          ");
            Printer.println(" #     # #     #     #     # #######       #  #  #          Maximum memory (KBytes): " + lMaxMemory);
            Printer.println(" #     # #     #     #     # #     # #     #  #  #     #    Free memory (KBytes): " + lFreeMem);
            Printer.println("  #####  ######      ######  #     #  #####  ###  #####     ");
            Printer.println();
        }

        if (oCommandLine != null
                && oCommandLine.hasOption("v")) {
            String strLogLevel = oCommandLine.getOptionValue("v");
            String strLogLevelList = "trace|debug|info|warning";

            if (strLogLevelList.contains(strLogLevel.toLowerCase(Locale.ROOT))) {
                oLogger.setLogLevel(strLogLevel);
            }

            oLogger.debug("Log Level set:" + strLogLevel + "...");
        }

        if (oCommandLine != null
                && oCommandLine.hasOption("h")) {
            // automatically generate the help statement
            oLogger.debug("Display help message...");

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar BASIC-<build-name>.jar <filename.bas>", options);
        }

        List<String> astrArguments = oCommandLine.getArgList();

        if (astrArguments.size() < 1) {
            oLogger.error("Program file name missing...");
            Printer.println("");
            Printer.println("usage: java -jar BASIC-<build-name>.jar <filename.bas>");
            Printer.println("where <filename.bas> is a relative path to a .bas program to run.");
            System.exit(-1);
        }

        for (String strArgument : astrArguments) {

            // Read the file.
            oLogger.info("Read file: " + strArgument + "...");
            oProgram.load(FileHandler.readFile(strArgument));

            // Run it.
            oLogger.info("Run the interpreter...");
            Basic oBasic = new Basic();
            oBasic.interpret(oProgram);
        }
    }
}
