REM ========================================================================
REM Test: NOT Operator
REM Description: Tests the NOT logical operator
REM Expected: NOT should correctly negate boolean values
REM ========================================================================

10 PRINT "=== Testing NOT Operator ==="
20 PRINT ""

REM Test NOT with boolean variable
30 PRINT "Testing NOT with Boolean Variable:"
40 a! = 1 == 1
50 PRINT "a! = "; a!
60 b! = NOT a!
70 PRINT "NOT a! = "; b!
80 IF b! THEN GOTO 9000
90 PRINT "NOT TRUE = FALSE (correct)"

100 c! = 1 == 2
110 PRINT "c! = "; c!
120 d! = NOT c!
130 PRINT "NOT c! = "; d!
140 IF NOT d! THEN GOTO 9000
150 PRINT "NOT FALSE = TRUE (correct)"

REM Test NOT in IF statement
160 PRINT "Testing NOT in IF Statement:"
170 x% = 5
180 IF NOT (x% == 10) THEN PRINT "x% is not 10 (correct)"
190 IF NOT (x% == 10) THEN GOTO 200
195 GOTO 9000

200 PRINT ""
210 PRINT "=== All NOT Operator Tests PASSED ==="
220 END

9000 PRINT "ERROR: Test failed!"
9010 END
