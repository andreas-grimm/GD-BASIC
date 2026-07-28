package eu.gricom.basic.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ConsoleColorsTest.java
 * <p>
 * Test suite for ConsoleColors class, testing ANSI escape sequence constants
 * for colored terminal output.
 */
@DisplayName("ConsoleColors Test Suite")
class ConsoleColorsTest {

    // ===== RESET AND BASIC STRUCTURE TESTS =====

    @Test
    @DisplayName("RESET constant should contain valid ANSI escape sequence")
    void testResetConstant() {
        assertNotNull(ConsoleColors.RESET, "RESET constant should not be null");
        assertTrue(ConsoleColors.RESET.startsWith("\033["), "RESET should start with ANSI escape sequence");
        assertTrue(ConsoleColors.RESET.contains("0m"), "RESET should contain reset code 0m");
        assertEquals("\033[0m", ConsoleColors.RESET, "RESET constant should have correct value");
    }

    // ===== REGULAR COLOR CONSTANTS TESTS =====

    @Test
    @DisplayName("BLACK constant should be properly defined")
    void testBlackConstant() {
        assertNotNull(ConsoleColors.BLACK);
        assertEquals("\033[0;30m", ConsoleColors.BLACK);
        assertTrue(ConsoleColors.BLACK.contains("30m"));
    }

    @Test
    @DisplayName("RED constant should be properly defined")
    void testRedConstant() {
        assertNotNull(ConsoleColors.RED);
        assertEquals("\033[0;31m", ConsoleColors.RED);
        assertTrue(ConsoleColors.RED.contains("31m"));
    }

    @Test
    @DisplayName("GREEN constant should be properly defined")
    void testGreenConstant() {
        assertNotNull(ConsoleColors.GREEN);
        assertEquals("\033[0;32m", ConsoleColors.GREEN);
        assertTrue(ConsoleColors.GREEN.contains("32m"));
    }

    @Test
    @DisplayName("YELLOW constant should be properly defined")
    void testYellowConstant() {
        assertNotNull(ConsoleColors.YELLOW);
        assertEquals("\033[0;33m", ConsoleColors.YELLOW);
        assertTrue(ConsoleColors.YELLOW.contains("33m"));
    }

    @Test
    @DisplayName("BLUE constant should be properly defined")
    void testBlueConstant() {
        assertNotNull(ConsoleColors.BLUE);
        assertEquals("\033[0;34m", ConsoleColors.BLUE);
        assertTrue(ConsoleColors.BLUE.contains("34m"));
    }

    @Test
    @DisplayName("PURPLE constant should be properly defined")
    void testPurpleConstant() {
        assertNotNull(ConsoleColors.PURPLE);
        assertEquals("\033[0;35m", ConsoleColors.PURPLE);
        assertTrue(ConsoleColors.PURPLE.contains("35m"));
    }

    @Test
    @DisplayName("CYAN constant should be properly defined")
    void testCyanConstant() {
        assertNotNull(ConsoleColors.CYAN);
        assertEquals("\033[0;36m", ConsoleColors.CYAN);
        assertTrue(ConsoleColors.CYAN.contains("36m"));
    }

    @Test
    @DisplayName("WHITE constant should be properly defined")
    void testWhiteConstant() {
        assertNotNull(ConsoleColors.WHITE);
        assertEquals("\033[0;37m", ConsoleColors.WHITE);
        assertTrue(ConsoleColors.WHITE.contains("37m"));
    }

    // ===== BOLD COLOR CONSTANTS TESTS =====

    @Test
    @DisplayName("BLACK_BOLD constant should be properly defined")
    void testBlackBoldConstant() {
        assertNotNull(ConsoleColors.BLACK_BOLD);
        assertEquals("\033[1;30m", ConsoleColors.BLACK_BOLD);
        assertTrue(ConsoleColors.BLACK_BOLD.contains("1;30m"));
    }

    @Test
    @DisplayName("RED_BOLD constant should be properly defined")
    void testRedBoldConstant() {
        assertNotNull(ConsoleColors.RED_BOLD);
        assertEquals("\033[1;31m", ConsoleColors.RED_BOLD);
    }

