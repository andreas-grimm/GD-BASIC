#!/bin/bash

################################################################################
# GD-BASIC System Test Runner
# 
# This script runs all BASIC test programs in the test/system directory.
# Each test is executed using the GD-BASIC interpreter.
# If any test fails (non-zero exit code), the script stops and reports the error.
#
# Usage: ./run_all_tests.sh
#
# Prerequisites:
# - Maven must be installed
# - The GD-BASIC project must be built (mvn package)
################################################################################

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Script configuration
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
TEST_DIR="$SCRIPT_DIR"
JAR_PATH="$PROJECT_ROOT/target/BASIC-*-jar-with-dependencies.jar"

# Counters
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# Function to print section headers
print_header() {
    echo ""
    echo -e "${BLUE}========================================================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}========================================================================${NC}"
    echo ""
}

# Function to print test status
print_test_start() {
    echo -e "${YELLOW}Running test: $1${NC}"
}

print_test_pass() {
    echo -e "${GREEN}✓ PASSED: $1${NC}"
    echo ""
}

print_test_fail() {
    echo -e "${RED}✗ FAILED: $1${NC}"
    echo -e "${RED}Exit code: $2${NC}"
    echo ""
}

# Function to check if JAR exists
check_jar() {
    if ! ls $JAR_PATH 1> /dev/null 2>&1; then
        echo -e "${RED}ERROR: JAR file not found!${NC}"
        echo "Expected location: $JAR_PATH"
        echo ""
        echo "Please build the project first:"
        echo "  cd $PROJECT_ROOT"
        echo "  mvn clean package"
        exit 1
    fi
}

# Function to run a single test
run_test() {
    local test_file="$1"
    local test_name=$(basename "$test_file" .bas)
    
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    
    print_test_start "$test_name"
    
    # Run the test and capture output. A test fails when the interpreter exits with an error (exit code not 0),
    # or when the program itself prints its failure marker "ERROR: Test failed" (Phase 7).
    local output_file=$(mktemp)
    java -jar $JAR_PATH -r "$test_file" > "$output_file" 2>&1
    local exit_code=$?
    if [ $exit_code -eq 0 ] && grep -q "ERROR: Test failed" "$output_file"; then
        exit_code=2
    fi
    rm -f "$output_file"
    
    if [ $exit_code -eq 0 ]; then
        PASSED_TESTS=$((PASSED_TESTS + 1))
        print_test_pass "$test_name"
        return 0
    else
        FAILED_TESTS=$((FAILED_TESTS + 1))
        print_test_fail "$test_name" "$exit_code"
        
        # Show actual output for debugging
        echo -e "${YELLOW}Test output:${NC}"
        java -jar $JAR_PATH -r "$test_file"
        echo ""
        
        return 1
    fi
}

# Function to run an example program that needs keyboard input and compare its output with a saved result.
# Arguments: program file, input file (fed to standard input; /dev/null if empty), expected output file,
# expected exit code (default 0; negative tests that must stop with an error use 1).
run_example_test() {
    local program_file="$1"
    local input_file="$2"
    local expected_file="$3"
    local expected_code="${4:-0}"
    local test_name=$(basename "$program_file" .bas)
    if [ -z "$input_file" ]; then
        input_file="/dev/null"
    fi
    local raw_file=$(mktemp)
    local actual_file=$(mktemp)

    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    print_test_start "$test_name (with input file, compared with expected output)"

    java -jar $JAR_PATH -r -q "$program_file" < "$input_file" > "$raw_file" 2>&1
    local exit_code=$?

    # Remove colour codes, timestamps and stack-trace lines before comparing
    sed -E 's/\x1b\[[0-9;]*m//g' "$raw_file" | grep -v -E '^[0-9]{4}-[0-9]{2}-[0-9]{2} |^[[:space:]]+at ' > "$actual_file"

    if [ $exit_code -eq "$expected_code" ] && diff -q "$expected_file" "$actual_file" > /dev/null; then
        PASSED_TESTS=$((PASSED_TESTS + 1))
        print_test_pass "$test_name"
        rm -f "$raw_file" "$actual_file"
        return 0
    fi

    FAILED_TESTS=$((FAILED_TESTS + 1))
    print_test_fail "$test_name" "$exit_code"
    echo -e "${YELLOW}Difference between expected and actual output:${NC}"
    diff "$expected_file" "$actual_file" || true
    echo ""
    rm -f "$raw_file" "$actual_file"
    return 1
}

