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
60 PRINT "a! = "; a!
70 b! = NOT a!
80 IF b! THEN GOTO 9000
90 PRINT "NOT TRUE = FALSE (correct)"

100 c! = 1 == 2
110 PRINT "c! = "; c!
120 d! = NOT c!
130 PRINT "NOT c! = "; d!
140 IF NOT d! THEN GOTO 9000
150 PRINT "NOT FALSE = TRUE (correct)"

160 REM Test NOT in IF statement
170 PRINT "Testing NOT in IF Statement:"
180 x% = 5
190 IF NOT (x% == 10) THEN PRINT "x% is not 10 (correct)"
200 IF NOT (x% == 10) THEN GOTO 210
205 GOTO 9000

210 PRINT ""
220 PRINT "=== All NOT Operator Tests PASSED ==="
230 END

9000 PRINT "ERROR: Test failed!"
9010 END
