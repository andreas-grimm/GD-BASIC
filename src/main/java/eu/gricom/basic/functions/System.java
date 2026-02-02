package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

/**
 * System.java
 * <p>
 * Description: The System class implements the BASIC SYSTEM function, which executes operating system commands and
 * returns their output. It allows BASIC programs to interact with the underlying system shell for tasks such as file
 * operations or running external programs.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class System {

    /**
     * Private Constructor.
     */
    private System() {
    }

    /**
     * Functions implemented here are similar to Statements with the difference
     * that they actually return a result to the caller of type Value. The method execute
     * triggers the function.
     *
     * @param oCommand parameters for the program to execute
     * @param oValue name of the program to execute
     * @return Value the return message of the function
     * @throws Exception as any execution error found during execution
     */
    public static Value execute(final Value oCommand, final Value oValue) throws Exception {
        String strReturn = new String();
        if (oValue instanceof StringValue
                && oCommand instanceof StringValue) {

            ProcessBuilder oProcessBuilder = new ProcessBuilder();

            if (oCommand.toString().toUpperCase(Locale.ROOT).matches("RUN")) {
                oProcessBuilder.command("bash", "-c", oValue.toString());
            } else {
                throw new RuntimeException("Parameter error");
            }

            try {
                Process oProcess = oProcessBuilder.start();
                BufferedReader oReader;
                String strInput = new String();

                oReader = new BufferedReader(new InputStreamReader(oProcess.getInputStream()));

                while ((strInput = oReader.readLine()) != null) {
                    strReturn += strInput + "\n";
                }

                int iExitCode = oProcess.waitFor();

                if (iExitCode != 0) {
                    throw new RuntimeException("System execution error: " + iExitCode);
                }

            } catch (IOException eIOException) {
                throw new RuntimeException("System execution interrupt: " + eIOException.toString());
            } catch (InterruptedException eInteruptedException) {
                throw new RuntimeException("System execution interrupt: " + eInteruptedException.toString());
            }

            return new StringValue(strReturn);
        }

        throw new RuntimeException("Parameter for System function call not of type String");
    }
}
