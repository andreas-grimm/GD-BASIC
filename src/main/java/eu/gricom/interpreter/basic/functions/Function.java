package eu.gricom.interpreter.basic.functions;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.statements.Expression;
import eu.gricom.interpreter.basic.tokenizer.Token;
import eu.gricom.interpreter.basic.variableTypes.Value;


/**
 * Function.java
 *
 * Description:
 *
 * Function dispatcher: Execute the selected function and return the result.
 *
 * (c) = 2004,..,2016 by Andreas Grimm, Den Haag, The Netherlands
 *
 * Created in 2020
 *
 */
public class Function implements Expression {
    private Token _oToken;
    private Expression _oExpression;

    /**
     * Contructor for functions without parameter.
     *
     * @param oToken token to be executed.
     */
    public Function(final Token oToken) {
        _oToken = oToken;
        _oExpression = null;
    }

    /**
     * Contructor for functions with one parameter.
     *
     * @param oToken token to be executed
     * @param oExpression parameter of the function
     */
    public Function(final Token oToken, final Expression oExpression) {
        _oToken = oToken;
        _oExpression = oExpression;
    }

    /**
     * This method is used to trigger the function selected.
     *
     * @return - value containing the result of the function
     * @throws Exception - when the processing is running into a problem
     */
    public final Value evaluate() throws Exception {

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

            default:
                throw new RuntimeException("Unknown Function Called: " + _oToken.getText());
        }
    }

    /**
     * This method is used in testing and debugging. It returns the set values when the constructor has been called.
     *
     * @return - readable string with the name and the value of the assignment
     */
    public final String content() {

        return "Token: " + _oToken.getType().toString() + " Content: <" + _oExpression + ">";
    }
}
