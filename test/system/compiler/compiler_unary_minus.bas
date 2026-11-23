1 REM ========================================================================
2 REM Compiler test: unary minus and operator spacing
3 REM Description: -A gives the value of A times -1, and the type of A is kept.
4 REM              A-B, 2*A and X=-A are read as A - B, 2 * A and X = -A.
5 REM              The compiled program must print exactly what the interpreter prints.
6 REM ========================================================================
10 A% = 5
20 B = 2.5
30 C# = 1.25
40 PRINT -A%
50 PRINT -B
60 PRINT -C#
70 PRINT A%-7
80 PRINT A% - 7
90 PRINT 2*A%
100 X = -A%
110 PRINT X
120 PRINT 1-A%
130 PRINT -5
140 PRINT 2 * -A%
150 IF A%<=B THEN 170
160 PRINT "NOT SMALLER"
170 PRINT "END"
180 END
