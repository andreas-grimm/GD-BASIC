package eu.gricom.basic.functions;

import eu.gricom.basic.tokenizer.Token;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.statements.Expression;
import eu.gricom.basic.variableTypes.Value;


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
    private final Expression _oFirstParam;
    private final Expression _oSecondParam;
    private final Expression _oThirdParam;

    /**
     * Contructor for functions without parameter.
     *
     * @param oToken token to be executed.
     */
    public Function(final Token oToken) {
        _oToken = oToken;
        _oFirstParam = null;
        _oSecondParam = null;
        _oThirdParam = null;
    }

    /**
     * Contructor for functions with one parameter.
     *
     * @param oToken      token to be executed
     * @param oExpression parameter of the function
     */
    public Function(final Token oToken,
                    final Expression oExpression) {
        _oToken = oToken;
        _oFirstParam = oExpression;
        _oSecondParam = null;
        _oThirdParam = null;
    }

    /**
     * Contructor for functions with one parameter.
     *
     * @param oToken      token to be executed
     * @param oFirstParam parameter of the function
     * @param oSecondParam parameter of the function
     */
    public Function(final Token oToken,
                    final Expression oFirstParam,
                    final Expression oSecondParam) {
        _oToken = oToken;
        _oFirstParam = oFirstParam;
        _oSecondParam = oSecondParam;
        _oThirdParam = null;
    }

    /**
     * Contructor for functions with three parameter.
     *
     * @param oToken      token to be executed
     * @param oFirstParam parameter of the function
     * @param oSecondParam parameter of the function
     * @param oThirdParam parameter of the function
     */
    public Function(final Token oToken,
                    final Expression oFirstParam,
                    final Expression oSecondParam,
                    final Expression oThirdParam) {
        _oToken = oToken;
        _oFirstParam = oFirstParam;
        _oSecondParam = oSecondParam;
        _oThirdParam = oThirdParam;
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
                return Abs.execute(_oFirstParam.evaluate());

            // ASC Token: Return the ASCII value of the parameter
            case ASC:
                return Asc.execute(_oFirstParam.evaluate());

            // ATN Token: Return the arch tangents of the parameter
            case ATN:
                return Atn.execute(_oFirstParam.evaluate());

            // CDBL Token: Convert to a DBL (Real)
            case CDBL:
                return Cdbl.execute(_oFirstParam.evaluate());

            // CHR Token: Return the character of the ASCII value parameter
            case CHR:
                return Chr.execute(_oFirstParam.evaluate());

            // CINT Token: Convert to a INT (Integer)
            case CINT:
                return Cint.execute(_oFirstParam.evaluate());

            // COS Token: Return the CoSinus of the value parameter
            case COS:
                return Cos.execute(_oFirstParam.evaluate());

            // EXP Token: Return the Exponent to the base e of the value parameter
            case EXP:
                return Exp.execute(_oFirstParam.evaluate());

            // INSTR Token: Return the Index of the location of the second parameter in the first parameter
            case INSTR:
                return Instr.execute(_oFirstParam.evaluate(), _oSecondParam.evaluate());

            // LEFT Token: Return the first number of characters from the input string. The first parameter needs
            // to be a type String, the second parameter has to be part Integer.
            case LEFT:
                return Left.execute(_oFirstParam.evaluate(), _oSecondParam.evaluate());

            // LEN Token: Returns the Length of the String in the first parameter
            case LEN:
                return Len.execute(_oFirstParam.evaluate());

            // LOG10 Token: Return the Logarithm Decimalis of the value parameter
            case LOG10:
                return Log10.execute(_oFirstParam.evaluate());

            // MEM Token: Return size of available memory
            case MEM:
                return Mem.execute();

            // MID Token: Returns a substring of an input string with start and end position
            case MID:
                return Mid.execute(_oFirstParam.evaluate(), _oSecondParam.evaluate(), _oThirdParam.evaluate());

            // RIGHT Token: Return the last number of characters from the input string. The first parameter needs
            // to be a type String, the second parameter has to be part Integer.
            case RIGHT:
                return Right.execute(_oFirstParam.evaluate(), _oSecondParam.evaluate());

            // RND Token: Return a pseudo random number between 0 and 1
            case RND:
                return Rnd.execute();

            // SIN Token: Return the Sinus of the value parameter
            case SIN:
                return Sin.execute(_oFirstParam.evaluate());

            // SQR Token: Return the Square Root of the value parameter
            case SQR:
                return Sqr.execute(_oFirstParam.evaluate());

            // STR Token: Return the String value to a numeric parameter
            case STR:
                return Str.execute(_oFirstParam.evaluate());

            // SYSTEM Token: Call operation system function
            case SYSTEM:
                return System.execute(_oFirstParam.evaluate(), _oSecondParam.evaluate());

            // TAN Token: Return the Tangents of the value parameter
            case TAN:
                return Tan.execute(_oFirstParam.evaluate());

            // VAL Token: Return the value of the parameter
            case VAL:
                return Val.execute(_oFirstParam.evaluate());

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
        String strReturn = "Token: " + _oToken.getType().toString() + " Content: <";

        if (_oFirstParam != null) {
            strReturn += _oFirstParam.toString();
        }

        if (_oSecondParam != null) {
            strReturn += ", " + _oSecondParam.toString();
        }

        if (_oThirdParam != null) {
            strReturn += ", " + _oThirdParam.toString();
        }

        strReturn += ">";

        return strReturn;
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
