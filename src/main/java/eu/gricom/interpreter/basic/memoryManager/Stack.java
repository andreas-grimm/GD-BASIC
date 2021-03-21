package eu.gricom.interpreter.basic.memoryManager;

import eu.gricom.interpreter.basic.error.EmptyStackException;
import eu.gricom.interpreter.basic.variableTypes.Value;

/**
 * Stack.java
 * <p>
 * Description:
 * <p>
 * This class is implements the local used stack. The stack will be required at a later version, when the GOSUB and
 * FOR/NEXT commands will be added.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
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
     * Reoves the top Value typed element from the Stack
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
