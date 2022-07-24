package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

/**
 * STR Function.
 *
 * Description:
 *
 * The STR function returns the string value of an input value.
 *
 * (c) = 2021,.., by Andreas Grimm, Den Haag, The Netherlands
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
