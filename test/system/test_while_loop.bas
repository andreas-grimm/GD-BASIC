1 REM ========================================================================
2 REM Test: WHILE Loop
3 REM Description: Tests WHILE loop with various conditions
4 REM Expected: Loops should execute while condition is true
5 REM ========================================================================

10 PRINT "=== Testing WHILE Loop ==="
20 PRINT ""

30 REM Test basic WHILE loop
40 PRINT "Testing Basic WHILE (count to 5):"
50 counter% = 1
60 WHILE counter% <= 5
70   PRINT counter%;
80   counter% = counter% + 1
90 END-WHILE
100 PRINT ""

110 REM Test WHILE with condition
120 PRINT "Testing WHILE with Condition:"
130 sum% = 0
140 i% = 1
150 WHILE sum% < 15
160   sum% = sum% + i%
170   i% = i% + 1
180 END-WHILE
190 PRINT "Sum reached: "; sum%
200 IF sum% < 15 THEN GOTO 9000

210 REM Test WHILE that doesn't execute
220 PRINT "Testing WHILE with False Initial Condition:"
230 x% = 10
240 WHILE x% < 5
250   PRINT "ERROR: This should not print"
260   GOTO 9000
270 END-WHILE
280 PRINT "WHILE correctly skipped (correct)"

290 REM Test nested WHILE loops
300 PRINT "Testing Nested WHILE:"
310 outer% = 1
320 WHILE outer% <= 2
330   PRINT "Outer: "; outer%
340   inner% = 1
350   WHILE inner% <= 2
360     PRINT "  Inner: "; inner%
370     inner% = inner% + 1
380   END-WHILE
390   outer% = outer% + 1
400 END-WHILE

410 PRINT ""
420 PRINT "=== All WHILE Loop Tests PASSED ==="
430 END

9000 PRINT "ERROR: Test failed!"
9010 END
