package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.memoryManager.FileOpenType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * FOpenStatement.java
 * <p>
 * Description: The FOpenStatement class implements the BASIC FOpen command. Based on the parameters used, it opens an
 * internal file for reading or writing. The management of the file is done by the FileManager class.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FOpenStatement implements Statement {
    private FileOpenType _eReadWrite = FileOpenType.READ;
    private final int _iTokenNumber;
    private final int _iFileId;
    private final String _strFileName;

    /**
     * Default Constructor.
     *
     */
    public FOpenStatement(int iTokenNumber, int iFileId, String strFileName, String strMode) {
        if (strMode.equalsIgnoreCase("write")) {
            _eReadWrite = FileOpenType.WRITE;
        }

        _iTokenNumber = iTokenNumber;
        _iFileId = iFileId;
        _strFileName = strFileName;
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
     * Statements implement this to actually perform whatever behavior the
     * statement causes. "print" statements will display text here, "goto"
     * statements will change the current statement, etc.
     *
     * @throws Exception as any execution error found during execution
     */
    @Override
    public void execute() throws Exception {
        if (_eReadWrite == FileOpenType.WRITE) {
            Path oPath = Paths.get(_strFileName);
            if (!Files.exists(oPath)) {
                try {
                    Files.createFile(oPath);
                } catch (java.nio.file.FileAlreadyExistsException e) {
                    // File was created by another thread, this is acceptable
                }
            }
        }

        FileManager oFileManager = new FileManager();
        oFileManager.openFile(_strFileName, _iFileId, _eReadWrite);
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
        return "FOPEN";
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
        String strReturn = "{\"FOPEN\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\"";
        strReturn += "\"FILE_ID\": \""+ _iFileId +"\"";
        strReturn += "\"FILE_NAME\": \""+ _strFileName +"\"";
        if (_eReadWrite == FileOpenType.WRITE) {
            strReturn += "\"READ_WRITE\": \"WRITE\"";
        } else {
            strReturn += "\"READ_WRITE\": \"READ\"";
        }
        strReturn += "}}";
        return strReturn;
    }
}
