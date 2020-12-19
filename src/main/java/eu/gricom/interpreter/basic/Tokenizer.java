package eu.gricom.interpreter.basic;

import java.util.ArrayList;
import java.util.List;

/**
 * Tokenizer.java
 * <p>
 * Description:
 * <p>
 * The Tokenizer class converts the source code into a list of tokens. Question here is whether we should convert
 * the Tokenizer class not into a Token factory class. The read a source code file and move the content into a single
 * string. This might cause problems in case of really big programs, so this needs to be addressed later.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public final class Tokenizer {

    /**
     * Default constructor, private as due to a utility class.
     */
    private Tokenizer() {
    }

    /**
     * This function takes a script as a string of characters and chunks it into
     * a sequence of tokens. Each token is a meaningful unit of program, like a
     * variable name, a number, a string, or an operator.
     *
     * @param strSource - the basic source code as a single string.
     * @return list of token found in the source code.
     */
    public static List<Token> tokenize(final String strSource) {
        List<Token> aoTokens = new ArrayList<>();

        String strToken = "";
        TokenizeState oState = TokenizeState.DEFAULT; //The token has not been identified - so the token type is default

        // Many tokens are a single character, like operators and ().
        String charTokens = "\n=+-*/<>()";
        TokenType[] eTokenTypes = {TokenType.LINE, TokenType.EQUALS,
                TokenType.OPERATOR, TokenType.OPERATOR, TokenType.OPERATOR,
                TokenType.OPERATOR, TokenType.OPERATOR, TokenType.OPERATOR,
                TokenType.LEFT_PAREN, TokenType.RIGHT_PAREN
        };

        // Scan through the code one character at a time, building up the list of tokens.
        for (int i = 0; i < strSource.length(); i++) {
            char c = strSource.charAt(i);
            switch (oState) {
                case DEFAULT:
                    if (charTokens.indexOf(c) != -1) {
                        aoTokens.add(new Token(Character.toString(c), eTokenTypes[charTokens.indexOf(c)]));
                    } else if (Character.isLetter(c)) {
                        strToken += c;
                        oState = TokenizeState.WORD;
                    } else if (Character.isDigit(c)) {
                        strToken += c;
                        oState = TokenizeState.NUMBER;
                    } else if (c == '"') {
                        oState = TokenizeState.STRING;
                    } else if (c == '\'') {
                        oState = TokenizeState.COMMENT;
                    }
                    break;

                case WORD:
                    if (Character.isLetterOrDigit(c)) {
                        strToken += c;
                    } else if (c == ':') {
                        aoTokens.add(new Token(strToken, TokenType.LABEL));
                        strToken = "";
                        oState = TokenizeState.DEFAULT;
                    } else {
                        aoTokens.add(new Token(strToken, TokenType.WORD));
                        strToken = "";
                        oState = TokenizeState.DEFAULT;
                        i--; // Reprocess this character in the default state.
                    }
                    break;

                case NUMBER:
                    // HACK: Negative numbers and floating points aren't supported.
                    // To get a negative number, just do 0 - <your number>.
                    // To get a floating point, divide.
                    if (Character.isDigit(c)) {
                        strToken += c;
                    } else {
                        aoTokens.add(new Token(strToken, TokenType.NUMBER));
                        strToken = "";
                        oState = TokenizeState.DEFAULT;
                        i--; // Reprocess this character in the default state.
                    }
                    break;

                case STRING:
                    if (c == '"') {
                        aoTokens.add(new Token(strToken, TokenType.STRING));
                        strToken = "";
                        oState = TokenizeState.DEFAULT;
                    } else {
                        strToken += c;
                    }
                    break;

                case COMMENT:
                    if (c == '\n') {
                        oState = TokenizeState.DEFAULT;
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
        return (aoTokens);
    }
}
