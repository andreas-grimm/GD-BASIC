1 REM ========================================================================
2 REM Test: Bitwise Operators
3 REM Description: Tests bitwise shift operators (<<, >>)
4 REM Expected: All bitwise operations should produce correct results
5 REM ========================================================================

10 PRINT "=== Testing Bitwise Operators ==="
20 PRINT ""

30 REM Test left shift
40 PRINT "Testing Left Shift (<<):"
50 a% = 4 << 1
60 PRINT "4 << 1 = "; a%
70 IF a% != 8 THEN GOTO 9000

80 b% = 3 << 2
90 PRINT "3 << 2 = "; b%
100 IF b% != 12 THEN GOTO 9000

110 REM Test right shift
120 PRINT "Testing Right Shift (>>):"
130 c% = 16 >> 1
140 PRINT "16 >> 1 = "; c%
150 IF c% != 8 THEN GOTO 9000

160 d% = 20 >> 2
170 PRINT "20 >> 2 = "; d%
180 IF d% != 5 THEN GOTO 9000

190 PRINT ""
200 PRINT "=== All Bitwise Operator Tests PASSED ==="
210 END

9000 PRINT "ERROR: Test failed!"
9010 END
