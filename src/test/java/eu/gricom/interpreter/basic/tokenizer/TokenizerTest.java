package eu.gricom.interpreter.basic.tokenizer;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.helper.FileHandler;
import eu.gricom.interpreter.basic.helper.Logger;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenizerTest {
    private Logger _oLogger = new Logger(this.getClass().getName());

    @Test
    public void testTokenizeForJasic() {
        // disable the logger
        try {
            _oLogger.setLogLevel("");

            String strReadText = FileHandler.readFile("src/test/resources/test_jasic_1.bas");
            Tokenizer oTokenizer = new JasicLexer();

            List<Token> aoTokens = oTokenizer.tokenize(strReadText);

            //TODO: Compare the expected lists with the generated list...
            int iCounter = 0;
            for (Token oToken : aoTokens) {
                System.out.println(iCounter + "-->" + oToken.getText() + "<-->" + oToken.getType().toString());
                iCounter++;
            }

            assertEquals(iCounter, 35);
        } catch (SyntaxErrorException e) {
            assertTrue(false);
        }
    }
}
