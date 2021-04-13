10 REM Test READ / DATA Structure
20 PRINT "Test Series 6"
30 READ A#, B$
40 PRINT B$
50 READ A$, B#
60 PRINT A$
100 @PRAGMA("Trace" = "on")
240 FOR I% = 0 TO 9 STEP 1
250 READ A$(I%)
260 NEXT
270 FOR I% = 0 TO 9 STEP 1
280 PRINT A$(I%), " ";
290 NEXT
300 PRINT " "
310 PRINT "Testing IF - THEN - ELSE "
320 FOR D# = 1 TO 3 STEP 1
330 IF D# == 2 THEN
340 PRINT D#, " is even"
350 ELSE
360 PRINT D#, " is odd"
370 END-IF
380 NEXT
400 PRINT "Testing colon to split lines..."
410 A$ = "Start":B$ = "End"
420 PRINT A$, " ... ", B$
430 PRINT "Test done..."
500 PRINT "Testing new maths functions..."
510 A% = 3 % 2
520 PRINT "3 mod 2 = ", A%
530 A% = 4 : B% = 1 : A% = A% << B%
540 PRINT "4 << 1 = ", A%
550 A% = 4 : B% = 1 : A% = A% >> B%
560 PRINT "4 >> 1 = ", A%
800 @PRAGMA( "Trace" = "off" )
810 DATA 123, "Test"
820 DATA "Test 2", 456
830 DATA "Mister", "Herr", "Senior", "De Heer", "Monsigneur"
840 DATA "Miss", "Frau", "Seniora", "Mvr", "Madam"
900 PRINT "Pragma Test"
910 @PRAGMA( "Logger" = "debug" )
920 PRINT "Testing..."
930 @PRAGMA( "Logger" = "off" )
940 PRINT "Test done..."
999 END
