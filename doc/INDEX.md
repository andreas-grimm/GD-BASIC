# GD-BASIC: Complete Project Documentation Index

**Welcome to GD-BASIC Documentation!**

GD-BASIC (GriCom Diminutive BASIC Interpreter) is a full-featured Java 21 implementation of a Dartmouth-style BASIC interpreter with interactive line editing, 1248+ test cases, and optional Java code generation.

---

## 🚀 Quick Start (5 Minutes)

```bash
# Build the project
cd /Users/andreas/Projects/Sources/Java/GD-BASIC
mvn clean test package

# Run interactively
java -jar target/BASIC-*.jar

# Run a program
java -jar target/BASIC-*.jar program.bas
```

**Status**: Production Ready (1248/1248 tests passing)  
**Version**: 0.2.1  
**Java Version**: 21+  
**License**: See LICENSE.md

---

## 📚 Documentation Structure

### Entry Points

| Document | Purpose | Time |
|----------|---------|------|
| **QUICK_START.md** | 5-minute quick reference | 5 min |
| **ARCHITECTURE.md** | System design overview | 30 min |

### Learning Path

| Document | Purpose | Time |
|----------|---------|------|
| **01_PIPELINE_OVERVIEW.md** | Tokenization → Parsing → Execution | 45 min |
| **02_TOKENIZER.md** | Lexical analysis and token types | 30 min |
| **03_PARSER.md** | Recursive descent parser explained | 45 min |
| **04_EXECUTION.md** | Runtime execution engine | 40 min |
| **05_STATEMENT_GUIDE.md** | Statement implementations | 30 min |
| **06_FUNCTION_GUIDE.md** | Built-in functions (40+) | 35 min |
| **07_VARIABLE_TYPES.md** | Type system and memory | 25 min |
| **08_LINE_EDITOR.md** | Interactive editor (NEW in v0.2.0) | 20 min |

### Code Walkthroughs

**Detailed code analysis located in: `../code-walkthroughs/`**

- `CORE_CLASSES.md` - Basic.java, Token, BasicLexer
- `PARSER_WALKTHROUGH.md` - BasicParser in detail
- `STATEMENT_WALKTHROUGH.md` - Common statements (IF, FOR, PRINT)
- `FUNCTION_WALKTHROUGH.md` - Function implementations

### Reference & Maintenance

| Document | Purpose |
|----------|---------|
| **REFERENCE.md** | Quick lookup tables, commands, glossary |
| **TESTING_GUIDE.md** | Test structure and patterns (1248 tests) |
| **MAINTENANCE_GUIDE.md** | Building, debugging, common issues |
| **CHANGELOG_SUMMARY.md** | Version history highlights |

---

## 🎯 Choose Your Learning Path

### 👶 **New Developer - First Time**

**Goal**: Understand the project basics  
**Time**: ~3 hours  

1. Read: `QUICK_START.md` (5 min)
2. Read: `ARCHITECTURE.md` (30 min)
3. Read: `01_PIPELINE_OVERVIEW.md` (45 min)
4. Skim: `02_TOKENIZER.md` (20 min)
5. Skim: `03_PARSER.md` (20 min)
6. Explore: Code walkthroughs (30 min)

**Result**: Understand overall flow and key concepts

---

### 🔍 **Code Understanding - Reading Code**

**Goal**: Understand how code works  
**Time**: ~4 hours  

1. Read: `01_PIPELINE_OVERVIEW.md` (45 min)
2. Read: `02_TOKENIZER.md` (30 min)
3. Read: `03_PARSER.md` (45 min)
4. Read: `../code-walkthroughs/CORE_CLASSES.md` (30 min)
5. Read: `../code-walkthroughs/PARSER_WALKTHROUGH.md` (30 min)

**Result**: Understand code structure and design

---

### 🐛 **Bug Fixing & Maintenance**

**Goal**: Know how to fix issues  
**Time**: ~2 hours  

1. Read: `MAINTENANCE_GUIDE.md` (45 min)
2. Read: `TESTING_GUIDE.md` (30 min)
3. Reference: `REFERENCE.md` (as needed)
4. Explore: Relevant code walkthroughs (30 min)