# Function to check that a BASIC program, compiled to Java and run as a jar, prints exactly what the interpreter prints.
# Arguments: program file. The compiler is driven through the editor commands (LOAD, COMPILE, EXIT) read from standard
# input. Lines that depend on the clock, memory or random numbers (RND, MEM, TIME$) are left out of the comparison.
run_compiler_test() {
    local program_file="$1"
    local test_name=$(basename "$program_file" .bas)
    local work_dir=$(mktemp -d)
    local interp_file="$work_dir/interpreter.txt"
    local compiled_file="$work_dir/compiled.txt"
    local filter='s/\x1b\[[0-9;]*m//g'
    # Standard input: a file named like the program with the extension .in (for INPUT tests), otherwise nothing
    local stdin_file="/dev/null"
    if [ -f "${program_file%.bas}.in" ]; then
        stdin_file="${program_file%.bas}.in"
    fi

    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    print_test_start "$test_name (compiled to Java, compared with the interpreter)"

    java -jar "$JAR_FILE" -r -q "$program_file" < "$stdin_file" 2>&1 \
        | sed -E "$filter" | grep -v -E '^[0-9]{4}-[0-9]{2}-[0-9]{2} |^[[:space:]]+at |^$|^(RND\(\)|MEM|TIME\$) ' > "$interp_file"

    (cd "$work_dir" && printf 'LOAD %s\nCOMPILE %s\nEXIT\n' "$program_file" "$test_name" \
        | java -jar "$JAR_FILE" > "$work_dir/compile.log" 2>&1)

    if [ ! -f "$work_dir/$test_name.jar" ]; then
        FAILED_TESTS=$((FAILED_TESTS + 1))
        print_test_fail "$test_name" "compile"
        echo -e "${YELLOW}Compiler output:${NC}"
        sed -E "$filter" "$work_dir/compile.log"
        rm -rf "$work_dir"
        return 1
    fi

    (cd "$work_dir" && java -jar "$test_name.jar" < "$stdin_file" 2>&1) \
        | sed -E "$filter" | grep -v -E '^[0-9]{4}-[0-9]{2}-[0-9]{2} |^[[:space:]]+at |^$|^(RND\(\)|MEM|TIME\$) ' > "$compiled_file"

    if diff -q "$interp_file" "$compiled_file" > /dev/null; then
        PASSED_TESTS=$((PASSED_TESTS + 1))
        print_test_pass "$test_name"
        rm -rf "$work_dir"
        return 0
    fi

    FAILED_TESTS=$((FAILED_TESTS + 1))
    print_test_fail "$test_name" "output"
    echo -e "${YELLOW}Difference between interpreter (<) and compiled program (>):${NC}"
    diff "$interp_file" "$compiled_file" || true
    echo ""
    rm -rf "$work_dir"
    return 1
}

