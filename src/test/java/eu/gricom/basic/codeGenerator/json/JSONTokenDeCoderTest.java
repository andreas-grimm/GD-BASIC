package eu.gricom.basic.codeGenerator.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import eu.gricom.basic.tokenizer.Token;
import org.junit.jupiter.api.Test;

public class JSONTokenDeCoderTest {
    @Test
    public void testProcessSingleToken() {
        JsonElement oJsonElement;
        Token oToken;

        String strJsonString = "{\"TOKEN\": {\"LINE_NR\": \"40\",\"TYPE\": \"REM\",\"COMMAND_SEQUENCE_NUMBER\": \"1\",\"TEXT\": \"rem stop looping if we are done\"}}";
        Gson oGsonProcessor = new Gson();
        try {
            oJsonElement = oGsonProcessor.fromJson(strJsonString, JsonElement.class);
            oToken = TokenDecoder.decode((JsonObject) oJsonElement);

            assert oToken.structure().equals(strJsonString);
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing json string: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
