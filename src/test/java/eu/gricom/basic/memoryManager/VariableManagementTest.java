package eu.gricom.basic.memoryManager;

import eu.gricom.basic.variableTypes.BcdValue;
import eu.gricom.basic.variableTypes.BooleanValue;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.error.OverflowException;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VariableManagementTest {

    @Test
    @Order(1)
    public void testVariableStorage() throws SyntaxErrorException, OverflowException, RuntimeException {
        VariableManagement oVariableManagement = new VariableManagement();

        oVariableManagement.putMap("Integer%", 999);
        oVariableManagement.putMap("String$", "TestValue");

        try {
            IntegerValue oResult = (IntegerValue) oVariableManagement.getMap("Integer%");
            assertEquals(oResult.toInt(), 999);

            StringValue strResult = (StringValue) oVariableManagement.getMap("String$");
            assertTrue(strResult.toString().matches("TestValue"));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    public void testHashSuffixStoresBcdFromRealValue() throws Exception {
        // Positive: a name with '#' holds a BCD value; a REAL value is converted
        VariableManagement vm = new VariableManagement();

        vm.putMap("amount#", new RealValue(1234.56));
        Value oResult = vm.getMap("amount#");
        assertTrue(oResult instanceof BcdValue);
        assertEquals("1234.56", oResult.toString());
    }

    @Test
    @Order(3)
    public void testHashSuffixStoresBcdFromDouble() throws Exception {
        // Positive: the double overload also stores a BCD value
        VariableManagement vm = new VariableManagement();

        vm.putMap("myVar#", 3.5);
        assertEquals("3.5", vm.getMap("myVar#").toString());
        assertTrue(vm.mapContainsKey("myVar#"));
    }

    @Test
    public void testHashSuffixRejectsText() {
        // Negative: text cannot be stored in a '#' name
        VariableManagement vm = new VariableManagement();

        assertThrows(SyntaxErrorException.class, () -> vm.putMap("amount#", new StringValue("abc")));
    }

    @Test
    public void testHashSuffixRejectsTrueOrFalse() {
        // Negative: TRUE or FALSE cannot be stored in a '#' name
        VariableManagement vm = new VariableManagement();

        assertThrows(SyntaxErrorException.class, () -> vm.putMap("amount#", new BooleanValue(true)));
    }

    @Test
    public void testHashSuffixArrayIsRejected() {
        // Negative: BCD arrays are not implemented, so N#(I) must be an error and not one shared scalar
        VariableManagement vm = new VariableManagement();

        // The array key format used by ArrayAssignStatement and ArrayAccessExpression is NAME-index
        assertThrows(SyntaxErrorException.class, () -> vm.putMap("N#-3", new RealValue(1.0)));
        assertThrows(SyntaxErrorException.class, () -> vm.getMap("N#-3"));
    }

    @Test
    public void testHashSuffixOverflowIsReported() {
        // Negative: a number with more than 40 digits before the decimal point overflows
        VariableManagement vm = new VariableManagement();

        assertThrows(OverflowException.class, () -> vm.putMap("amount#", 1e50));
    }

    @Test
    @Order(4)
    public void testUntypedVariableStoredAsReal() throws SyntaxErrorException, OverflowException, RuntimeException {
        VariableManagement vm = new VariableManagement();

        vm.putMap("x", new RealValue(3.14159));
        Value result = vm.getMap("x");
        assertNotNull(result);
        if (result != null) {
            assertEquals(3.14159, result.toReal(), 0.00001);
        }
    }

    @Test
    @Order(5)
    public void testPutMapDoubleAcceptsUntyped() throws SyntaxErrorException, OverflowException, RuntimeException {
        VariableManagement vm = new VariableManagement();

        // Should NOT throw exception
        vm.putMap("pi", 3.14159);
        Value result = vm.getMap("pi");
        assertNotNull(result);
        if (result != null) {
            assertEquals(3.14159, result.toReal(), 0.00001);
        }
    }

    @Test
    @Order(6)
    public void testPutMapDoubleZeroGivesTrueInExclamationName() throws SyntaxErrorException, OverflowException, RuntimeException {
        // Decision A11 (as stated by the owner): a number equal to 0 gives TRUE in a '!' name
        VariableManagement vm = new VariableManagement();

        vm.putMap("zero!", 0.0);
        assertTrue(((BooleanValue) vm.getMap("zero!")).toBoolean());
    }

    @Test
    public void testPutMapDoubleNonZeroGivesFalseInExclamationName() throws SyntaxErrorException, OverflowException, RuntimeException {
        // Decision A11 (as stated by the owner): any number other than 0 gives FALSE in a '!' name
        VariableManagement vm = new VariableManagement();

        vm.putMap("nonzero!", 2.71828);
        assertFalse(((BooleanValue) vm.getMap("nonzero!")).toBoolean());
    }

    @Test
    public void testPutMapStringIntoExclamationNameFails() {
        // Negative: text cannot be stored in a '!' name
        VariableManagement vm = new VariableManagement();

        assertThrows(SyntaxErrorException.class, () -> vm.putMap("flag!", new StringValue("text")));
    }

    @Test
    public void testPutMapBooleanIntoExclamationName() throws SyntaxErrorException, OverflowException, RuntimeException {
        // Positive: TRUE stored in a '!' name is read back as a boolean
        VariableManagement vm = new VariableManagement();

        vm.putMap("flag!", new BooleanValue(true));
        Value result = vm.getMap("flag!");
        assertTrue(result instanceof BooleanValue);
        assertTrue(((BooleanValue) result).toBoolean());
    }

    @Test
    public void testPutMapRealValueZeroIntoExclamationNameGivesTrue() throws SyntaxErrorException, OverflowException, RuntimeException {
        // A RealValue of 0 is converted the same way as a double 0 (TRUE)
        VariableManagement vm = new VariableManagement();

        vm.putMap("flag!", new RealValue(0.0));
        assertTrue(((BooleanValue) vm.getMap("flag!")).toBoolean());
    }

    @Test
    public void testPutMapBooleanIntoUntypedNameFails() {
        // Negative: TRUE or FALSE cannot be stored in an untyped (DOUBLE) name
        VariableManagement vm = new VariableManagement();

        SyntaxErrorException exception = assertThrows(
            SyntaxErrorException.class,
            () -> vm.putMap("a", new BooleanValue(true))
        );
        assertTrue(exception.getMessage().contains("Type mismatch"));
    }

    @Test
    public void testPutMapUntypedNumberPositive() throws SyntaxErrorException, OverflowException, RuntimeException {
        // Positive: an untyped name holds a number (DOUBLE)
        VariableManagement vm = new VariableManagement();

        vm.putMap("a", new RealValue(3.5));
        assertEquals(3.5, vm.getMap("a").toReal(), 0.0000001);
    }

    @Test
    public void testPutMapAtSuffixIsRetired() {
        // Negative: '@' is no longer a variable suffix (decision D2)
        VariableManagement vm = new VariableManagement();

        SyntaxErrorException exception = assertThrows(
            SyntaxErrorException.class,
            () -> vm.putMap("ok@", new BooleanValue(true))
        );
        assertTrue(exception.getMessage().contains("'!'"));
    }

    @Test
    @Order(7)
    public void testPutMapDoubleRejectsTypedVariables() {
        VariableManagement vm = new VariableManagement();

        // String type
        SyntaxErrorException ex1 = assertThrows(SyntaxErrorException.class,
            () -> vm.putMap("x$", 3.14));
        assertTrue(ex1.getMessage().contains("cannot store double"));

        // Integer type
        SyntaxErrorException ex2 = assertThrows(SyntaxErrorException.class,
            () -> vm.putMap("x%", 3.14));
        assertTrue(ex2.getMessage().contains("cannot store double"));
    }
}