# Function to check that a program stops with an error. Both the interpreter and the compiled program must exit with
# a non-zero code and print the expected text. Arguments: program file, expected text.
run_compiler_error_test() {
    local program_file="$1"
    local expected_text="$2"
    local test_name=$(basename "$program_file" .bas)
    local work_dir=$(mktemp -d)
    local filter='s/\x1b\[[0-9;]*m//g'
    local failed=0

    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    print_test_start "$test_name (must stop with an error, interpreter and compiled)"

    java -jar "$JAR_FILE" -r -q "$program_file" < /dev/null > "$work_dir/interpreter.txt" 2>&1
    local interp_code=$?
    (cd "$work_dir" && printf 'LOAD %s\nCOMPILE %s\nEXIT\n' "$program_file" "$test_name" \
        | java -jar "$JAR_FILE" > "$work_dir/compile.log" 2>&1)
    local compiled_code=-1
    if [ -f "$work_dir/$test_name.jar" ]; then
        (cd "$work_dir" && java -jar "$test_name.jar" < /dev/null > "$work_dir/compiled.txt" 2>&1)
        compiled_code=$?
    fi

    if [ $interp_code -eq 0 ] || ! sed -E "$filter" "$work_dir/interpreter.txt" | grep -q "$expected_text"; then
        echo "  interpreter did not stop with: $expected_text"; failed=1
    fi
    if [ $compiled_code -eq 0 ] || ! sed -E "$filter" "$work_dir/compiled.txt" 2>/dev/null | grep -q "$expected_text"; then
        echo "  compiled program did not stop with: $expected_text"; failed=1
    fi

    rm -rf "$work_dir"
    if [ $failed -eq 0 ]; then
        PASSED_TESTS=$((PASSED_TESTS + 1))
        print_test_pass "$test_name"
        return 0
    fi
    FAILED_TESTS=$((FAILED_TESTS + 1))
    print_test_fail "$test_name" "error"
    return 1
}

# Function to check the COMPILE usage message: COMPILE without a name, and without -o, must print the message and must
# not write any file.
run_compile_usage_test() {
    local work_dir=$(mktemp -d)
    local filter='s/\x1b\[[0-9;]*m//g'

    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    print_test_start "compile_no_name (COMPILE without a name and without -o prints the usage message)"

    (cd "$work_dir" && printf 'COMPILE\nEXIT\n' | java -jar "$JAR_FILE" > "$work_dir/out.txt" 2>&1)

    local files_written=$(ls -A "$work_dir" | grep -v '^out.txt$' | wc -l | tr -d ' ')
    if sed -E "$filter" "$work_dir/out.txt" | grep -q "COMPILE: give a target name" && [ "$files_written" -eq 0 ]; then
        rm -rf "$work_dir"
        PASSED_TESTS=$((PASSED_TESTS + 1))
        print_test_pass "compile_no_name"
        return 0
    fi
    echo "  expected the usage message and no files; files written: $files_written"
    rm -rf "$work_dir"
    FAILED_TESTS=$((FAILED_TESTS + 1))
    print_test_fail "compile_no_name" "usage"
    return 1
}

# Function to check the start directory: GETCWD() must return the directory in which the program was started, as an
# absolute path, and CHDIR must change it (a relative CHDIR is taken from the current directory).
run_start_directory_test() {
    local work_dir=$(mktemp -d)
    local expected=$(cd "$work_dir" && pwd -P)
    local filter='s/\x1b\[[0-9;]*m//g'
    local failed=0

    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    print_test_start "start_directory (GETCWD is the start directory, absolute)"

    mkdir "$work_dir/sub"
    printf '10 PRINT GETCWD()\n20 CHDIR "sub"\n30 PRINT GETCWD()\n40 CHDIR ".."\n50 PRINT GETCWD()\n60 END\n' \
        > "$work_dir/start.bas"
    (cd "$work_dir" && java -jar "$JAR_FILE" -r -q start.bas < /dev/null > "$work_dir/out.txt" 2>&1)
    local output=$(sed -E "$filter" "$work_dir/out.txt")

    if ! echo "$output" | grep -qx -F "$expected"; then
        echo "  start directory is not printed as: $expected"; failed=1
    fi
    if ! echo "$output" | grep -qx -F "$expected/sub"; then
        echo "  CHDIR \"sub\" did not give: $expected/sub"; failed=1
    fi

    rm -rf "$work_dir"
    if [ $failed -eq 0 ]; then
        PASSED_TESTS=$((PASSED_TESTS + 1))
        print_test_pass "start_directory"
        return 0
    fi
    FAILED_TESTS=$((FAILED_TESTS + 1))
    print_test_fail "start_directory" "error"
    return 1
}

