package eu.gricom.interpreter.basic.functions;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.statements.Expression;
import eu.gricom.interpreter.basic.tokenizer.Token;
import eu.gricom.interpreter.basic.variableTypes.Value;

public class Function implements Expression {
    private Token _oToken;
    private Expression _oExpression;

    public Function(Token oToken) {
        _oToken = oToken;
        _oExpression = null;
    }

    public Function(Token oToken, Expression oExpression) {
        _oToken = oToken;
        _oExpression = oExpression;
    }

    public Value evaluate() throws Exception {

        switch (_oToken.getType()) {
            // MEM Token: Return size of available memory
            case ABS:
                return Abs.execute(_oExpression.evaluate());

            // MEM Token: Return size of available memory
            case ATN:
                return Atn.execute(_oExpression.evaluate());

            // MEM Token: Return size of available memory
            case CDBL:
                return Cdbl.execute(_oExpression.evaluate());

            // MEM Token: Return size of available memory
            case CINT:
                return Cint.execute(_oExpression.evaluate());

            // MEM Token: Return size of available memory
            case COS:
                return Cos.execute(_oExpression.evaluate());

            // MEM Token: Return size of available memory
            case MEM:
                return Mem.execute();

            // MEM Token: Return size of available memory
            case RND:
                return Rnd.execute();
        }

        throw new RuntimeException("Unknown Function Called: " + _oToken.getText());
    }

    @Override
    public String content() {
        return null;
    }
}