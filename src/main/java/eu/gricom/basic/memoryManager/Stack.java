package eu.gricom.basic.memoryManager;

import eu.gricom.basic.variableTypes.Value;
import eu.gricom.basic.error.EmptyStackException;

/**
 * Stack.java
 * <p>
 * Description: The Stack class provides a LIFO (Last In, First Out) data structure used by the interpreter for
 * managing return addresses during GOSUB/RETURN operations and loop control in FOR-NEXT and WHILE-ENDWHILE constructs.
 * It wraps Java's built-in Stack class with singleton pattern access.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class Stack {
    private static java.util.Stack _oStack = null;


    /**
     * Initializes the stack if it does not exist.
     */
    public Stack() {
        if (_oStack == null) {
            _oStack = new java.util.Stack();
        }
    }

    @SuppressWarnings("unchecked")
    /**
     * Pushes a Value object into the Stack.
     *
     * @param  oValue Value object to be pushed.
     */
    public final void push(final Value oValue) {
        _oStack.push(oValue);
    }


    @SuppressWarnings("unchecked")
    /**
     * Returns the top Value typed element from the Stack
     *
     * @return Value retrieved from the stack
     * @throws EmptyStackException the stack is empty while a pop method is called
     */
    public final Value pop() throws EmptyStackException {
        if (!_oStack.isEmpty()) {
            return (Value) _oStack.pop();
        }

        throw new EmptyStackException("Stack Empty");
    }


    /**
     * Clear the Stack.
     */
    public final void reset() {
        _oStack.empty();
    }
}
