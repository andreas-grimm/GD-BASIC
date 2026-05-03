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
60 PRINT "4 << 1 = ";
70 PRINT a%
80 IF a% != 8 THEN 9000
90 b% = 3 << 2
100 PRINT "3 << 2 = ";
110 PRINT b%
120 IF b% != 12 THEN 9000
130 REM Test right shift
140 PRINT "Testing Right Shift (>>):"
150 c% = 16 >> 1
160 PRINT "16 >> 1 = ";
170 PRINT c%
180 IF c% != 8 THEN 9000
190 d% = 20 >> 2
200 PRINT "20 >> 2 = ";
210 PRINT d%
220 IF d% != 5 THEN 9000
230 PRINT ""
240 PRINT "=== All Bitwise Operator Tests PASSED ==="
250 END
9000 PRINT "ERROR: Test failed!"
9010 END
