package eu.gricom.basic.codeGenerator;

import eu.gricom.basic.memoryManager.Program;
import eu.gricom.basic.statements.Statement;
import eu.gricom.basic.helper.Logger;
import java.util.List;
import org.json.JSONObject;

public class JSONCodeGenerator {
    private final List<Statement> _aoPreRunStatements;
    private final List<Statement> _aoStatements;
    private String _strProgram;


    public JSONCodeGenerator(String strProgram, Program oProgram) {
        _aoPreRunStatements = oProgram.getPreRunStatements();
        _aoStatements = oProgram.getStatements();
        _strProgram = strProgram;
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
}
