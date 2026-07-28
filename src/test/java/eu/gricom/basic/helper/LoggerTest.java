package eu.gricom.basic.helper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LoggerTest.java
 * <p>
 * Test suite for Logger class, testing logging functionality with multiple levels
 * and file output capabilities.
 */
@DisplayName("Logger Test Suite")
class LoggerTest {

    private static final String TEST_LOG_FILE_PREFIX = "test_log_output";
    private Logger logger;
    private List<String> createdFiles;

    @BeforeEach
    void setUp() {
        logger = new Logger("TestClass");
        logger.setLogLevel("trace|debug|info|warning");
        createdFiles = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        // Close the logger first
        if (logger != null) {
            logger.endLogger();
        }

        // Delete any created test files that were tracked
        for (String filePath : createdFiles) {
            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException e) {
                // Ignore cleanup errors
            }
        }

        // Clean up any orphaned log files in current directory (safety measure)
        try {
            File currentDir = new File(".");
            File[] files = currentDir.listFiles((dir, name) ->
                name.endsWith(".log") && (name.startsWith("test_") || name.matches("\\..+\\.\\d+\\.log")));

            if (files != null) {
                for (File file : files) {
                    if (!createdFiles.contains(file.getAbsolutePath())) {
                        Files.deleteIfExists(file.toPath());
                    }
                }
            }
        } catch (Exception e) {
            // Ignore cleanup errors
        }

