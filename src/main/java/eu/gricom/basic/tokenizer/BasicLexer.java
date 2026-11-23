package eu.gricom.basic.tokenizer;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.helper.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * BasicLexer.java
 * <p>
 * Description: The BasicLexer class implements the Lexer interface for BASIC source code. It scans the input character
 * by character, recognising keywords, identifiers, numbers, strings, and operators, and produces a list of Token
 * objects for the parser to consume.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class BasicLexer implements Lexer {
    private final Logger _oLogger = new Logger(this.getClass().getName());

    @Override
    public final List<Token> tokenize(final String strSource) throws SyntaxErrorException {

        List<Token> voTokens = new ArrayList<>();

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
                voTokens.add(new Token("empty", BasicTokenType.LINE, iLineNumber));
            } else {
                // normalise the line: put spaces in places where needed, or remove them
                strProgramLine = Normalizer.normalize(strProgramLine);
                strProgramLine = Normalizer.normalizeFunction(strProgramLine);
                // put spaces around operators written without them (A-B becomes A - B, X=-A becomes X = - A)
                strProgramLine = OperatorSpacer.space(strProgramLine);

                // find reserved words: divide the string in an array of words
                String[] astrWords = strProgramLine.split("\\s");

                // and iterate over them
                for (int iWordIndex = 0; iWordIndex < astrWords.length; iWordIndex++) {
                    String strWord = astrWords[iWordIndex];

                    // eliminate all empty strings
                    if (strWord.length() <= 0) {
                        continue;
                    }

                    // this section verifies whether the next word is part of a string (as a string started but did not end yet)
                    // if a string started (bIsStringRunning == true) then the word is added to the string, if the word contains
                    // quotation marks ("), the string is closed.
                    if (bIsStringRunning) {
                        if (oToken == null) {
                            throw new SyntaxErrorException("Syntax Error: Unrecognised character sequence: " + iLineNumber
                                            + " " + strProgramLine);
                        }

                        // if the word ends with a ", then we stop any running string and remove the last character "
                        if (strWord.endsWith("\"")) {
                            strWord = strWord.substring(0, strWord.length() - 1);
                            bIsStringRunning = false;
                            voTokens.add(oToken);
                        }

                        // add the word to the string in the token
                        oToken.setText(oToken.getText() + " " + strWord);
                    } else {
                        // ok - we know this is not part of a string.

                        // compare the word with the list of reserved words
                        int iIndex = ReservedWords.getIndex(strWord);

                        // String functions have a $ at the end (STR$, CHR$, LEFT$ ...). If the full word is not a
                        // reserved word, the name without the $ is looked up, so that "STR$(" is the STR function
                        // and not an array. The token keeps the text with the $ (bug fixed in Phase 7).
                        // Only when the word is followed by "(" (a function call). "str$ = ..." stays a variable name.
                        boolean bFunctionCall = java.util.regex.Pattern
                                .compile(java.util.regex.Pattern.quote(strWord) + "\\s*\\(")
                                .matcher(strProgramLine).find();
                        if (iIndex == -1 && bFunctionCall && strWord.endsWith("$") && strWord.length() > 1) {
                            iIndex = ReservedWords.getIndex(strWord.substring(0, strWord.length() - 1));
                        }

                        if (iIndex != -1) {
                            // we found a reserved word...
                            BasicTokenType eTokenType = ReservedWords.getTokenType(iIndex);

                            // this block handles all comments
                            if (eTokenType == BasicTokenType.REM
                                    || eTokenType == BasicTokenType.COMMENT
                                    || eTokenType == BasicTokenType.DIM)  {
                                voTokens.add(new Token(strProgramLine, eTokenType, iLineNumber));

                                break;
                            }

                            oToken = new Token(strWord, eTokenType, iLineNumber);

                        // a "!" on its own is a comment marker (decision A6 and A6b):
                        // at the start of a line it is a comment; after a colon it gives a warning and is a comment.
                        } else if (strWord.equals("!")) {
                            Token oLastToken = voTokens.isEmpty() ? null : voTokens.get(voTokens.size() - 1);
                            boolean bFirstOnLine = oLastToken == null || oLastToken.getLine() != iLineNumber;

                            if (bFirstOnLine) {
                                voTokens.add(new Token(strProgramLine, BasicTokenType.REM, iLineNumber));
                                break;
                            }
                            if (oLastToken.getType() == BasicTokenType.COLON) {
                                _oLogger.warning("Comment after colon: use REM in place of !");
                                voTokens.add(new Token(strProgramLine, BasicTokenType.REM, iLineNumber));
                                break;
                            }
                            throw new SyntaxErrorException("Syntax Error: '!' starts a comment only at the start of a"
                                    + " line or after ':' in Line [" + iLineNumber + "]");

                        // a "!" inside an expression (not "!=", and not a name suffix) is a syntax error (decision Q2)
                        } else if (strWord.startsWith("!") && !strWord.startsWith("!=")) {
                            throw new SyntaxErrorException("Syntax Error: '!' is not valid inside an expression: "
                                    + strWord + " in Line [" + iLineNumber + "]");

                        // a decimal suffix "#" directly after a number is not valid (decision A8)
                        } else if (strWord.matches("[0-9][0-9.]*#")) {
                            throw new SyntaxErrorException("Syntax Error: the decimal suffix '#' cannot follow a number: "
                                    + strWord + " in Line [" + iLineNumber + "]");

                        // ok - this is not reserved word - so maybe it is a number?
                        } else if (isNumber(strWord)) {
                            oToken = new Token(strWord, BasicTokenType.NUMBER, iLineNumber);

                        // now check whether the word is marked as the beginning of a String
                        } else if (isString(strWord)) {
                            strWord = strWord.substring(1); // remove the "

                            // this section handles single word strings
                            if (strWord.endsWith("\"")) {
                                strWord = strWord.substring(0, strWord.length() - 1);
                                bIsStringRunning = false;
                                oToken = new Token(strWord, BasicTokenType.STRING, iLineNumber);
                                voTokens.add(oToken);
                            } else {
                                oToken = new Token(strWord, BasicTokenType.STRING, iLineNumber);
                                bIsStringRunning = true;
                            }

                        // now check whether the word is marked as a boolean
                        } else if (isBoolean(strWord)) {
                            oToken = new Token(strWord, BasicTokenType.BOOLEAN, iLineNumber);

                        } else {
                            // as it is neither a number, string, or boolean - it has to be a variable / constant...
                            BasicTokenType eTokenType = BasicTokenType.WORD;

                            oToken = new Token(strWord, eTokenType, iLineNumber);
                        }

                        if (oToken.getType() != BasicTokenType.STRING) { // Strings are added after they are completed.
                            voTokens.add(oToken);
                        }
                    }
                }
            }
        }

        for (Token oToken: voTokens) {
            _oLogger.debug(" [" + oToken.getLine() + "]  [" + oToken.getType().toString() + "] " + oToken.getText());
        }

        return voTokens;
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
