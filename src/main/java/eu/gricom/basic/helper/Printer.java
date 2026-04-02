package eu.gricom.basic.helper;

/**
 * Printer.java
 * <p>
 * Description: The Printer class provides static utility methods for all program output operations. It abstracts the
 * output destination, allowing BASIC PRINT statements to write to the console or potentially other output targets. It
 * handles both line-terminated and continuous output.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class Printer {
    private static String _strTarget = "Console";

    /**
     * default constructor.
     */
    private Printer() {

    }

    /**
     * print a single empty line output to the console terminal.
     */
    public static void println() {
        if (_strTarget.contains("Console")) {
            System.out.println();
        }
    }

    /**
     * provide output to the console terminal.
     *
     * @param strPrintLine - defined output line
     */
    public static void println(final String strPrintLine) {
        if (_strTarget.contains("Console")) {
            System.out.println(strPrintLine);
        }
    }
}
