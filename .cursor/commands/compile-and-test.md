Ensure that the project compiles and tests pass.

Set JAVA_HOME to the Java 21 installation:
  export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home

Verify Java 21 is active by running `java -version` before building.
If Java 21 is not installed at that path, raise an error and request installation.

When the project compiles, always use the Maven build manager. If a Maven build manager is not available,
request the installation of Maven.

Always allow the Maven build manager to run the junit tests.
Always perform a clean build with: mvn clean test package