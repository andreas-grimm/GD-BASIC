package eu.gricom.interpreter.basic.parser;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.functions.Function;
import eu.gricom.interpreter.basic.helper.Logger;
import eu.gricom.interpreter.basic.memoryManager.LineNumberXRef;
import eu.gricom.interpreter.basic.statements.AssignStatement;
import eu.gricom.interpreter.basic.statements.ColonStatement;
import eu.gricom.interpreter.basic.statements.DataStatement;
import eu.gricom.interpreter.basic.statements.DoStatement;
import eu.gricom.interpreter.basic.statements.ElseStatement;
import eu.gricom.interpreter.basic.statements.EndStatement;
import eu.gricom.interpreter.basic.statements.Expression;
import eu.gricom.interpreter.basic.statements.ForStatement;
import eu.gricom.interpreter.basic.statements.GosubStatement;
import eu.gricom.interpreter.basic.statements.GotoStatement;
import eu.gricom.interpreter.basic.statements.IfThenStatement;
import eu.gricom.interpreter.basic.statements.InputStatement;
import eu.gricom.interpreter.basic.statements.LabelStatement;
import eu.gricom.interpreter.basic.statements.NextStatement;
import eu.gricom.interpreter.basic.statements.OperatorExpression;
import eu.gricom.interpreter.basic.statements.PragmaStatement;
import eu.gricom.interpreter.basic.statements.PrintStatement;
import eu.gricom.interpreter.basic.statements.ReadStatement;
import eu.gricom.interpreter.basic.statements.RemStatement;
import eu.gricom.interpreter.basic.statements.ReturnStatement;
import eu.gricom.interpreter.basic.statements.Statement;
import eu.gricom.interpreter.basic.statements.UntilStatement;
import eu.gricom.interpreter.basic.statements.VariableExpression;
import eu.gricom.interpreter.basic.statements.WhileStatement;
import eu.gricom.interpreter.basic.tokenizer.BasicTokenType;
import eu.gricom.interpreter.basic.tokenizer.Token;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;

