package eu.gricom.basic.tokenizer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenTest {

    private final transient Token _oToken = new Token("TestToken", BasicTokenType.STRING, 0);

    @Test
    public void testGetText() {
        String strReturn = _oToken.getText();
        assertEquals(strReturn, "TestToken");
    }

    @Test
    public void testGetType() {
        BasicTokenType oReturn = _oToken.getType();
        assertEquals(oReturn, BasicTokenType.STRING);
    }
}
