# Quick Start Guide - GD-BASIC Test Suite

## Running the Tests

### Step 1: Build the Project

```bash
cd /path/to/GD-BASIC
mvn clean package
```

This creates the JAR file: `target/BASIC-*-jar-with-dependencies.jar`

### Step 2: Run All Tests

```bash
cd test/system
./run_all_tests.sh
```

### Expected Output

```
========================================================================
GD-BASIC System Test Suite
========================================================================

Project Root: /path/to/GD-BASIC
Test Directory: /path/to/GD-BASIC/test/system

Using JAR: target/BASIC-0.1.1-jar-with-dependencies.jar

Running test: test_arithmetic_operators
✓ PASSED: test_arithmetic_operators

Running test: test_comparison_operators
✓ PASSED: test_comparison_operators

Running test: test_logical_operators
✓ PASSED: test_logical_operators

... (all 26 tests) ...

========================================================================
TEST SUITE COMPLETED SUCCESSFULLY
========================================================================
All tests passed!

Summary:
  Total tests: 26
  Passed: 26
  Failed: 0
```

## Running Individual Tests

To run a single test:

```bash
java -jar target/BASIC-*-jar-with-dependencies.jar test/system/test_arithmetic_operators.bas
```

Example output:

```
=== Testing Arithmetic Operators ===

Testing Addition:
5 + 3 = 8.0
Testing Subtraction:
10 - 4 = 6.0
Testing Multiplication:
6 * 7 = 42.0
Testing Division:
20 / 4 = 5.0
Testing Power:
2 ^ 3 = 8.0
Testing Modulo:
17 % 5 = 2
Testing Operator Precedence:
2 + 3 * 4 = 14.0
(2 + 3) * 4 = 20.0

=== All Arithmetic Operator Tests PASSED ===
```

## Test Categories

Run specific categories:

```bash
# Operator tests
java -jar target/BASIC-*-jar-with-dependencies.jar test/system/test_arithmetic_operators.bas
java -jar target/BASIC-*-jar-with-dependencies.jar test/system/test_comparison_operators.bas
java -jar target/BASIC-*-jar-with-dependencies.jar test/system/test_logical_operators.bas

# Control flow tests
java -jar target/BASIC-*-jar-with-dependencies.jar test/system/test_if_then_else.bas
java -jar target/BASIC-*-jar-with-dependencies.jar test/system/test_for_next_loop.bas
java -jar target/BASIC-*-jar-with-dependencies.jar test/system/test_while_loop.bas

# Function tests
java -jar target/BASIC-*-jar-with-dependencies.jar test/system/test_math_functions.bas
java -jar target/BASIC-*-jar-with-dependencies.jar test/system/test_string_functions.bas
```

## Troubleshooting

### JAR file not found

If you see: `ERROR: JAR file not found!`

Solution:
```bash
cd /path/to/GD-BASIC
mvn clean package
```

### Test fails

If a test fails, the runner will:
1. Stop immediately
2. Show which test failed
3. Display the test output
4. Show the exit code

Example:

```
Running test: test_arithmetic_operators
✗ FAILED: test_arithmetic_operators
Exit code: 1

Test output:
=== Testing Arithmetic Operators ===

Testing Addition:
5 + 3 = 8.0
Testing Subtraction:
ERROR: Test failed!
```

### Permission denied on run_all_tests.sh

If you see: `Permission denied`

Solution:
```bash
chmod +x test/system/run_all_tests.sh
```

## What Each Test Does

| Test File | What It Tests |
|-----------|---------------|
| `test_arithmetic_operators.bas` | +, -, *, /, ^, % operators |
| `test_comparison_operators.bas` | ==, !=, <, >, <=, >= operators |
| `test_logical_operators.bas` | AND, OR, NOT operators |
| `test_bitwise_operators.bas` | <<, >> shift operators |
| `test_assignment_operators.bas` | =, :=, LET statements |
| `test_variable_types.bas` | Integer, Real, String, Boolean, Long types |
| `test_string_assignment.bas` | String operations and concatenation |
| `test_if_then_else.bas` | IF-THEN-ELSE-END-IF statements |
| `test_for_next_loop.bas` | FOR-NEXT loops with STEP |
| `test_do_until_loop.bas` | DO-UNTIL loops |
| `test_while_loop.bas` | WHILE-END-WHILE loops |
| `test_goto_gosub_return.bas` | GOTO, GOSUB, RETURN statements |
| `test_math_functions.bas` | ABS, SQR, SIN, COS, TAN, LOG, etc. |
| `test_string_functions.bas` | LEN, LEFT, RIGHT, MID, ASC, CHR, etc. |
| `test_string_indexing.bas` | String[index] character access |
| `test_arrays_dim.bas` | DIM and array operations |
| `test_data_read.bas` | DATA and READ statements |
| `test_system_functions.bas` | MEM, TIME$ system functions |
| `test_print_statement.bas` | PRINT with various formats |
| `test_rem_comments.bas` | REM and ' comment syntax |
| `test_colon_separator.bas` | : statement separator |
| `test_not_operator.bas` | NOT logical operator |
| `test_pragma_statement.bas` | @PRAGMA directives |
| `test_complex_expressions.bas` | Nested and complex expressions |
| `test_edge_cases.bas` | Boundary conditions |
| `test_end_statement.bas` | END statement |

## Test Structure

Each test follows this pattern:

```basic
REM ========================================================================
REM Test: [Feature Name]
REM Description: [What is being tested]
REM Expected: [Expected results]
REM ========================================================================

10 PRINT "=== Testing [Feature] ==="
20 PRINT ""

REM Test case 1
30 PRINT "Testing [specific case]:"
40 REM ... test code ...
50 IF [condition] != [expected] THEN GOTO 9000

REM More test cases...

900 PRINT ""
910 PRINT "=== All [Feature] Tests PASSED ==="
920 END

9000 PRINT "ERROR: Test failed!"
9010 END
```

## Next Steps

1. Read `README.md` for detailed test documentation
2. Read `TEST_SUITE_SUMMARY.md` for overview
3. Run the full test suite: `./run_all_tests.sh`
4. Examine individual test files to learn BASIC syntax
5. Use tests as examples for your own BASIC programs

## Contributing

To add a new test:

1. Create `test_[feature_name].bas` in `test/system/`
2. Follow the test structure pattern above
3. Add test to `run_all_tests.sh` TEST_FILES array
4. Update `README.md` with test documentation
5. Run test suite to verify all tests pass

## Support

For issues or questions:
- Check `README.md` for detailed documentation
- Review existing test files for examples
- Consult the GD-BASIC project documentation
