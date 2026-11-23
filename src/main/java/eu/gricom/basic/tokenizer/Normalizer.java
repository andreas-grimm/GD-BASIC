package eu.gricom.basic.tokenizer;

import eu.gricom.basic.error.SyntaxErrorException;

/**
 * Normalizer.java
 * <p>
 * Description: The Normalizer class preprocesses BASIC source code lines before tokenisation. It standardises spacing
 * around operators and delimiters, handles array index notation, and ensures consistent formatting that the Lexer can
 * reliably parse.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class Normalizer {

    /**
     * private, hidden default constructor.
     */
    private Normalizer() {

    }

    /**
     * The normalise function converts the input string by removing un-needed spaces and ensures that the format of the
     * line in working with the lexer.
     *
     * @param strInput string to be adjusted
     * @return normalised string
     */
    public static String normalize(final String strInput) {
        String strWork = strInput;
        String strOutput = new String();

        boolean bQuotationMark = false;
        boolean bSquareBrackets = false;
        int iParenthesisDepth = 0;

        // replace tabs with spaces
        strWork = strWork.replace("\t", "    ");

        // ignore anything in quotation marks (") and add it to the string
        char cPreviousChar = '\0';
        char cCurrentChar = '\0';

        for (int iIndex = 0; iIndex < strWork.length(); iIndex++) {
            cPreviousChar = cCurrentChar;
            cCurrentChar = strWork.charAt(iIndex);

            // a "[" inside a string is text, not an index (Phase 7 fix: it used to remove the spaces of the string)
            if (cCurrentChar == '[' && !bQuotationMark) {
                bSquareBrackets = true;
            }

            // if the quotation mark is not set, then just pass thru...
            if (cCurrentChar == '"') {
                bQuotationMark = !bQuotationMark;
            }

            // track parenthesis depth when not in quotes or square brackets
            if (!bQuotationMark && !bSquareBrackets) {
                if (cCurrentChar == '(') {
                    iParenthesisDepth++;
                } else if (cCurrentChar == ')') {
                    iParenthesisDepth--;
                }
            }

            // now check whether we are between square brackets [] - here remove all spaces
            if (bSquareBrackets) {
                if (cCurrentChar != ' ') {
                    strOutput += cCurrentChar;
                }

                if (cCurrentChar == ']') {
                    bSquareBrackets = false;
                }
            } else if (bQuotationMark) {
                strOutput += cCurrentChar;
            } else {
                // else apply filters
                switch (cCurrentChar) {
                    case ',':
                        strOutput += " , ";
                        break;
                    case ';':
                        strOutput += " ; ";
                        break;
                    case ':':
                        strOutput += " : ";
                        break;
                    case '(':
                        strOutput += " ( ";
                        break;
                    case ')':
                        strOutput += " ) ";
                        break;
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                    case '^':
                    case '&':
                    case '|':
                        // Inside parentheses, add spaces around arithmetic operators
                        // Skip spaces for unary minus/plus (right after '(' or ',')
                        if (iParenthesisDepth > 0 && cPreviousChar != '(' && cPreviousChar != ',') {
                            strOutput += " " + cCurrentChar + " ";
                        } else {
                            strOutput += cCurrentChar;
                        }
                        break;
                    case '=':
                    case '>':
                    case '<':
                        // Comparison operators: don't add spaces to avoid breaking multi-char operators
                        // (e.g., >=, <=, ==, !=, <<, >>)
                        strOutput += cCurrentChar;
                        break;
                    case '!':
                        // "!" directly after a name, outside brackets, is the boolean suffix (decision A7).
                        // "A!=TRUE" becomes "A! = TRUE" (Boolean assignment). Inside brackets, and anywhere
                        // else, "!" stays part of an operator such as "!=" (comparison).
                        if (iParenthesisDepth == 0 && isBooleanSuffix(strWork, iIndex, strOutput)) {
                            strOutput += "!";
                            if (iIndex + 1 < strWork.length() && strWork.charAt(iIndex + 1) == '=') {
                                strOutput += " = ";
                                iIndex++;
                            }
                        } else {
                            strOutput += cCurrentChar;
                        }
                        break;
                    default:
                        strOutput += cCurrentChar;
                }
            }
        }
        return strOutput;
    }

    /**
     * Normalises spacing around operators to ensure exactly one space before and after each operator.
     * Handles multiple spaces and removes extra whitespace around arithmetic and assignment operators.
     * Respects variable type suffixes (%, $, #, &, !) which should not be treated as operators.
     * <p>
     * Not called. It was parked in an unmerged change and conflicts with the expected output of
     * NormalizerTest (operators outside parentheses are not spaced). The "!" suffix rule is in normalize() and
     * isBooleanSuffix(). Kept until the owner approves removal.
     *
     * @param strInput string with potentially multiple spaces
     * @return normalised string with consistent operator spacing
     */
    @Deprecated
    private static String normalizeOperatorSpacing(final String strInput) {
        String strWork = strInput;
        boolean bQuotationMark = false;

        StringBuilder oOutput = new StringBuilder();

        for (int iIndex = 0; iIndex < strWork.length(); iIndex++) {
            char cCurrentChar = strWork.charAt(iIndex);

            if (cCurrentChar == '"') {
                bQuotationMark = !bQuotationMark;
                oOutput.append(cCurrentChar);
            } else if (bQuotationMark) {
                oOutput.append(cCurrentChar);
            } else if (isOperator(cCurrentChar)) {
                // Check if this is a variable type suffix (%, $, #, &, !)
                // Type suffixes appear after alphanumeric characters
                if (isVariableTypeSuffix(cCurrentChar, oOutput, strWork, iIndex)) {
                    oOutput.append(cCurrentChar);
                } else {
                    // Check if this is part of a two-character operator (!=, <=, >=, <>)
                    String strTwoCharOp = getTwoCharOperator(strWork, iIndex);
                    if (strTwoCharOp != null) {
                        // Don't add spacing inside multi-character operators
                        while (oOutput.length() > 0 && oOutput.charAt(oOutput.length() - 1) == ' ') {
                            oOutput.deleteCharAt(oOutput.length() - 1);
                        }
                        oOutput.append(' ').append(strTwoCharOp).append(' ');
                        iIndex++; // Skip the next character (second char of two-char operator)
                        // Skip any spaces after operator
                        while (iIndex + 1 < strWork.length() && strWork.charAt(iIndex + 1) == ' ') {
                            iIndex++;
                        }
                    } else {
                        // This is a single-character operator
                        // Remove trailing spaces before operator
                        while (oOutput.length() > 0 && oOutput.charAt(oOutput.length() - 1) == ' ') {
                            oOutput.deleteCharAt(oOutput.length() - 1);
                        }
                        oOutput.append(' ').append(cCurrentChar).append(' ');

                        // Skip any spaces after operator
                        while (iIndex + 1 < strWork.length() && strWork.charAt(iIndex + 1) == ' ') {
                            iIndex++;
                        }
                    }
                }
            } else {
                oOutput.append(cCurrentChar);
            }
        }

        // Clean up any remaining multiple spaces but preserve single spaces
        String strResult = oOutput.toString();
        strResult = strResult.replaceAll("  +", " ");
        strResult = strResult.trim();

        return strResult;
    }

    /**
     * Determines if a character is a variable type suffix rather than an operator.
     * Type suffixes include %, $, #, &, ! and appear at the end of variable names.
     *
     * @param c character to check
     * @param strOutput current output being built
     * @param strInput original input string
     * @param iIndex current index in input string
     * @return true if this is a type suffix, false if it's an operator
     */
    private static boolean isVariableTypeSuffix(final char cChar, final StringBuilder oOutput,
                                                  final String strInput, final int iIndex) {
        // Only %, $, #, &, ! can be type suffixes
        if (!(cChar == '%' || cChar == '$' || cChar == '#' || cChar == '&' || cChar == '!')) {
            return false;
        }

        // A suffix belongs to a variable name. The name must start with a letter, so a number such as
        // "10#" never receives a suffix. The check is done first, so "A!=TRUE" is read as "A!" followed by "="
        // (Boolean assignment), while "A != 2" (space before !) stays the not-equal operator.
        if (!nameStartsWithLetter(oOutput)) {
            return false;
        }

        // Check if the character before this is alphanumeric or another type suffix
        // If so, this is likely a type suffix at the end of a variable name
        if (oOutput.length() > 0) {
            char cPreviousChar = oOutput.charAt(oOutput.length() - 1);
            if (Character.isLetterOrDigit(cPreviousChar) || isVariableTypeSuffixChar(cPreviousChar)) {
                // Now check what comes after: space, operator, or parenthesis
                // These all indicate this is the end of the variable name
                if (iIndex + 1 >= strInput.length()) {
                    return true; // End of line
                }
                char cNextChar = strInput.charAt(iIndex + 1);
                if (cNextChar == ' ' || isOperator(cNextChar) || cNextChar == '(' || cNextChar == ')'
                    || cNextChar == ',' || cNextChar == ';' || cNextChar == ':') {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks whether the word being built at the end of the output starts with a letter.
     * A word is a run of letters, digits and underscores directly before the current position.
     *
     * @param strOutput output built so far
     * @return true if the run of name characters at the end of the output starts with a letter
     */
    private static boolean nameStartsWithLetter(final CharSequence oOutput) {
        int iStart = oOutput.length();
        while (iStart > 0 && (Character.isLetterOrDigit(oOutput.charAt(iStart - 1))
                || oOutput.charAt(iStart - 1) == '_')) {
            iStart--;
        }
        return iStart < oOutput.length() && Character.isLetter(oOutput.charAt(iStart));
    }

    /**
     * Decides whether the "!" at a given position is the boolean suffix of a variable name (decision A7).
     * It is a suffix when it directly follows a name that starts with a letter, and it is followed by the end of the
     * line, a space, "=", a bracket, a comma, a semicolon or a colon.
     *
     * @param strInput the input line
     * @param iIndex position of the "!"
     * @param strOutput output built so far
     * @return true if the "!" is a boolean suffix
     */
    private static boolean isBooleanSuffix(final String strInput, final int iIndex, final String strOutput) {
        if (!nameStartsWithLetter(strOutput)) {
            return false;
        }
        if (iIndex + 1 >= strInput.length()) {
            return true;
        }
        char cNext = strInput.charAt(iIndex + 1);
        return cNext == ' ' || cNext == '=' || cNext == '(' || cNext == ')' || cNext == ','
                || cNext == ';' || cNext == ':';
    }

    /**
     * Checks if a character is a variable type suffix character.
     *
     * @param c character to check
     * @return true if character is %, $, #, &, or !
     */
    private static boolean isVariableTypeSuffixChar(final char cChar) {
        return cChar == '%' || cChar == '$' || cChar == '#' || cChar == '&' || cChar == '!';
    }

    /**
     * Determines if the character at the given index starts a two-character operator.
     * Two-character operators: ==, !=, <=, >=, <>
     *
     * @param strInput the input string
     * @param iIndex current index
     * @return the two-character operator string if found, null otherwise
     */
    private static String getTwoCharOperator(final String strInput, final int iIndex) {
        if (iIndex + 1 >= strInput.length()) {
            return null;
        }
        char cFirst = strInput.charAt(iIndex);
        char cSecond = strInput.charAt(iIndex + 1);

        if ((cFirst == '=' || cFirst == '!' || cFirst == '<' || cFirst == '>') && cSecond == '=') {
            return String.valueOf(cFirst) + cSecond;
        }
        if (cFirst == '<' && cSecond == '>') {
            return "<>";
        }
        return null;
    }

    /**
     * Checks if a character is an arithmetic or assignment operator.
     *
     * @param c character to check
     * @return true if character is an operator (+, -, *, /, ^, %, =, !)
     */
    private static boolean isOperator(final char cChar) {
        return cChar == '+' || cChar == '-' || cChar == '*' || cChar == '/' || cChar == '^' || cChar == '%' || cChar == '=' || cChar == '!';
    }

    /**
     * The normalizeIndex function converts the input string by removing un-needed spaces and ensures that the format of
     * the
     * line in working with the lexer.
     *
     * @param strInput string to be adjusted
     * @return normalised string
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
     * @return normalised string
     * @throws SyntaxErrorException if the parenthesis is not set correctly
     */
    public static String normalizeFunction(final String strInput) throws SyntaxErrorException {
        String strWork = strInput;

        // this function only runs if there are two parentheses in the string
        int iIndexStart = strWork.indexOf("(");
        int iIndexEnd = strWork.indexOf(")");

        if (iIndexStart < 0 && iIndexEnd < 0) {
            return strWork;
        // if there is only one, then I see a problem and throw an exception
        } else {
            if (iIndexStart < 0 || iIndexEnd < 0) {
                throw new SyntaxErrorException("Parenthesis incorrectly set: " + strInput);
            }
        }

        // ok - I know there are two parentheses. Now let's see whether the word immediately connected to the
        // parenthesis is a keyword. This is also done in the Lexer, but here we need it to put appropriate spaces in
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

        // after the normalisation above, () becomes " (  ) " - still needs to be removed
        strWork = strWork.replaceAll(" \\(  \\) ", "");

        return strWork;
    }
}
