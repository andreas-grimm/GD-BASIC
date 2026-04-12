# GD-BASIC System Test Suite

## Overview

This directory contains a comprehensive test suite for the GD-BASIC interpreter. The test suite is designed to verify all major features and functionality of the BASIC language implementation.

## Test Organization

Each test file focuses on a specific feature or aspect of the BASIC language. Tests are self-contained and can be run individually or as part of the complete suite.

## Test Files

### Operator Tests

#### test_arithmetic_operators.bas
**Purpose:** Tests all basic arithmetic operators  
**Operators Tested:** `+`, `-`, `*`, `/`, `^`, `%`  
**Features:**
- Basic arithmetic operations
- Operator precedence
- Parentheses grouping
- Power and modulo operations

**Expected Results:**
- 5 + 3 = 8
- 10 - 4 = 6
- 6 * 7 = 42
- 20 / 4 = 5
- 2 ^ 3 = 8
- 17 % 5 = 2
- Operator precedence: 2 + 3 * 4 = 14
- Parentheses: (2 + 3) * 4 = 20

#### test_comparison_operators.bas
**Purpose:** Tests all comparison operators  
**Operators Tested:** `==`, `!=`, `<`, `>`, `<=`, `>=`  
**Features:**
- Equality and inequality testing
- Relational comparisons
- Use in conditional statements

**Expected Results:**
- All equality tests evaluate correctly
- All inequality tests evaluate correctly
- All relational comparisons work as expected

#### test_logical_operators.bas
**Purpose:** Tests logical operators  
**Operators Tested:** `AND`, `OR`, `NOT`  
**Features:**
- Boolean AND operations
- Boolean OR operations
- Boolean NOT operations
- Truth table verification

**Expected Results:**
- TRUE AND TRUE = TRUE
- TRUE AND FALSE = FALSE
- TRUE OR FALSE = TRUE
- FALSE OR FALSE = FALSE
- NOT TRUE = FALSE
- NOT FALSE = TRUE

#### test_bitwise_operators.bas
**Purpose:** Tests bitwise shift operators  
**Operators Tested:** `<<`, `>>`  
**Features:**
- Left shift operations
- Right shift operations
- Integer bit manipulation

**Expected Results:**
- 4 << 1 = 8
- 3 << 2 = 12
- 16 >> 1 = 8
- 20 >> 2 = 5

#### test_assignment_operators.bas
**Purpose:** Tests assignment operators  
**Operators Tested:** `=`, `:=`, `LET`  
**Features:**
- Standard assignment
- Pascal-style assignment
- LET statement
- Variable reassignment

**Expected Results:**
- All assignment styles work correctly
- Variables can be reassigned
- Multiple assignments chain properly

### Variable Type Tests

#### test_variable_types.bas
**Purpose:** Tests all variable types  
**Types Tested:** Integer (`%`), Real (`#`), String (`$`), Boolean (`!`), Long (`&`)  
**Features:**
- Variable declaration and initialization
- Type-specific operations
- Mixed-type arithmetic
- Type conversion

**Expected Results:**
- Each variable type stores values correctly
- Type suffixes work properly
- Mixed-type operations produce correct results

#### test_string_assignment.bas
**Purpose:** Tests string variable operations  
**Features:**
- String assignment
- String concatenation
- Empty strings
- Special characters in strings

**Expected Results:**
- Strings are assigned correctly
- Concatenation works with + operator
- Empty strings are handled properly

### Control Flow Tests

#### test_if_then_else.bas
**Purpose:** Tests IF-THEN-ELSE conditional statements  
**Features:**
- Simple IF-THEN
- IF-THEN blocks with END-IF
- IF-THEN-ELSE branches
- Nested IF statements
- Multiple conditions with AND/OR
- Direct GOTO after THEN

**Expected Results:**
- Correct branch execution based on conditions
- Nested conditions work properly
- ELSE clause executes when condition is false

#### test_for_next_loop.bas
**Purpose:** Tests FOR-NEXT loop structures  
**Features:**
- Basic FOR-NEXT loops
- STEP clause (positive and negative)
- Nested loops
- Variable bounds and step values
- Loop counter verification

**Expected Results:**
- Loops iterate correct number of times
- STEP values control increment/decrement
- Nested loops maintain separate counters
- Variable bounds work correctly

#### test_do_until_loop.bas
**Purpose:** Tests DO-UNTIL loop structures  
**Features:**
- Basic DO-UNTIL loops
- Condition evaluation
- Nested DO-UNTIL loops
- Loop exit conditions

**Expected Results:**
- Loops execute until condition becomes true
- Condition checked after each iteration
- Nested loops work independently