import eu.gricom.interpreter.basic.variableTypes.Value;
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
    private final LineNumberXRef _oLineNumber = new LineNumberXRef();

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


    /**
     * This parse step catches statements that are executed before the program starts. Typical command is DATA.
     *
     * @return List of pre-run commands that need to be processed.
     * @throws SyntaxErrorException for incorrect Basic structures
     */
    public final List<Statement> parsePreRun() throws SyntaxErrorException {
        _iPosition = 0;
        List<Statement> aoStatements = new ArrayList<>();

        _oLogger.debug("Start parsing for pre-run commands...");
        boolean bContinue = true;

        while (bContinue) {
            switch (getToken(0).getType()) {
                case DATA:
                    _oLogger.debug("-parsePreRun-> found Token: <" + _iPosition + "> [DATA] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    List<Value> aoValues = new ArrayList<>();
                    Value oValue;

                    _iPosition++;
                    Token oToken = getToken(0);
                    if (oToken.getType() != BasicTokenType.STRING
                            && oToken.getType() != BasicTokenType.NUMBER) {
                        throw new SyntaxErrorException("Token not of expected type:" + oToken.getType() + " Value: " + oToken.getText());
                    }

                    if (oToken.getType() == BasicTokenType.STRING) {
                        oValue = new StringValue(oToken.getText());
                    } else {
                        oValue = new RealValue(Double.parseDouble(oToken.getText()));
                    }

                    aoValues.add(oValue);
                    _iPosition++;

                    while (getToken(0).getType() == BasicTokenType.COMMA) {
                        _iPosition++;
                        oToken = getToken(0);
                        if (oToken.getType() != BasicTokenType.STRING
                                && oToken.getType() != BasicTokenType.NUMBER) {
                            throw new SyntaxErrorException("Token not of expected type:" + oToken.getType() + " Value: " + oToken.getText());
                        }

                        if (oToken.getType() == BasicTokenType.STRING) {
                            oValue = new StringValue(oToken.getText());
                        } else {
                            oValue = new RealValue(Double.parseDouble(oToken.getText()));
                        }

                        aoValues.add(oValue);
                        _iPosition++;
                    }

                    aoStatements.add(new DataStatement(_iPosition, aoValues));
                    break;

                // EOP Token: End of the program found, finishing the loop thru the program
                case EOP:
                    _oLogger.debug("-parsePreRun-> found Token: <" + _iPosition + "> [EOP] ");
                    bContinue = false;
                    _iPosition++;
                    break;

                // No Token identified, Syntax Error
                default:
                    _oLogger.debug("-parsePreRun-> found Token: <" + _iPosition + "> [" + getToken(0).getType() + "] ");
                    _iPosition++;
            }
        }

        return aoStatements;
    }


    @Override
    public final List<Statement> parse() throws SyntaxErrorException {
        LabelStatement oLabelStatement = new LabelStatement();
        List<Statement> aoStatements = new ArrayList<>();
        _iPosition = 0;

        int iOrgPosition;
        String strTargetLineNumber;

        _oLogger.debug("Start parsing...");
        boolean bContinue = true;

        while (bContinue) {
            switch (getToken(0).getType()) {
                // PRAGMA Token: Change execution behaviour of the program.
                case PRAGMA:
                    int iPragmaLineNumber = _iPosition;
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [@PRAGMA] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    _iPosition++;

                    // Get start assignment, target value, and step size
                    _iPosition++;
                    String strSetting = consumeToken(BasicTokenType.STRING).getText();
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [STRING] " + strSetting);

                    if (getToken(0).getType() != BasicTokenType.ASSIGN_EQUAL) {
                        throw new SyntaxErrorException("Incorrect Operator: " + getToken(0).getType().toString() + " in Line ["
                                                               + getToken(0).getLine() + "]");
                    }

                    _iPosition++;
                    String strValue = consumeToken(BasicTokenType.STRING).getText();
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [VALUE] " + strValue);

                    aoStatements.add(new PragmaStatement(iPragmaLineNumber, strSetting, strValue));

                    _iPosition++;
                    break;

                // COMMENT Token: Ignore any following part of the line, identical to the REM token.
                case COLON:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [COLON] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    aoStatements.add(new ColonStatement(_iPosition));
                    _iPosition++;
                    break;

                // COMMENT Token: Ignore any following part of the line, identical to the REM token.
                case COMMENT:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [COMMENT] ");
                    _iPosition++;
                    break;

                // DATA Token: Already processed in the pre-run parse step, still in to ensure stability
                case DATA:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [DATA] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);

                    Token oToken = getToken(1);
                    if (oToken.getType() != BasicTokenType.STRING
                            && oToken.getType() != BasicTokenType.NUMBER) {
                        throw new SyntaxErrorException("Token not of expected type:" + oToken.getType() + " Value: " + oToken.getText());
                    }
                    _iPosition++;

                    while (getToken(1).getType() == BasicTokenType.COMMA) {
                        _iPosition++;

                        oToken = getToken(1);
                        if (oToken.getType() != BasicTokenType.STRING
                                && oToken.getType() != BasicTokenType.NUMBER) {
                            throw new SyntaxErrorException("Token not of expected type:" + oToken.getType() + " Value: " + oToken.getText());
                        }
                        _iPosition++;
                    }

                    _iPosition++;
                    break;

                // DO Token: Define the anchor point for the DO - UNTIL loop
                case DO:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [DO] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    aoStatements.add(new DoStatement(_iPosition));
                    _iPosition++;
                    break;

                // END Token: Terminate execution of program
                case END:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [END] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    aoStatements.add(new EndStatement(_iPosition));
                    _iPosition++;
                    break;

                // List of all tokens that are used in a different context (e.g. as part of the IFTHEN token)
                case ENDIF:
                    _oLogger.debug("-parse-> ignoring token: <" + _iPosition + "> [" + getToken(0).getType() + "] ");
                    _iPosition++;
                    break;

                // EOP Token: End of the program found, finishing the loop thru the program
                case EOP:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [EOP] ");
                    bContinue = false;
                    _iPosition++;
                    break;

                // FOR Token: Start of the FOR-NEXT loop
                case FOR:
                    Expression oStartValueExpression;
                    Expression oEndValueExpression;
                    Expression oStepSize;

                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [FOR] ");
                    final int iForPosition = _iPosition++;
                    _oLineNumber.putLineNumber(getToken(0).getLine(), iForPosition);

                    // Get start assignment, target value, and step size
                    String strForVariable = consumeToken(BasicTokenType.WORD).getText();
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [WORD] " + strForVariable);

                    if (getToken(0).getType() != BasicTokenType.ASSIGN_EQUAL) {
                        throw new SyntaxErrorException("Incorrect Operator: " + getToken(0).getType().toString() + " in Line ["
                                + getToken(0).getLine() + "]");
                    } else {
                        _iPosition = _iPosition + 1;
                        oStartValueExpression = expression();
                        _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [NUMBER] " + oStartValueExpression.content());
                    }

                    if (getToken(0).getType() != BasicTokenType.TO) {
                        throw new SyntaxErrorException("Missing TO Operator: " + getToken(0).getType().toString()
                                + " in Line [" + getToken(0).getLine() + "]");
                    } else {
                        _iPosition = _iPosition + 1;
                        oEndValueExpression = expression();
                        _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [NUMBER] " + oEndValueExpression.content());
                    }

                    if (getToken(0).getType() != BasicTokenType.STEP) {
                        _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [" + getToken(0).getType().toString() + " ] StepSize set to 1");
                        oStepSize = new RealValue(1); // default step size
                    } else {
                        _iPosition = _iPosition + 1;
                         oStepSize = expression();

                        _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [NUMBER] " + oStepSize.content());
                    }

                    Token oNextToken = findToken(BasicTokenType.NEXT);
                    _oLogger.debug("-parse-> followed Token: <" + oNextToken.getLine() + "> [NEXT]");

                    // add FOR statement to statement list
                    try {
                        ForStatement oForStatement = new ForStatement(iForPosition, strForVariable, oStartValueExpression,
                                                                  oEndValueExpression, oStepSize, oNextToken.getLine());

                        aoStatements.add(oForStatement);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                // GOTO Token: Read the line from terminal for processing
                case GOTO:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [GOTO] ");
                    iOrgPosition = _iPosition;
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    _iPosition++;
                    strTargetLineNumber = consumeToken(BasicTokenType.NUMBER).getText();
                    aoStatements.add(new GotoStatement(iOrgPosition, strTargetLineNumber));
                    break;

                // GOSUB Token: Read the line from terminal for processing
                case GOSUB:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [GOSUB] ");
                    iOrgPosition = _iPosition;
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    _iPosition++;
                    strTargetLineNumber = consumeToken(BasicTokenType.NUMBER).getText();
                    aoStatements.add(new GosubStatement(iOrgPosition, strTargetLineNumber));
                    break;

                // IF Token: Conditional processing
                case IF:
                    int iElsePosition; // initialize the variable that holds the location of the ELSE token
                    iOrgPosition = _iPosition;
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    _iPosition++;
                    Expression oCondition = expression();
                    _oLogger.debug("-parse-> found Token: <" + (_iPosition - 1) + "> [IF]: <" + oCondition.content() + ">");
                    String strLabel = consumeToken(BasicTokenType.THEN).getText();
                    _oLogger.debug("-parse-> followed Token: <" + _iPosition + "> [THEN]: <" + strLabel + ">");

                    // if the programmer wants to jump to a specific location instead of executing the IF block, then
                    // the next token is a number.
                    if (getToken(0).getType() == BasicTokenType.NUMBER) {
                        aoStatements.add(new IfThenStatement(oCondition, iOrgPosition, 0, 0,
                                                             Integer.parseInt(getToken(0).getText())));
                        _iPosition++;
                    } else {
                        // This block is executed if the next token is a command - means the parser looks for a ELSE
                        // token.
                        Token oElseToken = null;

                        try {
                            oElseToken = findToken(BasicTokenType.ELSE);
                            _oLogger.debug("-parse-> followed Token: <" + oElseToken.getLine() + "> [ELSE]");
                            iElsePosition = oElseToken.getLine();
                        } catch (SyntaxErrorException eException) {
                            iElsePosition = 0;
                        }

                        Token oEndIfToken = findToken(BasicTokenType.ENDIF);
                        _oLogger.debug("-parse-> followed Token: <" + oEndIfToken.getLine() + "> [END-IF]");

                        if (oElseToken != null) {
                            _oLogger.debug("-parse-> processing Token: <" + oEndIfToken.getLine() + "> [ELSE]");
                            if (oElseToken.getLine() >= oEndIfToken.getLine()) {
                                _oLogger.debug("-parse-> found ELSE token not in block: <" + oElseToken.getLine()
                                                       + "> [ELSE]");
                            } else {
                                _oLogger.debug("-parse-> found ELSE token in block: <" + oElseToken.getLine() + "> "
                                                       + "> [ELSE]");
                            }
                        }

                        aoStatements.add(new IfThenStatement(oCondition, iOrgPosition, iElsePosition, oEndIfToken.getLine(),
                                                             0));
                    }

                    break;

                // ELSE Token: When the IF block is executed, then the program can run into an ELSE statement.
                case ELSE:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [ELSE] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    _iPosition++;
                    aoStatements.add(new ElseStatement(_iPosition - 1));
                    break;

                // INPUT Token: Read the line from terminal for processing
                case INPUT:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [INPUT] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    _iPosition++;
                    aoStatements.add(new InputStatement(_iPosition - 1, consumeToken(BasicTokenType.WORD).getText()));
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
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    aoStatements.add(new ReturnStatement(_iPosition));
                    _iPosition++;
                    break;

                // ENDWHILE Token: Identical to the NEXT functionality:
                case ENDWHILE:

                // NEXT Token: Start of the FOR-NEXT loop
                case NEXT:
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
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
                    _oLineNumber.putLineNumber(getToken(0).getLine(), iPrintPosition);
                    _iPosition++;


                    if (getToken(0).getType() != BasicTokenType.NUMBER
                            && getToken(0).getType() != BasicTokenType.STRING
                            && getToken(0).getType() != BasicTokenType.WORD) {
                        aoExpression.add(new StringValue(" "));
                    } else {
                        aoExpression.add(expression());
                    }

                    while (getToken(0).getType() == BasicTokenType.COMMA) {
                        _iPosition++;
                        aoExpression.add(expression());
                    }

                    if (getToken(0).getType() == BasicTokenType.SEMICOLON) {
                        _iPosition++;
                        bCRLF = false;
                    }

                    aoStatements.add(new PrintStatement(iPrintPosition, aoExpression, bCRLF));
                    break;

                // READ Token: read a data value from a DATA block
                case READ:
                    int iReadPosition = _iPosition;
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [READ] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    List<String> astrVariables = new ArrayList<>();
                    String strVariable;
                    _iPosition++;

                    Token oReadToken = getToken(0);
                    if (oReadToken.getType() != BasicTokenType.WORD) {
                        throw new SyntaxErrorException("Token not of expected type:" + oReadToken.getType()
                                                               + " Value" + ": " + oReadToken.getText());
                    }

                    astrVariables.add(oReadToken.getText());
                    _iPosition++;

                    while (getToken(0).getType() == BasicTokenType.COMMA) {
                        _iPosition++;
                        oReadToken = getToken(0);
                        if (oReadToken.getType() != BasicTokenType.WORD) {
                            throw new SyntaxErrorException("Token not of expected type:" + oReadToken.getType()
                                                                   + " Value: " + oReadToken.getText());
                        }

                        astrVariables.add(oReadToken.getText());
                        _iPosition++;
                    }

                    aoStatements.add(new ReadStatement(iReadPosition, astrVariables));
                    break;

                // REM Token: contains comments to the program, ignore the rest of the line
                case REM:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [REM] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    aoStatements.add(new RemStatement(_iPosition));
                    _iPosition++;
                    break;

                // UNTIL Token: Conditional processing
                case UNTIL:
                    iOrgPosition = _iPosition;
                    _oLineNumber.putLineNumber(getToken(0).getLine(), iOrgPosition);
                    _iPosition++;
                    Expression oUntilCondition = expression();
                    _oLogger.debug("-parse-> found Token: <" + (iOrgPosition) + "> [UNTIL]: <" + oUntilCondition.content() + ">");
                    aoStatements.add(new UntilStatement(iOrgPosition, oUntilCondition));
                    break;

                // WHILE Token: Conditional looping
                case WHILE:
                    iOrgPosition = _iPosition;
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    _iPosition++;
                    Expression oWhileCondition = expression();
                    _oLogger.debug("-parse-> found Token: <" + (_iPosition - 1) + "> [WHILE]: <" + oWhileCondition.content() + ">");
                    Token oEndWhileToken = findToken(BasicTokenType.ENDWHILE);
                    _oLogger.debug("-parse-> followed Token: <" + oEndWhileToken.getLine() + "> [END-WHILE]");
                    aoStatements.add(new WhileStatement(iOrgPosition, oWhileCondition, oEndWhileToken.getLine()));
                    break;

                // WORD Token: This word is a variable or function, anything following is variable manipulation
                case WORD:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [WORD] ");

                    int iCurrPosition = _iPosition;

                    _oLineNumber.putLineNumber(getToken(0).getLine(), iCurrPosition);

                    if (getToken(1).getType() == BasicTokenType.ASSIGN_EQUAL) {
                        String strName = getToken(0).getText();
                        _iPosition = _iPosition + 2;
                        Expression oExpression = expression();
                        aoStatements.add(new AssignStatement(iCurrPosition, strName, oExpression));
                    } else {
                        throw new SyntaxErrorException("Incorrect Operator: " + getToken(0).getType().toString()
                                + " in Line [" + getToken(0).getLine() + "]");
                    }
                    break;

                // No Token identified, Syntax Error
                default:
                    throw new SyntaxErrorException("Incorrect Command: " + getToken(0).getLine() + ": ["
                            + getToken(0).getType() + "] <"
                            + getToken(0).getLine() + ">");
            }
        }

        for (Statement oStatement: aoStatements) {
            _oLineNumber.putStatementNumber(oStatement.getTokenNumber(), aoStatements.indexOf(oStatement));
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
     * expression. In Basic, all operators have the same precedence and
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
        _oLogger.debug("-operator-> token: <" + _iPosition + "> [" + oToken.getType().toString() + "] '" + oToken.getText()
                + "' [" + oToken.getLine() + "]");

        // loop while the token inspected is either an operator or an assignment
        while (oToken.getType() == BasicTokenType.PLUS
                || oToken.getType() == BasicTokenType.MINUS
                || oToken.getType() == BasicTokenType.MULTIPLY
                || oToken.getType() == BasicTokenType.DIVIDE
                || oToken.getType() == BasicTokenType.MODULO
                || oToken.getType() == BasicTokenType.POWER
                || oToken.getType() == BasicTokenType.AND
                || oToken.getType() == BasicTokenType.OR
                || oToken.getType() == BasicTokenType.COMPARE_EQUAL
                || oToken.getType() == BasicTokenType.COMPARE_NOT_EQUAL
                || oToken.getType() == BasicTokenType.SMALLER
                || oToken.getType() == BasicTokenType.SMALLER_EQUAL
                || oToken.getType() == BasicTokenType.GREATER
                || oToken.getType() == BasicTokenType.GREATER_EQUAL
                || oToken.getType() == BasicTokenType.ASSIGN_EQUAL
                || oToken.getType() == BasicTokenType.SHIFT_LEFT
                || oToken.getType() == BasicTokenType.SHIFT_RIGHT
        ) {
            _oLogger.debug("-operator-> token: <" + _iPosition + "> [" + oToken.getType() + "] '"
                    + oToken.getText() + "' [" + oToken.getLine() + "]");
            _iPosition++;
            Expression oRight = atomic();
            oExpression = new OperatorExpression(oExpression, oToken.getType(), oRight);
            oToken = getToken(0);
        }

        _oLogger.debug("-operator-> expression: [" + oExpression.content() + "]");
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
        Token oToken = getToken(0);

        _oLogger.debug("-atomic-> search token: <" + _iPosition + "> [" + oToken.getType().toString() + "] ");

        switch (oToken.getType()) {

            // If the current token is of type WORD, then we assume it is a variable.
            case WORD:
                oToken = getToken(0);
                _oLogger.debug("-atomic-> found token: <" + _iPosition + "> [" + oToken.getType().toString() + "] '"
                        + oToken.getText() + "' [" + oToken.getLine() + "]");
                _iPosition++;
                return new VariableExpression(oToken.getText());

            // If the current token is of type NUMBER, then return the value as a double value
            case NUMBER:
                oToken = getToken(0);
                _oLogger.debug("-atomic-> found token: <" + _iPosition + "> [" + oToken.getType().toString() + "] '"
                        + oToken.getText() + "' [" + oToken.getLine() + "]");
                _iPosition++;
                return new RealValue(Double.parseDouble(oToken.getText()));

            // If the current token us of type STRING, then return the value as a string value
            case STRING:
                oToken = getToken(0);
                _oLogger.debug("-atomic-> found token: <" + _iPosition + "> [" + oToken.getType().toString() + "] '"
                        + oToken.getText() + "' [" + oToken.getLine() + "]");
                _iPosition++;
                return new StringValue(oToken.getText());

            // The contents of a parenthesized expression can be any expression. This lets us "restart" the precedence cascade
            // so that you can have a lower precedence expression inside the parentheses.
            case LEFT_PAREN:
                oToken = getToken(0);
                _oLogger.debug("-atomic-> found token: <" + _iPosition + "> [" + oToken.getType().toString() + "] '"
                        + oToken.getText() + "' [" + oToken.getLine() + "]");
                _iPosition++;
                Expression oExpression = expression();
                consumeToken(BasicTokenType.RIGHT_PAREN);
                return oExpression;

            // three parameter function calls
            case MID:
                oToken = getToken(0);
                _oLogger.debug("-atomic-> found token: <" + _iPosition + "> [" + oToken.getType().toString() + "] '"
                                       + oToken.getText() + "' [" + oToken.getLine() + "]");
                _iPosition++;
                consumeToken(BasicTokenType.LEFT_PAREN);
                Expression oParameter1 = expression();
                consumeToken(BasicTokenType.COMMA);
                Expression oParameter2 = expression();
                consumeToken(BasicTokenType.COMMA);
                Expression oParameter3 = expression();
                Expression oThreeParameterFunction = new Function(oToken, oParameter1, oParameter2, oParameter3);
                consumeToken(BasicTokenType.RIGHT_PAREN);
                return oThreeParameterFunction;

            // two parameter function calls
            case INSTR: case LEFT: case RIGHT: case SYSTEM:
                oToken = getToken(0);
                _oLogger.debug("-atomic-> found token: <" + _iPosition + "> [" + oToken.getType().toString() + "] '"
                                       + oToken.getText() + "' [" + oToken.getLine() + "]");
                _iPosition++;
                consumeToken(BasicTokenType.LEFT_PAREN);
                Expression oParameter1Expression = expression();
                consumeToken(BasicTokenType.COMMA);
                Expression oParameter2Expression = expression();
                Expression oTwoParameterFunction = new Function(oToken, oParameter1Expression, oParameter2Expression);
                consumeToken(BasicTokenType.RIGHT_PAREN);
                return oTwoParameterFunction;

            // single parameter function calls
            case ABS: case ASC: case ATN: case CDBL: case CHR: case CINT: case COS: case EXP: case LEN: case LOG:
                case LOG10: case NOT: case SIN: case SQR: case STR: case TAN: case VAL:
                oToken = getToken(0);
                _oLogger.debug("-atomic-> found token: <" + _iPosition + "> [" + oToken.getType().toString() + "] '"
                        + oToken.getText() + "' [" + oToken.getLine() + "]");
                _iPosition++;
                consumeToken(BasicTokenType.LEFT_PAREN);
                Expression oFunctionExpression = expression();
                Expression oParameterFunction = new Function(oToken, oFunctionExpression);
                consumeToken(BasicTokenType.RIGHT_PAREN);
                return oParameterFunction;

            // zero parameter function calls
            case MEM: case RND: case TIME:
                oToken = getToken(0);
                _oLogger.debug("-atomic-> found token: <" + _iPosition + "> [" + oToken.getType().toString() + "] '"
                        + oToken.getText() + "' [" + oToken.getLine() + "]");
                _iPosition++;
                return new Function(oToken);

            default:
                // OK - here we have a text block that we cannot parse, so we throw an syntax exception
                throw new SyntaxErrorException("Couldn't parse : [" + getToken(0).getType().toString() + "] <"
                        + getToken(0).getLine() + ">");

        }
    }

    /**
     * Consumes the next token if it's a word token with the given name.
     *
     * @param  strName  Expected name of the next word token.
     * @return          True if the token was consumed.
     */
    @Deprecated
    public final boolean matchNextToken(final String strName) {

        if (getToken(0).getType() != BasicTokenType.WORD) {
            _oLogger.debug("-matchNextToken-> token compare failed");
            return false;
        }

        if (!getToken(0).getText().equals(strName)) {
            _oLogger.debug("-matchNextToken-> token compare failed");
            return false;
        }

        _iPosition++;
        _oLogger.debug("-matchNextToken-> token compare success");
        return true;
    }

    /**
     * Consumes the next token if it's the given type. If not, throws an
     * exception. This is for cases where the parser demands a token of a
     * certain type in a certain position, for example a matching ) after
     * an opening (.
     *
     * @param  oType expected type of the next token.
     * @return the consumed token.
     * @throws SyntaxErrorException the next token is not of the expected type
     */
    public final Token consumeToken(final BasicTokenType oType) throws SyntaxErrorException {
        _oLogger.debug("-consumeToken-> looking for Token: <" + (_iPosition) + "> [" + oType.toString() + "]: <"
                + getToken(0).getType() + ">");

        if (getToken(0).getType() != oType) {
            throw new SyntaxErrorException(getToken(0).getLine() + " Found: "
                                                   + getToken(0).getText() + " " + "expected " + oType + ".");
        }

        return _aoTokens.get(_iPosition++);
    }

    /**
     * Retrieves the next token of a certain type. If no token is found, the method
     * throws an exception. This is for cases where the parser demands a token of a
     * certain type in a certain position, for example a matching ) after an opening (.
     *
     * @param  oType expected type of the found token.
     * @return the found token.
     * @throws SyntaxErrorException the token type cannot be found in the remainder of the BASIC program
     */
    public final Token findToken(final BasicTokenType oType) throws SyntaxErrorException {
        _oLogger.debug("-findToken-> looking for Token: <" + (_iPosition) + "> [" + oType.toString() + "]: <"
                + getToken(0).getType() + ">");

        int iCurrentPosition = _iPosition;

        while (iCurrentPosition < _aoTokens.size()) {
            Token oToken = _aoTokens.get(iCurrentPosition);

            if (oToken.getType() == oType) {
                _oLogger.debug("-findToken-> found: <" + (iCurrentPosition) + "> [" + oToken.getType() + "]");
                return oToken;
            } else {
                _oLogger.debug("-findToken-> failed: <" + iCurrentPosition + "> [" + oToken.getType() + "]");
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
            return new Token("", BasicTokenType.EOP, 0);
        }

        // get the requested token
        return _aoTokens.get(_iPosition + iOffset);
    }
}
