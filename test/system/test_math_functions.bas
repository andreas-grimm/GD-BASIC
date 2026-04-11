1 REM ========================================================================
2 REM Test: Mathematical Functions
3 REM Description: Tests all math functions (ABS, ATN, CDBL, CINT, COS, EXP, LOG, LOG10, SIN, SQR, TAN)
4 REM Expected: All mathematical functions should return correct values
5 REM ========================================================================
10 PRINT "=== Testing Mathematical Functions ==="
20 PRINT ""
30 REM Test ABS function
40 PRINT "Testing ABS (Absolute Value):"
50 a# = ABS(-5)
60 PRINT "ABS(-5) = ";
70 PRINT a#
80 IF a# != 5 THEN 9000
90 b# = ABS(3.14)
100 PRINT "ABS(3.14) = ";
110 PRINT b#
120 IF b# != 3.14 THEN 9000
130 REM Test SQR function
140 PRINT "Testing SQR (Square Root):"
150 c# = SQR(16)
160 PRINT "SQR(16) = ";
170 PRINT c#
180 IF c# != 4 THEN 9000
190 d# = SQR(2)
200 PRINT "SQR(2) = ";
210 PRINT d#
220 REM Test CINT function
230 PRINT "Testing CINT (Convert to Integer):"
240 e% = CINT(3.7)
250 PRINT "CINT(3.7) = ";
260 PRINT e%
270 IF e% != 4 THEN 9000
280 f% = CINT(3.2)
290 PRINT "CINT(3.2) = ";
300 PRINT f%
310 IF f% != 3 THEN 9000
320 REM Test CDBL function
330 PRINT "Testing CDBL (Convert to Double):"
340 g# = CDBL(5)
350 PRINT "CDBL(5) = ";
360 PRINT g#
370 IF g# != 5 THEN 9000
380 REM Test SIN function
390 PRINT "Testing SIN (Sine):"
400 h# = SIN(0)
410 PRINT "SIN(0) = ";
420 PRINT h#
430 IF h# != 0 THEN 9000
440 REM Test COS function
450 PRINT "Testing COS (Cosine):"
460 i# = COS(0)
470 PRINT "COS(0) = ";
480 PRINT i#
490 IF i# != 1 THEN 9000
500 REM Test TAN function
510 PRINT "Testing TAN (Tangent):"
520 j# = TAN(0)
530 PRINT "TAN(0) = ";
540 PRINT j#
550 IF j# != 0 THEN 9000
560 REM Test ATN function
570 PRINT "Testing ATN (Arctangent):"
580 k# = ATN(0)
590 PRINT "ATN(0) = ";
600 PRINT k#
610 IF k# != 0 THEN 9000
620 REM Test EXP function
630 PRINT "Testing EXP (Exponential):"
640 l# = EXP(0)
650 PRINT "EXP(0) = ";
660 PRINT l#
670 IF l# != 1 THEN 9000
680 REM Test LOG function
690 PRINT "Testing LOG (Natural Logarithm):"
700 m# = LOG(1)
710 PRINT "LOG(1) = ";
720 PRINT m#
730 IF m# != 0 THEN 9000
740 REM Test LOG10 function
750 PRINT "Testing LOG10 (Base-10 Logarithm):"
760 n# = LOG10(1)
770 PRINT "LOG10(1) = ";
780 PRINT n#
790 IF n# != 0 THEN 9000
800 o# = LOG10(100)
810 PRINT "LOG10(100) = ";
820 PRINT o#
830 IF o# != 2 THEN 9000
840 REM Test RND function
850 PRINT "Testing RND (Random Number):"
860 r# = RND()
870 PRINT "RND() = ";
880 PRINT r#
890 IF r# < 0 THEN 9000
900 IF r# > 1 THEN 9000
910 PRINT "RND returned value between 0 and 1 (correct)"
920 PRINT ""
930 PRINT "=== All Mathematical Function Tests PASSED ==="
940 END
9000 PRINT "ERROR: Test failed!"
9010 END
