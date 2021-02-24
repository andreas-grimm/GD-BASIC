package eu.gricom.interpreter.basic.functions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import eu.gricom.interpreter.basic.variableTypes.RealValue;
import org.junit.jupiter.api.Test;

public class RndTest {

    @Test
    public void testRnd() {

        try {
            RealValue oResult = (RealValue) Rnd.execute();

            assertTrue(oResult.toReal() > 0.0);
            assertTrue(oResult.toReal() < 1.0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
