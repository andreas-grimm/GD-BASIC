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
import eu.gricom.interpreter.basic.statements.LineNumberStatement;
import eu.gricom.interpreter.basic.statements.OperatorExpression;
import eu.gricom.interpreter.basic.statements.PrintStatement;
import eu.gricom.interpreter.basic.statements.Statement;
import eu.gricom.interpreter.basic.statements.VariableExpression;
import eu.gricom.interpreter.basic.tokenizer.Token;
import eu.gricom.interpreter.basic.tokenizer.TokenType;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;

import java.util.ArrayList;
import java.util.List;

/**
 * This defines the Basic parser. The parser takes in a sequence of tokens
 * and generates an abstract syntax tree. This is the nested data structure
 * that represents the series of statements, and the expressions (which can
 * nest arbitrarily deeply) that they evaluate. In technical terms, what we
 * have is a recursive descent parser, the simplest kind to hand-write.
 *
 * As a side-effect, this phase also stores off the line numbers for each
 * label in the program. It's a bit gross, but it works.
 */
public class BasicParser implements Parser {
    private final Logger _oLogger = new Logger(this.getClass().getName());
    private final List<Token> _aoTokens;
    private int _iPosition;
    private LineNumberStatement oLineNumber = new LineNumberStatement();

    /**
     * Default constructor.
     * The constructor receives the tokenized program and parses it.
     *
     * @param aoTokens - the tokenized program
     */
    public BasicParser(final List<Token> aoTokens) {
        _aoTokens = aoTokens;
        _iPosition = 0;
    }

    @Override
    public List<Statement> parse() throws SyntaxErrorException {
        LabelStatement oLabelStatement = new LabelStatement();
        List<Statement> aoStatements = new ArrayList<>();

        int iOrgPosition;

        _oLogger.debug("Start parsing...");
        boolean _bContinue = true;

        while (_bContinue) {
            switch (getToken(0).getType()) {
                // COMMENT Token: Ignore any following part of the line, identical to the REM token.
                case COMMENT:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [COMMENT] ");
                    _iPosition++;
                    break;

                // END Token: Terminate run of program
                case END:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [END] ");
                    oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    aoStatements.add(new EndStatement(_iPosition));
                    _iPosition++;
                    break;

                // EOP Token: End of the program found, finishing the loop thru the program
                case EOP:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [EOP] ");
                    _bContinue = false;
                    _iPosition++;
                    break;

                // GOTO Token: Read the line from terminal for processing
                case GOTO:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [GOTO] ");
                    iOrgPosition = _iPosition;
                    oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    _iPosition++;
                    String strLineNumber = consumeToken(TokenType.NUMBER).getText();
                    aoStatements.add(new GotoStatement(iOrgPosition, strLineNumber));
                    break;

                case IF:
                    iOrgPosition = _iPosition;
                    oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    _iPosition++;
                    Expression oCondition = expression();
                    _oLogger.debug("-parse-> found Token: <" + (_iPosition -1) + "> [IF]: <" + oCondition.content() + ">");
                    String strLabel = consumeToken(TokenType.THEN).getText();
                    _oLogger.debug("-parse-> followed Token: <" + _iPosition + "> [THEN]: <" + strLabel + ">");
                    Token oEndIfToken = findToken(TokenType.ENDIF);
                    _oLogger.debug("-parse-> followed Token: <" + oEndIfToken.getLine() + "> [END-IF]");
                    aoStatements.add(new IfThenStatement(oCondition, iOrgPosition, oEndIfToken.getLine()));
                    break;

                // INPUT Token: Read the line from terminal for processing
                case INPUT:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [INPUT] ");
                    oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    _iPosition++;
                    aoStatements.add(new InputStatement(_iPosition -1, consumeToken(TokenType.WORD).getText()));
                    break;

                // LABEL Token: tbd
                case LABEL:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [LABEL] ");
                    oLabelStatement.putLabelStatement(getToken(0).getText(), aoStatements.size());
                    _iPosition++;
                    break;

                // LINE Token: Describe an empty line, ignore
                case LINE:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [LINE] ");
                    _iPosition++;
                    break;

                // PRINT Token: print to the terminal
                case PRINT:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [PRINT] ");
                    oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    _iPosition++;
                    aoStatements.add(new PrintStatement(_iPosition -1, expression()));
                    break;

                // REM Token: contains comments to the program, ignore the rest of the line
                case REM:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [REM] ");
                    _iPosition++;
                    break;

                // WORD Token: This word is a variable, anything following is variable manipulation
                case WORD:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [WORD] ");
                    int iJumpPosition = _iPosition;
                    oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);

                    if (getToken(1).getType() == TokenType.ASSIGN_EQUAL) {
                        String strName = getToken(0).getText();
                        _iPosition = _iPosition + 2;
                        Expression oValue = expression();
                        aoStatements.add(new AssignStatement(iJumpPosition, strName, oValue));
                    } else {
                        throw new SyntaxErrorException("Incorrect Operator: " + getToken(0).getType().toString() +
                                " in Line [" + getToken(0).getLine() + "]");
                    }

                    break;

                // List of all tokens that are used in a different context (e.g. as part of the IFTHEN token)
                case ENDIF:
                    _oLogger.debug("-parse-> ignoring token: <" + _iPosition + "> [" + getToken(0).getType() + "] ");
                    _iPosition++;
                    break;

                // No Token identified, Syntax Error
                default:
                    throw new SyntaxErrorException("Incorrect Command: " + getToken(0).getLine() + ": ["
                            + getToken(0).getType() + "] <"
                            + getToken(0).getLine() + ">");
            }
        }

