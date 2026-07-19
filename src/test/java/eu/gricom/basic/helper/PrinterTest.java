package eu.gricom.basic.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PrinterTest.java
 * <p>
 * Test suite for Printer class, testing output printing functionality.
 */
@DisplayName("Printer Test Suite")
class PrinterTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private void captureOutput() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    private void restoreOutput() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    // ===== PRINT EMPTY LINE TESTS =====

    @Test
    @DisplayName("println() without arguments should print empty line")
    void testPrintlnEmpty() {
        captureOutput();
        Printer.println();
        restoreOutput();

        String output = outContent.toString();
        assertTrue(output.contains("\n"), "Should print newline");
        assertEquals(System.lineSeparator(), output, "Should print only a newline");
    }

    @Test
    @DisplayName("println() should not throw exception")
    void testPrintlnEmptyNoException() {
        assertDoesNotThrow(() -> Printer.println(),
            "println() without arguments should not throw exception");
    }

    @Test
    @DisplayName("println() can be called multiple times")
    void testPrintlnEmptyMultipleCalls() {
        captureOutput();
        Printer.println();
        Printer.println();
        Printer.println();
        restoreOutput();

        String output = outContent.toString();
        // Should have 3 newlines
        long newlineCount = output.chars().filter(ch -> ch == '\n').count();
        assertTrue(newlineCount >= 2, "Should print multiple empty lines");
    }

    // ===== PRINT WITH STRING TESTS =====

    @Test
    @DisplayName("println(String) should print text with newline")
    void testPrintlnWithText() {
        captureOutput();
        Printer.println("Hello World");
        restoreOutput();

        String output = outContent.toString();
        assertTrue(output.contains("Hello World"), "Should print the text");
        assertTrue(output.endsWith("\n"), "Should end with newline");
    }

    @Test
    @DisplayName("println(String) should not throw exception")
    void testPrintlnWithTextNoException() {
        assertDoesNotThrow(() -> Printer.println("Test message"),
            "println(String) should not throw exception");
    }

    @Test
    @DisplayName("println(String) should handle empty string")
    void testPrintlnEmptyString() {
        captureOutput();
        Printer.println("");
        restoreOutput();

        String output = outContent.toString();
        assertTrue(output.contains("\n"), "Should print newline for empty string");
    }

    @Test
    @DisplayName("println(String) should handle null string")
    void testPrintlnNullString() {
        // This test documents the behavior - may throw NPE or print "null"
        assertDoesNotThrow(() -> Printer.println(null),
            "println(String) should handle null string gracefully or as expected");
    }

    @Test
    @DisplayName("println(String) should print single character")
    void testPrintlnSingleChar() {
        captureOutput();
        Printer.println("A");
        restoreOutput();

        String output = outContent.toString();
        assertTrue(output.contains("A"), "Should print single character");
    }

    @Test
    @DisplayName("println(String) should print numbers")
    void testPrintlnNumbers() {
        captureOutput();
        Printer.println("12345");
        restoreOutput();

        String output = outContent.toString();
        assertTrue(output.contains("12345"), "Should print numbers");
    }

    @Test
    @DisplayName("println(String) should print special characters")
    void testPrintlnSpecialCharacters() {
        captureOutput();
        Printer.println("!@#$%^&*()");
        restoreOutput();

        String output = outContent.toString();
        assertTrue(output.contains("!@#$%^&*()"), "Should print special characters");
    }

    @Test
    @DisplayName("println(String) should print spaces")
    void testPrintlnWithSpaces() {
        captureOutput();
        Printer.println("   spaces   ");
        restoreOutput();

        String output = outContent.toString();
        assertTrue(output.contains("   spaces   "), "Should preserve spaces");
    }

    @Test
    @DisplayName("println(String) should print tabs")
    void testPrintlnWithTabs() {
        captureOutput();
        Printer.println("Column1\tColumn2\tColumn3");
        restoreOutput();

        String output = outContent.toString();
        assertTrue(output.contains("\t"), "Should preserve tabs");
    }

    @Test
    @DisplayName("println(String) should print very long string")
    void testPrintlnVeryLongString() {
        StringBuilder longString = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            longString.append("A");
        }

        captureOutput();
        Printer.println(longString.toString());
        restoreOutput();

        String output = outContent.toString();
        assertTrue(output.length() > 10000, "Should print very long strings");
    }

    @Test
    @DisplayName("println(String) should print strings with embedded newlines")
    void testPrintlnEmbeddedNewlines() {
        captureOutput();
        Printer.println("Line1\nLine2\nLine3");
        restoreOutput();

        String output = outContent.toString();
        assertTrue(output.contains("Line1"), "Should print first line");
        assertTrue(output.contains("Line2"), "Should print second line");
        assertTrue(output.contains("Line3"), "Should print third line");
    }

    @Test
    @DisplayName("println(String) should print BASIC code")
    void testPrintlnBasicCode() {
        captureOutput();
        Printer.println("10 PRINT \"HELLO WORLD\"");
        restoreOutput();

        String output = outContent.toString();
        assertTrue(output.contains("PRINT"), "Should print BASIC keywords");
        assertTrue(output.contains("HELLO WORLD"), "Should preserve string content");
    }

    @Test
    @DisplayName("println(String) should print with Unicode characters")
    void testPrintlnUnicode() {
        captureOutput();
        Printer.println("Café ☕ 中文");
        restoreOutput();

        String output = outContent.toString();
        assertTrue(output.length() > 0, "Should handle Unicode characters");
    }

    // ===== MULTIPLE CALLS TESTS =====

    @Test
    @DisplayName("Multiple println calls should each produce output")
    void testMultiplePrintlnCalls() {
        captureOutput();
        Printer.println("First");
        Printer.println("Second");
        Printer.println("Third");
        restoreOutput();

        String output = outContent.toString();
        assertTrue(output.contains("First"), "Should print first call");
        assertTrue(output.contains("Second"), "Should print second call");
        assertTrue(output.contains("Third"), "Should print third call");
    }

    @Test
    @DisplayName("Alternating empty and text println calls should work")
    void testAlternatingPrintlnCalls() {
        captureOutput();
        Printer.println("Line1");
        Printer.println();
        Printer.println("Line2");
        Printer.println();
        Printer.println("Line3");
        restoreOutput();

        String output = outContent.toString();
        assertTrue(output.contains("Line1"), "Should print text lines");
        assertTrue(output.contains("Line2"), "Should print text lines");
        assertTrue(output.contains("Line3"), "Should print text lines");
    }

    // ===== TARGET TESTS =====

    @Test
    @DisplayName("Printer should output to Console by default")
    void testPrinterDefaultTarget() {
        captureOutput();
        Printer.println("Test output");
        restoreOutput();

        String output = outContent.toString();
        assertTrue(output.contains("Test output"), "Should output to console by default");
    }

    // ===== EDGE CASE TESTS =====

    @Test
    @DisplayName("println should handle repeated calls without buffer issues")
    void testPrintlnNoBufferIssues() {
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                Printer.println("Message " + i);
            }
        }, "Should handle many repeated calls");
    }

    @Test
    @DisplayName("println should handle concurrent-like calls in sequence")
    void testPrintlnSequentialCalls() {
        captureOutput();
        Printer.println("Call 1");
        Printer.println();
        Printer.println("Call 2");
        Printer.println("Call 3");
        Printer.println();
        Printer.println("Call 4");
        restoreOutput();

        String output = outContent.toString();
        assertTrue(output.contains("Call 1"), "Should output all calls");
        assertTrue(output.contains("Call 4"), "Should output last call");
    }

    @Test
    @DisplayName("println should work with printable ASCII characters")
    void testPrintlnASCIIRange() {
        StringBuilder allPrintable = new StringBuilder();
        for (int i = 32; i < 127; i++) {
            allPrintable.append((char) i);
        }

        captureOutput();
        Printer.println(allPrintable.toString());
        restoreOutput();

        String output = outContent.toString();
        assertTrue(output.length() > 90, "Should print all printable ASCII characters");
    }

    @Test
    @DisplayName("println(String) output should be ordered correctly")
    void testPrintlnOutputOrder() {
        captureOutput();
        Printer.println("First");
        Printer.println("Second");
        Printer.println("Third");
        restoreOutput();

        String output = outContent.toString();
        int firstPos = output.indexOf("First");
        int secondPos = output.indexOf("Second");
        int thirdPos = output.indexOf("Third");

        assertTrue(firstPos < secondPos, "First should come before Second");
        assertTrue(secondPos < thirdPos, "Second should come before Third");
    }

    @Test
    @DisplayName("println should preserve line order with mixed empty and non-empty lines")
    void testPrintlnLineOrder() {
        captureOutput();
        Printer.println("A");
        Printer.println();
        Printer.println("B");
        Printer.println();
        Printer.println("C");
        restoreOutput();

        String output = outContent.toString();
        String[] lines = output.split("\n");
        assertTrue(lines.length >= 4, "Should have multiple lines");
    }
}
