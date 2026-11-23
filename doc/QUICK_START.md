# GD-BASIC: Quick Start Guide

**For the impatient developer who wants to get started in 5 minutes.**

---

## ⚡ 5-Minute Setup

```bash
# Clone/Enter project
cd /Users/andreas/Projects/Sources/Java/GD-BASIC

# Build
mvn clean test package

# Run interactively (new!)
java -jar target/BASIC-*.jar

# Or run a program
java -jar target/BASIC-*.jar examples/hello.bas

# Run tests
mvn test
```

---

## 🎯 What is GD-BASIC?

A complete BASIC interpreter written in Java that:
- ✅ Executes BASIC programs (`.bas` files)
- ✅ Interactive line editor with LOAD/SAVE commands
- ✅ Supports multiple BASIC dialects (Dartmouth, TRS-80, PET, Apple)
- ✅ 40+ built-in functions (PRINT, INPUT, SIN, RND, etc.)
- ✅ Type system (Integer, Real, String, Long, Boolean, decimal BCD `#` in version 0.3.0 on branch `feature/bcd-arithmetic`)
- ✅ 1435 comprehensive unit tests (100% pass rate) and 62 system test programs
- ✅ Optional Java code generation

---

## 📊 Project Stats

```
Java Version:    21+
Maven:           3.6.3+
Files:           60+ Java source files
Main Code:       ~15,000 lines
Tests:           1435 unit (100% passing); 62 system programs
Status:          Production Ready
```

---

## 🚀 Common Commands

```bash
# Build & Test
mvn clean test package        # Build + tests + JAR
mvn test                      # Just tests
mvn test -Dtest=BasicTest     # Specific test

# Run Modes
java -jar BASIC-*.jar                      # Interactive editor
java -jar BASIC-*.jar prog.bas              # Run program
java -jar BASIC-*.jar -r prog.bas           # Direct run (no editor)
java -jar BASIC-*.jar -d prog.bas           # Dartmouth mode
java -jar BASIC-*.jar -v debug prog.bas     # Verbose logging
java -jar BASIC-*.jar -h                    # Help

# Reports
mvn site                      # Generate Checkstyle, PMD, JavaDoc
mvn javadoc:javadoc          # Generate JavaDoc
```

---

## 📚 Understanding the Pipeline

```
.bas File
   ↓
Macro Processing (replace includes)
   ↓
Tokenization (BasicLexer) → Tokens
   ↓
Parsing (BasicParser) → AST
   ↓
Execution (Execute) → Output
   ↓
Results
```

**Each stage is documented separately** in the doc/ directory.

---

## 📁 Project Structure

```
src/main/java/eu/gricom/basic/
├─ Basic.java              (Entry point)
├─ tokenizer/              (Lexical analysis)
│  ├─ BasicLexer.java
│  ├─ Token.java
│  └─ TokenType.java
├─ parser/                 (Syntax analysis)
│  ├─ BasicParser.java
│  └─ BasicListener.java
├─ statements/             (35+ statement types)
│  ├─ If.java
│  ├─ For.java
│  ├─ Print.java
│  └─ ...
├─ functions/              (40+ built-in functions)
│  ├─ Sin.java
│  ├─ Cos.java
│  ├─ Len.java
│  └─ ...
├─ variableTypes/          (Type system)
│  ├─ RealValue.java
│  ├─ IntegerValue.java
│  └─ ...
├─ memoryManager/          (State management)
│  ├─ Program.java
│  ├─ Stack.java
│  └─ VariableManagement.java
├─ runtimeManager/         (Execution engine)
│  └─ Execute.java
├─ lineEditor/             (Interactive editor - v0.2.0+)
│  └─ LineEditor.java
└─ helper/                 (Utilities)
   ├─ Logger.java
   ├─ FileHandler.java
   └─ Printer.java
```

---

## 🎮 Interactive Editor Commands

**NEW in v0.2.0+** - Run without a file:

```bash
java -jar BASIC-*.jar
> LIST          # Show program
> RUN           # Execute program
> LOAD file.bas # Load file
> SAVE file.bas # Save program
> DELETE n      # Delete line n
> NEW           # Clear program
> HELP          # Show help
> QUIT          # Exit
```

---

## 📝 Writing BASIC Programs

