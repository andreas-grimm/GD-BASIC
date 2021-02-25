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
 */
public class Function implements Expression {
    private final Token _oToken;
    private final Expression _oExpression;

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
     * @param oToken      token to be executed
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
            // ABS Token: Return the absolute value of the parameter
            case ABS:
                return Abs.execute(_oExpression.evaluate());

            // ASC Token: Return the ASCII value of the parameter
            case ASC:
                return Asc.execute(_oExpression.evaluate());

            // ATN Token: Return the arch tangents of the parameter
            case ATN:
                return Atn.execute(_oExpression.evaluate());

            // CDBL Token: Convert to a DBL (Real)
            case CDBL:
                return Cdbl.execute(_oExpression.evaluate());

            // CHR Token: Return the character of the ASCII value parameter
            case CHR:
                return Chr.execute(_oExpression.evaluate());

            // CINT Token: Convert to a INT (Integer)
            case CINT:
                return Cint.execute(_oExpression.evaluate());

            // COS Token: Return the CoSinus of the value parameter
            case COS:
                return Cos.execute(_oExpression.evaluate());

            // EXP Token: Return the Exponent to the base e of the value parameter
            case EXP:
                return Exp.execute(_oExpression.evaluate());

            // LOG Token: Return the Logarithm Naturalis of the value parameter
            case LOG:
                return Log.execute(_oExpression.evaluate());

            // LOG10 Token: Return the Logarithm Decimalis of the value parameter
            case LOG10:
                return Log10.execute(_oExpression.evaluate());

            // MEM Token: Return size of available memory
            case MEM:
                return Mem.execute();

            // RND Token: Return a pseudo random number between 0 and 1
            case RND:
                return Rnd.execute();

            // SIN Token: Return the Sinus of the value parameter
            case SIN:
                return Sin.execute(_oExpression.evaluate());

            // SQR Token: Return the Square Root of the value parameter
            case SQR:
                return Sqr.execute(_oExpression.evaluate());

            // TAN Token: Return the Tangents of the value parameter
            case TAN:
                return Tan.execute(_oExpression.evaluate());

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
