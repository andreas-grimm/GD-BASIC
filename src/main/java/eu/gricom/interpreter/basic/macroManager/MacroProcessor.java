package eu.gricom.interpreter.basic.macroManager;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.helper.Logger;
import eu.gricom.interpreter.basic.tokenizer.BasicLexer;
import eu.gricom.interpreter.basic.tokenizer.BasicTokenType;
import eu.gricom.interpreter.basic.tokenizer.Lexer;
import eu.gricom.interpreter.basic.tokenizer.Token;
import java.util.List;
import java.util.Vector;

public class MacroProcessor {

    private final transient Logger _oLogger = new Logger(this.getClass().getName());
    private final Lexer _oTokenizer = new BasicLexer();


    /**
     * Constructs a new Program instance. The instance stores the global state of
     * the program such as the values of all of the variables and the
     * current statement.
     */
    public MacroProcessor() {
        _oLogger.info("Processing Macros into the program code...");
    }

    /**
     * Process Function.
     * This function converts the macros into code that is added to the code.
     *
     * @param strProgram - the program to be processed to find and replace macros.
     * @return - the generated code after the macros have benn processed
     */
    public String process (String strProgram) throws SyntaxErrorException {
        // Step 1: Tokenize the source code
        List<Token> aoToken = tokenize(strProgram);
        Vector<String> vstrParameter = new Vector<>();
        MacroList oMarcoList = new MacroList();
        String strConvertedProgram = new String();

        // Step 2: Build the list of all found macros
        int iCounter = 0;

        while (iCounter < aoToken.size()) {
            Token oToken  = aoToken.get(iCounter);

            if (oToken.getType().toString().contains("DEF")) {
                _oLogger.debug("[" + oToken.getLine() + "] Token # <" + iCounter + ">: [" + oToken.getType() + "]: ["
                                       + oToken.getText() + "]");

                String strName = aoToken.get(iCounter + 1).getText();

                int iAdd = 3;
                while (aoToken.get(iCounter + iAdd).getType() != BasicTokenType.RIGHT_PAREN) {
                    if (aoToken.get(iCounter + iAdd).getType() == BasicTokenType.WORD) {
                        vstrParameter.add(aoToken.get(iCounter + iAdd).getText());
                        _oLogger.debug("[" + oToken.getLine() + "] Token # <" + iCounter + ">: ["
                            + oToken.getType() + "]: Parameter: [" + aoToken.get(iCounter + iAdd).getText() + "]");
                    }
                    iAdd++;
                }

                String strFunction = aoToken.get(getFunction(aoToken, iCounter)).getText();

                _oLogger.debug("[" + oToken.getLine() + "] Token # <" + iCounter + ">: ["
                                       + oToken.getType() + "] Function: <" + strFunction + ">");

                oMarcoList.add(strName, vstrParameter, strFunction);
                vstrParameter.clear();
            } else {
                _oLogger.debug("[" + oToken.getLine() + "] Token # <" + iCounter + ">: [" + oToken.getType() + "]: []");
            }
            iCounter++;
        }

        oMarcoList.print();

        // Step 3: Run thru the source code again and find macro usage to replace it...
        // Convert the program into a list of lines
        String[] astrProgramLines = strProgram.split("\\s*\n\\s*");

        for (String strProgramLine: astrProgramLines) {
            String strStatus;

            if (strProgramLine.contains(" DEF ")) {
                strConvertedProgram += strProgramLine + '\n';
                strStatus = "ignored: [DEF] found";
            } else
            if (oMarcoList.containsMacro(strProgramLine) == null) {
                strConvertedProgram += strProgramLine + '\n';
                strStatus = "ignored: no macro found";
            } else {
                String strConvertedLine = convertLine(strProgramLine);
                strConvertedProgram += strConvertedLine + '\n';
                strStatus = "code changed: [" + strConvertedLine + "]";
            }

            _oLogger.debug(" Original line: [" + strProgramLine + "] " + strStatus);
        }

        return strConvertedProgram;
    }

    private int getFunction(List<Token> aoToken, int iStartCounter) throws SyntaxErrorException {
        int iCounter = iStartCounter;

        while (iCounter < aoToken.size()) {
            if (aoToken.get(iCounter).getType() == BasicTokenType.STRING) {
                return iCounter;
            }
            iCounter++;
        }

        throw new SyntaxErrorException("Macro body not found");
    }

    private String convertLine(final String strOriginal) {
        String strWork = strOriginal;
        String strChanged = strOriginal;
        MacroList oMarcoList = new MacroList();

        String strFoundMacro = oMarcoList.containsMacro(strWork);

        while (strFoundMacro != null) {
            strChanged = strWork.substring(0, strWork.indexOf(strFoundMacro));

            String strRest = strWork.substring(strWork.indexOf(strFoundMacro));
            String strGetParameters = strRest.substring(strRest.indexOf("(") + 1, strRest.indexOf(")"));
            String strGetMacroName = strRest.substring(0, strRest.indexOf("("));

            strChanged += oMarcoList.getFunction(strGetParameters, strGetMacroName) + strRest.substring(strRest.indexOf(")") + 1);

            strWork = strChanged;
            strFoundMacro = oMarcoList.containsMacro(strChanged);
        }

        return strChanged;
    }

    private List<Token> tokenize(String strProgram) throws SyntaxErrorException {
        return _oTokenizer.tokenize(strProgram);
    }
}
