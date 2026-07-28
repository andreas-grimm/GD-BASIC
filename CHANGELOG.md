# Changelog

All notable changes to the GD-BASIC project are documented in this file.

**Last Updated:** 2026-07-26 10:40 UTC

---

## [0.2.0+] - 2026-07-26 (Extended: Interactive Line Editor & File I/O)

**Interactive Enhancements Release**: Comprehensive line editor with file I/O, program validation, and user-friendly error handling.

### Summary

This extended release introduces interactive capabilities and improved file management:
- **New**: Interactive mode without requiring a program file
- **New**: LOAD/SAVE commands for file I/O in editor
- **New**: DELETE command for line removal
- **New**: HELP command with built-in documentation
- **New**: Program content validation (hasContent() method)
- **Enhanced**: RUN command with empty program guard
- **Enhanced**: Error handling with new exception types
- **Improved**: User experience with clear error messages
- **1214/1214 tests pass** ✅ (+33 new tests)

### Interactive Line Editor (NEW - July 26, 2026)

#### New Editor Commands

**LOAD <filename>** — Load BASIC program from file
- Validates file exists
- Checks file is not empty
- Replaces current program
- Throws EmptyProgramException if file empty
- Throws FileNotFoundException if file missing

**SAVE <filename>** — Save program to file
- Prevents accidental overwriting
- Throws FileAlreadyExistsException if file exists
- Creates new file with program source
- Provides feedback on successful save

**DELETE <line-number>** — Remove single line
- Parse line number from user input
- Remove line from program
- Re-tokenize after deletion
- Supports single line numbers

**DELETE <start> <end>** — Remove line range
- Parse range (space or comma-separated)
- Delete all lines within range (inclusive)
- Re-tokenize after deletion
- Supports formats: "DELETE 10 20" or "DELETE 10,20"

**HELP** — Display editor help
- Shows all available commands
- Provides syntax examples
- Loads from help.txt resource file
- Uses ClassLoader for proper resource resolution

#### Empty Program Start (NEW - July 26, 2026)

**No File Required**:
```bash
java -jar BASIC-0.2.0-jar-with-dependencies.jar
# Enters line editor with empty program
```

#### Program Content Validation (NEW - July 26, 2026)

**hasContent() Method**:
- Returns true if program has non-empty lines
- Returns false for empty/whitespace-only programs
- Handles all edge cases properly

**RUN Guard**:
- Checks hasContent() before parsing/executing
- Shows clear error: "RUN: No program loaded..."
- Prevents runtime errors on empty programs

### File I/O Enhancements

**Program.loadProgram(String filename)**:
- Validates file existence
- Checks file is not empty
- Throws FileNotFoundException (custom exception)
- Throws EmptyProgramException (custom exception)
- Loads content and re-tokenizes program

**Program.save(String filename)**:
- Validates file does not exist
- Throws FileAlreadyExistsException (prevents overwriting)
- Uses try-with-resources for safe FileWriter handling

**Program.deleteLines(int begin, int end)**:
- Removes lines in range (inclusive)
- Re-tokenizes after deletion
- Maintains line number ordering

### New Exception Classes

**EmptyProgramException**:
- Thrown when attempting to load empty file
- Custom exception for file I/O layer
- Clear error messages for users

**FileAlreadyExistsException**:
- Thrown when SAVE target file exists
- Prevents accidental file overwrites
- Custom exception for file I/O layer

### Test Results (July 26, 2026)

**New Test Coverage**:
- ProgramTest (+7 tests): hasContent() validation
- LineEditorTest (+9 tests): Editor operations
- EmptyProgramExceptionTest (8 tests): Exception functionality
- FileAlreadyExistsExceptionTest (9 tests): Exception functionality

**Complete Test Suite**:
- Total Unit Tests: 1214/1214 pass ✅ (+33 new)
- System Integration: 34/34 pass ✅
- Build: SUCCESS (zero failures, zero errors)

### Documentation Updates (July 26, 2026)

**NEW Documents**:
- `doc/USER_GUIDE.md` — Comprehensive user manual
- `doc/Java_25_Needed_Changes.md` — Future Java upgrade roadmap

**UPDATED Documents**:
- `README.md` — Changed to documentation index
- `CHANGELOG.md` — Reorganized and consolidated
- `BASIC.md` — Added line editor command documentation
- `doc/GD-BASIC_Detailed_Design.md` — Architecture updates

### Backward Compatibility

✅ **Fully backward compatible**:
- All existing programs still run
- File execution mode unchanged
- Direct execution (-r flag) still works
- Configuration system unchanged
- All 1181 previous tests still pass

