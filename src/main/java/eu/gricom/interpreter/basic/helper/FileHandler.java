package eu.gricom.interpreter.basic.helper;

import java.io.*;
import java.nio.charset.Charset;

public class FileHandler {
    private static Logger _oLogger = new Logger(FileHandler.class.getClass().getName());

    /**I missed
     * Reads the file from the given path and returns its contents as a single
     * string.
     *
     * @param  strPath  Path to the text file to read.
     * @return          The contents of the file or null if the load failed.
     */
    public static String readFile(String strPath) {
        try {
            FileInputStream oStream = new FileInputStream(strPath);

            try {
                // define the input into the interpreter
                InputStreamReader oInput = new InputStreamReader(oStream, Charset.defaultCharset());
                Reader oReader = new BufferedReader(oInput);

                // read the file in blocks of 8K until EOF
                StringBuilder oBuilder = new StringBuilder();
                char[] aBuffer = new char[8192];
                int iRead;

                while ((iRead = oReader.read(aBuffer, 0, aBuffer.length)) > 0) {
                    oBuilder.append(aBuffer, 0, iRead);
                }

                // HACK: The parser expects every statement to end in a newline, even the very last one, so we'll just
                // tack one on here in case the file doesn't have one.
                oBuilder.append("\n");

                _oLogger.debug("Read File:\n" + oBuilder.toString());
                return (oBuilder.toString());
            } finally {
                oStream.close();
            }
        } catch (IOException ex) {
            _oLogger.error("[ERR-IO-EXCEPT] Program file could not be read.");
            System.exit(-1);
        }
        return (null);
    }
}
