1 REM ========================================================================
2 REM Test: Assignment Operators
3 REM Description: Tests different assignment operators (=, :=)
4 REM Expected: Both assignment operators should work correctly
5 REM ========================================================================

10 PRINT "=== Testing Assignment Operators ==="
20 PRINT ""

30 REM Test standard assignment (=)
40 PRINT "Testing Standard Assignment (=):"
50 a% = 10
60 PRINT "a% = "; a%
70 IF a% != 10 THEN GOTO 9000

80 REM Test Pascal-style assignment (:=)
90 PRINT "Testing Pascal Assignment (:=):"
100 b% := 20
110 PRINT "b% := "; b%
120 IF b% != 20 THEN GOTO 9000

130 REM Test LET statement
140 PRINT "Testing LET Statement:"
150 LET c% = 30
160 PRINT "LET c% = "; c%
170 IF c% != 30 THEN GOTO 9000

180 REM Test multiple assignments
190 PRINT "Testing Multiple Assignments:"
200 x% = 5
210 y% = x%
220 z% = y%
230 PRINT "x% = "; x%; ", y% = "; y%; ", z% = "; z%
240 IF x% != 5 THEN GOTO 9000
250 IF y% != 5 THEN GOTO 9000
260 IF z% != 5 THEN GOTO 9000

270 PRINT ""
280 PRINT "=== All Assignment Operator Tests PASSED ==="
290 END

9000 PRINT "ERROR: Test failed!"
9010 END
