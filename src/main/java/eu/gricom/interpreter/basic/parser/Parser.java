package eu.gricom.interpreter.basic.parser;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.statements.Statement;

import java.util.List;

/**
 * Parser.java
 * <p>
 * Description:
 * <p>
 * The Parser class converts the source code into a list of tokens. Question here is whether we should convert
 * the Tokenizer class not into a Token factory class. The read a source code file and move the content into a single
 * string. This might cause problems in case of really big programs, so this needs to be addressed later.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public interface Parser {

    /**
     * Default constructor.
     * The constructor receives the tokenized program and parses it.
     * @return
     */
    public List<Statement> parse() throws SyntaxErrorException;

}
