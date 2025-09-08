package eu.gricom.basic.codeGenerator.java;

import eu.gricom.basic.helper.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

public class GenerateJavaCode {
    private Logger _oLogger = new Logger(this.getClass().getName());

    public GenerateJavaCode() {
    }

    public void generate(String strJSONProgram, String strJavaProgramName, String strCompileTemplate) {
        _oLogger.debug("Creating Java Code");
        String strJSONCode = "private final String strJSONProgram = " + "\"" + strJSONProgram + "\";";
        _oLogger.debug("Java Code:         "  + strJSONCode);
        _oLogger.info("Java Program Name: "  + strJavaProgramName);

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

        generatePomFile(strCompileTemplate, strJavaProgramName);
    }

    public void generatePomFile(String strCompileTemplate, String strJavaProgramName) {
        _oLogger.debug("Creating POM File");
        String strPOMTemplate = strCompileTemplate.replace("compile.template", "compile.pom");
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
