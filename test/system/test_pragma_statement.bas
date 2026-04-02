REM ========================================================================
REM Test: PRAGMA Statement
REM Description: Tests @PRAGMA statement for setting interpreter options
REM Expected: PRAGMA should execute without errors
REM ========================================================================

10 PRINT "=== Testing PRAGMA Statement ==="
20 PRINT ""

REM Test PRAGMA for log level
30 @PRAGMA LOG_LEVEL=ERROR
40 PRINT "PRAGMA LOG_LEVEL=ERROR executed"

50 @PRAGMA LOG_LEVEL=INFO
60 PRINT "PRAGMA LOG_LEVEL=INFO executed"

70 PRINT ""
80 PRINT "=== All PRAGMA Statement Tests PASSED ==="
90 END
