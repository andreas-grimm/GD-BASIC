package eu.gricom.basic.functions;

import eu.gricom.basic.variableTypes.BooleanValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

import java.io.File;

/**
 * FExists.java
 * <p>
 * Description: The FExists class implements the BASIC function to check for the existance of a file in the working directory,
 * which returns a boolean being true if the file exists, and false in any other case.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class FExists {

    /**
     * Private Constructor.
     */
    private FExists() {
    }

    /**
     * The function returns true if the file with the name given in the parameter exists.
     * If the file does not exist or is not accessible, the return value is false.
     *
     * @param oValue input value (expects StringValue containing file name)
     * @return BooleanValue true if file exists, false otherwise
     * @throws RuntimeException if input is not a StringValue
     */
    public static Value execute(final Value oValue) throws Exception {
        if (oValue instanceof StringValue) {
            StringValue strValue = (StringValue) oValue;
            File oFile = new File(strValue.toString());
            return new BooleanValue(oFile.exists());
        }

        throw new RuntimeException("Input value not of type String: " + oValue);
    }

}
