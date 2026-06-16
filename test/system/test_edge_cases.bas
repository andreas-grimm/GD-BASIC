1 REM ========================================================================
2 REM Test: Edge Cases
3 REM Description: Tests edge cases and boundary conditions
4 REM Expected: Edge cases should be handled correctly
5 REM ========================================================================
10 PRINT "=== Testing Edge Cases ==="
20 PRINT ""
30 REM Test zero values
40 PRINT "Testing Zero Values:"
50 zero% = 0
60 PRINT "zero% = ";
70 PRINT zero%
80 IF zero% != 0 THEN 9000
90 result = 5 + zero%
100 PRINT "5 + 0 = ";
110 PRINT result
120 IF result != 5 THEN 9000
130 REM Test division by one
140 PRINT "Testing Division by One:"
150 div1 = 10 / 1
160 PRINT "10 / 1 = ";
170 PRINT div1
180 IF div1 != 10 THEN 9000
190 REM Test multiplication by zero
200 PRINT "Testing Multiplication by Zero:"
210 mult0% = 42 * 0
220 PRINT "42 * 0 = ";
230 PRINT mult0%
240 IF mult0% != 0 THEN 9000
250 REM Test power of zero
260 PRINT "Testing Power of Zero:"
270 pow0 = 5 ^ 0
280 PRINT "5 ^ 0 = ";
290 PRINT pow0
300 IF pow0 != 1 THEN 9000
310 REM Test power of one
320 PRINT "Testing Power of One:"
330 pow1 = 5 ^ 1
340 PRINT "5 ^ 1 = ";
350 PRINT pow1
360 IF pow1 != 5 THEN 9000
370 REM Test empty string
380 PRINT "Testing Empty String:"
390 empty$ = ""
400 len% = LEN(empty$)
410 PRINT "LEN('') = ";
420 PRINT len%
430 IF len% != 0 THEN 9000
440 REM Test single character string
450 PRINT "Testing Single Character String:"
460 single$ = "A"
470 len2% = LEN(single$)
480 PRINT "LEN('A') = ";
490 PRINT len2%
500 IF len2% != 1 THEN 9000
510 REM Test FOR loop with same start and end
520 PRINT "Testing FOR Loop (start = end):"
530 count% = 0
540 FOR i% = 5 TO 5
550   count% = count% + 1
560 NEXT
570 PRINT "Loop executed ";
580 PRINT count%;
590 PRINT " time(s)"
600 IF count% != 1 THEN 9000
610 PRINT ""
620 PRINT "=== All Edge Case Tests PASSED ==="
630 END
9000 PRINT "ERROR: Test failed!"
9010 END
