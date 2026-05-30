package eu.gricom.basic.tokenizer;

import eu.gricom.basic.error.SyntaxErrorException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class NormalizerTest {

    @Test
    public void testNormalString() {
        String strTest = "This is a normal String";
        String strResult = Normalizer.normalize(strTest);

        assertEquals(strTest, strResult);
    }

    @Test
    public void testNormalStringWithQuotes() {
        String strTest = "This is a normal String with \"Quotes\"";
        String strResult = Normalizer.normalize(strTest);

        assertEquals(strTest, strResult);
    }

    @Test
    public void testNormalStringWithComma() {
        String strTest = "This is a normal String with Comma:,";
        String strTarget = "This is a normal String with Comma :  , ";
        String strResult = Normalizer.normalize(strTest);

        assertEquals(strTarget, strResult);
    }

    @Test
    public void testNormalStringWithCommaInQuotes() {
        String strTest = "This is a normal String with Comma in \"Quotes:,\"";
        String strResult = Normalizer.normalize(strTest);

        assertEquals(strTest, strResult);
    }

    @Test
    public void testNormalStringWithEverything() {
        String strTest = "This is a normal String with Comma in \"Quotes:,\" and out of Quotes:,";
        String strTarget = "This is a normal String with Comma in \"Quotes:,\" and out of Quotes :  , ";

        String strResult = Normalizer.normalize(strTest);

        assertEquals(strTarget, strResult);
    }

    @Test
    public void testIndexString() {
        String strTest = "(1, 1, 1, 1,1)";
        String strTarget = "-1,1,1,1,1";
        try {
            String strResult = Normalizer.normalizeIndex(strTest);
            assertEquals(strTarget, strResult);
        } catch (SyntaxErrorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIndexStringWithoutParenthesis() {
        String strTest = "1, 1, 1, 1,1";
        try {
            String strResult = Normalizer.normalizeIndex(strTest);
            assertEquals(strTest, strResult);
        } catch (SyntaxErrorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIndexStringWrongParenthesisLeft() {
        String strTest = "1, 1, 1, 1,1)";

        assertThrows(SyntaxErrorException.class, () -> {
            Normalizer.normalizeIndex(strTest);
        });
    }

    @Test
    public void testIndexStringWrongParenthesisRight() {
        String strTest = "(1, 1, 1, 1,1";

        assertThrows(SyntaxErrorException.class, () -> {
            Normalizer.normalizeIndex(strTest);
        });
    }

    @Test
    public void testNormalizeFunctionNoParenthesis() {
        String strTest = "Test without parenthesis";
        try {
            String strResult = Normalizer.normalizeFunction(strTest);
            assertEquals(strTest, strResult);
        } catch (SyntaxErrorException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testNormalizeFunctionCorrectParenthesis() {
        String strTest = "This is a function call: sin(x)";
        String strResult = "This is a function call: sin (x)";
        try {
            String strExpect = Normalizer.normalizeFunction(strTest);
            assertEquals(strExpect, strResult);
        } catch (SyntaxErrorException e) {
            fail();
        }

    }

    @Test
    public void testNormailzeFunctionWrongParenthesisRight() {
        String strTest = "This is a function call with wrong parenthesis: sin(x";

        assertThrows(SyntaxErrorException.class, () -> {
            Normalizer.normalizeFunction(strTest);
        });
    }

    // ============================================================================
    // NEW TESTS: Normalizer parenthesis operator spacing
    // ============================================================================

    @Test
    public void testArrayAccessWithAddition() {
        String strTest = "a$(i+1)";
        String strTarget = "a$ ( i + 1 ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testArrayAccessWithAdditionSpaced() {
        String strTest = "a$(i +1)";
        String strTarget = "a$ ( i  + 1 ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testArrayAccessWithAdditionFullySpaced() {
        String strTest = "a$(i + 1)";
        String strTarget = "a$ ( i  +  1 ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testArrayAccessWithSubtraction() {
        String strTest = "arr%(j-2)";
        String strTarget = "arr% ( j - 2 ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testArrayAccessWithMultiplication() {
        String strTest = "matrix#(x*3)";
        String strTarget = "matrix# ( x * 3 ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testArrayAccessWithDivision() {
        String strTest = "data&(n/2)";
        String strTarget = "data& ( n / 2 ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testArrayAccessWithPower() {
        String strTest = "vals(m^2)";
        String strTarget = "vals ( m ^ 2 ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testArrayAccessWithMultipleOperators() {
        String strTest = "result%(a+b*c)";
        String strTarget = "result% ( a + b * c ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testArrayAccessWithComplexExpression() {
        String strTest = "table%(i+j-k)";
        String strTarget = "table% ( i + j - k ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testNestedParenthesesWithOperators() {
        String strTest = "func(a+(b+c))";
        String strTarget = "func ( a +  ( b + c )  ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testArrayAccessComparisonOperator() {
        String strTest = "vals(i>=1)";
        String strTarget = "vals ( i>=1 ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testArrayAccessEqualityOperator() {
        String strTest = "check%(x==5)";
        String strTarget = "check% ( x==5 ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testArrayAccessInequalityOperator() {
        String strTest = "valid!(y!=0)";
        String strTarget = "valid! ( y!=0 ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testArrayAccessBitwiseAnd() {
        String strTest = "bits%(a&b)";
        String strTarget = "bits% ( a & b ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testArrayAccessBitwiseOr() {
        String strTest = "flags%(x|y)";
        String strTarget = "flags% ( x | y ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testOperatorsOutsideParenthesesNotAffected() {
        String strTest = "x = y+z";
        String strTarget = "x = y+z";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testOperatorsInsideQuotesNotAffected() {
        String strTest = "PRINT \"a+b\"";
        String strTarget = "PRINT \"a+b\"";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testOperatorsInQuotesWithParentheses() {
        String strTest = "PRINT \"func(x+1) = y\"";
        String strTarget = "PRINT \"func(x+1) = y\"";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testMixedQuotesAndParentheses() {
        String strTest = "a$(i+1): PRINT \"x+y\"";
        String strTarget = "a$ ( i + 1 )  :  PRINT \"x+y\"";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testMultipleDimensionalArrayAccess() {
        String strTest = "matrix%(i+1,j-1)";
        String strTarget = "matrix% ( i + 1 , j - 1 ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testAssignmentWithArrayIndexOperation() {
        String strTest = "a%(x+1)=5";
        String strTarget = "a% ( x + 1 ) =5";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testFunctionCallWithOperatorExpression() {
        String strTest = "SIN(x+y*z)";
        String strTarget = "SIN ( x + y * z ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testIfConditionWithArrayIndex() {
        String strTest = "IF a(i+1)>5 THEN";
        String strTarget = "IF a ( i + 1 ) >5 THEN";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testNegativeIndexExpression() {
        String strTest = "arr(n-1)";
        String strTarget = "arr ( n - 1 ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testZeroIndexWithAddition() {
        String strTest = "data(0+i)";
        String strTarget = "data ( 0 + i ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testVariableWithTypeSuffix() {
        String strTest = "myVar%(idx+1)";
        String strTarget = "myVar% ( idx + 1 ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testLongTypeVariable() {
        String strTest = "counter&(pos+offset)";
        String strTarget = "counter& ( pos + offset ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testStringVariableArray() {
        String strTest = "names$(row+1)";
        String strTarget = "names$ ( row + 1 ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testBooleanVariableArray() {
        String strTest = "flags!(item-1)";
        String strTarget = "flags! ( item - 1 ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testRealNumberVariableArray() {
        String strTest = "values#(index*2)";
        String strTarget = "values# ( index * 2 ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testComplexNestedExpression() {
        String strTest = "result(a+(b*c)-(d/e))";
        String strTarget = "result ( a +  ( b * c )  -  ( d / e )  ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testShiftLeftOperator() {
        String strTest = "bits(x<<1)";
        String strTarget = "bits ( x<<1 ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }

    @Test
    public void testShiftRightOperator() {
        String strTest = "value(num>>2)";
        String strTarget = "value ( num>>2 ) ";
        String strResult = Normalizer.normalize(strTest);
        assertEquals(strTarget, strResult);
    }
}
