package eu.gricom.basic.codeGenerator.java;

import eu.gricom.basic.memoryManager.Program;
import eu.gricom.basic.statements.Statement;
import eu.gricom.basic.helper.Logger;
import java.util.List;
import org.json.JSONObject;

public class ObjectCodeGenerator {
    private final List<Statement> _aoPreRunStatements;
    private final List<Statement> _aoStatements;


    public ObjectCodeGenerator(Program oProgram) {
        _aoPreRunStatements = oProgram.getPreRunStatements();
        _aoStatements = oProgram.getStatements();
    }

    public String create() {
        Logger oLogger = new Logger(this.getClass().getName());
        StringBuilder strBasicProgram = new StringBuilder();

        strBasicProgram.append("{\"PROGRAM\": [ \n");
        for (Statement oStatement : _aoStatements) {
            try {
                strBasicProgram.append(oStatement.structure());
                strBasicProgram.append(",\n");
            } catch (Exception eException) {
                oLogger.error("Compiler error: " + eException.getMessage());
                System.exit(1);
            }
        }
        strBasicProgram.deleteCharAt(strBasicProgram.length()-2);
        strBasicProgram.append(" ] }\n");

        // Beautify the object file
        try {
            JSONObject oJSONObject = new JSONObject(strBasicProgram.toString());
            return oJSONObject.toString(4);
        }  catch (Exception eException) {
            oLogger.error("Internal compiler error: " + eException.getMessage());
            System.exit(-1);
        }

        return strBasicProgram.toString();
    }
}
