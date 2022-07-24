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
     * readFile.
     * Reads the file from the given path and returns its contents as a single string.
     *
     * @param  strPath  Path to the text file to read.
     * @return          The contents of the file or null if the load failed.
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

    /**
     * writeFile.
     * Writes the target file for the compiler.
     *
     * @param  strFileType  Type of the generated file.
     */
    public static void writeFile(String strFileType) {
        if (_strBasicFile != null) {
            String strOutputFileName = _strBasicFile.substring(0, _strBasicFile.indexOf(".")) + "." + strFileType;
            _oLogger.debug("Write File:\n" + strOutputFileName);
        }
        _oLogger.error("Filename empty:\n");
    }
}
