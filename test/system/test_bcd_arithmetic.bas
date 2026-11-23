10 REM Test: BCD (#) arithmetic with exact decimal results
20 REM Description: BCD values add, subtract, multiply and divide with 40 significant digits and no binary rounding.
30 PRINT "=== Testing BCD arithmetic ==="
40 x# = 0.1
50 y# = 0.2
60 z# = x# + y#
70 s$ = STR$(z#)
80 PRINT "0.1 + 0.2 = ", s$
90 IF s$ != "0.3" THEN 9000
100 one# = 1
110 three# = 3
120 q# = one# / three#
130 s$ = STR$(q#)
140 PRINT "1 / 3 = ", s$
150 IF s$ != "0.3333333333333333333333333333333333333333" THEN 9000
160 two# = 2
170 e# = two# / three#
180 s$ = STR$(e#)
190 PRINT "2 / 3 = ", s$
200 IF s$ != "0.6666666666666666666666666666666666666667" THEN 9000
210 m# = 1234.56 * 2
220 s$ = STR$(m#)
230 PRINT "1234.56 * 2 = ", s$
240 IF s$ != "2469.12" THEN 9000
250 n# = 0 - 2.5
260 s$ = STR$(n#)
270 PRINT "0 - 2.5 = ", s$
280 IF s$ != "-2.5" THEN 9000
290 i% = 5
300 t# = i% + 2.5
310 s$ = STR$(t#)
320 PRINT "5 + 2.5 = ", s$
330 IF s$ != "7.5" THEN 9000
340 IF q# <= 0.33 THEN 9000
350 IF q# > 0.34 THEN 9000
360 PRINT "=== All BCD arithmetic tests PASSED ==="
370 END
9000 PRINT "ERROR: Test failed!"
9010 END
