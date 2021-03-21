10 REM Test READ / DATA Struktur
20 PRINT "Test Series 6"
30 READ A#, B$
40 PRINT B$
50 READ A$, B#
60 PRINT A$
240 FOR I# = 0 TO 5 STEP 1
250 READ A$(I#)
260 NEXT
270 PRINT A$(3)
800 DATA 123, "Test"
810 DATA "Test 2", 456
820 DATA "Mister", "Herr", "Senior", "De Heer", "Monsigneur"
830 DATA "Miss", "Frau", "", "Mvr", "Madam"
999 END
