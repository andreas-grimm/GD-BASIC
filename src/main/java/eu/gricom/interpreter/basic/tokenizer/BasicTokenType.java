package eu.gricom.interpreter.basic.tokenizer;

/**
 * This defines the different kinds of tokens for the Dartmouth BASIC styles.
 */
public enum BasicTokenType {
    ABS,
    AND,
    ASC,
    ATN,
    CALL,
    CDBL,
    CHR, // token for the CHR$ function
    CINT,
    CLOSE,
    CLS,
    CMD,
    CONT,
    COS,
    DATA,
    DEFFN,
    DIM,
    DO,
    ELSE,
    END,
    ENDIF,
    ENDWHILE,
    EOF,
    EOL,
    EOP,
    ERL,
    ERR,
    EXIT,
    EXP,
    FOR,
    FRE,
    GOSUB,
    GOTO,
    IF,
    INSTR,
    INPUT,
    LEFT, // token for the LEFT$ function
    LEN,
    LENGTH,
    LET,
    LOG,
    LOG10,
    MEM,
    MID, // token for the MID$ function
    NEXT,
    NOT,
    ON,
    OR,
    PRINT,
    RANDOM,
    READ,
    REM,
    REMAINDER, // token for the % operation
    RETURN,
    RIGHT, //token for the RIGHT$ function
    RND,
    SIN,
    SQR,
    STEP,
    STOP,
    TOSTRING, // token for the STRING$ function
    STR, // token for the STR$ function
    SYSTEM,
    TAB,
    TAN,
    THEN,
    TIME, // token for the TIME$ function
    TO,
    UNTIL,
    VAL,
    WHILE,
    WRITE,
    AMPERSAND, // token for the & operator
    PLUS, // token for the + operator
    MINUS, // token for the - operator
    MULTIPLY, // token for the * operator
    DIVIDE, // token for the / operator
    SHIFT_LEFT, // shift a number / bytes to the left (multiply by 2)
    SHIFT_RIGHT, // shift a number / bytes to the right (divide by 2)
    COLON, // token for the program line divider ':'
    GREATER, // token for the > operator
    GREATER_EQUAL, // token for the => operator
    SMALLER, // token for the < operator
    SMALLER_EQUAL, // token for the <= operator
    ASSIGN_EQUAL, // token for the assignment operator =
    PASCAL_ASSIGN_EQUAL, // token for the PASCAL assignment operator :=
    COMPARE_EQUAL, // token for the == operator
    COMPARE_NOT_EQUAL, // token for the != operator
    POWER, // token for the power operator
    LEFT_PAREN, // token for the ( symbol
    RIGHT_PAREN, // token for the ) symbol
    COMMENT, // to be replaced by REM
    WORD, // super-token to cover command and functions in JASIC
    NUMBER, // Token for any real values
    STRING, // Token for any strings
    INTEGER, // Token for any integer numbers
    BOOLEAN, // Token for any boolean variable
    OPERATOR, // super-token to cover all operations in JASIC
    LABEL, // JASIC GOTO label, not supported for the Dartmouth BASIC dialects
    LINE, // empty line - white space. Kept to keep the line numbering consistent
    EQUALS, // used exclusively for the JASIC version
    COMMA, // used exclusively for the print command
    SEMICOLON // used exclusively for the print command
}
