package eu.gricom.basic.codeGenerator.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import eu.gricom.basic.statements.*;
import org.junit.jupiter.api.Test;

public class JSONProgramDeCoderTest {

    @Test
    public void testProcessRemStatement() {
        JsonObject oJsonObject;
        RemStatement oRemStatement;
        String strJsonString = "{\"REM\": {\"TOKEN_NR\": \"86\",\"REM_TEXT\": \"Comment\"}}";

        Gson oGsonProcessor = new Gson();
        try {
            oJsonObject = oGsonProcessor.fromJson(strJsonString, JsonObject.class);

            JSONDecoder oJSONDecoder = new JSONDecoder(strJsonString);
            oRemStatement = (RemStatement) oJSONDecoder.processRemStatement(oJsonObject);

            assert oRemStatement.structure().equals(strJsonString);
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing json string: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testProcessPrintStatement() {
        JsonObject oJsonObject;
        PrintStatement oPrintStatement;
        String[] astrJsonString = new String[]{
                "{\"PRINT\": {\"TOKEN_NR\": \"107\",\"EXPRESSION\": \"This line should be printed\",\"CRLF\": \"TRUE\"}}",
                "{\"PRINT\": {\"TOKEN_NR\": \"16\",\"EXPRESSION\": \"Hello, world!\",\"CRLF\": \"TRUE\"}}",
                "{\"PRINT\": {\"TOKEN_NR\": \"73\",\"EXPRESSION\": {\"VARIABLE\": {\"NAME\": \"b#\"}},\"CRLF\": \"TRUE\"}}"
        };

        for (String strJsonString: astrJsonString ) {
            Gson oGsonProcessor = new Gson();
            try {
                oJsonObject = oGsonProcessor.fromJson(strJsonString, JsonObject.class);

                JSONDecoder oJSONDecoder = new JSONDecoder(strJsonString);
                oPrintStatement = (PrintStatement) oJSONDecoder.processPrintStatement(oJsonObject);

                System.out.println(oPrintStatement.structure());
                System.out.println(strJsonString);

                assert oPrintStatement.structure().equals(strJsonString);
            } catch (JsonSyntaxException e) {
                System.err.println("Error parsing json string: " + e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void testProcessAssignStatement() {
        JsonObject oJsonObject;
        AssignStatement oAssignStatement;
        String[] astrJsonString = new String[]{
                "{\"ASSIGN\": {\"TOKEN_NR\": \"1\",\"NAME\": \"count#\",\"EXPRESSION\": 5.0}}",      //Test 1: Simple assignment of a float variable
                "{\"ASSIGN\": {\"TOKEN_NR\": \"1\",\"NAME\": \"count\",\"EXPRESSION\": 5}}",         //Test 2: Simple assignment of an integer variable
                "{\"ASSIGN\": {\"TOKEN_NR\": \"1\",\"NAME\": \"count\",\"EXPRESSION\": \"Test\"}}"}; //Test 3: Simple assignment of a String variable

        for (String strJsonString : astrJsonString) {
            Gson oGsonProcessor = new Gson();
            try {
                oJsonObject = oGsonProcessor.fromJson(strJsonString, JsonObject.class);

                JSONDecoder oJSONDecoder = new JSONDecoder(strJsonString);
                oAssignStatement = (AssignStatement) oJSONDecoder.processAssignStatement(oJsonObject);

                assert oAssignStatement.structure().equals(strJsonString);
            } catch (JsonSyntaxException e) {
                System.err.println("Error parsing json string: " + e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void testNegativeProcessAssignStatement() {
        JsonObject oJsonObject;
        AssignStatement oAssignStatement;
        String strJsonString = "{\"ASSIGN\": {\"TOKEN_NR\": \"1\",\"NAME\": \"count#\",\"EXPRESSION\": 5}}";

        Gson oGsonProcessor = new Gson();
        try {
            oJsonObject = oGsonProcessor.fromJson(strJsonString, JsonObject.class);

            JSONDecoder oJSONDecoder = new JSONDecoder(strJsonString);
            oAssignStatement = (AssignStatement) oJSONDecoder.processAssignStatement(oJsonObject);

            assert !oAssignStatement.structure().equals(strJsonString);
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing json string: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testProcessAssignStatement_Complex() {
        JsonObject oJsonObject;
        AssignStatement oAssignStatement;
        String[] astrJsonString = new String[]{
                "{\"ASSIGN\": {\"TOKEN_NR\": \"49\",\"NAME\": \"a#\",\"EXPRESSION\": {\"OPERATOR\": {\"LEFT_EXPRESSION\": {\"VARIABLE\": {\"NAME\": \"a#\"}},\"OPERATOR_OBJECT\": \"POWER\",\"RIGHT_EXPRESSION\": 5.0}}}}",
                "{\"ASSIGN\": {\"TOKEN_NR\": \"68\",\"NAME\": \"b#\",\"EXPRESSION\": {\"OPERATOR\": {\"LEFT_EXPRESSION\": {\"VARIABLE\": {\"NAME\": \"b#\"}},\"OPERATOR_OBJECT\": \"MULTIPLY\",\"RIGHT_EXPRESSION\": 2.0}}}}",
                "{\"ASSIGN\": {\"TOKEN_NR\": \"35\",\"NAME\": \"a#\",\"EXPRESSION\": {\"OPERATOR\": {\"LEFT_EXPRESSION\": 1.0,\"OPERATOR_OBJECT\": \"PLUS\",\"RIGHT_EXPRESSION\": {\"OPERATOR\": {\"LEFT_EXPRESSION\": {\"OPERATOR\": {\"LEFT_EXPRESSION\": 2.0,\"OPERATOR_OBJECT\": \"MULTIPLY\",\"RIGHT_EXPRESSION\": 3.0}},\"OPERATOR_OBJECT\": \"DIVIDE\",\"RIGHT_EXPRESSION\": 4.0}}}}}}"};
        for (String strJsonString : astrJsonString) {
            Gson oGsonProcessor = new Gson();
            try {
                oJsonObject = oGsonProcessor.fromJson(strJsonString, JsonObject.class);

                JSONDecoder oJSONDecoder = new JSONDecoder(strJsonString);
                oAssignStatement = (AssignStatement) oJSONDecoder.processAssignStatement(oJsonObject);

                assert oAssignStatement.structure().equals(strJsonString);
            } catch (JsonSyntaxException e) {
                System.err.println("Error parsing json string: " + e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void testProcessAssignStatement_Complex_Multiply() {
        JsonObject oJsonObject;
        AssignStatement oAssignStatement;
        String strJsonString = "{\"ASSIGN\": {\"TOKEN_NR\": \"35\",\"NAME\": \"a#\",\"EXPRESSION\": {\"OPERATOR\": {\"LEFT_EXPRESSION\": 1.0,\"OPERATOR_OBJECT\": \"PLUS\",\"RIGHT_EXPRESSION\": {\"OPERATOR\": {\"LEFT_EXPRESSION\": {\"OPERATOR\": {\"LEFT_EXPRESSION\": 2.0,\"OPERATOR_OBJECT\": \"MULTIPLY\",\"RIGHT_EXPRESSION\": 3.0}},\"OPERATOR_OBJECT\": \"DIVIDE\",\"RIGHT_EXPRESSION\": 4.0}}}}}}";

        Gson oGsonProcessor = new Gson();
        try {
            oJsonObject = oGsonProcessor.fromJson(strJsonString, JsonObject.class);

            JSONDecoder oJSONDecoder = new JSONDecoder(strJsonString);
            oAssignStatement = (AssignStatement) oJSONDecoder.processAssignStatement(oJsonObject);

//            System.out.println(oAssignStatement.structure());
//            System.out.println(strJsonString);

            assert oAssignStatement.structure().equals(strJsonString);
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing json string: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testProcessGotoStatement() {
        JsonObject oJsonObject;
        GotoStatement oGotoStatement;
        String strJsonString = "{\"GOTO\": {\"TOKEN_NR\": \"80\",\"TARGET\": \"340\"}}";

        Gson oGsonProcessor = new Gson();
        try {
            oJsonObject = oGsonProcessor.fromJson(strJsonString, JsonObject.class);

            JSONDecoder oJSONDecoder = new JSONDecoder(strJsonString);
            oGotoStatement = (GotoStatement) oJSONDecoder.processGotoStatement(oJsonObject);

            assert oGotoStatement.structure().equals(strJsonString);
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing json string: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testProcessIfStatement() {
        JsonObject oJsonObject;
        IfThenStatement oIfStatement;
        String strJsonString = "{\"IF\": {\"TOKEN_NR\": \"92\",\"CONDITION\": {\"OPERATOR\": {\"LEFT_EXPRESSION\": {\"VARIABLE\": {\"NAME\": \"c#\"}},\"OPERATOR_OBJECT\": \"COMPARE_EQUAL\",\"RIGHT_EXPRESSION\": 3.0}},\"ELSE_STATEMENT\": \"0\",\"END_IF\": \"450\",\"TARGET_LINE\": \"0\"}}";
        Gson oGsonProcessor = new Gson();
        try {
            oJsonObject = oGsonProcessor.fromJson(strJsonString, JsonObject.class);

            JSONDecoder oJSONDecoder = new JSONDecoder(strJsonString);
            oIfStatement = (IfThenStatement) oJSONDecoder.processIfStatement(oJsonObject);

            assert oIfStatement.structure().equals(strJsonString);
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing json string: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testProcessEndStatement() {
        JsonObject oJsonObject;
        EndStatement oEndStatement;
        String strJsonString = "{\"END\": {\"TOKEN_NR\": \"110\"}}";
        Gson oGsonProcessor = new Gson();
        try {
            oJsonObject = oGsonProcessor.fromJson(strJsonString, JsonObject.class);

            JSONDecoder oJSONDecoder = new JSONDecoder(strJsonString);
            oEndStatement = (EndStatement) oJSONDecoder.processEndStatement(oJsonObject);

            assert oEndStatement.structure().equals(strJsonString);
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing json string: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testProcessExpressionPrimitiveNumber() {
        JsonElement oJsonElement;
        Expression oExpression;

        String strJsonString = "{\"EXPRESSION\": 5}";
        Gson oGsonProcessor = new Gson();
        try {
            oJsonElement = oGsonProcessor.fromJson(strJsonString, JsonElement.class);
            oExpression = ExpressionDecoder.expressionDeCoder((JsonObject) oJsonElement);

            assert oExpression.structure().equals("5.0");
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing json string: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
