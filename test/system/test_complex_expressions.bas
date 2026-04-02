REM ========================================================================
REM Test: Complex Expressions
REM Description: Tests complex mathematical and logical expressions
REM Expected: Complex expressions should evaluate correctly
REM ========================================================================

10 PRINT "=== Testing Complex Expressions ==="
20 PRINT ""

REM Test nested arithmetic
30 PRINT "Testing Nested Arithmetic:"
40 result# = ((5 + 3) * 2 - 4) / 3
50 PRINT "((5 + 3) * 2 - 4) / 3 = "; result#
60 IF result# != 4 THEN GOTO 9000

REM Test mixed operators
70 PRINT "Testing Mixed Operators:"
80 a% = 10
90 b% = 5
100 c% = 2
110 result2% = a% + b% * c% - 3
120 PRINT "10 + 5 * 2 - 3 = "; result2%
130 IF result2% != 17 THEN GOTO 9000

REM Test complex boolean expression
140 PRINT "Testing Complex Boolean Expression:"
150 x% = 7
160 IF (x% > 5 AND x% < 10) OR x% == 15 THEN
170   PRINT "Complex condition TRUE (correct)"
180 ELSE
190   GOTO 9000
200 END-IF

REM Test expression with functions
210 PRINT "Testing Expression with Functions:"
220 val# = ABS(-5) + SQR(16) * 2
230 PRINT "ABS(-5) + SQR(16) * 2 = "; val#
240 IF val# != 13 THEN GOTO 9000

REM Test power with parentheses
250 PRINT "Testing Power with Parentheses:"
260 pow1# = (2 + 3) ^ 2
270 PRINT "(2 + 3) ^ 2 = "; pow1#
280 IF pow1# != 25 THEN GOTO 9000

REM Test negative numbers
290 PRINT "Testing Negative Numbers:"
300 neg1% = -5 + 10
310 PRINT "-5 + 10 = "; neg1%
320 IF neg1% != 5 THEN GOTO 9000
330 neg2% = 10 - -5
340 PRINT "10 - -5 = "; neg2%
350 IF neg2% != 15 THEN GOTO 9000

360 PRINT ""
370 PRINT "=== All Complex Expression Tests PASSED ==="
380 END

9000 PRINT "ERROR: Test failed!"
9010 END
