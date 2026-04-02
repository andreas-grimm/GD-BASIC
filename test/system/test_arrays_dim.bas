1 REM ========================================================================
2 REM Test: Arrays and DIM Statement
3 REM Description: Tests array declaration with DIM and array operations
4 REM Expected: Arrays should be declared and accessed correctly
5 REM ========================================================================

10 PRINT "=== Testing Arrays and DIM Statement ==="
20 PRINT ""

30 REM Test 1D integer array
40 PRINT "Testing 1D Integer Array:"
50 DIM arr%(10)
60 arr%(1) = 10
70 arr%(2) = 20
80 arr%(3) = 30
90 PRINT "arr%(1) = "; arr%(1)
100 PRINT "arr%(2) = "; arr%(2)
110 PRINT "arr%(3) = "; arr%(3)
120 IF arr%(1) != 10 THEN GOTO 9000
130 IF arr%(2) != 20 THEN GOTO 9000
140 IF arr%(3) != 30 THEN GOTO 9000

150 REM Test 1D real array
160 PRINT "Testing 1D Real Array:"
170 DIM nums#(5)
180 nums#(1) = 1.5
190 nums#(2) = 2.5
200 nums#(3) = 3.5
210 PRINT "nums#(1) = "; nums#(1)
220 PRINT "nums#(2) = "; nums#(2)
230 PRINT "nums#(3) = "; nums#(3)

240 REM Test 1D string array
250 PRINT "Testing 1D String Array:"
260 DIM names$(3)
270 names$(1) = "Alice"
280 names$(2) = "Bob"
290 names$(3) = "Charlie"
300 PRINT "names$(1) = "; names$(1)
310 PRINT "names$(2) = "; names$(2)
320 PRINT "names$(3) = "; names$(3)

330 REM Test array with loop
340 PRINT "Testing Array with FOR Loop:"
350 DIM values%(5)
360 FOR i% = 1 TO 5
370   values%(i%) = i% * 10
380 NEXT i%
390 FOR i% = 1 TO 5
400   PRINT "values%("; i%; ") = "; values%(i%)
410 NEXT i%

420 REM Test 2D array
430 PRINT "Testing 2D Array:"
440 DIM matrix%(3, 3)
450 matrix%(1, 1) = 1
460 matrix%(1, 2) = 2
470 matrix%(2, 1) = 3
480 matrix%(2, 2) = 4
490 PRINT "matrix%(1,1) = "; matrix%(1, 1)
500 PRINT "matrix%(1,2) = "; matrix%(1, 2)
510 PRINT "matrix%(2,1) = "; matrix%(2, 1)
520 PRINT "matrix%(2,2) = "; matrix%(2, 2)
530 IF matrix%(1, 1) != 1 THEN GOTO 9000
540 IF matrix%(2, 2) != 4 THEN GOTO 9000

550 REM Test array variable as index
560 PRINT "Testing Array Variable as Index:"
570 DIM data%(10)
580 idx% = 5
590 data%(idx%) = 99
600 PRINT "data%(5) = "; data%(idx%)
610 IF data%(idx%) != 99 THEN GOTO 9000

620 PRINT ""
630 PRINT "=== All Array and DIM Tests PASSED ==="
640 END

9000 PRINT "ERROR: Test failed!"
9010 END
