package eu.gricom.basic.codeGenerator.JSON;

import com.google.gson.JsonObject;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.tokenizer.BasicTokenType;
import eu.gricom.basic.tokenizer.Token;

public class TokenDeCoder {
    public static Token decode(JsonObject oTokenData) {
        Logger oLogger = new Logger("eu.gricom.basic.codeGenerator.JSON.TokenDeCoder.decode(object)");
        JsonObject oValue = oTokenData.get("TOKEN").getAsJsonObject();

        int iLineNr = oValue.get("LINE_NR").getAsInt();
        int iCommandSeqNr = oValue.get("COMMAND_SEQUENCE_NUMBER").getAsInt();
        BasicTokenType oTokenType = BasicTokenType.valueOf(oValue.get("TYPE").getAsString());
        String strText = oValue.get("TEXT").getAsString();

        oLogger.debug("Found Token: Line Number" + iLineNr + ", Text: " + strText);

        return new Token(strText, oTokenType, iLineNr, iCommandSeqNr);
    }
}