# Main execution
main() {
    print_header "GD-BASIC System Test Suite"
    
    echo "Project Root: $PROJECT_ROOT"
    echo "Test Directory: $TEST_DIR"
    echo ""
    
    # Check if JAR exists
    check_jar
    
    # Find JAR file
    JAR_FILE=$(ls $JAR_PATH | head -1)
    echo "Using JAR: $JAR_FILE"
    echo ""
    
    # Get list of test files in order
    TEST_FILES=(
        "test_arithmetic_operators.bas"
        "test_comparison_operators.bas"
        "test_logical_operators.bas"
        "test_bitwise_operators.bas"
        "test_assignment_operators.bas"
        "test_variable_types.bas"
        "test_string_assignment.bas"
        "test_if_then_else.bas"
        "test_block_if_else.bas"
        "test_for_next_loop.bas"
        "test_for_nested_restart.bas"
        "test_for_first_statement.bas"
        "test_do_until_loop.bas"
        "test_while_loop.bas"
        "test_while_nested.bas"
        "test_goto_gosub_return.bas"
        "test_math_functions.bas"
        "test_string_functions.bas"
        "test_string_indexing.bas"
        "test_arrays_dim.bas"
        "test_data_read.bas"
        "test_system_functions.bas"
        "test_print_statement.bas"
        "test_rem_comments.bas"
        "test_colon_separator.bas"
        "test_not_operator.bas"
        "test_pragma_statement.bas"
        "test_complex_expressions.bas"
        "test_edge_cases.bas"
        "test_end_statement.bas"
        "test_defs_functions.bas"
        "test_file_io.bas"
        "test_mixed_tests_1.bas"
        "test_mixed_tests_2.bas"
        "test_mixed_tests_3.bas"
        "test_mixed_tests_4.bas"
        "test_mixed_tests_5.bas"
        "test_large_program.bas"
        # Added in Phase 7: programs that were not run by the runner before
        "test_character_level_io.bas"
        "test_file_character_transform.bas"
        "test_file_copy.bas"
        "test_file_eof.bas"
        "test_file_exists.bas"
        "test_file_integration.bas"
        "test_file_operations_comprehensive.bas"
        "test_file_peek.bas"
        "test_file_rename.bas"
        "test_file_rewind.bas"
        "test_file_search_lookahead.bas"
        "test_on_gosub_basic.bas"
        "test_on_gosub_simple.bas"
        "test_on_goto_basic.bas"
        "test_on_goto_simple.bas"
        "test_on_outofrange.bas"
        # PI to 32 digits with BCD (#) variables: Gauss-Legendre and Machin's formula
        "test_bcd_pi.bas"
    )
    
    # Run each test
    for test_file in "${TEST_FILES[@]}"; do
        full_path="$TEST_DIR/$test_file"
        
        if [ ! -f "$full_path" ]; then
            echo -e "${YELLOW}Warning: Test file not found: $test_file${NC}"
            echo ""
            continue
        fi
        
        run_test "$full_path"
        
        # Stop on first failure
        if [ $? -ne 0 ]; then
            print_header "TEST SUITE FAILED"
            echo -e "${RED}A test failed. Stopping execution.${NC}"
            echo ""
            echo "Summary:"
            echo "  Total tests run: $TOTAL_TESTS"
            echo "  Passed: $PASSED_TESTS"
            echo "  Failed: $FAILED_TESTS"
            echo ""
            exit 1
        fi
    done
    
    # Phase 7: BCD (#) tests. Each program's output is compared with a saved expected output file.
    # The last four programs are negative tests: they must stop with the stated error message.
    # test_bcd_bits: shifts are defined for INTEGER values only (owner decision Q6), so a BCD shift must fail.
    BCD_TESTS=(
        "test_bcd_arithmetic"
        "test_bcd_functions"
        "test_bcd_output"
        "test_bcd_bits"
        "test_bcd_overflow"
        "test_bcd_divzero"
        "test_bcd_array_rejected"
    )
    for bcd_test in "${BCD_TESTS[@]}"; do
        expected_code=0
        case "$bcd_test" in
            test_bcd_overflow|test_bcd_divzero|test_bcd_array_rejected|test_bcd_bits) expected_code=1 ;;
        esac
        run_example_test "$TEST_DIR/$bcd_test.bas" "" "$TEST_DIR/examples/$bcd_test.expected" "$expected_code"
        if [ $? -ne 0 ]; then
            print_header "TEST SUITE FAILED"
            echo -e "${RED}A test failed. Stopping execution.${NC}"
            exit 1
        fi
    done

    # Example program that needs keyboard input (result compared with saved expected output)
    run_example_test "$PROJECT_ROOT/doc/GD_Basic_Examples/BSearch.bas" \
        "$TEST_DIR/examples/BSearch.input" "$TEST_DIR/examples/BSearch.expected"
    if [ $? -ne 0 ]; then
        print_header "TEST SUITE FAILED"
        echo -e "${RED}A test failed. Stopping execution.${NC}"
        exit 1
    fi

    # Compiler tests: each program is compiled to Java, built into a jar, run, and its output compared with the
    # interpreter's output. The programs in this list are the ones the compiler supports (see doc/COMPILER.md).
    COMPILER_TESTS=(
        "compiler/compiler_values.bas"
        "compiler/compiler_flow.bas"
        "compiler/compiler_unary_minus.bas"
        "compiler/compiler_input.bas"
        "compiler/compiler_system.bas"
        "compiler/compiler_file_io.bas"
        "compiler/compiler_directories.bas"
        "test_arithmetic_operators.bas"
        "test_comparison_operators.bas"
        "test_logical_operators.bas"
        "test_bitwise_operators.bas"
        "test_assignment_operators.bas"
        "test_variable_types.bas"
        "test_string_assignment.bas"
        "test_if_then_else.bas"
        "test_block_if_else.bas"
        "test_for_next_loop.bas"
        "test_for_nested_restart.bas"
        "test_for_first_statement.bas"
        "test_do_until_loop.bas"
        "test_while_loop.bas"
        "test_while_nested.bas"
        "test_goto_gosub_return.bas"
        "test_string_functions.bas"
        "test_string_indexing.bas"
        "test_print_statement.bas"
        "test_rem_comments.bas"
        "test_colon_separator.bas"
        "test_not_operator.bas"
        "test_pragma_statement.bas"
        "test_complex_expressions.bas"
        "test_edge_cases.bas"
        "test_end_statement.bas"
        "test_defs_functions.bas"
        "test_mixed_tests_4.bas"
        "test_mixed_tests_5.bas"
        "test_large_program.bas"
        "test_on_gosub_basic.bas"
        "test_on_gosub_simple.bas"
        "test_on_goto_basic.bas"
        "test_on_goto_simple.bas"
        "test_on_outofrange.bas"
        "test_bcd_pi.bas"
    )
    for compiler_test in "${COMPILER_TESTS[@]}"; do
        run_compiler_test "$TEST_DIR/$compiler_test"
        if [ $? -ne 0 ]; then
            print_header "TEST SUITE FAILED"
            echo -e "${RED}A compiler test failed. Stopping execution.${NC}"
            exit 1
        fi
    done

    run_compile_usage_test
    if [ $? -ne 0 ]; then
        print_header "TEST SUITE FAILED"
        exit 1
    fi

    # Error tests: the program must stop with the stated message (interpreter and compiled program)
    run_compiler_error_test "$TEST_DIR/compiler/compiler_error_unary_string.bas" "Cannot apply unary minus to a string"
    if [ $? -ne 0 ]; then
        print_header "TEST SUITE FAILED"
        exit 1
    fi
    run_compiler_error_test "$TEST_DIR/compiler/compiler_error_mkdir_exists.bas" "already exists"
    if [ $? -ne 0 ]; then
        print_header "TEST SUITE FAILED"
        exit 1
    fi

    run_start_directory_test
    if [ $? -ne 0 ]; then
        print_header "TEST SUITE FAILED"
        exit 1
    fi

    # Print summary
    print_header "TEST SUITE COMPLETED SUCCESSFULLY"
    
    # Cleanup temporary files
    echo "Cleaning up temporary files..."
    rm -f test_io.txt
    echo ""
    
    echo -e "${GREEN}All tests passed!${NC}"
    echo ""
    echo "Summary:"
    echo "  Total tests: $TOTAL_TESTS"
    echo "  Passed: $PASSED_TESTS"
    echo "  Failed: $FAILED_TESTS"
    echo ""
    
    exit 0
}

# Run main function
main
