package eu.gricom.interpreter.basic.memoryManager;

import eu.gricom.interpreter.basic.helper.Logger;
import eu.gricom.interpreter.basic.statements.Statement;
import eu.gricom.interpreter.basic.tokenizer.Token;
import java.util.List;

/**
 * Program.java
 *
 * Description:
 *
 * This is the storage class for the Basic program to be executed. This is the main place of persistent information for
 * the different stages of the code: loaded, tokenized, and parsed. This allows the use of intermediate storage during
 * execution.
 *
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class Program {
    private final transient Logger _oLogger = new Logger(this.getClass().getName());
    private String _strLoadedProgramSource;
    private String _strCurrentProgramSource;
    private LineNumberXRef _oLineNumbers = new LineNumberXRef();
    private List<Statement> _aoPreRunStatements;
    private List<Statement> _aoStatements = null;
    private List<Token> _aoTokens = null;


    /**
     * Constructs a new Program instance. The instance stores the global state of
     * the program such as the values of all of the variables and the
     * current statement.
     */
    public Program() {
        _oLogger.info("Initializing program object...");
    }


    /**
     * Load.
     * This is the entrance point for the program source.
     *
     * @param strProgram The basic program, containing the source code of a .bas script to interpret.
     */
    public final void load(final String strProgram) {
        _oLogger.info("Loading program...");

        _strLoadedProgramSource = strProgram;
        _strCurrentProgramSource = strProgram;
    }


    /**
     * get Program.
     * This method return the program source code.
     *
     * @return the basic program, containing the source code of a .bas script to interpret.
     */
    public final String getProgram() {
        return _strCurrentProgramSource;
    }


    /**
     * set Program.
     * This method is used to adopt the source code, e.g. due to the processing of macros.
     *
     * @param strProgram - the source code of the changed code.
     */
    public final void setProgram(String strProgram) {
        _strCurrentProgramSource = strProgram;
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
     * get Tokens.
     * This method provides the list of tokens inside this object.
     *
     * @return array of token objects.
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
     * get Statements.
     * This method provides the list of statements inside this object.
     *
     * @return array of statements objects.
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
     * get PreRunStatements.
     * This method provides the list of pre-run statements inside this object.
     *
     * @return array of statements objects.
     */
    public final List<Statement> getPreRunStatements() {
        return _aoPreRunStatements;
    }

    public final void setLineNumber(LineNumberXRef oLineNumbers) {
        _oLineNumbers = oLineNumbers;
    }
}
