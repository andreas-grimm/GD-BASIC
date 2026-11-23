![CodeRabbit Pull Request Reviews](https://img.shields.io/coderabbit/prs/github/andreas-grimm/GD-BASIC?utm_source=oss&utm_medium=github&utm_campaign=andreas-grimm%2FGD-BASIC&labelColor=171717&color=FF570A&link=https%3A%2F%2Fcoderabbit.ai&label=CodeRabbit+Reviews)

# GriCom Diminutive BASIC Interpreter (GD-BASIC)

&copy; 2020 - 2026 Andreas Grimm | Use according to the included licence file ([LICENSE.md](LICENSE.md))

**Version:** 0.4.0 (branch `feature/compiler`, not yet merged; it contains the decimal work from `feature/bcd-arithmetic`; last released version 0.2.1)  
**Status:** In development. On 2026-11-23: 1560 of 1560 unit tests and 109 of 109 system test runs passed. Open problems are listed in [doc/KNOWN_ISSUES.md](doc/KNOWN_ISSUES.md).  
**Last Updated:** 2026-11-23

---

## 📚 Project Overview

**GD-BASIC** is a Java 21+ implementation of a Dartmouth-style BASIC interpreter. It can execute `.bas` programs and supports interactive line editing.

### Key Features

- ✅ **Interactive Line Editor** — Build and test programs without file setup
- ✅ **File I/O** — LOAD, SAVE, DELETE commands in the editor
- ✅ **Comprehensive BASIC Support** — 41 files in `functions/` (39 function classes) and 46 files in `statements/` (40 statement classes), counted 2026-11-23
- ✅ **Computed Branches** — ON GOTO and ON GOSUB for dynamic control flow
- ✅ **Type Safety** — Strongly-typed variable system, including exact decimal (BCD) numbers
- ✅ **Advanced Features** — Arrays, loops, conditionals, file operations
- ✅ **Tested** — 1560/1560 unit tests passing on 2026-11-23. System test programs: 109/109 runs passing. Open defects are in `doc/KNOWN_ISSUES.md`
- ✅ **BASIC-to-Java compiler** — `COMPILE` builds a runnable jar from a `.bas` program (0.4.0)
- ✅ **Well Documented** — User guides, architecture docs, glossary, code examples

---

## 🚀 Quick Start

### Installation

```bash
# Requirements: Java 21+
java -version  # Should show Java 21 or later

# Download the JAR
# File: BASIC-0.4.0-jar-with-dependencies.jar
```

### Run Interactive Mode (No File Required)

```bash
java -jar BASIC-0.4.0-jar-with-dependencies.jar
>10 PRINT "HELLO WORLD"
>20 END
>RUN
HELLO WORLD
>HELP
```

### Load and Modify a Program

```bash
java -jar BASIC-0.4.0-jar-with-dependencies.jar existing.bas
>LIST
>DELETE 15
>30 PRINT "Modified"
>SAVE output.bas
```

### Direct Execution (No Editor)

```bash
java -jar BASIC-0.4.0-jar-with-dependencies.jar -r -q program.bas
```

---

## 📖 Documentation

### For Users
- **[QUICK_START.md](doc/QUICK_START.md)** — Start in 15 minutes, every example checked
- **[GLOSSARY.md](doc/GLOSSARY.md)** — Plain-English explanation of the words used in the documents
- **[KNOWN_ISSUES.md](doc/KNOWN_ISSUES.md)** — Open problems, with repro programs and source lines
- **[USER_GUIDE.md](doc/USER_GUIDE.md)** — Complete user manual
  - Installation and setup
  - Command-line parameters with examples
  - Interactive line editor commands (LIST, RUN, LOAD, SAVE, DELETE, HELP, EXIT)
  - Program development workflows
  - 5 example programs
  - Troubleshooting guide
  - Performance tips

### For Developers
- **[BASIC.md](doc/BASIC.md)** — BASIC language reference
  - Language syntax and features
  - Reserved words and operators
  - Variable types and arrays
  - Built-in functions
  - Interactive line editor commands
  - Code examples

- **[BASIC_CODING_STANDARD.md](doc/BASIC_CODING_STANDARD.md)** — BASIC programming standards
  - Language specification
  - Coding conventions
  - Statement reference
  - Function reference
  - Migration guides

- **[GD-BASIC_Detailed_Design.md](doc/GD-BASIC_Detailed_Design.md)** — Architecture documentation
  - System architecture
  - Processing pipeline
  - Class hierarchy
  - Design patterns
  - Future enhancements

### For Project Maintainers
- **[Java_25_Needed_Changes.md](doc/Java_25_Needed_Changes.md)** — Future upgrade roadmap
  - Java version migration path
  - Planned enhancements
  - Risk assessment
  - Timeline estimates

- **[CHANGELOG.md](CHANGELOG.md)** — Complete version history
  - All releases from v0.0.1 to v0.4.0 (0.4.0 not yet merged)
  - Features by version
  - Test results
  - Bug fixes and improvements

- **[SYSTEM_TEST_GUIDE.md](test/system/SYSTEM_TEST_GUIDE.md)** — System test programs
  - How to run them, and how a program is judged
  - Which program covers which feature
- **[COMPILER.md](doc/COMPILER.md)** — The BASIC-to-Java compiler, step by step

---

## 📋 Current Version (0.4.0, branch `feature/bcd-arithmetic`)

### What is new in 0.4.0

**Decimal numbers (BCD)**:
- `amount#` holds a decimal number with up to 40 significant digits: `0.1 + 0.2` gives exactly `0.3`
- Arithmetic, comparisons, `SQR`, `EXP`, `LOG`, `LOG10`, `SIN`, `COS`, `TAN`, `ATN`, `ABS`, `CINT`, `STR$`
- An overflow (more than 40 digits before the decimal point) stops the program with an error

**Suffix changes**:
- `!` is now the BOOLEAN suffix (`FLAG! = TRUE`)
- `@` is no longer a variable suffix and gives an error

**Language fixes** (Phase 6 and 7): block `IF` with `ELSE`, nested `FOR` and `WHILE` loops, `FOR` as the first statement, `INPUT` read in several places, `ON GOTO` / `ON GOSUB`, file statements inside blocks, `FRENAME`, `FPEEK`, `FGET`, `FPRINT`, `PRINT` lists, `WEND`, string index with a variable, `INSTR` (one-based), `TIME` (milliseconds)

**Test runner**: `test/system/run_all_tests.sh` now fails when a program stops with an error or prints `ERROR: Test failed`. The interpreter exits with code 1 on a parse error or a runtime error.

**Test coverage**:
- 1560/1560 unit tests passing ✅ (run on 2026-11-23)
- 109/109 system test runs passing ✅ (62 BASIC programs, with the BCD, start-directory and compiler tests)
- BASIC-to-Java compiler: `-o`, `-j`, `COMPILE` and `SAVEJAVA` (see `doc/COMPILER.md`)
- Directory and PRAGMA fixes: `GETCWD` returns the absolute start directory; `@PRAGMA( "key" = "value" )` needs brackets; unary minus on a boolean swaps `TRUE` and `FALSE`

### Previous Releases

- **v0.2.0** (June 2026) — Type system simplification, string case functions
- **v0.1.1** (May 2026) — Advanced file operations, complete test coverage
- **v0.1.0** (July 2025) — Operator precedence, mathematical functions
- **v0.0.8** — File handling system
- **v0.0.7** — Macro framework
- **v0.0.6** — Arrays and functions
- **v0.0.5** — Loops (FOR, DO-UNTIL, GOSUB)
- **v0.0.4** — Dartmouth BASIC parser
- **v0.0.3** — Type system and commands
- **v0.0.2** — Tooling (CLI, Logger, Tests)
- **v0.0.1** — Initial project setup

See [CHANGELOG.md](CHANGELOG.md) for complete details.

---

## 🔧 Building from Source

### Prerequisites
- Java 21+ JDK
- Maven 3.6.3+

### Build Commands

```bash
# Clean build with tests
mvn clean test package

# Run specific test class
mvn test -Dtest=BasicParserTest

# Build without tests
mvn clean package -DskipTests

# Generate documentation
mvn javadoc:javadoc

# Site reports (Checkstyle, PMD)
mvn site
```

### Test Results
```
Tests: 1560/1560 unit tests pass ✅ (109/109 system test runs, 2026-11-23)
Build: SUCCESS
Quality: Checkstyle reports 32 warnings (all `MethodLength`, approved to stay as warnings). PMD was not run on 2026-11-23.
```

---

## 💡 Use Cases

### Educational
- Learn BASIC programming
- Understand interpreter design
- Study parsing and execution pipelines

### Scripting
- Automate tasks with BASIC scripts
- Embed in Java applications
- Integrate with build systems

### Testing
- Test BASIC compatibility
- Develop BASIC teaching materials
- Prototype language features

### Development
- Extend the interpreter
- Add new functions
- Implement new statements

---

## 🏗️ Architecture Highlights

### Processing Pipeline
```
.bas source → Tokenization (BasicLexer)
           → Parsing (BasicParser)
           → Execution (Execute)
           → Output
```

### Key Components
- **Tokenizer** (`BasicLexer`) — Lexical analysis
- **Parser** (`BasicParser`) — Recursive-descent parser
- **Statements** (40 classes) — Statement implementations
- **Functions** (39 function classes in 41 files) — Built-in functions
- **Variables** (6 types) — Type system
- **Memory** (`VariableManagement`) — Variable storage
- **File I/O** (`FileManager`) — File operations
- **Line Editor** (`LineEditor`) — Interactive mode

### Type System
- **Untyped** (default real) — `X`, `value`
- **Integer** — `count%`, `index%`
- **Long** — `bignum&`, `largeint&`
- **String** — `name$`, `text$`
- **Boolean** — `flag!`, `condition!` (version 0.4.0; in 0.2.1 the suffix is `@`)
- **Decimal (BCD)** — `amount#`, up to 40 significant digits (version 0.4.0)

Version 0.4.0: a variable with no suffix is REAL, `#` is the decimal (BCD) suffix, and `!` is the boolean suffix. In 0.2.1 `#` is rejected and `!` is Double. See `doc/BASIC.md`, section "Variables, Arrays, and Constants".

---

## 📊 Statistics

- **1560/1560 unit tests** pass ✅ and **109/109 system test runs** pass ✅ (2026-11-23)
- **40 statement classes** in `statements/`
- **39 function classes** in `functions/`
- **6 variable types** supported
- **Zero code quality issues** (Checkstyle, PMD)
- **~5 years** development history
- **150+ commits** (tracked)

---

## 🔗 References

### Based On
- [JASIC Project](https://github.com/munificent/jasic) by Bob Nystrom
- [Dartmouth BASIC October 1964](doc/Dartmouth_BASIC_Oct64.pdf) specification
- [Atari VSC Extension](https://github.com/marcin-jozwikowski/atari-basic-vsc-extension)

### Related Resources
- [Dartmouth BASIC Manual](https://en.wikipedia.org/wiki/Dartmouth_BASIC)
- [Java 21 Documentation](https://docs.oracle.com/en/java/javase/21/)
- [Maven Documentation](https://maven.apache.org/guides/)

---

## 📁 Directory Structure

```
GD-BASIC/
├── README.md .......................... This file
├── CHANGELOG.md ....................... Version history
├── LICENSE.md ......................... License
├── pom.xml ............................ Maven configuration
│
├── doc/
│   ├── USER_GUIDE.md .................. User manual
│   ├── BASIC.md ....................... Language reference
│   ├── BASIC_CODING_STANDARD.md ....... Coding standards
│   ├── GLOSSARY.md .................... Plain-English word list
│   ├── QUICK_START.md ................. Start in 15 minutes
│   ├── COMPILER.md .................... BASIC-to-Java compiler
│   ├── KNOWN_ISSUES.md ................ Open problems, with repro steps
│   ├── GD-BASIC_Detailed_Design.md .... Architecture
│   └── Java_25_Needed_Changes.md ...... Future roadmap
│
├── src/
│   ├── main/java/eu/gricom/basic/ .... Source code
│   │   ├── tokenizer/ ................. Lexer
│   │   ├── parser/ .................... Parser
│   │   ├── statements/ ................ Statements (40 classes)
│   │   ├── functions/ ................. Functions (39 classes)
│   │   ├── variabletypes/ ............. Type system
│   │   ├── memorymanager/ ............. Memory & storage
│   │   ├── lineeditor/ ................ Interactive editor
│   │   ├── runtimemanager/ ............ Execution engine
│   │   ├── helper/ .................... Utilities
│   │   └── error/ ..................... Exceptions
│   │
│   ├── main/resources/
│   │   ├── application.yaml ........... Configuration
│   │   └── help.txt ................... Editor help
│   │
│   └── test/java/eu/gricom/basic/ .... Tests (1560 unit tests)
│
├── target/
│   ├── BASIC-0.4.0.jar ................ Standard JAR
│   └── BASIC-0.4.0-jar-with-dependencies.jar .. Executable JAR
│
└── test/system/ ....................... System tests (62 programs, plus test/system/compiler/)
```

---

## 🤝 Contributing

Contributions are welcome! The project maintains:
- **Code Quality**: Checkstyle and PMD compliance
- **Test Coverage**: 100% pass rate required
- **Documentation**: All features documented
- **Standards**: Hungarian notation, 4-space indent

### Development Workflow
1. Create a feature branch
2. Make your changes
3. Run tests: `mvn clean test`
4. Verify site reports: `mvn site`
5. Submit pull request

---

## ⚖️ License

This project is licensed under the terms in [LICENSE.md](LICENSE.md).

**Copyright Notice:**
```
© 2020 - 2026 Andreas Grimm
The Netherlands / Norway
```

---

## 📞 Support

### Documentation
- [USER_GUIDE.md](doc/USER_GUIDE.md) — Getting started
- [BASIC.md](doc/BASIC.md) — Language reference
- [CHANGELOG.md](CHANGELOG.md) — Version history

### Issues
- Report bugs on GitHub Issues
- Include Java version and error message
- Provide example BASIC code if applicable

### Questions
- Check the troubleshooting section in USER_GUIDE.md
- Check [KNOWN_ISSUES.md](doc/KNOWN_ISSUES.md) before reporting a bug
- Review example programs
- Read BASIC_CODING_STANDARD.md

---

## 🎯 Roadmap

### Current Status (0.4.0, 2026-11-23)
In development (2026-11-23); open defects in `doc/KNOWN_ISSUES.md`  
✅ All Core Features Complete  
✅ Comprehensive Documentation  
✅ Full Test Coverage  

### Future (v0.4.0+)
- Java 25+ upgrade path
- Enhanced pattern matching
- Virtual thread support
- Potential GUI IDE
- Extended library functions

---

## 🙏 Acknowledgments

**Project Creator**: Andreas Grimm

**Based On**: 
- JASIC by Bob Nystrom
- Dartmouth BASIC specification (1964)

**Built With**:
- Java 21 JDK
- Apache Maven
- JUnit 5
- Apache Commons CLI
- SnakeYAML

---

**Last Updated:** 2026-11-22  
**Status:** In development (2026-11-23)  
**Test Pass Rate:** 1560/1560 unit tests (100%); 109/109 system test runs

---

## Quick Navigation

| Topic | File |
|-------|------|
| User Manual | [doc/USER_GUIDE.md](doc/USER_GUIDE.md) |
| Language Ref | [doc/BASIC.md](doc/BASIC.md) |
| Coding Standards | [doc/BASIC_CODING_STANDARD.md](doc/BASIC_CODING_STANDARD.md) |
| Architecture | [doc/GD-BASIC_Detailed_Design.md](doc/GD-BASIC_Detailed_Design.md) |
| Version History | [CHANGELOG.md](CHANGELOG.md) |
| Test Info | [test/system/SYSTEM_TEST_GUIDE.md](test/system/SYSTEM_TEST_GUIDE.md) (system tests); unit tests are in `src/test/java/` |
| Upgrade Plans | [doc/Java_25_Needed_Changes.md](doc/Java_25_Needed_Changes.md) |
| License | [LICENSE.md](LICENSE.md) |

---

*For detailed information on any topic, see the documentation links above.*