package eu.gricom.interpreter.basic.tokenizer;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.error.SyntaxErrorException;

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
     * The normalize function converts the input string by removing un-needed spaces and ensures that the format of the
     * line in working with the lexer.
     *
     * @param strInput string to be adjusted
     * @return normalized string
     */
    public static String normalize(final String strInput) {
        String strWork = strInput;
        String strOutput = new String();

        boolean bQuotationMark = false;
        boolean bParenthenes = false;

        // replace tabs with spaces
        strWork = strWork.replace("\t", "    ");

        // ignore anything in quotation marks (") and add it to the string
        for (int i = 0; i < strWork.length(); i++) {
            char c = strWork.charAt(i);

            if (c == '(') {
                bParenthenes = true;
            }

            if (c == ')') {
                bParenthenes = false;
            }

            if (c == '"') {
                if (bQuotationMark == false) {
                    bQuotationMark = true;
                } else {
                    bQuotationMark = false;
                };
            }

            // if the quotation mark is not set, then just pass thru...
            if (bQuotationMark == true
                || bParenthenes == true) {
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

    /**
     * The normalizeIndex function converts the input string by removing un-needed spaces and ensures that the format of
     * the
     * line in working with the lexer.
     *
     * @param strInput string to be adjusted
     * @return normalized string
     * @throws SyntaxErrorException if the parenthesis are not set correctly
     */
    public static String normalizeIndex(final String strInput) throws RuntimeException {
        String strWork = strInput;

        int iIndexStart = strWork.indexOf("(");
        int iIndexEnd = strWork.indexOf(")");

        if (iIndexStart < 0 && iIndexEnd < 0) {
            return strWork;
        } else {
            if (iIndexStart < 0 || iIndexEnd < 0) {
                throw new RuntimeException("Parenthesis incorrectly set: " + strInput);
            }
        }

        // replace tabs with spaces
        strWork = strWork.substring(iIndexStart +1, iIndexEnd);

        strWork = strWork.replaceAll("\t", " ");
        strWork = strWork.replaceAll("\\s+","");

        strWork = strInput.substring(0, iIndexStart) + "-" + strWork;

        return strWork;
    }
}
