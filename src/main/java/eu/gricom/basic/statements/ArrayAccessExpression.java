package eu.gricom.basic.statements;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.memoryManager.VariableManagement;
import eu.gricom.basic.variableTypes.Value;

import java.util.List;
import java.util.StringJoiner;

/**
 * ArrayAccessExpression.java
 * <p>
 * Description: Evaluates a read access to an array element where the index is a full expression.
 * Each index expression is evaluated to an integer at runtime, the results are combined into the
 * storage key used by {@link VariableManagement}, and the corresponding value is returned.
 * <p>
 * Example BASIC: {@code PRINT N%(V% + 1)}
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class ArrayAccessExpression implements Expression {

    private final String _strArrayName;
    private final List<Expression> _aoIndexExpressions;
    private final VariableManagement _oVariableManager = new VariableManagement();

    /**
     * Constructor.
     *
     * @param strArrayName       the name of the array variable including its type suffix, e.g. {@code "N%"}
     * @param aoIndexExpressions one expression per dimension, evaluated left to right
     */
    public ArrayAccessExpression(final String strArrayName,
                                 final List<Expression> aoIndexExpressions) {
        _strArrayName = strArrayName;
        _aoIndexExpressions = aoIndexExpressions;
    }

    /**
     * Evaluates every index expression, builds the storage key, and returns the stored value.
     *
     * @return the {@link Value} stored at the resolved index
     * @throws Exception if an index expression fails to evaluate or the element does not exist
     */
    @Override
    public Value evaluate() throws Exception {
        String strKey = buildKey();

        if (!_oVariableManager.mapContainsKey(strKey)) {
            throw new SyntaxErrorException("Array element not found: " + strKey);
        }

        return _oVariableManager.getMap(strKey);
    }

    /**
     * Get the content in human-readable form.
     *
     * @return content as a string showing the array name and its index expressions
     */
    @Override
    public String content() {
        StringBuilder oBuilder = new StringBuilder(_strArrayName).append("(");
        for (int i = 0; i < _aoIndexExpressions.size(); i++) {
            oBuilder.append(_aoIndexExpressions.get(i).content());
            if (i < _aoIndexExpressions.size() - 1) {
                oBuilder.append(",");
            }
        }
        oBuilder.append(")");
        return oBuilder.toString();
    }

    /**
     * Structure.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return JSON-style structure string
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        StringBuilder oBuilder = new StringBuilder("{\"ARRAY_ACCESS\": {");
        oBuilder.append("\"NAME\": \"").append(_strArrayName).append("\",");
        oBuilder.append("\"INDICES\": [");
        for (int i = 0; i < _aoIndexExpressions.size(); i++) {
            oBuilder.append(_aoIndexExpressions.get(i).structure());
            if (i < _aoIndexExpressions.size() - 1) {
                oBuilder.append(",");
            }
        }
        oBuilder.append("]}}");
        return oBuilder.toString();
    }

    /**
     * Evaluates all index expressions and assembles the VariableManagement storage key.
     *
     * <p>Single dimension example: {@code "N%-3"}  Multi-dimension example: {@code "M%-2,4"}
     *
     * @return the assembled key string
     * @throws Exception if any index expression cannot be evaluated
     */
    private String buildKey() throws Exception {
        StringJoiner oJoiner = new StringJoiner(",");

        for (Expression oIndexExpr : _aoIndexExpressions) {
            Value oValue = oIndexExpr.evaluate();
            int iIndex = (int) oValue.toReal();
            oJoiner.add(String.valueOf(iIndex));
        }

        return _strArrayName + "-" + oJoiner;
    }
}