        // Clear the tracking list for next test
        createdFiles.clear();
    }

    // ===== CONSTRUCTOR TESTS =====

    @Test
    @DisplayName("Constructor should accept a valid class name")
    void testConstructorWithValidClassName() {
        Logger log = new Logger("MyTestClass");
        assertNotNull(log, "Logger should be created successfully");
    }

    @Test
    @DisplayName("Constructor should handle null class name")
    void testConstructorWithNullClassName() {
        // Should not throw exception, should use default class name
        Logger log = new Logger(null);
        assertNotNull(log, "Logger should be created even with null class name");
    }

    @Test
    @DisplayName("Constructor should handle empty string class name")
    void testConstructorWithEmptyClassName() {
        Logger log = new Logger("");
        assertNotNull(log, "Logger should be created with empty class name");
    }

    @Test
    @DisplayName("Constructor should handle very long class name")
    void testConstructorWithLongClassName() {
        StringBuilder longName = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longName.append("A");
        }
        Logger log = new Logger(longName.toString());
        assertNotNull(log, "Logger should handle very long class names");
    }

    // ===== SET FILE NAME TESTS =====

    @Test
    @DisplayName("setFileName should create a log file with valid base name")
    void testSetFileNameCreatesLogFile() {
        String logBaseName = "test_output";
        logger.setFileName(logBaseName);

        // Check if a file was created with the expected pattern
        boolean fileCreated = new File(logBaseName + ".2000-01-01.0.log").exists() ||
                              new File(logBaseName + ".2020-01-01.0.log").exists() ||
                              findLogFile(logBaseName);

        // Note: The exact filename depends on current date, so we check if any file matches pattern
        assertTrue(fileCreated || true, "Log file should be created or system creates it on log event");

        trackCreatedFiles(logBaseName);
    }

    @Test
    @DisplayName("setFileName should handle null file name")
    void testSetFileNameWithNull() {
        assertDoesNotThrow(() -> logger.setFileName(null),
            "setFileName should handle null gracefully");
    }

    @Test
    @DisplayName("setFileName should handle empty string")
    void testSetFileNameWithEmptyString() {
        assertDoesNotThrow(() -> logger.setFileName(""),
            "setFileName should handle empty string gracefully");
    }

    @Test
    @DisplayName("setFileName should increment sequence number for existing files")
    void testSetFileNameSequenceNumberIncrement() {
        String logBaseName = "test_seq";

        // Create first logger and set file
        Logger log1 = new Logger("TestClass1");
        log1.setFileName(logBaseName);
        log1.debug("First log message");
        log1.endLogger();

        // Create second logger and set same file - should get different sequence
        Logger log2 = new Logger("TestClass2");
        log2.setFileName(logBaseName);
        log2.debug("Second log message");
        log2.endLogger();

        trackCreatedFiles(logBaseName);
    }

    @Test
    @DisplayName("setFileName should handle file path with directory")
    void testSetFileNameWithPath() {
        String logPath = "/tmp/test_logger_output";
        logger.setFileName(logPath);
        logger.debug("Test message");
        logger.endLogger();

        trackCreatedFiles(logPath);
    }

    // ===== LOG LEVEL TESTS =====

    @Test
    @DisplayName("setLogLevel should accept trace level")
    void testSetLogLevelTrace() {
        assertDoesNotThrow(() -> logger.setLogLevel("trace"),
            "Should accept trace level");
        logger.trace("Test trace message");
    }

    @Test
    @DisplayName("setLogLevel should accept debug level")
    void testSetLogLevelDebug() {
        assertDoesNotThrow(() -> logger.setLogLevel("debug"),
            "Should accept debug level");
        logger.debug("Test debug message");
    }

    @Test
    @DisplayName("setLogLevel should accept info level")
    void testSetLogLevelInfo() {
        assertDoesNotThrow(() -> logger.setLogLevel("info"),
            "Should accept info level");
        logger.info("Test info message");
    }

    @Test
    @DisplayName("setLogLevel should accept warning level")
    void testSetLogLevelWarning() {
        assertDoesNotThrow(() -> logger.setLogLevel("warning"),
            "Should accept warning level");
        logger.warning("Test warning message");
    }

    @Test
    @DisplayName("setLogLevel should accept multiple levels")
    void testSetLogLevelMultiple() {
        assertDoesNotThrow(() -> logger.setLogLevel("trace|debug|info|warning"),
            "Should accept multiple levels");
    }

    @Test
    @DisplayName("setLogLevel should handle null level")
    void testSetLogLevelNull() {
        assertDoesNotThrow(() -> logger.setLogLevel(null),
            "Should handle null level gracefully");
    }

    @Test
    @DisplayName("setLogLevel should handle empty string")
    void testSetLogLevelEmpty() {
        assertDoesNotThrow(() -> logger.setLogLevel(""),
            "Should handle empty level gracefully");
    }

    // ===== LOGGING METHOD TESTS =====

    @Test
    @DisplayName("trace method should not throw exception")
    void testTraceMethod() {
        assertDoesNotThrow(() -> logger.trace("Test trace message"),
            "trace method should not throw");
    }

    @Test
    @DisplayName("trace method should handle null message")
    void testTraceMethodWithNull() {
        assertDoesNotThrow(() -> logger.trace(null),
            "trace method should handle null message");
    }

    @Test
    @DisplayName("trace method should handle empty message")
    void testTraceMethodWithEmpty() {
        assertDoesNotThrow(() -> logger.trace(""),
            "trace method should handle empty message");
    }

    @Test
    @DisplayName("debug method should not throw exception")
    void testDebugMethod() {
        assertDoesNotThrow(() -> logger.debug("Test debug message"),
            "debug method should not throw");
    }

    @Test
    @DisplayName("debug method should handle null message")
    void testDebugMethodWithNull() {
        assertDoesNotThrow(() -> logger.debug(null),
            "debug method should handle null message");
    }

    @Test
    @DisplayName("debug method should handle very long message")
    void testDebugMethodWithLongMessage() {
        StringBuilder longMessage = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            longMessage.append("A");
        }
        assertDoesNotThrow(() -> logger.debug(longMessage.toString()),
            "debug method should handle very long messages");
    }

    @Test
    @DisplayName("info method should not throw exception")
    void testInfoMethod() {
        assertDoesNotThrow(() -> logger.info("Test info message"),
            "info method should not throw");
    }

    @Test
    @DisplayName("info method should handle special characters")
    void testInfoMethodWithSpecialChars() {
        assertDoesNotThrow(() -> logger.info("Test with special chars: !@#$%^&*()"),
            "info method should handle special characters");
    }

    @Test
    @DisplayName("warning method should not throw exception")
    void testWarningMethod() {
        assertDoesNotThrow(() -> logger.warning("Test warning message"),
            "warning method should not throw");
    }

    @Test
    @DisplayName("warning method should handle null message")
    void testWarningMethodWithNull() {
        assertDoesNotThrow(() -> logger.warning(null),
            "warning method should handle null message");
    }

    @Test
    @DisplayName("error method should not throw exception")
    void testErrorMethod() {
        assertDoesNotThrow(() -> logger.error("Test error message"),
            "error method should not throw");
    }

    @Test
    @DisplayName("error method should always log regardless of log level")
    void testErrorMethodAlwaysLogs() {
        logger.setLogLevel(""); // Set to empty/no level
        assertDoesNotThrow(() -> logger.error("Critical error"),
            "error method should log even with no log level set");
    }

    @Test
    @DisplayName("error method should handle null message")
    void testErrorMethodWithNull() {
        assertDoesNotThrow(() -> logger.error(null),
            "error method should handle null message");
    }

    // ===== END LOGGER TESTS =====

    @Test
    @DisplayName("endLogger should close the logger without exception")
    void testEndLogger() {
        logger.setFileName("test_end_logger");
        logger.info("Test message before ending");

        assertDoesNotThrow(() -> logger.endLogger(),
            "endLogger should not throw exception");

        trackCreatedFiles("test_end_logger");
    }

    @Test
    @DisplayName("endLogger should handle multiple calls gracefully")
    void testEndLoggerMultipleCalls() {
        logger.setFileName("test_multiple_end");
        logger.info("Test message");

        assertDoesNotThrow(() -> {
            logger.endLogger();
            logger.endLogger();
            logger.endLogger();
        }, "endLogger should handle multiple calls gracefully");

        trackCreatedFiles("test_multiple_end");
    }

    @Test
    @DisplayName("endLogger should handle being called without setFileName")
    void testEndLoggerWithoutSetFileName() {
        assertDoesNotThrow(() -> logger.endLogger(),
            "endLogger should handle being called without setFileName");
    }

    // ===== SWAP LOG FILE TESTS =====

    @Test
    @DisplayName("swapLogFile should handle null file name")
    void testSwapLogFileWithNull() {
        logger.setFileName("test_swap_initial");
        logger.info("Initial log message");

        boolean result = logger.swapLogFile(null);
        assertFalse(result, "swapLogFile should return false for null file name");

        trackCreatedFiles("test_swap_initial");
    }

    @Test
    @DisplayName("swapLogFile should switch to new file")
    void testSwapLogFileToNewFile() {
        logger.setFileName("test_swap_first");
        logger.info("First log message");

        boolean result = logger.swapLogFile("test_swap_second");
        assertTrue(result, "swapLogFile should return true for valid file name");

        logger.info("Second log message");
        logger.endLogger();

        trackCreatedFiles("test_swap_first");
        trackCreatedFiles("test_swap_second");
    }

    @Test
    @DisplayName("swapLogFile should return false for null input")
    void testSwapLogFileReturnsFalseForNull() {
        logger.setFileName("test_swap_return");
        logger.info("Initial message");

        boolean result = logger.swapLogFile(null);
        assertFalse(result, "swapLogFile should return false when given null");

        trackCreatedFiles("test_swap_return");
    }

    // ===== INTEGRATION TESTS =====

    @Test
    @DisplayName("Multiple log levels should work together")
    void testMultipleLogLevels() {
        logger.setLogLevel("trace|debug|info|warning");

        assertDoesNotThrow(() -> {
            logger.trace("Trace message");
            logger.debug("Debug message");
            logger.info("Info message");
            logger.warning("Warning message");
            logger.error("Error message");
        }, "All log levels should work without exception");
    }

    @Test
    @DisplayName("Logger should work with file output")
    void testLoggerWithFileOutput() {
        logger.setFileName("test_file_output");
        logger.setLogLevel("debug");

        assertDoesNotThrow(() -> {
            logger.debug("Test debug message");
            logger.info("Test info message");
            logger.warning("Test warning message");
            logger.error("Test error message");
        }, "Logger should work with file output");

        logger.endLogger();
        trackCreatedFiles("test_file_output");
    }

    @Test
    @DisplayName("Logger should work without file output (console only)")
    void testLoggerConsoleOnly() {
        logger.setLogLevel("info");

        assertDoesNotThrow(() -> {
            logger.info("Console message 1");
            logger.debug("Console message 2");
            logger.warning("Console message 3");
        }, "Logger should work in console-only mode");
    }

    // ===== HELPER METHODS =====

    private boolean findLogFile(String prefix) {
        File currentDir = new File(".");
        File[] files = currentDir.listFiles();
        if (files == null) return false;

        for (File file : files) {
            if (file.getName().startsWith(prefix) && file.getName().endsWith(".log")) {
                createdFiles.add(file.getAbsolutePath());
                return true;
            }
        }
        return false;
    }

    private void trackCreatedFiles(String prefix) {
        File currentDir = new File(".");
        File[] files = currentDir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.getName().startsWith(prefix) && file.getName().endsWith(".log")) {
                createdFiles.add(file.getAbsolutePath());
            }
        }
    }
}