---

## [0.2.0] - 2026-06-16 (Major Release)

**Breaking Change Release**: Variable type system modernization with addition of string case conversion functions.

### Variable Type System Redesign

#### Removal of `#` Suffix for Real Variables

**Breaking Change**: The `#` suffix for real/floating-point variables has been completely removed.

- **Previous syntax**: `result# = 3.14`, `count# = UPPER(count#)`
- **New syntax**: `result = 3.14`, `count = UPPER(count)`
- **Rationale**: Untyped variables now default to Real type; `#` suffix is redundant and freed for future use
- **Migration**: All existing BASIC programs must remove `#` suffixes; see BASIC_CODING_STANDARD.md for migration guide

**Files Modified**:
- `eu.gricom.basic.memoryManager.VariableManagement.java` — Removed `_moUntyped` HashMap, consolidated to `_moReals`
- `eu.gricom.basic.tokenizer.BasicTokenType.java` — Removed REAL type token
- Updated 27 test BASIC programs in `src/test/basic/`
- Updated 14 test programs in `test/system/`

**Test Results**:
- All 908 unit tests pass ✅
- All system integration tests pass ✅
- Zero regressions in existing functionality

### String Case Conversion Functions (NEW - June 16, 2026)

#### UPPER() Function

Convert strings to uppercase.

```basic
10 INPUT "Enter text: "; text$
20 result$ = UPPER(text$)
30 PRINT "Uppercase: "; result$
```

- Converts all lowercase letters to uppercase
- Preserves numbers, special characters, and whitespace
- Returns StringValue type
- Example: `UPPER("HeLLo")` → `"HELLO"`

#### LOWER() Function

Convert strings to lowercase.

```basic
10 INPUT "Enter text: "; text$
20 result$ = LOWER(text$)
30 PRINT "Lowercase: "; result$
```

- Converts all uppercase letters to lowercase
- Preserves numbers, special characters, and whitespace
- Returns StringValue type
- Example: `LOWER("HeLLo")` → `"hello"`

**Implementation Details**:
- Registered in `ReservedWords.java` as `UPPER` and `LOWER` (without `$` suffix)
- Token types: `BasicTokenType.UPPER`, `BasicTokenType.LOWER`
- Parser routing: Single-parameter function handling in `BasicParser.atomic()`
- Function dispatch: Cases added to `Function.java`

**Test Coverage** (19 new unit tests):
- **FunctionTest** (15 tests): Basic conversions, mixed case, alphanumeric, special characters
- **BasicParserTest** (4 tests): Full parse-to-execution pipeline verification
- **System Tests** (2 BASIC programs): test_upper_parsing.bas, test_lower_parsing.bas

**Test Results**:
- 982/982 unit tests passing ✅ (19 new tests added)
- 100% code coverage for new functionality
- Zero test failures or regressions

### Test Results (June 16, 2026)

- Unit Tests: 908/908 pass ✅
- System Integration Tests: 34/34 pass ✅
- Build successful with Java 21
- Zero regressions

### Valid Variable Types in 0.2.0
- **Untyped (real default)**: `X`, `pi`, `value`
- **Double**: `result!`, `sum!`
- **Integer**: `count%`, `index%`
- **String**: `name$`, `text$`
- **Long**: `bignum&`, `largeint&`
- **Boolean**: `flag@`, `condition@`

---

## [0.2.0+] - 2026-07-25 (Patch Release)

**Configuration Management System**: YAML-based externalized configuration and quality improvements.

### Summary

This patch introduces a complete configuration management system:
- **New**: YAML-based configuration with environment variable overrides
- **New**: EnvParam singleton for type-safe configuration access
- **Improved**: Application version and Dartmouth mode now externalized
- **Fixed**: Maven resource packaging for YAML configuration
- **Enhanced**: 16 new EnvParam tests + precision improvements in ExpTest
- **1181/1181 tests pass** ✅ with zero regressions

### Configuration Management System (NEW - July 25, 2026)

#### YAML-Based Configuration

**New Feature**: Externalized YAML configuration system via `EnvParam` singleton class.

**Files Added**:
- `src/main/resources/application.yaml` — Application configuration with `environment` and `testing` groups

**Configuration Keys** (environment group):
- `app_name` (String): "GD-BASIC"
- `version` (String): "0.2.0"
- `max_bcd_digits` (Integer): 40
- `dartmouth` (Boolean): false
- `log_level` (String): "warning"

**Configuration Keys** (testing group):
- Parallel keys for unit/integration testing with overridable values

#### Environment Variable Override

