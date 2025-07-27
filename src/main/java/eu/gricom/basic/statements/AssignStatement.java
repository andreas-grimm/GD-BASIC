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
     * Default constructor.
     *
     * @param iTokenNumber - number of the basic command line
     * @param strName - target of the assign statement
     * @param oExpression - value of the assignment statement
     */
    public AssignStatement(final int iTokenNumber, final String strName, final Expression oExpression) {
        _strKey = strName;
        _oExpression = oExpression;
        _iTokenNumber = iTokenNumber;
    }

    /**
     * Default constructor.
     *
     * @param strName - target of the assign statement
     * @param oExpression - value of the assignment statement
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
     * The assignment is defined as part of the default constructor. But only here the transaction is actually
     * executed. After the execution, the variable is assigned.
     *
     * @throws Exception - any exception coming from the memory management
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
     * This method is used in testing and debugging. It returns the set values when the constructor has been called.
     *
     * @return - readable string with the name and the value of the assignment
     */
    @Override
    public String content() {
        return "ASSIGN [" + _strKey + ":= " + _oExpression.content() + "]";
    }

    /**
     * Structure.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("INPUT") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
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
