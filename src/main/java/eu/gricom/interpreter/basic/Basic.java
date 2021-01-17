package eu.gricom.interpreter.basic;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.helper.FileHandler;
import eu.gricom.interpreter.basic.helper.Logger;
import eu.gricom.interpreter.basic.helper.Printer;
import eu.gricom.interpreter.basic.memoryManager.ProgramPointer;
import eu.gricom.interpreter.basic.parser.BasicParser;
import eu.gricom.interpreter.basic.parser.JasicParser;
import eu.gricom.interpreter.basic.parser.Parser;
import eu.gricom.interpreter.basic.statements.LineNumberStatement;
import eu.gricom.interpreter.basic.statements.Statement;
import eu.gricom.interpreter.basic.tokenizer.BasicLexer;
import eu.gricom.interpreter.basic.tokenizer.Token;
import eu.gricom.interpreter.basic.tokenizer.Tokenizer;
import eu.gricom.interpreter.basic.tokenizer.JasicLexer;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
    private static BufferedReader _oLineIn;
    private final Logger _oLogger = new Logger(this.getClass().getName());
    private static String _strBasicVersion = "basic";
    private LineNumberStatement _aoLineNumbers = new LineNumberStatement();

    /**
     * Constructs a new Basic instance. The instance stores the global state of
     * the interpreter such as the values of all of the variables and the
     * current statement.
     */
    public Basic() {
        InputStreamReader oConverter = new InputStreamReader(System.in);
        _oLineIn = new BufferedReader(oConverter);
    }

    /**
     * Interpret.
     * This is where the magic happens. This runs the code through the parsing pipeline to generate the AST. Then it
     * executes each statement. It keeps track of the current line in a member variable that the statement objects
     * have access to. This lets "goto" and "if then" do flow control by simply setting the index of the current statement.
     *
     * In an interpreter that didn't mix the interpretation logic in with the AST node classes, this would be doing a lot more work.
     *
     * TODO: Convert to stand-alone program
     *
     * @param strProgram A string containing the source code of a .bas script to interpret.
     */
    public final void interpret(final String strProgram) {
        ProgramPointer oProgramPointer = new ProgramPointer();
        List<Statement> aoStatements = null;

        // Tokenize. At the end of the tokenization I have the program transferred into a list of tokens and parameters
        _oLogger.info("Starting tokenization...");

        Tokenizer oTokenizer;

        if (_strBasicVersion.contains("jasic")) {
            oTokenizer = new JasicLexer();
        } else {
            oTokenizer = new BasicLexer();
        }

        List<Token> aoTokens = null;

        try {
            aoTokens = oTokenizer.tokenize(strProgram);

        } catch (SyntaxErrorException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        int iCounter = 0;
        for (Token oToken: aoTokens) {
            if (oToken.getType().toString().contains("LINE")) {
                _oLogger.debug("[" + oToken.getLine() + "] Token # <" + iCounter + ">: [" + oToken.getType().toString() + "]: []");
            } else {
                _oLogger.debug("[" + oToken.getLine() + "] Token # <" + iCounter + ">: [" + oToken.getType().toString() + "]: [" + oToken.getText() + "]");
            }
            iCounter++;
        }

        // Parse.
        _oLogger.info("Starting parsing...");
        try {
            Parser oParser;

            if (_strBasicVersion.contains("jasic")) {
                oParser = new JasicParser(aoTokens);
            } else {
                oParser = new BasicParser(aoTokens);
            }

            aoStatements = oParser.parse();
        } catch (SyntaxErrorException eSyntaxError) {
            eSyntaxError.printStackTrace();
        }

        // Run.
        _oLogger.info("Starting execution...");
        try {
            if (aoStatements != null) {
                while (oProgramPointer.getCurrentStatement() < aoStatements.size()) {
                    // as long as we have not reached the end of the code
                    int iThisStatement = oProgramPointer.getCurrentStatement();
                    int iSourceCodeLineNumber = _aoLineNumbers.getLineNumber(aoStatements.get(iThisStatement).getLineNumber());
                    oProgramPointer.calcNextStatement();

                    if (_strBasicVersion.contains("jasic")) {
                        _oLogger.debug(
                                "Current Source Code Line [" + iThisStatement + "/"+ aoStatements.size() + "]: " + aoStatements.get(
                                        iThisStatement).content());
                    } else {
                        _oLogger.debug(
                                "Basic Source Code Line [" + iSourceCodeLineNumber + "] Statement [ " + aoStatements.get(
                                        iThisStatement).getLineNumber() + "]: " + aoStatements.get(
                                        iThisStatement).content());
                    }

                    aoStatements.get(iThisStatement).execute();
                    iThisStatement = oProgramPointer.getCurrentStatement();

                    if (_strBasicVersion.contains("jasic")) {
                        _oLogger.debug(
                                "Next Source Code Line [" + iThisStatement + "/"+ aoStatements.size() + "]");
                    } else {
                        _oLogger.debug(
                                "Next Source Code Line [" + iSourceCodeLineNumber + "] Statement [ " + aoStatements.get(
                                        iThisStatement).getLineNumber() + "]: " + aoStatements.get(
                                        iThisStatement).content());
                    }
                }
            } else {
                _oLogger.error("Parsing delivered empty program");
            }
        } catch (Exception eException) {
            eException.printStackTrace();
        }
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
        oLogger.setLogLevel("");

        boolean bParseOK = true;
        String strParseError = null;
        CommandLine oCommandLine = null;

        // create Options object
        Options options = new Options();

        try {
            options.addOption("h", false, "help (This screen)");
            options.addOption("i", true, "define input file");
            options.addOption("q", false, "quiet mode");
            options.addOption("v", true, "verbose level: (0 - 3");
            options.addOption("b", true, "BASIC type (J = Jasic, B = Basic");

            CommandLineParser parser = new DefaultParser();
            oCommandLine = parser.parse(options, args);
        } catch (ParseException exParseException) {
            bParseOK = false;
            // TODO: This here makes no sense...
            strParseError = exParseException.getMessage();
        }

        if ((oCommandLine != null) && (!oCommandLine.hasOption("q"))) {

            Printer.println("GriCom Basic Interpreter Version 0.1.0");
            Printer.println("(c) Copyright Bob Nystrom 2010");
            Printer.println("(c) Copyright Andreas Grimm 2020");

            long lMaxMemory = Runtime.getRuntime().maxMemory();
            Printer.println("Maximum memory (bytes): " + lMaxMemory + ", ");
            Printer.println("Free memory (bytes): " + Runtime.getRuntime().freeMemory());
        }

        if ((oCommandLine != null) && (oCommandLine.hasOption("v"))) {
            String strLogLevel = oCommandLine.getOptionValue("v");
            String strLogLevelList = "trace|debug|info|warning";

            if (strLogLevelList.contains(strLogLevel.toLowerCase(Locale.ROOT))) {
                oLogger.setLogLevel(strLogLevel);
            }

            oLogger.debug("Log Level set:" + strLogLevel + "...");
        }

        if ((oCommandLine != null) && (oCommandLine.hasOption("h"))) {
            // automatically generate the help statement
            oLogger.debug("Display help message...");

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar BASIC-<build-name>.jar -i <filename.bas>", options);
        }

        if (!bParseOK) {
            oLogger.error("Unknown command line parameter...");
            System.exit(-1);
        }

        // Just show the usage and quit if a script wasn't provided.
        if ((oCommandLine != null) && (!oCommandLine.hasOption("i"))) {
            oLogger.error("Program file name missing...");
            Printer.println("");
            Printer.println("usage: java -jar BASIC-<build-name>.jar -i <filename.bas>");
            Printer.println("where <filename.bas> is a relative path to a .bas program to run.");
            System.exit(-1);
        }

        if ((oCommandLine != null) && (oCommandLine.hasOption("b"))) {
            // get the Basic Version
            oLogger.debug("Get BASIC version...");
            String strBasicVersion = oCommandLine.getOptionValue("b").toLowerCase(Locale.ROOT);
            String strBasicVersionList = "jasic|basic";

            if (strBasicVersionList.contains(strBasicVersion.toLowerCase(Locale.ROOT))) {
                _strBasicVersion = strBasicVersion.toLowerCase(Locale.ROOT);
            }

            oLogger.debug("Basic version set: " + _strBasicVersion + "...");
        }

        String strProgram = oCommandLine.getOptionValue("i");

        // Read the file.
        oLogger.info("Read file: " + strProgram + "...");
        String strContents = FileHandler.readFile(strProgram);

        // Run it.
        oLogger.info("Run the interpreter...");
        Basic oBasic = new Basic();
        oBasic.interpret(strContents);
    }
}
