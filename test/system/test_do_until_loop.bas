REM ========================================================================
REM Test: DO-UNTIL Loop
REM Description: Tests DO-UNTIL loop with various exit conditions
REM Expected: Loops should execute until condition becomes true
REM ========================================================================

10 PRINT "=== Testing DO-UNTIL Loop ==="
20 PRINT ""

REM Test basic DO-UNTIL loop
30 PRINT "Testing Basic DO-UNTIL (count to 5):"
40 counter% = 1
50 DO
60   PRINT counter%;
70   counter% = counter% + 1
80 UNTIL counter% > 5
90 PRINT ""

REM Test DO-UNTIL with condition check
100 PRINT "Testing DO-UNTIL with Condition:"
110 sum% = 0
120 i% = 1
130 DO
140   sum% = sum% + i%
150   i% = i% + 1
160 UNTIL sum% >= 10
170 PRINT "Sum reached: "; sum%
180 IF sum% < 10 THEN GOTO 9000

REM Test nested DO-UNTIL loops
190 PRINT "Testing Nested DO-UNTIL:"
200 outer% = 1
210 DO
220   PRINT "Outer: "; outer%
230   inner% = 1
240   DO
250     PRINT "  Inner: "; inner%
260     inner% = inner% + 1
270   UNTIL inner% > 2
280   outer% = outer% + 1
290 UNTIL outer% > 2

300 PRINT ""
310 PRINT "=== All DO-UNTIL Loop Tests PASSED ==="
320 END

9000 PRINT "ERROR: Test failed!"
9010 END
