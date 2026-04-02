REM ========================================================================
REM Test: IF-THEN-ELSE Statement
REM Description: Tests IF-THEN-ELSE control flow with various conditions
REM Expected: Correct branches should be executed based on conditions
REM ========================================================================

10 PRINT "=== Testing IF-THEN-ELSE Statement ==="
20 PRINT ""

REM Test simple IF-THEN
30 PRINT "Testing Simple IF-THEN:"
40 x% = 5
50 IF x% == 5 THEN PRINT "x% is 5 (correct)"
60 IF x% == 5 THEN GOTO 70
65 GOTO 9000

REM Test IF-THEN with block
70 PRINT "Testing IF-THEN Block:"
80 y% = 10
90 IF y% > 5 THEN
100   PRINT "y% is greater than 5 (correct)"
110   PRINT "This is inside the IF block"
120 END-IF

REM Test IF-THEN-ELSE
130 PRINT "Testing IF-THEN-ELSE:"
140 z% = 3
150 IF z% > 5 THEN
160   PRINT "ERROR: This should not print"
170   GOTO 9000
180 ELSE
190   PRINT "z% is not greater than 5 (correct)"
200 END-IF

REM Test nested IF statements
210 PRINT "Testing Nested IF:"
220 a% = 7
230 IF a% > 5 THEN
240   PRINT "a% is greater than 5"
250   IF a% < 10 THEN
260     PRINT "a% is also less than 10 (correct)"
270   END-IF
280 END-IF

REM Test IF with multiple conditions
290 PRINT "Testing IF with AND:"
300 b% = 8
310 IF b% > 5 AND b% < 10 THEN
320   PRINT "b% is between 5 and 10 (correct)"
330 END-IF

340 PRINT "Testing IF with OR:"
350 c% = 12
360 IF c% < 5 OR c% > 10 THEN
370   PRINT "c% is outside 5-10 range (correct)"
380 END-IF

REM Test IF-THEN with direct GOTO
390 PRINT "Testing IF-THEN with direct GOTO:"
400 d% = 15
410 IF d% == 15 THEN 430
420 GOTO 9000
430 PRINT "Direct GOTO after THEN works (correct)"

440 PRINT ""
450 PRINT "=== All IF-THEN-ELSE Tests PASSED ==="
460 END

9000 PRINT "ERROR: Test failed!"
9010 END
