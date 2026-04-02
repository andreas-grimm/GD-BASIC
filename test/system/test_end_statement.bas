REM ========================================================================
REM Test: END Statement
REM Description: Tests the END statement for program termination
REM Expected: Program should terminate at END statement
REM ========================================================================

10 PRINT "=== Testing END Statement ==="
20 PRINT ""

30 PRINT "Before END statement"
40 PRINT "This is the last line before END"
50 END

60 PRINT "ERROR: This line should never be reached!"
70 PRINT "END statement did not work correctly"
