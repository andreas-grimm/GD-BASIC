package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

/**
 * ChDir.java
 * <p>
 * Description: The ChDir class implements the BASIC function to change the current working directory.
 * It updates the file manager's current directory to the specified path, allowing subsequent file operations
 * to use the new directory as their base path.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class ChDir {

    /**
     * Private Constructor.
     */
    private ChDir() {
    }

    /**
     * Changes the current working directory to the specified path.
     * Updates the FileManager's current directory setting, which is used as the base path
     * for subsequent file operations (OPEN, CLOSE, INPUT#, PRINT#). The directory path is
     * stored but not validated for existence at the time of the call.
     *
     * @param oValue input value (expects StringValue containing the new directory path)
     * @throws RuntimeException if input is not a StringValue
     */
    public static void execute(final Value oValue) throws Exception {
        if (oValue instanceof StringValue) {
            StringValue strValue = (StringValue) oValue;
            FileManager oFileManager = new FileManager();
            oFileManager.setCurrentDirectory(strValue.toString());
            return;
        }

        throw new RuntimeException("Input value not of type String: " + oValue);
    }
}
