# GD-BASIC — Claude Code Guide

## Project Overview

GD-BASIC (GriCom Diminutive BASIC Interpreter) is a Java 21 implementation of a Dartmouth-style BASIC interpreter. It can execute `.bas` programs and optionally compile them to Java source code. It also serves as an embeddable scripting engine.

- **Version**: 0.1.1
- **License**: See LICENSE.md

## Build & Run

```bash
# Build (produces target/BASIC-*-jar-with-dependencies.jar)
mvn clean package

# Run tests
mvn test

# Run a specific test class
mvn test -Dtest=BasicParserTest

# Generate site reports (Checkstyle, PMD, JavaDoc)
mvn site

# Run the interpreter
java -jar target/BASIC-0.1.1-jar-with-dependencies.jar program.bas

# Run with options
java -jar target/BASIC-*-jar-with-dependencies.jar -v debug program.bas   # verbose
java -jar target/BASIC-*-jar-with-dependencies.jar -c -b program.bas       # compile mode
java -jar target/BASIC-*-jar-with-dependencies.jar -d program.bas          # Dartmouth mode
```

**Requirements**: Java 21+, Maven 3.6.3+

## Architecture

Processing pipeline:

```
.bas source → Macro Processing → Tokenization (BasicLexer)
           → Parsing (BasicParser) → Execution (Execute)
```

Optionally: instead of Execute, the Generator compiles to Java via a JSON intermediate.

### Key Packages (`eu.gricom.basic.*`)

| Package | Purpose |
|---|---|
| `tokenizer` | Lexical analysis — `BasicLexer`, `Token`, `BasicTokenType` |
| `parser` | Recursive-descent parser — `BasicParser` |
| `statements` | 35+ statement implementations (IF, FOR, PRINT, GOSUB, …) |
| `variableTypes` | Type system — `RealValue`, `IntegerValue`, `StringValue`, `LongValue`, `BooleanValue` |
| `memoryManager` | State — `Program`, `VariableManagement`, `Stack`, `ProgramPointer` |
| `functions` | 30+ built-in functions (math, string, system) |
| `runtimeManager` | Execution engine — `Execute` |
| `codeGenerator` | Java code generation |
| `error` | Exception types |
| `helper` | `Logger`, `Printer`, `FileHandler` |

### Parser Evaluation Modes

Controlled by `_bDartmouthFlag` in `BasicParser`:
- **Dartmouth mode** (`-d` flag): left-to-right evaluation (legacy compatibility)
- **Standard mode** (default): standard BODMAS/PEMDAS precedence

### Variable Types

Type is indicated by suffix on variable name:
- `#` Real (double), `%` Integer, `&` Long, `$` String, `!` Boolean, (none) = Real

Arrays are dynamically allocated — no DIM statement required.

## Testing

```bash
# Unit tests (JUnit 5)
mvn test

# System integration tests (BASIC programs)
test/system/run_all_tests.sh

# Run a single system test
java -jar target/BASIC-*-jar-with-dependencies.jar test/system/test_arithmetic_operators.bas
```

- **Unit tests**: `src/test/java/eu/gricom/basic/`
- **System tests**: `test/system/*.bas` — each focused on one feature area
- **Regression tests**: `test/regression/` — known-good programs

System test pattern: tests print step descriptions; failures GOTO 9000 (error handler); success prints "PASSED".

## Coding Standards

Follow `prompts/STYLEGUIDE.md`. Key rules:

- **Hungarian notation** for member variables with underscore prefix:
  - `_strName` (String), `_iCount` (int), `_bFlag` (boolean), `_oRef` (Object)
  - `_fValue` (float), `_vList` (Vector), `_aArr` (array), `_mMap` (Map)
- **Classes**: PascalCase; **Methods**: camelCase; **Constants**: UPPER_SNAKE_CASE
- **Indentation**: 4 spaces (no tabs)
- **Line length**: max 100 chars (120 in exceptional cases)
- **Braces**: opening on same line, closing on own line
- **Access modifiers**: always explicit

Code quality is enforced by Checkstyle (`etc/checkstyle-config.xml`) and PMD — both run during `mvn site`.

## BASIC Language Reference

See `BASIC_CODING_STANDARD.md` for the full language spec. Quick reference:

- Line numbers increment by 10 (convention)
- Multiple statements per line with `:`
- Programs must end with `END`
- Math functions: ABS, SIN, COS, TAN, SQR, EXP, LOG, RND, …
- String functions: LEN, LEFT$, RIGHT$, MID$, STR$, VAL, CHR$, ASC, INSTR
- File I/O: FOPEN, FCLOSE, FINPUT, FPRINT, EOF

## Key Documentation

- `README.md` — project history and version changelog
- `BASIC_CODING_STANDARD.md` — BASIC language specification
- `doc/TechnicalDocumentation.md` — architecture guide
- `doc/ParserDesign.md` — parser implementation details
- `doc/OperatorPrecedenceImplementation.md` — expression evaluation modes
- `prompts/STYLEGUIDE.md` — Java coding style guide

## AI Assistant Persona

Act as a **senior Java architect and developer** collaborating on design, development, and testing.

- **Role**: Senior Java Architect and Developer
- **Expertise**: Java 8 through the latest release

**Always**:
- Provide professional, clear, actionable technical guidance
- Offer expert Java knowledge with specific, well-explained solutions
- Follow project guidelines (coding standards, documentation)
- Write tests and detailed documentation targeting the highest quality standards
- Write documentation accessible to junior developers or developers from other languages
- When analysing code that is missing method-header or inline documentation, ask whether it should be added
- Use Agile methodologies and test-driven development
- Use plain English and plain Java (no frameworks)

**Avoid**:
- Technical jargon
- The Java Spring Framework

## Build Rules

Always use the same Java version as defined in `pom.xml` (`maven.compiler.source`). Currently Java 21.

Before building, ensure that Java version is active:

```bash
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home
java -version   # must report Java 21
```

Always use Maven. Always run a clean build with tests:

```bash
mvn clean test package
```

## Workspace Organisation

```
project/
├── src/        # all source code, resource files, and tests
├── bin/        # support scripts to run and test the project
├── docs/       # project documentation and design
│               # (Markdown files, Mermaid graphics, British English)
└── pom.xml     # build configuration
```

## Protection Rules

- **Read-only**: `agent/` and `.cursor/` folders — never modify these
- **Git push**: never `git push` without explicit user confirmation ("yes" or "confirm")
- **Branch**: never leave the current branch without explicit instruction
- **Third-party software**: never install without explicit user confirmation ("yes" or "confirm")
- **Deleting code**: never delete code without approval — mark redundant code as `@Deprecated`, comment out unused code and tag with `TODO`
