package eu.gricom.basic.codeGenerator.java;

import eu.gricom.basic.memoryManager.Program;
import eu.gricom.basic.statements.Statement;
import java.util.List;

public class JavaGenerator {
    private final List<Statement> _aoPreRunStatements;
    private final List<Statement> _aoStatements;


    public JavaGenerator (Program oProgram) {
        _aoPreRunStatements = oProgram.getPreRunStatements();
        _aoStatements = oProgram.getStatements();
    }

    public void create() {

    }
}
