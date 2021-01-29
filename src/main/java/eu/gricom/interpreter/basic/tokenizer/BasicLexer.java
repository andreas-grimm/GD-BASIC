package eu.gricom.interpreter.basic.tokenizer;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.helper.Logger;

import java.util.ArrayList;
import java.util.Arrays;
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
public class BasicLexer implements Tokenizer {
    private final Logger _oLogger = new Logger(this.getClass().getName());

    @Override
    public List<Token> tokenize(String strSource) throws SyntaxErrorException {

        List<Token> aoTokens = new ArrayList<>();

        // Convert the program into a list of lines
        List<String> astrProgramLines = Arrays.asList(strSource.split("\\s*\n\\s*"));

        boolean bIsStringRunning = false;

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
                iLineNumber = Integer.parseInt(
                        strProgramLine.substring(0,
                                strProgramLine.indexOf(" ")));

                strProgramLine = strProgramLine.substring(strProgramLine.indexOf(" ") + 1);
            }

            // here we handle all empty lines, e.g. lines that only contain the line number
            if (strProgramLine.length() < 1) {
                aoTokens.add(new Token("empty", TokenType.LINE, iLineNumber));
            } else {

                // now remove all tabulators and replace them with 4 spaces
                strProgramLine = strProgramLine.replace("\t", "    ");

                // find reserved words
                List<String> astrWords = Arrays.asList(strProgramLine.split("\\s"));

                for (String strWord : astrWords) {
                    // this section verifies whether the next word is part of a string (as a string started but did not end yet)
                    // if a string started (bIsStringRunning == true) then the word is added to the string, if the word contains
                    // quotation marks ("), the string is closed.
                    if (bIsStringRunning) {
                        if (oToken == null) {
                            throw (new SyntaxErrorException(
                                    "Syntax Error: Unrecognized character sequence: " + iLineNumber +
                                            " " + strProgramLine));
                        }

                        if (strWord.endsWith("\"")) {
                            strWord = strWord.substring(0, strWord.length() - 1);
                            bIsStringRunning = false;
                            aoTokens.add(oToken);
                        }

                        oToken.setText(oToken.getText() + " " + strWord);
                    } else {
                        int iIndex = ReservedWords.getIndex(strWord);

                        if (iIndex != -1) {
                            // we found a reserved word...
                            TokenType oTokenType = ReservedWords.getTokenType(iIndex);

                            if ((oTokenType == TokenType.REM) ||
                                    (oTokenType == TokenType.COMMENT))  {
                                aoTokens.add(new Token(strProgramLine, oTokenType, iLineNumber));

                                break;
                            }

                            oToken = new Token(strWord, oTokenType, iLineNumber);

                        } else if (isNumber(strWord)) {
                            // ok - this is not reserved word - so maybe it is a number?
                            oToken = new Token(strWord, TokenType.NUMBER, iLineNumber);

                        } else if (isString(strWord)) {
                            // now check whether the word is marked as the beginning of a String
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

                        } else if (isBoolean(strWord)) {
                            // now check whether it is a boolean
                            oToken = new Token(strWord, TokenType.BOOLEAN, iLineNumber);

                        } else {
                            // as it is neither a number, string, or boolean - it has to be a varible / constant...
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

        return (aoTokens);
    }

    private boolean isBoolean(String strWord) {
        if (strWord.toUpperCase(Locale.ROOT).matches("TRUE") || strWord.toUpperCase(Locale.ROOT).matches("FALSE")) {
            return (true);
        }

        return (false);
    }

    private boolean isString(String strWord) {
        if (strWord.startsWith("\"")) {
            return (true);
        }

        return (false);
    }

    private boolean isNumber(String strWord) {
        Pattern oPattern = Pattern.compile("-?\\d*(\\.\\d+)?");

        if (strWord == null) {
            return (false);
        }
        return (oPattern.matcher(strWord).matches());
    }
}
