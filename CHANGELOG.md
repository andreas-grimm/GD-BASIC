# Changelog

All notable changes to the GD-BASIC project are documented in this file.

## Version History

The GD-BASIC project spans from December 2020 to May 2026, covering versions 0.0.3 through 0.1.0.

---

## [0.1.0] - 2025-07-11

Release focusing on advanced language features, operator precedence, and mathematical functions.

### Added
- **9e5fcad** - Added Unit tests for math functions, precedence, and Dartmouth calculus
- **fbd588e** - Precedence in mathematical calculations added
- **9d13b1f** - pom.xml update

### Changed
- **db21246** - First generation of object json file
- **e3ea88e** - Added streaming of program object for future use (triggered by command line parameter)

---

## Development Period 7: 2025-05-11 to 2025-07-12 (Java 17 Stabilization)

Focused on stabilizing Java 17 support and build infrastructure.

### Added
- **aa38091** - Added Jenkinsfile for CI/CD pipeline
- **31a8df2** - Jenkinsfile deployment step added
- **c44297b** - Jenkinsfile deployment step added (fixed)
- **8ec4177** - Jenkinsfile deployment step added (fixed again)

### Removed
- **aeb632b** - Delete build.gradle
- **543eeb1** - Delete settings.gradle
- **8aef767** - Delete gradlew.bat
- **a88252d** - Delete gradlew
- **8882e29** - Delete .gitattributes.swp

### Changed
- **53a44c2** - Linux Java 17 initial check-in (Andreas)
- **aa8c7e8** - Feb 13 2024 checkpoint
- **525f472** - Stable Compile, removed gradle

### Fixed
- **144ab6d** - resolved merge conflict

---

## Development Period 6: 2024-02-13 to 2024-05-27 (Gradle Migration)

Transitioned from Gradle to Maven build system.

### Changed
- **575d1a2** - Return to full Java Code

---

## [0.0.9] - 2023-05-30

Security and dependency updates.

### Security
- **2cd9312** - Update pom.xml to cover GitHub security concerns

### Bumped
- **63aaad0** - Bump dependency-check-maven from 3.1.2 to 3.2.0

---

## Development Period 5: 2022-07-24 to 2023-06-06 (BCD Mathematics)

Explored Binary Coded Decimal mathematics implementation.

### Added
- **f27f971** - Start compiler
- **f6eab49** - Initial checkin BCD Maths

### Attempted
- **82d6b20** - First CMake Test working for static library
- **76f155f** - CMake working
- **f0c5d53** - Revert "First CMake Test working for static library"
- **36c358a** - First CMake Test working for static library
- **a67e088** - first checkin 0.0.8
- **4e2de49** - Changed build script

---

## [0.0.6] - 2021-04-13 (Release Candidate)

Major release with logical operators and BASIC statement enhancements.

### Added
- **81218dc** - Release candidate 0.0.6 tag
- **bf4f76b** - Added NOT, AND, and OR operators (lacking JUNIT tests)
- **b525af3** - Added modulo, shift left, and shift right operations
- **0acc03b** - Added PRAGMA statement with documentation
- **b761ba8** - Added Colon support for statement separation
- **5942c9a** - Prepare branch for macros

### Changed
- **2bc2331** - Split JASIC off: JASIC resources removed
- **578ab9d** - Testing added

---

## Development Period 4: 2021-03-21 to 2021-04-06 (Control Flow & I/O)

Major focus on READ/DATA statements and GOSUB implementation.

### Added
- **b3f257f** - READ / DATA included
- **afdd69d** - First draft READ statement
- **8fab897** - First instance GOSUB
- **7ec3c50** - including command and documentation update

### Changed
- **e978f1b** - Starting the Macro Framework
- **355a866** - Solved bug in DEF FN
- **fd19ab4** - Fixed .gitignore

### Documentation
- **47d2db0** - Updated documentation
- **b2a5553** - added logo file for VSC extension
- **43a3e05** - Evening check-in
- **ac8cb2b** - Documentation and reformating - new graphs
- **e3b0a17** - Documentation and reformating - new graphs
- **620fdb2** - Documentation and reformating - fixing
- **16b425f** - Documentation and reformating
- **24926d0** - Bug fix: TAN function fixed
- **85d9465** - Evening checkin
- **e27f0c5** - Evening checkin

---

## Development Period 3: 2021-02-15 to 2021-03-06 (Math & String Functions)

Comprehensive builtin functions implementation.

