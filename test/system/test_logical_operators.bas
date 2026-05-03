1 REM ========================================================================
2 REM Test: Logical Operators
3 REM Description: Tests logical operators (AND, OR, NOT)
4 REM Expected: All logical operations should evaluate correctly
5 REM ========================================================================
10 PRINT "=== Testing Logical Operators ==="
20 PRINT ""
30 REM Test AND operator
40 PRINT "Testing AND Operator:"
50 IF 1 == 1 AND 2 == 2 THEN
51 PRINT "TRUE AND TRUE = TRUE (correct)"
52 END-IF
60 IF 1 == 1 AND 2 == 2 THEN 70
65 GOTO 9000
70 IF 1 == 1 AND 2 == 3 THEN 9000
80 PRINT "TRUE AND FALSE = FALSE (correct)"
90 IF 1 == 2 AND 2 == 2 THEN 9000
100 PRINT "FALSE AND TRUE = FALSE (correct)"
110 REM Test OR operator
120 PRINT "Testing OR Operator:"
130 IF 1 == 1 OR 2 == 3 THEN
131 PRINT "TRUE OR FALSE = TRUE (correct)"
132 END-IF
140 IF 1 == 1 OR 2 == 3 THEN 150
145 GOTO 9000
150 IF 1 == 2 OR 2 == 2 THEN
151 PRINT "FALSE OR TRUE = TRUE (correct)"
152 END-IF
160 IF 1 == 2 OR 2 == 2 THEN 170
165 GOTO 9000
170 IF 1 == 2 OR 2 == 3 THEN 9000
180 PRINT "FALSE OR FALSE = FALSE (correct)"
190 REM Test NOT operator
200 PRINT "Testing NOT Operator:"
210 a! = 1 == 1
220 PRINT "a! = ";
230 PRINT a!
240 b! = NOT a!
250 IF b! THEN 9000
260 PRINT "NOT TRUE = FALSE (correct)"
270 c! = 1 == 2
280 PRINT "c! = ";
290 PRINT c!
300 d! = NOT c!
310 IF d! THEN
311 PRINT "NOT FALSE = TRUE (correct)"
312 END-IF
320 IF d! THEN 330
325 GOTO 9000
330 PRINT ""
340 PRINT "=== All Logical Operator Tests PASSED ==="
350 END
9000 PRINT "ERROR: Test failed!"
9010 END
