package eu.gricom.basic.functions;
import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.variableTypes.BooleanValue;
import eu.gricom.basic.variableTypes.Value;

import java.io.File;

/**
 * FExists.java
 * <p>
 * Description: The FIsOpen class implements the BASIC function to check whether a file has been opened,
 * which returns a boolean being true if the file is currently opened, and false in any other case.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FIsOpen {

    /**
     * Private Constructor.
     */
    private FIsOpen() {
    }

    /**
     * The function returns true if the file with the ID given in the parameter has been opened.
     * If the file has not been opened, the return value is false.
     *
     * @param iFileID as int input value
     * @return BooleanValue true if file exists, false otherwise
     */
    public static Value execute(final int iFileID) {
        FileManager oFileManager = new FileManager();
        return new BooleanValue(oFileManager.getFileStatus(iFileID));
    }
}