**Result**: Can debug and fix issues

---

### ➕ **Adding Features**

**Goal**: Extend the interpreter  
**Time**: ~5 hours  

1. Read: `01_PIPELINE_OVERVIEW.md` (45 min)
2. Read: `05_STATEMENT_GUIDE.md` or `06_FUNCTION_GUIDE.md` (30 min)
3. Read: Relevant code walkthroughs (60 min)
4. Study: Similar existing implementation (30 min)
5. Hands-on: Write tests then implement (120 min)

**Result**: Can add statements or functions

---

### 🏗️ **Architecture Deep Dive**

**Goal**: Understand system design  
**Time**: ~6 hours  

1. Read: `ARCHITECTURE.md` (30 min)
2. Read: `01_PIPELINE_OVERVIEW.md` (45 min)
3. Read: All pipeline docs `02_*.md` through `08_*.md` (2.5 hours)
4. Read: All code walkthroughs (1.5 hours)

**Result**: Complete understanding of system design

---

## 📖 Documentation by Topic

### Understanding the Pipeline

```
Tokenization  →  Parsing  →  Execution
├─ 02_TOKENIZER.md
├─ 03_PARSER.md
└─ 04_EXECUTION.md
```

### Language Features

```
Statements       Functions       Variables
├─ 05_STATEMENT  ├─ 06_FUNCTION  └─ 07_VARIABLE_TYPES
└─ code-walk...  └─ code-walk...
```

### Interactive Features

```
Line Editor
└─ 08_LINE_EDITOR.md
```

### Working with Code

```
Maintenance      Testing         Reference
├─ Debugging     ├─ 1248 tests    └─ Quick lookup
└─ Building      └─ Patterns      └─ Glossary
```

---

## 🔍 Finding Information

### "I want to understand..."

| Topic | Read This | Time |
|-------|-----------|------|
| Overall system | ARCHITECTURE.md | 30 min |
| How tokenization works | 02_TOKENIZER.md | 30 min |
| How parsing works | 03_PARSER.md | 45 min |
| How execution works | 04_EXECUTION.md | 40 min |
| Statement implementation | code-walk/STATEMENT_WALKTHROUGH.md | 30 min |
| Function implementation | code-walk/FUNCTION_WALKTHROUGH.md | 30 min |
| Type system | 07_VARIABLE_TYPES.md | 25 min |
| Interactive editor | 08_LINE_EDITOR.md | 20 min |
| Code quality | TESTING_GUIDE.md | 30 min |

### "I need to..."

| Task | Read This | Time |
|------|-----------|------|
| Build locally | MAINTENANCE_GUIDE.md → Building | 10 min |
| Run tests | MAINTENANCE_GUIDE.md → Testing | 10 min |
| Fix a bug | MAINTENANCE_GUIDE.md → Debugging | 30 min |
| Add a statement | 05_STATEMENT_GUIDE.md + code-walk | 60 min |
| Add a function | 06_FUNCTION_GUIDE.md + code-walk | 60 min |
| Understand a test | TESTING_GUIDE.md | 30 min |
| Find a command | REFERENCE.md → Commands | 5 min |

---

## 📊 Project Statistics

```
Source Files:          60+ Java files
Main Code:             ~15,000 lines
Test Code:             ~8,000+ lines (1248 tests)
Test Coverage:         100% pass rate (1248/1248)

Key Components:
├─ Tokenizer           BasicLexer, Token, TokenType
├─ Parser              BasicParser (recursive descent)
├─ Statements          35+ implementations (IF, FOR, PRINT, etc.)
├─ Functions           40+ built-in functions
├─ Variable Types      6 types (Real, Integer, String, etc.)
├─ Memory Management   Program, Stack, Variables
├─ Execution Engine    Execute, ProgramPointer
├─ Line Editor         Interactive mode (v0.2.0+)
└─ Code Generator      Compiles to Java

Supported Dialects:
├─ Dartmouth BASIC (standard)
├─ TRS-80 BASIC
├─ PET BASIC
└─ Apple BASIC
```

