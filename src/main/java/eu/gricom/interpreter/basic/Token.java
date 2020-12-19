package eu.gricom.interpreter.basic;

/**
 * Token.java
 * <p>
 * Description:
 * <p>
 * The Tokenizer is responsible for translating the source code into a chain of Token. When recognizing a token, this
 * object is created, consisting of the found token and the text in the source code. Using this class wrapper, the
 * parser can identify the token and process it.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public final class Token {
    private final String _strText;
    private final TokenType _oType;

    /**
     * Constructor of the Code Generator object.
     *
     * @param strText - read text of the source code
     * @param oType - type of the token
     */
    public Token(final String strText, final TokenType oType) {
        _strText = strText;
        _oType = oType;
    }

    /**
     * Get method for the TEXT attribute.
     *
     * @return Read text of the source code
     */
    public String getText() {
        return (_strText);
    }

    /**
     * Get method for the TYPE attribute.
     *
     * @return Read type of the token found
     */
    public TokenType getType() {
        return (_oType);
    }
}