### Hello World
```basic
10 PRINT "Hello, World!"
20 END
```

### Variables (Multiple Types)
```basic
10 LET X = 3.14         ' Real number (default)
20 LET I% = 42          ' Integer
30 LET S$ = "Hello"     ' String
40 LET B@ = TRUE        ' Boolean
50 PRINT X, I%, S$, B@
```

### Loops
```basic
10 FOR I = 1 TO 10
20   PRINT I
30 NEXT I
40 END
```

### Conditionals
```basic
10 INPUT "Enter age: ", AGE
20 IF AGE >= 18 THEN
30   PRINT "Adult"
40 ELSE
50   PRINT "Minor"
60 END IF
```

### Functions
```basic
10 PRINT SIN(3.14159)   ' Math
20 PRINT LEN("Hello")   ' String
30 PRINT INT(3.7)       ' Conversion
40 PRINT RND()          ' Random
```

---

## 🐛 Common Issues & Solutions

### "Command not found: java"
```bash
# Install Java 21+
# macOS: brew install openjdk@21
# Linux: sudo apt install openjdk-21-jdk
# Or download from oracle.com
```

### "Class not found: Basic"
```bash
# Rebuild the JAR
mvn clean package
```

### "Program doesn't run"
```bash
# Check syntax (common BASIC mistakes)
# Run with verbose logging:
java -jar BASIC-*.jar -v debug program.bas

# Or in editor, use RUN to get error messages
```

### Tests fail
```bash
# Full rebuild
mvn clean test

# Run specific test
mvn test -Dtest=BasicParserTest#testIfStatement
```

---

## 📖 Going Deeper

| I Want To... | Read This |
|-------------|-----------|
| Understand overall design | `ARCHITECTURE.md` |
| Learn the pipeline | `01_PIPELINE_OVERVIEW.md` |
| Understand tokenization | `02_TOKENIZER.md` |
| Understand parsing | `03_PARSER.md` |
| Understand execution | `04_EXECUTION.md` |
| Add a statement | `05_STATEMENT_GUIDE.md` |
| Add a function | `06_FUNCTION_GUIDE.md` |
| Understand types | `07_VARIABLE_TYPES.md` |
| Detailed code explanation | `code-walkthroughs/CORE_CLASSES.md` |
| Fixing bugs | `MAINTENANCE_GUIDE.md` |
| Understanding tests | `TESTING_GUIDE.md` |

---

## 🎯 Development Workflow

### 1. Understand Current Code
```bash
# Read relevant documentation
cat doc/02_TOKENIZER.md      # For tokenizer work
cat doc/03_PARSER.md         # For parser work
cat code-walkthroughs/CORE_CLASSES.md
```

### 2. Write Tests First
```bash
# Add test to src/test/java/.../*Test.java
mvn test -Dtest=YourTestClass
```

### 3. Implement
```bash
# Add implementation to src/main/java/...
# Follow existing patterns
```

### 4. Verify
```bash
# Full test suite
mvn clean test

# Build
mvn package

# Manual test
java -jar target/BASIC-*.jar -r test_program.bas
```

---

## 💡 Pro Tips

1. **Read CLAUDE.md** - Project configuration and recent changes
2. **Check tests** - Best examples of how to use code
3. **Use verbose mode** - `java -jar BASIC-*.jar -v debug program.bas`
4. **Study existing statements** - All follow same pattern
5. **Keep tests passing** - 1435 tests are your safety net

---

## 🚀 Next Steps

1. **Build the project**: `mvn clean package`
2. **Run interactively**: `java -jar target/BASIC-*.jar`
3. **Pick a learning path** from INDEX.md
4. **Read relevant documentation** for your task
5. **Explore code walkthroughs** for detailed understanding

---

## 📞 Quick Reference

```bash
# Most Common
mvn clean test package              # Full build
java -jar target/BASIC-*.jar        # Run interactive
mvn test -Dtest=BasicParserTest     # Specific test

# For Documentation
cat /Users/andreas/JottaCloud/Notes/Projects/GD-Basic/doc/INDEX.md

# For Refresh
/Users/andreas/JottaCloud/Notes/Projects/GD-Basic/scripts/refresh-docs.sh
```

---

**Version**: 0.3.0  
**Updated: 2026-08-31
**Status**: Production Ready ✓

