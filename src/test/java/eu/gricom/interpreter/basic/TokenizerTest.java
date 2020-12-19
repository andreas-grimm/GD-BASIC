package eu.gricom.interpreter.basic;

import eu.gricom.interpreter.basic.helper.FileHandler;
import eu.gricom.interpreter.basic.helper.Logger;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenizerTest {
    private Logger _oLogger = new Logger(this.getClass().getName());

    @Test
    public void testTokenize() {
        // disable the logger
        _oLogger.setLogLevel("");

        String strReadText = FileHandler.readFile("src/test/resources/testfile.bas");

        List<Token> aoTokens = Tokenizer.tokenize(strReadText);

        //TODO: Compare the expected lists with the generated list...
        int iCounter = 0;
        for (Token oToken: aoTokens) {
            System.out.println(iCounter + "-->" + oToken.getText() + "<-->" + oToken.getType().toString());
            iCounter++;
        }

        assertEquals(iCounter, 35);
    }
}
