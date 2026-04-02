REM ========================================================================
REM Test: Variable Types
REM Description: Tests all variable types (integer%, real#, string$, boolean!, long&)
REM Expected: All variable types should work correctly with assignments
REM ========================================================================

10 PRINT "=== Testing Variable Types ==="
20 PRINT ""

REM Test integer variable (%)
30 PRINT "Testing Integer Variable (%):"
40 intVar% = 42
50 PRINT "intVar% = "; intVar%
60 IF intVar% != 42 THEN GOTO 9000

REM Test real variable (#)
70 PRINT "Testing Real Variable (#):"
80 realVar# = 3.14159
90 PRINT "realVar# = "; realVar#
100 IF realVar# < 3.14 THEN GOTO 9000
110 IF realVar# > 3.15 THEN GOTO 9000

REM Test string variable ($)
120 PRINT "Testing String Variable ($):"
130 strVar$ = "Hello, BASIC!"
140 PRINT "strVar$ = "; strVar$

REM Test boolean variable (!)
150 PRINT "Testing Boolean Variable (!):"
160 boolTrue! = 1 == 1
170 boolFalse! = 1 == 2
180 PRINT "boolTrue! = "; boolTrue!
190 PRINT "boolFalse! = "; boolFalse!
200 IF boolTrue! THEN PRINT "boolTrue! is TRUE (correct)"
210 IF boolTrue! THEN GOTO 220
215 GOTO 9000
220 IF boolFalse! THEN GOTO 9000
230 PRINT "boolFalse! is FALSE (correct)"

REM Test long variable (&)
240 PRINT "Testing Long Variable (&):"
250 longVar& = 1000000
260 PRINT "longVar& = "; longVar&
270 IF longVar& != 1000000 THEN GOTO 9000

REM Test variable reassignment
280 PRINT "Testing Variable Reassignment:"
290 x% = 10
300 PRINT "x% = "; x%
310 x% = 20
320 PRINT "x% = "; x%
330 IF x% != 20 THEN GOTO 9000

REM Test mixed type operations
340 PRINT "Testing Mixed Type Operations:"
350 i% = 5
360 r# = 2.5
370 result# = i% * r#
380 PRINT "5 * 2.5 = "; result#
390 IF result# != 12.5 THEN GOTO 9000

400 PRINT ""
410 PRINT "=== All Variable Type Tests PASSED ==="
420 END

9000 PRINT "ERROR: Test failed!"
9010 END
