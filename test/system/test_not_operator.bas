1 REM ========================================================================
2 REM Test: NOT Operator
3 REM Description: Tests the NOT logical operator
4 REM Expected: NOT should correctly negate boolean values
5 REM ========================================================================
10 PRINT "=== Testing NOT Operator ==="
20 PRINT ""
30 REM Test NOT with boolean variable
40 PRINT "Testing NOT with Boolean Variable:"
50 a! = 1 == 1
60 PRINT "a! = ";
70 PRINT a!
80 b! = NOT a!
90 IF b! THEN 9000
100 PRINT "NOT TRUE = FALSE (correct)"
110 c! = 1 == 2
120 PRINT "c! = ";
130 PRINT c!
140 d! = NOT c!
150 PRINT "NOT c! = ";
160 PRINT d!
170 IF NOT d! THEN 9000
180 PRINT "NOT FALSE = TRUE (correct)"
190 REM Test NOT in IF statement
200 PRINT "Testing NOT in IF Statement:"
210 x% = 5
220 IF NOT (x% == 10) THEN
221  PRINT "x% is not 10 (correct)"
222 END-IF
230 IF NOT (x% == 10) THEN 240
235 GOTO 9000
240 PRINT ""
250 PRINT "=== All NOT Operator Tests PASSED ==="
260 END
9000 PRINT "ERROR: Test failed!"
9010 END
