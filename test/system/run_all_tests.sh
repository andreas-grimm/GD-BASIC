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
    
    # Run the test and capture output
    java -jar $JAR_PATH "$test_file" > /dev/null 2>&1
    local exit_code=$?
    
    if [ $exit_code -eq 0 ]; then
        PASSED_TESTS=$((PASSED_TESTS + 1))
        print_test_pass "$test_name"
        return 0
    else
        FAILED_TESTS=$((FAILED_TESTS + 1))
        print_test_fail "$test_name" "$exit_code"
        
        # Show actual output for debugging
        echo -e "${YELLOW}Test output:${NC}"
        java -jar $JAR_PATH "$test_file"
        echo ""
        
        return 1
    fi
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
        "test_for_next_loop.bas"
        "test_do_until_loop.bas"
        "test_while_loop.bas"
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
        "test_mixed_tests_1.bas"
        "test_mixed_tests_2.bas"
        "test_mixed_tests_3.bas"
        "test_mixed_tests_4.bas"
        "test_mixed_tests_5.bas"
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
    
    # Print summary
    print_header "TEST SUITE COMPLETED SUCCESSFULLY"
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
