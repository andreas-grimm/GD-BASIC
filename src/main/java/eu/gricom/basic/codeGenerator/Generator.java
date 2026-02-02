package eu.gricom.basic.codeGenerator;

import eu.gricom.basic.codeGenerator.JSON.JSONCodeGenerator;
import eu.gricom.basic.codeGenerator.JSON.JSONDecoder;
import eu.gricom.basic.codeGenerator.java.GenerateJavaCode;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.memoryManager.Program;

import java.io.PrintWriter;

/**
 * Generator.java
 * <p>
 * Description: The Generator class serves as the central coordinator for code generation. It provides methods to
 * create JSON intermediate representation of parsed BASIC programs and to generate executable Java source code from
 * the intermediate representation.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
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

    /**
     * Create and store the target Java code.
     * Return the name of the program loaded.
     *
     */
    public static void createCode(String strBASICProgramName, String strBASICCode, String strCompileTemplate) {
        Logger oLogger = new Logger("eu.gricom.basic.codeGenerator.Generator.createJavaCode");

        if (strBASICProgramName.endsWith(".bas")) {
            _strObjectName = strBASICProgramName.replace(".bas", ".comp.java");
        } else {
            if (!strBASICProgramName.endsWith(".basic")) {
                _strObjectName = strBASICProgramName.replace(".basic", ".comp.java");
            } else {
                _strObjectName = strBASICProgramName.concat(".comp.java");
            }
        }
        oLogger.debug("Name of object file: " + _strObjectName);


        String strJavaProgramName = _strObjectName;
        oLogger.debug("Name of target Java file: " + strJavaProgramName);

        GenerateJavaCode oGenerateJavaCode = new GenerateJavaCode();
        oGenerateJavaCode.generate(strBASICCode, strJavaProgramName, strCompileTemplate);
    }
}
