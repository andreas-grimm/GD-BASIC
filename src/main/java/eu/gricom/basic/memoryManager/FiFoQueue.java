package eu.gricom.basic.memoryManager;

import eu.gricom.basic.variableTypes.Value;
import eu.gricom.basic.error.EmptyStackException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * FiFoQueue.java
 * <p>
 * Description:
 * <p>
 * This class is implements the local used fifo queue. The fifo queue is used for the DATA command and later functions.  * <p>
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class FiFoQueue {
    private static Queue<Value> _oFiFo = null;

    /**
     * Initializes the FiFo Queue. As the queue is static, it can be generated in every statement and gets access to
     * the data
     */
    public FiFoQueue() {
        if (_oFiFo == null) {
            _oFiFo  = new LinkedList<>();
        }
    }

    @SuppressWarnings("unchecked")
    /**
     * Adds a value to the queue.
     *
     * @param  oValue Data to be added to the queue.
     */
    public final void push(final Value oValue) {
        _oFiFo.add(oValue);
    }


    @SuppressWarnings("unchecked")
    /**
     * pulls and removes the current head from the queue.
     *
     * @return Value retrieved from the queue
     * @throws EmptyStackException the queue is empty while a pop method is called
     */
    public final Value pop() throws EmptyStackException {
        if (!_oFiFo.isEmpty()) {
            return (Value) _oFiFo.poll();
        }

        throw new EmptyStackException("FiFo Queue Empty");
    }


    /**
     * Clear the queue.
     */
    public final void reset() {
        _oFiFo.clear();
    }
}
