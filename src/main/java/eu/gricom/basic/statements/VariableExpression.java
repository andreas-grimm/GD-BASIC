package eu.gricom.basic.statements;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.memoryManager.VariableManagement;
import eu.gricom.basic.tokenizer.Normalizer;
import eu.gricom.basic.variableTypes.Value;

/**
 * VariableExpression.java
 * <p>
 * Description:
 * <p>
 * A variable expression evaluates to the current value stored in that variable.
 * <p>
 * (c) = 2004,...,2016 by Andreas Grimm, Den Haag, The Netherlands
 * <p>
 * Created in 2020
 *
 */
public final class VariableExpression implements Expression {

    private final String _strName;

    /**
     * Default constructor.
     *
     * @param strName name of the variable.
     */
    public VariableExpression(final String strName) {

        _strName = strName;
    }

    /**
     * Evaluates the variable expression and returns the current value of the referenced variable.
     *
     * Supports variables with array-like indexing, including indices that are themselves variable expressions. Resolves and normalizes indices before evaluation. Throws an exception if the variable does not exist or if an error occurs during evaluation, such as an out-of-bounds array index.
     *
     * @return the value of the referenced variable
     * @throws Exception if the variable does not exist or if an error occurs during evaluation
     */
    public Value evaluate() throws Exception {
        VariableManagement oVariableManager = new VariableManagement();
        String strKey = _strName;

        int iIndexStart = strKey.indexOf("(");
        int iIndexEnd = strKey.indexOf(")");

        if (iIndexStart > 0 && iIndexEnd > 0) {
            String strInner = strKey.substring(iIndexStart + 1, iIndexEnd);

            if (strInner.contains(",")) {
                String[] astrCommaSeperatedList = strInner.split(",");
                StringBuilder strCommaSeperatedList = new StringBuilder();

                for (String strExpression: astrCommaSeperatedList) {
                    String strValue = strExpression;
                    if (oVariableManager.mapContainsKey(strExpression)) {
                        Expression oExpression = new VariableExpression(strExpression);

                        strValue = oExpression.evaluate().toString();
                    }

                    strCommaSeperatedList.append(strValue).append(",");
                }

                strKey = strKey.substring(0, iIndexStart + 1)
                        + strCommaSeperatedList.substring(0, strCommaSeperatedList.length() - 1)
                        + strKey.substring(iIndexEnd);

            } else {
                if (oVariableManager.mapContainsKey(strInner)) {
                    Expression oExpression = new VariableExpression(strInner);

                    strKey = strKey.substring(0, iIndexStart + 1)
                            + oExpression.evaluate().toString()
                            + strKey.substring(iIndexEnd);

                }
            }

            strKey = Normalizer.normalizeIndex(strKey);
        }

        if (oVariableManager.mapContainsKey(strKey)) {
            return oVariableManager.getMap(strKey);
        }

        throw new RuntimeException("Unknown variable <" + strKey + ">");
    }

    /**
     * Returns the name of the variable represented by this expression.
     *
     * @return the variable name as a string
     */
    public String getName() {

        return _strName;
    }

    /**
     * Returns the variable name represented by this expression.
     *
     * Primarily used for testing and debugging to display the assigned variable name.
     *
     * @return the variable name as a string
     */
    public String content() {

        return getName();
    }

    /**
     * Returns a JSON-like string representing the structure of this variable expression.
     *
     * <p>
     * The returned string includes the statement type ("VARIABLE") and the variable name.
     *
     * @return a JSON-like string describing the variable expression's structure
     * @throws Exception if an error occurs during structure generation
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"VARIABLE\": {";
        strReturn += "\"NAME\": \""+ _strName +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
