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
120 PRINT "Inline REM: x% = ";
130 PRINT x%
140 IF x% != 5 THEN 9000
150 y% = 10 ' inline tick comment
160 PRINT "Inline ': y% = ";
170 PRINT y%
180 IF y% != 10 THEN 9000
190 PRINT ""
200 PRINT "=== All REM and Comment Tests PASSED ==="
210 END
9000 PRINT "ERROR: Test failed!"
9010 END