#### test_while_loop.bas
**Purpose:** Tests WHILE loop structures  
**Features:**
- Basic WHILE loops
- Pre-condition evaluation
- Loops that don't execute (false initial condition)
- Nested WHILE loops

**Expected Results:**
- Loops execute while condition is true
- Condition checked before each iteration
- Loops skip if initial condition is false

#### test_goto_gosub_return.bas
**Purpose:** Tests program flow control  
**Statements Tested:** `GOTO`, `GOSUB`, `RETURN`  
**Features:**
- Unconditional jumps with GOTO
- Subroutine calls with GOSUB
- Subroutine returns with RETURN
- Nested subroutine calls
- Multiple subroutine calls

**Expected Results:**
- GOTO jumps to specified line
- GOSUB calls subroutine and returns
- RETURN returns to correct location
- Nested calls maintain proper stack

### Mathematical Function Tests

#### test_math_functions.bas
**Purpose:** Tests all mathematical functions  
**Functions Tested:** `ABS`, `ATN`, `CDBL`, `CINT`, `COS`, `EXP`, `LOG`, `LOG10`, `RND`, `SIN`, `SQR`, `TAN`  
**Features:**
- Absolute value
- Trigonometric functions
- Logarithmic functions
- Type conversion functions
- Square root
- Random number generation

**Expected Results:**
- ABS(-5) = 5
- SQR(16) = 4
- CINT(3.7) = 4 (rounding)
- CDBL(5) = 5.0
- SIN(0) = 0
- COS(0) = 1
- LOG(1) = 0
- LOG10(100) = 2
- RND returns value between 0 and 1

### String Function Tests

#### test_string_functions.bas
**Purpose:** Tests all string manipulation functions  
**Functions Tested:** `ASC`, `CHR$`, `INSTR`, `LEFT$`, `LEN`, `MID$`, `RIGHT$`, `STR$`, `VAL`  
**Features:**
- String length calculation
- Substring extraction
- ASCII conversion
- String/number conversion
- Substring search

**Expected Results:**
- LEN("Hello") = 5
- LEFT$("BASIC", 3) = "BAS"
- RIGHT$("BASIC", 2) = "IC"
- MID$("PROGRAMMING", 4, 4) = "GRAM"
- ASC("A") = 65
- CHR$(65) = "A"
- STR$(42) = "42"
- VAL("123") = 123
- INSTR("Hello World", "World") = 7

#### test_string_indexing.bas
**Purpose:** Tests string character indexing  
**Features:**
- Individual character access using [index]
- Character extraction in loops
- Index-based string manipulation

**Expected Results:**
- Characters accessible by index
- Indexing works in expressions and loops

### Array and Data Structure Tests

#### test_arrays_dim.bas
**Purpose:** Tests array declaration and operations  
**Statement Tested:** `DIM`  
**Features:**
- 1D arrays (integer, real, string)
- 2D arrays
- Array indexing
- Arrays in loops
- Variable indices
- Array bounds

**Expected Results:**
- Arrays declared with DIM
- Elements accessed by index
- Multi-dimensional arrays work
- Variable indices function correctly

#### test_data_read.bas
**Purpose:** Tests DATA and READ statements  
**Statements Tested:** `DATA`, `READ`  
**Features:**
- Basic data storage and retrieval
- String data
- Mixed-type data
- Sequential reading
- READ in loops

**Expected Results:**
- READ retrieves values from DATA
- Multiple DATA statements work
- Mixed types handled correctly
- Sequential reading maintains position

### File I/O Tests

#### test_file_io.bas
**Purpose:** Tests File I/O operations  
**Statements/Functions Tested:** `FOPEN`, `FCLOSE`, `FINPUT`, `FPRINT`, `EOF`  
**Features:**
- Opening files for reading and writing
- Writing data to files with FPRINT
- Reading data from files with FINPUT
- End-of-File detection with EOF function
- Closing files and deleting temporary files

**Expected Results:**
- Files are created and written to successfully
- Data read back matches the data written
- EOF is correctly detected at the end of the file
- Files are closed and can be optionally deleted

### System Function Tests

#### test_system_functions.bas
**Purpose:** Tests system-level functions  
**Functions Tested:** `MEM`, `TIME$`, `SYSTEM`  
**Features:**
- Memory availability query
- System time retrieval
- System command execution

**Expected Results:**
- MEM returns non-negative value
- TIME$ returns time string
- Functions execute without errors

### Statement Tests

#### test_print_statement.bas
**Purpose:** Tests PRINT statement variations  
**Features:**
- Basic output
- Semicolon separator (no newline)
- Comma separator (tab)
- Variable output
- Expression output
- Empty PRINT (blank line)
- Multiple values

**Expected Results:**
- Text outputs correctly
- Separators work as expected
- Variables and expressions print

