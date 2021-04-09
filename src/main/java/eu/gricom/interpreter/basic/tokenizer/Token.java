package eu.gricom.interpreter.basic.tokenizer;

/**
 * Token.java
 * <p>
 * Description:
 * <p>
 * The Lexer is responsible for translating the source code into a chain of Token. When recognizing a token, this
 * object is created, consisting of the found token and the text in the source code. Using this class wrapper, the
 * parser can identify the token and process it.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public final class Token {
    private String _strText;
    private final BasicTokenType _oType;
    private final int _iLineNumber;
    private final int _iCommandSequenceNumber;

    /**
     * Constructor of the Code Generator object.
     * @param strText read text of the source code
     * @param oType type of the token
     * @param iLineNumber number of the line in the BASIC source code
     */
    public Token(final String strText, final BasicTokenType oType, final int iLineNumber) {
        _strText = strText;
        _oType = oType;
        _iLineNumber = iLineNumber;
        _iCommandSequenceNumber = 1; // this is the first command in the line
    }

    /**
     * Constructor of the Code Generator object.
     * @param strText read text of the source code
     * @param oType type of the token
     * @param iLineNumber number of the line in the BASIC source code
     * @param iCommandSequenceNumber the number of the command in the source code line
     */
    public Token(final String strText,
                 final BasicTokenType oType,
                 final int iLineNumber,
                 final int iCommandSequenceNumber) {
        _strText = strText;
        _oType = oType;
        _iLineNumber = iLineNumber;
        _iCommandSequenceNumber = iCommandSequenceNumber;
    }

    /**
     * Get method for the TEXT attribute.
     *
     * @return Read text of the source code
     */
    public String getText() {
        return _strText;
    }

    /**
     * Get method for the TYPE attribute.
     *
     * @return Read type of the token found
     */
    public BasicTokenType getType() {
        return _oType;
    }

    /**
     * Get the line number of the BASIC command.
     *
     * @return BASIC line number
     */
    public int getLine() {
        return _iLineNumber;
    }

    /**
     * Get the sequence number of the command.
     *
     * @return sequence number of the command
     */
    public int getCommandSequence() {
        return _iCommandSequenceNumber;
    }

    /**
     * Get method for the TYPE attribute.
     *
     * @param strText set the content of the token
     * @return Read type of the token found
     */
    public String setText(final String strText) {
        _strText = strText;

        return _strText;
    }
}

