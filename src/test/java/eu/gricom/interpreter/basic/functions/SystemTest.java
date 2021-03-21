package eu.gricom.interpreter.basic.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

public class SystemTest {

    @Test
    public void testSystem() {

        try {
            StringValue oCommand = new StringValue("RUN");
            StringValue oProgram = new StringValue("/bin/ls pom.xml");

            StringValue oResult = (StringValue) System.execute(oCommand, oProgram);

            assertEquals("pom.xml\n", oResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testValWithException() {

        try {
            RealValue oValue = new RealValue(-1);

            assertThrows(RuntimeException.class, () -> {
                Val.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
