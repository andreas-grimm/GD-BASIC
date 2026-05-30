package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.VariableManagement;
import eu.gricom.basic.variableTypes.Value;

import java.util.List;
import java.util.StringJoiner;

/**
 * ArrayAssignStatement.java
 * <p>
 * Description: Evaluates an assignment to an array element where the index is a full expression.
 * Each index expression is evaluated to an integer at runtime. The result is used to build the
 * storage key for {@link VariableManagement}.
 * <p>
 * Example BASIC: {@code N%(V% + 1) = 42}
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class ArrayAssignStatement implements Statement {

    private final int _iTokenNumber;
    private final String _strArrayName;
    private final List<Expression> _aoIndexExpressions;
    private final Expression _oValue;
    private final VariableManagement _oVariableManager = new VariableManagement();

    /**
     * Constructor.
     *
     * @param iTokenNumber       token position in the source program (for error reporting)
     * @param strArrayName       array variable name including type suffix, e.g. {@code "N%"}
     * @param aoIndexExpressions one expression per dimension
     * @param oValue             expression that produces the value to store
     */
    public ArrayAssignStatement(final int iTokenNumber,
                                final String strArrayName,
                                final List<Expression> aoIndexExpressions,
                                final Expression oValue) {
        _iTokenNumber = iTokenNumber;
        _strArrayName = strArrayName;
        _aoIndexExpressions = aoIndexExpressions;
        _oValue = oValue;
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
     * Evaluates all index expressions and the value expression, then stores the result.
     *
     * @throws Exception if any expression fails to evaluate
     */
    @Override
    public void execute() throws Exception {
        String strKey = buildKey();
        Value oResult = _oValue.evaluate();
        _oVariableManager.putMap(strKey, oResult);
    }

    /**
     * Content.
     *
     * @return human-readable description of the statement
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String content() throws Exception {
        return "ARRAY_ASSIGN [" + _strArrayName + "(...) := " + _oValue.content() + "]";
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
        StringBuilder oBuilder = new StringBuilder("{\"ARRAY_ASSIGN\": {");
        oBuilder.append("\"TOKEN_NR\": \"").append(_iTokenNumber).append("\",");
        oBuilder.append("\"NAME\": \"").append(_strArrayName).append("\",");
        oBuilder.append("\"INDICES\": [");
        for (int i = 0; i < _aoIndexExpressions.size(); i++) {
            oBuilder.append(_aoIndexExpressions.get(i).structure());
            if (i < _aoIndexExpressions.size() - 1) {
                oBuilder.append(",");
            }
        }
        oBuilder.append("],");
        oBuilder.append("\"EXPRESSION\": ").append(_oValue.structure());
        oBuilder.append("}}");
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
