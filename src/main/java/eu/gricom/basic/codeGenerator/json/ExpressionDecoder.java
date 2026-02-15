package eu.gricom.basic.codeGenerator.JSON;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.statements.Expression;
import eu.gricom.basic.statements.VariableExpression;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;

public final class ExpressionDeCoder {
    public static Expression expressionDeCoder(JsonPrimitive oJSONExpression) {
        Logger oLogger = new Logger("eu.gricom.basic.codeGenerator.JSON.ExpressionDeCoder.expressionDeCoder(primitive)");
        Expression oExpression = null;

        if (oJSONExpression.isString()) {
            oLogger.debug("Found JSONPrimitive as String");
            oExpression = new StringValue((oJSONExpression.getAsString()));
        } else
        if (oJSONExpression.isNumber()) {
            oLogger.debug("Found JSONPrimitive as Number");
            oExpression = new RealValue((oJSONExpression.getAsDouble()));
        } else {
            oLogger.error("Found JSONPrimitive without type?");
            System.exit(-1);
        }

        return oExpression;
    }

    public static Expression expressionDeCoder(JsonObject oJSONExpression) {
        Logger oLogger = new Logger("eu.gricom.basic.codeGenerator.JSON.ExpressionDeCoder.expressionDeCoder(object)");
        Expression oExpression = null;

        oLogger.debug("Expression: " + oJSONExpression);

        if (oJSONExpression.isJsonPrimitive()) {
            oExpression = expressionDeCoder(oJSONExpression.getAsJsonPrimitive());
        } else {
            oLogger.info("Found JSONObject");
            // check for Operator
            JsonElement oJsonExpression = oJSONExpression.get("OPERATOR");

            if (oJsonExpression != null) {
                oLogger.debug("Found JSONObject of type OPERATOR: " + oJsonExpression);
                OperatorDeCoder oOperatorDecoder = new OperatorDeCoder();
                oExpression = oOperatorDecoder.deCodeOperator(oJsonExpression.getAsJsonObject());
                return oExpression;
            }

            oJsonExpression = oJSONExpression.get("EXPRESSION");

            if (oJsonExpression != null) {
                ExpressionDeCoder oExpressionDeCoder = new ExpressionDeCoder();
                oExpression = oExpressionDeCoder.expressionDeCoder(oJsonExpression.getAsJsonPrimitive());
                return oExpression;
            }

            oJsonExpression = oJSONExpression.get("VARIABLE");

            if (oJsonExpression != null) {
                oLogger.debug("Found JSONObject of type VARIABLE: " + oJsonExpression);
                OperatorDeCoder oOperatorDecoder = new OperatorDeCoder();
                oExpression = new VariableExpression(oJsonExpression.getAsJsonObject().get("NAME").getAsString());
                try {
                    oLogger.debug("VARIABLE: " + oExpression.structure());
                } catch (Exception e) {
                    oLogger.error(e.getMessage());
                }
            }
        }

        return oExpression;
    }

    /**
     * Decode a JSON Document that represents a "CONDITION" block.
     *
     * @param oJSONCondition - JSON Object, inside of a condition block
     * @return Expression - Condition Object generated from JSON Object
     */
    public static Expression conditionDeCoder(JsonObject oJSONCondition) {
        Expression oCondition = null;

        if (oJSONCondition.isJsonPrimitive()) {
            oCondition = expressionDeCoder(oJSONCondition.getAsJsonPrimitive());
        } else {
            // check for Operator
            JsonElement oJsonExpression = oJSONCondition.get("OPERATOR");

            if (oJsonExpression != null) {
                OperatorDeCoder oOperatorDecoder = new OperatorDeCoder();
                oCondition = oOperatorDecoder.deCodeOperator(oJsonExpression.getAsJsonObject());
            }

            oJsonExpression = oJSONCondition.get("VARIABLE");

            if (oJsonExpression != null) {
                oCondition = new VariableExpression(oJsonExpression.getAsJsonObject().get("NAME").getAsString());
            }
        }

        return oCondition;
    }
}
