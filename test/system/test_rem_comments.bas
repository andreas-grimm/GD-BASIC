1 REM ========================================================================
2 REM Test: REM and Comment Statements
3 REM Description: Tests REM keyword and ' comment syntax
4 REM Expected: Comments should be ignored and not affect execution
5 REM ========================================================================

10 PRINT "=== Testing REM and Comments ==="
20 PRINT ""

30 REM This is a REM comment
40 PRINT "Testing REM Statement"
50 REM This line should be completely ignored
60 PRINT "REM statement works"

70 ' This is a tick comment
80 PRINT "Testing ' Comment"
90 ' This line should also be ignored
100 PRINT "' comment works"

110 x% = 5 REM inline comment
120 PRINT "Inline REM: x% = "; x%
130 IF x% != 5 THEN GOTO 9000

140 y% = 10 ' inline tick comment
150 PRINT "Inline ': y% = "; y%
160 IF y% != 10 THEN GOTO 9000

170 PRINT ""
180 PRINT "=== All REM and Comment Tests PASSED ==="
190 END

9000 PRINT "ERROR: Test failed!"
9010 END
