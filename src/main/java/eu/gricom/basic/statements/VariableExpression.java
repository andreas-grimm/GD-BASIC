package eu.gricom.basic.statements;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.memoryManager.VariableManagement;
import eu.gricom.basic.tokenizer.Normalizer;
import eu.gricom.basic.variableTypes.Value;

/**
 * VariableExpression.java
 *
 * Description:
 *
 * A variable expression evaluates to the current value stored in that variable.
 *
 * (c) = 2004,..,2016 by Andreas Grimm, Den Haag, The Netherlands
 *
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
     * Return the content of the variable in the Memory Management component.
     *
     * @return returns the value of the variable - or if the variable does not exists - returns a 0 as a numerical value
     * @throws Exception for any errors occuring in the execution of the evaluation. Currently this happens if
     * the index in an array subscription is larger than the array.
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
                String strCommaSeperatedList = new String();

                for (String strExpression: astrCommaSeperatedList) {
                    String strValue = strExpression;
                    if (oVariableManager.mapContainsKey(strExpression)) {
                        Expression oExpression = new VariableExpression(strExpression);

                        strValue = oExpression.evaluate().toString();
                    }

                    strCommaSeperatedList += strValue + ",";
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
     * Get the name of the variable.
     *
     * @return name of the vaiable as a string.
     */
    public String getName() {

        return _strName;
    }

    /**
     * This method is used in testing and debugging. It returns the set values when the constructor has been called.
     *
     * @return - readable string with the name and the value of the assignment
     */
    public String content() {

        return getName();
    }

    /**
     * Structure.
     *
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("INPUT") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = this.toString();
        return strReturn;
    }
}
