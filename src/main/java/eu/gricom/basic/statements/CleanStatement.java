package eu.gricom.basic.statements;

import eu.gricom.basic.error.EmptyStackException;
import eu.gricom.basic.memoryManager.Stack;

/**
 * CleanStatement.java
 * <p>
 * Description:
 * <p>
 * The DimStatement class defines all kind of arrays.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
public class CleanStatement implements Statement {
    private final int _iTokenNumber;

    /**
     * Default constructor.
     *
     * An "CLEAN" statement cleans the stack from the last entrence if you jump out of the block.
     * @param iTokenNumber - number of the command in the basic program
     */
    public CleanStatement(final int iTokenNumber) {
        _iTokenNumber = iTokenNumber;
    }

    /**
     * Get Token Number.
     *
     * @return the command line number of the statement
     */
    @Override
    public final int getTokenNumber() {
        return _iTokenNumber;
    }

    /**
     * Execute.
     *
     * Remove the last stack entry.
     */
    public final void execute() {
        Stack oStack = new Stack();

        try {
            oStack.pop();
        } catch (EmptyStackException e) {
            e.printStackTrace();
        }
    }

    /**
     * Content.
     *
     * Method for JUnit to return the content of the statement.
     *
     * @return - gives the name of the statement ("END")
     */
    @Override
    public final String content() {

        return "END";
    }

    /**
     * Structure.
     *
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("INPUT") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"CLEAN\": {\n";
        strReturn += "\t\"TOKEN_NR\": \""+ _iTokenNumber +"\"\n";
        strReturn += "\t}\n";
        strReturn += "}\n";
        return strReturn;
    }
}
