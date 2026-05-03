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
70 PRINT a#
80 IF a# != 8 THEN 9000
90 REM Test subtraction
100 PRINT "Testing Subtraction:"
110 b# = 10 - 4
120 PRINT "10 - 4 = ";
130 PRINT b#
140 IF b# != 6 THEN 9000
150 REM Test multiplication
160 PRINT "Testing Multiplication:"
170 c# = 6 * 7
180 PRINT "6 * 7 = ";
190 PRINT c#
200 IF c# != 42 THEN 9000
210 REM Test division
220 PRINT "Testing Division:"
230 d# = 20 / 4
240 PRINT "20 / 4 = ";
250 PRINT d#
260 IF d# != 5 THEN 9000
270 REM Test power
280 PRINT "Testing Power:"
290 e# = 2 ^ 3
300 PRINT "2 ^ 3 = ";
310 PRINT e#
320 IF e# != 8 THEN 9000
330 REM Test modulo
340 PRINT "Testing Modulo:"
350 f% = 17 % 5
360 PRINT "17 % 5 = ";
370 PRINT f%
380 IF f% != 2 THEN 9000
390 REM Test operator precedence
400 PRINT "Testing Operator Precedence:"
410 g# = 2 + 3 * 4
420 PRINT "2 + 3 * 4 = ";
430 PRINT g#
440 IF g# != 14 THEN 9000
450 h# = (2 + 3) * 4
460 PRINT "(2 + 3) * 4 = ";
470 PRINT h#
480 IF h# != 20 THEN 9000
490 PRINT ""
500 PRINT "=== All Arithmetic Operator Tests PASSED ==="
510 END
9000 PRINT "ERROR: Test failed!"
9010 END
