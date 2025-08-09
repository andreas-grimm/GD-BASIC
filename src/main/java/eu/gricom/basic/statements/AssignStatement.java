package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.VariableManagement;
import eu.gricom.basic.tokenizer.Normalizer;

/**
 * AssignStatement.java
 * <p>
 * Description:
 * <p>
 * An assignment statement evaluates an expression and stores the result in a variable.
 * <p>
 * (c) = 2004,...,2016 by Andreas Grimm, Den Haag, The Netherlands
 * <p>
 * Created in 2003
 *
 */
public final class AssignStatement implements Statement {
    private final String _strKey;
    private final Expression _oExpression;
    private final int _iTokenNumber;
    private final VariableManagement _oVariableManagement = new VariableManagement();

    /**
     * Constructs an assignment statement with the specified token number, target variable name, and expression to assign.
     *
     * @param iTokenNumber the token or line number associated with this statement
     * @param strName the name or key of the variable to assign the result to
     * @param oExpression the expression whose evaluated result will be assigned
     */
    public AssignStatement(final int iTokenNumber, final String strName, final Expression oExpression) {
        _strKey = strName;
        _oExpression = oExpression;
        _iTokenNumber = iTokenNumber;
    }

    /**
     * Constructs an assignment statement with the specified target variable and expression, using a default token number of 0.
     *
     * @param strName the name of the variable to assign to
     * @param oExpression the expression whose evaluated result will be assigned
     */
    public AssignStatement(final String strName, final Expression oExpression) {
        _strKey = strName;
        _oExpression = oExpression;
        _iTokenNumber = 0;
    }

    /**
     * Get Token Number.
     *
     * @return the command line number of the statement
     */
    @Override
    public int getTokenNumber() {
        return _iTokenNumber;
    }

    /**
     * Executes the assignment statement by evaluating the expression and storing its result in the target variable.
     *
     * If the target variable includes index or function-like notation, any variable references within the indices are resolved before assignment. The evaluated result is stored in the variable management system under the processed key.
     *
     * @throws Exception if an error occurs during variable resolution, expression evaluation, or variable assignment.
     */
    @Override
    public void execute() throws Exception {
        String strKey = _strKey;

        // here the found word could be an array or a function... first determine the being and the end position of
        // the bracketed part...
        int iIndexStart = strKey.indexOf("(");
        int iIndexEnd = strKey.indexOf(")");

        if (iIndexStart > 0 && iIndexEnd > 0) {
            String strInner = strKey.substring(iIndexStart + 1, iIndexEnd);

            if (strInner.contains(",")) {
                String[] astrCommaSeperatedList = strInner.split(",");
                StringBuilder strCommaSeperatedList = new StringBuilder();

                for (String strExpression: astrCommaSeperatedList) {
                    String strValue = strExpression;
                    if (_oVariableManagement.mapContainsKey(strExpression)) {
                        Expression oExpression = new VariableExpression(strExpression);

                        strValue = oExpression.evaluate().toString();
                    }

                    strCommaSeperatedList.append(strValue).append(",");
                }

                strKey = strKey.substring(0, iIndexStart + 1)
                        + strCommaSeperatedList.substring(0, strCommaSeperatedList.length() - 1)
                        + strKey.substring(iIndexEnd);

            } else {
                if (_oVariableManagement.mapContainsKey(strInner)) {
                    Expression oExpression = new VariableExpression(strInner);

                    strKey = strKey.substring(0, iIndexStart + 1)
                            + oExpression.evaluate().toString()
                            + strKey.substring(iIndexEnd);

                }
            }

            strKey = Normalizer.normalizeIndex(strKey);
        }

        _oVariableManagement.putMap(strKey, _oExpression.evaluate());
    }

    /**
     * Returns a string representation of the assignment statement, showing the target variable and the expression content.
     *
     * @return a readable string displaying the assignment target and the assigned expression.
     */
    @Override
    public String content() {
        return "ASSIGN [" + _strKey + ":= " + _oExpression.content() + "]";
    }

    /**
     * Returns a JSON-like string describing the structure of this assignment statement.
     *
     * The returned string includes the statement type ("ASSIGN"), token number, target variable name, and the structure of the assigned expression.
     *
     * @return a JSON-like string representing the structure of the assignment statement
     * @throws Exception if an error occurs while retrieving the structure of the expression
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"ASSIGN\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\",";
        strReturn += "\"NAME\": \""+ _strKey +"\",";
        strReturn += "\"EXPRESSION\": "+ _oExpression.structure();
        strReturn += "}}";
        return strReturn;
    }
}