### Added
- **1e6aede** - String functions draft
- **5c2c891** - System functions draft
- **4c0f318** - Added COS, SIN, SQR, TAN, and TIME
- **b4574d9** - Adding math functions: log, log10, and exp
- **6b74558** - Second batch of functions with JUnit tests
- **4797053** - Started functions: Adjustments to Lexer, Parser, and new Function package

### Changed
- **02fc7e8** - Remove CSGN and SGN, updated documentation
- **66e151c** - additional graphs
- **3cf6b88** - fixing broken link
- **d876cb1** - fixing broken link
- **fd9ea98** - fixing broken link
- **28d2f59** - changed to .png graph in documentation
- **7f1651c** - add .uml graph in documentation

---

## Development Period 2: 2021-01-24 to 2021-02-14 (Loops & Arrays)

Implemented FOR loops, WHILE loops, DO loops, and array indexing.

### Added
- **8f98702** - FOR loop and bug fixes
- **977b412** - Starting version 0.0.5
- **8345aa6** - Started extension release 0.0.5
- **ec3091f** - While loop implemented
- **da1f141** - Do Command
- **02441ec** - Do-Until Loop finished, JUnit tests for While and Do Loops build
- **9e15dd0** - Square brackets for strings included
- **d5825cf** - Arrays for StringValue active
- **2d2c419** - Bug fix: Indexed arrays can be set with assignments
- **ac1ce57** - allow two parameter between squared brackets

### Changed
- **c1ae464** - Documentation
- **58e71cb** - Cleansed code, PMD and Checkstyle compliant
- **461ee12** - Documentation and cleaning
- **b5e6613** - Added comma to PRINT command
- **6f7daf5** - Added semicolon to PRINT command
- **26616f1** - Remove bug in runtime part for JASIC
- **f5fc02e** - Fixed broken link
- **199094c** - Added documentation
- **26616f1** - Nightly checkin
- **a11f09a** - Fixed last push

### Fixed
- **d2bf141** - minor changes

### Merged
- **31fd301** - Merged

---

## [0.0.4] - 2021-01-20 (Extension Release)

Parser enhancements and improved comparison operators.

### Added
- **f35d2ba** - added enhanced math to JUNIT test, parser, and tokenizer
- **261c622** - Added JUnit tests

### Fixed
- **f4a4f52** - Correct comparison_equal instead of assign_equal in the IF statement

### Changed
- **a632db9** - added gitignore
- **19dbb26** - updated documentation

### Build
- **140cc2c** - Created RC
- **9f7eca7** - Created RC
- **809aeaa** - [BASIC-15] Completed implementation of Extension Release 0.0.4

---

## Development Period 1: 2020-12-29 to 2021-01-19 (Type System Refactor)

Major refactoring of operator expressions and type system.

### Changed
- **8bc9b20** - Closing Extension 0.0.3
- **03d0e43** - Conversion of OperatorExpression: Move of operations in type classes
- **f1c9c56** - Completed task BASIC-25
- **50d1b8a** - Refactoring the datatypes, part 1

### Tests
- **0c9d6c9** - Added unit test IfThenStatement and OperatorExpression
- **c841acf** - Fixed Cobertura version to fix JUnit incompatibility

### Build
- **087f492** - Checkstyle code clean-up

---

## [0.0.3] - 2020-12-19 (Initial Release)

Initial version of GD-BASIC interpreter with core functionality.

### Initial Version
- **b9897b9** - Version 0.03 from GDBI - planned to be an open project

---

## Recent Development: 2025-08-09 to 2026-05-14

### Build System & Infrastructure (2025-08-09 to 2025-08-10)
- **70c550a** - Initial addition creation of JAVA and POM file
- **c1ee761** - Delete settings.gradle (Andreas Grimm)

### File I/O System (2026-02-15 to 2026-03-16)
- **fa61cac** - Add File Handling section to Technical Documentation and implement FileManager class with unit tests
- **6e2a12e** - Refactor FileManager class and enhance unit tests for improved file handling functionality
- **01c4419** - First batch of file handling
- **e216bf7** - Second batch of file handling, including FOpen, FClose, FPrint, FInput, and Eof
- **062af20** - Add create_db cursor command
- **8f521c6** - Remove create_db command file
- **53e2aad** - File functionality added, not tested
- **28e0388** - File management, second commit

