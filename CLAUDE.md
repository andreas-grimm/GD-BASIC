# GD-BASIC — Claude Code Guide

## Project Overview

GD-BASIC (GriCom Diminutive BASIC Interpreter) is a Java 21 implementation of a Dartmouth-style BASIC interpreter. It can execute `.bas` programs, support interactive line editing, and optionally compile them to Java source code. It also serves as an embeddable scripting engine.

- **Version**: 0.2.0+ (Extended)
- **License**: See LICENSE.md
- **Status**: Production Ready (1214/1214 tests passing)
- **Last Updated**: 2026-07-26

⚠️ **Breaking Change**: Version 0.2.0 removes the `#` suffix for real variables. Use untyped variables (no suffix) instead. See BASIC_CODING_STANDARD.md for migration guide.

✨ **New in 0.2.0+**: Interactive line editor with LOAD, SAVE, DELETE, HELP commands. Start without a file: `java -jar BASIC-*.jar`

## Build & Run

```bash
# Build (produces target/BASIC-*-jar-with-dependencies.jar)
mvn clean test package

# Run tests
mvn test

# Run a specific test class
mvn test -Dtest=BasicParserTest

# Generate site reports (Checkstyle, PMD, JavaDoc)
mvn site

# Interactive mode (no file required, NEW in 0.2.0+)
java -jar target/BASIC-0.2.0-jar-with-dependencies.jar

# Run with a program file
java -jar target/BASIC-0.2.0-jar-with-dependencies.jar program.bas

# Direct execution (skip interactive editor)
java -jar target/BASIC-0.2.0-jar-with-dependencies.jar -r program.bas

# Run with options
java -jar target/BASIC-*-jar-with-dependencies.jar -v debug program.bas   # verbose logging
java -jar target/BASIC-*-jar-with-dependencies.jar -q -r program.bas      # quiet, direct run
java -jar target/BASIC-*-jar-with-dependencies.jar -d program.bas         # Dartmouth mode
java -jar target/BASIC-*-jar-with-dependencies.jar -h                     # help
```

**Requirements**: Java 21+, Maven 3.6.3+

**Test Results**: 1214/1214 tests passing (100% pass rate, zero failures)

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
| `functions` | 40+ built-in functions (math, string, system, file) |
| `runtimeManager` | Execution engine — `Execute` |
| `lineEditor` | Interactive line editor — `LineEditor` (NEW in 0.2.0+) |
| `codeGenerator` | Java code generation |
| `error` | Exception types (includes new `EmptyProgramException`, `FileAlreadyExistsException`) |
| `helper` | `Logger`, `Printer`, `FileHandler`, `EnvParam` |

### Parser Evaluation Modes

Controlled by `_bDartmouthFlag` in `BasicParser`:
- **Dartmouth mode** (`-d` flag): left-to-right evaluation (legacy compatibility)
- **Standard mode** (default): standard BODMAS/PEMDAS precedence

### Variable Types

Type is indicated by suffix on variable name:
- `%` Integer, `&` Long, `$` String, `!` Double, `@` Boolean, (none) = Real (default)

Arrays are dynamically allocated — no DIM statement required.

## Interactive Line Editor (NEW in 0.2.0+)

The interpreter includes an interactive line editor accessible by running without a program file or by loading one.

### Editor Commands

| Command | Purpose |
|---|---|
| `LIST` | Display current program source code |
| `RUN` | Parse and execute the program (requires content) |
| `LOAD <filename>` | Load BASIC program from file |
| `SAVE <filename>` | Save program to new file (prevents overwriting) |
| `DELETE <line>` | Delete a single line |
| `DELETE <start> <end>` | Delete line range (inclusive) |
| `HELP` | Display built-in command help |
| `EXIT` / `BYE` / `QUIT` | Exit the interpreter |

### Program Entry

Enter lines with format: `<line-number> <statement>`

Example:
```
>10 PRINT "HELLO"
>20 END
>LIST
10 PRINT "HELLO"
20 END
>RUN
HELLO
>SAVE output.bas
Program saved to output.bas
```

### New Exception Classes

- **`EmptyProgramException`** — Thrown when loading empty file (file I/O validation)
- **`FileAlreadyExistsException`** — Thrown when SAVE target exists (file safety)

### Key New Methods

**Program.java**:
- `hasContent()` — Check if program has loaded content
- `loadProgram(String filename)` — Load from file with validation
- `save(String filename)` — Save to file with safety checks
- `deleteLines(int begin, int end)` — Remove line range

**LineEditor.java**:
- Enhanced `run()` method checks `hasContent()` before executing

