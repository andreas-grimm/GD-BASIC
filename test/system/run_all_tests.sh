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