---

## 🗺️ File Structure

```
/Users/andreas/JottaCloud/Notes/Projects/GD-Basic/

doc/                           (Documentation)
├─ INDEX.md                    ← You are here
├─ QUICK_START.md              (5-min quick reference)
├─ ARCHITECTURE.md             (System design)
├─ 01_PIPELINE_OVERVIEW.md     (Complete flow)
├─ 02_TOKENIZER.md             (Lexical analysis)
├─ 03_PARSER.md                (Syntax analysis)
├─ 04_EXECUTION.md             (Runtime)
├─ 05_STATEMENT_GUIDE.md       (IF, FOR, PRINT, etc.)
├─ 06_FUNCTION_GUIDE.md        (40+ built-ins)
├─ 07_VARIABLE_TYPES.md        (Type system)
├─ 08_LINE_EDITOR.md           (Interactive mode)
├─ REFERENCE.md                (Quick lookup)
├─ TESTING_GUIDE.md            (1248 tests)
├─ MAINTENANCE_GUIDE.md        (Build & debug)
└─ CHANGELOG_SUMMARY.md        (Version history)

code-walkthroughs/             (Detailed code analysis)
├─ CORE_CLASSES.md             (Basic.java, Token, Lexer)
├─ PARSER_WALKTHROUGH.md       (BasicParser details)
├─ STATEMENT_WALKTHROUGH.md    (Statement examples)
└─ FUNCTION_WALKTHROUGH.md     (Function examples)

scripts/                       (Automation)
└─ refresh-docs.sh             (Auto-generate docs)

README.md                      (This directory's guide)
```

---

## ✨ Documentation Features

✅ **Comprehensive** - 60+ Java files documented  
✅ **Structured** - Multiple learning paths for different roles  
✅ **Visual** - Diagrams and flowcharts  
✅ **Practical** - Real code examples from the project  
✅ **Beginner-Friendly** - Written for junior developers  
✅ **Code Walkthroughs** - Detailed line-by-line analysis  
✅ **Reference Material** - Quick lookup tables and glossary  
✅ **Actionable** - Clear steps for common tasks  

---

## 🚀 Next Steps

### 1. Choose Your Role
- New Developer → **Start with QUICK_START.md**
- Reading Code → **Go to 01_PIPELINE_OVERVIEW.md**
- Fixing Bugs → **Go to MAINTENANCE_GUIDE.md**
- Adding Features → **Go to 05_STATEMENT_GUIDE.md**

### 2. Follow Your Path
Each path is designed to take you from basics to mastery

### 3. Explore Code
Use code walkthroughs to understand real implementations

### 4. Practice
Build, test, and experiment with the project

---

## 📝 Documentation Maintenance

These documents are generated from the project source.  
Use the refresh script to update documentation:

```bash
/Users/andreas/JottaCloud/Notes/Projects/GD-Basic/scripts/refresh-docs.sh
```

**Last Generated: 2026-08-31 09:55:10
**Version**: 1.0  
**Status**: Current ✓

---

## 💡 Quick Reference

```bash
# Build
mvn clean test package

# Test
mvn test
mvn test -Dtest=BasicParserTest

# Run
java -jar target/BASIC-*.jar                    # Interactive
java -jar target/BASIC-*.jar program.bas        # Run program
java -jar target/BASIC-*.jar -r program.bas     # Direct run

# Documentation
cat /Users/andreas/JottaCloud/Notes/Projects/GD-Basic/doc/QUICK_START.md
/Users/andreas/JottaCloud/Notes/Projects/GD-Basic/scripts/refresh-docs.sh
```

---

## 🤝 Contributing

1. **Read**: Choose your learning path above
2. **Understand**: Study relevant documentation + code walkthroughs
3. **Code**: Make your changes with tests
4. **Test**: Run full test suite (`mvn test`)
5. **Document**: Update docs if you change functionality

---

**Start here**: Pick your role above and read the recommended first document!

