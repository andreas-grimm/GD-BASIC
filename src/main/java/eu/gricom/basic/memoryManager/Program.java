package eu.gricom.basic.memoryManager;

import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.tokenizer.Token;
import eu.gricom.basic.statements.Statement;
import java.util.List;

/**
 * Program.java
 * <p>
 * Description: The Program class serves as the central storage container for a BASIC program throughout its lifecycle.
 * It holds the program source, tokenised representation, parsed statements, and maintains line number cross-references
 * for runtime navigation.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
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
     * Constructs a new Program instance. The instance stores the global state of
     * the program such as the values of all the variables and the
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
    public final void load(final String strProgramName, final String strProgram) {
        _oLogger.info("Loading program...");

        _strProgramSource = strProgram;
        _strProgramName = strProgramName;
    }

    /**
     * Get the Program Name.
     * Return the name of the program loaded.
     *
     * @return The name of the basic program.
     */
    public final String getProgramName() {
        return _strProgramName;
    }

    /**
     * Get Program.
     * This method returns the program source code.
     *
     * @return the basic program, containing the source code of a .bas script to interpret.
     */
    public final String getProgram() {
        return _strProgramSource;
    }


    /**
     * set Program.
     * This method is used to adopt the source code, e.g., due to the processing of macros.
     *
     * @param strProgram - the source code of the changed code.
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
     * get Tokens.
     * This method provides the list of tokens inside this object.
     *
     * @return list of token objects.
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
     * @return list of statement objects.
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
     * @return list of statement objects.
     */
    public final List<Statement> getPreRunStatements() {
        return _aoPreRunStatements;
    }

    /**
     * set Line Numbers.
     * Add the link to the line number object. Needed for unit testing.
     *
     * @param oLineNumbers object to reference the line number
     */
    public final void setLineNumber(LineNumberXRef oLineNumbers) {
        _oLineNumbers = oLineNumbers;
    }

    /**
     * equals.
     * Add the link to the line number object. Needed for unit testing.
     *
     * @param oCompare - Program object to compare to...
     */
    public final boolean equals(Program oCompare) {
        if (oCompare == null) {
            return false;
        }

        String          strProgramName            = oCompare.getProgramName();
        String          strProgramSource          = oCompare.getProgram();
        List<Statement> aoComparePreRunStatements = oCompare.getPreRunStatements();
        List<Statement> aoCompareStatements       = oCompare.getStatements();
        List<Token>     aoCompareTokens           = oCompare.getTokens();

        if (_strProgramName == null) {
            if (strProgramName != null) {
                return false;
            }
        } else if (!_strProgramName.equals(strProgramName)) {
            return false;
        }

        if (_strProgramSource == null) {
            if (strProgramSource != null) {
                return false;
            }
        } else if (!_strProgramSource.equals(strProgramSource)) {
            return false;
        }

        if (_aoPreRunStatements != null) {
            if (aoComparePreRunStatements == null) {
                return false;
            }
            for (Statement aoPreRunStatement : _aoPreRunStatements) {
                if (!aoComparePreRunStatements.contains(aoPreRunStatement)) {
                    return false;
                }
            }
        } else if (aoComparePreRunStatements != null) {
            return false;
        }

        if (_aoStatements != null) {
            if (aoCompareStatements == null) {
                return false;
            }
            for (Statement aoStatement : _aoStatements) {
                if (!aoCompareStatements.contains(aoStatement)) {
                    return false;
                }
            }
        } else if (aoCompareStatements != null) {
            return false;
        }

        if (_aoTokens != null) {
            if (aoCompareTokens == null) {
                return false;
            }
            for (Token oToken : _aoTokens) {
                if (!aoCompareTokens.contains(oToken)) {
                    return false;
                }
            }
        } else if (aoCompareTokens != null) {
            return false;
        }

        return true;
    }
}
