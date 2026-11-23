package eu.gricom.basic.tokenizer;

import java.util.Locale;

/**
 * OperatorSpacer.java
 * <p>
 * Description: The OperatorSpacer class puts spaces around operators that are written without spaces, so that the
 * Lexer sees them as separate words. For example, the Lexer reads "A-B" as one word, but "A - B" as three tokens.
 * <p>
 * The rules, in plain words:
 * <ul>
 *   <li>Text inside quotation marks (") is never changed.</li>
 *   <li>Comment lines (REM, DIM, ', !) are never changed.</li>
 *   <li>* / ^ = &lt; &gt; and the two-character operators &lt;= &gt;= == != &lt;&lt; &gt;&gt; get a space before and after.</li>
 *   <li>A binary + or - (one that follows a name, a number, a closing parenthesis or a string) gets a space before and
 *       after. Example: "A-B" becomes "A - B".</li>
 *   <li>A unary + or - (one that follows a keyword, an opening parenthesis, a comma, or another operator) gets a space
 *       only after it, so that the sign and the name are separate words. Example: "PRINT -A" becomes "PRINT - A".</li>
 *   <li>A unary sign in front of a digit is kept as it is, so "-5" stays one number.</li>
 *   <li>The hyphen in the keywords END-IF and END-WHILE is kept as it is.</li>
 *   <li>Text inside square brackets (a string index such as W$[I-1]) is never changed. The Normalizer keeps that
 *       text together, and the interpreter reports an error for an arithmetic string index.</li>
 * </ul>
 * <p>
 * Array elements are written with round brackets, A(1), and those are spaced as usual. A name such as "A-1" can only
 * come from the array element A(1): the interpreter stores the element under the key "A-1" (see
 * ArrayAccessExpression and VariableManagement). A variable name never contains a minus sign, and the only words with
 * a minus sign are the keywords END-IF and END-WHILE. So "A-1" written by a programmer is always the subtraction
 * A - 1.
 * <p>
 * The percent (%), ampersand (&amp;), pipe (|) and exclamation mark (!) are not changed, because they are also type
 * suffixes (A%, A&amp;, A!) or are part of other keywords.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class OperatorSpacer {

    /** Two-character operators that the Lexer knows (see ReservedWords). */
    private static final String[] TWO_CHARACTER_OPERATORS = {"<=", ">=", "==", "!=", "<<", ">>"};

    private OperatorSpacer() {
    }

    /**
     * Adds spaces around operators that are written without spaces.
     *
     * @param strLine one normalised program line (without the line number)
     * @return the line with spaces around the operators
     */
    public static String space(final String strLine) {
        if (isCommentLine(strLine)) {
            return strLine;
        }

        StringBuilder oOutput = new StringBuilder();
        boolean bInString = false;
        boolean bInBrackets = false;
        int iIndex = 0;

        while (iIndex < strLine.length()) {
            char cChar = strLine.charAt(iIndex);

            if (cChar == '"') {
                bInString = !bInString;
                oOutput.append(cChar);
                iIndex++;
            } else if (bInString) {
                oOutput.append(cChar);
                iIndex++;
            } else if (cChar == '[') {
                // string index, for example W$[I-1]: the text inside the brackets is kept together
                bInBrackets = true;
                oOutput.append(cChar);
                iIndex++;
            } else if (cChar == ']') {
                bInBrackets = false;
                oOutput.append(cChar);
                iIndex++;
            } else if (bInBrackets) {
                oOutput.append(cChar);
                iIndex++;
            } else {
                String strTwo = iIndex + 1 < strLine.length() ? strLine.substring(iIndex, iIndex + 2) : "";
                if (isTwoCharacterOperator(strTwo)) {
                    oOutput.append(' ').append(strTwo).append(' ');
                    iIndex += 2;
                } else if (cChar == '+' || cChar == '-') {
                    iIndex = spaceSign(strLine, iIndex, oOutput);
                } else if (cChar == '*' || cChar == '/' || cChar == '^' || cChar == '=' || cChar == '<'
                        || cChar == '>') {
                    oOutput.append(' ').append(cChar).append(' ');
                    iIndex++;
                } else {
                    oOutput.append(cChar);
                    iIndex++;
                }
            }
        }
        return oOutput.toString();
    }

    /**
     * Handles a + or - sign at position i. Returns the next position to read.
     */
    private static int spaceSign(final String strLine, final int iPosition, final StringBuilder oOutput) {
        char cSign = strLine.charAt(iPosition);
        int iNext = iPosition + 1;

        // END-IF and END-WHILE are keywords with a hyphen: keep them as they are
        if (cSign == '-' && isHyphenatedKeyword(strLine, iPosition)) {
            oOutput.append(cSign);
            return iNext;
        }

        if (isOperandPosition(oOutput)) {
            // unary sign: a sign in front of a digit stays a negative number; otherwise separate the sign
            if (iNext < strLine.length() && (Character.isDigit(strLine.charAt(iNext))
                    || strLine.charAt(iNext) == '.')) {
                oOutput.append(cSign);
            } else {
                oOutput.append(cSign).append(' ');
            }
        } else {
            oOutput.append(' ').append(cSign).append(' ');
        }
        return iNext;
    }

    /** True when the previous significant character means that an operand (not an operator) comes next. */
    private static boolean isOperandPosition(final StringBuilder oOutput) {
        int iIndex = oOutput.length() - 1;
        while (iIndex >= 0 && oOutput.charAt(iIndex) == ' ') {
            iIndex--;
        }
        if (iIndex < 0) {
            return true;
        }

        char cLast = oOutput.charAt(iIndex);
        if ("(,=<>+-*/^:".indexOf(cLast) >= 0) {
            return true;
        }
        if (cLast == ')' || cLast == '"') {
            return false;
        }

        // a name, a number or a suffix: find the whole word and check whether it is a keyword (PRINT, THEN, AND ...)
        int iStart = iIndex;
        while (iStart >= 0 && isWordCharacter(oOutput.charAt(iStart))) {
            iStart--;
        }
        String strWord = oOutput.substring(iStart + 1, iIndex + 1);
        return !strWord.isEmpty() && ReservedWords.getIndex(strWord) != -1;
    }

    /** True when the hyphen at iPosition joins two parts that together are a keyword (END-IF, END-WHILE). */
    private static boolean isHyphenatedKeyword(final String strLine, final int iPosition) {
        int iLeft = iPosition - 1;
        while (iLeft >= 0 && isWordCharacter(strLine.charAt(iLeft))) {
            iLeft--;
        }
        int iRight = iPosition + 1;
        while (iRight < strLine.length() && isWordCharacter(strLine.charAt(iRight))) {
            iRight++;
        }
        String strCandidate = strLine.substring(iLeft + 1, iRight);
        return strCandidate.contains("-") && ReservedWords.getIndex(strCandidate.toUpperCase(Locale.ROOT)) != -1;
    }

    private static boolean isWordCharacter(final char cChar) {
        return Character.isLetterOrDigit(cChar) || cChar == '_' || cChar == '$' || cChar == '%' || cChar == '#'
                || cChar == '&' || cChar == '!' || cChar == '-';
    }

    private static boolean isTwoCharacterOperator(final String strTwo) {
        for (String strOperator : TWO_CHARACTER_OPERATORS) {
            if (strOperator.equals(strTwo)) {
                return true;
            }
        }
        return false;
    }

    /** Comment lines are left as they are, because the Lexer keeps their whole text. */
    private static boolean isCommentLine(final String strLine) {
        String strTrimmed = strLine.trim();
        if (strTrimmed.isEmpty()) {
            return true;
        }
        if (strTrimmed.startsWith("'") || strTrimmed.startsWith("!")) {
            return true;
        }
        String strFirstWord = strTrimmed.split("\\s+")[0].toUpperCase(Locale.ROOT);
        return "REM".equals(strFirstWord) || "DIM".equals(strFirstWord);
    }
}
