package eu.gricom.interpreter.basic.parser;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.helper.Logger;
import eu.gricom.interpreter.basic.statements.AssignStatement;
import eu.gricom.interpreter.basic.statements.EndStatement;
import eu.gricom.interpreter.basic.statements.Expression;
import eu.gricom.interpreter.basic.statements.GotoStatement;
import eu.gricom.interpreter.basic.statements.IfThenStatement;
import eu.gricom.interpreter.basic.statements.InputStatement;
import eu.gricom.interpreter.basic.statements.LabelStatement;
import eu.gricom.interpreter.basic.statements.OperatorExpression;
import eu.gricom.interpreter.basic.statements.PrintStatement;
import eu.gricom.interpreter.basic.statements.Statement;
import eu.gricom.interpreter.basic.statements.VariableExpression;
import eu.gricom.interpreter.basic.tokenizer.BasicTokenType;
import eu.gricom.interpreter.basic.tokenizer.Token;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;

import java.util.ArrayList;
import java.util.List;

/**
 * This defines the Jasic parser. The parser takes in a sequence of tokens
 * and generates an abstract syntax tree. This is the nested data structure
 * that represents the series of statements, and the expressions (which can
 * nest arbitrarily deeply) that they evaluate. In technical terms, what we
 * have is a recursive descent parser, the simplest kind to hand-write.
 *
 * As a side-effect, this phase also stores off the line numbers for each
 * label in the program. It's a bit gross, but it works.
 */
public class JasicParser implements Parser {
    private final Logger _oLogger = new Logger(this.getClass().getName());
    private final List<Token> _aoTokens;
    private int _iPosition;

    /**
     * Default constructor.
     * The constructor receives the tokenized program and parses it.
     *
     * @param aoTokens - the tokenized program
     */
    public JasicParser(final List<Token> aoTokens) {
        _aoTokens = aoTokens;
        _iPosition = 0;
    }

    /**
     * The top-level function to start parsing. This will keep consuming
     * tokens and routing to the other parse functions for the different
     * grammar syntax until we run out of code to parse.
     *
     * @return The list of parsed statements.
     * @throws SyntaxErrorException - marks any syntax errors
     */
    public final List<Statement> parse() throws SyntaxErrorException {
        LabelStatement oLabelStatement = new LabelStatement();
        List<Statement> aoStatements = new ArrayList<>();

        while (true) {
            // Ignore empty lines.
            while (matchNextToken(BasicTokenType.LINE)) {

                _oLogger.debug("--> empty line ");
            }

            // if the current token is of type LABEL, then store the token number in the label list
            if (matchNextToken(BasicTokenType.LABEL)) {
                // Mark the index of the statement after the label.
                _oLogger.debug("--> label: [" + lastToken(1).getText() + "] @ Token List position: " +  _iPosition);
                oLabelStatement.putLabelStatement(lastToken(1).getText(), aoStatements.size());
            } else
                // if the first token is of type WORD and the second one is of type EQUAL (e.g. "a =")
                if (matchNextTwoToken(BasicTokenType.WORD, BasicTokenType.EQUALS)) {
                    // get the name of the variable
                    String strName = lastToken(2).getText();
                    // get the value from the current expression - this is the right side of the equation
                    Expression oValue = expression();
                    // add an assignment statement to the AST
                    _oLogger.debug("--> assignment: [" + strName + "] := [" + oValue.content() + "] @ Token List position: " +  _iPosition);
                    aoStatements.add(new AssignStatement(strName, oValue));
                } else
                    // if the token is of name "PRINT", put the print statement into the AST
                    if (matchNextToken("print")) {
                        _oLogger.debug("--> PRINT: " +  _iPosition);
                        aoStatements.add(new PrintStatement(expression()));
                    } else
                        if (matchNextToken("input")) {
                            _oLogger.debug("--> INPUT: " +  _iPosition);
                            aoStatements.add(new InputStatement(consumeToken(BasicTokenType.WORD).getText()));
                        } else
                            if (matchNextToken("goto")) {
                                _oLogger.debug("--> GOTO: " +  _iPosition);
                                aoStatements.add(new GotoStatement(consumeToken(BasicTokenType.WORD).getText()));
                            } else
                                if (matchNextToken("if")) {
                                    Expression oCondition = expression();
                                    _oLogger.debug("--> IF: [" +  oCondition.content() + "] " +  _iPosition);
                                    consumeToken("then");
                                    String strLabel = consumeToken(BasicTokenType.WORD).getText();
                                    _oLogger.debug("----> THEN: [" +  strLabel + "] " +  _iPosition);
                                    aoStatements.add(new IfThenStatement(oCondition, strLabel));
                                } else
                                    if (matchNextToken("end")) {
                                        aoStatements.add(new EndStatement());
                                    } else {
                                        break; // Unexpected token (likely EOF), so end.
                                    }
        }

        return aoStatements;
    }

