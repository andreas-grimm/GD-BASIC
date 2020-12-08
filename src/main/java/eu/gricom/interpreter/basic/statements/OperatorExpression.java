package eu.gricom.interpreter.basic.statements;


import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.helper.Logger;

/**
 * An operator expression evaluates two expressions and then performs some
 * arithmetic operation on the results.
 */

public class OperatorExpression implements Expression {
    private Logger _oLogger = new Logger(this.getClass().getName());
    private final Expression _oLeft;
    private final char _strOperator;
    private final Expression _oRight;

    public OperatorExpression(Expression oLeft, char strOperator, Expression oRight) {
        _oLogger.debug("--->  " + oLeft.content() + " " + strOperator + " " + oRight.content());
        _oLeft = oLeft;
        _strOperator = strOperator;
        _oRight = oRight;
    }

    public Value evaluate() throws Exception {
        Value leftVal = _oLeft.evaluate();
        Value rightVal = _oRight.evaluate();

        switch (_strOperator) {
            case '=':
                // Coerce to the left argument's type, then compare.
                if (leftVal instanceof NumberValue) {
                    return new NumberValue((leftVal.toNumber() ==
                            rightVal.toNumber()) ? 1 : 0);
                }
                return new NumberValue(leftVal.toString().equals(rightVal.toString()) ? 1 : 0);

            case '+':
                // Addition if the left argument is a number, otherwise do
                // string concatenation.
                if (leftVal instanceof NumberValue) {
                    return new NumberValue(leftVal.toNumber() +
                            rightVal.toNumber());
                }
                return new StringValue(leftVal.toString() + rightVal.toString());

            case '-':
                return new NumberValue(leftVal.toNumber() -
                        rightVal.toNumber());
            case '*':
                return new NumberValue(leftVal.toNumber() *
                        rightVal.toNumber());
            case '/':
                return new NumberValue(leftVal.toNumber() /
                        rightVal.toNumber());
            case '<':
                // Coerce to the left argument's type, then compare.
                if (leftVal instanceof NumberValue) {
                    return new NumberValue((leftVal.toNumber() <
                            rightVal.toNumber()) ? 1 : 0);
                }
                return new NumberValue((leftVal.toString().compareTo(rightVal.toString()) < 0) ? 1 : 0);

            case '>':
                // Coerce to the left argument's type, then compare.
                if (leftVal instanceof NumberValue) {
                    return new NumberValue((leftVal.toNumber() >
                            rightVal.toNumber()) ? 1 : 0);
                }
                return new NumberValue((leftVal.toString().compareTo(rightVal.toString()) > 0) ? 1 : 0);

        }
        throw new SyntaxErrorException("Unknown operator: " + _strOperator);
    }

    public final Expression getLeft() {return (_oLeft);}
    public String getOperator() {return (String.valueOf(_strOperator));}
    public final Expression getRight() {return (_oRight);}

    public String content () {
        return (_oLeft.content() + " " + _strOperator + " " + _oRight.content());

    }
}
