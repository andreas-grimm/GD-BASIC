package eu.gricom.basic.memoryManager;

import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.tokenizer.Token;
import eu.gricom.basic.statements.Statement;
import java.util.List;

/**
 * Program.java
 * <p>
 * Description:
 * <p>
 * This is the storage class for the Basic program to be executed. This is the main place of persistent information for
 * the different stages of the code: loaded, tokenized, and parsed. This allows the use of intermediate storage during
 * execution.
 * <p>
 * (c) = 2021,...,2025 by Andreas Grimm, Den Haag, The Netherlands
 */
public class Program {
    private final transient Logger _oLogger = new Logger(this.getClass().getName());
    private String _strProgramName;
    private String _strProgramSource;
    private LineNumberXRef _oLineNumbers = new LineNumberXRef();
    private List<Statement> _aoPreRunStatements;
    private List<Statement> _aoStatements = null;
    private List<Token> _aoTokens = null;


    /**
     * Initializes a new Program instance for managing a BASIC program's source code and its intermediate representations.
     */
    public Program() {
        _oLogger.info("Initializing program object...");
    }


    /**
     * Loads the BASIC program source and assigns its name.
     *
     * @param strProgramName The name to assign to the loaded program.
     * @param strProgram The source code of the BASIC program to load.
     */
    public final void load(final String strProgramName, final String strProgram) {
        _oLogger.info("Loading program...");

        _strProgramSource = strProgram;
        _strProgramName = strProgramName;
    }

    /**
     * Returns the name of the currently loaded BASIC program.
     *
     * @return the program name, or {@code null} if no program is loaded.
     */
    public final String getProgramName() {
        return _strProgramName;
    }

    /**
     * Returns the source code of the loaded BASIC program.
     *
     * @return the source code of the currently loaded BASIC program.
     */
    public final String getProgram() {
        return _strProgramSource;
    }


    /****
     * Updates the stored source code of the program.
     *
     * Use this method to replace the program's source code, for example after macro processing or other modifications.
     *
     * @param strProgram the new source code to store for the program
     */
    public final void setProgram(String strProgram) {
        _strProgramSource = strProgram;
    }


    /**
     * set Tokens.
     * This method takes a list of tokens, coming out of the tokenizer.
     *
     * @param aoTokens array of token objects, after the tokenization.
     */
    public final void setTokens(List<Token> aoTokens) {
        _aoTokens = aoTokens;
    }


    /**
     * Returns the list of tokens generated from the program's source code.
     *
     * @return the list of Token objects representing the tokenized program.
     */
    public final List<Token> getTokens() {
        return _aoTokens;
    }


    /**
     * set Statements.
     * This method takes a list of statements, coming out of the parser.
     *
     * @param aoStatements array of Token objects, after the tokenization.
     */
    public final void setStatements(List<Statement> aoStatements) {
        _aoStatements = aoStatements;
    }


    /**
     * Returns the list of parsed statements representing the BASIC program.
     *
     * @return the list of Statement objects for the loaded program.
     */
    public final List<Statement> getStatements() {
        return _aoStatements;
    }


    /**
     * set PreRunStatements.
     * This method takes a list of pre-run statements, coming out of the parser.
     *
     * @param aoPreRunStatements array of Token objects, after the tokenization.
     */
    public final void setPreRunStatements(List<Statement> aoPreRunStatements) {
        _aoPreRunStatements = aoPreRunStatements;
    }


    /**
     * Returns the list of statements to be executed before the main program run.
     *
     * @return the list of pre-run Statement objects, or null if not set.
     */
    public final List<Statement> getPreRunStatements() {
        return _aoPreRunStatements;
    }

    public final void setLineNumber(LineNumberXRef oLineNumbers) {
        _oLineNumbers = oLineNumbers;
    }
}
