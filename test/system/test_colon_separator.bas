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
60 PRINT "a% = "; a%; ", b% = "; b%; ", c% = "; c%
70 IF a% != 5 THEN GOTO 9000
80 IF b% != 10 THEN GOTO 9000
90 IF c% != 15 THEN GOTO 9000

100 REM Test colon with PRINT
110 PRINT "Testing Colon with PRINT:"
120 PRINT "First" : PRINT "Second" : PRINT "Third"

130 REM Test colon with calculations
140 PRINT "Testing Colon with Calculations:"
150 x% = 2 : y% = 3 : z% = x% * y%
160 PRINT "x% * y% = "; z%
170 IF z% != 6 THEN GOTO 9000

180 PRINT ""
190 PRINT "=== All Colon Separator Tests PASSED ==="
200 END

9000 PRINT "ERROR: Test failed!"
9010 END