    @Test
    @DisplayName("GREEN_BOLD constant should be properly defined")
    void testGreenBoldConstant() {
        assertNotNull(ConsoleColors.GREEN_BOLD);
        assertEquals("\033[1;32m", ConsoleColors.GREEN_BOLD);
    }

    @Test
    @DisplayName("YELLOW_BOLD constant should be properly defined")
    void testYellowBoldConstant() {
        assertNotNull(ConsoleColors.YELLOW_BOLD);
        assertEquals("\033[1;33m", ConsoleColors.YELLOW_BOLD);
    }

    @Test
    @DisplayName("BLUE_BOLD constant should be properly defined")
    void testBlueBoldConstant() {
        assertNotNull(ConsoleColors.BLUE_BOLD);
        assertEquals("\033[1;34m", ConsoleColors.BLUE_BOLD);
    }

    @Test
    @DisplayName("PURPLE_BOLD constant should be properly defined")
    void testPurpleBoldConstant() {
        assertNotNull(ConsoleColors.PURPLE_BOLD);
        assertEquals("\033[1;35m", ConsoleColors.PURPLE_BOLD);
    }

    @Test
    @DisplayName("CYAN_BOLD constant should be properly defined")
    void testCyanBoldConstant() {
        assertNotNull(ConsoleColors.CYAN_BOLD);
        assertEquals("\033[1;36m", ConsoleColors.CYAN_BOLD);
    }

    @Test
    @DisplayName("WHITE_BOLD constant should be properly defined")
    void testWhiteBoldConstant() {
        assertNotNull(ConsoleColors.WHITE_BOLD);
        assertEquals("\033[1;37m", ConsoleColors.WHITE_BOLD);
    }

    // ===== UNDERLINED COLOR CONSTANTS TESTS =====

    @Test
    @DisplayName("BLACK_UNDERLINED constant should be properly defined")
    void testBlackUnderlinedConstant() {
        assertNotNull(ConsoleColors.BLACK_UNDERLINED);
        assertEquals("\033[4;30m", ConsoleColors.BLACK_UNDERLINED);
        assertTrue(ConsoleColors.BLACK_UNDERLINED.contains("4;30m"));
    }

    @Test
    @DisplayName("RED_UNDERLINED constant should be properly defined")
    void testRedUnderlinedConstant() {
        assertNotNull(ConsoleColors.RED_UNDERLINED);
        assertEquals("\033[4;31m", ConsoleColors.RED_UNDERLINED);
    }

    @Test
    @DisplayName("GREEN_UNDERLINED constant should be properly defined")
    void testGreenUnderlinedConstant() {
        assertNotNull(ConsoleColors.GREEN_UNDERLINED);
        assertEquals("\033[4;32m", ConsoleColors.GREEN_UNDERLINED);
    }

    @Test
    @DisplayName("YELLOW_UNDERLINED constant should be properly defined")
    void testYellowUnderlinedConstant() {
        assertNotNull(ConsoleColors.YELLOW_UNDERLINED);
        assertEquals("\033[4;33m", ConsoleColors.YELLOW_UNDERLINED);
    }

    @Test
    @DisplayName("BLUE_UNDERLINED constant should be properly defined")
    void testBlueUnderlinedConstant() {
        assertNotNull(ConsoleColors.BLUE_UNDERLINED);
        assertEquals("\033[4;34m", ConsoleColors.BLUE_UNDERLINED);
    }

    @Test
    @DisplayName("PURPLE_UNDERLINED constant should be properly defined")
    void testPurpleUnderlinedConstant() {
        assertNotNull(ConsoleColors.PURPLE_UNDERLINED);
        assertEquals("\033[4;35m", ConsoleColors.PURPLE_UNDERLINED);
    }

    @Test
    @DisplayName("CYAN_UNDERLINED constant should be properly defined")
    void testCyanUnderlinedConstant() {
        assertNotNull(ConsoleColors.CYAN_UNDERLINED);
        assertEquals("\033[4;36m", ConsoleColors.CYAN_UNDERLINED);
    }

