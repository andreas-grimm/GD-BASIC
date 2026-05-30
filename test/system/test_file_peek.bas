10 REM =============================================================
20 REM File Peek Test
30 REM Tests: FPEEK - lookahead without advancing position
40 REM =============================================================
50 
60 PRINT "Testing: FPEEK - Character Lookahead"
70 GOSUB 1000
80 END
90 
1000 REM ============================================================
1010 REM Test 1: FPEEK does not advance position
1020 REM ============================================================
1030 PRINT ""
1040 PRINT "Test 1: FPEEK - Lookahead without advancing"
1050 
1060 REM Create test file
1070 FOPEN 1 "/tmp/test_peek.txt" "w"
1080 FPUT 1, "A"
1090 FPUT 1, "B"
1100 FPUT 1, "C"
1110 FCLOSE 1 ""
1120 
1130 REM Open for reading
1140 FOPEN 1 "/tmp/test_peek.txt" "r"
1150 
1160 REM Peek at first character
1170 FPEEK 1, C$
1180 IF C$ != "A" THEN GOTO 9000
1190 
1200 REM Peek again - should return same character
1210 FPEEK 1, C2$
1220 IF C2$ != "A" THEN GOTO 9000
1230 
1240 REM Now actually read it
1250 FGET 1, ACTUAL$
1260 IF ACTUAL$ != "A" THEN GOTO 9000
1270 
1280 REM Peek next
1290 FPEEK 1, NEXT_CHAR$
1300 IF NEXT_CHAR$ != "B" THEN GOTO 9000
1310 
1320 FCLOSE 1 "DELETE"
1330 PRINT "  PASSED: FPEEK does not advance position"
1340 
1350 RETURN
1360 
9000 REM Error handler
9010 PRINT "  ERROR: Test failed"
9020 END
