1 REM ========================================================================
2 REM Test: FOR-NEXT Loop
3 REM Description: Tests FOR-NEXT loop with various configurations
4 REM Expected: Loops should iterate correctly with different step values
5 REM ========================================================================
10 PRINT "=== Testing FOR-NEXT Loop ==="
20 PRINT ""
30 REM Test basic FOR-NEXT loop
40 PRINT "Testing Basic FOR-NEXT (1 to 5):"
50 FOR i% = 1 TO 5
60   PRINT i%;
70 NEXT
80 PRINT ""
90 REM Test FOR-NEXT with STEP
100 PRINT "Testing FOR-NEXT with STEP 2 (0 to 10):"
110 FOR j% = 0 TO 10 STEP 2
120   PRINT j%;
130 NEXT
140 PRINT ""
150 REM Test FOR-NEXT with negative STEP
160 PRINT "Testing FOR-NEXT with STEP -1 (10 to 1):"
170 FOR k% = 10 TO 1 STEP -1
180   PRINT k%;
190 NEXT
200 PRINT ""
210 REM Test nested FOR-NEXT loops
220 PRINT "Testing Nested FOR-NEXT:"
230 FOR outer% = 1 TO 3
240   PRINT "Outer: ";
250   PRINT outer%
260   FOR inner% = 1 TO 2
270     PRINT "  Inner: ";
280     PRINT inner%
290   NEXT
300 NEXT
310 REM Test FOR-NEXT with variable bounds
320 PRINT "Testing FOR-NEXT with Variable Bounds:"
330 start% = 5
340 end% = 8
350 step% = 1
360 FOR m% = start% TO end% STEP step%
370   PRINT m%;
380 NEXT
390 PRINT ""
400 REM Test FOR-NEXT with calculations
410 PRINT "Testing FOR-NEXT Loop Counter:"
420 count% = 0
430 FOR n% = 1 TO 10
440   count% = count% + 1
450 NEXT
460 PRINT "Loop executed ";
470 PRINT count%;
480 PRINT " times"
490 IF count% != 10 THEN 9000
500 PRINT ""
510 PRINT "=== All FOR-NEXT Loop Tests PASSED ==="
520 END
9000 PRINT "ERROR: Test failed!"
9010 END
