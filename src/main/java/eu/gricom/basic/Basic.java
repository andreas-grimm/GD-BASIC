package eu.gricom.basic;

import eu.gricom.basic.codeGenerator.Generator;
import eu.gricom.basic.functions.Mem;
import eu.gricom.basic.tokenizer.BasicLexer;
import eu.gricom.basic.tokenizer.Lexer;
import eu.gricom.basic.tokenizer.Token;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.helper.FileHandler;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.helper.Printer;
import eu.gricom.basic.macroManager.MacroProcessor;
import eu.gricom.basic.memoryManager.Program;
import eu.gricom.basic.parser.BasicParser;
import eu.gricom.basic.runtimeManager.Execute;
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
 * (c) = 2020,...,2025 by Andreas Grimm, Den Haag, The Netherlands
 */
@SuppressWarnings("SpellCheckingInspection")
public class Basic {
    private Program _oProgram = new Program();
    private final transient Logger _oLogger = new Logger(this.getClass().getName());
    private static String _strCompileLanguage = "java";
    private static boolean _bCompile = false;
    private static boolean _bBeautified = false;
    private static boolean _bDartmouthFlag = false;
    private static boolean _bPCode = false;

    /**
     * Initializes a new instance of the BASIC interpreter, maintaining the interpreter's global state.
     */
    public Basic() {
    }