    @Test
    @DisplayName("WHITE_UNDERLINED constant should be properly defined")
    void testWhiteUnderlinedConstant() {
        assertNotNull(ConsoleColors.WHITE_UNDERLINED);
        assertEquals("\033[4;37m", ConsoleColors.WHITE_UNDERLINED);
    }

    // ===== BACKGROUND COLOR CONSTANTS TESTS =====

    @Test
    @DisplayName("BLACK_BACKGROUND constant should be properly defined")
    void testBlackBackgroundConstant() {
        assertNotNull(ConsoleColors.BLACK_BACKGROUND);
        assertEquals("\033[40m", ConsoleColors.BLACK_BACKGROUND);
        assertTrue(ConsoleColors.BLACK_BACKGROUND.contains("40m"));
    }

    @Test
    @DisplayName("RED_BACKGROUND constant should be properly defined")
    void testRedBackgroundConstant() {
        assertNotNull(ConsoleColors.RED_BACKGROUND);
        assertEquals("\033[41m", ConsoleColors.RED_BACKGROUND);
    }

    @Test
    @DisplayName("GREEN_BACKGROUND constant should be properly defined")
    void testGreenBackgroundConstant() {
        assertNotNull(ConsoleColors.GREEN_BACKGROUND);
        assertEquals("\033[42m", ConsoleColors.GREEN_BACKGROUND);
    }

    @Test
    @DisplayName("YELLOW_BACKGROUND constant should be properly defined")
    void testYellowBackgroundConstant() {
        assertNotNull(ConsoleColors.YELLOW_BACKGROUND);
        assertEquals("\033[43m", ConsoleColors.YELLOW_BACKGROUND);
    }

    @Test
    @DisplayName("BLUE_BACKGROUND constant should be properly defined")
    void testBlueBackgroundConstant() {
        assertNotNull(ConsoleColors.BLUE_BACKGROUND);
        assertEquals("\033[44m", ConsoleColors.BLUE_BACKGROUND);
    }

    @Test
    @DisplayName("PURPLE_BACKGROUND constant should be properly defined")
    void testPurpleBackgroundConstant() {
        assertNotNull(ConsoleColors.PURPLE_BACKGROUND);
        assertEquals("\033[45m", ConsoleColors.PURPLE_BACKGROUND);
    }

    @Test
    @DisplayName("CYAN_BACKGROUND constant should be properly defined")
    void testCyanBackgroundConstant() {
        assertNotNull(ConsoleColors.CYAN_BACKGROUND);
        assertEquals("\033[46m", ConsoleColors.CYAN_BACKGROUND);
    }

    @Test
    @DisplayName("WHITE_BACKGROUND constant should be properly defined")
    void testWhiteBackgroundConstant() {
        assertNotNull(ConsoleColors.WHITE_BACKGROUND);
        assertEquals("\033[47m", ConsoleColors.WHITE_BACKGROUND);
    }

    // ===== BRIGHT COLOR CONSTANTS TESTS =====

    @Test
    @DisplayName("BLACK_BRIGHT constant should be properly defined")
    void testBlackBrightConstant() {
        assertNotNull(ConsoleColors.BLACK_BRIGHT);
        assertEquals("\033[0;90m", ConsoleColors.BLACK_BRIGHT);
        assertTrue(ConsoleColors.BLACK_BRIGHT.contains("90m"));
    }

    @Test
    @DisplayName("RED_BRIGHT constant should be properly defined")
    void testRedBrightConstant() {
        assertNotNull(ConsoleColors.RED_BRIGHT);
        assertEquals("\033[0;91m", ConsoleColors.RED_BRIGHT);
    }

    @Test
    @DisplayName("GREEN_BRIGHT constant should be properly defined")
    void testGreenBrightConstant() {
        assertNotNull(ConsoleColors.GREEN_BRIGHT);
        assertEquals("\033[0;92m", ConsoleColors.GREEN_BRIGHT);
    }

    @Test
    @DisplayName("YELLOW_BRIGHT constant should be properly defined")
    void testYellowBrightConstant() {
        assertNotNull(ConsoleColors.YELLOW_BRIGHT);
        assertEquals("\033[0;93m", ConsoleColors.YELLOW_BRIGHT);
    }

