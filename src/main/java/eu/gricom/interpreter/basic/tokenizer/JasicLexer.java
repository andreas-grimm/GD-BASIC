package eu.gricom.interpreter.basic.tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * JasicLexer.java
 * <p>
 * Description:
 * <p>
 * The Lexer class converts the source code into a list of tokens. Question here is whether we should convert
 * the Lexer class not into a Token factory class. The read a source code file and move the content into a single
 * string. This might cause problems in case of really big programs, so this needs to be addressed later.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public final class JasicLexer implements Lexer {
    private int _iLineNumber = 1;

    /**
     * Default constructor, private as due to a utility class.
     */
    public JasicLexer() {
    }

    @Override
    public List<Token> tokenize(final String strSource) {
        List<Token> aoTokens = new ArrayList<>();

        String strToken = "";
        JasicTokenType oState = JasicTokenType.DEFAULT; //The token has not been identified - so the token type is default

        // Many tokens are a single character, like operators and ().
        String charTokens = "\n=+-*/<>()";
        BasicTokenType[] eTokenTypes = {
                BasicTokenType.LINE,
                BasicTokenType.EQUALS,
                BasicTokenType.OPERATOR,
                BasicTokenType.OPERATOR,
                BasicTokenType.OPERATOR,
                BasicTokenType.OPERATOR,
                BasicTokenType.OPERATOR,
                BasicTokenType.OPERATOR,
                BasicTokenType.LEFT_PAREN,
                BasicTokenType.RIGHT_PAREN};

        // Scan through the code one character at a time, building up the list of tokens.
        for (int i = 0; i < strSource.length(); i++) {
            char c = strSource.charAt(i);
            switch (oState) {
                case DEFAULT:
                    if (charTokens.indexOf(c) != -1) {
                        aoTokens.add(new Token(Character.toString(c), eTokenTypes[charTokens.indexOf(c)], _iLineNumber));
                        if (eTokenTypes[charTokens.indexOf(c)] == BasicTokenType.LINE) {
                            // if the found character is a 'new line' then increase the line number.
                            _iLineNumber++;
                        }
                    } else if (Character.isLetter(c)) {
                        strToken += String.valueOf(c);
                        oState = JasicTokenType.WORD;
                    } else if (Character.isDigit(c)) {
                        strToken += String.valueOf(c);
                        oState = JasicTokenType.NUMBER;
                    } else if (c == '"') {
                        oState = JasicTokenType.STRING;
                    } else if (c == '\'') {
                        oState = JasicTokenType.COMMENT;
                    }
                    break;

                case WORD:
                    if (Character.isLetterOrDigit(c)) {
                        strToken += String.valueOf(c);
                    } else if (c == ':') {
                        aoTokens.add(new Token(strToken, BasicTokenType.LABEL, _iLineNumber));
                        strToken = "";
                        oState = JasicTokenType.DEFAULT;
                    } else {
                        aoTokens.add(new Token(strToken, BasicTokenType.WORD, _iLineNumber));
                        strToken = "";
                        oState = JasicTokenType.DEFAULT;
                        i--; // Reprocess this character in the default state.
                    }
                    break;

                case NUMBER:
                    // HACK: Negative numbers and floating points aren't supported.
                    // To get a negative number, just do 0 - <your number>.
                    // To get a floating point, divide.
                    if (Character.isDigit(c)) {
                        strToken += String.valueOf(c);
                    } else {
                        aoTokens.add(new Token(strToken, BasicTokenType.NUMBER, _iLineNumber));
                        strToken = "";
                        oState = JasicTokenType.DEFAULT;
                        i--; // Reprocess this character in the default state.
                    }
                    break;

                case STRING:
                    if (c == '"') {
                        aoTokens.add(new Token(strToken, BasicTokenType.STRING, _iLineNumber));
                        strToken = "";
                        oState = JasicTokenType.DEFAULT;
                    } else {
                        strToken += String.valueOf(c);
                    }
                    break;

                case COMMENT:
                    if (c == '\n') {
                        oState = JasicTokenType.DEFAULT;
                    }
                    break;

                default:
                    break;
            }
        }
        // TODO - This needs to be fixed...
        // HACK: Silently ignore any in-progress token when we run out of
        // characters. This means that, for example, if a script has a string
        // that's missing the closing ", it will just ditch it.
        return aoTokens;
    }
}
