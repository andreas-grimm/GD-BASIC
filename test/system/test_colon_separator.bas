1 REM ========================================================================
2 REM Test: Colon Statement Separator
3 REM Description: Tests using colon (:) to separate multiple statements on one line
4 REM Expected: Multiple statements on one line should execute correctly
5 REM ========================================================================
10 PRINT "=== Testing Colon Statement Separator ==="
20 PRINT ""
30 REM Test basic colon usage
40 PRINT "Testing Basic Colon Usage:"
50 a% = 5 : b% = 10 : c% = 15
60 PRINT "a% = ";
70 PRINT a%;
80 PRINT ", b% = ";
90 PRINT b%;
100 PRINT ", c% = ";
110 PRINT c%
120 IF a% != 5 THEN 9000
130 IF b% != 10 THEN 9000
140 IF c% != 15 THEN 9000
150 REM Test colon with PRINT
160 PRINT "Testing Colon with PRINT:"
170 PRINT "First" : PRINT "Second" : PRINT "Third"
180 REM Test colon with calculations
190 PRINT "Testing Colon with Calculations:"
200 x% = 2 : y% = 3 : z% = x% * y%
210 PRINT "x% * y% = ";
220 PRINT z%
230 IF z% != 6 THEN 9000
240 PRINT ""
250 PRINT "=== All Colon Separator Tests PASSED ==="
260 END
9000 PRINT "ERROR: Test failed!"
9010 END
