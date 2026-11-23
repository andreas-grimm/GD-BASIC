package eu.gricom.basic.parser;

import eu.gricom.basic.functions.Function;
import eu.gricom.basic.statements.ArrayAccessExpression;
import eu.gricom.basic.statements.ArrayAssignStatement;
import eu.gricom.basic.statements.AssignStatement;
import eu.gricom.basic.statements.ChDirStatement;
import eu.gricom.basic.statements.ColonStatement;
import eu.gricom.basic.statements.DataStatement;
import eu.gricom.basic.statements.DimStatement;
import eu.gricom.basic.statements.DoStatement;
import eu.gricom.basic.statements.ElseStatement;
import eu.gricom.basic.statements.EndStatement;
import eu.gricom.basic.statements.Expression;
import eu.gricom.basic.statements.FCloseStatement;
import eu.gricom.basic.statements.FCopyStatement;
import eu.gricom.basic.statements.FDeleteStatement;
import eu.gricom.basic.statements.FGetStatement;
import eu.gricom.basic.statements.FInputStatement;
import eu.gricom.basic.statements.FOpenStatement;
import eu.gricom.basic.statements.FPeekStatement;
import eu.gricom.basic.statements.FPrintStatement;
import eu.gricom.basic.statements.FPutStatement;
import eu.gricom.basic.statements.FRenameStatement;
import eu.gricom.basic.statements.FRewindStatement;
import eu.gricom.basic.statements.ForStatement;
import eu.gricom.basic.statements.GosubStatement;
import eu.gricom.basic.statements.GotoStatement;
import eu.gricom.basic.statements.IfThenStatement;
import eu.gricom.basic.statements.InputStatement;
import eu.gricom.basic.statements.LabelStatement;
import eu.gricom.basic.statements.MkDirStatement;
import eu.gricom.basic.statements.NextStatement;
import eu.gricom.basic.statements.OnGosubStatement;
import eu.gricom.basic.statements.OnGotoStatement;
import eu.gricom.basic.statements.OperatorExpression;
import eu.gricom.basic.statements.PragmaStatement;
import eu.gricom.basic.statements.PrintStatement;
import eu.gricom.basic.statements.ReadStatement;
import eu.gricom.basic.statements.RemStatement;
import eu.gricom.basic.statements.ReturnStatement;
import eu.gricom.basic.statements.RmDirStatement;
import eu.gricom.basic.statements.Statement;
import eu.gricom.basic.statements.UnaryOperatorExpression;
import eu.gricom.basic.statements.UntilStatement;
import eu.gricom.basic.statements.VariableExpression;
import eu.gricom.basic.statements.WhileStatement;
import eu.gricom.basic.tokenizer.BasicTokenType;
import eu.gricom.basic.tokenizer.ReservedWords;
import eu.gricom.basic.tokenizer.Token;
import eu.gricom.basic.variabletypes.BooleanValue;
import eu.gricom.basic.variabletypes.RealValue;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.memorymanager.LineNumberXRef;
import eu.gricom.basic.variabletypes.StringValue;

import eu.gricom.basic.variabletypes.Value;
import java.util.ArrayList;
import java.util.List;

/**
 * This defines the Basic parser. The parser takes in a sequence of tokens
 * and generates an abstract syntax tree. This is the nested data structure
 * that represents the series of statements and the expressions (which can
 * nest arbitrarily deeply) that they evaluate. In technical terms, what we
 * have is a recursive descent parser, the simplest kind to hand-write.
 * <p>
 * As a side-effect, this phase also stores off the line numbers for each
 * label in the program. It's a bit gross, but it works.
 */
public class BasicParser implements Parser {
    private final Logger _oLogger = new Logger(this.getClass().getName());
    private final List<Token> _aoTokens;
    private int _iPosition;
    private final LineNumberXRef _oLineNumber = new LineNumberXRef();
    private final boolean _bDartmouthFlag;

    /**
     * Default constructor.
     * The constructor receives the tokenised program and parses it.
     *
     * @param aoTokens - the tokenised program
     */
    public BasicParser(final List<Token> voTokens, boolean bDartmouthFlag) {
        _aoTokens = voTokens;
        _iPosition = 0;
        _bDartmouthFlag = bDartmouthFlag;
    }


