1 REM ========================================================================
2 REM Test: PRAGMA Statement
3 REM Description: Tests @PRAGMA statement for setting interpreter options
4 REM Expected: PRAGMA should execute without errors
5 REM ========================================================================
10 PRINT "=== Testing PRAGMA Statement ==="
20 PRINT ""
30 REM Test PRAGMA for log level
40 @PRAGMA( "LOG_LEVEL" = "ERROR" )
50 PRINT "PRAGMA LOG_LEVEL=ERROR executed"
60 @PRAGMA( "LOG_LEVEL" = "INFO" )
70 PRINT "PRAGMA LOG_LEVEL=INFO executed"
80 PRINT ""
90 PRINT "=== All PRAGMA Statement Tests PASSED ==="
100 END
