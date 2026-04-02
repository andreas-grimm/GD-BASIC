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
60 PRINT "ABS(-5) = "; a#
70 IF a# != 5 THEN GOTO 9000
80 b# = ABS(3.14)
90 PRINT "ABS(3.14) = "; b#
100 IF b# != 3.14 THEN GOTO 9000

110 REM Test SQR function
120 PRINT "Testing SQR (Square Root):"
130 c# = SQR(16)
140 PRINT "SQR(16) = "; c#
150 IF c# != 4 THEN GOTO 9000
160 d# = SQR(2)
170 PRINT "SQR(2) = "; d#

180 REM Test CINT function
190 PRINT "Testing CINT (Convert to Integer):"
200 e% = CINT(3.7)
210 PRINT "CINT(3.7) = "; e%
220 IF e% != 4 THEN GOTO 9000
230 f% = CINT(3.2)
240 PRINT "CINT(3.2) = "; f%
250 IF f% != 3 THEN GOTO 9000

260 REM Test CDBL function
270 PRINT "Testing CDBL (Convert to Double):"
280 g# = CDBL(5)
290 PRINT "CDBL(5) = "; g#
300 IF g# != 5 THEN GOTO 9000

310 REM Test SIN function
320 PRINT "Testing SIN (Sine):"
330 h# = SIN(0)
340 PRINT "SIN(0) = "; h#
350 IF h# != 0 THEN GOTO 9000

360 REM Test COS function
370 PRINT "Testing COS (Cosine):"
380 i# = COS(0)
390 PRINT "COS(0) = "; i#
400 IF i# != 1 THEN GOTO 9000

410 REM Test TAN function
420 PRINT "Testing TAN (Tangent):"
430 j# = TAN(0)
440 PRINT "TAN(0) = "; j#
450 IF j# != 0 THEN GOTO 9000

460 REM Test ATN function
470 PRINT "Testing ATN (Arctangent):"
480 k# = ATN(0)
490 PRINT "ATN(0) = "; k#
500 IF k# != 0 THEN GOTO 9000

510 REM Test EXP function
520 PRINT "Testing EXP (Exponential):"
530 l# = EXP(0)
540 PRINT "EXP(0) = "; l#
550 IF l# != 1 THEN GOTO 9000

560 REM Test LOG function
570 PRINT "Testing LOG (Natural Logarithm):"
580 m# = LOG(1)
590 PRINT "LOG(1) = "; m#
600 IF m# != 0 THEN GOTO 9000

610 REM Test LOG10 function
620 PRINT "Testing LOG10 (Base-10 Logarithm):"
630 n# = LOG10(1)
640 PRINT "LOG10(1) = "; n#
650 IF n# != 0 THEN GOTO 9000
660 o# = LOG10(100)
670 PRINT "LOG10(100) = "; o#
680 IF o# != 2 THEN GOTO 9000

690 REM Test RND function
700 PRINT "Testing RND (Random Number):"
710 r# = RND(1)
720 PRINT "RND(1) = "; r#
730 IF r# < 0 THEN GOTO 9000
740 IF r# > 1 THEN GOTO 9000
750 PRINT "RND returned value between 0 and 1 (correct)"

760 PRINT ""
770 PRINT "=== All Mathematical Function Tests PASSED ==="
780 END

9000 PRINT "ERROR: Test failed!"
9010 END
