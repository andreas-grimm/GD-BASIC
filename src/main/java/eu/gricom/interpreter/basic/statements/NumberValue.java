package eu.gricom.interpreter.basic.statements;

/**
 * A numeric value. Basic uses doubles internally for all numbers.
 */
public class NumberValue implements Value {
    private final double _iValue;

    public NumberValue(double iValue) {
        _iValue = iValue;
    }

    @Override public String toString() {
        return Double.toString(_iValue);
    }

    public double toNumber() {
        return (_iValue);
    }

    public Value evaluate() {
        return (this);
    }

    public String content() { return (String.valueOf(_iValue));}
}