## Configuration Management

**Version 0.2.0+**: The interpreter uses a YAML-based configuration system via the `EnvParam` singleton class.

### Configuration File

Configuration is loaded from `src/main/resources/application.yaml` with two configuration groups:

```yaml
environment:          # Default runtime settings
  app_name: GD-BASIC
  version: 0.2.0
  max_bcd_digits: 40
  dartmouth: false
  log_level: warning

testing:              # Test environment overrides
  max_bcd_digits: 40
  debug_mode: false
  timeout_seconds: 30
  timeout_float: 30.5
```

### Environment Variable Override

Environment variables take precedence over YAML configuration. To override a setting:

```bash
# Override via environment variable
export dartmouth=true
export log_level=debug
java -jar target/BASIC-*.jar program.bas

# Or inline
JAVA_HOME=/path/to/jdk-21 log_level=debug mvn test
```

### EnvParam API

The `EnvParam` singleton provides type-safe configuration access:

```java
String version = EnvParam.getString("version");        // "0.2.0"
int maxDigits = EnvParam.getInt("max_bcd_digits");     // 40
float timeout = EnvParam.getFloat("timeout_float");    // 30.5
boolean dartmouth = EnvParam.getBoolean("dartmouth");  // false
int legacy = EnvParam.getMaxBcdDigits();               // 40 (convenience method)
```

### Configuration Groups

Switch between configuration groups for testing or different environments:

```java
EnvParam.setConfigGroup("testing");  // Use testing config group
String appName = EnvParam.getString("app_name");
```

The singleton instance is cached; reset via reflection in tests using the teardown hook.

### Key Configuration Keys

| Key | Type | Default | Purpose |
|---|---|---|---|
| `app_name` | String | GD-BASIC | Application identifier |
| `version` | String | 0.2.0 | Version number (displayed in splash) |
| `max_bcd_digits` | Integer | 40 | Maximum BCD digits for real numbers |
| `dartmouth` | Boolean | false | Enable Dartmouth-style left-to-right evaluation |
| `log_level` | String | warning | Default logging level (trace, debug, info, warning) |
| `debug_mode` | Boolean | false | Enable debug output |
| `timeout_seconds` | Integer | 30 | Operation timeout in seconds |
| `timeout_float` | Float | 30.5 | Timeout with fractional seconds |

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

See `BASIC_CODING_STANDARD.md` for the full language spec and `doc/BASIC.md` for syntax and editor commands. Quick reference:

- Line numbers increment by 10 (convention)
- Multiple statements per line with `:`
- Programs must end with `END`
- **Math functions**: ABS, SIN, COS, TAN, SQR, EXP, LOG, LOG10, RND, ATN, CDBL, CINT
- **String functions**: LEN, LEFT$, RIGHT$, MID$, STR$, VAL, CHR$, ASC, INSTR, UPPER, LOWER
- **File I/O**: FOPEN, FCLOSE, FINPUT, FPRINT, EOF, FGET, FPUT, FPEEK, FREWIND, FEXISTS, FGETNAME, FGETSIZE, FMODTIME, FISOPEN, FLINECOUNT, FCOMPARE
- **Directory operations**: CHDIR, DIREXISTS, GETCWD, MKDIR, RMDIR
- **System functions**: CALL, SYSTEM, MEM, TIME
- **Total**: 40+ built-in functions

## Key Documentation

### Quick Reference
- **README.md** — Documentation index and quick start
- **CHANGELOG.md** — Complete version history (v0.0.1 through v0.2.0+)
- **TEST_SUMMARY.md** — Test coverage and documentation

### For Users
- **doc/USER_GUIDE.md** — Comprehensive user manual (NEW in 0.2.0+)
  - Installation, setup, command-line parameters
  - Interactive editor commands and workflows
  - Example programs and troubleshooting

### For Developers
- **doc/BASIC.md** — BASIC language reference
  - Syntax, keywords, variable types
  - Line editor command reference (NEW in 0.2.0+)
- **doc/BASIC_CODING_STANDARD.md** — BASIC language specification and migration guides
- **doc/GD-BASIC_Detailed_Design.md** — Architecture and design documentation

### For Maintainers
- **doc/Java_25_Needed_Changes.md** — Future Java upgrade roadmap (replaces Java22ComplianceGuide.md)
- **prompts/STYLEGUIDE.md** — Java coding style guide
- **DOCUMENTATION_REORGANIZATION.md** — Documentation structure changes (NEW in 0.2.0+)

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

## Clean Up

- Delete any temporary files before commiting the project
