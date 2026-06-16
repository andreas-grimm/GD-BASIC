package eu.gricom.basic.tokenizer;

import java.util.Locale;

/**
 * ReservedWords.java
 * <p>
 * Description: The ReservedWords class maintains the dictionary of all BASIC language keywords and their corresponding
 * token types. During lexical analysis, the lexer uses this class to identify whether a word is a reserved keyword
 * (such as PRINT, GOTO, IF) or a user-defined identifier.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
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
            "@PRAGMA", "ABS", "AND", "ASC", "ATN",
            "CALL", "CDBL", "CHDIR", "CHR", "CINT", "CLEAN", "CLOSE", "CLS", "CMD", "CONT", "COS",
            "DATA", "DEF", "DIM", "DIREXISTS", "DO",
            "ELSE", "END", "END-IF", "END-WHILE", "EOF", "EOL", "ERL", "ERR", "EXIT", "EXP",
            "FCLOSE", "FCOMPARE", "FCOPY", "FDELETE", "FEXISTS", "FGET", "FGETNAME", "FGETSIZE", "FINPUT", "FISOPEN",
            "FLINECOUNT", "FMODTIME", "FOPEN", "FOR", "FPEEK", "FPRINT", "FPUT", "FREE", "FRENAME", "FREWIND",
            "GETCWD", "GOSUB", "GOTO",
            "IF", "INSTR", "INPUT",
            "LEFT", "LEN", "LENGTH", "LET", "LISTDIR", "LOG", "LOG10", "LOWER",
            "MEM", "MID", "%",
            "NEXT", "NOT",
            "ON", "OPEN", "OR",
            "PRINT",
            "RANDOM", "READ", "REM", "RETURN", "RIGHT", "RMDIR", "RND",
            "SIN", "SQR", "STEP", "STOP", "STRING", "STR", "SYSTEM",
            "TAB", "TAN", "THEN", "TIME", "TO",
            "UNTIL", "UPPER",
            "VAL",
            "WHILE", "WRITE",
            "&&", "\\+", "\\-", "\\*", "\\/", ":",
            ">", ">=", "<", "<=", "=", ":=", "==", "!=", "\\^",
            "\\(", "\\)", "\\'", "\\;", "\\,", "||", "\\?",
            ">>", "<<"
    };

    private static BasicTokenType[] _aeTokenTypes = {
        BasicTokenType.PRAGMA,
        BasicTokenType.ABS, BasicTokenType.AND, BasicTokenType.ASC, BasicTokenType.ATN,
        BasicTokenType.CALL, BasicTokenType.CDBL, BasicTokenType.CHDIR, BasicTokenType.CHR, BasicTokenType.CINT,
            BasicTokenType.CLEAN, BasicTokenType.CLOSE, BasicTokenType.CLS, BasicTokenType.CMD, BasicTokenType.CONT,
            BasicTokenType.COS,
        BasicTokenType.DATA, BasicTokenType.DEF, BasicTokenType.DIM, BasicTokenType.DIREXISTS, BasicTokenType.DO,
        BasicTokenType.ELSE, BasicTokenType.END, BasicTokenType.ENDIF, BasicTokenType.ENDWHILE, BasicTokenType.EOF,
            BasicTokenType.EOL, BasicTokenType.ERL, BasicTokenType.ERR, BasicTokenType.EXIT, BasicTokenType.EXP,
        BasicTokenType.FCLOSE, BasicTokenType.FCOMPARE, BasicTokenType.FCOPY, BasicTokenType.FDELETE,
            BasicTokenType.FEXISTS, BasicTokenType.FGET, BasicTokenType.FGETNAME, BasicTokenType.FGETSIZE,
            BasicTokenType.FINPUT, BasicTokenType.FISOPEN, BasicTokenType.FLINECOUNT, BasicTokenType.FMODTIME,
            BasicTokenType.FOPEN, BasicTokenType.FOR, BasicTokenType.FPEEK, BasicTokenType.FPRINT, BasicTokenType.FPUT,
            BasicTokenType.FREE, BasicTokenType.FRENAME, BasicTokenType.FREWIND,
        BasicTokenType.GETCWD, BasicTokenType.GOSUB, BasicTokenType.GOTO,
        BasicTokenType.IF, BasicTokenType.INSTR, BasicTokenType.INPUT,
        BasicTokenType.LEFT, BasicTokenType.LEN, BasicTokenType.LENGTH, BasicTokenType.LET,
            BasicTokenType.LISTDIRECTORY, BasicTokenType.LOG, BasicTokenType.LOG10, BasicTokenType.LOWER,
        BasicTokenType.MEM, BasicTokenType.MID, BasicTokenType.MODULO,
            BasicTokenType.NEXT, BasicTokenType.NOT,
        BasicTokenType.ON, BasicTokenType.OPEN, BasicTokenType.OR,
        BasicTokenType.PRINT,
        BasicTokenType.RANDOM, BasicTokenType.READ, BasicTokenType.REM, BasicTokenType.RETURN,
            BasicTokenType.RIGHT, BasicTokenType.RMDIR, BasicTokenType.RND,
        BasicTokenType.SIN, BasicTokenType.SQR, BasicTokenType.STEP, BasicTokenType.STOP, BasicTokenType.TOSTRING, BasicTokenType.STR,
            BasicTokenType.SYSTEM,
        BasicTokenType.TAB, BasicTokenType.TAN, BasicTokenType.THEN, BasicTokenType.TIME, BasicTokenType.TO,
        BasicTokenType.UNTIL, BasicTokenType.UPPER,
        BasicTokenType.VAL,
        BasicTokenType.WHILE, BasicTokenType.WRITE,
        BasicTokenType.AND, BasicTokenType.PLUS, BasicTokenType.MINUS, BasicTokenType.MULTIPLY, BasicTokenType.DIVIDE,
            BasicTokenType.COLON,
        BasicTokenType.GREATER, BasicTokenType.GREATER_EQUAL, BasicTokenType.SMALLER, BasicTokenType.SMALLER_EQUAL, BasicTokenType.ASSIGN_EQUAL,
            BasicTokenType.PASCAL_ASSIGN_EQUAL, BasicTokenType.COMPARE_EQUAL, BasicTokenType.COMPARE_NOT_EQUAL, BasicTokenType.POWER,
        BasicTokenType.LEFT_PAREN, BasicTokenType.RIGHT_PAREN, BasicTokenType.COMMENT, BasicTokenType.SEMICOLON,
            BasicTokenType.COMMA, BasicTokenType.OR, BasicTokenType.PRINT,
        BasicTokenType.SHIFT_RIGHT, BasicTokenType.SHIFT_LEFT};

    /**
     * getIndex returns the index of the token based on an entered reserved word.
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
     * getTokenIndex returns the index of the token based on an entered token type.
     *
     * @param strTokenType name of the token
     * @return index found for the token
     */
    public static int getTokenIndex(final String strTokenType) {
        int iReturn = -1;
        int iIndex = 0;

        for (BasicTokenType oReserveWord: _aeTokenTypes) {
            if (strTokenType.toUpperCase(Locale.ROOT).matches(oReserveWord.toString())) {
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
    public static BasicTokenType getTokenType(final int iIndex) {
        return _aeTokenTypes[iIndex];
    }

    /**
     * getReservedWord returns the reserved word based on an entered index value.
     *
     * @param iIndex index of the token
     * @return reserved word found for the index
     */
    public static String getReservedWord(final int iIndex) {
        return _astrReservedWords[iIndex];
    }
}
