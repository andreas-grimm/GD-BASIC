package eu.gricom.interpreter.basic.tokenizer;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;

/**
 * Normalizer.java
 *
 * Description:
 *
 * The Normalizer class formats the string received in such a way that the string is prepared for consumption by the
 * Lexer.
 *
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
        boolean bArrayParenthenes = false;

        // replace tabs with spaces
        strWork = strWork.replace("\t", "    ");

        // ignore anything in quotation marks (") and add it to the string
        char cPreviousChar = '\0';
        char cCurrentChar = '\0';

        for (int i = 0; i < strWork.length(); i++) {
            cPreviousChar = cCurrentChar;
            cCurrentChar = strWork.charAt(i);

            if (cCurrentChar == '('
                    && (cPreviousChar == '$'
                        || cPreviousChar == '#'
                        || cPreviousChar == '!'
                        || cPreviousChar == '%'
                        || cPreviousChar == '&'
                        || cPreviousChar == '@')) {
                bArrayParenthenes = true;
            }

            if (cCurrentChar == '"') {
                bQuotationMark = !bQuotationMark;
            }

            // if the quotation mark is not set, then just pass thru...
            if (bQuotationMark || bArrayParenthenes) {
                strOutput += cCurrentChar;
                if (cCurrentChar == ')' && bArrayParenthenes) {
                    bArrayParenthenes = false;
                }
            } else {
                // else apply filters
                switch (cCurrentChar) {
                    case ',':
                        strOutput += " ,";
                        break;
                    case ';':
                        strOutput += " ;";
                        break;
                    case '(':
                        strOutput += " ( ";
                        break;
                    case ')':
                        strOutput += " ) ";
                        break;
                    default:
                        strOutput += cCurrentChar;
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
    public static String normalizeIndex(final String strInput) throws SyntaxErrorException {
        String strWork = strInput;

        int iIndexStart = strWork.indexOf("(");
        int iIndexEnd = strWork.indexOf(")");

        if (iIndexStart < 0 && iIndexEnd < 0) {
            return strWork;
        } else {
            if (iIndexStart < 0 || iIndexEnd < 0) {
                throw new SyntaxErrorException("Parenthesis incorrectly set: " + strInput);
            }
        }

        // replace tabs with spaces
        strWork = strWork.substring(iIndexStart + 1, iIndexEnd);

        strWork = strWork.replaceAll("\t", " ");
        strWork = strWork.replaceAll("\\s+", "");

        strWork = strInput.substring(0, iIndexStart) + "-" + strWork;

        return strWork;
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
    public static String normalizeFunction(final String strInput) throws SyntaxErrorException {
        String strWork = strInput;

        // this function only runs if there are two parenthesis in the string
        int iIndexStart = strWork.indexOf("(");
        int iIndexEnd = strWork.indexOf(")");

        if (iIndexStart < 0 && iIndexEnd < 0) {
            return strWork;
        // if there is only one then I see a problem and throw an exception
        } else {
            if (iIndexStart < 0 || iIndexEnd < 0) {
                throw new SyntaxErrorException("Parenthesis incorrectly set: " + strInput);
            }
        }

        // ok - I know there are two parenthesis. Now lets see whether the word immediately connected to the
        // parenthesis is a keyword. This is also done in the Lexer, but here we need it to put appropiate spaces in
        // place

        // reduce the found word from the left parenthesis
        String strStub = strWork.substring(0, iIndexStart);
        strStub = strStub.substring(strStub.lastIndexOf(" ") + 1);

        // and check whether the rest is a keyword
        int iIndex = ReservedWords.getIndex(strStub);

        // we found a keyword
        if (iIndex > -1) {
            // the pre-scan shows that the scan will find a function. So we need to put extra spaces between the
            // function, the opening parentheses, the number in the parenthesis, and the closing parenthesis
            strWork = strWork.substring(0, iIndexStart) + " " + strWork.substring(iIndexStart);
        }

        // after the normalization above, () becomes " (  ) " - still needs to be removed
        strWork = strWork.replaceAll(" \\(  \\) ", "");

        return strWork;
    }
}