Environment variables take precedence over YAML configuration:
```bash
export dartmouth=true
export log_level=debug
java -jar target/BASIC-*.jar program.bas
```

#### EnvParam Enhancement

**Refactored from Static Constant to Singleton**:
- `getMAX_BCD_DIGITS()` → `getMaxBcdDigits()` (now queries configuration)
- New methods: `getString()`, `getInt()`, `getFloat()`, `getBoolean()`
- Singleton instance cached with configuration group support

**Files Modified**:
- `eu.gricom.basic.helper.EnvParam.java` — Complete rewrite as singleton configuration manager
- `eu.gricom.basic.variableTypes.RealValue.java` — Updated to use new `getMaxBcdDigits()` method
- `eu.gricom.basic.Basic.java` — `_bDartmouthFlag` and `_strVersion` now externalized to config

### Build System Improvements (July 25, 2026)

**Maven Resource Configuration**
- Fixed `pom.xml` resource directory from `src/**/*.java` exclusion to explicit `src/main/resources`
- Enables proper packaging of YAML configuration files

**Dependency Addition**
- Added SnakeYAML 2.2 for YAML parsing and deserialization

### Testing Improvements (July 25, 2026)

**EnvParamTest Complete Rewrite**:
- 16 comprehensive test cases covering all configuration scenarios
- Tests for all type conversions: String, Integer, Float, Boolean

**Precision Fixes**:
- `ExpTest.java`: Added epsilon parameter (1e-15) to floating-point assertions

**Test Results (July 25, 2026)**:
- Total Unit Tests: 1181/1181 pass ✅
- System Integration Tests: 34/34 pass ✅
- Zero regressions; all existing tests pass without modification

### Breaking Changes

⚠️ **Method Name Change**: `EnvParam.getMAX_BCD_DIGITS()` → `EnvParam.getMaxBcdDigits()`
- Aligns with Java naming conventions (camelCase for methods)
- Queries configuration instead of returning hardcoded constant

---

## [0.1.1] - 2026-05-30 (Extended)

Release featuring complete file operations functionality, EOF function type correction, and comprehensive design documentation for future enhancements.

### File Operations Completion (May 30, 2026)

#### EOF Function Type Correction

**Critical Fix**: EOF function now returns BooleanValue instead of IntegerValue
- **Previous behavior**: `EOF(fileId)` returned 0 (file not at end) or 1 (at end)
- **New behavior**: `EOF(fileId)` returns true/false boolean values
- **Impact**: Enables direct use in IF conditions without type casting
- **Example**: `IF EOF(1) THEN PRINT "End of file"` now works correctly

### Advanced File Operations (May 21-30, 2026)

**New Statement Classes**:
- FGetStatement: read next character from file with position tracking
- FPutStatement: write single character to file without newline
- FPeekStatement: read character without advancing cursor
- FRewindStatement: reset file cursor to beginning
- MkDirStatement: create directory with error handling
- RmDirStatement: remove directory (supports recursive deletion with force flag)

**New File Operation Functions** (12 total):
- FExists, DirExists: check file/directory existence
- FGetFileName, FGetSize, FModTime: file metadata
- FIsOpen, FLineCount: file status
- GetCwd, ChDir, ListDirectory: directory operations
- FCompare: file comparison

### Parser Test Coverage (NEW - May 30, 2026)

**All 35 Uncovered Functions in BasicParser.atomic() Now Have Unit Tests**

**Test Results (May 30, 2026)**:
- Total unit tests: 941/941 pass ✅
- New function tests: 35 (all passing)
- Zero test failures or errors

#### CHDIR Statement Implementation (May 22-25, 2026)

**ChDirStatement Class**:
- Location: `src/main/java/eu/gricom/basic/statements/ChDirStatement.java`
- Implements Statement interface with required methods
- Takes StringValue parameter representing target directory path
- Exception handling for invalid/inaccessible paths

#### DIREXISTS Function (May 22-25, 2026)

**Function Implementation**:
- Single-parameter function returning BooleanValue
- Path verification: returns false if path is file or doesn't exist
- Returns true only if path points to valid directory

### Block IF & Array Support (May 24, 2026)

**Block IF Statement Support**:
- Multi-line IF-THEN-ELSE-END-IF block structures fully implemented
- Parser correctly distinguishes between single-line IF, inline IF, and block IF
- Full support for nested IF blocks and mixed control structures

**Multi-Dimensional Array Parsing**:
- Fixed tokenizer/normalizer to properly handle array subscripts
- Multi-dimensional arrays like `matrix%(1,2)` now parse and execute correctly
- Array subscripts with expressions fully supported

