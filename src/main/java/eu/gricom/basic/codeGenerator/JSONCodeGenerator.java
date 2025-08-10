package eu.gricom.basic.codeGenerator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import eu.gricom.basic.memoryManager.Program;
import eu.gricom.basic.statements.Statement;
import eu.gricom.basic.helper.Logger;

import java.util.List;
import java.util.Set;

import org.json.JSONObject;

public class JSONCodeGenerator {
    private final List<Statement> _aoPreRunStatements;
    private final List<Statement> _aoStatements;
    private String _strProgram;

// constructor to generate JSON file
    public JSONCodeGenerator(String strProgram, Program oProgram) {
        _aoPreRunStatements = oProgram.getPreRunStatements();
        _aoStatements = oProgram.getStatements();
        _strProgram = strProgram;
    }

// constructor to generate a Program
    public JSONCodeGenerator(String strProgram) {
        Logger oLogger = new Logger(this.getClass().getName());
        _strProgram = strProgram;
        _aoPreRunStatements = null;
        _aoStatements = null;
    }

    public String create(boolean bBeautified) {
        Logger oLogger = new Logger(this.getClass().getName());

        // cut the name of the program
        _strProgram = _strProgram.substring(_strProgram.lastIndexOf('/') + 1);

        // Run the setting for the macro settings, such as "DEF FN"
        StringBuilder strJSONProgram = new StringBuilder("{ \"" + _strProgram + "\": ");
        strJSONProgram.append("[{\"SETTINGS\": [");
        for (Statement oStatement : _aoPreRunStatements) {
            try {
                strJSONProgram.append(oStatement.structure());
                strJSONProgram.append(",");
            } catch (Exception eException) {
                oLogger.error("Compiler error: " + eException.getMessage());
                System.exit(1);
            }
        }
        strJSONProgram.deleteCharAt(strJSONProgram.length()-2);

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
        strJSONProgram.deleteCharAt(strJSONProgram.length()-1);
        strJSONProgram.append("]},{\"TOKEN\": [");
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

    public Program transferJSONCodeToProgram() {
        Logger oLogger = new Logger(this.getClass().getName());

        Program oProgram = new Program();
        JsonObject oJSONFile = JsonParser.parseString(_strProgram).getAsJsonObject();
        Set<String> aoListOfFileName = oJSONFile.keySet();

        // Process each file in the JSON (ignoring the first level key)
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
                    processToken(oTokenComponent);
                }
            } else {
                oLogger.error("Expected JSON array but found: " + oFileElement.getClass().getSimpleName());
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
        JsonArray programArray = oProgramComponent.getAsJsonArray("PROGRAM");
        oLogger.info("PROGRAM array size: " + programArray.size());

        // Process each statement in the program
        for (JsonElement statement : programArray) {
            if (statement.isJsonObject()) {
                JsonObject statementObj = statement.getAsJsonObject();
                processStatement(statementObj);
            }
        }
    }

    private void processToken(JsonObject oTokenComponent) {
        Logger oLogger = new Logger(this.getClass().getName());
        oLogger.info("Processing TOKEN component");
        JsonArray aoTokenArray = oTokenComponent.getAsJsonArray("TOKEN");
        oLogger.info("TOKEN array size: " + aoTokenArray.size());
        // Add your TOKEN processing logic here
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
                case "REM":
                    processRemStatement(oStatementData);
                    break;
                case "PRINT":
                    processPrintStatement(oStatementData);
                    break;
                case "ASSIGN":
                    processAssignStatement(oStatementData);
                    break;
                case "GOTO":
                    processGotoStatement(oStatementData);
                    break;
                case "IF":
                    processIfStatement(oStatementData);
                    break;
                case "END":
                    processEndStatement(oStatementData);
                    break;
                default:
                    oLogger.error("Unknown statement type: " + oStatementType);
            }
        }
    }

    private void processRemStatement(JsonObject oRemData) {
        Logger oLogger = new Logger(this.getClass().getName());
        String tokenNr = oRemData.get("TOKEN_NR").getAsString();
        String remText = oRemData.get("REM_TEXT").getAsString();
        oLogger.info("REM statement - Token: " + tokenNr + ", Text: " + remText);
    }

    private void processPrintStatement(JsonObject oPrintData) {
        Logger oLogger = new Logger(this.getClass().getName());
        String tokenNr = oPrintData.get("TOKEN_NR").getAsString();
        String crlf = oPrintData.get("CRLF").getAsString();
        JsonElement expression = oPrintData.get("EXPRESSION");
        oLogger.info("PRINT statement - Token: " + tokenNr + ", CRLF: " + crlf);
    }

    private void processAssignStatement(JsonObject oAssignData) {
        Logger oLogger = new Logger(this.getClass().getName());
        String tokenNr = oAssignData.get("TOKEN_NR").getAsString();
        String name = oAssignData.get("NAME").getAsString();
        JsonElement expression = oAssignData.get("EXPRESSION");
        oLogger.info("ASSIGN statement - Token: " + tokenNr + ", Variable: " + name);
    }

    private void processGotoStatement(JsonObject oGotoData) {
        Logger oLogger = new Logger(this.getClass().getName());
        String tokenNr = oGotoData.get("TOKEN_NR").getAsString();
        String target = oGotoData.get("TARGET").getAsString();
        oLogger.info("GOTO statement - Token: " + tokenNr + ", Target: " + target);
    }

    private void processIfStatement(JsonObject oIfData) {
        Logger oLogger = new Logger(this.getClass().getName());
        String tokenNr = oIfData.get("TOKEN_NR").getAsString();
        String targetLine = oIfData.get("TARGET_LINE").getAsString();
        String elseStatement = oIfData.get("ELSE_STATEMENT").getAsString();
        String endIf = oIfData.get("END_IF").getAsString();
        oLogger.info("IF statement - Token: " + tokenNr + ", Target: " + targetLine + ", End: " + endIf);
    }

    private void processEndStatement(JsonObject oEndData) {
        Logger oLogger = new Logger(this.getClass().getName());
        String tokenNr = oEndData.get("TOKEN_NR").getAsString();
        oLogger.info("END statement - Token: " + tokenNr);
    }
}
