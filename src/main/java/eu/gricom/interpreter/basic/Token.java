package eu.gricom.interpreter.basic;

/**
 * This is a single meaningful chunk of code. It is created by the tokenizer
 * and consumed by the parser.
 */
public /*static */ final class Token {
    public Token(String strText, TokenType oType) {
        this.mstrText = strText;
        this.moType = oType;
    }

    private final String mstrText;
    private final TokenType moType;

    public final String getText() {
        return(this.mstrText);
    }

    public final TokenType getType() {
        return(this.moType);
    }
}

