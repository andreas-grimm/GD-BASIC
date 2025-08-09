package eu.gricom.basic.codeGenerator.java;

import eu.gricom.basic.helper.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class GenerateJavaCode {
    public GenerateJavaCode(String strJSONProgram) {
    }

    public void generate(String strJSONProgram) {
        Logger oLogger = new Logger(this.getClass().getName());

        try {
            JSONObject oJSONObject = new JSONObject(strJSONProgram.toString());
        } catch (JSONException eJSONException) {
            oLogger.error("JSON parser error: " + eJSONException.getMessage());
        }
    }
}
