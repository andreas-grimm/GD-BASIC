package eu.gricom.basic.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EnvParamTest.java
 * <p>
 * Test suite for EnvParam class, testing environment parameter configuration.
 */
@DisplayName("EnvParam Test Suite")
class EnvParamTest {

    @Test
    @DisplayName("getMAX_BCD_DIGITS should return exactly 40")
    void testGetMaxBcdDigitsReturnsForty() {
        int result = EnvParam.getMAX_BCD_DIGITS();
        assertEquals(40, result, "MAX_BCD_DIGITS should return 40");
    }

    @Test
    @DisplayName("getMAX_BCD_DIGITS should return positive value")
    void testGetMaxBcdDigitsIsPositive() {
        int result = EnvParam.getMAX_BCD_DIGITS();
        assertTrue(result > 0, "MAX_BCD_DIGITS should be positive");
    }

    @Test
    @DisplayName("getMAX_BCD_DIGITS should return greater than 10")
    void testGetMaxBcdDigitsGreaterThanTen() {
        int result = EnvParam.getMAX_BCD_DIGITS();
        assertTrue(result > 10, "MAX_BCD_DIGITS should be greater than 10");
    }

    @Test
    @DisplayName("getMAX_BCD_DIGITS should be consistent across multiple calls")
    void testGetMaxBcdDigitsConsistent() {
        int firstCall = EnvParam.getMAX_BCD_DIGITS();
        int secondCall = EnvParam.getMAX_BCD_DIGITS();
        int thirdCall = EnvParam.getMAX_BCD_DIGITS();

        assertEquals(firstCall, secondCall, "Result should be consistent");
        assertEquals(secondCall, thirdCall, "Result should be consistent");
        assertEquals(firstCall, thirdCall, "Result should be consistent");
    }

    @Test
    @DisplayName("getMAX_BCD_DIGITS should not be zero")
    void testGetMaxBcdDigitsNotZero() {
        int result = EnvParam.getMAX_BCD_DIGITS();
        assertNotEquals(0, result, "MAX_BCD_DIGITS should not be zero");
    }

    @Test
    @DisplayName("getMAX_BCD_DIGITS should be less than 100")
    void testGetMaxBcdDigitsLessThanHundred() {
        int result = EnvParam.getMAX_BCD_DIGITS();
        assertTrue(result < 100, "MAX_BCD_DIGITS should be less than 100");
    }

    @Test
    @DisplayName("getMAX_BCD_DIGITS should be in reasonable range for BCD digits")
    void testGetMaxBcdDigitsInReasonableRange() {
        int result = EnvParam.getMAX_BCD_DIGITS();
        assertTrue(result >= 32 && result <= 256, "MAX_BCD_DIGITS should be in a reasonable range");
    }
}