    /**
     * Expands macros in the program source code before tokenization and parsing.
     *
     * @param oProgram The program object containing the source code to process.
     * @return The updated program object with all macros expanded.
     */
    private Program macroProcessing(final Program oProgram) {
        _oLogger.info("Processing macros...");

        MacroProcessor oMacroProcessor = new MacroProcessor();

        try {
            oProgram.setProgram(oMacroProcessor.process(oProgram.getProgram()));
        } catch (SyntaxErrorException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return oProgram;
    }


    /**
     * Processes a BASIC program by performing macro expansion, tokenization, and parsing.
     *
     * This method updates the provided program object by expanding macros, converting the source code into tokens,
     * and parsing those tokens into executable statements. Any syntax errors encountered during macro processing or
     * tokenization will cause the program to print an error message and terminate immediately.
     *
     * @param oProgram The program object containing the source code to process.
     */
    public final void process(final Program oProgram) {
        _oLogger.info("Processing program...");

        _oLogger.info("Processing macros...");
        _oProgram = oProgram;
        MacroProcessor oMacroProcessor = new MacroProcessor();

        try {
            _oProgram.setProgram(oMacroProcessor.process(oProgram.getProgram()));
        } catch (SyntaxErrorException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        // Tokenize. At the end of the tokenization, I have the program transferred into a list of tokens and parameters
        _oLogger.info("Starting tokenization...");

        Lexer oTokenizer = new BasicLexer();

        try {
            _oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        } catch (SyntaxErrorException e) {
            // This syntax error has to generate due to the use of the macro. Original code errors in the lexer are
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
            BasicParser oParser = new BasicParser(oProgram.getTokens(), _bDartmouthFlag);
            _oProgram.setPreRunStatements(oParser.parsePreRun());
            _oProgram.setStatements(oParser.parse());
        } catch (SyntaxErrorException eSyntaxError) {
            _oLogger.error(eSyntaxError.getMessage());
        }
    }

    /**
     * Interprets and executes a BASIC program from source code.
     *
     * Performs macro processing, tokenization, and parsing to build the program's abstract syntax tree (AST), then executes the parsed statements. Handles flow control constructs such as GOTO and IF-THEN by tracking the current execution line. Terminates the process upon completion or if a syntax error occurs during macro processing or tokenization.
     *
     * @param oProgram The program object containing the source code to interpret.
     */
    public final void interpret(final Program oProgram) {

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

        // Tokenize. At the end of the tokenization, I have the program transferred into a list of tokens and parameters
        _oLogger.info("Starting tokenization...");

        Lexer oTokenizer = new BasicLexer();

        try {
            _oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        } catch (SyntaxErrorException e) {
            // This syntax error has to generate due to the use of the macro. Original code errors in the lexer are
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
            BasicParser oParser = new BasicParser(oProgram.getTokens(), _bDartmouthFlag);
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

        System.exit(0);
    }

    /****
     * Compiles a BASIC program by processing macros, tokenizing, parsing, and generating target code.
     *
     * Performs macro expansion, tokenization, and parsing on the provided program. Generates intermediate object code in JSON format or as runtime object code, and produces target language source code (currently Java) if specified. Terminates the process upon completion.
     *
     * @param oProgram   The BASIC program to compile.
     * @param strLanguage The target programming language for code generation (e.g., "java").
     */
    public final void compile(final Program oProgram, final String strLanguage) {
        _oLogger.info("Compiler started, using " + strLanguage + "...");

        // Find and process Macros.
        _oLogger.info("Processing macros...");
        _oProgram = macroProcessing(oProgram);

        // Tokenize. At the end of the tokenization, I have the program transferred into a list of tokens and parameters
        _oLogger.info("Starting tokenization...");

        Lexer oTokenizer = new BasicLexer();

        try {
            _oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        } catch (SyntaxErrorException e) {
            // This syntax error has to generate due to the use of the macro. Original code errors in the lexer are
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
            BasicParser oParser = new BasicParser(oProgram.getTokens(), _bDartmouthFlag);
            _oProgram.setPreRunStatements(oParser.parsePreRun());
            _oProgram.setStatements(oParser.parse());
        } catch (SyntaxErrorException eSyntaxError) {
            _oLogger.error(eSyntaxError.getMessage());
        }

        // Generate and store object code.
        _oLogger.info("Create the object code...");
        if (!_bPCode) {
            Generator.createJSONCode(_oProgram, _bBeautified);

            // Generate target code.
            _oLogger.info("Create the target code...");
            if (strLanguage.equals("java")) {
                Generator.createJavaCode();
            }

            // compile.

        } else {
            Generator.createObjectCode(_oProgram);
        }

        // Complete.
        System.exit(0);
    }


    // Utility stuff -----------------------------------------------------------

    /****
     * Entry point for the BASIC interpreter and compiler command-line application.
     *
     * Parses command-line arguments to configure interpreter or compiler options, loads the specified BASIC source file, and either interprets or compiles the program based on user flags. Supports options for verbosity, quiet mode, compile mode, beautified output, p-code generation, target language selection, Dartmouth BASIC compatibility, and help display. Exits the JVM after execution or compilation.
     *
     * @param args Command-line arguments for configuring execution and specifying the BASIC source file.
     */
    public static void main(final String[] args) {
        Logger oLogger = new Logger("main");
        Program oProgram = new Program();
        oLogger.setLogLevel("");

        CommandLine oCommandLine = null;

        // create the Options object
        Options options = new Options();

        try {
            options.addOption("h", false, "help (This screen)");
            options.addOption("i", true, "define input file");
            options.addOption("q", false, "quiet mode");
            options.addOption("v", true, "verbose level: (info, debug, trace, or error)");
            options.addOption("c", false, "compile");
            options.addOption("b", false, "beautified JSON intermediate code for compilation");
            options.addOption("p", false, "experimental: build p-code for later runtime component");
            options.addOption("l", true, "compile language <java>");
            options.addOption("d", false, "dartmouth mode");

            CommandLineParser parser = new DefaultParser();
            oCommandLine = parser.parse(options, args);
        } catch (ParseException eParseException) {
            System.out.println(eParseException.getMessage());
            System.exit(-1);
        }

        if (oCommandLine != null && !oCommandLine.hasOption("q")) {

            long lMaxMemory = Runtime.getRuntime().maxMemory() / 1024;
            long lFreeMem = Mem.execute().toInt() / 1024;
            Printer.println();
            Printer.println("   _____ _____             ____            _    ");
            Printer.println("  / ____|  __ \\           |  _ \\          (_)        GriCom Basic Interpreter Version 0.1.0");
            Printer.println(" | |  __| |  | |  ______  | |_) | __ _ ___ _  ___    (c) Copyright A.Grimm 2025");
            Printer.println(" | | |_ | |  | | |______| |  _ < / _` / __| |/ __|   ");
            Printer.println(" | |__| | |__| |          | |_) | (_| \\__ \\ | (__    Maximum memory (KBytes): " + lMaxMemory);
            Printer.println("  \\_____|_____/           |____/ \\__,_|___/_|\\___|   Free memory (KBytes): " + lFreeMem);
            Printer.println();
        }

        if (oCommandLine != null && oCommandLine.hasOption("v")) {
            String strLogLevel = oCommandLine.getOptionValue("v");
            String strLogLevelList = "trace|debug|info|warning";

            if (strLogLevelList.contains(strLogLevel.toLowerCase(Locale.ROOT))) {
                oLogger.setLogLevel(strLogLevel);
            }

            oLogger.debug("Log Level set:" + strLogLevel + "...");
        }

        if (oCommandLine != null && oCommandLine.hasOption("l")) {
            String strLanguage = oCommandLine.getOptionValue("l");
            String strLanguageList = "java";

            if (strLanguageList.contains(strLanguage.toLowerCase(Locale.ROOT))) {
                _strCompileLanguage = strLanguage;
            }

            oLogger.debug("Compile Language:" + strLanguage + "...");
        }

        if (oCommandLine != null && oCommandLine.hasOption("c")) {
            _bCompile = true;
            oLogger.debug("Compiler selected...");
        }

        if (oCommandLine != null && oCommandLine.hasOption("b")) {
            _bBeautified = true;
            oLogger.debug("Compiler is generating beautified JSON intermediate code...");
        }

        if (oCommandLine != null && oCommandLine.hasOption("p")) {
            _bPCode = true;
            oLogger.debug("Compiler is generating object code for runtime component...");
        }

        if (oCommandLine != null && oCommandLine.hasOption("h")) {
            // automatically generator the help statement
            oLogger.debug("Display help message...");

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar BASIC-<build-name>.jar <filename.bas>", options);
        }

        if (oCommandLine != null && oCommandLine.hasOption("d")) {
            _bDartmouthFlag = true;
            oLogger.debug("Dartmouth mode selected...");
        }

        if (oCommandLine != null) {
            List<String> astrArguments = oCommandLine.getArgList();

            if (astrArguments.isEmpty()) {
                oLogger.error("Program file name missing...");
                Printer.println("");
                Printer.println("usage: java -jar BASIC-<build-name>.jar <filename.bas>");
                Printer.println("where <filename.bas> is a relative path to a .bas program to run.");
                System.exit(-1);
            }

            String strFileName = astrArguments.getLast();

            // Read the file.
            oLogger.info("Read file: " + strFileName + "...");
            oProgram.load(strFileName, FileHandler.readFile(strFileName));

            // Run it.
            Basic oBasic = new Basic();

            if (_bCompile) {
                oLogger.info("Run the compiler...");
                oBasic.compile(oProgram, _strCompileLanguage);
            } else {
                oLogger.info("Run the interpreter...");
                oBasic.interpret(oProgram);
            }

            System.exit(-1);
        }
    }
}
