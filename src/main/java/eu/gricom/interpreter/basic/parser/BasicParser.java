package eu.gricom.interpreter.basic.parser;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.helper.Logger;
import eu.gricom.interpreter.basic.statements.AssignStatement;
import eu.gricom.interpreter.basic.statements.EndStatement;
import eu.gricom.interpreter.basic.statements.Expression;
import eu.gricom.interpreter.basic.statements.ForStatement;
import eu.gricom.interpreter.basic.statements.GosubStatement;
import eu.gricom.interpreter.basic.statements.GotoStatement;
import eu.gricom.interpreter.basic.statements.IfThenStatement;
import eu.gricom.interpreter.basic.statements.InputStatement;
import eu.gricom.interpreter.basic.statements.LabelStatement;
import eu.gricom.interpreter.basic.memoryManager.LineNumberXRef;
import eu.gricom.interpreter.basic.statements.NextStatement;
import eu.gricom.interpreter.basic.statements.OperatorExpression;
import eu.gricom.interpreter.basic.statements.PrintStatement;
import eu.gricom.interpreter.basic.statements.ReturnStatement;
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
    private LineNumberXRef oLineNumber = new LineNumberXRef();

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
        String strTargetLineNumber = null;

        _oLogger.debug("Start parsing...");
        boolean _bContinue = true;

        while (_bContinue) {
            switch (getToken(0).getType()) {
                // COMMENT Token: Ignore any following part of the line, identical to the REM token.
                case COMMENT:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [COMMENT] ");
                    _iPosition++;
                    break;

                // END Token: Terminate execution of program
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

                // FOR Token: Start of the FOR-NEXT loop
                case FOR:
                    Expression oStartValueExpression;
                    RealValue oEndValueExpression;
                    RealValue oStepSize;

                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [FOR] ");
                    final int iForPosition = _iPosition++;
                    oLineNumber.putLineNumber(getToken(0).getLine(), iForPosition);

                    // Get start assignment, target value, and step size
                    String strForVariable = consumeToken(TokenType.WORD).getText();
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [WORD] " + strForVariable);

                    if (getToken(0).getType() != TokenType.ASSIGN_EQUAL) {
                        throw new SyntaxErrorException("Incorrect Operator: " + getToken(0).getType().toString() + " in Line [" + getToken(0).getLine() + "]");
                    } else {
                        _iPosition = _iPosition + 1;
                        oStartValueExpression = new RealValue(Double.parseDouble(consumeToken(TokenType.NUMBER).getText()));
                        _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [NUMBER] " + oStartValueExpression.content());
                    }

                    if (getToken(0).getType() != TokenType.TO) {
                        throw new SyntaxErrorException("Missing TO Operator: " + getToken(0).getType().toString() +
                                " in Line [" + getToken(0).getLine() + "]");
                    } else {
                        _iPosition = _iPosition + 1;
                        oEndValueExpression = new RealValue(Double.parseDouble(consumeToken(TokenType.NUMBER).getText()));
                        _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [NUMBER] " + oEndValueExpression.content());
                    }

                    if (getToken(0).getType() != TokenType.STEP) {
                        _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [" + getToken(0).getType().toString() + " ] StepSize set to 1");
                        oStepSize = new RealValue(1); // default step size
                    } else {
                        _iPosition = _iPosition + 1;
                         oStepSize = new RealValue(Double.parseDouble(consumeToken(TokenType.NUMBER).getText()));

                        _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [NUMBER] " + oStepSize.content());
                    }

                    Token oNextToken = findToken(TokenType.NEXT);
                    _oLogger.debug("-parse-> followed Token: <" + oNextToken.getLine() + "> [NEXT]");

                    // add FOR statement to statement list
                    ForStatement oForStatement = new ForStatement(iForPosition, strForVariable, oStartValueExpression,
                            oEndValueExpression, oStepSize, oNextToken.getLine());

                    try {
                        _oLogger.debug("-parse-> build statement: " + oForStatement.content());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    aoStatements.add(oForStatement);
                    break;

                // GOTO Token: Read the line from terminal for processing
                case GOTO:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [GOTO] ");
                    iOrgPosition = _iPosition;
                    oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    _iPosition++;
                    strTargetLineNumber = consumeToken(TokenType.NUMBER).getText();
                    aoStatements.add(new GotoStatement(iOrgPosition, strTargetLineNumber));
                    break;

                // GOSUB Token: Read the line from terminal for processing
                case GOSUB:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [GOSUB] ");
                    iOrgPosition = _iPosition;
                    oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    _iPosition++;
                    strTargetLineNumber = consumeToken(TokenType.NUMBER).getText();
                    aoStatements.add(new GosubStatement(iOrgPosition, strTargetLineNumber));
                    break;

                // IF Token: Conditional processing
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

                // RETURN Token: Jump to the GoSub statement
                case RETURN:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [RETURN], to be translated ");
                    oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    aoStatements.add(new ReturnStatement(_iPosition));
                    _iPosition++;
                    break;

                // NEXT Token: Start of the FOR-NEXT loop
                case NEXT:
                    oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [NEXT] ");
                    aoStatements.add(new NextStatement(_iPosition));
                    _iPosition++;
                    break;

                // PRINT Token: print to the terminal
                case PRINT:
                    int iPrintPosition = _iPosition;
                    List<Expression> aoExpression = new ArrayList<>();
                    boolean bCRLF = true;

                    _oLogger.debug("-parse-> found Token: <" + iPrintPosition + "> [PRINT] ");
                    oLineNumber.putLineNumber(getToken(0).getLine(), iPrintPosition);
                    _iPosition++;

                    aoExpression.add(expression());

                    while (getToken(0).getType() == TokenType.COMMA) {
                        _iPosition++;
                        aoExpression.add(expression());
                    }

                    if (getToken(0).getType() == TokenType.SEMICOLON) {
                        _iPosition++;
                        bCRLF = false;
                    }

                    aoStatements.add(new PrintStatement(iPrintPosition, aoExpression, bCRLF));
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
                (oToken.getType() == TokenType.COMPARE_EQUAL) ||
                (oToken.getType() == TokenType.COMPARE_NOT_EQUAL) ||
                (oToken.getType() == TokenType.SMALLER) ||
                (oToken.getType() == TokenType.SMALLER_EQUAL) ||
                (oToken.getType() == TokenType.GREATER) ||
                (oToken.getType() == TokenType.GREATER_EQUAL) ||
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

    /**
     * Consumes the next token if it's a word token with the given name.
     *
     * @param  strName  Expected name of the next word token.
     * @return          True if the token was consumed.
     */
    @Deprecated
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