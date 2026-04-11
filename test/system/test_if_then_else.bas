1 REM ========================================================================
2 REM Test: IF-THEN-ELSE Statement
3 REM Description: Tests IF-THEN-ELSE control flow with various conditions
4 REM Expected: Correct branches should be executed based on conditions
5 REM ========================================================================
10 PRINT "=== Testing IF-THEN-ELSE Statement ==="
20 PRINT ""
30 REM Test simple IF-THEN
40 PRINT "Testing Simple IF-THEN:"
50 x% = 5
60 IF x% == 5 THEN PRINT "x% is 5 (correct)"
70 IF x% == 5 THEN 80
75 GOTO 9000
80 REM Test IF-THEN with block
90 PRINT "Testing IF-THEN Block:"
100 y% = 10
110 IF y% > 5 THEN
120   PRINT "y% is greater than 5 (correct)"
130   PRINT "This is inside the IF block"
140 END-IF
150 REM Test IF-THEN-ELSE
160 PRINT "Testing IF-THEN-ELSE:"
170 z% = 3
180 IF z% > 5 THEN
190   PRINT "ERROR: This should not print"
200   GOTO 9000
210 ELSE
220   PRINT "z% is not greater than 5 (correct)"
230 END-IF
240 REM Test nested IF statements
250 PRINT "Testing Nested IF:"
260 a% = 7
270 IF a% > 5 THEN
280   PRINT "a% is greater than 5"
290   IF a% < 10 THEN
300     PRINT "a% is also less than 10 (correct)"
310   END-IF
320 END-IF
330 REM Test IF with multiple conditions
340 PRINT "Testing IF with AND:"
350 b% = 8
360 IF b% > 5 AND b% < 10 THEN
370   PRINT "b% is between 5 and 10 (correct)"
380 END-IF
390 PRINT "Testing IF with OR:"
400 c% = 12
410 IF c% < 5 OR c% > 10 THEN
420   PRINT "c% is outside 5-10 range (correct)"
430 END-IF
440 REM Test IF-THEN with direct line number
450 PRINT "Testing IF-THEN with direct line number:"
460 d% = 15
470 IF d% == 15 THEN 490
480 GOTO 9000
490 PRINT "Direct line number after THEN works (correct)"
500 PRINT ""
510 PRINT "=== All IF-THEN-ELSE Tests PASSED ==="
520 END
9000 PRINT "ERROR: Test failed!"
9010 END