    @Test
    @DisplayName("BLUE_BRIGHT constant should be properly defined")
    void testBlueBrightConstant() {
        assertNotNull(ConsoleColors.BLUE_BRIGHT);
        assertEquals("\033[0;94m", ConsoleColors.BLUE_BRIGHT);
    }

    @Test
    @DisplayName("PURPLE_BRIGHT constant should be properly defined")
    void testPurpleBrightConstant() {
        assertNotNull(ConsoleColors.PURPLE_BRIGHT);
        assertEquals("\033[0;95m", ConsoleColors.PURPLE_BRIGHT);
    }

    @Test
    @DisplayName("CYAN_BRIGHT constant should be properly defined")
    void testCyanBrightConstant() {
        assertNotNull(ConsoleColors.CYAN_BRIGHT);
        assertEquals("\033[0;96m", ConsoleColors.CYAN_BRIGHT);
    }

    @Test
    @DisplayName("WHITE_BRIGHT constant should be properly defined")
    void testWhiteBrightConstant() {
        assertNotNull(ConsoleColors.WHITE_BRIGHT);
        assertEquals("\033[0;97m", ConsoleColors.WHITE_BRIGHT);
    }

    // ===== BOLD BRIGHT COLOR CONSTANTS TESTS =====

    @Test
    @DisplayName("BLACK_BOLD_BRIGHT constant should be properly defined")
    void testBlackBoldBrightConstant() {
        assertNotNull(ConsoleColors.BLACK_BOLD_BRIGHT);
        assertEquals("\033[1;90m", ConsoleColors.BLACK_BOLD_BRIGHT);
        assertTrue(ConsoleColors.BLACK_BOLD_BRIGHT.contains("1;90m"));
    }

    @Test
    @DisplayName("RED_BOLD_BRIGHT constant should be properly defined")
    void testRedBoldBrightConstant() {
        assertNotNull(ConsoleColors.RED_BOLD_BRIGHT);
        assertEquals("\033[1;91m", ConsoleColors.RED_BOLD_BRIGHT);
    }

    @Test
    @DisplayName("GREEN_BOLD_BRIGHT constant should be properly defined")
    void testGreenBoldBrightConstant() {
        assertNotNull(ConsoleColors.GREEN_BOLD_BRIGHT);
        assertEquals("\033[1;92m", ConsoleColors.GREEN_BOLD_BRIGHT);
    }

    @Test
    @DisplayName("YELLOW_BOLD_BRIGHT constant should be properly defined")
    void testYellowBoldBrightConstant() {
        assertNotNull(ConsoleColors.YELLOW_BOLD_BRIGHT);
        assertEquals("\033[1;93m", ConsoleColors.YELLOW_BOLD_BRIGHT);
    }

    @Test
    @DisplayName("BLUE_BOLD_BRIGHT constant should be properly defined")
    void testBlueBoldBrightConstant() {
        assertNotNull(ConsoleColors.BLUE_BOLD_BRIGHT);
        assertEquals("\033[1;94m", ConsoleColors.BLUE_BOLD_BRIGHT);
    }

    @Test
    @DisplayName("PURPLE_BOLD_BRIGHT constant should be properly defined")
    void testPurpleBoldBrightConstant() {
        assertNotNull(ConsoleColors.PURPLE_BOLD_BRIGHT);
        assertEquals("\033[1;95m", ConsoleColors.PURPLE_BOLD_BRIGHT);
    }

    @Test
    @DisplayName("CYAN_BOLD_BRIGHT constant should be properly defined")
    void testCyanBoldBrightConstant() {
        assertNotNull(ConsoleColors.CYAN_BOLD_BRIGHT);
        assertEquals("\033[1;96m", ConsoleColors.CYAN_BOLD_BRIGHT);
    }

    @Test
    @DisplayName("WHITE_BOLD_BRIGHT constant should be properly defined")
    void testWhiteBoldBrightConstant() {
        assertNotNull(ConsoleColors.WHITE_BOLD_BRIGHT);
        assertEquals("\033[1;97m", ConsoleColors.WHITE_BOLD_BRIGHT);
    }

