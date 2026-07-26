package eu.gricom.basic.memoryManager;

import eu.gricom.basic.error.EmptyProgramException;
import eu.gricom.basic.error.FileAlreadyExistsException;
import eu.gricom.basic.error.FileNotFoundException;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.helper.FileHandler;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.macroManager.MacroProcessor;
import eu.gricom.basic.tokenizer.BasicLexer;
import eu.gricom.basic.tokenizer.Lexer;
import eu.gricom.basic.tokenizer.Token;
import eu.gricom.basic.statements.Statement;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
     * Load program from file.
     *
     * This method reads a BASIC program from a file on the file system. The file must exist
     * and must not be empty. The file content is loaded into the program source.
     *
     * @param strFileName The path to the file containing the BASIC program
     * @throws FileNotFoundException if the file does not exist
     * @throws EmptyProgramException if the file exists but is empty
     */
    public final void loadProgram(final String strFileName) throws FileNotFoundException, EmptyProgramException, SyntaxErrorException {
        _oLogger.info("Loading program from file: " + strFileName);

        File oFile = new File(strFileName);

        if (!oFile.exists()) {
            throw new FileNotFoundException("File not found: " + strFileName);
        }

        String strProgramContent = FileHandler.readFile(strFileName);

        if (strProgramContent == null || strProgramContent.trim().isEmpty()) {
            throw new EmptyProgramException("Program file is empty: " + strFileName);
        }

        _strProgramSource = strProgramContent;
        _strProgramName = strFileName;

        _oLogger.info("Program loaded successfully from " + strFileName);

        reprocessSourceCode();
    }

    /**
     * Save program to file.
     *
     * This method writes the current BASIC program source code to a file on the file system.
     * The file must not already exist to prevent accidental overwriting of existing files.
     *
     * @param strFileName The path to the file where the BASIC program will be saved
     * @throws FileAlreadyExistsException if the file already exists or if writing fails
     */
    public final void save(final String strFileName) throws FileAlreadyExistsException {
        _oLogger.info("Saving program to file: " + strFileName);

        File oFile = new File(strFileName);

        if (oFile.exists()) {
            throw new FileAlreadyExistsException("File already exists: " + strFileName);
        }

        try (FileWriter oWriter = new FileWriter(oFile)) {
            oWriter.write(_strProgramSource);
            _oLogger.info("Program saved successfully to " + strFileName);
        } catch (IOException e) {
            _oLogger.error("Failed to save program: " + e.getMessage());
            throw new FileAlreadyExistsException("Error saving file: " + e.getMessage());
        }
    }

    /**
     * Reprocess the program source code.
     *
     * Internal helper method that processes macros and re-tokenizes the current program source.
     * This method is called after any modification to _strProgramSource to ensure the tokens
     * remain in sync with the source code.
     *
     * @throws SyntaxErrorException if macro processing or tokenization fails
     */
    private void reprocessSourceCode() throws SyntaxErrorException {
        MacroProcessor oMacroProcessor = new MacroProcessor();
        setProgram(oMacroProcessor.process(getProgram()));

        _oLogger.info("Starting tokenization...");

        Lexer oTokenizer = new BasicLexer();
        setTokens(oTokenizer.tokenize(getProgram()));

        int iCounter = 0;
        for (Token oToken: getTokens()) {
            if (oToken.getType().toString().contains("LINE")) {
                _oLogger.debug("[" + oToken.getLine() + "] Token # <" + iCounter + ">: [" + oToken.getType() + "]: []");
            } else {
                _oLogger.debug("[" + oToken.getLine() + "] Token # <" + iCounter + ">: [" + oToken.getType() + "]: ["
                        + oToken.getText() + "]");
            }
            iCounter++;
        }
    }

    /**
     * Check if program has content.
     *
     * @return true if the program contains at least one statement line, false if empty
     */
    public final boolean hasContent() {
        if (_strProgramSource == null || _strProgramSource.trim().isEmpty()) {
            return false;
        }
        String[] astrLines = _strProgramSource.split("\n");
        for (String strLine : astrLines) {
            if (!strLine.trim().isEmpty()) {
                return true;
            }
        }
        return false;
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

    /**
     * Add or replace a BASIC program line by line number.
     *
     * This method manages the program source code by parsing existing lines, replacing or adding
     * a new line with the specified line number, and sorting all lines numerically by line number.
     * The input line should include the line number followed by a space and the program body
     * (e.g., "100 PRINT \"HELLO\""). Only the body portion is stored; the line number is extracted
     * and used as the key for sorting.
     *
     * After updating the program source, the method re-processes macros and re-tokenizes the program
     * to ensure consistency with the updated source code.
     *
     * @param iLineNumber The numeric line number to add or replace
     * @param strProgramLine The complete program line including line number and body
     *                        (format: "lineNumber programBody")
     * @throws SyntaxErrorException if macro processing or tokenization fails
     */
    public void addOrReplace(int iLineNumber, String strProgramLine) throws SyntaxErrorException {
        Map<Integer, String> mProgramLines = new TreeMap<>();

        // Parse existing program source into individual lines
        String[] astrLines = _strProgramSource.split("\n");

        // Extract line number and body from each existing line
        for (String strLine : astrLines) {
            if (strLine.isEmpty()) {
                continue;
            }
            String[] astrParts = strLine.split(" ", 2);
            if (astrParts.length >= 1) {
                try {
                    int iCurrentLineNumber = Integer.parseInt(astrParts[0].trim());
                    String strLineBody = astrParts.length > 1 ? astrParts[1] : "";
                    mProgramLines.put(iCurrentLineNumber, strLineBody);
                } catch (NumberFormatException e) {
                    _oLogger.warning("Skipping line with invalid line number: " + strLine);
                }
            }
        }

        // Extract line number and body from the input line, then add or replace
        String[] astrNewLineParts = strProgramLine.split(" ", 2);
        String strLineBody = astrNewLineParts.length > 1 ? astrNewLineParts[1] : "";
        mProgramLines.put(iLineNumber, strLineBody);

        // Reconstruct program source with sorted lines (TreeMap maintains order by key)
        StringBuilder oSB = new StringBuilder();
        for (Map.Entry<Integer, String> oEntry : mProgramLines.entrySet()) {
            oSB.append(oEntry.getKey()).append(" ").append(oEntry.getValue()).append("\n");
        }

        // Remove trailing newline
        if (oSB.length() > 0) {
            oSB.setLength(oSB.length() - 1);
        }

        // Update program source with sorted content
        _strProgramSource = oSB.toString();

        reprocessSourceCode();
    }

    /**
     * Delete a range of BASIC program lines by line number.
     *
     * This method removes all lines from the program source where the line number falls
     * within the specified range (inclusive). After deletion, the program source is updated
     * and the remaining lines are preserved in their original order.
     *
     * @param iBegin The starting line number to delete (inclusive)
     * @param iEnd The ending line number to delete (inclusive)
     */
    public void deleteLines(int iBegin, int iEnd) throws SyntaxErrorException {
        Map<Integer, String> mProgramLines = new TreeMap<>();

        // Parse existing program source into individual lines
        String[] astrLines = _strProgramSource.split("\n");

        // Extract line number and body from each existing line
        for (String strLine : astrLines) {
            if (strLine.isEmpty()) {
                continue;
            }
            String[] astrParts = strLine.split(" ", 2);
            if (astrParts.length >= 1) {
                try {
                    int iCurrentLineNumber = Integer.parseInt(astrParts[0].trim());
                    String strLineBody = astrParts.length > 1 ? astrParts[1] : "";

                    // Skip lines that fall within the deletion range (inclusive)
                    if (iCurrentLineNumber < iBegin || iCurrentLineNumber > iEnd) {
                        mProgramLines.put(iCurrentLineNumber, strLineBody);
                    }
                } catch (NumberFormatException e) {
                    _oLogger.warning("Skipping line with invalid line number: " + strLine);
                }
            }
        }

        // Reconstruct program source with remaining lines (sorted by line number)
        StringBuilder oSB = new StringBuilder();
        for (Map.Entry<Integer, String> oEntry : mProgramLines.entrySet()) {
            oSB.append(oEntry.getKey()).append(" ").append(oEntry.getValue()).append("\n");
        }

        // Remove trailing newline
        if (oSB.length() > 0) {
            oSB.setLength(oSB.length() - 1);
        }

        // Update program source with remaining content
        _strProgramSource = oSB.toString();

        _oLogger.info("Deleted lines from " + iBegin + " to " + iEnd);

        reprocessSourceCode();
    }
}
