package eu.gricom.interpreter.basic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenTest {

    Token oToken = new Token("TestToken", TokenType.STRING);

    @Test
    public void testGetText() {
        String strReturn = oToken.getText();
        assertTrue(strReturn.matches("TestToken"));
    }

    @Test
    public void testGetType() {
        TokenType oReturn = oToken.getType();
        assertTrue(oReturn.equals(TokenType.STRING));
    }
}
