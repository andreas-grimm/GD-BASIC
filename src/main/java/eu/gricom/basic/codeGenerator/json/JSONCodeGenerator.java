package eu.gricom.basic.codeGenerator.json;

import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.memoryManager.Program;
import eu.gricom.basic.statements.*;
import eu.gricom.basic.tokenizer.Token;
import org.json.JSONObject;

import java.util.List;

public class JSONCodeGenerator {
    private final List<Statement> _aoPreRunStatements;
    private final List<Statement> _aoStatements;
    private final List<Token> _aoTokens;
    private String _strProgram;

    // constructor to generate json file
    public JSONCodeGenerator(String strProgram, Program oProgram) {
        _aoPreRunStatements = oProgram.getPreRunStatements();
        _aoStatements = oProgram.getStatements();
        _aoTokens = oProgram.getTokens();
        _strProgram = strProgram;
    }

    public String create(boolean bBeautified) {
        Logger oLogger = new Logger(this.getClass().getName());

        // cut the name of the program
        _strProgram = _strProgram.substring(_strProgram.lastIndexOf('/') + 1);

        // Run the setting for the macro settings, such as "DEF FN"
        StringBuilder strJSONProgram = new StringBuilder("{ \"" + _strProgram + "\": [");
        strJSONProgram.append("{\"SETTINGS\": [");
        for (Statement oStatement : _aoPreRunStatements) {
            try {
                strJSONProgram.append(oStatement.structure());
                strJSONProgram.append(",");
            } catch (Exception eException) {
                oLogger.error("Compiler error: " + eException.getMessage());
                System.exit(1);
            }
        }
        strJSONProgram.deleteCharAt(strJSONProgram.length() - 2);

        strJSONProgram.append("]},{\"PROGRAM\": [");

        for (Statement oStatement : _aoStatements) {
            try {
                strJSONProgram.append(oStatement.structure());
                strJSONProgram.append(",");
            } catch (Exception eException) {
                oLogger.error("Compiler error: " + eException.getMessage());
                System.exit(1);
            }
        }
        strJSONProgram.deleteCharAt(strJSONProgram.length() - 1);

        strJSONProgram.append("]},{\"TOKENS\": [");
        for (Token oToken : _aoTokens) {
            try {
                strJSONProgram.append(oToken.structure());
                strJSONProgram.append(",");
            } catch (Exception eException) {
                oLogger.error("Compiler error: " + eException.getMessage());
                System.exit(1);
            }
        }
        strJSONProgram.deleteCharAt(strJSONProgram.length() - 1);

        strJSONProgram.append("]}]}");

        // Beautify the object file
        if (bBeautified) {
            try {
                JSONObject oJSONObject = new JSONObject(strJSONProgram.toString());
                return oJSONObject.toString(4);
            } catch (Exception eException) {
                oLogger.error("Internal compiler error: " + eException.getMessage());
                System.exit(-1);
            }
        }

        return strJSONProgram.toString();
    }
/*
    public Program transferJSONCodeToProgram() {
        Logger oLogger = new Logger(this.getClass().getName());

        Program oProgram = new Program();
        JsonObject oJSONFile = JsonParser.parseString(_strProgram).getAsJsonObject();
        Set<String> aoListOfFileName = oJSONFile.keySet();

        // Process each file in the json (ignoring the first level key)
        for (String strFileName : aoListOfFileName) {
            oLogger.info("Reading file: " + strFileName);

            // Get the array containing the three components
            JsonElement oFileElement = oJSONFile.get(strFileName);
            if (oFileElement.isJsonArray()) {
                JsonArray componentsArray = oFileElement.getAsJsonArray();

                // Extract the three components: SETTINGS, PROGRAM, TOKEN
                JsonObject oSettingsComponent = null;
                JsonObject oProgramComponent = null;
                JsonObject oTokenComponent = null;

                for (JsonElement component : componentsArray) {
                    if (component.isJsonObject()) {
                        JsonObject componentObj = component.getAsJsonObject();

                        if (componentObj.has("SETTINGS")) {
                            oSettingsComponent = componentObj;
                            oLogger.info("Found SETTINGS component");
                        } else if (componentObj.has("PROGRAM")) {
                            oProgramComponent = componentObj;
                            oLogger.info("Found PROGRAM component");
                        } else if (componentObj.has("TOKEN")) {
                            oTokenComponent = componentObj;
                            oLogger.info("Found TOKEN component");
                        }
                    }
                }

                // Process each separated component
                if (oSettingsComponent != null) {
                    processSettings(oSettingsComponent);
                }

                if (oProgramComponent != null) {
                    processProgram(oProgramComponent);
                }

                if (oTokenComponent != null) {
                    oProgram.setTokens(processToken(oTokenComponent));
                }
            } else {
                oLogger.error("Expected json array but found: " + oFileElement.getClass().getSimpleName());
            }
        }

        return oProgram;
    }

    private void processSettings(JsonObject oSettingsComponent) {
        Logger oLogger = new Logger(this.getClass().getName());
        oLogger.info("Processing settings");
        JsonArray settingsArray = oSettingsComponent.getAsJsonArray("SETTINGS");
        oLogger.info("SETTINGS array size: " + settingsArray.size());
        // Add your SETTINGS processing logic here
    }

    private void processProgram(JsonObject oProgramComponent) {
        Logger oLogger = new Logger(this.getClass().getName());
        oLogger.info("Processing PROGRAM component");
        JsonArray oProgramArray = oProgramComponent.getAsJsonArray("PROGRAM");
        oLogger.info("PROGRAM array size: " + oProgramArray.size());

        // Process each statement in the program
        for (JsonElement oStatement : oProgramArray) {
            if (oStatement.isJsonObject()) {
                JsonObject statementObj = oStatement.getAsJsonObject();
                processStatement(statementObj);
            }
        }
    }

    private List<Token> processToken(JsonObject oTokenComponent) {
        Logger oLogger = new Logger(this.getClass().getName());
        oLogger.info("Processing TOKEN component");

        JsonArray aoJSONTokenArray = oTokenComponent.getAsJsonArray("TOKENS");
        List<Token> aoTokenList = new ArrayList<>();

        oLogger.info("TOKEN array size: " + aoJSONTokenArray.size());
        // Process each statement in the program
        for (JsonElement oTokenElement : aoJSONTokenArray) {
            if (oTokenElement.isJsonObject()) {
                JsonObject oTokenObj = oTokenElement.getAsJsonObject();
                Token oToken = TokenDecoder.decode(oTokenObj);
                aoTokenList.add(oToken);
            }
        }

        return aoTokenList;
    }

    private void processStatement(JsonObject oStatement) {
        Logger oLogger = new Logger(this.getClass().getName());
        // Process individual statements from the PROGRAM array
        Set<String> aoStatementTypes = oStatement.keySet();
        for (String oStatementType : aoStatementTypes) {
            System.out.println("Processing statement type: " + oStatementType);
            JsonObject oStatementData = oStatement.getAsJsonObject(oStatementType);

            // Add specific processing logic for each statement type
            switch (oStatementType) {
                case "ASSIGN":
                    processAssignStatement(oStatementData);
                    break;
                case "END":
                    processEndStatement(oStatementData);
                    break;
                case "GOTO":
                    processGotoStatement(oStatementData);
                    break;
                case "IF":
                    processIfStatement(oStatementData);
                    break;
                case "PRINT":
                    processPrintStatement(oStatementData);
                    break;
                case "REM":
                    processRemStatement(oStatementData);
                    break;
                default:
                    oLogger.error("Unknown statement type: " + oStatementType);
            }
        }
    }

    public Statement processRemStatement(JsonObject oRemData) {
        Logger oLogger = new Logger(this.getClass().getName());

        JsonObject oValue = oRemData.get("REM").getAsJsonObject();

        int iTokenNr = oValue.get("TOKEN_NR").getAsInt();
        String strRemText = oValue.get("REM_TEXT").getAsString();
        oLogger.debug("REM statement - Token: " + iTokenNr + ", REM Text: " + strRemText);

        return new RemStatement(oValue.get("TOKEN_NR").getAsInt(), oValue.get("REM_TEXT").getAsString());
    }

    public Statement processPrintStatement(JsonObject oPrintData) {
        Logger oLogger = new Logger(this.getClass().getName());

        JsonObject oValue = oPrintData.get("PRINT").getAsJsonObject();

        int iTokenNr = oValue.get("TOKEN_NR").getAsInt();
        boolean bCRLF = true;

        Expression oExpression = null;
        List<Expression> aoExpression = new ArrayList<>();

        if (oValue.get("EXPRESSION").isJsonPrimitive()) {
            oExpression = ExpressionDecoder.expressionDeCoder(oValue.getAsJsonPrimitive("EXPRESSION"));
        } else {
            oExpression = ExpressionDecoder.expressionDeCoder(oValue.getAsJsonObject("EXPRESSION"));
        }

        aoExpression.add(oExpression);
        oLogger.info("PRINT statement - Token: " + iTokenNr + ", CRLF: " + bCRLF + ", EXPRESSION: " + aoExpression);

        return new PrintStatement(iTokenNr, aoExpression, bCRLF);
    }

    public Statement processAssignStatement(JsonObject oAssignData) {
        Logger oLogger = new Logger(this.getClass().getName());
        Expression oExpression = null;
        JsonObject oValue = oAssignData.get("ASSIGN").getAsJsonObject();

        int iTokenNr = oValue.get("TOKEN_NR").getAsInt();
        String strName = oValue.get("NAME").getAsString();

        if (oValue.get("EXPRESSION").isJsonPrimitive()) {
            JsonPrimitive oPrimitive = oValue.getAsJsonPrimitive("EXPRESSION");

            if (oPrimitive.isString()) {
                oExpression = new StringValue(oPrimitive.getAsString());
            } else if (oPrimitive.isNumber()) {
                if (strName.endsWith("#")) {
                    oExpression = new RealValue(oValue.get("EXPRESSION").getAsDouble());
                } else {
                    oExpression = new IntegerValue(oValue.get("EXPRESSION").getAsInt());
                }
            }
            oLogger.debug("ASSIGN statement - Token: " + iTokenNr + ", Name: " + strName + ", Expression: " + oExpression);

            return new AssignStatement(oValue.get("TOKEN_NR").getAsInt(), oValue.get("NAME").getAsString(), oExpression);
        } else if (!oValue.get("EXPRESSION").isJsonObject()) {
            oLogger.error("Ill-formated json Assign Statement - Token: " + oValue.get("TOKEN_NR").getAsInt());
        }

        oExpression = ExpressionDecoder.expressionDeCoder((JsonObject) oValue.get("EXPRESSION"));
        return new AssignStatement(oValue.get("TOKEN_NR").getAsInt(), oValue.get("NAME").getAsString(), oExpression);
    }

    public Statement processGotoStatement(JsonObject oGotoData) {
        Logger oLogger = new Logger(this.getClass().getName());

        JsonObject oValue = oGotoData.get("GOTO").getAsJsonObject();

        int iTokenNr = oValue.get("TOKEN_NR").getAsInt();
        String strTarget = oValue.get("TARGET").getAsString();
        oLogger.debug("GOTO statement - Token: " + iTokenNr + ", Target: " + strTarget);

        return new GotoStatement(oValue.get("TOKEN_NR").getAsInt(), oValue.get("TARGET").getAsString());
    }

    public Statement processIfStatement(JsonObject oIfData) {
        Logger oLogger = new Logger(this.getClass().getName());
        JsonObject oValue = oIfData.get("IF").getAsJsonObject();

        int iTokenNr = oValue.get("TOKEN_NR").getAsInt();
        int iTargetLine = oValue.get("TARGET_LINE").getAsInt();
        int iEndIfLine = oValue.get("END_IF").getAsInt();
        int iElseStatementLine = oValue.get("ELSE_STATEMENT").getAsInt();

        oLogger.info("IF statement - Token: " + iTokenNr + ", Target: " + iTargetLine + ", End: " + iEndIfLine);

        Expression oCondition = ExpressionDecoder.conditionDeCoder(oValue.get("CONDITION").getAsJsonObject());

        if (oCondition == null) {
            oLogger.error("Condition object is null");
            System.exit(-1);
        }

        return new IfThenStatement(oCondition, iTokenNr, iElseStatementLine, iEndIfLine, iTargetLine);
    }

    public Statement processEndStatement(JsonObject oEndData) {
        Logger oLogger = new Logger(this.getClass().getName());
        JsonObject oValue = oEndData.get("END").getAsJsonObject();

        int iTokenNr = oValue.get("TOKEN_NR").getAsInt();
        oLogger.debug("END statement - Token: " + iTokenNr);

        return new EndStatement(oValue.get("TOKEN_NR").getAsInt());
    }
*/
}

