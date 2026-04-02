REM ========================================================================
REM Test: WHILE Loop
REM Description: Tests WHILE loop with various conditions
REM Expected: Loops should execute while condition is true
REM ========================================================================

10 PRINT "=== Testing WHILE Loop ==="
20 PRINT ""

REM Test basic WHILE loop
30 PRINT "Testing Basic WHILE (count to 5):"
40 counter% = 1
50 WHILE counter% <= 5
60   PRINT counter%;
70   counter% = counter% + 1
80 END-WHILE
90 PRINT ""

REM Test WHILE with condition
100 PRINT "Testing WHILE with Condition:"
110 sum% = 0
120 i% = 1
130 WHILE sum% < 15
140   sum% = sum% + i%
150   i% = i% + 1
160 END-WHILE
170 PRINT "Sum reached: "; sum%
180 IF sum% < 15 THEN GOTO 9000

REM Test WHILE that doesn't execute
190 PRINT "Testing WHILE with False Initial Condition:"
200 x% = 10
210 WHILE x% < 5
220   PRINT "ERROR: This should not print"
230   GOTO 9000
240 END-WHILE
250 PRINT "WHILE correctly skipped (correct)"

REM Test nested WHILE loops
260 PRINT "Testing Nested WHILE:"
270 outer% = 1
280 WHILE outer% <= 2
290   PRINT "Outer: "; outer%
300   inner% = 1
310   WHILE inner% <= 2
320     PRINT "  Inner: "; inner%
330     inner% = inner% + 1
340   END-WHILE
350   outer% = outer% + 1
360 END-WHILE

370 PRINT ""
380 PRINT "=== All WHILE Loop Tests PASSED ==="
390 END

9000 PRINT "ERROR: Test failed!"
9010 END
