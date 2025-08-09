package eu.gricom.basic.codeGenerator;

import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.memoryManager.Program;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ObjectCodeGenerator {

    public static void createObjectCode(Program oProgram) {
        Logger oLogger = new Logger("eu.gricom.basic.codeGenerator.ObjectCodeGenerator");

        String strProgramName = oProgram.getProgramName();
        String strObjectName;

        oLogger.info("Loaded program: " + strProgramName);

        if (strProgramName.endsWith(".bas")) {
            strObjectName = strProgramName.replace(".bas", ".obj");
        } else {
            if (!strProgramName.endsWith(".basic")) {
                strObjectName = strProgramName.replace(".basic", ".obj");
            } else {
                strObjectName = strProgramName.concat(".obj");
            }
        }
        oLogger.info("Name of object file: " + strObjectName);

        // export
        try (FileOutputStream fileOut = new FileOutputStream(strObjectName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(oProgram);
            System.out.println("Serialized data is saved " + strObjectName);
        } catch (IOException eException) {
            throw new RuntimeException(eException);
        }
    }
}
