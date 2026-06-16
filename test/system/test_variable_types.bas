1 REM ========================================================================
2 REM Test: Variable Types
3 REM Description: Tests all variable types (integer%, real, string$, boolean!, long&)
4 REM Expected: All variable types should work correctly with assignments
5 REM ========================================================================
10 PRINT "=== Testing Variable Types ==="
20 PRINT ""
30 REM Test integer variable (%)
40 PRINT "Testing Integer Variable (%):"
50 intVar% = 42
60 PRINT "intVar% = ";
70 PRINT intVar%
80 IF intVar% != 42 THEN 9000
90 REM Test real variable (#)
100 PRINT "Testing Real Variable (#):"
110 realVar = 3.14159
120 PRINT "realVar = ";
130 PRINT realVar
140 IF realVar < 3.14 THEN 9000
150 IF realVar > 3.15 THEN 9000
160 REM Test string variable ($)
170 PRINT "Testing String Variable ($):"
180 strVar$ = "Hello, BASIC!"
190 PRINT "strVar$ = ";
200 PRINT strVar$
210 REM Test boolean variable (!)
220 PRINT "Testing Boolean Variable (!):"
230 boolTrue! = 1 == 1
240 boolFalse! = 1 == 2
250 PRINT "boolTrue! = ";
260 PRINT boolTrue!
270 PRINT "boolFalse! = ";
280 PRINT boolFalse!
290 IF boolTrue! THEN
291 PRINT "boolTrue! is TRUE (correct)"
292 END-IF
300 IF boolTrue! THEN 310
305 GOTO 9000
310 IF boolFalse! THEN 9000
320 PRINT "boolFalse! is FALSE (correct)"
330 REM Test long variable (&)
340 PRINT "Testing Long Variable (&):"
350 longVar& = 1000000
360 PRINT "longVar& = ";
370 PRINT longVar&
380 IF longVar& != 1000000 THEN 9000
390 REM Test variable reassignment
400 PRINT "Testing Variable Reassignment:"
410 x% = 10
420 PRINT "x% = ";
430 PRINT x%
440 x% = 20
450 PRINT "x% = ";
460 PRINT x%
470 IF x% != 20 THEN 9000
480 REM Test mixed type operations
490 PRINT "Testing Mixed Type Operations:"
500 i% = 5
510 r = 2.5
520 result = i% * r
530 PRINT "5 * 2.5 = ";
540 PRINT result
550 IF result != 12.5 THEN 9000
560 PRINT ""
570 PRINT "=== All Variable Type Tests PASSED ==="
580 END
9000 PRINT "ERROR: Test failed!"
9010 END
