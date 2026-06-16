![CodeRabbit Pull Request Reviews](https://img.shields.io/coderabbit/prs/github/andreas-grimm/GD-BASIC?utm_source=oss&utm_medium=github&utm_campaign=andreas-grimm%2FGD-BASIC&labelColor=171717&color=FF570A&link=https%3A%2F%2Fcoderabbit.ai&label=CodeRabbit+Reviews)


This is the ReadMe File of the Project:

# GriCom Diminutive BASIC Interpreter (GDBI)

&copy; 2020 - Andreas Grimm, Use according to the included licence file (LICENSE.md)

---
References

Based on the [JASIC project of Bob Nystrom](https://github.com/munificent/jasic)

The Visual Studio Code (VSC) extension in  /etc/VSCExtension is based on the [Atari VSC Extension of Marcin 
Jozwikowski](https://github.com/marcin-jozwikowski/atari-basic-vsc-extension)

Both projects provided the logical starting point, the actual software artefacts are not copies of the original 
projects. 

---

Project Planning and Control

Build Process:

This package has been tested to build with Maven 3.6.3+, using Java 21+ under macOS

Use the following command line:

    mvn clean test package

--- 

The current version contains the following changes, bug fixes, and enhancements:

0.0.1:
* Setup and project re-factor the original project by Bob Nystrom, containing:
* "Mavenizing" the project: Using Apache Maven as the build processor, also allowing a CI/CD development process
* Adding Checkstyle and PMD static code testing

0.0.2:
* Adding tooling to the existing code:
* Command Line - Using Apache Commons CLI to process the input parameter
* Logger - lightweight logger to avoid heavy-weight log4j
* Adding JUnit Test Cases for a majority of the functions
* Adding regression testing with provided BASIC programs 

Implemented Backlog Items: 

    [BASIC-16], [BASIC-7], [BASIC-11], [BASIC-12], [BASIC-14], [BASIC-17]

0.0.3:
* Leaving JASIC as it is and starting the BASIC interpreter functionality
* Changed variable management to new typed variables: string, integer, real, and boolean 
  * Moving math functions `+`,`*`, `-`, `/`, `=`, `<`, and `>` into the type classes
  * Added JUnit test classes  
* Changed memory management: Converting the MemoryManage class into a package with dedicated classes  
* Introduction of new command: `END`
* Bug fixes:
  * `[BASIC-28]`: Correcting the design and implementation of the `LabeLStatement` class

Implemented Backlog Items: 

    [BASIC-4], [BASIC-23], [BASIC-8], [BASIC-38], [BASIC-25], [BASIC-6], [BASIC-39], [BASIC-9], [BASIC-28]

0.0.4:
* Starting to build the interpreter to support Dartmouth style BASIC
  * Tokenizer and Parser re-build:
    * Moving from character based tokenization to line based tokenization
    * General code cleansing
    * Added JUnit test cases for new classes
  * Implemented commands:
    * All JASIC commands are now implemented in the BASIC branch
    * Added functionality to the `IF` command: using block structure to allow more commands after an `IF` command (BASIC branch only)  
    * `PRINT` - general output command
    * `REM` for any remarks. This is now not only ignored but passed to the parser.
* Introducing variable naming to typed variables: string type: `variable$`, integer type: `variable%`, real type: `variable#`, and boolean type: `variable!`
* Added additional math operators: `^` (power of), `!=`, `<=`, and `>=`; replaced `=` in comparisons by `==`
* Added JUNIT tests for all new functions

Implemented Backlog Items: 

    [BASIC-15], [BASIC-10], [BASIC-47], [BASIC-40], [BASIC-56], [BASIC-21], [BASIC-20], [BASIC-22], [BASIC-32], [BASIC-33],
    [BASIC-34], [BASIC-35], [BASIC-18], [BASIC-29], [BASIC-30], [BASIC-31], [BASIC-44], [BASIC-45], [BASIC-46], [BASIC-59], 
    [BASIC-60], [BASIC-61], [BASIC-63]

0.0.5: Tagged as Loop Release
* Added the `FOR-NEXT`-loop
* Added the `DO-UNTIL`-loop  
* Added `GOSUB`-statement
* `PRINT` with a trailing semicolon `;` surpresses LF at the end of the output. Also using a comma `,` in the argument list to
  print multiple outputs in a single command.
* Added variable naming to typed variables: long type: `variable&`

Implemented Backlog Items: 

    [BASIC-24], [BASIC-26], [BASIC-43], [BASIC-48], [BASIC-58], [BASIC-62], [BASIC-66], [BASIC-65], [BASIC-74],
    [BASIC-80], [BASIC-67], [BASIC-68], [BASIC-69], [BASIC-70], [BASIC-71], [BASIC-72], [BASIC-81], [BASIC-84]

0.0.6: Tagged as Array and Function Release
* Removed the `-i` mandatory parameter in the command line
* Removed the JASIC functionality  
* Added the `@PRAGMA` feature. At this moment it allows to change the setting of the log level, but it can also be 
  used to change any other execute parameter of the interpreter.
* Added the colon `:` programming feature to the interpreter, which allows multiple commands in a single line.  
* Added array function to all data types. The array function allows the use of n-dimensional arrays. The index of the 
  array can be one or more variables itself. The documentation is adjusted to it.
* Adding functionality to retrieve single characters out of any string. A string's characters can be retrieved by 
  using squared brackets:
  `[` and `]`.
* Added mathematical operators: `>>`, `<<`, `%`, `AND`, and `OR`  
* Added mathematical functions: `ABS`, `ATN`, `CDBL`, `CINT`, `COS`, `EXP`, `LOG`, `LOG10`, `NOT`, `RND`, `SIN`, `SQR`, 
  `TAN`
* Added string functions: `ASC`, `CHR`, `INSTR`, `LEFT`, `LEN`, `MID`, `RIGHT`, `STR`, `VAL`
* Added system functions: `MEM`, `SYSTEM`, `TIME`
* Enhanced build system with improved Maven configuration
* Added `READ` and `DATA` statements
* Fixed issues:
1. `FOR` statement only accepted fixed numbers for start-, end-, and step- value. This has changed that now 
   variables, arrays, and functions can be used for those parameters.
2. `IF` statement is now able to use a direct jump target after the `THEN` keyword. This is a reaction to problems 
   using a `GOTO` out of the block, leaving an unresolved stack entry back. To allow a developer to jump out of any 
   block (`DO`,`FOR`, `IF`, and `WHILE`) a new statement `CLEAN` is planned that removes the un-used stack entry. 
   The BASIC programming guide will be added to discuss best practices concerning the `GOTO` command: with the 
   existing commands, the use of the `GOTO` command should be avoided.
3. `IF` statement is now supporting the `ELSE` extension, generating a command block for the alternate flow processing.

Implemented Backlog Items:

    [BASIC-19][BASIC-37][BASIC-53][BASIC-64][BASIC-73][BASIC-89][BASIC-102][BASIC-104][BASIC-105][BASIC-108][BASIC-109]
    [BASIC-110][BASIC-117][BASIC-120][BASIC-127]

0.0.7: Tagged as Macro Release
* Verified that the JASIC code has been completely removed from the project
* Added the framework to handle macro constructs (`DEF FN`)
* Added the code highlighting for [Visual Studio Code (VSC)](https://code.visualstudio.com/) 

0.0.8: File Handling Release
* Added file interface to allow the interpreter to work with external files
* Change the build setting to allow compilation on Debian and Raspberry PI 4 under 64 bit Ubuntu
* Added directories to allow C and GO compiler modules

Implemented Backlog Items:

    [BASIC-55][BASIC-121][BASIC-133]

Under Development:

    [BASIC-48][BASIC-76][BASIC-79][BASIC-82][BASIC-83][BASIC-88][BASIC-115][BASIC-124]

0.1.0: Stable Release for Testing
* Added file functions to the BASIC interpreter
* Added `CALL` function to call external RESTful APIs
* Added system tests to the project
* Removed GRADLE build system
* Updated the documentation to reflect the new features
* Replaced third party graphs in the documentation with `mermaid` diagrams

0.1.1: Block IF & Array Support Release (May 24, 2026)
* **Block IF Statement Support**: Multi-line IF-THEN-ELSE-END-IF block structures fully implemented and tested
  - Parser now properly distinguishes between single-line IF, inline IF, and block IF
  - Block statements are collected and executed directly instead of relying on line-number jumping
  - Full support for nested IF blocks and mixed control structures
* **Multi-Dimensional Array Parsing**: Fixed tokenizer/normalizer to properly handle array subscripts
  - Improved Normalizer to add spaces after delimiters (commas, semicolons, colons)
  - Multi-dimensional arrays like `matrix%(1,2)` now parse and execute correctly
  - Array subscripts with expressions fully supported
* **Automatic Operator Spacing Normalization** (NEW)
  - Normalizer automatically normalizes spacing around operators inside parentheses
  - Accepts flexible spacing: `A$(X%+1)`, `A$(X% + 1)`, and `A$(X% +1)` all work
  - Preserves multi-character operators: `>=`, `<=`, `!=`, `<<`, `>>`
  - Correctly handles unary operators: `-5` stays as negative literal
  - 28 comprehensive unit tests added for Normalizer functionality
* **READ Statement Enhancement**: Updated READ to support array subscripts
  - Statements like `READ A$(I%)` now parse correctly
  - Works with both simple variables and array elements
  - Properly reconstructs array variable references from tokenized input
* **Parser Refactoring**: Major code quality improvements
  - Extracted 9 statement parsing methods from main parser switch statement
  - Eliminated 200+ lines of code duplication
  - Created reusable statement parsing methods for both main loop and block contexts
  - Methods: parsePrintStatement, parseReadStatement, parseInputStatement, parseGotoStatement, parseGosubStatement, parseReturnStatement, parseWordStatement, parseForLoop, parseWhileLoop
* **Test Results (May 24)**: 100% test success rate
  - Unit Tests: 881/881 pass ✅ (28 new Normalizer tests)
  - System Integration Tests: 34/34 pass ✅
  - BASIC Test Programs: 21/21 pass ✅

0.1.1 (Extended): Complete Parser Test Coverage (May 30, 2026)
* **CHDIR Statement Implementation**: Full directory change support
  - ChDirStatement class implementing Statement interface
  - Proper line number capture and FileManager integration
  - Directory path validation and error handling
  - Complete test coverage with statement sequencing verification
* **DIREXISTS Function Enhancement**: Complete directory existence checking
  - Integrated into single-parameter function parsing
  - Returns BooleanValue (true only for valid directories)
  - Multiple test cases covering string literals and variables
  - Token type classification verification
* **Parser Atomic Method - Complete Test Coverage** (NEW - May 30, 2026)
  - **All 35 Previously Untested Functions Now Have Unit Tests**:
    - 4 Zero-parameter functions: GETCWD, MEM, RND, TIME
    - 9 Math functions: ABS, SIN, COS, TAN, LOG, LOG10, EXP, SQR, ATN
    - 6 Conversion functions: CHR, ASC, VAL, STR, CINT, CDBL
    - 7 File functions: EOF, FEXISTS, FGETNAME, FGETSIZE, FISOPEN, FLINECOUNT, FMODTIME
    - 2 Utility functions: LEN, NOT
    - 6 Two-parameter functions: INSTR, LEFT, RIGHT, FCOMPARE, SYSTEM, CALL
    - 2 Three-parameter functions: MID, LISTDIR
  - Each function tested for: token recognition, Function object creation, proper type handling
  - 35 comprehensive test BASIC programs created
  - 35 JUnit test methods added to BasicParserTest.java
* **Test Results (May 30)**: Complete success
  - Total Unit Tests: 941/941 pass ✅ (+60 new function tests)
  - System Integration Tests: 34/34 pass ✅
  - BASIC Test Programs: 21/21 pass ✅
  - Build time: ~16-22 seconds
  - 0 failures, 0 errors

---
Implemented test and demonstration programs, located at `src/test/basic/`:
- `fibonacci.bas`: translation of the ECMA demonstration `FIBONACCI.BAS` program
- `fibonacci_array.bas`: Fibonacci using array storage
- Array test programs: `test_array*.bas` testing array functionality
- Parser test programs: `test_basic_*.bas` testing various language features
- Directory operation tests: `test_chdir_statement.bas`, `test_direxists_atomic.bas`
- Function parsing tests: 35 comprehensive function test programs

Test Coverage Summary:
- **941 unit tests** covering all core functionality
  - 28 Normalizer tests (spacing normalization)
  - 35 Parser atomic function tests (all uncovered functions)
  - 6 CHDIR and DIREXISTS tests
  - 850+ additional unit tests for core features
- **34 system integration tests** covering all BASIC language features
- **21 BASIC test programs** exercising real-world code patterns

Test Categories:
- Parser: 51+ tests (IF, array, CHDIR, DIREXISTS, atomic functions)
- Functions: 350+ tests (math, string, file operations, system)
- Statements: 200+ tests (control flow, loops, I/O)
- Type System: 100+ tests (RealValue, IntegerValue, StringValue, etc.)
- File Operations: 150+ tests (FOPEN, FCLOSE, FGET, file functions)
- Tokenizer & Normalizer: 100+ tests (lexical analysis, spacing)

** NOTE: as of this version, all further versions pass the CheckStyle test and have complete BasicParser.atomic() coverage **

0.1.1 (Extended): EOF Function Type Correction (May 30, 2026)
* **EOF Function Type Fix**: Corrected return type from IntegerValue to BooleanValue
  - EOF now returns boolean true/false instead of integer 1/0
  - Enables direct use in IF statements without type casting
  - Example: `IF EOF(1) THEN PRINT "End of file"` now works correctly
  - All 902 unit tests pass ✅
  - All 34 system integration tests pass ✅
* **Documentation Updates**:
  - Updated BASIC.md EOF function documentation
  - Clarified EOF flag behavior: set to true only when reading past EOF
  - Added boolean return type specification
* **Test Results (May 30)**: Complete success
  - Unit Tests: 902/902 pass ✅
  - System Integration Tests: 34/34 pass ✅
  - Build successful with Java 21 compilation

---

## 0.2.0: Type System Simplification - Removal of # Suffix (June 16, 2026)

⚠️ **BREAKING CHANGE**: This version removes the `#` suffix for real variables.

### What Changed
* **Removed `#` suffix for real variables** - No longer supported (syntax error if used)
* **Untyped variables default to REAL** - Variables with no suffix now store real (double) values
* **Cleaner type system** - Simplified from 7 type suffixes to 6
* **Freed `#` symbol** - Available for future use (e.g., comments, operators)

### Migration Required
Programs using `#` suffix must be updated:
- `X# = 3.14` → `X = 3.14` (untyped real)
- `a# = a# + 1` → `a = a + 1` (simple arithmetic)
- `PRINT x#` → `PRINT x` (variable reference)

See BASIC_CODING_STANDARD.md for complete migration guide.

### Implementation Details
* **VariableManagement.java**: Redesigned type detection and storage routing
  - Removed `_moUntyped` HashMap (untyped now stored in `_moReals`)
  - Added `_hasTypeSuffix()` helper method for suffix detection
  - Explicit validation to reject `#` with clear error messages
* **Test Updates**:
  - All Java unit tests updated (79 tests in Phase 1-3)
  - All BASIC system tests updated (34/34 now pass)
  - Added 7 new unit tests for hash suffix rejection
* **Documentation**:
  - Updated BASIC_CODING_STANDARD.md with migration guide
  - Updated CLAUDE.md variable types specification
  - Updated README.md (this file) with breaking change notice

### Test Results (June 16, 2026)
- Unit Tests: 908/908 pass ✅ (no failures, 1 skipped)
- System Integration Tests: 34/34 pass ✅
- Build successful with Java 21
- Zero regressions in existing functionality

### Error Handling
Clear error messages guide users to correct syntax:
```
Syntax Error: Variable name [X#] uses unsupported '#' suffix. 
Use untyped (no suffix) or '!' for real numbers.
```

### Valid Variable Types in 0.2.0
- **Untyped (real default)**: `X`, `pi`, `value`
- **Double**: `result!`, `sum!`
- **Integer**: `count%`, `index%`
- **String**: `name$`, `text$`
- **Long**: `bignum&`, `largeint&`
- **Boolean**: `flag@`, `condition@`
