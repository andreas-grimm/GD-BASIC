REM ========================================================================
REM Test: FOR-NEXT Loop
REM Description: Tests FOR-NEXT loop with various configurations
REM Expected: Loops should iterate correctly with different step values
REM ========================================================================

10 PRINT "=== Testing FOR-NEXT Loop ==="
20 PRINT ""

REM Test basic FOR-NEXT loop
30 PRINT "Testing Basic FOR-NEXT (1 to 5):"
40 FOR i% = 1 TO 5
50   PRINT i%;
60 NEXT i%
70 PRINT ""

REM Test FOR-NEXT with STEP
80 PRINT "Testing FOR-NEXT with STEP 2 (0 to 10):"
90 FOR j% = 0 TO 10 STEP 2
100   PRINT j%;
110 NEXT j%
120 PRINT ""

REM Test FOR-NEXT with negative STEP
130 PRINT "Testing FOR-NEXT with STEP -1 (10 to 1):"
140 FOR k% = 10 TO 1 STEP -1
150   PRINT k%;
160 NEXT k%
170 PRINT ""

REM Test nested FOR-NEXT loops
180 PRINT "Testing Nested FOR-NEXT:"
190 FOR outer% = 1 TO 3
200   PRINT "Outer: "; outer%
210   FOR inner% = 1 TO 2
220     PRINT "  Inner: "; inner%
230   NEXT inner%
240 NEXT outer%

REM Test FOR-NEXT with variable bounds
250 PRINT "Testing FOR-NEXT with Variable Bounds:"
260 start% = 5
270 end% = 8
280 step% = 1
290 FOR m% = start% TO end% STEP step%
300   PRINT m%;
310 NEXT m%
320 PRINT ""

REM Test FOR-NEXT with calculations
330 PRINT "Testing FOR-NEXT Loop Counter:"
340 count% = 0
350 FOR n% = 1 TO 10
360   count% = count% + 1
370 NEXT n%
380 PRINT "Loop executed "; count%; " times"
390 IF count% != 10 THEN GOTO 9000

400 PRINT ""
410 PRINT "=== All FOR-NEXT Loop Tests PASSED ==="
420 END

9000 PRINT "ERROR: Test failed!"
9010 END
