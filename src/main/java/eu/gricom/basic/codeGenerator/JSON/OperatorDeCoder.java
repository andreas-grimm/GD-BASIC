package eu.gricom.basic.codeGenerator.JSON;

import com.google.gson.JsonObject;
import eu.gricom.basic.helper.Logger;
import eu.gricom.basic.statements.Expression;
import eu.gricom.basic.statements.OperatorExpression;
import eu.gricom.basic.statements.VariableExpression;
import eu.gricom.basic.tokenizer.BasicTokenType;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;

public class OperatorDeCoder {
    Logger _oLogger;

    public OperatorDeCoder() {
    }

    public Expression deCodeOperator(JsonObject oOperatorData) {
        _oLogger = new Logger(this.getClass().getName());
        ExpressionDeCoder oExpressionDeCoder = new ExpressionDeCoder();

        _oLogger.debug("JSONOperator: " + oOperatorData);

        Expression oLeftExpression = null;
        Expression oRightExpression = null;

        if (oOperatorData.get("LEFT_EXPRESSION").isJsonObject()) {
            _oLogger.debug("Left Expression is Json Object: " + oOperatorData.get("LEFT_EXPRESSION"));
            oLeftExpression = oExpressionDeCoder.expressionDeCoder(oOperatorData.get("LEFT_EXPRESSION").getAsJsonObject());
        } else if (oOperatorData.get("LEFT_EXPRESSION").isJsonPrimitive()) {
            _oLogger.debug("Left Expression is Json Primitive: " + oOperatorData.get("LEFT_EXPRESSION"));
            if (oOperatorData.get("LEFT_EXPRESSION").getAsJsonPrimitive().isString()) {
                oLeftExpression = new VariableExpression(oOperatorData.get("LEFT_EXPRESSION").getAsJsonPrimitive().getAsString());
            }  else {
                if (oOperatorData.get("LEFT_EXPRESSION").getAsJsonPrimitive().isNumber()) {
                    _oLogger.debug("Found JSONPrimitive as Number");
                    oLeftExpression = new RealValue((oOperatorData.get("LEFT_EXPRESSION").getAsDouble()));
                }
            }
        }

        if (oOperatorData.get("RIGHT_EXPRESSION").isJsonObject()) {
            _oLogger.debug("Right Expression is Json Object: " + oOperatorData.get("RIGHT_EXPRESSION"));
            oRightExpression = oExpressionDeCoder.expressionDeCoder(oOperatorData.get("RIGHT_EXPRESSION").getAsJsonObject());
        } else if (oOperatorData.get("RIGHT_EXPRESSION").isJsonPrimitive()) {
            _oLogger.debug("Right Expression is Json Primitive: " + oOperatorData.get("RIGHT_EXPRESSION"));
            if (oOperatorData.get("RIGHT_EXPRESSION").getAsJsonPrimitive().isString()) {
                _oLogger.debug("Found JSONPrimitive as String");
                oRightExpression = new StringValue((oOperatorData.get("RIGHT_EXPRESSION").getAsString()));
            } else
            if (oOperatorData.get("RIGHT_EXPRESSION").getAsJsonPrimitive().isNumber()) {
                _oLogger.debug("Found JSONPrimitive as Number");
                oRightExpression = new RealValue((oOperatorData.get("RIGHT_EXPRESSION").getAsDouble()));
            } else
                _oLogger.error("Found JSONPrimitive without type?");
        }

        OperatorExpression oExpression = null;

        try {
            if (!oOperatorData.get("OPERATOR_STRING").getAsJsonObject().isJsonNull()) {
                _oLogger.debug("Operator String: " + oOperatorData.get("OPERATOR_STRING").getAsString());
                oExpression = new OperatorExpression(oLeftExpression, oOperatorData.get("OPERATOR_STRING").getAsString(), oRightExpression);
            }
        } catch (NullPointerException e) {oExpression = null;}

        try {
            String strOperatorObject = oOperatorData.get("OPERATOR_OBJECT").getAsString();
            if (strOperatorObject != null) {
                _oLogger.debug("Operator Object: " + strOperatorObject);
                BasicTokenType oOperatorObject = BasicTokenType.valueOf(strOperatorObject);
                oExpression = new OperatorExpression(oLeftExpression, oOperatorObject, oRightExpression);
            }
        }  catch (NullPointerException e) {}

        return oExpression;
    }
}
