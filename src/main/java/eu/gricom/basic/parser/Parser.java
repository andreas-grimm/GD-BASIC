package eu.gricom.basic.parser;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.statements.Statement;

import java.util.List;

/**
 * Parser.java
 * <p>
 * Description: The Parser interface defines the contract for converting a sequence of tokens into an Abstract Syntax
 * Tree (AST). Implementations analyse the token stream, validate syntax according to BASIC grammar rules, and produce
 * a list of executable Statement objects.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public interface Parser {

    /**
     * Default constructor.
     * The constructor receives the tokenized program and parses it.
     * @return list of Java objects instantiated based on the token list.
     * @throws SyntaxErrorException for any found incorrect code
     */
    List<Statement> parse() throws SyntaxErrorException;

}
