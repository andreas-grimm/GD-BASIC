1 REM ========================================================================
2 REM Test: Comparison Operators
3 REM Description: Tests all comparison operators (==, !=, <, >, <=, >=)
4 REM Expected: All comparisons should evaluate correctly
5 REM ========================================================================

10 PRINT "=== Testing Comparison Operators ==="
20 PRINT ""

30 REM Test equality
40 PRINT "Testing Equality (==):"
50 IF 5 == 5 THEN PRINT "5 == 5: TRUE (correct)"
60 IF 5 == 5 THEN GOTO 70
65 GOTO 9000
70 IF 5 == 6 THEN GOTO 9000
80 PRINT "5 == 6: FALSE (correct)"

90 REM Test inequality
100 PRINT "Testing Inequality (!=):"
110 IF 5 != 6 THEN PRINT "5 != 6: TRUE (correct)"
120 IF 5 != 6 THEN GOTO 130
125 GOTO 9000
130 IF 5 != 5 THEN GOTO 9000
140 PRINT "5 != 5: FALSE (correct)"

150 REM Test less than
160 PRINT "Testing Less Than (<):"
170 IF 3 < 5 THEN PRINT "3 < 5: TRUE (correct)"
180 IF 3 < 5 THEN GOTO 190
185 GOTO 9000
190 IF 5 < 3 THEN GOTO 9000
200 PRINT "5 < 3: FALSE (correct)"

210 REM Test greater than
220 PRINT "Testing Greater Than (>):"
230 IF 7 > 4 THEN PRINT "7 > 4: TRUE (correct)"
240 IF 7 > 4 THEN GOTO 250
245 GOTO 9000
250 IF 4 > 7 THEN GOTO 9000
260 PRINT "4 > 7: FALSE (correct)"

270 REM Test less than or equal
280 PRINT "Testing Less Than or Equal (<=):"
290 IF 3 <= 5 THEN PRINT "3 <= 5: TRUE (correct)"
300 IF 5 <= 5 THEN PRINT "5 <= 5: TRUE (correct)"
310 IF 3 <= 5 THEN GOTO 320
315 GOTO 9000
320 IF 6 <= 5 THEN GOTO 9000
330 PRINT "6 <= 5: FALSE (correct)"

340 REM Test greater than or equal
350 PRINT "Testing Greater Than or Equal (>=):"
360 IF 7 >= 4 THEN PRINT "7 >= 4: TRUE (correct)"
370 IF 4 >= 4 THEN PRINT "4 >= 4: TRUE (correct)"
380 IF 7 >= 4 THEN GOTO 390
385 GOTO 9000
390 IF 3 >= 5 THEN GOTO 9000
400 PRINT "3 >= 5: FALSE (correct)"

410 PRINT ""
420 PRINT "=== All Comparison Operator Tests PASSED ==="
430 END

9000 PRINT "ERROR: Test failed!"
9010 END
