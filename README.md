![CodeRabbit Pull Request Reviews](https://img.shields.io/coderabbit/prs/github/andreas-grimm/GD-BASIC?utm_source=oss&utm_medium=github&utm_campaign=andreas-grimm%2FGD-BASIC&labelColor=171717&color=FF570A&link=https%3A%2F%2Fcoderabbit.ai&label=CodeRabbit+Reviews)

# GriCom Diminutive BASIC Interpreter (GD-BASIC)

&copy; 2020 - 2026 Andreas Grimm | Use according to the included licence file ([LICENSE.md](LICENSE.md))

**Version:** 0.2.1  
**Status:** Production Ready  
**Last Updated:** 2026-08-24

---

## 📚 Project Overview

**GD-BASIC** is a Java 21+ implementation of a Dartmouth-style BASIC interpreter. It can execute `.bas` programs and supports interactive line editing.

### Key Features

- ✅ **Interactive Line Editor** — Build and test programs without file setup
- ✅ **File I/O** — LOAD, SAVE, DELETE commands in the editor
- ✅ **Comprehensive BASIC Support** — 40+ built-in functions, 35+ statements
- ✅ **Computed Branches** — ON GOTO and ON GOSUB for dynamic control flow
- ✅ **Type Safety** — Strongly-typed variable system with 6 types
- ✅ **Advanced Features** — Arrays, loops, conditionals, file operations
- ✅ **Production Ready** — 1248/1248 tests passing, zero failures
- ✅ **Well Documented** — User guides, architecture docs, code examples

---

## 🚀 Quick Start

### Installation

```bash
# Requirements: Java 21+
java -version  # Should show Java 21 or later

# Download the JAR
# File: BASIC-0.2.1-jar-with-dependencies.jar
```

### Run Interactive Mode (No File Required)

```bash
java -jar BASIC-0.2.1-jar-with-dependencies.jar
>10 PRINT "HELLO WORLD"
>20 END
>RUN
HELLO WORLD
>HELP
```

### Load and Modify a Program

```bash
java -jar BASIC-0.2.1-jar-with-dependencies.jar existing.bas
>LIST
>DELETE 15
>30 PRINT "Modified"
>SAVE output.bas
```

### Direct Execution (No Editor)

```bash
java -jar BASIC-0.2.1-jar-with-dependencies.jar -r -q program.bas
```

---

## 📖 Documentation

### For Users
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
  - All releases from v0.0.1 to v0.2.0+
  - Features by version
  - Test results
  - Bug fixes and improvements

- **[TEST_SUMMARY.md](TEST_SUMMARY.md)** — Test documentation
  - Test coverage analysis
  - New tests in 0.2.0+
  - Test execution instructions
  - Quality metrics

---

## 📋 Current Version (0.2.0+ Extended)

### Latest Features (July 26, 2026)

**Interactive Line Editor Enhancements**:
- Start without a program file
- LOAD command to import programs
- SAVE command with overwrite protection
- DELETE command for line removal
- HELP command with documentation
- Program validation before execution

**New Exception Classes**:
- `EmptyProgramException` — File loading validation
- `FileAlreadyExistsException` — File save protection

**Test Coverage**:
- 1214/1214 unit tests passing ✅
- 34/34 system integration tests passing ✅
- 100% pass rate with zero failures

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
Tests: 1214/1214 pass ✅
Build: SUCCESS
Quality: Zero violations (Checkstyle, PMD)
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
- **Statements** (35+) — Statement implementations
- **Functions** (40+) — Built-in functions
- **Variables** (6 types) — Type system
- **Memory** (`VariableManagement`) — Variable storage
- **File I/O** (`FileManager`) — File operations
- **Line Editor** (`LineEditor`) — Interactive mode

### Type System
- **Untyped** (default real) — `X`, `value`
- **Integer** — `count%`, `index%`
- **Long** — `bignum&`, `largeint&`
- **Double** — `result!`, `sum!`
- **String** — `name$`, `text$`
- **Boolean** — `flag@`, `condition@`

---

## 📊 Statistics

- **1214/1214 tests** pass ✅
- **35+ statements** implemented
- **40+ functions** available
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
│   ├── GD-BASIC_Detailed_Design.md .... Architecture
│   └── Java_25_Needed_Changes.md ...... Future roadmap
│
├── src/
│   ├── main/java/eu/gricom/basic/ .... Source code
│   │   ├── tokenizer/ ................. Lexer
│   │   ├── parser/ .................... Parser
│   │   ├── statements/ ................ Statements (35+)
│   │   ├── functions/ ................. Functions (40+)
│   │   ├── variableTypes/ ............. Type system
│   │   ├── memoryManager/ ............. Memory & storage
│   │   ├── lineEditor/ ................ Interactive editor
│   │   ├── runtimeManager/ ............ Execution engine
│   │   ├── helper/ .................... Utilities
│   │   └── error/ ..................... Exceptions
│   │
│   ├── main/resources/
│   │   ├── application.yaml ........... Configuration
│   │   └── help.txt ................... Editor help
│   │
│   └── test/java/eu/gricom/basic/ .... Tests (1214 tests)
│
├── target/
│   ├── BASIC-0.2.0.jar ................ Standard JAR
│   └── BASIC-0.2.1-jar-with-dependencies.jar .. Executable JAR
│
└── test/system/ ....................... System tests (34+)
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
- Check FAQ in USER_GUIDE.md
- Review example programs
- Read BASIC_CODING_STANDARD.md

---

## 🎯 Roadmap

### Current Status (v0.2.0+)
✅ Production Ready  
✅ All Core Features Complete  
✅ Comprehensive Documentation  
✅ Full Test Coverage  

### Future (v0.3.0+)
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

**Last Updated:** 2026-07-26 10:40 UTC  
**Status:** ✅ Production Ready  
**Test Pass Rate:** 1214/1214 (100%)

---

## Quick Navigation

| Topic | File |
|-------|------|
| User Manual | [doc/USER_GUIDE.md](doc/USER_GUIDE.md) |
| Language Ref | [doc/BASIC.md](doc/BASIC.md) |
| Coding Standards | [doc/BASIC_CODING_STANDARD.md](doc/BASIC_CODING_STANDARD.md) |
| Architecture | [doc/GD-BASIC_Detailed_Design.md](doc/GD-BASIC_Detailed_Design.md) |
| Version History | [CHANGELOG.md](CHANGELOG.md) |
| Test Info | [TEST_SUMMARY.md](TEST_SUMMARY.md) |
| Upgrade Plans | [doc/Java_25_Needed_Changes.md](doc/Java_25_Needed_Changes.md) |
| License | [LICENSE.md](LICENSE.md) |

---

*For detailed information on any topic, see the documentation links above.*