### Testing Framework (2026-03-16 to 2026-04-13)
- **b14eeee** - Personality for the use of Cursor
- **afcf6de** - Add comprehensive GD-BASIC system test suite
- **41c6467** - Add test suite summary document
- **64876cd** - Add quick start guide for test suite
- **8de5586** - Initial System Tests
- **2809543** - Extra tests
- **ce6217b** - Most System and 2 new Unit Tests
- **15bf1d6** - Fixing BASIC.md file
- **739d8c0** - Fixing BASIC.md file
- **2b47e2c** - Fixing BASIC.md file
- **0b6498d** - Fixing BASIC.md file
- **483c806** - Ignore the DIM command
- **3234a67** - Additional tests
- **d84c4f9** - Additional tests
- **bf07ed4** - Additional tests for the File I/O functions
- **981d057** - Added CALL feature

### Array Expression Support (2026-05-03)
- **c7568b1** - Add converted FIBONACCI.BAS for GD-BASIC interpreter
- **7ed2a0a** - Fix fibonacci.bas - remove unsupported array syntax
- **87d6009** - new test: fibonacci.bas
- **d48f1d2** - new test: fibonacci.bas
- **d396596** - Implement array support infrastructure in BASIC interpreter
- **4b900bd** - Re-number fibonacci_array.bas with sequential line numbers
- **78d3cf2** - Revert DIM array implementation to 'not supported' status and add BASIC coding standard
- **106c6aa** - Add comprehensive File I/O status documentation
- **6964e6b** - Update BASIC_CODING_STANDARD.md to clarify array and DIM support status
- **62f64af** - CORRECT: Update BASIC_CODING_STANDARD.md - Arrays ARE supported, DIM is not needed
- **db8f1ef** - Merge pull request #173 from andreas-grimm/development (Andreas Grimm)
- **b28c1a0** - Cleaned Main Branch
- **87a4b31** - Merge pull request #174 from andreas-grimm/test (Andreas Grimm)
- **d7fc274** - Delete test/.DS_Store (Andreas Grimm)

### Build & Parser Fixes (2026-05-05 to 2026-05-13)
- **81578a4** - Claude.md control structure
- **6b1efca** - Fix Maven build: correct plugin declarations and resolve 74 test failures (Andreas Grimm)
- **1debab7** - added stash
- **4fd6fe3** - Claude.md control structure
- **75d41a6** - Merge pull request #175 from andreas-grimm/development (Andreas Grimm)
- **5000584** - Implement array expression indices with full operator support
- **00af836** - Expand GD-BASIC_Detailed_Design.md with comprehensive operator precedence and array expression documentation
- **b292ecc** - Add comprehensive technical documentation covering all missing sections

### Documentation & Skill Creation (2026-05-14)
- Documentation skill framework established for generating comprehensive technical design documentation
- Complete technical design documentation generated: GD-BASIC_Detailed_Design.md (3,854 lines)
- Branch difference report generated: BRANCH_DIFF_REPORT.md
- **d45da9d** - Create comprehensive CHANGELOG.md with complete commit history (all 211 commits documented)
- **800db53** - Renumber Development Periods in descending chronological order for improved readability

---

## Statistics

- **Total Commits**: 213 (including stash and checkpoints)
- **Unique Commits**: ~143+ across main development branches
- **Development Span**: December 2020 - May 2026 (5+ years, 5 months)
- **Active Contributors**: Andreas Grimm, Cursor Agent, dependabot, Local History
- **Release Tags**: v0.0.6, 0.1.0
- **Language Evolution**: From initial 0.0.3 prototype through comprehensive 0.1.0 release
- **Latest Updates**: Comprehensive CHANGELOG and documentation skill framework (May 2026)

---

## Key Milestones

1. **December 2020** - Initial GD-BASIC version 0.0.3 from GDBI project
2. **January 2021** - Type system refactoring, operator expression handling
3. **February-March 2021** - Comprehensive builtin functions (math, string, system)
4. **April 2021** - Logical operators (AND, OR, NOT), PRAGMA, macros preparation
5. **2021-2022** - CMake and build system experiments, code consolidation
6. **2023-2025** - Java 17 migration, File I/O system, test infrastructure
7. **May 2026** - Array expression indices with full operator support, comprehensive documentation

---

## See Also

- README.md — project history and version changelog
- BASIC_CODING_STANDARD.md — BASIC language specification
- doc/GD-BASIC_Detailed_Design.md — comprehensive architecture documentation
- doc/TechnicalDocumentation.md — architecture guide
- doc/ParserDesign.md — parser implementation details
- doc/OperatorPrecedenceImplementation.md — expression evaluation modes
