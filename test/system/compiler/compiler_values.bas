1 REM ========================================================================
2 REM Compiler test: values and built-in functions
3 REM Description: Arithmetic, strings, functions and number types. The compiled
4 REM              program must print exactly what the interpreter prints.
5 REM ========================================================================
10 A = 5
20 B = -2.5
30 PRINT "A+B = ", A + B
40 PRINT "A*B = "; A * B
50 PRINT "A/2 = ", A / 2
60 PRINT "2^10 = ", 2 ^ 10
70 PRINT "A%B = ", 17 % 5
80 PRINT "neg: ", 0 - A
90 PRINT "ABS: ", ABS(B)
100 PRINT "SQR: ", SQR(16)
110 PRINT "LEN: ", LEN("HELLO")
120 PRINT "LEFT: ", LEFT$("BASIC", 3)
130 PRINT "RIGHT: ", RIGHT$("BASIC", 2)
140 PRINT "STR: ", STR$(42)
150 PRINT "VAL: ", VAL("12") + 1
160 I% = 7
170 L& = 100000
180 PRINT "int: ", I% * 3
190 PRINT "long: ", L& + 1
200 D# = 0.1
210 E# = D# + 0.2
220 PRINT "bcd: ", STR$(E#)
230 T! = 1
240 PRINT "bool: ", T!
250 PRINT "cmp: ", A > B, A == 5
260 PRINT "and: ", (A > 1) AND (B < 0)
270 PRINT "or: ", (A < 1) OR (B < 0)
280 PRINT "not: ", NOT (A > 1)
290 S$ = "AB" + "CD"
300 PRINT S$
310 PRINT "empty:"
320 PRINT
330 END
