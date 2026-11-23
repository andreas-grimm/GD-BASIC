package eu.gricom.basic.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Stream;

/**
 * JarPackager builds a runnable jar file for a compiled BASIC program.
 *
 * <p>The jar contains the compiled program <b>and</b> the GD-BASIC runtime classes that the program uses,
 * so it can be started on its own with <code>java -jar</code>. The runtime is copied from the jar (or the
 * class folder) that is running the compiler.</p>
 */
public final class JarPackager {

    private static final String MANIFEST_NAME = "META-INF/MANIFEST.MF";

    private JarPackager() {
    }

    /**
     * Writes the jar file.
     *
     * @param oRuntime the jar file (or class folder) that contains the GD-BASIC runtime
     * @param oClassesDir the folder with the compiled classes of the program
     * @param strMainClass the class with the main method
     * @param oJarFile the jar file to write
     * @throws IOException if a file cannot be read or written
     */
    public static void build(final Path oRuntime, final Path oClassesDir, final String strMainClass,
                             final Path oJarFile) throws IOException {
        Manifest oManifest = new Manifest();
        oManifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        oManifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, strMainClass);

        Set<String> aoWritten = new HashSet<>();
        try (OutputStream oFileOut = Files.newOutputStream(oJarFile);
             JarOutputStream oJar = new JarOutputStream(oFileOut, oManifest)) {
            aoWritten.add(MANIFEST_NAME);
            if (Files.isDirectory(oRuntime)) {
                addFolder(oJar, oRuntime, aoWritten);
            } else {
                copyRuntimeJar(oJar, oRuntime, aoWritten);
            }
            addFolder(oJar, oClassesDir, aoWritten);
        }
    }

    /** Copies every entry of the runtime jar, except the old manifest and the old signatures. */
    private static void copyRuntimeJar(final JarOutputStream oJar, final Path oRuntime, final Set<String> aoWritten)
            throws IOException {
        try (JarFile oSource = new JarFile(oRuntime.toFile())) {
            var oEntries = oSource.entries();
            while (oEntries.hasMoreElements()) {
                JarEntry oEntry = oEntries.nextElement();
                String strName = oEntry.getName();
                if (oEntry.isDirectory() || isSignatureOrManifest(strName) || !aoWritten.add(strName)) {
                    continue;
                }
                oJar.putNextEntry(new JarEntry(strName));
                try (InputStream oIn = oSource.getInputStream(oEntry)) {
                    oIn.transferTo(oJar);
                }
                oJar.closeEntry();
            }
        }
    }

    /** Adds every file below a folder, using the path relative to that folder as the entry name. */
    private static void addFolder(final JarOutputStream oJar, final Path oFolder, final Set<String> aoWritten)
            throws IOException {
        try (Stream<Path> oFiles = Files.walk(oFolder)) {
            for (Path oFile : (Iterable<Path>) oFiles.filter(Files::isRegularFile).sorted()::iterator) {
                String strName = oFolder.relativize(oFile).toString().replace('\\', '/');
                if (!aoWritten.add(strName)) {
                    continue;
                }
                oJar.putNextEntry(new JarEntry(strName));
                Files.copy(oFile, oJar);
                oJar.closeEntry();
            }
        }
    }

    private static boolean isSignatureOrManifest(final String strName) {
        if (strName.equals(MANIFEST_NAME)) {
            return true;
        }
        return strName.startsWith("META-INF/") && (strName.endsWith(".SF") || strName.endsWith(".RSA")
                || strName.endsWith(".DSA") || strName.endsWith(".EC"));
    }
}
