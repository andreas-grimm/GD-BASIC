package eu.gricom.basic.codeGenerator;

import eu.gricom.basic.codeGenerator.java.JavaGenerator;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.memoryManager.Program;
import eu.gricom.basic.statements.Statement;
import java.util.List;

public class Generator {

    private final transient Logger _oLogger = new Logger(this.getClass().getName());

    public static void create(Program oProgram, String strLanguage) {
        System.out.println(oProgram.getProgram());

        for (Statement oStatement: oProgram.getStatements()) {
            try {
                System.out.println(oStatement.content());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        JavaGenerator oJavaGenerator = new JavaGenerator(oProgram);
        oJavaGenerator.create();
    }
}
