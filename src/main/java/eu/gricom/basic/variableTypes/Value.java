package eu.gricom.basic.variableTypes;

import eu.gricom.basic.error.DivideByZeroException;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.statements.Expression;

/**
 * Value.java
 * <p>
 * Description:
 * This is the base interface for a value. Values are the data that the
 * interpreter processes. They are what gets stored in variables, printed,
 * and operated on.
 *
 * There is an implementation of this interface for each of the different
 * primitive types (really just double and string) that Jasic supports.
 * Wrapping them in a single Value interface lets Jasic be dynamically-typed
 * and convert between different representations as needed.
 *
 * Note that Value extends Expression. This is a bit of a hack, but it lets
 * us use values (which are typically only ever seen by the interpreter and
 * not the parser) as both runtime values, and as object representing
 * literals in code.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public interface Value extends Expression {

    /**
     * Value types override this to convert themselves to a string representation.
     *
     * @return the value as a string
     */
    String toString();

    /**
     * Value types override this to convert themselves to a numeric representation.
     *
     * @return the value as a double
     */
    double toReal();

    /**
     * Compares of one value object with another one. Returns true if equal.
     *
     * @param oValue second value for the comparison
     * @return BooleanValue(true) if the objects are equal (not identical), BooleanValue(false) otherwise
     * @throws SyntaxErrorException thrown when different types are used
     */
    Value equals(Value oValue) throws SyntaxErrorException;

    /**
     * Compares of one value object with another one. Returns true if not equal.
     *
     * @param oValue second value for the comparison
     * @return BooleanValue(true) if the objects are equal (not identical), BooleanValue(false) otherwise
     * @throws SyntaxErrorException thrown when different types are used
     */
    Value notEqual(Value oValue) throws SyntaxErrorException;

    /**
     * Addition of one value object with another one.
     *
     * @param oValue second value for the calculation
     * @return the result of the addition
     * @throws SyntaxErrorException thrown when different types are used
     */
    Value plus(Value oValue) throws SyntaxErrorException;

    /**
     * Substraction of one value object with another one.
     *
     * @param oValue subtractor for the calculation
     * @return the result of the substraction
     * @throws SyntaxErrorException thrown when different types are used
     */
    Value minus(Value oValue) throws SyntaxErrorException;

    /**
     * Multiplication of one value object with another one.
     *
     * @param oValue second factor for the calculation
     * @return the result of the multiplication
     * @throws SyntaxErrorException thrown when different types are used
     */
    Value multiply(Value oValue) throws SyntaxErrorException;

    /**
     * Division of one value object with another one.
     *
     * @param oValue divisor for the calculation
     * @return the result of the division
     * @throws DivideByZeroException thrown for a division by zero
     * @throws SyntaxErrorException thrown when different types are used
     */
    Value divide(Value oValue) throws DivideByZeroException, SyntaxErrorException;

    /**
     * Remainder of an integer division.
     *
     * @param oValue divisor for the calculation
     * @return the result of the division
     * @throws DivideByZeroException thrown for a division by zero
     * @throws SyntaxErrorException thrown when different types are used
     */
    Value modulo(Value oValue) throws DivideByZeroException, SyntaxErrorException;

    /**
     * Multiplication of one value object by 2, parameter times.
     *
     * @param oValue second factor for the calculation
     * @return the result of the multiplication
     * @throws SyntaxErrorException thrown when different types are used
     */
    Value shiftLeft(Value oValue) throws SyntaxErrorException;

    /**
     * Execution of the logical AND function.
     *
     * @param oValue second statement to compare
     * @return the result of the function
     * @throws SyntaxErrorException thrown when different or non-compatible types are used
     */
    Value and(Value oValue) throws DivideByZeroException, SyntaxErrorException;

    /**
     * Execution of the logical OR function.
     *
     * @param oValue second statement to compare
     * @return the result of the function
     * @throws SyntaxErrorException thrown when different or non-compatible types are used
     */
    Value or(Value oValue) throws DivideByZeroException, SyntaxErrorException;

    /**
     * Division of one value object by 2, parameter times.
     *
     * @param oValue divisor for the calculation
     * @return the result of the division
     * @throws DivideByZeroException thrown for a division by zero
     * @throws SyntaxErrorException thrown when different types are used
     */
    Value shiftRight(Value oValue) throws DivideByZeroException, SyntaxErrorException;

    /**
     * Calcualte the power of one value object with another one.
     *
     * @param oValue divisor for the calculation
     * @return the result of the divition
     * @throws SyntaxErrorException thrown when different types are used
     */
    Value power(Value oValue) throws SyntaxErrorException;

    /**
     * smallerThan compares one value object with another one.
     *
     * @param oValue comparison value for function
     * @return BooleanValue(true) if this object is smaller (not equal), BooleanValue(false) otherwise
     * @throws SyntaxErrorException thrown when different types are used
     */
    Value smallerThan(Value oValue) throws SyntaxErrorException;

    /**
     * smallerEqualThan compares one value object with another one.
     *
     * @param oValue comparison value for function
     * @return BooleanValue(true) if this object is smaller (not equal), BooleanValue(false) otherwise
     * @throws SyntaxErrorException thrown when different types are used
     */
    Value smallerEqualThan(Value oValue) throws SyntaxErrorException;

    /**
     * largerThan of one value object with another one.
     *
     * @param oValue comparison value for function
     * @return BooleanValue(true) if this object is larger (not equal), BooleanValue(false) otherwise
     * @throws SyntaxErrorException thrown when different types are used
     */
    Value largerThan(Value oValue) throws SyntaxErrorException;

    /**
     * largerEqualThan of one value object with another one.
     *
     * @param oValue comparison value for function
     * @return BooleanValue(true) if this object is larger (not equal), BooleanValue(false) otherwise
     * @throws SyntaxErrorException thrown when different types are used
     */
    Value largerEqualThan(Value oValue) throws SyntaxErrorException;
}
