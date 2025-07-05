# GD-BASIC Java Style Guide

## Table of Contents

1. [Overview](#overview)
2. [Naming Conventions](#naming-conventions)
3. [Code Structure](#code-structure)
4. [Formatting](#formatting)
5. [Best Practices](#best-practices)
6. [Project Standards](#project-standards)
7. [Version Control](#version-control)

## Overview

This style guide ensures consistent, readable code across the GD-BASIC project. Our primary goals are **conciseness**, **readability**, and **simplicity**.

### Key Principles

- **Consistency**: Follow established patterns throughout the codebase
- **Readability**: Code should be self-documenting and easy to understand
- **Maintainability**: Write code that's easy to modify and extend
- **Performance**: Consider performance implications of coding decisions

### Tools and Validation

All style guide rules are enforced using **Checkstyle** during the build process. This ensures consistent code quality across all contributions.

### Inspiration

This style guide combines elements from:
- [Android Contributors Style Guide](https://source.android.com/source/code-style.html)
- [Google Java Style Guide](https://google-styleguide.googlecode.com/svn/trunk/javaguide.html)
- Tutorial-focused Swift and Objective-C style guides

## Naming Conventions

### Packages

**Rule**: Use lowercase letters only, concatenate multiple words without hyphens or underscores.

❌ **Incorrect**:
```java
eu.GriCom.funky_widget
eu.gricom.funky-widget
```

✅ **Correct**:
```java
eu.gricom.funkywidget
eu.gricom.basic.parser
```

### Classes and Interfaces

**Rule**: Use **UpperCamelCase** (PascalCase).

✅ **Examples**:
```java
public class BasicParser { }
public interface Statement { }
public class OperatorExpression { }
```

### Methods

**Rule**: Use **lowerCamelCase**.

✅ **Examples**:
```java
public void parseExpression() { }
public Value evaluate() { }
public String getTokenText() { }
```

### Fields and Variables

#### General Rules

**Rule**: Use **lowerCamelCase** for regular fields and variables.

#### Hungarian Notation for Member Variables

**Rule**: Member variables (class-level fields) use Hungarian notation with type prefixes and underscore prefix.

**Type Prefixes**:
- `str` - String
- `i` - Integer
- `l` - Long
- `b` - Boolean
- `o` - Object (undetermined type)
- `f` - Float/Double
- `c` - Character

**Collection Prefixes**:
- `v` - Vector
- `a` - Array
- `l` - List
- `m` - Map

✅ **Examples**:
```java
// Constants
public static final int MAX_ARRAY_SIZE = 1000;
public static final String DEFAULT_ENCODING = "UTF-8";

// Member variables (with underscore prefix)
private static MyClass _oSingleton;
private int iCount;
private String strName;
private boolean bIsValid;
private Vector<String> vstrItems;
private List<Integer> liNumbers;
private Map<String, Value> mstrValues;
```

#### Static Fields

**Rule**: Use **UPPER_SNAKE_CASE** for static final constants.

✅ **Example**:
```java
public static final int THE_ANSWER = 42;
public static final String DEFAULT_CONFIG_PATH = "/etc/config.yaml";
```

### Variables and Parameters

**Rule**: Use **lowerCamelCase**. Avoid single-character names except for loop variables.

❌ **Incorrect**:
```java
String s;
int x;
```

✅ **Correct**:
```java
String username;
int counter;
for (int i = 0; i < items.size(); i++) { }  // Loop variable is acceptable
```

### Acronyms

**Rule**: Treat acronyms as words in camelCase.

❌ **Incorrect**:
```java
XMLHTTPRequest
String URL
findPostByID
```

✅ **Correct**:
```java
XmlHttpRequest
String url
findPostById
```

## Code Structure

### Access Level Modifiers

**Rule**: Always explicitly define access level modifiers for classes, methods, and member variables.

✅ **Example**:
```java
public class MyClass {
    private String strPrivateField;
    protected int iProtectedField;
    public void publicMethod() { }
    private void privateMethod() { }
}
```

### Field and Variable Declarations

**Rule**: Declare one variable per line for better readability.

❌ **Incorrect**:
```java
String username, twitterHandle, email;
int x, y, z;
```

✅ **Correct**:
```java
String username;
String twitterHandle;
String email;
int x;
int y;
int z;
```

### Classes

**Rule**: One class per source file. Inner classes are encouraged where appropriate for scoping.

✅ **Example**:
```java
public class OuterClass {
    // Outer class content
    
    private class InnerClass {
        // Inner class content
    }
}
```

### Enum Classes

**Rule**: Avoid enum classes when possible due to memory overhead. Prefer static constants.

**Exception**: Simple enums without methods may be formatted inline.

✅ **Examples**:
```java
// Preferred: Static constants
public static final int DIRECTION_EAST = 0;
public static final int DIRECTION_NORTH = 1;
public static final int DIRECTION_WEST = 2;
public static final int DIRECTION_SOUTH = 3;

// Acceptable: Simple enum
private enum CompassDirection { EAST, NORTH, WEST, SOUTH }
```

## Formatting

### Indentation

**Rule**: Use spaces, never tabs.

#### Block Indentation
**Rule**: Use 2 spaces for block indentation.

✅ **Example**:
```java
public void method() {
  if (condition) {
    doSomething();
  }
}
```

#### Line Wrap Indentation
**Rule**: Use 4 spaces for line continuation.

✅ **Example**:
```java
public void longMethodName(String parameter1, String parameter2,
    String parameter3) {
  // Method body
}
```

### Line Length

**Rule**: Maximum 100 characters per line.

### Vertical Spacing

**Rule**: One blank line between methods for visual clarity.

✅ **Example**:
```java
public void method1() {
  // Implementation
}

public void method2() {
  // Implementation
}
```

**Rule**: Use whitespace within methods to separate logical sections.

✅ **Example**:
```java
public void complexMethod() {
  // Section 1: Variable initialization
  String strInput = getInput();
  int iCount = parseCount(strInput);
  
  // Section 2: Validation
  if (iCount < 0) {
    throw new IllegalArgumentException("Count must be positive");
  }
  
  // Section 3: Processing
  processItems(iCount);
}
```

### Brace Style

**Rule**: Opening braces on the same line, closing braces on their own line.

✅ **Example**:
```java
public void method() {
  if (condition) {
    doSomething();
  } else {
    doSomethingElse();
  }
}
```

**Rule**: Always use braces for conditional statements, even single-line blocks.

❌ **Incorrect**:
```java
if (condition)
  doSomething();
```

✅ **Correct**:
```java
if (condition) {
  doSomething();
}
```

### Switch Statements

**Rule**: Always include a default case and comment intentional fall-through behavior.

✅ **Example**:
```java
switch (value) {
  case 1:
    doSomething();
    break;
  case 2:
    doSomething();
    // Intentional fall-through
  case 3:
    doSomethingElse();
    break;
  default:
    handleDefault();
    break;
}
```

### Annotations

**Rule**: Place annotations on the line before the method declaration.

✅ **Example**:
```java
@Override
public String toString() {
  return "MyClass";
}

@Deprecated
public void oldMethod() {
  // Implementation
}
```

## Best Practices

### Getters and Setters

**Rule**: Use getters and setters for external access to fields. Fields should rarely be public.

**Performance Note**: Access fields directly within the same class for better performance.

✅ **Example**:
```java
public class MyClass {
  private String strName;
  
  // External access
  public String getName() {
    return strName;
  }
  
  public void setName(String strName) {
    this.strName = strName;
  }
  
  // Internal access (direct field access for performance)
  public void processName() {
    if (strName != null) {  // Direct access
      // Process the name
    }
  }
}
```

### Method Length

**Rule**: If a method has too many logical sections, consider refactoring into multiple methods.

**Guideline**: Aim for methods under 50 lines. If a method exceeds this, consider breaking it down.

### Error Handling

**Rule**: Use appropriate exception types and provide meaningful error messages.

✅ **Example**:
```java
if (input == null) {
  throw new IllegalArgumentException("Input cannot be null");
}

if (count < 0) {
  throw new IllegalArgumentException("Count must be non-negative, got: " + count);
}
```

## Project Standards

### Project Naming Convention

**Rule**: Use 4-character project names with specific meaning:

- **First character**: "G" for internal projects
- **Second character**:
  - "D" - Development tools, libraries, utilities
  - "C" - Commercial projects
  - "R" - Reference and master data projects
  - "X" - Other categories

**Examples**:
- `GDXX` - Development utility project
- `GCXX` - Commercial project
- `GRXX` - Reference data project

### Required Project Files

**Rule**: Every project must include:

1. **README.md** - Project description, purpose, modification history, and planning
2. **LICENSE.md** - License governing software release
3. **STYLEGUIDE.md** - This coding conventions file

**Note**: Latest versions of these files are maintained in the GDXX project.

### Data Formats

**Rule**: Use the following formats:
- **Configuration**: YAML
- **Data Exchange**: JSON
- **Avoid**: XML (discouraged)

### Language

**Rule**: Use British English spelling throughout documentation and comments.

**Examples**:
- `colour` instead of `color`
- `behaviour` instead of `behavior`
- `organisation` instead of `organization`

## Version Control

### Git Workflow

**Rule**: Use Git as the version control system with the following branch structure:

- **Main** - Latest tested and released version
- **Test** - Release candidate
- **Development** - Daily build
- **Feature branches** - For projects with multiple developers

### Version Numbering

**Rule**: Use semantic versioning with three digits: `<major>.<minor>.<bugfix>`

**Examples**:
- `0.1.0` - Initial development version
- `1.0.0` - First stable release
- `1.2.3` - Bug fix release
- `2.0.0` - Major version with breaking changes

**Note**: Major version numbers < 0 indicate unpublished code.

### Commit Messages

**Rule**: Write clear, descriptive commit messages.

✅ **Good Examples**:
```
Add support for mathematical operator precedence
Fix division by zero error in RealValue class
Update documentation for new parser implementation
```

❌ **Poor Examples**:
```
fix bug
update
wip
```

## Copyright and Licensing

**Rule**: Keep copyright statements minimal in source files. Detailed copyright and licensing information should be documented in the LICENSE.md file.

✅ **Example**:
```java
/**
 * BasicParser.java
 * 
 * (c) 2020-2024 by Andreas Grimm, Den Haag, The Netherlands
 */
```

---

This style guide ensures consistent, maintainable code across the GD-BASIC project. All developers should follow these conventions to maintain code quality and readability.