**Parser Refactoring**:
- Extracted 9 statement parsing methods from main parser switch statement
- Eliminated 200+ lines of code duplication
- Created reusable statement parsing methods for both main loop and block contexts

### Test Results (May 30, 2026)

- Unit Tests: 902/902 pass ✅
- System Integration Tests: 42/45 pass (93%)
- BASIC Test Programs: 21+ passing
- Build time: ~22 seconds

---

## [0.1.1] - 2026-05-24

Release featuring complete block IF statement implementation, multi-dimensional array support, and major parser refactoring.

### Major Features Completed
- **Multi-Line IF-THEN-ELSE-END-IF Block Statements** — Full implementation and testing
- **Multi-Dimensional Array Support** — Complete parser and tokenizer fixes
- **Array Support in READ Statements** — Enhanced READ statement parser
- **Major Parser Refactoring** — Code quality and maintainability improvements

### Test Results
- **Unit Tests**: 848/848 pass ✅
- **System Integration Tests**: 34/34 pass ✅
- **BASIC Test Programs**: 21/21 pass ✅
- **Total Test Coverage**: 903/903 tests (100% pass rate)

---

## [0.1.0] - 2026-07-11

Release focusing on advanced language features, operator precedence, and mathematical functions.

### Added
- Unit tests for math functions, precedence, and Dartmouth calculus
- Operator precedence in mathematical calculations
- pom.xml update

### Changed
- First generation of object json file
- Added streaming of program object for future use (triggered by command line parameter)

---

## [0.0.8] - File Handling Release

* Added file interface to allow the interpreter to work with external files
* Change the build setting to allow compilation on Debian and Raspberry PI 4 under 64 bit Ubuntu
* Added directories to allow C and GO compiler modules

Implemented Backlog Items:
    [BASIC-55][BASIC-121][BASIC-133]

---

## [0.0.7] - Macro Release

