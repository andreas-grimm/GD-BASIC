package eu.gricom.basic.codeGenerator;

import eu.gricom.basic.codeGenerator.java.ObjectCodeGenerator;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.memoryManager.Program;

import java.io.PrintWriter;

public class Generator {
    private static String _strObjectName = "";

    /**
     * Create and store the object code.
     * Return the name of the program loaded.
     *
     * @param oProgram the parsed program to be stored.
     */
    public static void createObjectCode(Program oProgram) {
        Logger oLogger = new Logger("eu.gricom.basic.codeGenerator.Generator.");

        String strObjectCode;
        String strProgramName = oProgram.getProgramName();

        oLogger.info("Loaded program: " + strProgramName);
        if (strProgramName.endsWith(".bas")) {
            _strObjectName = strProgramName.replace(".bas", ".json");
        } else {
            if (!strProgramName.endsWith(".basic")) {
                _strObjectName = strProgramName.replace(".basic", ".json");
            } else {
                _strObjectName = strProgramName.concat(".json");
            }
        }
        oLogger.info("Name of object file: " + _strObjectName);
        strObjectCode = "{ \"" + _strObjectName + "\": [";

        ObjectCodeGenerator oObjectCodeGenerator = new ObjectCodeGenerator(oProgram);
        strObjectCode += oObjectCodeGenerator.create();
        strObjectCode += "]}";

        if (strObjectCode != null) {
            oLogger.info(strObjectCode);
        }

        try {
            PrintWriter out = new PrintWriter(_strObjectName);
            out.println(strObjectCode);
            out.close();
        } catch (Exception eException) {
            oLogger.error("Cannot generate file, error: " + eException.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Create and store the target Java code.
     * Return the name of the program loaded.
     *
     */
    public static void createJavaCode() {
        Logger oLogger = new Logger("eu.gricom.basic.codeGenerator.Generator,createJavaCode");

        String strJavaProgramName = _strObjectName.replace(".json", ".comp.java");
        oLogger.info("Name of target Java file: " + strJavaProgramName);
    }
}
