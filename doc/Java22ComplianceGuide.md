# Java 22 Compliance Guide for GD-BASIC

## Overview

This document outlines the changes required to upgrade the GD-BASIC interpreter from Java 8 to Java 22 compliance. The current codebase targets Java 8 and requires several updates to leverage modern Java features and ensure compatibility.

## Current State Analysis

**Current Configuration:**
- Java Version: 1.8 (Java 8)
- Maven Compiler Source/Target: 1.8
- Dependencies: JUnit 5.4.2, Commons CLI 1.3.1

## Required Changes

### 1. Maven Configuration Updates

#### 1.1 Update pom.xml Properties
```xml
<properties>
    <maven.compiler.source>22</maven.compiler.source>
    <maven.compiler.target>22</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <project.reporting.outputDirectory>doc</project.reporting.outputDirectory>
    <additionalparam>-Xdoclint:none</additionalparam>
    <revision>0.1.0-java22</revision>
    <java-version>22</java-version>
</properties>
```

#### 1.2 Update Maven Compiler Plugin
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.12.1</version>
    <configuration>
        <source>22</source>
        <target>22</target>
        <encoding>UTF-8</encoding>
        <compilerArgs>
            <arg>--enable-preview</arg>
        </compilerArgs>
    </configuration>
</plugin>
```

#### 1.3 Update Dependencies
```xml
<dependencies>
    <!-- Update JUnit to latest version -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.10.1</version>
        <scope>test</scope>
    </dependency>
    
    <!-- Update Commons CLI -->
    <dependency>
        <groupId>commons-cli</groupId>
        <artifactId>commons-cli</artifactId>
        <version>1.6.0</version>
    </dependency>
    
    <!-- Add modern logging framework -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>2.0.9</version>
    </dependency>
</dependencies>
```

### 2. Code Modernization

#### 2.1 Replace HashMap with Modern Collections

**Current Code:**
```java
private static Map<String, Integer> _aoLabels = new HashMap<>();
```

**Java 22 Approach:**
```java
// Option 1: Use Map.of() for immutable maps
private static final Map<String, Integer> _aoLabels = Map.of();

// Option 2: Use ConcurrentHashMap for thread safety
private static final Map<String, Integer> _aoLabels = new ConcurrentHashMap<>();

// Option 3: Use LinkedHashMap for ordered maps
private static final Map<String, Integer> _aoLabels = new LinkedHashMap<>();
```

**Files to Update:**
- `LabelStatement.java` (line 16)
- `MacroList.java` (lines 10-11)
- `FileManager.java` (lines 16-18)
- `VariableManagement.java` (lines 27-31)
- `LineNumberXRef.java` (lines 20-21)

#### 2.2 Replace ArrayList with Modern Collections

**Current Code:**
```java
List<Statement> aoStatements = new ArrayList<>();
```

**Java 22 Approach:**
```java
// Option 1: Use List.of() for immutable lists
List<Statement> aoStatements = List.of();

// Option 2: Use ArrayList with initial capacity
List<Statement> aoStatements = new ArrayList<>(16);

// Option 3: Use LinkedList for frequent insertions
List<Statement> aoStatements = new LinkedList<>();
```

**Files to Update:**
- `BasicParser.java` (lines 75, 85, 146, 443, 477)
- `BasicLexer.java` (line 26)
- `ReadStatementTest.java` (lines 21, 42, 67)

#### 2.3 Modernize Switch Statements

**Current Code:**
```java
switch (getToken(0).getType()) {
    case DATA:
        // ... code ...
        break;
    case EOP:
        // ... code ...
        break;
    default:
        // ... code ...
}
```

**Java 22 Approach (Switch Expressions):**
```java
// Option 1: Switch expression with yield
var result = switch (getToken(0).getType()) {
    case DATA -> {
        // ... code ...
        yield new DataStatement(_iPosition, aoValues);
    }
    case EOP -> {
        bContinue = false;
        _iPosition++;
        yield null;
    }
    default -> {
        _oLogger.debug("-parsePreRun-> found Token: <" + _iPosition + "> [" + getToken(0).getType() + "] ");
        _iPosition++;
        yield null;
    }
};

