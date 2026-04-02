REM ========================================================================
REM Test: Logical Operators
REM Description: Tests logical operators (AND, OR, NOT)
REM Expected: All logical operations should evaluate correctly
REM ========================================================================

10 PRINT "=== Testing Logical Operators ==="
20 PRINT ""

REM Test AND operator
30 PRINT "Testing AND Operator:"
40 IF 1 == 1 AND 2 == 2 THEN PRINT "TRUE AND TRUE = TRUE (correct)"
50 IF 1 == 1 AND 2 == 2 THEN GOTO 60
55 GOTO 9000
60 IF 1 == 1 AND 2 == 3 THEN GOTO 9000
70 PRINT "TRUE AND FALSE = FALSE (correct)"
80 IF 1 == 2 AND 2 == 2 THEN GOTO 9000
90 PRINT "FALSE AND TRUE = FALSE (correct)"

REM Test OR operator
100 PRINT "Testing OR Operator:"
110 IF 1 == 1 OR 2 == 3 THEN PRINT "TRUE OR FALSE = TRUE (correct)"
120 IF 1 == 1 OR 2 == 3 THEN GOTO 130
125 GOTO 9000
130 IF 1 == 2 OR 2 == 2 THEN PRINT "FALSE OR TRUE = TRUE (correct)"
140 IF 1 == 2 OR 2 == 2 THEN GOTO 150
145 GOTO 9000
150 IF 1 == 2 OR 2 == 3 THEN GOTO 9000
160 PRINT "FALSE OR FALSE = FALSE (correct)"

REM Test NOT operator
170 PRINT "Testing NOT Operator:"
180 a! = 1 == 1
190 b! = NOT a!
200 IF b! THEN GOTO 9000
210 PRINT "NOT TRUE = FALSE (correct)"
220 c! = 1 == 2
230 d! = NOT c!
240 IF d! THEN PRINT "NOT FALSE = TRUE (correct)"
250 IF d! THEN GOTO 260
255 GOTO 9000

260 PRINT ""
270 PRINT "=== All Logical Operator Tests PASSED ==="
280 END

9000 PRINT "ERROR: Test failed!"
9010 END