#### test_rem_comments.bas
**Purpose:** Tests comment syntax  
**Statements Tested:** `REM`, `'`  
**Features:**
- REM keyword comments
- Tick (') comments
- Inline comments
- Comment lines

**Expected Results:**
- Comments are ignored
- Inline comments don't affect code
- Both comment styles work

#### test_colon_separator.bas
**Purpose:** Tests colon statement separator  
**Features:**
- Multiple statements on one line
- Colon with assignments
- Colon with PRINT
- Colon with calculations

**Expected Results:**
- Multiple statements execute in order
- Colon properly separates statements

#### test_not_operator.bas
**Purpose:** Tests NOT logical operator  
**Features:**
- NOT with boolean variables
- NOT in IF statements
- Boolean negation

**Expected Results:**
- NOT correctly negates boolean values
- Works in conditional expressions

#### test_pragma_statement.bas
**Purpose:** Tests @PRAGMA directive  
**Features:**
- Setting interpreter options
- LOG_LEVEL configuration
- Runtime parameter changes

**Expected Results:**
- PRAGMA executes without errors
- Settings can be changed at runtime

#### test_end_statement.bas
**Purpose:** Tests END statement  
**Features:**
- Program termination
- Unreachable code after END

**Expected Results:**
- Program terminates at END
- Code after END is not executed

### Advanced Tests

#### test_complex_expressions.bas
**Purpose:** Tests complex mathematical and logical expressions  
**Features:**
- Nested arithmetic operations
- Mixed operators
- Complex boolean expressions
- Expressions with functions
- Negative numbers
- Multiple parentheses levels

**Expected Results:**
- Complex expressions evaluate correctly
- Operator precedence maintained
- Functions work within expressions

#### test_edge_cases.bas
**Purpose:** Tests boundary conditions and edge cases  
**Features:**
- Zero values
- Division by one
- Multiplication by zero
- Power of zero and one
- Empty strings
- Single-character strings
- Loops with equal start/end

**Expected Results:**
- Edge cases handled correctly
- No crashes or unexpected behavior
- Boundary values work properly

### User Defined Function Tests

#### test_defs_functions.bas
**Purpose:** Tests DEF FN user-defined macro functions  
**Features:**
- Macro definition with DEF
- Functions with 1, 2, and 3 parameters
- Expression evaluation within macros
- String-based macro results

**Expected Results:**
- Macros are defined and processed without error
- Functions return correct results based on parameters
- Multiple parameters are handled correctly

### Regression and Mixed Feature Tests

#### test_mixed_tests_1.bas
**Purpose:** Tests mixed features including string indexing and multi-dimensional arrays  
**Features:**
- String character indexing with []
- Multi-dimensional string and real arrays
- FOR-NEXT loops with arrays
- Real array summation

**Expected Results:**
- String indexing returns correct characters
- Multi-dimensional arrays (up to 4 dimensions) work
- Loops iterate and access array elements correctly

#### test_mixed_tests_2.bas
**Purpose:** Tests parentheses, math functions, and system commands  
**Features:**
- Operator precedence with parentheses
- System functions: MEM(), RND()
- Math functions: ABS() with various types
- String functions: LEFT(), RIGHT()
- SYSTEM command execution

**Expected Results:**
- Parentheses correctly override precedence
- MEM and RND return valid numeric values
- ABS handles integers, reals, and variables
- String functions extract correct substrings
- SYSTEM command executes external programs

#### test_mixed_tests_3.bas
**Purpose:** Tests DATA/READ, IF-THEN-ELSE, and advanced statements  
**Features:**
- DATA and READ with arrays and loops
- IF-THEN-ELSE blocks
- Colon (:) statement separator
- Bitwise operators (<<, >>) and modulo (%)
- @PRAGMA Trace and Logger control

**Expected Results:**
- READ correctly retrieves DATA into array elements
- IF-THEN-ELSE branches execute correctly
- Colon separator allows multiple statements per line
- Bitwise and modulo operations return correct values
- PRAGMA directives are processed correctly

#### test_mixed_tests_4.bas
**Purpose:** Tests basic parser features and flow control  
**Features:**
- REM and tick (') comments
- Empty lines and line numbering
- Variable assignment and arithmetic precedence
- GOTO jumps
- Simple IF-THEN blocks

**Expected Results:**
- Comments and empty lines are ignored
- Arithmetic follows standard BODMAS rules
- GOTO correctly redirects execution
- IF-THEN blocks work as expected

#### test_mixed_tests_5.bas
**Purpose:** Tests various loop structures and subroutines  
**Features:**
- FOR loops with positive and negative STEP
- GOSUB and RETURN for subroutines
- WHILE-END-WHILE loops
- DO-UNTIL loops

**Expected Results:**
- FOR loops count correctly in both directions
- Subroutines execute and return to the caller
- WHILE and DO-UNTIL loops iterate correctly based on conditions

## Running the Tests

### Run All Tests

To run the complete test suite:

```bash
cd test/system
./run_all_tests.sh
```

The script will:
1. Check that the GD-BASIC JAR file exists
2. Run each test in sequence
3. Report pass/fail status for each test
4. Stop on the first failure
5. Display a summary at the end

### Run Individual Tests

To run a single test:

```bash
cd /path/to/GD-BASIC
java -jar target/BASIC-*-jar-with-dependencies.jar test/system/test_arithmetic_operators.bas
```

### Prerequisites

Before running tests, build the project:

```bash
mvn clean package
```

This creates the JAR file needed to run the tests.

## Test Results

### Success Criteria

A test passes if:
- It executes without errors (exit code 0)
- All assertions pass (IF conditions that GOTO error on failure)
- Expected output is produced
- The test prints "PASSED" message

### Failure Handling

A test fails if:
- The interpreter returns a non-zero exit code
- An assertion fails (GOTO 9000 error handler)
- The test prints "ERROR: Test failed!"

When a test fails, the runner script:
- Stops execution immediately
- Displays the failing test name
- Shows the test output for debugging
- Reports the exit code

## Test Coverage

This test suite covers:

### Language Features (100%)
- ✓ All arithmetic operators (+, -, *, /, ^, %)
- ✓ All comparison operators (==, !=, <, >, <=, >=)
- ✓ All logical operators (AND, OR, NOT)
- ✓ All bitwise operators (<<, >>)
- ✓ All assignment operators (=, :=, LET)

### Data Types (100%)
- ✓ Integer variables (%)
- ✓ Real variables (#)
- ✓ String variables ($)
- ✓ Boolean variables (!)
- ✓ Long variables (&)

### Control Flow (100%)
- ✓ IF-THEN-ELSE-END-IF
- ✓ FOR-NEXT loops
- ✓ DO-UNTIL loops
- ✓ WHILE-END-WHILE loops
- ✓ GOTO statements
- ✓ GOSUB/RETURN subroutines

### Functions (100%)
- ✓ Mathematical: ABS, ATN, CDBL, CINT, COS, EXP, LOG, LOG10, RND, SIN, SQR, TAN
- ✓ String: ASC, CHR$, INSTR, LEFT$, LEN, MID$, RIGHT$, STR$, VAL
- ✓ System: MEM, TIME$

### Statements (100%)
- ✓ PRINT (with semicolon and comma separators)
- ✓ REM and ' comments
- ✓ DIM (array declaration)
- ✓ DATA/READ
- ✓ END
- ✓ @PRAGMA
- ✓ Colon separator (:)

### Advanced Features (100%)
- ✓ Arrays (1D and 2D)
- ✓ String indexing with []
- ✓ Nested control structures
- ✓ Complex expressions
- ✓ Edge cases and boundary conditions

## Maintenance

### Adding New Tests

To add a new test:

1. Create a new `.bas` file in `test/system/`
2. Follow the naming convention: `test_<feature_name>.bas`
3. Include a header comment block with:
   - Test name
   - Description
   - Expected results
4. Implement test cases with assertions
5. Use `GOTO 9000` for error handling
6. Print "PASSED" message on success
7. Add the test to `run_all_tests.sh` in the TEST_FILES array
8. Update this README with test documentation

### Test Template

```basic
REM ========================================================================
REM Test: <Feature Name>
REM Description: <What this test validates>
REM Expected: <Expected behavior>
REM ========================================================================

10 PRINT "=== Testing <Feature Name> ==="
20 PRINT ""

REM Test case 1
30 PRINT "Testing <specific case>:"
40 REM ... test code ...
50 IF <condition> != <expected> THEN GOTO 9000

REM More test cases...

900 PRINT ""
910 PRINT "=== All <Feature Name> Tests PASSED ==="
920 END

9000 PRINT "ERROR: Test failed!"
9010 END
```

## Known Issues

None currently. All tests are expected to pass with the current GD-BASIC implementation.

## Version History

- **v1.1** (2026-04-12): Expanded test suite
  - 33 test files including new File I/O coverage
  - Fixed bugs in File I/O implementation (Parser, FPRINT, EOF)
  - Added cleanup to test runner
  - Updated documentation

## Contributing

When contributing new tests:
1. Ensure tests are self-contained and independent
2. Use clear, descriptive test names
3. Document expected behavior
4. Follow the existing test structure
5. Verify tests pass before committing
6. Update this README with new test documentation

## License

These tests are part of the GD-BASIC project and are subject to the same license terms.
