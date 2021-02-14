package eu.gricom.interpreter.basic.helper;

/**
 * Printer.java
 * <p>
 * Description:
 * <p>
 * Static class to bundle all outputs as part of the program execution.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
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