    /**
     * This parse step catches statements that are executed before the program starts. Typical command is DATA.
     *
     * @return List of pre-run commands that need to be processed.
     * @throws SyntaxErrorException for incorrect Basic structures
     */
    public final List<Statement> parsePreRun() throws SyntaxErrorException {
        _iPosition = 0;
        List<Statement> voStatements = new ArrayList<>();

        _oLogger.debug("Start parsing for pre-run commands...");
        boolean bContinue = true;

        while (bContinue) {
            switch (getToken(0).getType()) {
                case DATA:
                    _oLogger.debug("-parsePreRun-> found Token: <" + _iPosition + "> [DATA] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    List<Value> voValues = new ArrayList<>();
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

                    voValues.add(oValue);
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

                        voValues.add(oValue);
                        _iPosition++;
                    }

                    voStatements.add(new DataStatement(_iPosition, voValues));
                    break;

                // EOP Token: End of the program found, finishing the loop through the program
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

        return voStatements;
    }


    @Override
    public final List<Statement> parse() throws SyntaxErrorException {
        LabelStatement oLabelStatement = new LabelStatement();
        List<Statement> voStatements = new ArrayList<>();
        _iPosition = 0;

        int iOrgPosition;
        int iFileId;

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

                    // Syntax: @PRAGMA( "name" = "value" ). The brackets are required and are checked.
                    consumeToken(BasicTokenType.LEFT_PAREN);
                    String strSetting = consumeToken(BasicTokenType.STRING).getText();
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [STRING] " + strSetting);
                    if (!PragmaStatement.isKnownParameter(strSetting)) {
                        throw new SyntaxErrorException("Unknown pragma: " + strSetting + " in Line ["
                                                               + getToken(0).getLine() + "]");
                    }

                    if (getToken(0).getType() != BasicTokenType.ASSIGN_EQUAL) {
                        throw new SyntaxErrorException("Incorrect Operator: " + getToken(0).getType().toString() + " in Line ["
                                                               + getToken(0).getLine() + "]");
                    }

                    _iPosition++;
                    String strValue = consumeToken(BasicTokenType.STRING).getText();
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [VALUE] " + strValue);

                    consumeToken(BasicTokenType.RIGHT_PAREN);
                    voStatements.add(new PragmaStatement(iPragmaLineNumber, strSetting, strValue));
                    break;

                // MKDIR Token: create a directory. Syntax: MKDIR "name"
                case MKDIR:
                    int iMkDirPosition = _iPosition;
                    _oLogger.debug("-parse-> found Token: <" + iMkDirPosition + "> [MKDIR] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), iMkDirPosition);
                    _iPosition++;

                    String strDirectory = consumeToken(BasicTokenType.STRING).getText();
                    voStatements.add(new MkDirStatement(iMkDirPosition, new StringValue(strDirectory)));
                break;

                // RMDIR Token: remove a directory. Syntax: RMDIR "name" or RMDIR "name", TRUE (TRUE also removes
                // the files inside it; without it only an empty directory is removed)
                case RMDIR:
                    int iRmDirPosition = _iPosition;
                    _oLogger.debug("-parse-> found Token: <" + iRmDirPosition + "> [RMDIR] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), iRmDirPosition);
                    _iPosition++;

                    String strRmDirDirectory = consumeToken(BasicTokenType.STRING).getText();
                    BooleanValue oForce = new BooleanValue(false);
                    if (getToken(0).getType() == BasicTokenType.COMMA) {
                        _iPosition++;
                        oForce = new BooleanValue(consumeToken(BasicTokenType.BOOLEAN).getText()
                                .equalsIgnoreCase("TRUE"));
                    }
                    voStatements.add(new RmDirStatement(iRmDirPosition, new StringValue(strRmDirDirectory), oForce));
                break;

                case CHDIR:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [CHDIR] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    int iPosition = _iPosition;
                    _iPosition++;

                    voStatements.add(new ChDirStatement(iPosition, new StringValue(getToken(0).getText())));

                    _iPosition++;
                    break;

                // COMMENT Token: Ignore any following part of the line, identical to the REM token.
                case COLON:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [COLON] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    voStatements.add(new ColonStatement(_iPosition));
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

                // DEF Token: Ignored as this part of the code is processed in the MacroProcessor.
                case DEF:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [DEF] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    voStatements.add(new RemStatement(_iPosition, "found DEF statement"));

                    int iMoveTo = 1;
                    while (getToken(iMoveTo).getType() != BasicTokenType.STRING) {
                        iMoveTo++;
                    }
                    _iPosition = _iPosition + iMoveTo;
                    _iPosition++;
                    break;

                // DIM Token: the DIM command is not supported, ignore the rest of the line
                case DIM:
                    _oLogger.warning("-parse-> found Token: <" + _iPosition + "> [DIM], not supported ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    voStatements.add(new DimStatement(_iPosition));
                    _iPosition++;
                    break;

                // DO Token: Define the anchor point for the DO - UNTIL loop
                case DO:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [DO] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    voStatements.add(new DoStatement(_iPosition));
                    _iPosition++;
                    break;

                // END Token: Terminate execution of program
                case END:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [END] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    voStatements.add(new EndStatement(_iPosition));
                    _iPosition++;
                    break;

                // List of all tokens that are used in a different context (e.g. as part of the IFTHEN token)
                case ENDIF:
                    _oLogger.debug("-parse-> ignoring token: <" + _iPosition + "> [" + getToken(0).getType() + "] ");
                    _iPosition++;
                    break;

                // EOP Token: End of the program found, finishing the loop through the program
                case EOP:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [EOP] ");
                    bContinue = false;
                    _iPosition++;
                    break;

                // FCLOSE Token: close a previously opened file
                case FCLOSE:
                    boolean bDeleteIndicator = false;

                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [FCLOSE] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    iOrgPosition = _iPosition;
                    _iPosition++;

                    iFileId = Integer.parseInt(consumeToken(BasicTokenType.NUMBER).getText());
                    _oLogger.debug("-parse-> [FCLOSE: FileId] <" + iFileId + "> ");

                    String strDeleteIndicator = consumeToken(BasicTokenType.STRING).getText();
                    if (strDeleteIndicator.equals("DELETE")) {
                        _oLogger.debug("-parse-> [FCLOSE: File to be deleted] ");
                        bDeleteIndicator = true;
                    }

                    voStatements.add(new FCloseStatement(iOrgPosition, iFileId, bDeleteIndicator));
                break;

                // FCOPY Token: Copy content from source file to destination file
                case FCOPY:
                    int iSourceFileId;
                    int iDestinationFileId;

                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [FCOPY] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    iOrgPosition = _iPosition;
                    _iPosition++;

                    iSourceFileId = Integer.parseInt(consumeToken(BasicTokenType.NUMBER).getText());
                    _oLogger.debug("-parse-> [FCOPY: Source FileId] <" + iSourceFileId + "> ");

                    consumeToken(BasicTokenType.COMMA);
                    _oLogger.debug("-parse-> [FCOPY: Comma consumed] ");

                    iDestinationFileId = Integer.parseInt(consumeToken(BasicTokenType.NUMBER).getText());
                    _oLogger.debug("-parse-> [FCOPY: Destination FileId] <" + iDestinationFileId + "> ");

                    voStatements.add(new FCopyStatement(iOrgPosition, iSourceFileId, iDestinationFileId));
                break;

                // FDELETE Token: Delete a file identified by its file ID
                case FDELETE:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [FDELETE] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    iOrgPosition = _iPosition;
                    _iPosition++;

                    iFileId = Integer.parseInt(consumeToken(BasicTokenType.NUMBER).getText());
                    _oLogger.debug("-parse-> [FDELETE: FileId] <" + iFileId + "> ");

                    voStatements.add(new FDeleteStatement(iOrgPosition, iFileId));
                break;

                // FGET Token: Read next character from file and assign to variable
                case FGET:
                    String strVariableName;

                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [FGET] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    iOrgPosition = _iPosition;
                    _iPosition++;

                    iFileId = Integer.parseInt(consumeToken(BasicTokenType.NUMBER).getText());
                    _oLogger.debug("-parse-> [FGET: FileId] <" + iFileId + "> ");

                    consumeToken(BasicTokenType.COMMA);
                    _oLogger.debug("-parse-> [FGET: Comma consumed] ");

                    strVariableName = consumeToken(BasicTokenType.WORD).getText();
                    _oLogger.debug("-parse-> [FGET: VariableName] <" + strVariableName + "> ");

                    voStatements.add(new FGetStatement(iOrgPosition, iFileId, strVariableName));
                break;

                // FPEEK Token: Peek at next character from file without consuming it
                case FPEEK:
                    String strFPeekVariableName;

                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [FPEEK] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    iOrgPosition = _iPosition;
                    _iPosition++;

                    iFileId = Integer.parseInt(consumeToken(BasicTokenType.NUMBER).getText());
                    _oLogger.debug("-parse-> [FPEEK: FileId] <" + iFileId + "> ");

                    consumeToken(BasicTokenType.COMMA);
                    _oLogger.debug("-parse-> [FPEEK: Comma consumed] ");

                    strFPeekVariableName = consumeToken(BasicTokenType.WORD).getText();
                    _oLogger.debug("-parse-> [FPEEK: VariableName] <" + strFPeekVariableName + "> ");

                    voStatements.add(new FPeekStatement(iOrgPosition, iFileId, strFPeekVariableName));
                break;

                // FPUT Token: Write character/data from variable to file
                case FPUT:
                    Expression oExpression;

                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [FPUT] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    iOrgPosition = _iPosition;
                    _iPosition++;

                    iFileId = Integer.parseInt(consumeToken(BasicTokenType.NUMBER).getText());
                    _oLogger.debug("-parse-> [FPUT: FileId] <" + iFileId + "> ");

                    consumeToken(BasicTokenType.COMMA);
                    _oLogger.debug("-parse-> [FPUT: Comma consumed] ");

                    oExpression = expression();
                    _oLogger.debug("-parse-> [FPUT: Expression parsed] ");

                    voStatements.add(new FPutStatement(iOrgPosition, iFileId, oExpression));
                break;

                // FRENAME Token: Rename a file identified by its file ID
                case FRENAME:
                    StringValue oNewFileName;

                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [FRENAME] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    iOrgPosition = _iPosition;
                    _iPosition++;

                    iFileId = Integer.parseInt(consumeToken(BasicTokenType.NUMBER).getText());
                    _oLogger.debug("-parse-> [FRENAME: FileId] <" + iFileId + "> ");

                    consumeToken(BasicTokenType.COMMA);
                    _oLogger.debug("-parse-> [FRENAME: Comma consumed] ");

                    oNewFileName = new StringValue(consumeToken(BasicTokenType.STRING).getText());
                    _oLogger.debug("-parse-> [FRENAME: NewFileName] <" + oNewFileName.toString() + "> ");

                    voStatements.add(new FRenameStatement(iOrgPosition, iFileId, oNewFileName));
                break;

                // FREWIND Token: Rewind file pointer to beginning
                case FREWIND:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [FREWIND] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    iOrgPosition = _iPosition;
                    _iPosition++;

                    iFileId = Integer.parseInt(consumeToken(BasicTokenType.NUMBER).getText());
                    _oLogger.debug("-parse-> [FREWIND: FileId] <" + iFileId + "> ");

                    voStatements.add(new FRewindStatement(iOrgPosition, iFileId));
                break;

                // FINPUT Token: Read the line from a file for processing
                case FINPUT:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [FINPUT] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    iOrgPosition = _iPosition;
                    _iPosition++;

                    iFileId = Integer.parseInt(consumeToken(BasicTokenType.NUMBER).getText());
                    _oLogger.debug("-parse-> [FOPEN: FileId] <" + iFileId + "> ");

                    // Phase 7: the comma after the file ID is optional (documented as "FINPUT 1, L$", used as "FINPUT 1 L$")
                    if (getToken(0).getType() == BasicTokenType.COMMA) {
                        _iPosition++;
                    }

                    voStatements.add(new FInputStatement(iOrgPosition, iFileId, consumeToken(BasicTokenType.WORD).getText()));
                break;


                // FOPEN Token: Open a file for read or write purpose
                case FOPEN:
                    String strFileName;
                    String strReadWriteIndicator;

                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [FOPEN] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    iOrgPosition = _iPosition;
                    _iPosition++;

                    iFileId = Integer.parseInt(consumeToken(BasicTokenType.NUMBER).getText());
                    _oLogger.debug("-parse-> [FOPEN: FileId] <" + iFileId + "> ");

                    strFileName = consumeToken(BasicTokenType.STRING).getText();
                    _oLogger.debug("-parse-> [FOPEN: FileName] <" + strFileName + "> ");

                    strReadWriteIndicator = consumeToken(BasicTokenType.STRING).getText();
                    _oLogger.debug("-parse-> [FOPEN: ReadWrite Indicator] <" + strReadWriteIndicator + "> ");

                    voStatements.add(new FOpenStatement(iOrgPosition, iFileId, strFileName, strReadWriteIndicator));
                break;

                // FPRINT Token: print to a file
                case FPRINT:
                    int iFPrintPosition = _iPosition;
                    List<Expression> voFPrintExpression = new ArrayList<>();
                    boolean bCRLF = true;

                    _oLogger.debug("-parse-> found Token: <" + iFPrintPosition + "> [PRINT] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), iFPrintPosition);
                    _iPosition++;


                    iFileId = Integer.parseInt(consumeToken(BasicTokenType.NUMBER).getText());
                    _oLogger.debug("-parse-> [FOPEN: FileId] <" + iFileId + "> ");


                    // Phase 7 fix: the comma after the file ID was not consumed, so a space was written before
                    // every FPRINT value. Now the comma is consumed first; "FPRINT 1" alone writes an empty line.
                    if (getToken(0).getType() == BasicTokenType.COMMA) {
                        _iPosition++;
                        voFPrintExpression.add(expression());
                    } else if (getToken(0).getType() == BasicTokenType.NUMBER
                            || getToken(0).getType() == BasicTokenType.STRING
                            || getToken(0).getType() == BasicTokenType.WORD) {
                        voFPrintExpression.add(expression());
                    } else {
                        voFPrintExpression.add(new StringValue(""));
                    }

                    while (getToken(0).getType() == BasicTokenType.COMMA) {
                        _iPosition++;
                        voFPrintExpression.add(expression());
                    }

                    if (getToken(0).getType() == BasicTokenType.SEMICOLON) {
                        _iPosition++;
                        bCRLF = false;
                    }

                    voStatements.add(new FPrintStatement(iFPrintPosition, iFileId, voFPrintExpression, bCRLF));
                break;


                // FOR Token: Start of the FOR-NEXT loop
                case FOR:
                    voStatements.add(parseForLoop());
                    break;

                // GOTO Token: Read the line from terminal for processing
                case GOTO:
                    voStatements.add(parseGotoStatement());
                    break;

                // GOSUB Token: Read the line from terminal for processing
                case GOSUB:
                    voStatements.add(parseGosubStatement());
                    break;

                // IF Token: Conditional processing
                case IF:
                    voStatements.add(parseIfStatement());
                    break;

                // ELSE Token: When the IF block is executed, then the program can run into an ELSE statement.
                case ELSE:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [ELSE] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    _iPosition++;
                    voStatements.add(new ElseStatement(_iPosition - 1));
                    break;

                // INPUT Token: Read the line from terminal for processing
                case INPUT:
                    voStatements.add(parseInputStatement());
                    break;

                // LABEL Token: tbd
                case LABEL:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [LABEL] ");
                    oLabelStatement.putLabelStatement(getToken(0).getText(), voStatements.size());
                    _iPosition++;
                    break;

                // LINE Token: Describe an empty line, ignore
                case LINE:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [LINE] ");
                    _iPosition++;
                    break;

                // RETURN Token: Jump to the GoSub statement
                case RETURN:
                    voStatements.add(parseReturnStatement());
                    break;

                // ENDWHILE Token: Identical to the NEXT functionality:
                case ENDWHILE:

                // NEXT Token: Start of the FOR-NEXT loop
                case NEXT:
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [NEXT] ");
                    voStatements.add(new NextStatement(_iPosition));
                    _iPosition++;
                    break;

                // PRINT Token: print to the terminal
                case PRINT:
                    voStatements.add(parsePrintStatement());
                    break;

                // READ Token: read a data value from a DATA block
                case READ:
                    voStatements.add(parseReadStatement());
                    break;

                // REM Token: contains comments to the program, ignore the rest of the line
                case REM:
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [REM] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    voStatements.add(new RemStatement(_iPosition, "Comment"));
                    _iPosition++;
                    break;

                // UNTIL Token: Conditional processing
                case UNTIL:
                    iOrgPosition = _iPosition;
                    _oLineNumber.putLineNumber(getToken(0).getLine(), iOrgPosition);
                    _iPosition++;
                    Expression oUntilCondition = expression();
                    _oLogger.debug("-parse-> found Token: <" + (iOrgPosition) + "> [UNTIL]: <" + oUntilCondition.content() + ">");
                    voStatements.add(new UntilStatement(iOrgPosition, oUntilCondition));
                    break;

                // WHILE Token: Conditional looping
                case WHILE:
                    voStatements.add(parseWhileLoop());
                    break;

                // ON Token: Computed GOTO or GOSUB
                case ON:
                    voStatements.add(parseOnStatement());
                    break;

                // WORD Token: This word is a variable or function, anything following is variable manipulation
                case WORD:
                    voStatements.add(parseWordStatement());
                    break;

                // No Token identified, Syntax Error
                default:
                    throw new SyntaxErrorException("Incorrect Command: " + getToken(0).getLine() + ": ["
                            + getToken(0).getType() + "] <"
                            + getToken(0).getLine() + ">");
            }
        }

        for (Statement oStatement: voStatements) {
            _oLineNumber.putStatementNumber(oStatement.getTokenNumber(), voStatements.indexOf(oStatement));
        }

        return voStatements;
    }

    // The following functions each represent one grammatical part of the
    // language. If this parsed English, these functions would be named like
    // noun() and verb().

    /**
     * Parses a single expression. Recursive descent parsers start with the
     * lowest-precedent term and move towards higher priority. For Basic,
     * binary operators (+, -, etc.) are the lowest.
     *
     * @return The parsed expression.
     * @throws SyntaxErrorException - marks any syntax errors
     */
    private Expression expression() throws SyntaxErrorException {
        _oLogger.debug("-expression-> <" + _iPosition + "> [" + getToken(0).getType().toString() + "]");
        if (_bDartmouthFlag) {
            return operator();
        }

        return logicalOr();
    }

    /**
     * For Dartmouth mode:
     * <p>
     * Parses a series of binary operator expressions into a single
     * expression. In Basic, all operators have the same precedence and
     * associate left-to-right. That means it will interpret:
     *    1 + 2 * 3 - 4 / 5
     * like:
     *    ((((1 + 2) * 3) - 4) / 5)
     * <p>
     * It works by building the expression tree one at a time. So, given
     * this code: 1 + 2 * 3, this will:
     * <p>
     * 1. Parse (1) as an atomic expression.
     * 2. See the (+) and start a new operator expression.
     * 3. Parse (2) as an atomic expression.
     * 4. Build a (1 + 2) expression and replace (1) with it.
     * 5. See the (*) and start a new operator expression.
     * 6. Parse (3) as an atomic expression.
     * 7. Build a ((1 + 2) * 3) expression and replace (1 + 2) with it.
     * 8. Return the last expression built.
     * <p>
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
     *
     * Logical OR operator (lowest precedence)
     *
     * @return Expression with logical OR precedence
     * @throws SyntaxErrorException for syntax errors
     */
    private Expression logicalOr() throws SyntaxErrorException {
        Expression oLeft = logicalAnd();

        while (getToken(0).getType() == BasicTokenType.OR) {
            Token oOperator = getToken(0);
            _iPosition++;
            Expression oRight = logicalAnd();
            oLeft = new OperatorExpression(oLeft, oOperator.getType(), oRight);
        }

        return oLeft;
    }

    /**
     * Logical AND operator
     *
     * @return Expression with logical AND precedence
     * @throws SyntaxErrorException for syntax errors
     */
    private Expression logicalAnd() throws SyntaxErrorException {
        Expression oLeft = equality();

        while (getToken(0).getType() == BasicTokenType.AND) {
            Token oOperator = getToken(0);
            _iPosition++;
            Expression oRight = equality();
            oLeft = new OperatorExpression(oLeft, oOperator.getType(), oRight);
        }

        return oLeft;
    }

    /**
     * Equality operators (==, !=)
     *
     * @return Expression with equality precedence
     * @throws SyntaxErrorException for syntax errors
     */
    private Expression equality() throws SyntaxErrorException {
        Expression oLeft = comparison();

        while (getToken(0).getType() == BasicTokenType.COMPARE_EQUAL
                || getToken(0).getType() == BasicTokenType.COMPARE_NOT_EQUAL) {
            Token oOperator = getToken(0);
            _iPosition++;
            Expression oRight = comparison();
            oLeft = new OperatorExpression(oLeft, oOperator.getType(), oRight);
        }

        return oLeft;
    }

    /**
     * Comparison operators (<, <=, >, >=)
     *
     * @return Expression with comparison precedence
     * @throws SyntaxErrorException for syntax errors
     */
    private Expression comparison() throws SyntaxErrorException {
        Expression oLeft = shift();

        while (getToken(0).getType() == BasicTokenType.SMALLER
                || getToken(0).getType() == BasicTokenType.SMALLER_EQUAL
                || getToken(0).getType() == BasicTokenType.GREATER
                || getToken(0).getType() == BasicTokenType.GREATER_EQUAL) {
            Token oOperator = getToken(0);
            _iPosition++;
            Expression oRight = shift();
            oLeft = new OperatorExpression(oLeft, oOperator.getType(), oRight);
        }

        return oLeft;
    }

    /**
     * Bitwise shift operators (<<, >>)
     *
     * @return Expression with shift precedence
     * @throws SyntaxErrorException for syntax errors
     */
    private Expression shift() throws SyntaxErrorException {
        Expression oLeft = addition();

        while (getToken(0).getType() == BasicTokenType.SHIFT_LEFT
                || getToken(0).getType() == BasicTokenType.SHIFT_RIGHT) {
            Token oOperator = getToken(0);
            _iPosition++;
            Expression oRight = addition();
            oLeft = new OperatorExpression(oLeft, oOperator.getType(), oRight);
        }

        return oLeft;
    }

    /**
     * Addition and subtraction operators (+, -)
     *
     * @return Expression with addition precedence
     * @throws SyntaxErrorException for syntax errors
     */
    private Expression addition() throws SyntaxErrorException {
        Expression oLeft = multiplication();

        while (getToken(0).getType() == BasicTokenType.PLUS
                || getToken(0).getType() == BasicTokenType.MINUS) {
            Token oOperator = getToken(0);
            _iPosition++;
            Expression oRight = multiplication();
            oLeft = new OperatorExpression(oLeft, oOperator.getType(), oRight);
        }

        return oLeft;
    }

    /**
     * Multiplication, division, and modulo operators (*, /, %)
     *
     * @return Expression with multiplication precedence
     * @throws SyntaxErrorException for syntax errors
     */
    private Expression multiplication() throws SyntaxErrorException {
        Expression oLeft = exponentiation();

        while (getToken(0).getType() == BasicTokenType.MULTIPLY
                || getToken(0).getType() == BasicTokenType.DIVIDE
                || getToken(0).getType() == BasicTokenType.MODULO) {
            Token oOperator = getToken(0);
            _iPosition++;
            Expression oRight = exponentiation();
            oLeft = new OperatorExpression(oLeft, oOperator.getType(), oRight);
        }

        return oLeft;
    }

    /**
     * Exponentiation operator (^) - right associative
     *
     * @return Expression with exponentiation precedence
     * @throws SyntaxErrorException for syntax errors
     */
    private Expression exponentiation() throws SyntaxErrorException {
        Expression oLeft = unary();

        if (getToken(0).getType() == BasicTokenType.POWER) {
            Token oOperator = getToken(0);
            _iPosition++;
            // Exponentiation is right-associative, so we call exponentiation() recursively
            Expression oRight = exponentiation();
            oLeft = new OperatorExpression(oLeft, oOperator.getType(), oRight);
        }

        return oLeft;
    }

    /**
     * Unary operators (+, -, NOT)
     *
     * @return Expression with unary precedence
     * @throws SyntaxErrorException for syntax errors
     */
    private Expression unary() throws SyntaxErrorException {
        if (getToken(0).getType() == BasicTokenType.PLUS
                || getToken(0).getType() == BasicTokenType.MINUS
                || getToken(0).getType() == BasicTokenType.NOT) {
            Token oOperator = getToken(0);
            _iPosition++;
            Expression oOperand = unary();
            return new UnaryOperatorExpression(oOperator.getType(), oOperand);
        }

        return atomic();
    }


    /**
     * Parses an "atomic" expression. This is the highest level of
     * precedence and contains single literal tokens like 123 and "foo", as
     * well as parenthesised expressions.
     *
     * @return The parsed expression.
     * @throws SyntaxErrorException - mark any syntax issues
     */
    public final Expression atomic() throws SyntaxErrorException {
        Token oToken = getToken(0);

        _oLogger.debug("-atomic-> search token: <" + _iPosition + "> [" + oToken.getType().toString() + "] ");

        switch (oToken.getType()) {

            // If the current token is of type WORD, then we assume it is a variable or array access.
            case WORD:
                oToken = getToken(0);
                _oLogger.debug("-atomic-> found token: <" + _iPosition + "> [" + oToken.getType().toString() + "] '"
                        + oToken.getText() + "' [" + oToken.getLine() + "]");
                _iPosition++;

                if (getToken(0).getType() == BasicTokenType.LEFT_PAREN) {
                    String strArrayName = oToken.getText();
                    _iPosition++;                          // consume (

                    List<Expression> voIndices = new ArrayList<>();
                    voIndices.add(expression());           // parse first index expression

                    while (getToken(0).getType() == BasicTokenType.COMMA) {
                        _iPosition++;                      // consume ,
                        voIndices.add(expression());
                    }

                    if (getToken(0).getType() != BasicTokenType.RIGHT_PAREN) {
                        throw new SyntaxErrorException("Expected ) after array index in Line ["
                                + getToken(0).getLine() + "]");
                    }
                    _iPosition++;                          // consume )

                    return new ArrayAccessExpression(strArrayName, voIndices);
                }

                return new VariableExpression(oToken.getText());

            // If the current token is of type BOOLEAN (TRUE or FALSE), then return the value as a boolean value
            case BOOLEAN:
                oToken = getToken(0);
                _oLogger.debug("-atomic-> found token: <" + _iPosition + "> [" + oToken.getType().toString() + "] '"
                        + oToken.getText() + "' [" + oToken.getLine() + "]");
                _iPosition++;
                return new BooleanValue(Boolean.parseBoolean(oToken.getText()));

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

            // The contents of a parenthesised expression can be any expression. This lets us "restart" the precedence cascade
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
            case LISTDIRECTORY: case MID:
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
            case FCOMPARE: case INSTR: case LEFT: case RIGHT: case SYSTEM: case CALL:
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

            // single parameter function calls. Add all functions that contain only one parameter.
            case ABS: case ASC: case ATN: case CDBL: case CHR: case CINT: case COS: case DIREXISTS: case EOF: case EXP:
                case FEXISTS: case FGETNAME: case FGETSIZE: case FISOPEN: case FLINECOUNT: case FMODTIME: case LEN:
                case LOG: case LOG10: case LOWER: case NOT: case SIN: case SQR: case STR: case TAN: case UPPER: case VAL:
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
            case GETCWD: case MEM: case RND: case TIME:
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
    public final Token consumeToken(final BasicTokenType eType) throws SyntaxErrorException {
        _oLogger.debug("-consumeToken-> looking for Token: <" + (_iPosition) + "> [" + eType.toString() + "]: <"
                + getToken(0).getType() + ">");

        if (getToken(0).getType() != eType) {
            throw new SyntaxErrorException(getToken(0).getLine() + " Found: "
                                                   + getToken(0).getText() + " " + "expected " + eType + ".");
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
    public final Token findToken(final BasicTokenType eType) throws SyntaxErrorException {
        _oLogger.debug("-findToken-> looking for Token: <" + (_iPosition) + "> [" + eType.toString() + "]: <"
                + getToken(0).getType() + ">");

        int iCurrentPosition = _iPosition;

        while (iCurrentPosition < _aoTokens.size()) {
            Token oToken = _aoTokens.get(iCurrentPosition);

            if (oToken.getType() == eType) {
                _oLogger.debug("-findToken-> found: <" + (iCurrentPosition) + "> [" + oToken.getType() + "]");
                return oToken;
            } else {
                _oLogger.debug("-findToken-> failed: <" + iCurrentPosition + "> [" + oToken.getType() + "]");
                iCurrentPosition++;
            }
        }

        String strReservedWord = ReservedWords.getReservedWord(ReservedWords.getTokenIndex(eType.toString()));
        throw new SyntaxErrorException("Missing statement " + strReservedWord + "."); //TO-DO Replace to reserved word
    }

    /**
     * Gets an unconsumed token, indexing forward. get(0) will be the next
     * token to be consumed, get(1) the one after that, etc.
     *
     * @param  iOffset How far forward in the token stream to look.
     * @return        The yet-to-be-consumed token.
     */
    public final Token getToken(final int iOffset) {

        //check whether the current position in the tokenised program is larger or equal the token size
        if (_iPosition + iOffset >= _aoTokens.size()) {
            // send an end_of_file token back - this is an unexpected EOP
            // TODO actually this is a syntax error and should throw the syntax error exception
            return new Token("", BasicTokenType.EOP, 0);
        }

        // get the requested token
        return _aoTokens.get(_iPosition + iOffset);
    }

    /**
     * Parse a PRINT statement.
     *
     * @return the parsed PrintStatement
     * @throws SyntaxErrorException if parsing fails
     */
    private Statement parsePrintStatement() throws SyntaxErrorException {
        int iPrintPosition = _iPosition;
        List<Expression> voExpression = new ArrayList<>();
        boolean bCRLF = true;

        _oLogger.debug("-parse-> found Token: <" + iPrintPosition + "> [PRINT] ");
        _oLineNumber.putLineNumber(getToken(0).getLine(), iPrintPosition);
        _iPosition++;

        // Phase 7 fix: any value on the same line can start the list (function calls such as LEN(x) too), not only
        // numbers, strings and words. An empty PRINT keeps the old behaviour (a space).
        int iPrintLine = _aoTokens.get(iPrintPosition).getLine();
        if (startsPrintItem(getToken(0), iPrintLine)) {
            voExpression.add(expression());
        } else {
            voExpression.add(new StringValue(" "));
        }

        // Items are separated by a comma or a semicolon. Both join the items without a space (as before).
        while (getToken(0).getType() == BasicTokenType.COMMA || getToken(0).getType() == BasicTokenType.SEMICOLON) {
            boolean bSemicolon = getToken(0).getType() == BasicTokenType.SEMICOLON;
            _iPosition++;

            if (!startsPrintItem(getToken(0), iPrintLine)) {
                // a separator at the end of the list: a trailing semicolon keeps the cursor on the same line
                if (bSemicolon) {
                    bCRLF = false;
                }
                break;
            }
            voExpression.add(expression());
        }

        return new PrintStatement(iPrintPosition, voExpression, bCRLF);
    }

    /**
     * Tells whether a token can start an item of a PRINT list: a value on the same line, and not the end of the
     * statement (colon, end of program), a separator, or the start of ELSE / END-IF. (Phase 7)
     *
     * @param oToken the token to check
     * @param iPrintLine the line number of the PRINT keyword
     * @return true when an expression starts at this token
     */
    private boolean startsPrintItem(final Token oToken, final int iPrintLine) {
        if (oToken.getLine() != iPrintLine) {
            return false;
        }
        switch (oToken.getType()) {
            case EOP:
            case COLON:
            case SEMICOLON:
            case COMMA:
            case ELSE:
            case ENDIF:
                return false;
            default:
                return true;
        }
    }

    /**
     * Parse a READ statement.
     *
     * @return the parsed ReadStatement
     * @throws SyntaxErrorException if parsing fails
     */
    private Statement parseReadStatement() throws SyntaxErrorException {
        int iReadPosition = _iPosition;
        _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [READ] ");
        _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
        List<String> vstrVariables = new ArrayList<>();
        _iPosition++;

        Token oReadToken = getToken(0);
        if (oReadToken.getType() != BasicTokenType.WORD) {
            throw new SyntaxErrorException("Token not of expected type:" + oReadToken.getType()
                    + " Value: " + oReadToken.getText());
        }

        String strVariableName = oReadToken.getText();
        _iPosition++;

        // Handle array subscripts if present (e.g., A$(I%))
        if (getToken(0).getType() == BasicTokenType.LEFT_PAREN) {
            StringBuilder oArrayRef = new StringBuilder(strVariableName);
            _iPosition++;
            oArrayRef.append("(");
            while (getToken(0).getType() != BasicTokenType.RIGHT_PAREN) {
                if (getToken(0).getType() == BasicTokenType.EOP) {
                    throw new SyntaxErrorException("Missing ) in array subscript");
                }
                if (getToken(0).getType() != BasicTokenType.LEFT_PAREN) {
                    oArrayRef.append(getToken(0).getText());
                }
                _iPosition++;
            }
            _iPosition++; // skip )
            oArrayRef.append(")");
            vstrVariables.add(oArrayRef.toString());
        } else {
            vstrVariables.add(strVariableName);
        }

        while (getToken(0).getType() == BasicTokenType.COMMA) {
            _iPosition++;
            oReadToken = getToken(0);
            if (oReadToken.getType() != BasicTokenType.WORD) {
                throw new SyntaxErrorException("Token not of expected type:" + oReadToken.getType()
                        + " Value: " + oReadToken.getText());
            }

            strVariableName = oReadToken.getText();
            _iPosition++;

            // Handle array subscripts if present
            if (getToken(0).getType() == BasicTokenType.LEFT_PAREN) {
                StringBuilder oArrayRef = new StringBuilder(strVariableName);
                _iPosition++;
                oArrayRef.append("(");
                while (getToken(0).getType() != BasicTokenType.RIGHT_PAREN) {
                    if (getToken(0).getType() == BasicTokenType.EOP) {
                        throw new SyntaxErrorException("Missing ) in array subscript");
                    }
                    if (getToken(0).getType() != BasicTokenType.LEFT_PAREN) {
                        oArrayRef.append(getToken(0).getText());
                    }
                    _iPosition++;
                }
                _iPosition++; // skip )
                oArrayRef.append(")");
                vstrVariables.add(oArrayRef.toString());
            } else {
                vstrVariables.add(strVariableName);
            }
        }

        return new ReadStatement(iReadPosition, vstrVariables);
    }

    /**
     * Parse an INPUT statement.
     *
     * @return the parsed InputStatement
     * @throws SyntaxErrorException if parsing fails
     */
    private Statement parseInputStatement() throws SyntaxErrorException {
        _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [INPUT] ");
        _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
        int iInputPos = _iPosition;
        _iPosition++;
        return new InputStatement(iInputPos, consumeToken(BasicTokenType.WORD).getText());
    }

    /**
     * Parse a GOTO statement.
     *
     * @return the parsed GotoStatement
     * @throws SyntaxErrorException if parsing fails
     */
    private Statement parseGotoStatement() throws SyntaxErrorException {
        int iOrgPosition = _iPosition;
        _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [GOTO] ");
        _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
        _iPosition++;
        String strTargetLineNumber = consumeToken(BasicTokenType.NUMBER).getText();
        return new GotoStatement(iOrgPosition, strTargetLineNumber);
    }

    /**
     * Parse a GOSUB statement.
     *
     * @return the parsed GosubStatement
     * @throws SyntaxErrorException if parsing fails
     */
    private Statement parseGosubStatement() throws SyntaxErrorException {
        int iOrgPosition = _iPosition;
        _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [GOSUB] ");
        _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
        _iPosition++;
        String strTargetLineNumber = consumeToken(BasicTokenType.NUMBER).getText();
        return new GosubStatement(iOrgPosition, strTargetLineNumber);
    }

    /**
     * Parse an ON statement (either ON GOTO or ON GOSUB).
     *
     * @return the parsed OnGotoStatement or OnGosubStatement
     * @throws SyntaxErrorException if parsing fails
     */
    private Statement parseOnStatement() throws SyntaxErrorException {
        int iOrgPosition = _iPosition;
        _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [ON]");
        _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
        _iPosition++;  // consume ON

        Expression oSelectExpression = expression();

        if (getToken(0).getType() == BasicTokenType.GOTO) {
            _iPosition++;  // consume GOTO
            _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [GOTO]");

            List<String> vstrTargets = new ArrayList<>();
            vstrTargets.add(consumeToken(BasicTokenType.NUMBER).getText());

            while (getToken(0).getType() == BasicTokenType.COMMA) {
                _iPosition++;  // consume comma
                vstrTargets.add(consumeToken(BasicTokenType.NUMBER).getText());
            }

            return new OnGotoStatement(iOrgPosition, oSelectExpression, vstrTargets);
        } else if (getToken(0).getType() == BasicTokenType.GOSUB) {
            _iPosition++;  // consume GOSUB
            _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [GOSUB]");

            List<String> vstrTargets = new ArrayList<>();
            vstrTargets.add(consumeToken(BasicTokenType.NUMBER).getText());

            while (getToken(0).getType() == BasicTokenType.COMMA) {
                _iPosition++;  // consume comma
                vstrTargets.add(consumeToken(BasicTokenType.NUMBER).getText());
            }

            return new OnGosubStatement(iOrgPosition, oSelectExpression, vstrTargets);
        } else {
            throw new SyntaxErrorException("Expected GOTO or GOSUB after ON expression at line " + getToken(0).getLine());
        }
    }

    /**
     * Parse a RETURN statement.
     *
     * @return the parsed ReturnStatement
     * @throws SyntaxErrorException if parsing fails
     */
    private Statement parseReturnStatement() throws SyntaxErrorException {
        _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [RETURN]");
        _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
        int iReturnPos = _iPosition;
        _iPosition++;
        return new ReturnStatement(iReturnPos);
    }

    /**
     * Parse a WORD statement (assignment or array assignment).
     *
     * @return the parsed AssignStatement or ArrayAssignStatement
     * @throws SyntaxErrorException if parsing fails
     */
    private Statement parseWordStatement() throws SyntaxErrorException {
        _oLogger.debug("-parse-> found Token: " + getToken(0).getText()
                + " <" + _iPosition + "> [WORD] ");

        int iCurrPosition = _iPosition;
        _oLineNumber.putLineNumber(getToken(0).getLine(), iCurrPosition);

        // Simple assignment: variable = expression
        if (getToken(1).getType() == BasicTokenType.ASSIGN_EQUAL) {
            String strName = getToken(0).getText();
            _iPosition = _iPosition + 2;
            Expression oExpression = expression();
            return new AssignStatement(iCurrPosition, strName, oExpression);
        // Array assignment: array(indices) = expression
        } else if (getToken(1).getType() == BasicTokenType.LEFT_PAREN) {
            String strName = getToken(0).getText();
            _iPosition = _iPosition + 2;

            List<Expression> voIndices = new ArrayList<>();
            voIndices.add(expression());

            while (getToken(0).getType() == BasicTokenType.COMMA) {
                _iPosition++;
                voIndices.add(expression());
            }

            if (getToken(0).getType() != BasicTokenType.RIGHT_PAREN) {
                throw new SyntaxErrorException("Expected ) after array index in Line ["
                        + getToken(0).getLine() + "]");
            }
            _iPosition++;

            if (getToken(0).getType() != BasicTokenType.ASSIGN_EQUAL) {
                throw new SyntaxErrorException("Expected = after array subscript in Line ["
                        + getToken(0).getLine() + "]");
            }
            _iPosition++;

            Expression oValue = expression();
            return new ArrayAssignStatement(iCurrPosition, strName, voIndices, oValue);
        } else {
            throw new SyntaxErrorException("Incorrect Operator: " + getToken(0).getText()
                    + " followed by " + getToken(1).getType().toString()
                    + " in Line [" + getToken(0).getLine() + "]");
        }
    }

    /**
     * Parse a FOR loop statement.
     *
     * @return the parsed ForStatement
     * @throws SyntaxErrorException if parsing fails
     */
    private Statement parseForLoop() throws SyntaxErrorException {
        Expression oStartValueExpression;
        Expression oEndValueExpression;
        Expression oStepSize;

        _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [FOR] ");
        final int iForPosition = _iPosition++;
        _oLineNumber.putLineNumber(getToken(0).getLine(), iForPosition);

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
            oStepSize = new RealValue(1);
        } else {
            _iPosition = _iPosition + 1;
            oStepSize = expression();
            _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [NUMBER] " + oStepSize.content());
        }

        Token oNextToken = findMatchingNext();
        _oLogger.debug("-parse-> followed Token: <" + oNextToken.getLine() + "> [NEXT]");

        try {
            return new ForStatement(iForPosition, strForVariable, oStartValueExpression,
                                   oEndValueExpression, oStepSize, oNextToken.getLine());
        } catch (Exception oException) {
            throw new SyntaxErrorException("Error parsing FOR loop: " + oException.getMessage());
        }
    }

    /**
     * Parse a WHILE loop statement.
     *
     * @return the parsed WhileLoopStatement
     * @throws SyntaxErrorException if parsing fails
     */
    private Statement parseWhileLoop() throws SyntaxErrorException {
        int iOrgPosition = _iPosition;
        _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
        _iPosition++;
        Expression oWhileCondition = expression();
        _oLogger.debug("-parse-> found Token: <" + (_iPosition - 1) + "> [WHILE]");
        Token oEndWhileToken = findMatchingToken(BasicTokenType.WHILE, BasicTokenType.ENDWHILE);
        _oLogger.debug("-parse-> followed Token: <" + oEndWhileToken.getLine() + "> [END-WHILE]");

        return new WhileStatement(iOrgPosition, oWhileCondition, oEndWhileToken.getLine());
    }

    /**
     * Parse statements within an IF block until ELSE or ENDIF is encountered.
     * The statements are collected and returned as a list.
     *
     * @param iBlockEndLine the line number where ELSE or ENDIF is expected
     * @return list of parsed statements
     * @throws SyntaxErrorException if parsing fails
     */
    private List<Statement> parseBlockStatements(final int iBlockEndLine) throws SyntaxErrorException {
        List<Statement> voBlockStatements = new ArrayList<>();

        while (_iPosition < _aoTokens.size()) {
            BasicTokenType eTokenType = getToken(0).getType();
            int iCurrentLine = getToken(0).getLine();

            // Stop if we hit ELSE or ENDIF at the correct line
            if ((eTokenType == BasicTokenType.ELSE || eTokenType == BasicTokenType.ENDIF)
                && iCurrentLine == iBlockEndLine) {
                break;
            }

            // Stop at EOP
            if (eTokenType == BasicTokenType.EOP) {
                break;
            }

            // Skip colons (statement separators)
            if (eTokenType == BasicTokenType.COLON) {
                _iPosition++;
                continue;
            }

            // Skip comments
            if (eTokenType == BasicTokenType.REM) {
                _iPosition++;
                continue;
            }

            // Parse statements based on token type
            Statement oStatement;

            try {
                switch (eTokenType) {
                    case PRINT:
                        oStatement = parsePrintStatement();
                        break;
                    case INPUT:
                        oStatement = parseInputStatement();
                        break;
                    case WORD:
                    case LET:
                        oStatement = parseWordStatement();
                        break;
                    case READ:
                        oStatement = parseReadStatement();
                        break;
                    case GOTO:
                        oStatement = parseGotoStatement();
                        break;
                    case GOSUB:
                        oStatement = parseGosubStatement();
                        break;
                    case FOR:
                        oStatement = parseForLoop();
                        break;
                    case WHILE:
                        oStatement = parseWhileLoop();
                        break;
                    case IF:
                        oStatement = parseIfStatement();
                        break;
                    case RETURN:
                        oStatement = parseReturnStatement();
                        break;
                    case ON:
                        oStatement = parseOnStatement();
                        break;
                    default:
                        // Phase 7 fix: file statements (FGET, FPUT, FOPEN ...), END, DATA and DIM inside a block
                        // were skipped, so their arguments were read as statements. They are parsed now.
                        if (isDelegatedBlockStatement(eTokenType)) {
                            voBlockStatements.addAll(parseDelegatedBlockStatement());
                            continue;
                        }
                        // Unknown statement type in block - skip it
                        _oLogger.debug("-parseBlockStatements-> skipping unknown token: " + eTokenType);
                        _iPosition++;
                        continue;
                }

                voBlockStatements.add(oStatement);
            } catch (SyntaxErrorException oException) {
                // Re-throw with block context
                throw new SyntaxErrorException("Error in block at line " + iCurrentLine + ": " + oException.getMessage());
            }
        }

        return voBlockStatements;
    }

    /**
     * Finds the NEXT that closes the FOR loop whose header has just been parsed.
     *
     * @return the NEXT token that matches the current FOR loop
     * @throws SyntaxErrorException if no matching NEXT exists
     */
    private Token findMatchingNext() throws SyntaxErrorException {
        return findMatchingToken(BasicTokenType.FOR, BasicTokenType.NEXT);
    }

    /**
     * Finds the closing token of a block that starts just before the current position. Blocks of the same kind that
     * are nested inside it are counted, so the outer block ends at its own closing token and not at the first one
     * (bug fixed in Phase 6: the first NEXT or ENDWHILE was used for every loop).
     *
     * @param oOpenType  token that opens a block, for example FOR or WHILE
     * @param oCloseType token that closes a block, for example NEXT or ENDWHILE
     * @return the closing token that matches the block
     * @throws SyntaxErrorException if no matching closing token exists
     */
    private Token findMatchingToken(final BasicTokenType eOpenType, final BasicTokenType eCloseType)
            throws SyntaxErrorException {
        int iDepth = 0;
        for (int iCurrentPosition = _iPosition; iCurrentPosition < _aoTokens.size(); iCurrentPosition++) {
            Token oToken = _aoTokens.get(iCurrentPosition);
            if (oToken.getType() == eOpenType) {
                iDepth++;
            } else if (oToken.getType() == eCloseType) {
                if (iDepth == 0) {
                    return oToken;
                }
                iDepth--;
            }
        }
        String strReservedWord = ReservedWords.getReservedWord(ReservedWords.getTokenIndex(eCloseType.toString()));
        throw new SyntaxErrorException("Missing statement " + strReservedWord + ".");
    }

    /**
     * Parse an inline statement after THEN (used for IF condition THEN statement).
     *
     * @return the parsed statement
     * @throws SyntaxErrorException if parsing fails
     */
    private Statement parseInlineStatement() throws SyntaxErrorException {
        BasicTokenType eTokenType = getToken(0).getType();

        return switch (eTokenType) {
            case PRINT -> parsePrintStatement();
            case INPUT -> parseInputStatement();
            case WORD, LET -> parseWordStatement();
            case READ -> parseReadStatement();
            case GOTO -> parseGotoStatement();
            case GOSUB -> parseGosubStatement();
            case RETURN -> parseReturnStatement();
            default -> throw new SyntaxErrorException("Unsupported statement after THEN: " + eTokenType);
        };
    }

    /**
     * Finds the ELSE or END-IF that belongs to the block IF whose header has just been parsed. Nested block IFs are
     * counted; a single-line IF has no END-IF and is not counted.
     *
     * @param oTarget ELSE or ENDIF
     * @return the position of the token in the token list, or -1 if it does not belong to this IF
     */
    private int findBlockIfPosition(final BasicTokenType eTarget) {
        int iDepth = 0;
        for (int iCurrentPosition = _iPosition; iCurrentPosition < _aoTokens.size(); iCurrentPosition++) {
            BasicTokenType eType = _aoTokens.get(iCurrentPosition).getType();
            if (eType == BasicTokenType.IF && isBlockIfAt(iCurrentPosition)) {
                iDepth++;
            } else if (eType == BasicTokenType.ENDIF) {
                if (iDepth == 0) {
                    return (eTarget == BasicTokenType.ENDIF) ? iCurrentPosition : -1;
                }
                iDepth--;
            } else if (eType == BasicTokenType.ELSE && iDepth == 0 && eTarget == BasicTokenType.ELSE) {
                return iCurrentPosition;
            }
        }
        return -1;
    }

    /**
     * Decides whether the IF token at the given position starts a block IF. A block IF has nothing after THEN on its
     * source line (its statements follow on the next lines). A single-line IF has its statement or line number after
     * THEN on the same line.
     *
     * @param iIfPosition position of the IF token in the token list
     * @return true if the IF starts a block with an END-IF
     */
    private boolean isBlockIfAt(final int iIfPosition) {
        int iLine = _aoTokens.get(iIfPosition).getLine();
        int iPosition = iIfPosition + 1;
        while (iPosition < _aoTokens.size() && _aoTokens.get(iPosition).getLine() == iLine
                && _aoTokens.get(iPosition).getType() != BasicTokenType.THEN) {
            iPosition++;
        }
        if (iPosition >= _aoTokens.size() || _aoTokens.get(iPosition).getType() != BasicTokenType.THEN) {
            return false;
        }
        int iAfterThen = iPosition + 1;
        return iAfterThen >= _aoTokens.size() || _aoTokens.get(iAfterThen).getLine() != iLine;
    }

    /**
     * Parse an IF statement (handles single-line, inline, and block IF).
     *
     * @return the parsed IfThenStatement
     * @throws SyntaxErrorException if parsing fails
     */
    private Statement parseIfStatement() throws SyntaxErrorException {
        int iOrgPosition = _iPosition;
        _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
        _iPosition++;
        Expression oCondition = expression();
        _oLogger.debug("-parse-> found Token: <" + (_iPosition - 1) + "> [IF]: <" + oCondition.content() + ">");
        String strLabel = consumeToken(BasicTokenType.THEN).getText();
        _oLogger.debug("-parse-> followed Token: <" + _iPosition + "> [THEN]: <" + strLabel + ">");

        // Case 1: Single-line IF with line number (IF condition THEN 100). The number must be on the same source line
        // as the IF, so that a block IF ("IF x THEN" at the end of its line) is not read as a jump.
        int iIfLine = _aoTokens.get(iOrgPosition).getLine();
        if (getToken(0).getType() == BasicTokenType.NUMBER && getToken(0).getLine() == iIfLine) {
            int iTargetLine = Integer.parseInt(getToken(0).getText());
            _iPosition++;
            return new IfThenStatement(oCondition, iOrgPosition, 0, 0, iTargetLine);
        // Case 2: Inline statement after THEN (IF condition THEN PRINT "msg"). The statement must be on the same source
        // line as the IF. The lexer emits no token for a line number, so a statement at the start of the next line
        // would look like an inline statement; such an IF is a block IF (bug fixed in Phase 6).
        } else if (isStatementKeyword(getToken(0).getType()) && getToken(0).getLine() == iIfLine) {
            _oLogger.debug("-parse-> found inline statement after THEN");
            List<Statement> voIfStatements = new ArrayList<>();
            voIfStatements.add(parseInlineStatement());
            return new IfThenStatement(oCondition, iOrgPosition, voIfStatements, new ArrayList<>(), 0);
        // Case 3: Block IF (IF condition THEN ... END-IF)
        } else {
            _oLogger.debug("-parse-> found block IF");
            // Nested block IFs are counted, so the ELSE and END-IF found belong to this IF (bug fixed in Phase 6)
            int iElseIndex = findBlockIfPosition(BasicTokenType.ELSE);
            int iEndIfIndex = findBlockIfPosition(BasicTokenType.ENDIF);
            if (iEndIfIndex < 0) {
                throw new SyntaxErrorException("Missing statement END-IF.");
            }
            Token oEndIfToken = _aoTokens.get(iEndIfIndex);
            _oLogger.debug("-parse-> found END-IF token at line: <" + oEndIfToken.getLine() + ">");

            Token oElseToken = (iElseIndex >= 0) ? _aoTokens.get(iElseIndex) : null;
            if (oElseToken != null) {
                _oLogger.debug("-parse-> found ELSE token at line: <" + oElseToken.getLine() + ">");
            }

            int iElseOrEndLine = (oElseToken != null) ? oElseToken.getLine() : oEndIfToken.getLine();
            List<Statement> voIfStatements = parseBlockStatements(iElseOrEndLine);

            List<Statement> voElseStatements = new ArrayList<>();
            if (oElseToken != null) {
                _iPosition = iElseIndex + 1;
                _oLogger.debug("-parse-> parsing ELSE block");
                voElseStatements = parseBlockStatements(oEndIfToken.getLine());
            }

            // Skip to END-IF
            _iPosition = iEndIfIndex + 1;
            _oLogger.debug("-parse-> skipped to END-IF");

            return new IfThenStatement(oCondition, iOrgPosition, voIfStatements, voElseStatements, oEndIfToken.getLine());
        }
    }

    /**
     * Check if a token type represents a statement keyword that can appear after THEN.
     *
     * @param oType the BasicTokenType to check
     * @return true if the token is a statement keyword, false otherwise
     */
    /**
     * Tells whether a statement inside a block is parsed by the normal statement parser (Phase 7). Loop ends and
     * block ends (NEXT, WEND, ELSE, END-IF) are not in this list, because the block parser handles them.
     *
     * @param oType the token type found at the current position
     * @return true when the statement is parsed by parseDelegatedBlockStatement
     */
    private boolean isDelegatedBlockStatement(final BasicTokenType eType) {
        return eType == BasicTokenType.FOPEN || eType == BasicTokenType.FCLOSE || eType == BasicTokenType.FINPUT
                || eType == BasicTokenType.CHDIR || eType == BasicTokenType.MKDIR || eType == BasicTokenType.RMDIR
                || eType == BasicTokenType.FPRINT || eType == BasicTokenType.FGET || eType == BasicTokenType.FPUT
                || eType == BasicTokenType.FPEEK || eType == BasicTokenType.FREWIND || eType == BasicTokenType.FRENAME
                || eType == BasicTokenType.FCOPY || eType == BasicTokenType.FDELETE || eType == BasicTokenType.END
                || eType == BasicTokenType.DATA || eType == BasicTokenType.DIM;
    }

    /**
     * Parses one statement inside a block with the normal parser. The statement ends at a colon, at ELSE or END-IF,
     * at the end of its line, or at the end of the program. The statement is parsed by a small parser that gets only
     * the tokens of this statement. Only statements without jumps are delegated, so the statement numbers of the
     * small parser are not used for jumps.
     *
     * @return the statements parsed from this part of the block (normally one)
     * @throws SyntaxErrorException if the statement is not correct
     */
    private List<Statement> parseDelegatedBlockStatement() throws SyntaxErrorException {
        int iStart = _iPosition;
        int iLine = getToken(0).getLine();
        int iEnd = iStart;

        while (iEnd < _aoTokens.size()) {
            Token oToken = _aoTokens.get(iEnd);
            if (oToken.getType() == BasicTokenType.EOP || oToken.getType() == BasicTokenType.COLON
                    || oToken.getType() == BasicTokenType.ELSE || oToken.getType() == BasicTokenType.ENDIF
                    || oToken.getLine() != iLine) {
                break;
            }
            iEnd++;
        }

        List<Token> voPart = new ArrayList<>(_aoTokens.subList(iStart, iEnd));
        voPart.add(new Token("", BasicTokenType.EOP, iLine));
        List<Statement> voParsed = new BasicParser(voPart, _bDartmouthFlag).parse();
        _iPosition = iEnd;
        return voParsed;
    }

    private boolean isStatementKeyword(final BasicTokenType eType) {
        return eType == BasicTokenType.PRINT || eType == BasicTokenType.INPUT || eType == BasicTokenType.LET
               || eType == BasicTokenType.WORD || eType == BasicTokenType.FOR || eType == BasicTokenType.WHILE
               || eType == BasicTokenType.DO || eType == BasicTokenType.GOTO || eType == BasicTokenType.GOSUB
               || eType == BasicTokenType.RETURN || eType == BasicTokenType.READ || eType == BasicTokenType.DATA
               || eType == BasicTokenType.DIM || eType == BasicTokenType.REM || eType == BasicTokenType.FOPEN
               || eType == BasicTokenType.FCLOSE || eType == BasicTokenType.FINPUT || eType == BasicTokenType.FPRINT;
    }

    /**
     * Find a token of a specific type on the next line(s), without changing position.
     *
     * @deprecated Not nesting-aware: it finds the first matching token. Replaced by findBlockIfPosition
     * (block IF, Phase 6). Kept, not deleted, per project rules.
     *
     * @param oType the token type to find
     * @return the found token
     * @throws SyntaxErrorException if token not found
     */
    @Deprecated
    private Token findTokenOnNextLine(final BasicTokenType eType) throws SyntaxErrorException {
        int iCurrentPosition = _iPosition;

        while (iCurrentPosition < _aoTokens.size()) {
            Token oToken = _aoTokens.get(iCurrentPosition);

            if (oToken.getType() == eType) {
                return oToken;
            }

            iCurrentPosition++;
        }

        throw new SyntaxErrorException("Missing statement " + ReservedWords.getReservedWord(ReservedWords.getTokenIndex(eType.toString())));
    }

    /**
     * Find the position of a token of a specific type without changing the parser's position.
     *
     * @deprecated Not nesting-aware: it finds the first matching token. Replaced by findBlockIfPosition
     * (block IF, Phase 6). Kept, not deleted, per project rules.
     *
     * @param oType the token type to find
     * @return the index of the token
     * @throws SyntaxErrorException if token not found
     */
    @Deprecated
    private int findTokenPosition(final BasicTokenType eType) throws SyntaxErrorException {
        int iCurrentPosition = _iPosition;

        while (iCurrentPosition < _aoTokens.size()) {
            Token oToken = _aoTokens.get(iCurrentPosition);

            if (oToken.getType() == eType) {
                return iCurrentPosition;
            }

            iCurrentPosition++;
        }

        throw new SyntaxErrorException("Missing statement " + ReservedWords.getReservedWord(ReservedWords.getTokenIndex(eType.toString())));
    }
}

