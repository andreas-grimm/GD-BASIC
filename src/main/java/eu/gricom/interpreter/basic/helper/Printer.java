package eu.gricom.interpreter.basic.helper;

public class Printer {
    private static final String _strTarget = new String("Console");
    public static void println (String strPrintLine) {
        if (_strTarget.contains("Console")) {
            System.out.println(strPrintLine);
        }
    }
}
