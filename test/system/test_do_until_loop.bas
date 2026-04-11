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
190 PRINT "Sum reached: ";
200 PRINT sum%
210 IF sum% < 10 THEN 9000
220 REM Test nested DO-UNTIL loops
230 PRINT "Testing Nested DO-UNTIL:"
240 outer% = 1
250 DO
260   PRINT "Outer: ";
270   PRINT outer%
280   inner% = 1
290   DO
300     PRINT "  Inner: ";
310     PRINT inner%
320     inner% = inner% + 1
330   UNTIL inner% > 2
340   outer% = outer% + 1
350 UNTIL outer% > 2
360 PRINT ""
370 PRINT "=== All DO-UNTIL Loop Tests PASSED ==="
380 END
9000 PRINT "ERROR: Test failed!"
9010 END
