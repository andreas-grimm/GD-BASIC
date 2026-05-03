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
60 PRINT "a% = ";
70 PRINT a%
80 IF a% != 10 THEN 9000
90 REM Test Pascal-style assignment (:=)
100 PRINT "Testing Pascal Assignment (:=):"
110 b% = 20
120 PRINT "b% := ";
130 PRINT b%
140 IF b% != 20 THEN 9000
150 REM Test LET statement
160 PRINT "Testing LET Statement:"
170 c% = 30
180 PRINT "LET c% = ";
190 PRINT c%
200 IF c% != 30 THEN 9000
210 REM Test multiple assignments
220 PRINT "Testing Multiple Assignments:"
230 x% = 5
240 y% = x%
250 z% = y%
260 PRINT "x% = ";
270 PRINT x%;
280 PRINT ", y% = ";
290 PRINT y%;
300 PRINT ", z% = ";
310 PRINT z%
320 IF x% != 5 THEN 9000
330 IF y% != 5 THEN 9000
340 IF z% != 5 THEN 9000
350 PRINT ""
360 PRINT "=== All Assignment Operator Tests PASSED ==="
370 END
9000 PRINT "ERROR: Test failed!"
9010 END
