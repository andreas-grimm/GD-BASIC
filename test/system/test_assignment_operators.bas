REM ========================================================================
REM Test: Assignment Operators
REM Description: Tests different assignment operators (=, :=)
REM Expected: Both assignment operators should work correctly
REM ========================================================================

10 PRINT "=== Testing Assignment Operators ==="
20 PRINT ""

REM Test standard assignment (=)
30 PRINT "Testing Standard Assignment (=):"
40 a% = 10
50 PRINT "a% = "; a%
60 IF a% != 10 THEN GOTO 9000

REM Test Pascal-style assignment (:=)
70 PRINT "Testing Pascal Assignment (:=):"
80 b% := 20
90 PRINT "b% := "; b%
100 IF b% != 20 THEN GOTO 9000

REM Test LET statement
110 PRINT "Testing LET Statement:"
120 LET c% = 30
130 PRINT "LET c% = "; c%
140 IF c% != 30 THEN GOTO 9000

REM Test multiple assignments
150 PRINT "Testing Multiple Assignments:"
160 x% = 5
170 y% = x%
180 z% = y%
190 PRINT "x% = "; x%; ", y% = "; y%; ", z% = "; z%
200 IF x% != 5 THEN GOTO 9000
210 IF y% != 5 THEN GOTO 9000
220 IF z% != 5 THEN GOTO 9000

230 PRINT ""
240 PRINT "=== All Assignment Operator Tests PASSED ==="
250 END

9000 PRINT "ERROR: Test failed!"
9010 END
