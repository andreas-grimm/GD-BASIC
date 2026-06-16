package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.memoryManager.FileManager;
import eu.gricom.basic.variableTypes.BooleanValue;
import eu.gricom.basic.variableTypes.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * FCompare.java
 * <p>
 * Description: The FCompare class implements the BASIC function to compare the content of two files.
 * It reads both files line by line and compares each line. If all lines in both files are identical,
 * the function returns a BooleanValue set to true. If there are any differences, or if the files
 * have different numbers of lines, the function returns a BooleanValue set to false.
 * <p>
 * Both file IDs must be registered in the FileManager. The files are opened temporarily for reading
 * and are not registered as open files in the FileManager after the comparison completes.
 * <p>
 * Usage Pattern:
 * - LET result = FCOMPARE(fileId1, fileId2)
 * - IF FCOMPARE(1, 2) THEN PRINT "Files are identical"
 * <p>
 * Return Values:
 * - BooleanValue(true): Both files have identical content line by line
 * - BooleanValue(false): Files have different content or different line counts
 * <p>
 * Technical Implementation:
 * - Queries FileManager to obtain file paths from file IDs
 * - Opens both files directly without registering in FileManager
 * - Reads files line by line using BufferedReader
 * - Compares each corresponding line
 * - Returns false on first difference found
 * - Returns false if files have different line counts
 * - Thread-safe and side-effect free
 * <p>
 * Error Handling:
 * - Throws RuntimeException if source file ID is not registered in FileManager
 * - Throws RuntimeException if destination file ID is not registered in FileManager
 * - Throws RuntimeException if file names are empty
 * - Throws RuntimeException if files cannot be opened or read
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FCompare {

    /**
     * Private Constructor.
     * Prevents instantiation of this utility class.
     */
    private FCompare() {
    }

    /**
     * Compares the content of two files identified by their file IDs.
     * <p>
     * Execution Flow:
     * 1. Verify both file IDs are registered in FileManager
     * 2. Get the file names from FileManager using the file IDs
     * 3. Validate file names are not empty
     * 4. Open both files for reading
     * 5. Read both files line by line and compare
     * 6. Close both file readers
     * 7. Return BooleanValue(true) if all lines match, BooleanValue(false) otherwise
     * <p>
     * Comparison Details:
     * - Files are compared line by line using String.equals()
     * - If one file has fewer lines than the other, returns false
     * - Empty lines are compared like any other line
     * - Comparison is case-sensitive and whitespace-sensitive
     * <p>
     * Return Values:
     * - BooleanValue(true) if both files have identical content
     * - BooleanValue(false) if content differs or line counts differ
     * - Never returns null
     * <p>
     * Implementation Notes:
     * - Files are opened directly using Java NIO, not through FileManager
     * - No side effects: does not modify FileManager state
     * - Thread-safe: Can be called concurrently
     * - Deterministic: Same files always produce same result
     * <p>
     * Performance Characteristics:
     * - O(n) where n is the size of the smaller file
     * - Returns false immediately on first difference found
     * - No file system modifications required
     * <p>
     * @param iFileId1 the file ID of the first file to compare
     * @param iFileId2 the file ID of the second file to compare
     * @return BooleanValue(true) if files have identical content, BooleanValue(false) otherwise
     * @throws RuntimeException if file IDs are not registered or files cannot be read
     */
    public static Value execute(final int iFileId1, final int iFileId2) throws RuntimeException {
        FileManager oFileManager = new FileManager();

        // Step 1: Verify first file is registered
        if (!oFileManager.getFileStatus(iFileId1)) {
            throw new RuntimeException("File with ID " + iFileId1 + " is not registered in FileManager");
        }

        // Step 2: Verify second file is registered
        if (!oFileManager.getFileStatus(iFileId2)) {
            throw new RuntimeException("File with ID " + iFileId2 + " is not registered in FileManager");
        }

        // Step 3: Get the file names from FileManager
        String strFile1Name = oFileManager.getFileName(iFileId1).toString();
        String strFile2Name = oFileManager.getFileName(iFileId2).toString();

        // Step 4: Validate file names are not empty
        if (strFile1Name.isEmpty()) {
            throw new RuntimeException("File name is empty for file ID " + iFileId1);
        }

        if (strFile2Name.isEmpty()) {
            throw new RuntimeException("File name is empty for file ID " + iFileId2);
        }

        // Step 5: Perform the comparison
        try {
            Path oFile1Path = Paths.get(strFile1Name);
            Path oFile2Path = Paths.get(strFile2Name);

            // Step 6: Open both files and compare line by line
            BufferedReader oReader1 = null;
            BufferedReader oReader2 = null;

            try {
                oReader1 = Files.newBufferedReader(oFile1Path, StandardCharsets.UTF_8);
                oReader2 = Files.newBufferedReader(oFile2Path, StandardCharsets.UTF_8);

                String strLine1;
                String strLine2;

                while (true) {
                    strLine1 = oReader1.readLine();
                    strLine2 = oReader2.readLine();

                    // Step 7: Check if both lines are null (end of both files)
                    if (strLine1 == null && strLine2 == null) {
                        return new BooleanValue(true);
                    }

                    // Step 8: Check if one file has more lines than the other
                    if (strLine1 == null || strLine2 == null) {
                        return new BooleanValue(false);
                    }

                    // Step 9: Compare the current lines
                    if (!strLine1.equals(strLine2)) {
                        return new BooleanValue(false);
                    }
                }
            } finally {
                // Step 10: Close resources
                if (oReader1 != null) {
                    try {
                        oReader1.close();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to close first file: " + e.getMessage());
                    }
                }

                if (oReader2 != null) {
                    try {
                        oReader2.close();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to close second file: " + e.getMessage());
                    }
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (IOException e) {
            throw new RuntimeException("File comparison operation failed: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during file comparison: " + e.getMessage());
        }
    }
}
