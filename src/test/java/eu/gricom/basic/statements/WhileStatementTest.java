package eu.gricom.basic.statements;

import eu.gricom.basic.variableTypes.BooleanValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;
import eu.gricom.basic.error.EmptyStackException;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.memoryManager.LineNumberXRef;
import eu.gricom.basic.memoryManager.ProgramPointer;
import eu.gricom.basic.memoryManager.Stack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class WhileStatementTest {

    @Test
    public void testWhileLoop() throws EmptyStackException {
        Stack oStack = new Stack();

        ProgramPointer oProgramPointer = new ProgramPointer();
        LineNumberXRef oLineNumberObject = new LineNumberXRef();

        oLineNumberObject.putLineNumber(6, 1);
        oLineNumberObject.putStatementNumber(1, 6);

        oLineNumberObject.putLineNumber(7, 2);
        oLineNumberObject.putStatementNumber(2, 7);

        oProgramPointer.setCurrentStatement(4);

        RealValue oLeftValue = new RealValue(2);

        try {
            OperatorExpression oExpression = new OperatorExpression(oLeftValue, "==", oLeftValue);

            WhileStatement oStatement = new WhileStatement(1, oExpression, 6);

            oStatement.execute();

            int iNewLabel = oProgramPointer.getCurrentStatement();

            assertEquals(iNewLabel, 4);
        } catch (Exception eException) {
            System.out.println(eException.getMessage());
            fail();
        }

        oStack.pop();
    }

    /** Returns an expression that always gives the same value (used as a WHILE condition). */
    private static Expression constant(final Value oValue) {
        return new Expression() {
            @Override
            public Value evaluate() {
                return oValue;
            }

            @Override
            public String content() {
                return oValue.toString();
            }

            @Override
            public String structure() {
                return "{}";
            }
        };
    }

    /**
     * Runs a WHILE with the given condition value. The body starts at statement 4 and the statement after
     * END-WHILE is statement 7. Returns the statement the program pointer is on afterwards.
     */
    private static int runWhile(final Value oCondition) throws Exception {
        LineNumberXRef oLineNumberObject = new LineNumberXRef();
        oLineNumberObject.putLineNumber(6, 1);
        oLineNumberObject.putStatementNumber(1, 6);
        oLineNumberObject.putLineNumber(7, 2);
        oLineNumberObject.putStatementNumber(2, 7);

        ProgramPointer oProgramPointer = new ProgramPointer();
        oProgramPointer.setCurrentStatement(4);

        new WhileStatement(1, constant(oCondition), 6).execute();
        int iStatement = oProgramPointer.getCurrentStatement();

        // A TRUE condition pushes a return position on the shared static Stack. Remove it so that other tests
        // (for example NextStatementTest) still see an empty stack.
        if (iStatement == 4) {
            new Stack().pop();
        }
        return iStatement;
    }

    // Positive: a non-zero number is TRUE, so the body runs (pointer stays on the body).
    @Test
    public void testWhileNumericNonZeroIsTrue() throws Exception {
        assertEquals(4, runWhile(new RealValue(5)));
        assertEquals(4, runWhile(new RealValue(-2)));
        assertEquals(4, runWhile(new RealValue(0.5)));
    }

    // Positive: zero is FALSE, so the loop ends (pointer moves past END-WHILE).
    @Test
    public void testWhileNumericZeroIsFalse() throws Exception {
        assertEquals(7, runWhile(new RealValue(0)));
    }

    // Positive: boolean conditions still work as before.
    @Test
    public void testWhileBooleanConditionUnchanged() throws Exception {
        assertEquals(4, runWhile(new BooleanValue(true)));
        assertEquals(7, runWhile(new BooleanValue(false)));
    }

    // Negative: a text condition is not a number or a boolean, so it is an error.
    @Test
    public void testWhileStringConditionIsError() {
        assertThrows(SyntaxErrorException.class, () -> runWhile(new StringValue("abc")));
    }

    @Test
    public void testEndWhileLoop() throws EmptyStackException {
        Stack oStack = new Stack();

        ProgramPointer oProgramPointer = new ProgramPointer();
        LineNumberXRef oLineNumberObject = new LineNumberXRef();

        oLineNumberObject.putLineNumber(6, 1);
        oLineNumberObject.putStatementNumber(1, 6);

        oLineNumberObject.putLineNumber(7, 2);
        oLineNumberObject.putStatementNumber(2, 7);

        oProgramPointer.setCurrentStatement(4);

        RealValue oLeftValue = new RealValue(2);

        try {
            OperatorExpression oExpression = new OperatorExpression(oLeftValue, ">", oLeftValue);

            WhileStatement oStatement = new WhileStatement(1, oExpression, 6);

            oStatement.execute();

            int iNewLabel = oProgramPointer.getCurrentStatement();

            assertEquals(iNewLabel, 7);
        } catch (Exception eException) {
            System.out.println(eException.getMessage());
            fail();
        }
    }
}