// Option 2: Pattern matching (Java 21+)
switch (getToken(0)) {
    case Token t when t.getType() == BasicTokenType.DATA -> {
        // ... handle DATA ...
    }
    case Token t when t.getType() == BasicTokenType.EOP -> {
        bContinue = false;
        _iPosition++;
    }
    default -> {
        // ... handle default ...
    }
}
```

**UnaryOperatorExpression Switch Usage:**
```java
// In UnaryOperatorExpression.evaluate()
switch (_oOperator) {
    case PLUS -> evaluateUnaryPlus(operandValue);
    case MINUS -> evaluateUnaryMinus(operandValue);
    case NOT -> evaluateUnaryNot(operandValue);
    default -> throw new SyntaxErrorException("Unknown unary operator: " + _oOperator);
}
```

**Files to Update:**
- `BasicParser.java` (lines 81, 156, 660)
- `OperatorExpression.java` (lines 61, 108)
- `VariableManagement.java` (line 64)
- `Normalizer.java` (line 85)
- `Function.java` (line 93)

#### 2.4 Use Pattern Matching for instanceof

**Current Code:**
```java
if (oToken.getType() == BasicTokenType.STRING) {
    oValue = new StringValue(oToken.getText());
} else {
    oValue = new RealValue(Double.parseDouble(oToken.getText()));
}
```

**Java 22 Approach:**
```java
// Pattern matching for instanceof
if (oToken.getType() instanceof BasicTokenType.STRING) {
    oValue = new StringValue(oToken.getText());
} else if (oToken.getType() instanceof BasicTokenType.NUMBER) {
    oValue = new RealValue(Double.parseDouble(oToken.getText()));
}
```

#### 2.5 Use var for Local Variables

**Current Code:**
```java
List<Value> aoValues = new ArrayList<>();
Value oValue;
Token oToken = getToken(0);
```

**Java 22 Approach:**
```java
var aoValues = new ArrayList<Value>();
var oValue = (Value) null;
var oToken = getToken(0);
```

**Note:** Use `var` judiciously - only when the type is obvious from the context.

#### 2.6 Use Text Blocks for Multi-line Strings

**Current Code:**
```java
String strMessage = "Token not of expected type:" + oToken.getType() + 
                   " Value: " + oToken.getText();
```

**Java 22 Approach:**
```java
String strMessage = """
    Token not of expected type: %s
    Value: %s
    """.formatted(oToken.getType(), oToken.getText());
```

#### 2.7 Use Records for Data Classes

**Current Code:**
```java
public class Token {
    private final String text;
    private final BasicTokenType type;
    private final int line;
    
    // constructor, getters, equals, hashCode, toString
}
```

**Java 22 Approach:**
```java
public record Token(String text, BasicTokenType type, int line) {
    // Automatically provides constructor, getters, equals, hashCode, toString
}
```

**Files to Consider:**
- `Token.java` - Convert to record
- `Value` implementations - Consider for simple value objects

#### 2.8 Use Sealed Classes for Type Safety

**Current Code:**
```java
public interface Value extends Expression {
    // ... methods ...
}

public class IntegerValue implements Value { ... }
public class RealValue implements Value { ... }
public class StringValue implements Value { ... }
```

**Java 22 Approach:**
```java
public sealed interface Value extends Expression 
    permits IntegerValue, RealValue, StringValue, BooleanValue, LongValue {
    // ... methods ...
}
```

### 3. Exception Handling Modernization

#### 3.1 Replace System.exit() with Proper Exception Handling

**Current Code:**
```java
} catch (Exception e) {
    System.out.println(e.getMessage());
    System.exit(1);
}
```

**Java 22 Approach:**
```java
} catch (SyntaxErrorException e) {
    _oLogger.error("Syntax error: " + e.getMessage());
    throw new InterpreterException("Failed to process program", e);
} catch (Exception e) {
    _oLogger.error("Unexpected error: " + e.getMessage(), e);
    throw new InterpreterException("Unexpected error during execution", e);
}
```

#### 3.2 Use Try-with-Resources for Resource Management

**Current Code:**
```java
FileInputStream fis = new FileInputStream(file);
// ... use file ...
fis.close();
```

**Java 22 Approach:**
```java
try (var fis = new FileInputStream(file)) {
    // ... use file ...
} catch (IOException e) {
    _oLogger.error("Failed to read file", e);
    throw new FileHandlingException("Failed to read file: " + file.getName(), e);
}
```

### 4. Performance Optimizations

#### 4.1 Use String Templates (Java 21+)

**Current Code:**
```java
_oLogger.debug("[" + oToken.getLine() + "] Token # <" + iCounter + ">: [" + oToken.getType() + "]: [" + oToken.getText() + "]");
```

**Java 22 Approach:**
```java
_oLogger.debug(STR."[\{oToken.getLine()}] Token # <\{iCounter}>: [\{oToken.getType()}]: [\{oToken.getText()}]");
```

#### 4.2 Use Virtual Threads for I/O Operations

**Current Code:**
```java
// Synchronous I/O operations
String input = scanner.nextLine();
```

**Java 22 Approach:**
```java
// Virtual threads for I/O
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    var future = executor.submit(() -> scanner.nextLine());
    String input = future.get(5, TimeUnit.SECONDS);
}
```

### 5. Testing Modernization

#### 5.1 Update Test Framework Usage

**Current Code:**
```java
@Test
public void testSomething() throws Exception {
    // ... test code ...
}
```

**Java 22 Approach:**
```java
@Test
@DisplayName("Should handle basic arithmetic operations")
void testBasicArithmetic() {
    assertThat(calculator.add(2, 3))
        .isEqualTo(5);
}
```

#### 5.2 Use AssertJ for Better Assertions

```xml
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <version>3.24.2</version>
    <scope>test</scope>
