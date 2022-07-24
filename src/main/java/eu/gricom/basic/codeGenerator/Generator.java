package eu.gricom.basic.codeGenerator;

import eu.gricom.basic.codeGenerator.java.JavaGenerator;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.memoryManager.Program;
import java.util.List;

public class Generator {

    private final transient Logger _oLogger = new Logger(this.getClass().getName());

    public static void create(Program oProgram) {
        JavaGenerator oJavaGenerator = new JavaGenerator(oProgram);
        oJavaGenerator.create();
    }
}
