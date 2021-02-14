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
        String strWork = strInput;
        String strOutput = new String();

        boolean bQuotationMark = false;

        // replace tabs with spaces
        strWork = strWork.replace("\t", "    ");

        // ignore anything in quotation marks (") and add it to the string
        for (int i = 0; i < strWork.length(); i++) {
            char c = strWork.charAt(i);

            if (c == '"') {
                if (bQuotationMark == false) {
                    bQuotationMark = true;
                } else {
                    bQuotationMark = false;
                };
            }

            // if the quotation mark is not set, then just pass thru...
            if (bQuotationMark == true) {
                strOutput += c;
            } else {
                // else apply filters
                switch (c) {
                    case ',':
                        strOutput += " " + c;
                        break;
                    case ';':
                        strOutput += " " + c;
                        break;
                    default:
                        strOutput += c;
                }
            }
        }
        return strOutput;
    }
}
