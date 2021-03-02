package eu.gricom.interpreter.basic.tokenizer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenTest {

    private final transient Token _oToken = new Token("TestToken", TokenType.STRING, 0);

    @Test
    public void testGetText() {
        String strReturn = _oToken.getText();
        assertEquals(strReturn, "TestToken");
    }

    @Test
    public void testGetType() {
        TokenType oReturn = _oToken.getType();
        assertEquals(oReturn, TokenType.STRING);
    }
}
