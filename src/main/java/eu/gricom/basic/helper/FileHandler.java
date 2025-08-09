package eu.gricom.basic.helper;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

/**
 * FileHandler.java
 * <p>
 * Description:
 * <p>
 * The FileHandler class has a single function: read a source code file and move the content into a single string.
 * This might cause problems in case of really big programs, so this needs to be addressed later.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public final class FileHandler {
    private static final Logger _oLogger = new Logger(FileHandler.class.getClass().getName());
    private static String _strBasicFile;

    /**
     * default constructor.
     */
    private FileHandler() {
    }

    /**
     * Reads the entire contents of the file at the specified path and returns it as a single string.
     *
     * Appends a newline character to the end of the content to ensure parser compatibility. If the file cannot be opened, logs an error and terminates the program. Returns {@code null} if reading fails.
     *
     * @param strPath the path to the file to read
     * @return the file contents as a string, or {@code null} if reading fails
     */
    public static String readFile(final String strPath) {
        _strBasicFile = strPath;

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

                _oLogger.debug("Read File:\n" + oBuilder);
                return oBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                oStream.close();
            }
        } catch (IOException e) {
            _oLogger.error("[ERR-IO-EXCEPT] Program file could not be read.");
            System.exit(-1);
        }
        return null;
    }

    /****
     * Constructs and logs the intended output filename by replacing the extension of the last read file with the specified file type.
     *
     * If no file has been read previously, logs an error indicating the filename is empty.
     *
     * @param strFileType the file extension to use for the output filename
     */
    public static void writeFile(String strFileType) {
        if (_strBasicFile != null) {
            String strOutputFileName = _strBasicFile.substring(0, _strBasicFile.indexOf(".")) + "." + strFileType;
            _oLogger.debug("Write File:\n" + strOutputFileName);
        }
        _oLogger.error("Filename empty:\n");
    }
}
