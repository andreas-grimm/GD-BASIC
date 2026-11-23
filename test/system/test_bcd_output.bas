10 REM Test: BCD (#) output format - plain decimal, no trailing zeros, no exponent
20 PRINT "=== Testing BCD output format ==="
30 v# = 2.50
40 s$ = STR$(v#)
50 PRINT "2.50 prints as ", s$
60 IF s$ != "2.5" THEN 9000
70 w# = 1000
80 s$ = STR$(w#)
90 PRINT "1000 prints as ", s$
100 IF s$ != "1000" THEN 9000
110 one# = 1
120 eight# = 8
130 h# = one# / eight#
140 s$ = STR$(h#)
150 PRINT "1 / 8 prints as ", s$
160 IF s$ != "0.125" THEN 9000
170 tiny# = 0.000001
180 s$ = STR$(tiny#)
190 PRINT "0.000001 prints as ", s$
200 IF s$ != "0.000001" THEN 9000
210 PRINT "=== All BCD output tests PASSED ==="
220 END
9000 PRINT "ERROR: Test failed!"
9010 END
