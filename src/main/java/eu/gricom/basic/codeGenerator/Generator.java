package eu.gricom.basic.codeGenerator;

import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.memoryManager.Program;

import java.io.PrintWriter;

public class Generator {
    private static String _strObjectName = "";

    /**
     * Generates JSON code from the given parsed program and writes it to a file.
     *
     * The output filename is derived from the program's name by replacing `.bas` or `.basic` extensions with `.json`, or appending `.json` if neither is present. The generated JSON code is written to this file. If an error occurs during file writing, the program logs the error and terminates.
     *
     * @param oProgram the parsed program to generate JSON code from
     * @param bBeautified whether the generated JSON should be formatted for readability
     */
    public static void createJSONCode(Program oProgram, boolean bBeautified) {
        Logger oLogger = new Logger("eu.gricom.basic.codeGenerator.Generator.createJSONCode");
        boolean bGenerateObjectCode = true;

        String strJSONCode = "";
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

        JSONCodeGenerator oJSONCodeGenerator = new JSONCodeGenerator(_strObjectName, oProgram);
        strJSONCode += oJSONCodeGenerator.create(bBeautified);
//        strJSONCode += "}";

        if (strJSONCode != null) {
            oLogger.info(strJSONCode);
        }

        try {
            PrintWriter out = new PrintWriter(_strObjectName);
            out.println(strJSONCode);
            out.close();
        } catch (Exception eException) {
            oLogger.error("Cannot generate file, error: " + eException.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Generates object code from the provided Program instance by delegating to the ObjectCodeGenerator.
     *
     * @param oProgram the parsed Program object to generate object code from
     */
    public static void createObjectCode(Program oProgram) {
        Logger oLogger = new Logger("eu.gricom.basic.codeGenerator.Generator.createObjectCode");

        ObjectCodeGenerator.createObjectCode(oProgram);
    }

        /**
     * Logs the intended Java output file name derived from the current object file name.
     *
     * The method replaces the `.json` extension in the stored object file name with `.comp.java` and logs the resulting Java file name.
     */
    public static void createJavaCode() {
        Logger oLogger = new Logger("eu.gricom.basic.codeGenerator.Generator.createJavaCode");

        String strJavaProgramName = _strObjectName.replace(".json", ".comp.java");
        oLogger.info("Name of target Java file: " + strJavaProgramName);
    }
}
