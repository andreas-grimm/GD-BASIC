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
60 PRINT "((5 + 3) * 2 - 4) / 3 = "; result#
70 IF result# != 4 THEN GOTO 9000

80 REM Test mixed operators
90 PRINT "Testing Mixed Operators:"
100 a% = 10
110 b% = 5
120 c% = 2
130 result2% = a% + b% * c% - 3
140 PRINT "10 + 5 * 2 - 3 = "; result2%
150 IF result2% != 17 THEN GOTO 9000

160 REM Test complex boolean expression
170 PRINT "Testing Complex Boolean Expression:"
180 x% = 7
190 IF (x% > 5 AND x% < 10) OR x% == 15 THEN
200   PRINT "Complex condition TRUE (correct)"
210 ELSE
220   GOTO 9000
230 END-IF

240 REM Test expression with functions
250 PRINT "Testing Expression with Functions:"
260 val# = ABS(-5) + SQR(16) * 2
270 PRINT "ABS(-5) + SQR(16) * 2 = "; val#
280 IF val# != 13 THEN GOTO 9000

290 REM Test power with parentheses
300 PRINT "Testing Power with Parentheses:"
310 pow1# = (2 + 3) ^ 2
320 PRINT "(2 + 3) ^ 2 = "; pow1#
330 IF pow1# != 25 THEN GOTO 9000

340 REM Test negative numbers
350 PRINT "Testing Negative Numbers:"
360 neg1% = -5 + 10
370 PRINT "-5 + 10 = "; neg1%
380 IF neg1% != 5 THEN GOTO 9000
390 neg2% = 10 - -5
400 PRINT "10 - -5 = "; neg2%
410 IF neg2% != 15 THEN GOTO 9000

420 PRINT ""
430 PRINT "=== All Complex Expression Tests PASSED ==="
440 END

9000 PRINT "ERROR: Test failed!"
9010 END
