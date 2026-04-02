REM ========================================================================
REM Test: Edge Cases
REM Description: Tests edge cases and boundary conditions
REM Expected: Edge cases should be handled correctly
REM ========================================================================

10 PRINT "=== Testing Edge Cases ==="
20 PRINT ""

REM Test zero values
30 PRINT "Testing Zero Values:"
40 zero% = 0
50 PRINT "zero% = "; zero%
60 IF zero% != 0 THEN GOTO 9000
70 result# = 5 + zero%
80 PRINT "5 + 0 = "; result#
90 IF result# != 5 THEN GOTO 9000

REM Test division by one
100 PRINT "Testing Division by One:"
110 div1# = 10 / 1
120 PRINT "10 / 1 = "; div1#
130 IF div1# != 10 THEN GOTO 9000

REM Test multiplication by zero
140 PRINT "Testing Multiplication by Zero:"
150 mult0% = 42 * 0
160 PRINT "42 * 0 = "; mult0%
170 IF mult0% != 0 THEN GOTO 9000

REM Test power of zero
180 PRINT "Testing Power of Zero:"
190 pow0# = 5 ^ 0
200 PRINT "5 ^ 0 = "; pow0#
210 IF pow0# != 1 THEN GOTO 9000

REM Test power of one
220 PRINT "Testing Power of One:"
230 pow1# = 5 ^ 1
240 PRINT "5 ^ 1 = "; pow1#
250 IF pow1# != 5 THEN GOTO 9000

REM Test empty string
260 PRINT "Testing Empty String:"
270 empty$ = ""
280 len% = LEN(empty$)
290 PRINT "LEN('') = "; len%
300 IF len% != 0 THEN GOTO 9000

REM Test single character string
310 PRINT "Testing Single Character String:"
320 single$ = "A"
330 len2% = LEN(single$)
340 PRINT "LEN('A') = "; len2%
350 IF len2% != 1 THEN GOTO 9000

REM Test FOR loop with same start and end
360 PRINT "Testing FOR Loop (start = end):"
370 count% = 0
380 FOR i% = 5 TO 5
390   count% = count% + 1
400 NEXT i%
410 PRINT "Loop executed "; count%; " time(s)"
420 IF count% != 1 THEN GOTO 9000

430 PRINT ""
440 PRINT "=== All Edge Case Tests PASSED ==="
450 END

9000 PRINT "ERROR: Test failed!"
9010 END
