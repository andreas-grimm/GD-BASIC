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
70 NEXT i%
80 PRINT ""

90 REM Test FOR-NEXT with STEP
100 PRINT "Testing FOR-NEXT with STEP 2 (0 to 10):"
110 FOR j% = 0 TO 10 STEP 2
120   PRINT j%;
130 NEXT j%
140 PRINT ""

150 REM Test FOR-NEXT with negative STEP
160 PRINT "Testing FOR-NEXT with STEP -1 (10 to 1):"
170 FOR k% = 10 TO 1 STEP -1
180   PRINT k%;
190 NEXT k%
200 PRINT ""

210 REM Test nested FOR-NEXT loops
220 PRINT "Testing Nested FOR-NEXT:"
230 FOR outer% = 1 TO 3
240   PRINT "Outer: "; outer%
250   FOR inner% = 1 TO 2
260     PRINT "  Inner: "; inner%
270   NEXT inner%
280 NEXT outer%

290 REM Test FOR-NEXT with variable bounds
300 PRINT "Testing FOR-NEXT with Variable Bounds:"
310 start% = 5
320 end% = 8
330 step% = 1
340 FOR m% = start% TO end% STEP step%
350   PRINT m%;
360 NEXT m%
370 PRINT ""

380 REM Test FOR-NEXT with calculations
390 PRINT "Testing FOR-NEXT Loop Counter:"
400 count% = 0
410 FOR n% = 1 TO 10
420   count% = count% + 1
430 NEXT n%
440 PRINT "Loop executed "; count%; " times"
450 IF count% != 10 THEN GOTO 9000

460 PRINT ""
470 PRINT "=== All FOR-NEXT Loop Tests PASSED ==="
480 END

9000 PRINT "ERROR: Test failed!"
9010 END
