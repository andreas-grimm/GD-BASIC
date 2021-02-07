package eu.gricom.interpreter.basic.tokenizer;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;

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
public interface Tokenizer {

    /**
     * This function takes a script as a string of characters and chunks it into
     * a sequence of tokens. Each token is a meaningful unit of program, like a
     * variable name, a number, a string, or an operator.
     *
     * @param strSource - the basic source code as a single string.
     * @return list of token found in the source code.
     * @throws SyntaxErrorException for any situation in which the tokenizer fails
     */
    List<Token> tokenize(final String strSource) throws SyntaxErrorException;
}
