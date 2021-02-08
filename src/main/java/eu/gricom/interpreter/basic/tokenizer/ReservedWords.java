package eu.gricom.interpreter.basic.tokenizer;

import java.util.Locale;

/**
 * ReservedWords.java
 * <p>
 * Description:
 * <p>
 * The list of reserved words and the matching token.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public final class ReservedWords {

    /**
     * Default constructor.
     */
    private ReservedWords() { }

    /**
     * This defines the different kinds of tokens for the Dartmouth BASIC styles.
     */
    private static String[] _astrReservedWords = {
            "ABS", "AND", "ASC", "ATN",
            "CALL", "CDBL", "CHR$", "CINT", "CLOSE", "CLS", "CMD", "CONT", "CONTINUE", "COS", "CSNG",
            "DATA", "DEFFN", "DIM",
            "ELSE", "END", "END-IF", "END-WHILE", "EOF", "EOL", "ERL", "ERR", "EXIT-WHILE", "EXP",
            "FOR", "FRE",
            "GOSUB", "GOTO",
            "IF", "INSTR$", "INT", "INPUT",
            "LEFT$", "LEN", "LET", "LOG",
            "MEM", "MID$",
            "NEXT", "NOT",
            "ON", "OR",
            "PRINT",
            "RANDOM", "READ", "REM", "%", "RETURN", "RIGHT$", "RND",
            "SGN", "SIN", "SQR", "STEP", "STOP", "STRING$", "STR$", "SYSTEM",
            "TAB", "TAN", "THEN", "TIME$", "TO",
            "UNTIL",
            "VAL",
            "WHILE",
            "&", "\\+", "\\-", "\\*", "\\/", ":",
            ">", ">=", "<", "<=", "=", ":=", "==", "!=", "\\^",
            "\\(", "\\)", "\\'", "\\;", "\\,"
    };

    private static TokenType[] _aeTokenTypes = {
        TokenType.ABS, TokenType.AND, TokenType.ASC, TokenType.ATN,
        TokenType.CALL, TokenType.CDBL, TokenType.CHR, TokenType.CINT, TokenType.CLOSE, TokenType.CLS, TokenType.CMD,
            TokenType.CONT, TokenType.CONTWHILE, TokenType.COS, TokenType.CSNG,
        TokenType.DATA, TokenType.DEFFN, TokenType.DIM,
        TokenType.ELSE, TokenType.END, TokenType.ENDIF, TokenType.ENDWHILE, TokenType.EOF, TokenType.EOL, TokenType.ERL,
            TokenType.ERR, TokenType.EXITWHILE, TokenType.EXP,
        TokenType.FOR, TokenType.FRE,
        TokenType.GOSUB, TokenType.GOTO,
        TokenType.IF, TokenType.INSTR, TokenType.INT, TokenType.INPUT,
        TokenType.LEFT, TokenType.LEN, TokenType.LET, TokenType.LOG,
        TokenType.MEM, TokenType.MID,
        TokenType.NEXT, TokenType.NOT,
        TokenType.ON, TokenType.OR,
        TokenType.PRINT,
        TokenType.RANDOM, TokenType.READ, TokenType.REM, TokenType.REMAINDER, TokenType.RETURN, TokenType.RIGHT, TokenType.RND,
        TokenType.SGN, TokenType.SIN, TokenType.SQR, TokenType.STEP, TokenType.STOP, TokenType.TOSTRING, TokenType.STR, TokenType.SYSTEM,
        TokenType.TAB, TokenType.TAN, TokenType.THEN, TokenType.TIME, TokenType.TO,
        TokenType.UNTIL,
        TokenType.VAL,
        TokenType.WHILE,
        TokenType.AMPERSAND, TokenType.PLUS, TokenType.MINUS, TokenType.MULTIPLY, TokenType.DIVIDE, TokenType.COLON,
        TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.SMALLER, TokenType.SMALLER_EQUAL, TokenType.ASSIGN_EQUAL,
            TokenType.PASCAL_ASSIGN_EQUAL, TokenType.COMPARE_EQUAL, TokenType.COMPARE_NOT_EQUAL, TokenType.POWER,
        TokenType.LEFT_PAREN, TokenType.RIGHT_PAREN, TokenType.COMMENT, TokenType.SEMICOLON, TokenType.COMMA};

    /**
     * getIndex returns the index of the token based on an entered token type.
     *
     * @param strTokenType name of the token
     * @return index found for the token
     */
    public static int getIndex(final String strTokenType) {
        int iReturn = -1;
        int iIndex = 0;

        for (String strReserveWord: _astrReservedWords) {
            if (strTokenType.toUpperCase(Locale.ROOT).matches(strReserveWord)) {
                iReturn = iIndex;
                break;
            }

            iIndex++;
        }

        return iReturn;
    }

    /**
     * getTokenType returns the type of the token based on an entered index value.
     *
     * @param iIndex index of the token
     * @return token type found for the index
     */
    public static TokenType getTokenType(final int iIndex) {
        return _aeTokenTypes[iIndex];
    }
}
