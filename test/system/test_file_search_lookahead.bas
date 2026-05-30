10 REM =============================================================
20 REM File Search with Lookahead Test
30 REM Tests: FPEEK for conditional character processing
40 REM =============================================================
50 
60 PRINT "Testing: File Search with Lookahead (FPEEK)"
70 GOSUB 1000
80 END
90 
1000 REM ============================================================
1010 REM Test 1: Search and count specific characters
1020 REM ============================================================
1030 PRINT ""
1040 PRINT "Test 1: FPEEK - Search and count characters"
1050 
1060 REM Create test file with multiple X characters
1070 FOPEN 1 "/tmp/test_search.txt" "w"
1080 FPUT 1, "X"
1090 FPUT 1, "a"
1100 FPUT 1, "X"
1110 FPUT 1, "b"
1120 FPUT 1, "X"
1130 FPUT 1, "c"
1140 FCLOSE 1 ""
1150 
1160 REM Open for reading
1170 FOPEN 1 "/tmp/test_search.txt" "r"
1180 FOUND% = 0
1190 
1200 REM Search using FPEEK lookahead
1210 WHILE NOT EOF(1)
1220    FPEEK 1, C$
1230    IF C$ != "EOF" THEN
1240       IF C$ = "X" THEN
1250          FGET 1, CHAR$
1260          FOUND% = FOUND% + 1
1270       ELSE
1280          FGET 1, DUMMY$
1290       END IF
1300    END IF
1310 WEND
1320 
1330 IF FOUND% != 3 THEN GOTO 9000
1340 
1350 FCLOSE 1 "DELETE"
1360 PRINT "  PASSED: FPEEK search found 3 X characters"
1370 
1380 RETURN
1390 
9000 REM Error handler
9010 PRINT "  ERROR: Test failed"
9020 END
