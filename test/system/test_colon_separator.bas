REM ========================================================================
REM Test: Colon Statement Separator
REM Description: Tests using colon (:) to separate multiple statements on one line
REM Expected: Multiple statements on one line should execute correctly
REM ========================================================================

10 PRINT "=== Testing Colon Statement Separator ==="
20 PRINT ""

REM Test basic colon usage
30 PRINT "Testing Basic Colon Usage:"
40 a% = 5 : b% = 10 : c% = 15
50 PRINT "a% = "; a%; ", b% = "; b%; ", c% = "; c%
60 IF a% != 5 THEN GOTO 9000
70 IF b% != 10 THEN GOTO 9000
80 IF c% != 15 THEN GOTO 9000

REM Test colon with PRINT
90 PRINT "Testing Colon with PRINT:"
100 PRINT "First" : PRINT "Second" : PRINT "Third"

REM Test colon with calculations
110 PRINT "Testing Colon with Calculations:"
120 x% = 2 : y% = 3 : z% = x% * y%
130 PRINT "x% * y% = "; z%
140 IF z% != 6 THEN GOTO 9000

150 PRINT ""
160 PRINT "=== All Colon Separator Tests PASSED ==="
170 END

9000 PRINT "ERROR: Test failed!"
9010 END
