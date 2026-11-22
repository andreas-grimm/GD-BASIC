package eu.gricom.basic.statements;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.memoryManager.ProgramPointer;
import eu.gricom.basic.variableTypes.BooleanValue;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;
import eu.gricom.basic.memoryManager.LineNumberXRef;
import eu.gricom.basic.memoryManager.Stack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class UntilStatementTest {

    @Test
    public void testEndLoop() throws SyntaxErrorException {
        ProgramPointer oProgramPointer = new ProgramPointer();
        LineNumberXRef oLineNumberObject = new LineNumberXRef();
        Stack oStack = new Stack();

        oStack.push(new IntegerValue(5));
        oLineNumberObject.putStatementNumber(5, 5);

        oProgramPointer.setCurrentStatement(10);

        RealValue oValue = new RealValue(10);

        try {
            OperatorExpression oExpression = new OperatorExpression(oValue, "==", oValue);

            UntilStatement oStatement = new UntilStatement(20, oExpression);

            oStatement.execute();

            int iNewLabel = oProgramPointer.getCurrentStatement();

            assertEquals(iNewLabel, 10);
        } catch (Exception eException) {
            fail();
        }
    }

    /** Returns an expression that always gives the same value (used as an UNTIL condition). */
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
     * Runs an UNTIL with the given condition value. The DO is statement 5 and the UNTIL is at statement 10.
     * Returns the statement the program pointer is on afterwards (10 = loop ends, 5 = jump back to DO).
     */
    private static int runUntil(final Value oCondition) throws Exception {
        LineNumberXRef oLineNumberObject = new LineNumberXRef();
        oLineNumberObject.putStatementNumber(5, 5);

        Stack oStack = new Stack();
        oStack.push(new IntegerValue(5));

        ProgramPointer oProgramPointer = new ProgramPointer();
        oProgramPointer.setCurrentStatement(10);

        new UntilStatement(20, constant(oCondition)).execute();
        return oProgramPointer.getCurrentStatement();
    }

    // Positive: a non-zero number is TRUE, so the loop ends (pointer stays on the UNTIL).
    @Test
    public void testUntilNumericNonZeroIsTrue() throws Exception {
        assertEquals(10, runUntil(new RealValue(3)));
        assertEquals(10, runUntil(new RealValue(-1)));
    }

    // Positive: zero is FALSE, so the loop repeats (pointer jumps back to the DO on statement 5).
    @Test
    public void testUntilNumericZeroIsFalse() throws Exception {
        assertEquals(5, runUntil(new RealValue(0)));
    }

    // Positive: boolean conditions still work as before.
    @Test
    public void testUntilBooleanConditionUnchanged() throws Exception {
        assertEquals(10, runUntil(new BooleanValue(true)));
        assertEquals(5, runUntil(new BooleanValue(false)));
    }

    // Negative: a text condition is not a number or a boolean, so it is an error.
    @Test
    public void testUntilStringConditionIsError() {
        assertThrows(SyntaxErrorException.class, () -> runUntil(new StringValue("abc")));
    }

    @Test
    public void testContinueLoop() throws SyntaxErrorException {
        ProgramPointer oProgramPointer = new ProgramPointer();
        LineNumberXRef oLineNumberObject = new LineNumberXRef();
        Stack oStack = new Stack();

        oStack.push(new IntegerValue(5));
        oLineNumberObject.putStatementNumber(5, 5);

        oProgramPointer.setCurrentStatement(10);

        RealValue oValue = new RealValue(10);

        try {
            OperatorExpression oExpression = new OperatorExpression(oValue, ">", oValue);

            UntilStatement oStatement = new UntilStatement(20, oExpression);

            oStatement.execute();

            int iNewLabel = oProgramPointer.getCurrentStatement();

            assertEquals(iNewLabel, 5);
        } catch (Exception eException) {
            fail();
        }
    }
}
