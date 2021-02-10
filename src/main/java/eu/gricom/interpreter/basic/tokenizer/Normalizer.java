package eu.gricom.interpreter.basic.tokenizer;

/**
 * Normalizer.java
 * <p>
 * Description:
 * <p>
 * The Normalizer class formats the string received in such a way that the string is prepared for consumption by the
 * Lexer.
 * <p>
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public final class Normalizer {

    /**
     * private, hidden default constructor.
     */
    private Normalizer() {

    }

    /**
     * The normailze function converts the input string by removing un-needed spaces and ensures that the format of the
     * line in working with the lexer.
     *
     * @param strInput string to be adjusted
     * @return normalized string
     */
    public static String normalize(final String strInput) {
        String strOutput = strInput;

        return strOutput;
    }
}
