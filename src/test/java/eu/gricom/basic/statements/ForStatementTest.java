package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.ProgramPointer;
import eu.gricom.basic.memoryManager.Stack;
import eu.gricom.basic.memoryManager.VariableManagement;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.memoryManager.LineNumberXRef;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ForStatementTest.java
 * <p>
 * Description: Tests the FOR statement. Each call to execute() is one arrival at the FOR line: the first call starts
 * the loop, each NEXT jump is a further call, and the call that finds the loop finished leaves the loop. A FOR
 * statement reached again after its loop has finished starts a new loop.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
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

        ForStatement oForStatement = new ForStatement(5, "X", oStart, oEnd, oStep, 10);

        // first call (loop start): X = 0
        oForStatement.execute();
        assertTrue(((RealValue) oVariableManager.getMap("X")).toReal() == 0.0);
        oStack.pop();

        // second call (NEXT jump): X = 1
        oForStatement.execute();
        assertTrue(((RealValue) oVariableManager.getMap("X")).toReal() == 1.0);
        oStack.pop();

        // third call (loop finished): X stays 1 and the program pointer moves past the loop
        oForStatement.execute();
        assertTrue(((RealValue) oVariableManager.getMap("X")).toReal() == 1.0);

        // fourth call (a new entry into the FOR line): the loop starts again at the start value 0
        oForStatement.execute();
        assertTrue(((RealValue) oVariableManager.getMap("X")).toReal() == 0.0);
        oStack.pop();
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

        ForStatement oForStatement = new ForStatement(5, "X", oStart, oEnd, oStep, 10);

        // first call (loop start): X = 1
        oForStatement.execute();
        assertTrue(((RealValue) oVariableManager.getMap("X")).toReal() == 1.0);
        oStack.pop();

        // second call (NEXT jump): X = 0
        oForStatement.execute();
        assertTrue(((RealValue) oVariableManager.getMap("X")).toReal() == 0.0);
        oStack.pop();

        // third call (loop finished): X stays 0
        oForStatement.execute();
        assertTrue(((RealValue) oVariableManager.getMap("X")).toReal() == 0.0);

        // fourth call (a new entry into the FOR line): the loop starts again at the start value 1
        oForStatement.execute();
        assertTrue(((RealValue) oVariableManager.getMap("X")).toReal() == 1.0);
        oStack.pop();
    }

    @Test
    public void testFinishedLoopIsStartedAgainWhenReachedByOuterLoop() throws Exception {
        // Models the reported bug: an inner FOR is reached once per outer pass. After its first loop has
        // finished, the second arrival must run the loop again instead of skipping it.
        Stack oStack = new Stack();
        VariableManagement oVariableManager = new VariableManagement();
        LineNumberXRef oLineNumber = new LineNumberXRef();
        oLineNumber.putStatementNumber(5, 5);
        oLineNumber.putLineNumber(5, 5);
        oLineNumber.putStatementNumber(10, 10);
        oLineNumber.putLineNumber(10, 10);

        ForStatement oForStatement = new ForStatement(5, "Y", new RealValue(1), new RealValue(2),
                new RealValue(1), 10);

        // first pass of the outer loop: start (Y = 1), NEXT jump (Y = 2), finish
        oForStatement.execute();
        oStack.pop();
        oForStatement.execute();
        oStack.pop();
        oForStatement.execute();
        assertTrue(((RealValue) oVariableManager.getMap("Y")).toReal() == 2.0);

        // second pass of the outer loop: the inner loop must start again at Y = 1
        oForStatement.execute();
        assertTrue(((RealValue) oVariableManager.getMap("Y")).toReal() == 1.0);
        oStack.pop();
    }
}
