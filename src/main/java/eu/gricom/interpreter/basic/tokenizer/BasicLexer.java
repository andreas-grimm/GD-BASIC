package eu.gricom.interpreter.basic.tokenizer;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.helper.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * BasicLexer.java
 * <p>
 * Description:
 * <p>
 * The BasicLexer class converts the source code into a list of tokens. The read a source code file and move the content
 * into a single string. This might cause problems in case of really big programs, so this needs to be addressed later.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class BasicLexer implements Lexer {
    private final Logger _oLogger = new Logger(this.getClass().getName());

    @Override
    public final List<Token> tokenize(final String strSource) throws SyntaxErrorException {

        List<Token> aoTokens = new ArrayList<>();

        // Convert the program into a list of lines
        String[] astrProgramLines = strSource.split("\\s*\n\\s*");

        boolean bIsStringRunning = false;
        int iLastLineNumber = -1;

        for (String strProgramLine: astrProgramLines) {
            Token oToken = null;

            // split the line number from the program line... make sure that the numbers are in ascending order...
            int iLineNumber;

            if (strProgramLine.length() <= 0) {
                // line is empty - we continue
                continue;
            } else if (strProgramLine.indexOf(" ") < 0)  {
                // line only contains line number
                iLineNumber = Integer.parseInt(strProgramLine);
                strProgramLine = "";
            } else {
                // isolate line number and the program line
                iLineNumber = Integer.parseInt(
                        strProgramLine.substring(0,
                                strProgramLine.indexOf(" ")));

                strProgramLine = strProgramLine.substring(strProgramLine.indexOf(" ") + 1);
            }

            // check whether the line number is smaller or equal to the previous number. If so - throw an syntax error
            if (iLineNumber <= iLastLineNumber) {
                throw new SyntaxErrorException("Line Number sequence not correct...:" + iLastLineNumber + " to " + iLineNumber);
            } else {
                iLastLineNumber = iLineNumber;
            }

            // here we handle all empty lines, e.g. lines that only contain the line number
            if (strProgramLine.length() < 1) {
                aoTokens.add(new Token("empty", TokenType.LINE, iLineNumber));
            } else {
                // normalize the line: put spaces in places where needed, or remove them
                strProgramLine = Normalizer.normalize(strProgramLine);
                strProgramLine = Normalizer.normalizeFunction(strProgramLine);

                // find reserved words: divide the string in an array of words
                String[] astrWords = strProgramLine.split("\\s");

                // and iterate over them
                for (String strWord : astrWords) {

                    // eliminate all empty strings
                    if (strWord.length() <= 0) {
                        continue;
                    }

                    // this section verifies whether the next word is part of a string (as a string started but did not end yet)
                    // if a string started (bIsStringRunning == true) then the word is added to the string, if the word contains
                    // quotation marks ("), the string is closed.
                    if (bIsStringRunning) {
                        if (oToken == null) {
                            throw new SyntaxErrorException("Syntax Error: Unrecognized character sequence: " + iLineNumber
                                            + " " + strProgramLine);
                        }

                        // if the word ends with a ", then we stop any running string and remove the last character "
                        if (strWord.endsWith("\"")) {
                            strWord = strWord.substring(0, strWord.length() - 1);
                            bIsStringRunning = false;
                            aoTokens.add(oToken);
                        }

                        // add the word to the string in the token
                        oToken.setText(oToken.getText() + " " + strWord);
                    } else {
                        // ok - we know this is not part of a string.

                        // compare the word with the list of reserved words
                        int iIndex = ReservedWords.getIndex(strWord);

                        if (iIndex != -1) {
                            // we found a reserved word...
                            TokenType oTokenType = ReservedWords.getTokenType(iIndex);

                            // this block handles all comments
                            if (oTokenType == TokenType.REM
                                    || oTokenType == TokenType.COMMENT)  {
                                aoTokens.add(new Token(strProgramLine, oTokenType, iLineNumber));

                                break;
                            }

                            oToken = new Token(strWord, oTokenType, iLineNumber);

                        // ok - this is not reserved word - so maybe it is a number?
                        } else if (isNumber(strWord)) {
                            oToken = new Token(strWord, TokenType.NUMBER, iLineNumber);

                        // now check whether the word is marked as the beginning of a String
                        } else if (isString(strWord)) {
                            strWord = strWord.substring(1); // remove the "

                            // this section handles single word strings
                            if (strWord.endsWith("\"")) {
                                strWord = strWord.substring(0, strWord.length() - 1);
                                bIsStringRunning = false;
                                oToken = new Token(strWord, TokenType.STRING, iLineNumber);
                                aoTokens.add(oToken);
                            } else {
                                oToken = new Token(strWord, TokenType.STRING, iLineNumber);
                                bIsStringRunning = true;
                            }

                        // now check whether the word is marked as a boolean
                        } else if (isBoolean(strWord)) {
                            oToken = new Token(strWord, TokenType.BOOLEAN, iLineNumber);

                        } else {
                            // as it is neither a number, string, or boolean - it has to be a variable / constant...
                            TokenType oTokenType = TokenType.WORD;

                            oToken = new Token(strWord, oTokenType, iLineNumber);
                        }

                        if (oToken.getType() != TokenType.STRING) { // Strings are added after they are completed.
                            aoTokens.add(oToken);
                        }
                    }
                }
            }
        }

        for (Token oToken: aoTokens) {
            _oLogger.debug(" [" + oToken.getLine() + "]  [" + oToken.getType().toString() + "] " + oToken.getText());
        }

        return aoTokens;
    }

    /**
     * isBoolean identifies a boolean in the BASIC program by the keywords "TRUE" and "FALSE".
     *
     * @param strWord argument for the check
     * @return true if argument is a boolean
     */
    private boolean isBoolean(final String strWord) {
        return strWord.toUpperCase(Locale.ROOT).matches("TRUE")
                || strWord.toUpperCase(Locale.ROOT).matches("FALSE");
    }

    /**
     * isString identifies a string in the BASIC program by the trailing quotation marks (").
     *
     * @param strWord argument for the check
     * @return true if argument is a string
     */
    private boolean isString(final String strWord) {
        return strWord.startsWith("\"");
    }

    /**
     * isNumber uses a regular expression to verify that the argument string contains a number.
     *
     * @param strWord argument for the check
     * @return true if argument is a number
     */
    private boolean isNumber(final String strWord) {
        Pattern oPattern = Pattern.compile("-?\\d*(\\.\\d+)?");

        if (strWord == null) {
            return false;
        }
        return oPattern.matcher(strWord).matches();
    }
}
