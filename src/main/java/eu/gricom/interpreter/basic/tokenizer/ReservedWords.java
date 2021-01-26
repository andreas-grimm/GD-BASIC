package eu.gricom.interpreter.basic.tokenizer;

import java.util.Locale;

public class ReservedWords {

    /**
     * This defines the different kinds of tokens for the Dartmouth BASIC styles.
     */
    public static String[] astrReservedWords = {
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
            "\\(", "\\)", "\\'"
    };

    static TokenType[] aeTokenTypes = {
        TokenType.ABS, TokenType.AND, TokenType.ASC, TokenType.ATN,
        TokenType.CALL, TokenType.CDBL, TokenType.CHR, TokenType.CINT, TokenType.CLOSE, TokenType.CLS, TokenType.CMD,
            TokenType.CONT, TokenType.CONTWHILE, TokenType.COS, TokenType.CSNG,
        TokenType.DATA, TokenType.DEFFN, TokenType.DIM,
        TokenType.ELSE, TokenType.END, TokenType.ENDIF, TokenType.ENDWHILE, TokenType.EOF, TokenType.EOL, TokenType.ERL, TokenType.ERR, TokenType.EXITWHILE, TokenType.EXP,
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
        TokenType.LEFT_PAREN, TokenType.RIGHT_PAREN, TokenType.COMMENT};

    public static int getIndex (final String strSearchRole) {
        int iReturn = -1;
        int iIndex = 0;

        for (String strReserveWord: astrReservedWords) {
            if (strSearchRole.toUpperCase(Locale.ROOT).matches(strReserveWord)) {
                iReturn = iIndex;
                break;
            }

            iIndex++;
        }

        return (iReturn);
    }

    public static TokenType getTokenType (int iIndex) {
        return (aeTokenTypes[iIndex]);
    }
}
