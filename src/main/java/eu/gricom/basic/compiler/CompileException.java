package eu.gricom.basic.compiler;

/**
 * CompileException is raised when a BASIC program cannot be turned into Java source code.
 *
 * <p>The message tells the user what is not supported yet, for example
 * "Statement not supported by the compiler yet (BASIC line 20): Input".</p>
 */
public class CompileException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception with a message for the user.
     *
     * @param strMessage the description of the problem
     */
    public CompileException(final String strMessage) {
        super(strMessage);
    }
}
