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
60 PRINT "zero% = "; zero%
70 IF zero% != 0 THEN GOTO 9000
80 result# = 5 + zero%
90 PRINT "5 + 0 = "; result#
100 IF result# != 5 THEN GOTO 9000

110 REM Test division by one
120 PRINT "Testing Division by One:"
130 div1# = 10 / 1
140 PRINT "10 / 1 = "; div1#
150 IF div1# != 10 THEN GOTO 9000

160 REM Test multiplication by zero
170 PRINT "Testing Multiplication by Zero:"
180 mult0% = 42 * 0
190 PRINT "42 * 0 = "; mult0%
200 IF mult0% != 0 THEN GOTO 9000

210 REM Test power of zero
220 PRINT "Testing Power of Zero:"
230 pow0# = 5 ^ 0
240 PRINT "5 ^ 0 = "; pow0#
250 IF pow0# != 1 THEN GOTO 9000

260 REM Test power of one
270 PRINT "Testing Power of One:"
280 pow1# = 5 ^ 1
290 PRINT "5 ^ 1 = "; pow1#
300 IF pow1# != 5 THEN GOTO 9000

310 REM Test empty string
320 PRINT "Testing Empty String:"
330 empty$ = ""
340 len% = LEN(empty$)
350 PRINT "LEN('') = "; len%
360 IF len% != 0 THEN GOTO 9000

370 REM Test single character string
380 PRINT "Testing Single Character String:"
390 single$ = "A"
400 len2% = LEN(single$)
410 PRINT "LEN('A') = "; len2%
420 IF len2% != 1 THEN GOTO 9000

430 REM Test FOR loop with same start and end
440 PRINT "Testing FOR Loop (start = end):"
450 count% = 0
460 FOR i% = 5 TO 5
470   count% = count% + 1
480 NEXT i%
490 PRINT "Loop executed "; count%; " time(s)"
500 IF count% != 1 THEN GOTO 9000

510 PRINT ""
520 PRINT "=== All Edge Case Tests PASSED ==="
530 END

9000 PRINT "ERROR: Test failed!"
9010 END
