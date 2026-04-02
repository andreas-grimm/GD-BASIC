REM ========================================================================
REM Test: Bitwise Operators
REM Description: Tests bitwise shift operators (<<, >>)
REM Expected: All bitwise operations should produce correct results
REM ========================================================================

10 PRINT "=== Testing Bitwise Operators ==="
20 PRINT ""

REM Test left shift
30 PRINT "Testing Left Shift (<<):"
40 a% = 4 << 1
50 PRINT "4 << 1 = "; a%
60 IF a% != 8 THEN GOTO 9000

70 b% = 3 << 2
80 PRINT "3 << 2 = "; b%
90 IF b% != 12 THEN GOTO 9000

REM Test right shift
100 PRINT "Testing Right Shift (>>):"
110 c% = 16 >> 1
120 PRINT "16 >> 1 = "; c%
130 IF c% != 8 THEN GOTO 9000

140 d% = 20 >> 2
150 PRINT "20 >> 2 = "; d%
160 IF d% != 5 THEN GOTO 9000

170 PRINT ""
180 PRINT "=== All Bitwise Operator Tests PASSED ==="
190 END

9000 PRINT "ERROR: Test failed!"
9010 END
