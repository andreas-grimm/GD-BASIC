REM ========================================================================
REM Test: Mathematical Functions
REM Description: Tests all math functions (ABS, ATN, CDBL, CINT, COS, EXP, LOG, LOG10, SIN, SQR, TAN)
REM Expected: All mathematical functions should return correct values
REM ========================================================================

10 PRINT "=== Testing Mathematical Functions ==="
20 PRINT ""

REM Test ABS function
30 PRINT "Testing ABS (Absolute Value):"
40 a# = ABS(-5)
50 PRINT "ABS(-5) = "; a#
60 IF a# != 5 THEN GOTO 9000
70 b# = ABS(3.14)
80 PRINT "ABS(3.14) = "; b#
90 IF b# != 3.14 THEN GOTO 9000

REM Test SQR function
100 PRINT "Testing SQR (Square Root):"
110 c# = SQR(16)
120 PRINT "SQR(16) = "; c#
130 IF c# != 4 THEN GOTO 9000
140 d# = SQR(2)
150 PRINT "SQR(2) = "; d#

REM Test CINT function
160 PRINT "Testing CINT (Convert to Integer):"
170 e% = CINT(3.7)
180 PRINT "CINT(3.7) = "; e%
190 IF e% != 4 THEN GOTO 9000
200 f% = CINT(3.2)
210 PRINT "CINT(3.2) = "; f%
220 IF f% != 3 THEN GOTO 9000

REM Test CDBL function
230 PRINT "Testing CDBL (Convert to Double):"
240 g# = CDBL(5)
250 PRINT "CDBL(5) = "; g#
260 IF g# != 5 THEN GOTO 9000

REM Test SIN function
270 PRINT "Testing SIN (Sine):"
280 h# = SIN(0)
290 PRINT "SIN(0) = "; h#
300 IF h# != 0 THEN GOTO 9000

REM Test COS function
310 PRINT "Testing COS (Cosine):"
320 i# = COS(0)
330 PRINT "COS(0) = "; i#
340 IF i# != 1 THEN GOTO 9000

REM Test TAN function
350 PRINT "Testing TAN (Tangent):"
360 j# = TAN(0)
370 PRINT "TAN(0) = "; j#
380 IF j# != 0 THEN GOTO 9000

REM Test ATN function
390 PRINT "Testing ATN (Arctangent):"
400 k# = ATN(0)
410 PRINT "ATN(0) = "; k#
420 IF k# != 0 THEN GOTO 9000

REM Test EXP function
430 PRINT "Testing EXP (Exponential):"
440 l# = EXP(0)
450 PRINT "EXP(0) = "; l#
460 IF l# != 1 THEN GOTO 9000

REM Test LOG function
470 PRINT "Testing LOG (Natural Logarithm):"
480 m# = LOG(1)
490 PRINT "LOG(1) = "; m#
500 IF m# != 0 THEN GOTO 9000

REM Test LOG10 function
510 PRINT "Testing LOG10 (Base-10 Logarithm):"
520 n# = LOG10(1)
530 PRINT "LOG10(1) = "; n#
540 IF n# != 0 THEN GOTO 9000
550 o# = LOG10(100)
560 PRINT "LOG10(100) = "; o#
570 IF o# != 2 THEN GOTO 9000

REM Test RND function
580 PRINT "Testing RND (Random Number):"
590 r# = RND(1)
600 PRINT "RND(1) = "; r#
610 IF r# < 0 THEN GOTO 9000
620 IF r# > 1 THEN GOTO 9000
630 PRINT "RND returned value between 0 and 1 (correct)"

640 PRINT ""
650 PRINT "=== All Mathematical Function Tests PASSED ==="
660 END

9000 PRINT "ERROR: Test failed!"
9010 END
