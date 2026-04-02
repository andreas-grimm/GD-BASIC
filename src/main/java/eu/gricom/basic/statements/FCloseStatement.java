package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.FileManager;

/**
 * FCloseStatement.java
 * <p>
 * Description: The FCloseStatement class implements the BASIC FClose command. Based on the parameters used, it closes
 * a previously opened internal file. Based on the parameter given in the command, the file will persist or be deleted.
 * The management of the file is done by the FileManager class.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FCloseStatement implements Statement {
    private final int _iTokenNumber;
    private final int _iFileId;
    private final boolean _bDelete;

    /**
     * Default constructor.
     * <p>
     * A "DIM" statement initializes an array of any type.
     * @param iTokenNumber - number of the command in the basic program
     */
    public FCloseStatement(int iTokenNumber, int iFileId,boolean bDelete) {
        _iTokenNumber = iTokenNumber;
        _iFileId = iFileId;
        _bDelete = bDelete;
    }


    /**
     * Get Token Number - get the number of the corresponding token to this statement.
     *
     * @return the command line number of the statement
     */
    @Override
    public int getTokenNumber() {
        return _iTokenNumber;
    }

    /**
     * closeFile closes the physical file for the FREAD or FWRITE statement. Based on the setting in the constructor,
     * the file is either deleted or kept.
     *
     * @throws Exception as any execution error found during execution
     */
    @Override
    public void execute() throws Exception {
        FileManager oFileManager = new FileManager();
        oFileManager.closeFile(_iFileId,_bDelete);
    }

    /**
     * Content.
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return gives the name of the statement ("INPUT") and the variable name
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String content() throws Exception {
        return "FCLOSE";
    }

    /**
     * Structure.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("INPUT") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"FCLOSE\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\"";
        strReturn += "\"FILE_ID\": \""+ _iFileId +"\"";
        if (_bDelete) {
            strReturn += "\"FILE_DELETE\": \"TRUE\"";
        } else {
            strReturn += "\"FILE_DELETE\": \"FALSE\"";
        }
        strReturn += "}}";
        return strReturn;
    }
}
