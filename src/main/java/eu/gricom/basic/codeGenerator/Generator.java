package eu.gricom.basic.codeGenerator;

import eu.gricom.basic.codeGenerator.JSON.JSONCodeGenerator;
import eu.gricom.basic.codeGenerator.JSON.JSONDecoder;
import eu.gricom.basic.codeGenerator.java.GenerateJavaCode;
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
    public static String createJSONCode(Program oProgram, boolean bBeautified, boolean bStoreJSONObject) {
        Logger oLogger = new Logger("eu.gricom.basic.codeGenerator.Generator.createJSONCode");

        String strJSONCode = "";
        String strProgramName = oProgram.getProgramName();

        oLogger.debug("Loaded program: " + strProgramName);
        if (strProgramName.endsWith(".bas")) {
            _strObjectName = strProgramName.replace(".bas", ".json");
        } else {
            if (!strProgramName.endsWith(".basic")) {
                _strObjectName = strProgramName.replace(".basic", ".json");
            } else {
                _strObjectName = strProgramName.concat(".json");
            }
        }
        oLogger.debug("Name of object file: " + _strObjectName);

        JSONCodeGenerator oJSONCodeGenerator = new JSONCodeGenerator(_strObjectName, oProgram);
        strJSONCode += oJSONCodeGenerator.create(bBeautified);

        oLogger.debug(strJSONCode);

        if (bStoreJSONObject) {
            try {
                PrintWriter out = new PrintWriter(_strObjectName);
                out.println(strJSONCode);
                out.close();
            } catch (Exception eException) {
                oLogger.error("Cannot generate file, error: " + eException.getMessage());
                System.exit(-1);
            }
        }

        return strJSONCode;
    }

    public static void createObjectCode(Program oProgram) {
        Logger oLogger = new Logger("eu.gricom.basic.codeGenerator.Generator.createObjectCode");

        ObjectCodeGenerator.createObjectCode(oProgram);
    }

        /**
         * Create and store the target Java code.
         * Return the name of the program loaded.
         *
         */
    public static void createJavaCode(String strJSONCode, String strCompileTemplate) {
        Logger oLogger = new Logger("eu.gricom.basic.codeGenerator.Generator.createJavaCode");

        String strJavaProgramName = _strObjectName.replace(".json", ".comp.java");
        oLogger.debug("Name of target Java file: " + strJavaProgramName);
        oLogger.debug("Name of target Java file: " + strJavaProgramName);
        oLogger.debug("Content of the JSON Program: " + strJSONCode);

        GenerateJavaCode oGenerateJavaCode = new GenerateJavaCode();
        oGenerateJavaCode.generate(strJSONCode, strJavaProgramName, strCompileTemplate);
    }

    public static Program createProgramObject(String strJSONProgram) {
        Logger oLogger = new Logger("eu.gricom.basic.codeGenerator.Generator.createProgram");

        JSONDecoder oGenerateProgram = new JSONDecoder(strJSONProgram);
        return (oGenerateProgram.decode());
    }
}
