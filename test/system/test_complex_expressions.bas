1 REM ========================================================================
2 REM Test: Complex Expressions
3 REM Description: Tests complex mathematical and logical expressions
4 REM Expected: Complex expressions should evaluate correctly
5 REM ========================================================================
10 PRINT "=== Testing Complex Expressions ==="
20 PRINT ""
30 REM Test nested arithmetic
40 PRINT "Testing Nested Arithmetic:"
50 result# = ((5 + 3) * 2 - 4) / 3
60 PRINT "((5 + 3) * 2 - 4) / 3 = ";
70 PRINT result#
80 IF result# != 4 THEN 9000
90 REM Test mixed operators
100 PRINT "Testing Mixed Operators:"
110 a% = 10
120 b% = 5
130 c% = 2
140 result2% = a% + b% * c% - 3
150 PRINT "10 + 5 * 2 - 3 = ";
160 PRINT result2%
170 IF result2% != 17 THEN 9000
180 REM Test complex boolean expression
190 PRINT "Testing Complex Boolean Expression:"
200 x% = 7
210 IF (x% > 5 AND x% < 10) OR x% == 15 THEN
220   PRINT "Complex condition TRUE (correct)"
230 ELSE
240   GOTO 9000
250 END-IF
260 REM Test expression with functions
270 PRINT "Testing Expression with Functions:"
280 val# = ABS(-5) + SQR(16) * 2
290 PRINT "ABS(-5) + SQR(16) * 2 = ";
300 PRINT val#
310 IF val# != 13 THEN 9000
320 REM Test power with parentheses
330 PRINT "Testing Power with Parentheses:"
340 pow1# = (2 + 3) ^ 2
350 PRINT "(2 + 3) ^ 2 = ";
360 PRINT pow1#
370 IF pow1# != 25 THEN 9000
380 REM Test negative numbers
390 PRINT "Testing Negative Numbers:"
400 neg1% = -5 + 10
410 PRINT "-5 + 10 = ";
420 PRINT neg1%
430 IF neg1% != 5 THEN 9000
440 neg2% = 10 - -5
450 PRINT "10 - -5 = ";
460 PRINT neg2%
470 IF neg2% != 15 THEN 9000
480 PRINT ""
490 PRINT "=== All Complex Expression Tests PASSED ==="
500 END
9000 PRINT "ERROR: Test failed!"
9010 END
