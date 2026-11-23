1 REM ========================================================================
2 REM Compiler test: control flow
3 REM Description: IF-THEN, GOTO, GOSUB, FOR with negative STEP, nested FOR,
4 REM              WHILE, DO-UNTIL. The compiled program must print exactly what the
5 REM              interpreter prints.
6 REM ========================================================================
10 PRINT "start"
20 X% = 4
30 IF X% > 3 THEN 60
40 PRINT "not printed"
50 GOTO 70
60 PRINT "IF true branch"
65 GOSUB 500
70 FOR K% = 10 TO 1 STEP -3
80 PRINT K%
90 NEXT
100 FOR O% = 1 TO 2
110 FOR J% = 1 TO 2
120 PRINT O%; J%
130 NEXT
140 NEXT
150 N% = 0
160 WHILE N% < 3
170 N% = N% + 1
180 GOSUB 600
190 END-WHILE
200 M% = 0
210 DO
220 M% = M% + 2
230 PRINT "M="; M%
240 UNTIL M% >= 6
250 FOR Z% = 5 TO 1
260 PRINT "once Z="; Z%
270 NEXT
280 PRINT "done"
290 END
500 PRINT "in sub 500"
510 RETURN
600 PRINT "in sub 600 n=", N%
610 RETURN
