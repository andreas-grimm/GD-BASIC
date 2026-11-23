10 REM Test: BCD (#) maths functions with 40 significant digits
20 PRINT "=== Testing BCD functions ==="
30 two# = 2
40 r# = SQR(two#)
50 s$ = STR$(r#)
60 PRINT "SQR(2) = ", s$
70 IF s$ != "1.41421356237309504880168872420969807857" THEN 9000
80 one# = 1
90 e# = EXP(one#)
100 s$ = STR$(e#)
110 PRINT "EXP(1) = ", s$
120 IF s$ != "2.718281828459045235360287471352662497757" THEN 9000
130 ten# = 10
140 l# = LOG(ten#)
150 s$ = STR$(l#)
160 PRINT "LOG(10) = ", s$
170 IF s$ != "2.302585092994045684017991454684364207601" THEN 9000
180 si# = SIN(one#)
190 s$ = STR$(si#)
200 PRINT "SIN(1) = ", s$
210 IF s$ != "0.8414709848078965066525023216302989996226" THEN 9000
220 n# = 0 - 2.5
230 a# = ABS(n#)
240 s$ = STR$(a#)
250 PRINT "ABS(-2.5) = ", s$
260 IF s$ != "2.5" THEN 9000
270 x# = 3.9
280 f% = CINT(x#)
290 PRINT "CINT(3.9) = ", f%
300 IF f% != 3 THEN 9000
310 PRINT "=== All BCD function tests PASSED ==="
320 END
9000 PRINT "ERROR: Test failed!"
9010 END
