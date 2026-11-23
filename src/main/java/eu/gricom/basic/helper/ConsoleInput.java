package eu.gricom.basic.helper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * ConsoleInput.java
 * <p>
 * Description: Gives the whole program one reader for the console (standard input). A BufferedReader reads ahead
 * more text than one line. If each INPUT statement created its own reader, the text buffered by the first reader
 * would be lost, and the next INPUT would find no data (bug fixed in Phase 6). The reader is replaced only when
 * System.in itself has been replaced, for example by a test.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class ConsoleInput {

    private static InputStream _oSource = null;
    private static BufferedReader _oReader = null;

    /**
     * Private Constructor.
     */
    private ConsoleInput() {
    }

    /**
     * Returns the shared reader for standard input.
     *
     * @return the reader that is used by INPUT and by the line editor
     */
    public static synchronized BufferedReader getReader() {
        if (_oReader == null || _oSource != System.in) {
            _oSource = System.in;
            _oReader = new BufferedReader(new InputStreamReader(_oSource));
        }
        return _oReader;
    }
}
