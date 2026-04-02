1 REM ========================================================================
2 REM Test: DO-UNTIL Loop
3 REM Description: Tests DO-UNTIL loop with various exit conditions
4 REM Expected: Loops should execute until condition becomes true
5 REM ========================================================================

10 PRINT "=== Testing DO-UNTIL Loop ==="
20 PRINT ""

30 REM Test basic DO-UNTIL loop
40 PRINT "Testing Basic DO-UNTIL (count to 5):"
50 counter% = 1
60 DO
70   PRINT counter%;
80   counter% = counter% + 1
90 UNTIL counter% > 5
100 PRINT ""

110 REM Test DO-UNTIL with condition check
120 PRINT "Testing DO-UNTIL with Condition:"
130 sum% = 0
140 i% = 1
150 DO
160   sum% = sum% + i%
170   i% = i% + 1
180 UNTIL sum% >= 10
190 PRINT "Sum reached: "; sum%
200 IF sum% < 10 THEN GOTO 9000

210 REM Test nested DO-UNTIL loops
220 PRINT "Testing Nested DO-UNTIL:"
230 outer% = 1
240 DO
250   PRINT "Outer: "; outer%
260   inner% = 1
270   DO
280     PRINT "  Inner: "; inner%
290     inner% = inner% + 1
300   UNTIL inner% > 2
310   outer% = outer% + 1
320 UNTIL outer% > 2

330 PRINT ""
340 PRINT "=== All DO-UNTIL Loop Tests PASSED ==="
350 END

9000 PRINT "ERROR: Test failed!"
9010 END