        for (Statement oStatement: aoStatements) {
            oLineNumber.putStatementNumber(oStatement.getLineNumber(), aoStatements.indexOf(oStatement));
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
        _oLogger.debug("-expression-> <" + _iPosition + "> [" + getToken(0).getType().toString() + "]");
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
        Token oToken = getToken(0);
        _oLogger.debug("-operator-> token: <" + _iPosition + "> [" + oToken.getType().toString() + "] '" + oToken.getText() + "' [" + oToken.getLine() + "]");

        // loop while the token inspected is either an operator or an assignment
        while ((oToken.getType() == TokenType.PLUS) ||
                (oToken.getType() == TokenType.MINUS) ||
                (oToken.getType() == TokenType.MULTIPLY) ||
                (oToken.getType() == TokenType.DIVIDE) ||
                (oToken.getType() == TokenType.POWER) ||
                (oToken.getType() == TokenType.ASSIGN_EQUAL)) {
            _oLogger.debug("-operator-> token: <" + _iPosition + "> [" + oToken.getType().toString() + "] '" + oToken.getText() + "' [" + oToken.getLine() + "]");
            _iPosition++;
            Expression oRight = atomic();
            oExpression = new OperatorExpression(oExpression, oToken.getText(), oRight);
            oToken = getToken(0);
        }

        _oLogger.debug("-operator-> expression: [" + oExpression.content() + "]");
        return (oExpression);
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
        Token oToken = getToken(0);

        _oLogger.debug("-atomic-> search token: <" + _iPosition + "> [" + oToken.getType().toString() + "] ");

        switch (oToken.getType()) {

            // If the current token is of type WORD, then we assume it is a variable.
            case WORD:
                oToken = getToken(0);
                _oLogger.debug("-atomic-> found token: <" + _iPosition + "> [" + oToken.getType().toString() + "] '" + oToken.getText() + "' [" + oToken.getLine() + "]");
                _iPosition++;
                return (new VariableExpression(oToken.getText()));

            // If the current token is of type NUMBER, then return the value as a double value
            case NUMBER:
                oToken = getToken(0);
                _oLogger.debug("-atomic-> found token: <" + _iPosition + "> [" + oToken.getType().toString() + "] '" + oToken.getText() + "' [" + oToken.getLine() + "]");
                _iPosition++;
                return new RealValue(Double.parseDouble(oToken.getText()));

            // If the current token us of type STRING, then return the value as a string value
            case STRING:
                oToken = getToken(0);
                _oLogger.debug("-atomic-> found token: <" + _iPosition + "> [" + oToken.getType().toString() + "] '" + oToken.getText() + "' [" + oToken.getLine() + "]");
                _iPosition++;
                return new StringValue(oToken.getText());

            // The contents of a parenthesized expression can be any expression. This lets us "restart" the precedence cascade
            // so that you can have a lower precedence expression inside the parentheses.
            case LEFT_PAREN:
                oToken = getToken(0);
                _oLogger.debug("-atomic-> found token: <" + _iPosition + "> [" + oToken.getType().toString() + "] '" + oToken.getText() + "' [" + oToken.getLine() + "]");
                _iPosition++;
                Expression expression = expression();
                consumeToken(TokenType.RIGHT_PAREN);
                return (expression);
        }

        // The contents of a parenthesized expression can be any expression. This lets us "restart" the precedence cascade
        // so that you can have a lower precedence expression inside the parentheses.
        //if (matchNextToken(TokenType.LEFT_PAREN)) {
        //    Expression expression = expression();
        //    consumeToken(TokenType.RIGHT_PAREN);
        //    return (expression);
        //}

        // OK - here we have a text block that we cannot parse, so we throw an syntax exception
        throw new SyntaxErrorException("Couldn't parse : [" + getToken(0).getType().toString() + "] <" + getToken(0).getLine() + ">");
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
    public final boolean matchNextTwoToken(final TokenType eType1, final TokenType eType2) {
        _oLogger.debug("-----> matchNextTwoToken...");

        Token oOffsetToken0 = getToken(0);
        Token oOffsetToken1 = getToken(1);

        if (oOffsetToken0.getType() != eType1) {
            return (false);
        }

        if (oOffsetToken1.getType() != eType2) {
            return (false);
        }

        _iPosition += 2;
        return (true);
    }

    /**
     * Consumes the next token if it's the given type.
     *
     * @param  oType Expected type of the next token.
     * @return       True if the token was consumed.
     */
    public final boolean matchNextToken(final TokenType oType) {
        Token oFoundToken = getToken(0);

        _oLogger.debug("-------> matchNextToken: <" + _iPosition + " + @0> [" + oFoundToken.getType().toString() + "]" +
                " '" + oFoundToken.getText() + "' [" + oFoundToken.getLine() + "] compared to " + oType.toString());

        if (oFoundToken.getType() != oType) {
            _oLogger.debug("---------> token compare failed");
            return (false);
        }
        _oLogger.debug("---------> token compare success");
        _iPosition++;
        return (true);
    }

    /**
     * Consumes the next token if it's a word token with the given name.
     *
     * @param  strName  Expected name of the next word token.
     * @return          True if the token was consumed.
     */
    public final boolean matchNextToken(final String strName) {

        if (getToken(0).getType() != TokenType.WORD) {
            _oLogger.debug("-matchNextToken-> token compare failed");
            return (false);
        }

        if (!getToken(0).getText().equals(strName)) {
            _oLogger.debug("-matchNextToken-> token compare failed");
            return (false);
        }

        _iPosition++;
        _oLogger.debug("-matchNextToken-> token compare success");
        return (true);
    }

    /**
     * Consumes the next token if it's the given type. If not, throws an
     * exception. This is for cases where the parser demands a token of a
     * certain type in a certain position, for example a matching ) after
     * an opening (.
     *
     * @param  oType  Expected type of the next token.
     * @return       The consumed token.
     */
    public final Token consumeToken(final TokenType oType) throws SyntaxErrorException {
        _oLogger.debug("-consumeToken-> looking for Token: <" + (_iPosition) + "> ["+ oType.toString() + "]: <" + getToken(0).getType() +
                ">");

        if (getToken(0).getType() != oType) {
            throw new SyntaxErrorException("Expected " + oType + ".");
        }

        return (_aoTokens.get(_iPosition++));
    }

    /**
     * Consumes the next token if it's the given type. If not, throws an
     * exception. This is for cases where the parser demands a token of a
     * certain type in a certain position, for example a matching ) after
     * an opening (.
     *
     * @param  oType  Expected type of the next token.
     * @return       The found token.
     */
    public final Token findToken(final TokenType oType) throws SyntaxErrorException {
        _oLogger.debug("-findToken-> looking for Token: <" + (_iPosition) + "> ["+ oType.toString() + "]: <" + getToken(0).getType() +
                ">");

        int iCurrentPosition = _iPosition;

        while (iCurrentPosition < _aoTokens.size()) {
            Token oToken = _aoTokens.get(iCurrentPosition);

            if (oToken.getType() == oType) {
                _oLogger.debug("-findToken-> found: <" + (iCurrentPosition) + "> ["+ oToken.getType() + "]");
                return (oToken);
            } else {
                _oLogger.debug("-findToken-> failed: <" + (iCurrentPosition) + "> ["+ oToken.getType() + "]");
                iCurrentPosition++;
            }
        }

        throw new SyntaxErrorException("Missing statement " + oType + ".");
    }


    /**
     * Consumes the next token if it's a word with the given name. If not,
     * throws an exception.
     *
     * @param  strName  Expected name of the next word token.
     * @return          The consumed token.
     */
    public final Token consumeToken(final String strName) {

        if (!matchNextToken(strName)) {
            // TODO convert to syntax error
            throw new Error("Expected " + strName + ".");
        }
        return (previousToken(1));
    }

    /**
     * Gets a previously consumed token, indexing backwards. last(1) will
     * be the token just consumed, last(2) the one before that, etc.
     *
     * @param  iOffset How far back in the token stream to look.
     * @return        The consumed token.
     */
    public final Token previousToken(final int iOffset) {
        Token oToken = _aoTokens.get(_iPosition - iOffset);
        _oLogger.debug("-previousToken-> found token: <" + _iPosition + " - " + iOffset + "> [" + oToken.getType().toString() + "]");

        return (oToken);
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
            // send an end_of_file token back - this is an unexpected EOP
            // TODO actually this is a syntax error and should throw the syntax error exception
            return (new Token("", TokenType.EOP, 0));
        }

        // get the requested token
        return (_aoTokens.get(_iPosition + iOffset));
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