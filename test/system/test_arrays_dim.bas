REM ========================================================================
REM Test: Arrays and DIM Statement
REM Description: Tests array declaration with DIM and array operations
REM Expected: Arrays should be declared and accessed correctly
REM ========================================================================

10 PRINT "=== Testing Arrays and DIM Statement ==="
20 PRINT ""

REM Test 1D integer array
30 PRINT "Testing 1D Integer Array:"
40 DIM arr%(10)
50 arr%(1) = 10
60 arr%(2) = 20
70 arr%(3) = 30
80 PRINT "arr%(1) = "; arr%(1)
90 PRINT "arr%(2) = "; arr%(2)
100 PRINT "arr%(3) = "; arr%(3)
110 IF arr%(1) != 10 THEN GOTO 9000
120 IF arr%(2) != 20 THEN GOTO 9000
130 IF arr%(3) != 30 THEN GOTO 9000

REM Test 1D real array
140 PRINT "Testing 1D Real Array:"
150 DIM nums#(5)
160 nums#(1) = 1.5
170 nums#(2) = 2.5
180 nums#(3) = 3.5
190 PRINT "nums#(1) = "; nums#(1)
200 PRINT "nums#(2) = "; nums#(2)
210 PRINT "nums#(3) = "; nums#(3)

REM Test 1D string array
220 PRINT "Testing 1D String Array:"
230 DIM names$(3)
240 names$(1) = "Alice"
250 names$(2) = "Bob"
260 names$(3) = "Charlie"
270 PRINT "names$(1) = "; names$(1)
280 PRINT "names$(2) = "; names$(2)
290 PRINT "names$(3) = "; names$(3)

REM Test array with loop
300 PRINT "Testing Array with FOR Loop:"
310 DIM values%(5)
320 FOR i% = 1 TO 5
330   values%(i%) = i% * 10
340 NEXT i%
350 FOR i% = 1 TO 5
360   PRINT "values%("; i%; ") = "; values%(i%)
370 NEXT i%

REM Test 2D array
380 PRINT "Testing 2D Array:"
390 DIM matrix%(3, 3)
400 matrix%(1, 1) = 1
410 matrix%(1, 2) = 2
420 matrix%(2, 1) = 3
430 matrix%(2, 2) = 4
440 PRINT "matrix%(1,1) = "; matrix%(1, 1)
450 PRINT "matrix%(1,2) = "; matrix%(1, 2)
460 PRINT "matrix%(2,1) = "; matrix%(2, 1)
470 PRINT "matrix%(2,2) = "; matrix%(2, 2)
480 IF matrix%(1, 1) != 1 THEN GOTO 9000
490 IF matrix%(2, 2) != 4 THEN GOTO 9000

REM Test array variable as index
500 PRINT "Testing Array Variable as Index:"
510 DIM data%(10)
520 idx% = 5
530 data%(idx%) = 99
540 PRINT "data%(5) = "; data%(idx%)
550 IF data%(idx%) != 99 THEN GOTO 9000

560 PRINT ""
570 PRINT "=== All Array and DIM Tests PASSED ==="
580 END

9000 PRINT "ERROR: Test failed!"
9010 END