    // ===== BRIGHT BACKGROUND COLOR CONSTANTS TESTS =====

    @Test
    @DisplayName("BLACK_BACKGROUND_BRIGHT constant should be properly defined")
    void testBlackBackgroundBrightConstant() {
        assertNotNull(ConsoleColors.BLACK_BACKGROUND_BRIGHT);
        assertEquals("\033[0;100m", ConsoleColors.BLACK_BACKGROUND_BRIGHT);
        assertTrue(ConsoleColors.BLACK_BACKGROUND_BRIGHT.contains("100m"));
    }

    @Test
    @DisplayName("RED_BACKGROUND_BRIGHT constant should be properly defined")
    void testRedBackgroundBrightConstant() {
        assertNotNull(ConsoleColors.RED_BACKGROUND_BRIGHT);
        assertEquals("\033[0;101m", ConsoleColors.RED_BACKGROUND_BRIGHT);
    }

    @Test
    @DisplayName("GREEN_BACKGROUND_BRIGHT constant should be properly defined")
    void testGreenBackgroundBrightConstant() {
        assertNotNull(ConsoleColors.GREEN_BACKGROUND_BRIGHT);
        assertEquals("\033[0;102m", ConsoleColors.GREEN_BACKGROUND_BRIGHT);
    }

    @Test
    @DisplayName("YELLOW_BACKGROUND_BRIGHT constant should be properly defined")
    void testYellowBackgroundBrightConstant() {
        assertNotNull(ConsoleColors.YELLOW_BACKGROUND_BRIGHT);
        assertEquals("\033[0;103m", ConsoleColors.YELLOW_BACKGROUND_BRIGHT);
    }

    @Test
    @DisplayName("BLUE_BACKGROUND_BRIGHT constant should be properly defined")
    void testBlueBackgroundBrightConstant() {
        assertNotNull(ConsoleColors.BLUE_BACKGROUND_BRIGHT);
        assertEquals("\033[0;104m", ConsoleColors.BLUE_BACKGROUND_BRIGHT);
    }

    @Test
    @DisplayName("PURPLE_BACKGROUND_BRIGHT constant should be properly defined")
    void testPurpleBackgroundBrightConstant() {
        assertNotNull(ConsoleColors.PURPLE_BACKGROUND_BRIGHT);
        assertEquals("\033[0;105m", ConsoleColors.PURPLE_BACKGROUND_BRIGHT);
    }

    @Test
    @DisplayName("CYAN_BACKGROUND_BRIGHT constant should be properly defined")
    void testCyanBackgroundBrightConstant() {
        assertNotNull(ConsoleColors.CYAN_BACKGROUND_BRIGHT);
        assertEquals("\033[0;106m", ConsoleColors.CYAN_BACKGROUND_BRIGHT);
    }

    @Test
    @DisplayName("WHITE_BACKGROUND_BRIGHT constant should be properly defined")
    void testWhiteBackgroundBrightConstant() {
        assertNotNull(ConsoleColors.WHITE_BACKGROUND_BRIGHT);
        assertEquals("\033[0;107m", ConsoleColors.WHITE_BACKGROUND_BRIGHT);
    }

    // ===== UTILITY TESTS =====

    @Test
    @DisplayName("All color constants should start with escape sequence")
    void testAllColorsStartWithEscape() {
        assertAll(
                () -> assertTrue(ConsoleColors.BLACK.startsWith("\033[")),
                () -> assertTrue(ConsoleColors.RED.startsWith("\033[")),
                () -> assertTrue(ConsoleColors.GREEN.startsWith("\033[")),
                () -> assertTrue(ConsoleColors.RESET.startsWith("\033["))
        );
    }

    @Test
    @DisplayName("All color constants should end with 'm'")
    void testAllColorsEndWithM() {
        assertAll(
                () -> assertTrue(ConsoleColors.BLACK.endsWith("m")),
                () -> assertTrue(ConsoleColors.RED.endsWith("m")),
                () -> assertTrue(ConsoleColors.GREEN.endsWith("m")),
                () -> assertTrue(ConsoleColors.RESET.endsWith("m"))
        );
    }
}
