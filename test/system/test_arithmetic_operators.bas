REM ========================================================================
REM Test: Arithmetic Operators
REM Description: Tests all basic arithmetic operators (+, -, *, /, ^, %)
REM Expected: All calculations should produce correct results
REM ========================================================================

10 PRINT "=== Testing Arithmetic Operators ==="
20 PRINT ""

REM Test addition
30 PRINT "Testing Addition:"
40 a# = 5 + 3
50 PRINT "5 + 3 = "; a#
60 IF a# != 8 THEN GOTO 9000

REM Test subtraction
70 PRINT "Testing Subtraction:"
80 b# = 10 - 4
90 PRINT "10 - 4 = "; b#
100 IF b# != 6 THEN GOTO 9000

REM Test multiplication
110 PRINT "Testing Multiplication:"
120 c# = 6 * 7
130 PRINT "6 * 7 = "; c#
140 IF c# != 42 THEN GOTO 9000

REM Test division
150 PRINT "Testing Division:"
160 d# = 20 / 4
170 PRINT "20 / 4 = "; d#
180 IF d# != 5 THEN GOTO 9000

REM Test power
190 PRINT "Testing Power:"
200 e# = 2 ^ 3
210 PRINT "2 ^ 3 = "; e#
220 IF e# != 8 THEN GOTO 9000

REM Test modulo
230 PRINT "Testing Modulo:"
240 f% = 17 % 5
250 PRINT "17 % 5 = "; f%
260 IF f% != 2 THEN GOTO 9000

REM Test operator precedence
270 PRINT "Testing Operator Precedence:"
280 g# = 2 + 3 * 4
290 PRINT "2 + 3 * 4 = "; g#
300 IF g# != 14 THEN GOTO 9000

310 h# = (2 + 3) * 4
320 PRINT "(2 + 3) * 4 = "; h#
330 IF h# != 20 THEN GOTO 9000

340 PRINT ""
350 PRINT "=== All Arithmetic Operator Tests PASSED ==="
360 END

9000 PRINT "ERROR: Test failed!"
9010 END
