1 REM ========================================================================
2 REM Test: Logical Operators
3 REM Description: Tests logical operators (AND, OR, NOT)
4 REM Expected: All logical operations should evaluate correctly
5 REM ========================================================================

10 PRINT "=== Testing Logical Operators ==="
20 PRINT ""

30 REM Test AND operator
40 PRINT "Testing AND Operator:"
50 IF 1 == 1 AND 2 == 2 THEN PRINT "TRUE AND TRUE = TRUE (correct)"
60 IF 1 == 1 AND 2 == 2 THEN GOTO 70
65 GOTO 9000
70 IF 1 == 1 AND 2 == 3 THEN GOTO 9000
80 PRINT "TRUE AND FALSE = FALSE (correct)"
90 IF 1 == 2 AND 2 == 2 THEN GOTO 9000
100 PRINT "FALSE AND TRUE = FALSE (correct)"

110 REM Test OR operator
120 PRINT "Testing OR Operator:"
130 IF 1 == 1 OR 2 == 3 THEN PRINT "TRUE OR FALSE = TRUE (correct)"
140 IF 1 == 1 OR 2 == 3 THEN GOTO 150
145 GOTO 9000
150 IF 1 == 2 OR 2 == 2 THEN PRINT "FALSE OR TRUE = TRUE (correct)"
160 IF 1 == 2 OR 2 == 2 THEN GOTO 170
165 GOTO 9000
170 IF 1 == 2 OR 2 == 3 THEN GOTO 9000
180 PRINT "FALSE OR FALSE = FALSE (correct)"

190 REM Test NOT operator
200 PRINT "Testing NOT Operator:"
210 a! = 1 == 1
220 PRINT "a! = "; a!
230 b! = NOT a!
240 IF b! THEN GOTO 9000
250 PRINT "NOT TRUE = FALSE (correct)"
260 c! = 1 == 2
270 PRINT "c! = "; c!
280 d! = NOT c!
290 IF d! THEN PRINT "NOT FALSE = TRUE (correct)"
300 IF d! THEN GOTO 310
305 GOTO 9000

310 PRINT ""
320 PRINT "=== All Logical Operator Tests PASSED ==="
330 END

9000 PRINT "ERROR: Test failed!"
9010 END
