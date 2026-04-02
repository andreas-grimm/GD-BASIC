1 REM ========================================================================
2 REM Test: Variable Types
3 REM Description: Tests all variable types (integer%, real#, string$, boolean!, long&)
4 REM Expected: All variable types should work correctly with assignments
5 REM ========================================================================

10 PRINT "=== Testing Variable Types ==="
20 PRINT ""

30 REM Test integer variable (%)
40 PRINT "Testing Integer Variable (%):"
50 intVar% = 42
60 PRINT "intVar% = "; intVar%
70 IF intVar% != 42 THEN GOTO 9000

80 REM Test real variable (#)
90 PRINT "Testing Real Variable (#):"
100 realVar# = 3.14159
110 PRINT "realVar# = "; realVar#
120 IF realVar# < 3.14 THEN GOTO 9000
130 IF realVar# > 3.15 THEN GOTO 9000

140 REM Test string variable ($)
150 PRINT "Testing String Variable ($):"
160 strVar$ = "Hello, BASIC!"
170 PRINT "strVar$ = "; strVar$

180 REM Test boolean variable (!)
190 PRINT "Testing Boolean Variable (!):"
200 boolTrue! = 1 == 1
210 boolFalse! = 1 == 2
220 PRINT "boolTrue! = "; boolTrue!
230 PRINT "boolFalse! = "; boolFalse!
240 IF boolTrue! THEN PRINT "boolTrue! is TRUE (correct)"
250 IF boolTrue! THEN GOTO 260
255 GOTO 9000
260 IF boolFalse! THEN GOTO 9000
270 PRINT "boolFalse! is FALSE (correct)"

280 REM Test long variable (&)
290 PRINT "Testing Long Variable (&):"
300 longVar& = 1000000
310 PRINT "longVar& = "; longVar&
320 IF longVar& != 1000000 THEN GOTO 9000

330 REM Test variable reassignment
340 PRINT "Testing Variable Reassignment:"
350 x% = 10
360 PRINT "x% = "; x%
370 x% = 20
380 PRINT "x% = "; x%
390 IF x% != 20 THEN GOTO 9000

400 REM Test mixed type operations
410 PRINT "Testing Mixed Type Operations:"
420 i% = 5
430 r# = 2.5
440 result# = i% * r#
450 PRINT "5 * 2.5 = "; result#
460 IF result# != 12.5 THEN GOTO 9000

470 PRINT ""
480 PRINT "=== All Variable Type Tests PASSED ==="
490 END

9000 PRINT "ERROR: Test failed!"
9010 END
