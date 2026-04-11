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
90 PRINT "arr%(1) = ";
100 PRINT arr%(1)
110 PRINT "arr%(2) = ";
120 PRINT arr%(2)
130 PRINT "arr%(3) = ";
140 PRINT arr%(3)
150 IF arr%(1) != 10 THEN 9000
160 IF arr%(2) != 20 THEN 9000
170 IF arr%(3) != 30 THEN 9000
180 REM Test 1D real array
190 PRINT "Testing 1D Real Array:"
200 DIM nums#(5)
210 nums#(1) = 1.5
220 nums#(2) = 2.5
230 nums#(3) = 3.5
240 PRINT "nums#(1) = ";
250 PRINT nums#(1)
260 PRINT "nums#(2) = ";
270 PRINT nums#(2)
280 PRINT "nums#(3) = ";
290 PRINT nums#(3)
300 REM Test 1D string array
310 PRINT "Testing 1D String Array:"
320 DIM names$(3)
330 names$(1) = "Alice"
340 names$(2) = "Bob"
350 names$(3) = "Charlie"
360 PRINT "names$(1) = ";
370 PRINT names$(1)
380 PRINT "names$(2) = ";
390 PRINT names$(2)
400 PRINT "names$(3) = ";
410 PRINT names$(3)
420 REM Test array with loop
430 PRINT "Testing Array with FOR Loop:"
440 DIM values%(5)
450 FOR i% = 1 TO 5
460   values%(i%) = i% * 10
470 NEXT
480 FOR i% = 1 TO 5
490   PRINT "values%";
500   PRINT i%;
510   PRINT " = ";
520   PRINT values%(i%)
530 NEXT
540 REM Test 2D array
550 PRINT "Testing 2D Array:"
560 DIM matrix%(3, 3)
570 matrix%(1, 1) = 1
580 matrix%(1, 2) = 2
590 matrix%(2, 1) = 3
600 matrix%(2, 2) = 4
610 PRINT "matrix%(1,1) = ";
620 PRINT matrix%(1, 1)
630 PRINT "matrix%(1,2) = ";
640 PRINT matrix%(1, 2)
650 PRINT "matrix%(2,1) = ";
660 PRINT matrix%(2, 1)
670 PRINT "matrix%(2,2) = ";
680 PRINT matrix%(2, 2)
690 IF matrix%(1, 1) != 1 THEN 9000
700 IF matrix%(2, 2) != 4 THEN 9000
710 REM Test array variable as index
720 PRINT "Testing Array Variable as Index:"
730 DIM data%(10)
740 idx% = 5
750 data%(idx%) = 99
760 PRINT "data%(5) = ";
770 PRINT data%(idx%)
780 IF data%(idx%) != 99 THEN 9000
790 PRINT ""
800 PRINT "=== All Array and DIM Tests PASSED ==="
810 END
9000 PRINT "ERROR: Test failed!"
9010 END
