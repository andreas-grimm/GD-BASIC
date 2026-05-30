package eu.gricom.basic.statements;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.memoryManager.FileManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * FCopyStatement.java
 * <p>
 * Description: The FCopyStatement class implements the BASIC FCOPY command. It copies the content
 * from a source file to a destination file. Both files are identified by their file IDs, which must
 * be registered in the FileManager. The source file is read and its content is written to the
 * destination file. If either file cannot be opened or the copy operation fails, a RuntimeException
 * is thrown.
 * <p>
 * Usage:
 * - FCOPY sourceFileID, destinationFileID
 * <p>
 * Error Handling:
 * - Throws RuntimeException if source file ID is not registered in FileManager
 * - Throws RuntimeException if destination file ID is not registered in FileManager
 * - Throws RuntimeException if source file cannot be opened or read
 * - Throws RuntimeException if destination file cannot be opened or written
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FCopyStatement implements Statement {
    private final int _iTokenNumber;
    private final int _iSourceFileId;
    private final int _iDestinationFileId;

    /**
     * Default constructor.
     * <p>
     * @param iTokenNumber the line number of the command in the BASIC program
     * @param iSourceFileId the file ID of the source file to copy from
     * @param iDestinationFileId the file ID of the destination file to copy to
     */
    public FCopyStatement(int iTokenNumber, int iSourceFileId, int iDestinationFileId) {
        _iTokenNumber = iTokenNumber;
        _iSourceFileId = iSourceFileId;
        _iDestinationFileId = iDestinationFileId;
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
     * execute copies the content from the source file to the destination file. Both files are
     * identified by their file IDs in the FileManager. The source file is read line by line
     * and written to the destination file. If any error occurs during the copy operation,
     * a RuntimeException is thrown.
     *
     * @throws Exception as any execution error found during execution
     */
    @Override
    public void execute() throws Exception {
        FileManager oFileManager = new FileManager();

        // Step 1: Verify source file is registered
        if (!oFileManager.getFileStatus(_iSourceFileId)) {
            throw new RuntimeException("Source file with ID " + _iSourceFileId + " is not registered in FileManager");
        }

        // Step 2: Verify destination file is registered
        if (!oFileManager.getFileStatus(_iDestinationFileId)) {
            throw new RuntimeException("Destination file with ID " + _iDestinationFileId + " is not registered in FileManager");
        }

        // Step 3: Get the file names from FileManager
        String strSourceFileName = oFileManager.getFileName(_iSourceFileId).toString();
        String strDestinationFileName = oFileManager.getFileName(_iDestinationFileId).toString();

        // Step 4: Validate file names are not empty
        if (strSourceFileName.isEmpty()) {
            throw new RuntimeException("Source file name is empty for file ID " + _iSourceFileId);
        }

        if (strDestinationFileName.isEmpty()) {
            throw new RuntimeException("Destination file name is empty for file ID " + _iDestinationFileId);
        }

        // Step 5: Perform the copy operation
        try {
            Path oSourcePath = Paths.get(strSourceFileName);
            Path oDestinationPath = Paths.get(strDestinationFileName);

            // Step 6: Read from source and write to destination
            BufferedReader oSourceReader = null;
            BufferedWriter oDestinationWriter = null;

            try {
                oSourceReader = Files.newBufferedReader(oSourcePath, StandardCharsets.UTF_8);
                oDestinationWriter = Files.newBufferedWriter(oDestinationPath, StandardCharsets.UTF_8);

                String strLine;
                while ((strLine = oSourceReader.readLine()) != null) {
                    oDestinationWriter.write(strLine);
                    oDestinationWriter.newLine();
                }

                oDestinationWriter.flush();
            } finally {
                // Step 7: Close resources
                if (oSourceReader != null) {
                    try {
                        oSourceReader.close();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to close source file: " + e.getMessage());
                    }
                }

                if (oDestinationWriter != null) {
                    try {
                        oDestinationWriter.close();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to close destination file: " + e.getMessage());
                    }
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (IOException e) {
            throw new RuntimeException("File copy operation failed: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during file copy: " + e.getMessage());
        }
    }

    /**
     * Content.
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return gives the name of the statement ("FCOPY") and the file IDs
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String content() throws Exception {
        return "FCOPY";
    }

    /**
     * Structure.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("FCOPY") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = "{\"FCOPY\": {";
        strReturn += "\"TOKEN_NR\": \"" + _iTokenNumber + "\", ";
        strReturn += "\"SOURCE_FILE_ID\": \"" + _iSourceFileId + "\", ";
        strReturn += "\"DESTINATION_FILE_ID\": \"" + _iDestinationFileId + "\"";
        strReturn += "}}";
        return strReturn;
    }
}
