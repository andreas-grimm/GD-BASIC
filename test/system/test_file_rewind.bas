10 REM =============================================================
20 REM File Rewind Test
30 REM Tests: FREWIND - reset file position to beginning
40 REM =============================================================
50 
60 PRINT "Testing: FREWIND - File Position Reset"
70 GOSUB 1000
80 END
90 
1000 REM ============================================================
1010 REM Test 1: Multi-pass file processing with FREWIND
1020 REM ============================================================
1030 PRINT ""
1040 PRINT "Test 1: FREWIND - Multi-pass processing"
1050 
1060 REM Create test file
1070 FOPEN 1 "/tmp/test_rewind.txt" "w"
1080 FPRINT 1 "First"
1090 FPRINT 1 "Second"
1100 FPRINT 1 "Third"
1110 FCLOSE 1 ""
1120 
1130 REM Open for reading
1140 FOPEN 1 "/tmp/test_rewind.txt" "r"
1150 
1160 REM First pass - read first line
1170 FINPUT 1 LINE1$
1180 IF LINE1$ != "First" THEN GOTO 9000
1190 
1200 REM Second pass - rewind and read again
1210 FREWIND 1
1220 FINPUT 1 LINE_AGAIN$
1230 IF LINE_AGAIN$ != "First" THEN GOTO 9000
1240 
1250 REM Verify position reset correctly
1260 FINPUT 1 LINE_SECOND$
1270 IF LINE_SECOND$ != "Second" THEN GOTO 9000
1280 
1290 FCLOSE 1 "DELETE"
1300 PRINT "  PASSED: FREWIND correctly reset position"
1310 
1320 RETURN
1330 
9000 REM Error handler
9010 PRINT "  ERROR: Test failed"
9020 END
