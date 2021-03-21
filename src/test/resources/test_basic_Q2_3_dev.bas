10 REM Test READ / DATA Structure
20 PRINT "Test Series 6"
30 READ A#, B$
40 PRINT B$
50 READ A$, B#
60 PRINT A$
240 FOR I% = 0 TO 9 STEP 1
250 READ A$(I%)
260 NEXT
270 FOR I% = 0 TO 9 STEP 1
280 PRINT A$(I%), " ";
290 NEXT
300 PRINT ""
800 PRINT "Test done..."
810 DATA 123, "Test"
820 DATA "Test 2", 456
830 DATA "Mister", "Herr", "Senior", "De Heer", "Monsigneur"
840 DATA "Miss", "Frau", "Seniora", "Mvr", "Madam"
999 END
