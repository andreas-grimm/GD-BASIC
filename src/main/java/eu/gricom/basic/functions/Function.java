package eu.gricom.basic.functions;

import eu.gricom.basic.tokenizer.Token;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.statements.Expression;
import eu.gricom.basic.variabletypes.IntegerValue;
import eu.gricom.basic.variabletypes.RealValue;
import eu.gricom.basic.variabletypes.Value;


/**
 * Function.java
 * <p>
 * Description: The Function class serves as a dispatcher for all built-in BASIC functions. It accepts a token
 * identifying the function and up to three parameters, then routes the call to the appropriate function implementation
 * and returns the computed result.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class Function implements Expression {
    private final Token _oToken;
    private final Expression _oFirstParam;
    private final Expression _oSecondParam;
    private final Expression _oThirdParam;

    /**
     * Constructor for functions without parameter.
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
     * Constructor for functions with one parameter.
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
     * Constructor for functions with one parameter.
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
     * Constructor for functions with three parameters.
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

        return switch (_oToken.getType()) {
            // ABS Token: Return the absolute value of the parameter
            case ABS -> {
                assert _oFirstParam != null;
                yield Abs.execute(_oFirstParam.evaluate());
            }

            // ASC Token: Return the ASCII value of the parameter
            case ASC -> {
                assert _oFirstParam != null;
                yield Asc.execute(_oFirstParam.evaluate());
            }

            // ATN Token: Return the arch tangents of the parameter
            case ATN -> {
                assert _oFirstParam != null;
                yield Atn.execute(_oFirstParam.evaluate());
            }

            // CDBL Token: Convert to a DBL (Real)
            case CDBL -> {
                assert _oFirstParam != null;
                yield Cdbl.execute(_oFirstParam.evaluate());
            }

            // CHR Token: Return the character of the ASCII value parameter
            case CHR -> {
                assert _oFirstParam != null;
                yield Chr.execute(_oFirstParam.evaluate());
            }

            // CINT Token: Convert to a INT (Integer)
            case CINT -> {
                assert _oFirstParam != null;
                yield Cint.execute(_oFirstParam.evaluate());
            }

            // COS Token: Return the CoSinus of the value parameter
            case COS -> {
                assert _oFirstParam != null;
                yield Cos.execute(_oFirstParam.evaluate());
            }

            // EOF Token: Return End-of-File indicator for the requested file ID
            case EOF -> {
                assert _oFirstParam != null;
                yield Eof.execute(_oFirstParam.evaluate());
            }

            // EXP Token: Return the Exponent to the base e of the value parameter
            case EXP -> {
                assert _oFirstParam != null;
                yield Exp.execute(_oFirstParam.evaluate());
            }

            // INSTR Token: Return the Index of the location of the second parameter in the first parameter
            case INSTR -> {
                assert _oFirstParam != null;
                assert _oSecondParam != null;
                yield Instr.execute(_oFirstParam.evaluate(), _oSecondParam.evaluate());
            }

            // LEFT Token: Return the first number of characters from the input string. The first parameter needs
            // to be a type String, the second parameter has to be part Integer.
            case LEFT -> {
                assert _oFirstParam != null;
                assert _oSecondParam != null;
                yield Left.execute(_oFirstParam.evaluate(), _oSecondParam.evaluate());
            }

            // LEN Token: Returns the Length of the String in the first parameter
            case LEN -> {
                assert _oFirstParam != null;
                yield Len.execute(_oFirstParam.evaluate());
            }

            // LOG Token: Return the Logarithm Naturalis of the value parameter
            case LOG -> {
                assert _oFirstParam != null;
                yield Log.execute(_oFirstParam.evaluate());
            }

            // LOG10 Token: Return the Logarithm Decimalis of the value parameter
            case LOG10 -> {
                assert _oFirstParam != null;
                yield Log10.execute(_oFirstParam.evaluate());
            }

            // LOWER Token: Convert string to lowercase
            case LOWER -> {
                assert _oFirstParam != null;
                yield Lower.execute(_oFirstParam.evaluate());
            }

            // MEM Token: Return size of available memory
            case MEM -> Mem.execute();

            // MID Token: Returns a substring of an input string with start and end position
            case MID -> {
                assert _oFirstParam != null;
                assert _oSecondParam != null;
                assert _oThirdParam != null;
                yield Mid.execute(_oFirstParam.evaluate(), _oSecondParam.evaluate(), _oThirdParam.evaluate());
            }

            // RIGHT Token: Return the last number of characters from the input string. The first parameter needs
            // to be a type String, the second parameter has to be part Integer.
            case RIGHT -> {
                assert _oFirstParam != null;
                assert _oSecondParam != null;
                yield Right.execute(_oFirstParam.evaluate(), _oSecondParam.evaluate());
            }

            // RND Token: Return a pseudo random number between 0 and 1
            case RND -> Rnd.execute();

            // SIN Token: Return the Sinus of the value parameter
            case SIN -> {
                assert _oFirstParam != null;
                yield Sin.execute(_oFirstParam.evaluate());
            }

            // SQR Token: Return the Square Root of the value parameter
            case SQR -> {
                assert _oFirstParam != null;
                yield Sqr.execute(_oFirstParam.evaluate());
            }

            // STR Token: Return the String value to a numeric parameter
            case STR -> {
                assert _oFirstParam != null;
                yield Str.execute(_oFirstParam.evaluate());
            }

            // SYSTEM Token: Call operating system function
            case SYSTEM -> {
                assert _oFirstParam != null;
                assert _oSecondParam != null;
                yield System.execute(_oFirstParam.evaluate(), _oSecondParam.evaluate());
            }

            // TAN Token: Return the Tangents of the value parameter
            case TAN -> {
                assert _oFirstParam != null;
                yield Tan.execute(_oFirstParam.evaluate());
            }

            // VAL Token: Return the value of the parameter
            case VAL -> {
                assert _oFirstParam != null;
                yield Val.execute(_oFirstParam.evaluate());
            }

            // UPPER Token: Convert string to uppercase
            case UPPER -> {
                assert _oFirstParam != null;
                yield Upper.execute(_oFirstParam.evaluate());
            }

            // CALL Token: send the payload to the URL (two parameters)
            case CALL -> {
                assert _oFirstParam != null;
                assert _oSecondParam != null;
                yield Call.execute(_oFirstParam.evaluate(), _oSecondParam.evaluate());
            }

            // DIREXISTS Token: true when the directory name exists
            case DIREXISTS -> {
                assert _oFirstParam != null;
                yield DirExists.execute(_oFirstParam.evaluate());
            }

            // FEXISTS Token: true when the file name exists
            case FEXISTS -> {
                assert _oFirstParam != null;
                yield FExists.execute(_oFirstParam.evaluate());
            }

            // FCOMPARE Token: compare the content of two open files (two file IDs)
            case FCOMPARE -> {
                assert _oFirstParam != null;
                assert _oSecondParam != null;
                yield FCompare.execute(fileId(_oFirstParam.evaluate()), fileId(_oSecondParam.evaluate()));
            }

            // FGETNAME Token: name of an open file (file ID)
            case FGETNAME -> {
                assert _oFirstParam != null;
                yield FGetFileName.execute(fileId(_oFirstParam.evaluate()));
            }

            // FGETSIZE Token: size of an open file (file ID)
            case FGETSIZE -> {
                assert _oFirstParam != null;
                yield FGetSize.execute(fileId(_oFirstParam.evaluate()));
            }

            // FISOPEN Token: true when the file ID is open
            case FISOPEN -> {
                assert _oFirstParam != null;
                yield FIsOpen.execute(fileId(_oFirstParam.evaluate()));
            }

            // FLINECOUNT Token: number of lines in an open file (file ID)
            case FLINECOUNT -> {
                assert _oFirstParam != null;
                yield FLineCount.execute(fileId(_oFirstParam.evaluate()));
            }

            // FMODTIME Token: last modification time of an open file (file ID)
            case FMODTIME -> {
                assert _oFirstParam != null;
                yield FModTime.execute(fileId(_oFirstParam.evaluate()));
            }

            // GETCWD Token: the current working directory
            case GETCWD -> GetCwd.execute();

            // TIME Token: the current time
            case TIME -> Time.execute();

            default -> throw new RuntimeException("Unknown Function Called: " + _oToken.getText());
        };
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
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("INPUT") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"FUNCTION\": {";
        strReturn += "\"TOKEN_TYPE\": \"" + _oToken.getType() + "\",";
        if (_oFirstParam != null) {
            strReturn += "\"FIRST_PARAM\": \"" + _oFirstParam.structure() + "\",";
        }
        if (_oSecondParam != null) {
            strReturn += "\"SECOND_PARAM\": \"" + _oSecondParam.structure() + "\",";
        }
        if (_oThirdParam != null) {
            strReturn += "\"THIRD_PARAM\": \"" + _oThirdParam.structure() + "\"";
        }
        strReturn += "}}";
        return strReturn;
    }

    /**
     * fileId converts a file ID value into a Java int. A file ID must be an INTEGER.
     *
     * @param oValue the evaluated parameter
     * @return the file ID as int
     * @throws RuntimeException when the value is not an INTEGER
     */
    /**
     * Converts a value to a file ID. Used by the interpreter and by the compiler, so both check the same way.
     *
     * @param oValue the value that holds the file ID
     * @return the file ID
     * @throws RuntimeException if the value is not an integer
     */
    public static int fileId(final Value oValue) throws RuntimeException {
        if (oValue instanceof IntegerValue) {
            return ((IntegerValue) oValue).toInt();
        }
        // a number literal such as 2 is read as a real value (2.0): accept it when it is a whole number
        if (oValue instanceof RealValue) {
            double fValue = oValue.toReal();
            if (fValue == Math.rint(fValue) && fValue >= Integer.MIN_VALUE && fValue <= Integer.MAX_VALUE) {
                return (int) fValue;
            }
        }
        throw new RuntimeException("File ID is not an integer: " + oValue);
    }

    /** @return the function token type, for example SQR (used by the compiler) */
    public eu.gricom.basic.tokenizer.BasicTokenType getTokenType() {
        return _oToken.getType();
    }

    /** @return the first parameter or null (used by the compiler) */
    public Expression getFirstParam() {
        return _oFirstParam;
    }

    /** @return the second parameter or null (used by the compiler) */
    public Expression getSecondParam() {
        return _oSecondParam;
    }

    /** @return the third parameter or null (used by the compiler) */
    public Expression getThirdParam() {
        return _oThirdParam;
    }
}