</dependency>
```

### 6. Build and Deployment Updates

#### 6.1 Update Maven Plugins

```xml
<!-- Update JAR plugin -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <version>3.3.0</version>
</plugin>

<!-- Update Assembly plugin -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-assembly-plugin</artifactId>
    <version>3.6.0</version>
</plugin>

<!-- Update Javadoc plugin -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-javadoc-plugin</artifactId>
    <version>3.6.3</version>
    <configuration>
        <source>22</source>
        <links>
            <link>https://docs.oracle.com/en/java/javase/22/docs/api/</link>
        </links>
    </configuration>
</plugin>
```

#### 6.2 Add Module System Support (Optional)

Create `module-info.java`:
```java
module eu.gricom.basic {
    requires java.base;
    requires java.logging;
    requires commons.cli;
    requires static junit.jupiter.engine;
    
    exports eu.gricom.basic;
    exports eu.gricom.basic.parser;
    exports eu.gricom.basic.statements;
    exports eu.gricom.basic.variableTypes;
    exports eu.gricom.basic.functions;
    exports eu.gricom.basic.memoryManager;
    exports eu.gricom.basic.tokenizer;
    exports eu.gricom.basic.error;
    exports eu.gricom.basic.helper;
    exports eu.gricom.basic.macroManager;
    exports eu.gricom.basic.runtimeManager;
    exports eu.gricom.basic.codeGenerator;
    exports eu.gricom.basic.math;
}
```

### 7. Migration Strategy

#### Phase 1: Configuration Updates
1. Update `pom.xml` with Java 22 settings
2. Update Maven plugins
3. Update dependencies

#### Phase 2: Core Modernization
1. Replace collections with modern alternatives
2. Update switch statements
3. Implement pattern matching

#### Phase 3: Exception Handling
1. Remove `System.exit()` calls
2. Implement proper exception hierarchy
3. Add logging framework

#### Phase 4: Performance Optimization
1. Implement string templates
2. Add virtual threads where appropriate
3. Optimize collections usage

#### Phase 5: Testing and Validation
1. Update test framework usage
2. Add comprehensive tests
3. Performance testing

### 8. Backward Compatibility

#### 8.1 Maintain Java 8 Compatibility (Optional)
If backward compatibility is required, consider:
- Using multi-release JARs
- Conditional compilation
- Feature detection at runtime

#### 8.2 Gradual Migration
- Implement changes incrementally
- Maintain working state throughout migration
- Use feature flags for new functionality

### 9. Benefits of Java 22 Migration

1. **Performance**: Better JVM performance and optimizations
2. **Security**: Latest security patches and features
3. **Maintainability**: Modern language features reduce boilerplate
4. **Developer Experience**: Better tooling and IDE support
5. **Future-Proofing**: Access to latest Java features and ecosystem

### 10. Potential Issues and Solutions

#### 10.1 Breaking Changes
- **Issue**: Some APIs may have changed between Java 8 and 22
- **Solution**: Review deprecation warnings and update accordingly

#### 10.2 Performance Regressions
- **Issue**: New features might introduce performance overhead
- **Solution**: Benchmark critical paths and optimize as needed

#### 10.3 Third-party Dependencies
- **Issue**: Some dependencies may not support Java 22
- **Solution**: Update to compatible versions or find alternatives

## Conclusion

The migration to Java 22 will significantly modernize the GD-BASIC interpreter, providing better performance, security, and maintainability. The changes should be implemented incrementally to ensure stability throughout the migration process.

**Estimated Effort**: 2-3 weeks for full migration
**Risk Level**: Medium (due to extensive changes)
**Recommended Approach**: Incremental migration with comprehensive testing at each phase 