package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.Value;

public final class Eof {

    /**
     * Private Constructor.
     */
    private Eof() {
    }

    /**
     * Functions implemented here are similar to Statements with the difference
     * that they actually return a result to the caller of type Value. The method execute
     * triggers the function.
     *
     * @param oValue input value
     * @return Value the return message of the function
     * @throws Exception as any execution error found during execution
     */
    public static IntegerValue execute(final Value oValue) throws Exception {
        if (oValue instanceof IntegerValue) {
            FileManager oFileManager = new FileManager();

            return oFileManager.getEOF(((IntegerValue) oValue).toInt());
        }
        if (oValue instanceof eu.gricom.basic.variableTypes.RealValue) {
             FileManager oFileManager = new FileManager();

             return oFileManager.getEOF((int)((eu.gricom.basic.variableTypes.RealValue) oValue).toReal());
        }

        throw new RuntimeException("Input value not of type Numeric: " + oValue);
    }
}
