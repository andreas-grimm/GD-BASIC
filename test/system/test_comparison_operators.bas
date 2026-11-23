10 PRINT "=== Testing Comparison Operators ==="
20 PRINT ""
30 REM Test equality
40 PRINT "Testing Equality (==):"
50 IF 5 == 5 THEN 70
60 GOTO 9000
70 IF 5 == 6 THEN 9000
80 PRINT "5 == 6: FALSE (correct)"
90 REM Test inequality
100 PRINT "Testing Inequality (!=):"
110 IF 5 != 6 THEN 130
120 GOTO 9000
130 IF 5 != 5 THEN 9000
140 PRINT "5 != 5: FALSE (correct)"
150 REM Test less than
160 PRINT "Testing Less Than (<):"
170 IF 3 < 5 THEN 190
180 GOTO 9000
190 IF 5 < 3 THEN 9000
200 PRINT "5 < 3: FALSE (correct)"
210 REM Test greater than
220 PRINT "Testing Greater Than (>):"
230 IF 7 > 4 THEN 250
240 GOTO 9000
250 IF 4 > 7 THEN 9000
260 PRINT "4 > 7: FALSE (correct)"
270 REM Test less than or equal
280 PRINT "Testing Less Than or Equal (<=):"
290 IF 3 <= 5 THEN 310
300 GOTO 9000
310 IF 6 <= 5 THEN 9000
320 PRINT "6 <= 5: FALSE (correct)"
330 REM Test greater than or equal
340 PRINT "Testing Greater Than or Equal (>=):"
350 IF 7 >= 4 THEN 370
360 GOTO 9000
370 IF 3 >= 5 THEN 9000
380 PRINT "3 >= 5: FALSE (correct)"
390 PRINT ""
400 PRINT "=== All Comparison Operator Tests PASSED ==="
410 END
9000 PRINT "ERROR: Test failed!"
9010 END
