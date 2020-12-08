package eu.gricom.interpreter.basic;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.helper.FileHandler;
import eu.gricom.interpreter.basic.helper.Logger;
import eu.gricom.interpreter.basic.helper.MemoryManagement;
import eu.gricom.interpreter.basic.helper.Printer;
import eu.gricom.interpreter.basic.statements.Statement;
import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

public class Basic {
    public static BufferedReader _oLineIn;
    private Logger _oLogger = new Logger(this.getClass().getName());

    /**
     * Constructs a new Basic instance. The instance stores the global state of
     * the interpreter such as the values of all of the variables and the
     * current statement.
     */
    public Basic() {
        InputStreamReader _oConverter = new InputStreamReader(System.in);
        _oLineIn = new BufferedReader(_oConverter);
    }

    /**
     * This is where the magic happens. This runs the code through the parsing pipeline to generate the AST. Then it
     * executes each statement. It keeps track of the current line in a member variable that the statement objects
     * have access to. This lets "goto" and "if then" do flow control by simply setting the index of the current statement.
     *
     * In an interpreter that didn't mix the interpretation logic in with the AST node classes, this would be doing a lot more work.
     *
     * @param strProgram A string containing the source code of a .bas script to interpret.
     */
    public void interpret(String strProgram) {
        MemoryManagement oMemoryManagement = new MemoryManagement();
        List<Statement> aoStatements = null;

        // Tokenize. At the end of the tokenization I have the program transferred into a list of tokens and parameters
        _oLogger.info("Starting tokenization...");
        List<Token> aoTokens = Tokenizer.tokenize(strProgram);
        int iCounter = 0;
        for (Token oToken: aoTokens) {
            if (oToken.getType().toString().contains("LINE")) {
                _oLogger.debug("Token # <" + iCounter + ">: [" + oToken.getType().toString() + "]: []");
            } else {
                _oLogger.debug("Token # <" + iCounter + ">: [" + oToken.getType().toString() + "]: [" + oToken.getText() + "]");
            }
            iCounter++;
        }

        // Parse.
        _oLogger.info("Starting parsing...");
        try {
            BasicParser oParser = new BasicParser(aoTokens);
            aoStatements = oParser.parse();
        } catch (SyntaxErrorException eSyntaxError) {
            eSyntaxError.printStackTrace();
        }

        _oLogger.info("Starting execution...");
        // Interpret until we're done.
        try {
            if (aoStatements != null) {
                while (oMemoryManagement.getCurrentStatement() < aoStatements.size()) {
                    // as long as we have not reached the end of the code
                    int thisStatement = oMemoryManagement.getCurrentStatement();
                    oMemoryManagement.nextStatement();
                    _oLogger.debug("Line [" + thisStatement + "]: " + aoStatements.get(thisStatement).content());
                    aoStatements.get(thisStatement).execute();
                }
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
    public static void main(String[] args) {
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

            CommandLineParser parser = new DefaultParser();
            oCommandLine = parser.parse(options, args);
        } catch (ParseException exParseException) {
            bParseOK = false;
            strParseError = exParseException.getMessage();
        }

        if ((oCommandLine != null) && (!oCommandLine.hasOption("q"))) {

            Printer.println("GriCom Basic Interpreter Version 0.1.0");
            Printer.println("(c) Copyright Bob Nystrom 2010");
            Printer.println("(c) Copyright Andreas Grimm 2020");

            long lMaxMemory = Runtime.getRuntime().maxMemory();
            Printer.println("Maximum memory (bytes): " + (lMaxMemory == Long.MAX_VALUE ? "no limit" : lMaxMemory) + ", ");
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
            formatter.printHelp( "java -jar BASIC-<build-name>.jar -i <filename.bas>", options );
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
