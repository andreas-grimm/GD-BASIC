REM ========================================================================
REM Test: Comparison Operators
REM Description: Tests all comparison operators (==, !=, <, >, <=, >=)
REM Expected: All comparisons should evaluate correctly
REM ========================================================================

10 PRINT "=== Testing Comparison Operators ==="
20 PRINT ""

REM Test equality
30 PRINT "Testing Equality (==):"
40 IF 5 == 5 THEN PRINT "5 == 5: TRUE (correct)"
50 IF 5 == 5 THEN GOTO 60
55 GOTO 9000
60 IF 5 == 6 THEN GOTO 9000
70 PRINT "5 == 6: FALSE (correct)"

REM Test inequality
80 PRINT "Testing Inequality (!=):"
90 IF 5 != 6 THEN PRINT "5 != 6: TRUE (correct)"
100 IF 5 != 6 THEN GOTO 110
105 GOTO 9000
110 IF 5 != 5 THEN GOTO 9000
120 PRINT "5 != 5: FALSE (correct)"

REM Test less than
130 PRINT "Testing Less Than (<):"
140 IF 3 < 5 THEN PRINT "3 < 5: TRUE (correct)"
150 IF 3 < 5 THEN GOTO 160
155 GOTO 9000
160 IF 5 < 3 THEN GOTO 9000
170 PRINT "5 < 3: FALSE (correct)"

REM Test greater than
180 PRINT "Testing Greater Than (>):"
190 IF 7 > 4 THEN PRINT "7 > 4: TRUE (correct)"
200 IF 7 > 4 THEN GOTO 210
205 GOTO 9000
210 IF 4 > 7 THEN GOTO 9000
220 PRINT "4 > 7: FALSE (correct)"

REM Test less than or equal
230 PRINT "Testing Less Than or Equal (<=):"
240 IF 3 <= 5 THEN PRINT "3 <= 5: TRUE (correct)"
250 IF 5 <= 5 THEN PRINT "5 <= 5: TRUE (correct)"
260 IF 3 <= 5 THEN GOTO 270
265 GOTO 9000
270 IF 6 <= 5 THEN GOTO 9000
280 PRINT "6 <= 5: FALSE (correct)"

REM Test greater than or equal
290 PRINT "Testing Greater Than or Equal (>=):"
300 IF 7 >= 4 THEN PRINT "7 >= 4: TRUE (correct)"
310 IF 4 >= 4 THEN PRINT "4 >= 4: TRUE (correct)"
320 IF 7 >= 4 THEN GOTO 330
325 GOTO 9000
330 IF 3 >= 5 THEN GOTO 9000
340 PRINT "3 >= 5: FALSE (correct)"

350 PRINT ""
360 PRINT "=== All Comparison Operator Tests PASSED ==="
370 END

9000 PRINT "ERROR: Test failed!"
9010 END
