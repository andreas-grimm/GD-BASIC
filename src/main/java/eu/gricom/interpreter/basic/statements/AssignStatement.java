package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.helper.MemoryManagement;

/**
 * AssignStatement.java
 * <p>
 * Description:
 * <p>
 * An assignment statement evaluates an expression and stores the result in a variable.
 * <p>
 * (c) = 2004,..,2016 by Andreas Grimm, Den Haag, The Netherlands
 * <p>
 * Created in 2003
 *
 */
public final class AssignStatement implements Statement {
    private final String _strName;
    private final Expression _oValue;
    private MemoryManagement _oMemoryManagement = new MemoryManagement();

    /**
     * Default constructor.
     *
     * @param strName - target of the assign statement
     * @param oValue - value of the assignment statement
     */
    public AssignStatement(final String strName, final Expression oValue) {
        _strName = strName;
        _oValue = oValue;
    }

    /**
     * The assignment is defined as part of the default constructor. But only here the transaction is actually
     * executed. After the execution, the variable is assigned.
     *
     * @throws Exception - any excpetion coming from the memory management
     */
    @Override
    public void execute() throws Exception {
        _oMemoryManagement.putMap(_strName, _oValue.evaluate().toNumber());
    }

    /**
     * This method is used in testing and debugging. It returns the set values when the constructor has been called.
     *
     * @return - readable string with the name and the value of the assignment
     */
    @Override
    public String content() {
        return ("ASSIGN [" + _strName + ":= " + _oValue.content() + "]");
    }
}
