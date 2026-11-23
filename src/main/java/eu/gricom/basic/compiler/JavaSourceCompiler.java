package eu.gricom.basic.compiler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 * JavaSourceCompiler runs the Java compiler (javac) that is built into the JDK, on one source file.
 *
 * <p>No extra library is needed. The JDK's compiler is found with {@link ToolProvider}. A plain Java runtime
 * (JRE) does not contain a compiler, so the compiler reports a clear message in that case.</p>
 */
public final class JavaSourceCompiler {

    private JavaSourceCompiler() {
    }

    /**
     * Compiles one Java source file into class files.
     *
     * @param oJavaFile the .java file to compile
     * @param oOutputDir the folder where the .class files are written
     * @param strClassPath the classpath that the source needs (the GD-BASIC runtime)
     * @throws CompileException if the compiler is missing or the Java code has errors
     * @throws IOException if the output folder cannot be used
     */
    public static void compile(final Path oJavaFile, final Path oOutputDir, final String strClassPath)
            throws CompileException, IOException {
        JavaCompiler oCompiler = ToolProvider.getSystemJavaCompiler();
        if (oCompiler == null) {
            throw new CompileException("No Java compiler found. The compiler needs a JDK (not only a JRE).");
        }
        ByteArrayOutputStream oMessages = new ByteArrayOutputStream();
        int iResult = oCompiler.run(null, null, oMessages,
                "-classpath", strClassPath,
                "-d", oOutputDir.toString(),
                "-proc:none",
                oJavaFile.toString());
        if (iResult != 0) {
            throw new CompileException("Java compilation failed:\n"
                    + oMessages.toString(StandardCharsets.UTF_8));
        }
    }
}