    // The following functions each represent one grammatical part of the
    // language. If this parsed English, these functions would be named like
    // noun() and verb().

    /**
     * Parses a single expression. Recursive descent parsers start with the
     * lowest-precedent term and moves towards higher priority. For Basic,
     * binary operators (+, -, etc.) are the lowest.
     *
     * @return The parsed expression.
     * @throws SyntaxErrorException - marks any syntax errors
     */
    private Expression expression() throws SyntaxErrorException {
        return operator();
    }

    /**
     * Parses a series of binary operator expressions into a single
     * expression. In Basic, all operators have the same predecence and
     * associate left-to-right. That means it will interpret:
     *    1 + 2 * 3 - 4 / 5
     * like:
     *    ((((1 + 2) * 3) - 4) / 5)
     *
     * It works by building the expression tree one at a time. So, given
     * this code: 1 + 2 * 3, this will:
     *
     * 1. Parse (1) as an atomic expression.
     * 2. See the (+) and start a new operator expression.
     * 3. Parse (2) as an atomic expression.
     * 4. Build a (1 + 2) expression and replace (1) with it.
     * 5. See the (*) and start a new operator expression.
     * 6. Parse (3) as an atomic expression.
     * 7. Build a ((1 + 2) * 3) expression and replace (1 + 2) with it.
     * 8. Return the last expression built.
     *
     * @return The parsed expression.
     * @throws SyntaxErrorException - marks any syntax issues
     */
    public final Expression operator() throws SyntaxErrorException {

        Expression oExpression = atomic();

        // Keep building operator expressions as long as we have operators.
        while (matchNextToken(BasicTokenType.OPERATOR)
                || matchNextToken(BasicTokenType.EQUALS)) {
            char strOperator = lastToken(1).getText().charAt(0);
            Expression oRight = atomic();
            oExpression = new OperatorExpression(oExpression, String.valueOf(strOperator), oRight);
        }

        return oExpression;
    }

    /**
     * Parses an "atomic" expression. This is the highest level of
     * precedence and contains single literal tokens like 123 and "foo", as
     * well as parenthesized expressions.
     *
     * @return The parsed expression.
     * @throws SyntaxErrorException - mark any syntax issues
     */
    public final Expression atomic() throws SyntaxErrorException {

        // If the current token is of type WORD, then we assume that the next token is a variable.
        if (matchNextToken(BasicTokenType.WORD)) {
            return new VariableExpression(lastToken(1).getText());
        }

        // If the current token is of type number, then return the value as a double value
        if (matchNextToken(BasicTokenType.NUMBER)) {
              return new RealValue(Double.parseDouble(lastToken(1).getText()));
        }

        // If the current token us of type STRING, then return the value as a string value
        if (matchNextToken(BasicTokenType.STRING)) {
            return new StringValue(lastToken(1).getText());
        }

        // The contents of a parenthesized expression can be any expression. This lets us "restart" the precedence cascade
        // so that you can have a lower precedence expression inside the parentheses.
        if (matchNextToken(BasicTokenType.LEFT_PAREN)) {
            Expression expression = expression();
            consumeToken(BasicTokenType.RIGHT_PAREN);
            return expression;
        }

        // OK - here we have a text block that we cannot parse, so we throw an syntax exception
        throw new SyntaxErrorException("Couldn't parse :(" + _aoTokens.toString() + ")");
    }

