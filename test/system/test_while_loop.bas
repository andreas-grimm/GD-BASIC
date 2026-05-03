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
190 PRINT "Sum reached: ";
200 PRINT sum%
210 IF sum% < 15 THEN 9000
220 REM Test WHILE that doesn't execute
230 PRINT "Testing WHILE with False Initial Condition:"
240 x% = 10
250 WHILE x% < 5
260   PRINT "ERROR: This should not print"
270   GOTO 9000
280 END-WHILE
290 PRINT "WHILE correctly skipped (correct)"
300 REM Test nested WHILE loops
310 PRINT "Testing Nested WHILE:"
320 outer% = 1
330 WHILE outer% <= 2
340   PRINT "Outer: ";
350   PRINT outer%
360   inner% = 1
370   WHILE inner% <= 2
380     PRINT "  Inner: ";
390     PRINT inner%
400     inner% = inner% + 1
410   END-WHILE
420   outer% = outer% + 1
430 END-WHILE
440 PRINT ""
450 PRINT "=== All WHILE Loop Tests PASSED ==="
460 END
9000 PRINT "ERROR: Test failed!"
9010 END
