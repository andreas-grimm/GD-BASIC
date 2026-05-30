10 REM =============================================================
20 REM File EOF Handling Test
30 REM Tests: EOF function with FGET, FPEEK, and various conditions
40 REM =============================================================
50 
60 PRINT "Testing: File EOF (End-of-File) Handling"
70 GOSUB 1000
80 END
90 
1000 REM ============================================================
1010 REM Test 1: EOF detection with multiple reads
1020 REM ============================================================
1030 PRINT ""
1040 PRINT "Test 1: EOF detection"
1050 
1060 REM Create small test file
1070 FOPEN 1 "/tmp/test_eof.txt" "w"
1080 FPUT 1, "A"
1090 FPUT 1, "B"
1100 FCLOSE 1 ""
1110 
1120 REM Open for reading
1130 FOPEN 1 "/tmp/test_eof.txt" "r"
1140 
1150 REM Read first character
1160 IF EOF(1) THEN GOTO 9000
1170 FGET 1, C1$
1180 
1190 REM Read second character
1200 IF EOF(1) THEN GOTO 9000
1210 FGET 1, C2$
1220 
1230 REM Now at EOF
1240 IF NOT EOF(1) THEN GOTO 9000
1250 
1260 REM FPEEK at EOF
1270 FPEEK 1, EOF_CHAR$
1280 IF EOF_CHAR$ != "EOF" THEN GOTO 9000
1290 
1300 FCLOSE 1 "DELETE"
1310 PRINT "  PASSED: EOF detection correct"
1320 
1330 RETURN
1340 
9000 REM Error handler
9010 PRINT "  ERROR: Test failed"
9020 END