    // The following functions are the core low-level operations that the
    // grammar parser is built in terms of. They match and consume tokens in
    // the token stream.

    /**
     * Consumes the next two tokens if they are the given type (in order).
     * Consumes no tokens if either check fails.
     *
     * @param  eType1 Expected type of the next token.
     * @param  eType2 Expected type of the subsequent token.
     * @return       True if tokens were consumed.
     */
    public final boolean matchNextTwoToken(final BasicTokenType eType1, final BasicTokenType eType2) {

        Token oOffsetToken0 = getToken(0);
        Token oOffsetToken1 = getToken(1);

        if (oOffsetToken0.getType() != eType1) {
            return false;
        }

        if (oOffsetToken1.getType() != eType2) {
            return false;
        }

        _iPosition += 2;
        return true;
    }

    /**
     * Consumes the next token if it's the given type.
     *
     * @param  oType Expected type of the next token.
     * @return       True if the token was consumed.
     */
    public final boolean matchNextToken(final BasicTokenType oType) {

        if (getToken(0).getType() != oType) {
            return false;
        }
        _iPosition++;
        return true;
    }

    /**
     * Consumes the next token if it's a word token with the given name.
     *
     * @param  strName  Expected name of the next word token.
     * @return          True if the token was consumed.
     */
    public final boolean matchNextToken(final String strName) {

        if (getToken(0).getType() != BasicTokenType.WORD) {
            return false;
        }

        if (!getToken(0).getText().equals(strName)) {
            return false;
        }

        _iPosition++;
        return true;
    }

    /**
     * Consumes the next token if it's the given type. If not, throws an
     * exception. This is for cases where the parser demands a token of a
     * certain type in a certain position, for example a matching ) after
     * an opening (.
     *
     * @param  type  Expected type of the next token.
     * @return       The consumed token.
     */
    public final Token consumeToken(final BasicTokenType type) {

        if (getToken(0).getType() != type) {
            throw new Error("Expected " + type + ".");
        }

        return _aoTokens.get(_iPosition++);
    }

    /**
     * Consumes the next token if it's a word with the given name. If not,
     * throws an exception.
     *
     * @param  strName expected name of the next word token.
     * @return the consumed token.
     * @throws SyntaxErrorException if the next token does not comply to the expected name
     */
    public final Token consumeToken(final String strName) throws SyntaxErrorException {

        if (!matchNextToken(strName)) {
            throw new SyntaxErrorException("Expected " + strName + ".");
        }
        return lastToken(1);
    }

    /**
     * Gets a previously consumed token, indexing backwards. last(1) will
     * be the token just consumed, last(2) the one before that, etc.
     *
     * @param  iOffset How far back in the token stream to look.
     * @return        The consumed token.
     */
    public final Token lastToken(final int iOffset) {

        return _aoTokens.get(_iPosition - iOffset);
    }

    /**
     * Gets an unconsumed token, indexing forward. get(0) will be the next
     * token to be consumed, get(1) the one after that, etc.
     *
     * @param  iOffset How far forward in the token stream to look.
     * @return        The yet-to-be-consumed token.
     */
    public final Token getToken(final int iOffset) {

        //check whether the current position in the tokenized program is larger or equal the token size
        if (_iPosition + iOffset >= _aoTokens.size()) {
            // send an end_of_file token back - this is an unexpected EOF
            return new Token("", BasicTokenType.EOP, 0);
        }

        // get the requested token
        return _aoTokens.get(_iPosition + iOffset);
    }

    /**
     * Helper Function for JUnit.
     *
     * @param iPosition artificial set iPosition
     */
    public final void setPosition(final int iPosition) {

        _iPosition = iPosition;
    }
}
