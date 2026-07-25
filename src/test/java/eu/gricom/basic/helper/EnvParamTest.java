package eu.gricom.basic.helper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class EnvParamTest {

    @BeforeEach
    void setUp() {
        EnvParam.setConfigGroup("testing");
        resetSingleton();
    }

    @AfterEach
    void tearDown() {
        EnvParam.setConfigGroup("environment");
        resetSingleton();
    }

    @Test
    void testGetStringFromConfigFile() {
        String appName = EnvParam.getString("app_name");
        assertEquals("GD-BASIC", appName);
    }

    @Test
    void testGetIntFromConfigFile() {
        int maxBcdDigits = EnvParam.getInt("max_bcd_digits");
        assertEquals(40, maxBcdDigits);
    }

    @Test
    void testGetFloatFromConfigFile() {
        float timeoutFloat = EnvParam.getFloat("timeout_float");
        assertEquals(30.5f, timeoutFloat, 0.01f);
    }

    @Test
    void testGetBooleanFromConfigFile() {
        boolean debugMode = EnvParam.getBoolean("debug_mode");
        assertFalse(debugMode);
    }

    @Test
    void testMissingStringKeyReturnsEmpty() {
        String result = EnvParam.getString("non_existent_key");
        assertEquals("", result);
    }

    @Test
    void testMissingIntKeyReturnsZero() {
        int result = EnvParam.getInt("non_existent_int_key");
        assertEquals(0, result);
    }

    @Test
    void testMissingFloatKeyReturnsZero() {
        float result = EnvParam.getFloat("non_existent_float_key");
        assertEquals(0.0f, result);
    }

    @Test
    void testMissingBooleanKeyReturnsFalse() {
        boolean result = EnvParam.getBoolean("non_existent_bool_key");
        assertFalse(result);
    }

    @Test
    void testGetMaxBcdDigits() {
        int iMaxBcdDigits = EnvParam.getMaxBcdDigits();
        assertEquals(40, iMaxBcdDigits);
    }

    @Test
    void testSingletonBehavior() {
        EnvParam instance1 = EnvParam.getInstance();
        EnvParam instance2 = EnvParam.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void testTimeoutSecondsConfiguration() {
        int timeout = EnvParam.getInt("timeout_seconds");
        assertEquals(30, timeout);
    }

    @Test
    void testBooleanParsing_False() {
        boolean debugMode = EnvParam.getBoolean("debug_mode");
        assertFalse(debugMode);
    }

    @Test
    void testIntegerConfiguration() {
        int result = EnvParam.getInt("timeout_seconds");
        assertEquals(30, result);
    }

    @Test
    void testNegativeIntegerHandling() {
        int result = EnvParam.getInt("negative_value_not_exists");
        assertEquals(0, result);
    }

    @Test
    void testStringWithSpecialChars() {
        String result = EnvParam.getString("app_name");
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testMultipleCallsReturnSameValue() {
        String result1 = EnvParam.getString("app_name");
        String result2 = EnvParam.getString("app_name");
        assertEquals(result1, result2);
        assertEquals("GD-BASIC", result1);
    }

    @Test
    void testConfigFileLoading() {
        assertNotNull(EnvParam.getInstance());
        String appName = EnvParam.getString("app_name");
        assertEquals("GD-BASIC", appName);
    }

    @Test
    void testFloatParsing() {
        float result = EnvParam.getFloat("timeout_float");
        assertTrue(result > 30.0f);
    }

    private void resetSingleton() {
        try {
            Field oField = EnvParam.class.getDeclaredField("_oInstance");
            oField.setAccessible(true);
            oField.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to reset singleton", e);
        }
    }
}
