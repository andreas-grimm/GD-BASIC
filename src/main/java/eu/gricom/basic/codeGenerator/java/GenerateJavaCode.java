package eu.gricom.basic.codeGenerator.java;

import eu.gricom.basic.helper.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * GenerateJavaCode.java
 * <p>
 * Description: The GenerateJavaCode class transpiles BASIC programs into Java source code. It uses a template file to
 * generate a complete Java class that embeds the BASIC program as JSON and includes the necessary runtime support for
 * execution.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class GenerateJavaCode {
    private Logger _oLogger = new Logger(this.getClass().getName());

    public GenerateJavaCode() {
    }

    public void generate(String strBASICProgram, String strJavaProgramName, String strCompileTemplate) {
        _oLogger.debug("Creating Java Code");
        String strBASICCode = "private final String strJSONProgram = " + "\"" + strBASICProgram + "\";";
        _oLogger.debug("Java Code:         "  + strBASICCode);
        _oLogger.info("Java Program Name: "  + strJavaProgramName);

        /*
        try {
            BufferedReader oBufferedReader = new BufferedReader(new FileReader(strCompileTemplate));
            PrintWriter oOutput = new PrintWriter(strJavaProgramName);

            for (String strTemplateLine; (strTemplateLine = oBufferedReader.readLine()) != null; ) {
                if (strTemplateLine.contains("private final String strJSONProgram = \"\";")) {
                    strTemplateLine = strJSONCode;
                }
                oOutput.println(strTemplateLine);
            }

            oOutput.close();
            oBufferedReader.close();
        } catch (FileNotFoundException eFileNotFound) {
            _oLogger.error("Template file not found, error: " + eFileNotFound.getMessage());
            System.exit(-1);
        } catch (Exception eException) {
            _oLogger.error("Cannot generate file, error: " + eException.getMessage());
            System.exit(-1);
        }
        */

        generatePomFile(strCompileTemplate, strJavaProgramName);
    }

    public void generatePomFile(String strCompileTemplate, String strJavaProgramName) {
        _oLogger.debug("Creating POM File");
        String strPOMTemplate = strCompileTemplate.replace("compile.java", "compile.pom");
        String strPOMFileName = strJavaProgramName.substring(0,strJavaProgramName.lastIndexOf("/")) + "/pom.xml";
        _oLogger.debug("POM Filename :" + strPOMFileName);

        try {
            BufferedReader oBufferedReader = new BufferedReader(new FileReader(strPOMTemplate));
            PrintWriter oOutput = new PrintWriter(strPOMFileName);

            for (String strTemplateLine; (strTemplateLine = oBufferedReader.readLine()) != null; ) {
//                if (strTemplateLine.contains("private final String strJSONProgram = \"\";")) {
//                    strTemplateLine = strJSONCode;
//                }
                oOutput.println(strTemplateLine);
            }

            oOutput.close();
            oBufferedReader.close();
        } catch (FileNotFoundException eFileNotFound) {
            _oLogger.error("Template file not found, error: " + eFileNotFound.getMessage());
            System.exit(-1);
        } catch (Exception eException) {
            _oLogger.error("Cannot generate file, error: " + eException.getMessage());
            System.exit(-1);
        }
    }
}
