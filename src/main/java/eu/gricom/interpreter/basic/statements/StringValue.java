package eu.gricom.interpreter.basic.statements;

/**
 * A string value.
 */
public class StringValue implements Value {
    private final String _strValue;

    public StringValue(String strValue) {
        _strValue = strValue;
    }

    @Override public String toString() {
        return (_strValue);
    }

    public double toNumber() {
        return (Double.parseDouble(_strValue));
    }

    public Value evaluate() {
        return (this);
    }

    public String content() {return (_strValue);}
}
