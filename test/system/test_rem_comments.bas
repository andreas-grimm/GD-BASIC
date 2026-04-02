REM ========================================================================
REM Test: REM and Comment Statements
REM Description: Tests REM keyword and ' comment syntax
REM Expected: Comments should be ignored and not affect execution
REM ========================================================================

10 PRINT "=== Testing REM and Comments ==="
20 PRINT ""

REM This is a REM comment
30 PRINT "Testing REM Statement"
40 REM This line should be completely ignored
50 PRINT "REM statement works"

' This is a tick comment
60 PRINT "Testing ' Comment"
70 ' This line should also be ignored
80 PRINT "' comment works"

90 x% = 5 REM inline comment
100 PRINT "Inline REM: x% = "; x%
110 IF x% != 5 THEN GOTO 9000

120 y% = 10 ' inline tick comment
130 PRINT "Inline ': y% = "; y%
140 IF y% != 10 THEN GOTO 9000

150 PRINT ""
160 PRINT "=== All REM and Comment Tests PASSED ==="
170 END

9000 PRINT "ERROR: Test failed!"
9010 END
