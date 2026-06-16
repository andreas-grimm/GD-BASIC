package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.BooleanValue;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

import java.io.File;

/**
 * DirExists.java
 * <p>
 * Description: The DirExists class implements the BASIC function to check for the existence of a directory in the working directory,
 * which returns a boolean being true if the directory exists, and false in any other case.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class DirExists {

    /**
     * Private Constructor.
     */
    private DirExists() {
    }

    /**
     * Functions implemented here are similar to Statements with the difference
     * that they actually return a result to the caller of type Value. The method execute
     * triggers the function.
     *
     * @param oValue input value (expects StringValue containing directory path)
     * @return BooleanValue true if directory exists, false otherwise
     * @throws RuntimeException if input is not a StringValue
     */
    public static Value execute(final Value oValue) throws Exception {
        if (oValue instanceof StringValue) {
            StringValue strValue = (StringValue) oValue;
            File oDir = new File(strValue.toString());
            return new BooleanValue(oDir.isDirectory());
        }

        throw new RuntimeException("Input value not of type String: " + oValue);
    }

}
