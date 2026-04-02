1 REM ========================================================================
2 REM Test: Arithmetic Operators
3 REM Description: Tests all basic arithmetic operators (+, -, *, /, ^, %)
4 REM Expected: All calculations should produce correct results
5 REM ========================================================================

10 PRINT "=== Testing Arithmetic Operators ==="
20 PRINT ""

30 REM Test addition
40 PRINT "Testing Addition:"
50 a# = 5 + 3
60 PRINT "5 + 3 = ";
65 PRINT a#
70 IF a# != 8 THEN 9000

80 REM Test subtraction
90 PRINT "Testing Subtraction:"
100 b# = 10 - 4
110 PRINT "10 - 4 = ";
115 PRINT b#
120 IF b# != 6 THEN 9000

130 REM Test multiplication
140 PRINT "Testing Multiplication:"
150 c# = 6 * 7
160 PRINT "6 * 7 = ";
165 PRINT c#
170 IF c# != 42 THEN 9000

180 REM Test division
190 PRINT "Testing Division:"
200 d# = 20 / 4
210 PRINT "20 / 4 = ";
215 PRINT d#
220 IF d# != 5 THEN 9000

230 REM Test power
240 PRINT "Testing Power:"
250 e# = 2 ^ 3
260 PRINT "2 ^ 3 = ";
265 PRINT e#
270 IF e# != 8 THEN 9000

280 REM Test modulo
290 PRINT "Testing Modulo:"
300 f% = 17 % 5
310 PRINT "17 % 5 = ";
315 PRINT f%
320 IF f% != 2 THEN 9000

330 REM Test operator precedence
340 PRINT "Testing Operator Precedence:"
350 g# = 2 + 3 * 4
360 PRINT "2 + 3 * 4 = ";
365 PRINT g#
370 IF g# != 14 THEN 9000

380 h# = (2 + 3) * 4
390 PRINT "(2 + 3) * 4 = ";
395 PRINT h#
400 IF h# != 20 THEN 9000

410 PRINT ""
420 PRINT "=== All Arithmetic Operator Tests PASSED ==="
430 END

9000 PRINT "ERROR: Test failed!"
9010 END
