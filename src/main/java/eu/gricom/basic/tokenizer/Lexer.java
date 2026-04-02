package eu.gricom.basic.tokenizer;

import eu.gricom.basic.error.SyntaxErrorException;

import java.util.List;

/**
 * Lexer.java
 * <p>
 * Description: The Lexer interface defines the contract for tokenising BASIC source code. Implementations convert raw
 * source text into a sequence of Token objects, each representing a meaningful unit of the program such as keywords,
 * identifiers, operators, numbers, and strings.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public interface Lexer {
    /**
     * This function takes a script as a string of characters and chunks it into
     * a sequence of tokens. Each token is a meaningful unit of program, like a
     * variable name, a number, a string, or an operator.
     *
     * @param strSource - the basic source code as a single string.
     * @return list of token found in the source code.
     * @throws SyntaxErrorException for any situation in which the tokenizer fails
     */
    List<Token> tokenize(String strSource) throws SyntaxErrorException;
}
