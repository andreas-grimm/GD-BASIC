package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.memoryManager.LineNumberXRef;
import eu.gricom.interpreter.basic.memoryManager.ProgramPointer;
import eu.gricom.interpreter.basic.memoryManager.Stack;
import eu.gricom.interpreter.basic.memoryManager.VariableManagement;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ForStatementTest {

    @Test
    public void testExecuteUpwards() throws Exception {
        Stack oStack = new Stack();
        ProgramPointer oProgramPointer = new ProgramPointer();
        VariableManagement oVariableManager = new VariableManagement();

        RealValue oStart = new RealValue(0);
        RealValue oEnd = new RealValue(1);
        RealValue oStep = new RealValue(1);
        LineNumberXRef oLineNumber = new LineNumberXRef();

        oLineNumber.putStatementNumber(5, 5);
        oLineNumber.putLineNumber(5, 5);
        oLineNumber.putStatementNumber(10, 10);
        oLineNumber.putLineNumber(10, 10);
        oLineNumber.putStatementNumber(15, 15);
        oLineNumber.putLineNumber(15, 15);

        ForStatement oForStatement = new ForStatement(5, "X#", oStart, oEnd, oStep, 10);

        // after the execute, there should be a X real in the system with the value of 0
        oForStatement.execute();

        RealValue oXVariable = (RealValue) oVariableManager.getMap("X#");

        assertTrue(oXVariable.toReal() == 0.0);
        oStack.pop();
        // after the second execution, X should have a value of 1.0
        oForStatement.execute();

        oXVariable = (RealValue) oVariableManager.getMap("X#");

        assertTrue(oXVariable.toReal() == 1.0);
        oStack.pop();

        // after the third execution, X should have a value of 2.0 and the program pointer should be set to 10
        oForStatement.execute();

        oXVariable = (RealValue) oVariableManager.getMap("X#");

        assertTrue(oXVariable.toReal() == 1.0);
    }

    @Test
    public void testExecuteDownwards() throws Exception {
        Stack oStack = new Stack();
        ProgramPointer oProgramPointer = new ProgramPointer();
        VariableManagement oVariableManager = new VariableManagement();

        RealValue oStart = new RealValue(1);
        RealValue oEnd = new RealValue(0);
        RealValue oStep = new RealValue(-1);

        LineNumberXRef oLineNumber = new LineNumberXRef();

        oLineNumber.putStatementNumber(5, 5);
        oLineNumber.putLineNumber(5, 5);
        oLineNumber.putStatementNumber(10, 10);
        oLineNumber.putLineNumber(10, 10);
        oLineNumber.putStatementNumber(15, 15);
        oLineNumber.putLineNumber(15, 15);

        ForStatement oForStatement = new ForStatement(5, "X#", oStart, oEnd, oStep, 10);

        // after the execute, there should be a X real in the system with the value of 0
        oForStatement.execute();

        RealValue oXVariable = (RealValue) oVariableManager.getMap("X#");

        assertTrue(oXVariable.toReal() == 1.0);
        oStack.pop();
        // after the second execution, X should have a value of 1.0
        oForStatement.execute();

        oXVariable = (RealValue) oVariableManager.getMap("X#");

        assertTrue(oXVariable.toReal() == 0.0);
        oStack.pop();

        // after the third execution, X should have a value of 2.0 and the program pointer should be set to 10
        oForStatement.execute();

        oXVariable = (RealValue) oVariableManager.getMap("X#");

        assertTrue(oXVariable.toReal() == 0.0);
    }
}
