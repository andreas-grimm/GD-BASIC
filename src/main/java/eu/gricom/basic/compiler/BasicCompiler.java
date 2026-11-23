package eu.gricom.basic.compiler;

import eu.gricom.basic.statements.Statement;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * BasicCompiler is the entry point of the compiler. The interpreter's editor calls it for the COMPILE and
 * SAVEJAVA commands.
 *
 * <p>Steps, in order: (1) generate the Java source from the parsed statements, (2) save the source file,
 * (3) compile it with javac, (4) build a runnable jar with the program and the GD-BASIC runtime.</p>
 */
public final class BasicCompiler {

    private static final Pattern CLASS_NAME = Pattern.compile("[A-Za-z_][A-Za-z0-9_]*");
    private static final String JAVA_EXTENSION = ".java";

    private BasicCompiler() {
    }

    /**
     * Generates the Java source code for a program.
     *
     * @param aoPreRunStatements the statements that run before the program (must be empty for now)
     * @param aoStatements the parsed program statements
     * @param strClassName the Java class name (taken from the file name)
     * @return the Java source code
     * @throws CompileException if the program uses something the compiler does not support yet
     */
    public static String generateJava(final List<Statement> voPreRunStatements,
                                      final List<Statement> voStatements, final String strClassName)
            throws CompileException {
        if (voPreRunStatements != null && !voPreRunStatements.isEmpty()) {
            throw new CompileException("Statement not supported by the compiler yet: DATA (the data is read "
                    + "before the program starts, so it is not compiled yet)");
        }
        return new JavaCodeGenerator(voStatements, strClassName).generate();
    }

    /**
     * Generates the Java source code and saves it to a file.
     *
     * @param aoPreRunStatements the pre-run statements (must be empty for now)
     * @param aoStatements the parsed program statements
     * @param oJavaFile the .java file to write; its name (without .java) is the class name
     * @throws CompileException if the program uses something the compiler does not support yet
     * @throws IOException if the file cannot be written
     */
    public static void saveJava(final List<Statement> voPreRunStatements, final List<Statement> voStatements,
                                final Path oJavaFile) throws CompileException, IOException {
        String strSource = generateJava(voPreRunStatements, voStatements, classNameOf(oJavaFile));
        Files.writeString(oJavaFile, strSource, StandardCharsets.UTF_8);
    }

    /**
     * Generates the Java source, compiles it and builds the runnable jar.
     *
     * @param aoPreRunStatements the pre-run statements (must be empty for now)
     * @param aoStatements the parsed program statements
     * @param oJavaFile the .java file to write; its name (without .java) is the class name
     * @param oJarFile the jar file to build
     * @throws CompileException if the program cannot be compiled
     * @throws IOException if a file cannot be read or written
     */
    public static void compileToJar(final List<Statement> voPreRunStatements, final List<Statement> voStatements,
                                    final Path oJavaFile, final Path oJarFile)
            throws CompileException, IOException {
        String strClassName = classNameOf(oJavaFile);
        saveJava(voPreRunStatements, voStatements, oJavaFile);

        Path oRuntime = runtimeLocation();
        Path oClassesDir = Files.createTempDirectory("gdbasic-classes");
        try {
            JavaSourceCompiler.compile(oJavaFile, oClassesDir, oRuntime.toString());
            JarPackager.build(oRuntime, oClassesDir, strClassName, oJarFile);
        } finally {
            deleteFolder(oClassesDir);
        }
    }

    /**
     * The class name for a Java file: the file name without the .java extension.
     *
     * @param oJavaFile the file
     * @return the class name
     * @throws CompileException if the name is not a valid Java class name
     */
    public static String classNameOf(final Path oJavaFile) throws CompileException {
        Path oFileName = oJavaFile.getFileName();
        String strFile = oFileName == null ? "" : oFileName.toString();
        if (!strFile.endsWith(JAVA_EXTENSION)) {
            throw new CompileException("The Java file name must end with .java: " + strFile);
        }
        String strName = strFile.substring(0, strFile.length() - JAVA_EXTENSION.length());
        if (!CLASS_NAME.matcher(strName).matches()) {
            throw new CompileException("The Java file name is not a valid class name: " + strFile);
        }
        return strName;
    }

    /**
     * The file (jar) or folder that contains the GD-BASIC runtime classes, the one running this code.
     *
     * @return the location
     * @throws CompileException if the location cannot be found
     */
    public static Path runtimeLocation() throws CompileException {
        try {
            return Paths.get(BasicCompiler.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException | NullPointerException oException) {
            throw new CompileException("The GD-BASIC runtime location cannot be found: " + oException);
        }
    }

    private static void deleteFolder(final Path oFolder) throws IOException {
        try (Stream<Path> oPaths = Files.walk(oFolder)) {
            for (Path oPath : (Iterable<Path>) oPaths.sorted(Comparator.reverseOrder())::iterator) {
                Files.deleteIfExists(oPath);
            }
        }
    }
}
