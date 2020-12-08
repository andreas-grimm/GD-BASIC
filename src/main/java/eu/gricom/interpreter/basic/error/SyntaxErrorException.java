package eu.gricom.interpreter.basic.error;

/**
 * Base error interface... This interface is used to all interpreter errors for the
 * Basic language...
 */

public class SyntaxErrorException extends Exception {
    public SyntaxErrorException (String strErrorMessage) {
        super(strErrorMessage);
    }
}
