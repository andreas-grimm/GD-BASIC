package eu.gricom.interpreter.basic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenTest {

    Token oToken = new Token("TestToken", TokenType.STRING);

    @Test
    public void testGetText() {
        String strReturn = oToken.getText();
        assertEquals(strReturn, "TestToken");
    }

    @Test
    public void testGetType() {
        TokenType oReturn = oToken.getType();
        assertEquals(oReturn, TokenType.STRING);
    }
}