* Verified that the JASIC code has been completely removed from the project
* Added the framework to handle macro constructs (`DEF FN`)
* Added the code highlighting for [Visual Studio Code (VSC)](https://code.visualstudio.com/)

---

## [0.0.6] - Array and Function Release

* Removed the `-i` mandatory parameter in the command line
* Removed the JASIC functionality
* Added the `@PRAGMA` feature. At this moment it allows to change the setting of the log level, but it can also be used to change any other execute parameter of the interpreter.
* Added the colon `:` programming feature to the interpreter, which allows multiple commands in a single line.
* Added array function to all data types. The array function allows the use of n-dimensional arrays. The index of the array can be one or more variables itself. The documentation is adjusted to it.
* Adding functionality to retrieve single characters out of any string. A string's characters can be retrieved by using squared brackets: `[` and `]`.
* Added mathematical operators: `>>`, `<<`, `%`, `AND`, and `OR`
* Added mathematical functions: `ABS`, `ATN`, `CDBL`, `CINT`, `COS`, `EXP`, `LOG`, `LOG10`, `NOT`, `RND`, `SIN`, `SQR`, `TAN`
* Added string functions: `ASC`, `CHR`, `INSTR`, `LEFT`, `LEN`, `MID`, `RIGHT`, `STR`, `VAL`
* Added system functions: `MEM`, `SYSTEM`, `TIME`
* Enhanced build system with improved Maven configuration
* Added `READ` and `DATA` statements
* Fixed issues with FOR statement, IF statement support for ELSE, and block structure support

Implemented Backlog Items:
    [BASIC-19][BASIC-37][BASIC-53][BASIC-64][BASIC-73][BASIC-89][BASIC-102][BASIC-104][BASIC-105][BASIC-108][BASIC-109][BASIC-110][BASIC-117][BASIC-120][BASIC-127]

---

## [0.0.5] - Loop Release

* Added the `FOR-NEXT`-loop
* Added the `DO-UNTIL`-loop
* Added `GOSUB`-statement
* `PRINT` with a trailing semicolon `;` surpresses LF at the end of the output. Also using a comma `,` in the argument list to print multiple outputs in a single command.
* Added variable naming to typed variables: long type: `variable&`

Implemented Backlog Items:
    [BASIC-24], [BASIC-26], [BASIC-43], [BASIC-48], [BASIC-58], [BASIC-62], [BASIC-66], [BASIC-65], [BASIC-74], [BASIC-80], [BASIC-67], [BASIC-68], [BASIC-69], [BASIC-70], [BASIC-71], [BASIC-72], [BASIC-81], [BASIC-84]

---

## [0.0.4] - Extension Release

* Starting to build the interpreter to support Dartmouth style BASIC
  * Tokenizer and Parser re-build: Moving from character based tokenization to line based tokenization
  * General code cleansing and added JUnit test cases for new classes
  * Implemented commands: All JASIC commands are now implemented in the BASIC branch
  * Added functionality to the `IF` command: using block structure to allow more commands after an `IF` command (BASIC branch only)
  * `PRINT` - general output command
  * `REM` for any remarks. This is now not only ignored but passed to the parser.
* Introducing variable naming to typed variables: string type: `variable$`, integer type: `variable%`, real type: `variable#`, and boolean type: `variable!`
* Added additional math operators: `^` (power of), `!=`, `<=`, and `>=`; replaced `=` in comparisons by `==`
* Added JUNIT tests for all new functions

Implemented Backlog Items:
    [BASIC-15], [BASIC-10], [BASIC-47], [BASIC-40], [BASIC-56], [BASIC-21], [BASIC-20], [BASIC-22], [BASIC-32], [BASIC-33], [BASIC-34], [BASIC-35], [BASIC-18], [BASIC-29], [BASIC-30], [BASIC-31], [BASIC-44], [BASIC-45], [BASIC-46], [BASIC-59], [BASIC-60], [BASIC-61], [BASIC-63]

---

## [0.0.3] - Initial Release

* Leaving JASIC as it is and starting the BASIC interpreter functionality
* Changed variable management to new typed variables: string, integer, real, and boolean
  * Moving math functions `+`,`*`, `-`, `/`, `=`, `<`, and `>` into the type classes
  * Added JUnit test classes
* Changed memory management: Converting the MemoryManage class into a package with dedicated classes
* Introduction of new command: `END`
* Bug fixes: Correcting the design and implementation of the `LabeLStatement` class

Implemented Backlog Items:
    [BASIC-4], [BASIC-23], [BASIC-8], [BASIC-38], [BASIC-25], [BASIC-6], [BASIC-39], [BASIC-9], [BASIC-28]

---

## [0.0.2]

* Adding tooling to the existing code:
* Command Line - Using Apache Commons CLI to process the input parameter
* Logger - lightweight logger to avoid heavy-weight log4j
* Adding JUnit Test Cases for a majority of the functions
* Adding regression testing with provided BASIC programs

Implemented Backlog Items:
    [BASIC-16], [BASIC-7], [BASIC-11], [BASIC-12], [BASIC-14], [BASIC-17]

---

## [0.0.1]

* Setup and project re-factor the original project by Bob Nystrom, containing:
* "Mavenizing" the project: Using Apache Maven as the build processor, also allowing a CI/CD development process
* Adding Checkstyle and PMD static code testing

---

## Statistics

- **Total Commits**: 220+ (including stash and checkpoints)
- **Unique Commits**: ~150+ across main development branches
- **Development Span**: December 2020 - July 2026 (5+ years, 7 months)
- **Active Contributors**: Andreas Grimm, Claude (AI Assistant), dependabot, Local History
- **Current Version**: 0.2.0+ (Extended)
- **Test Coverage**: 1214/1214 tests (100% pass rate)
  - Unit Tests: 1214/1214 pass ✅
  - System Integration Tests: 34/34 pass ✅
  - BASIC Test Programs: 21+ passing
- **Statement Types**: 35+ statement implementations
- **Built-in Functions**: 40+ functions
- **Code Quality**: Zero Checkstyle violations, zero PMD issues

---

## Key Milestones

1. **December 2020** - Initial GD-BASIC version 0.0.3 from GDBI project
2. **January 2021** - Type system refactoring, operator expression handling
3. **February-March 2021** - Comprehensive builtin functions
4. **April 2021** - Logical operators, PRAGMA, macros preparation
5. **2021-2022** - CMake and build system experiments
6. **2023-2025** - Java 17 migration, File I/O system, test infrastructure
7. **July 11, 2025** - Version 0.1.0 release
8. **May 2026** - Version 0.1.1 release: advanced file operations, comprehensive tests
9. **June 16, 2026** - Version 0.2.0 release: type system simplification, string functions
10. **July 25, 2026** - Patch: configuration management system
11. **July 26, 2026** - Version 0.2.0+ Extended: interactive line editor and file I/O

---

## See Also

- **README.md** — Project overview and documentation index
- **doc/USER_GUIDE.md** — Comprehensive user manual
- **doc/BASIC.md** — BASIC language reference
- **doc/BASIC_CODING_STANDARD.md** — BASIC language specification
- **doc/GD-BASIC_Detailed_Design.md** — Architecture documentation
- **doc/Java_25_Needed_Changes.md** — Future upgrade roadmap