10 REM =============================================================
20 REM File Existence Check Test
30 REM Tests: FEXISTS function
40 REM =============================================================
50 
60 PRINT "Testing: File Existence (FEXISTS)"
70 GOSUB 1000
80 END
90 
1000 REM ============================================================
1010 REM Test 1: Check file existence before and after operations
1020 REM ============================================================
1030 PRINT ""
1040 PRINT "Test 1: FEXISTS - File existence verification"
1050 
1060 REM Non-existent file should not exist
1070 IF FEXISTS("/tmp/nonexistent_file_xyz.txt") THEN GOTO 9000
1080 
1090 REM Create a file
1100 FOPEN 1 "/tmp/test_exists.txt" "w"
1110 FPRINT 1 "Test file"
1120 FCLOSE 1 ""
1130 
1140 REM Now it should exist
1150 IF NOT FEXISTS("/tmp/test_exists.txt") THEN GOTO 9000
1160 
1170 REM Delete it
1180 FOPEN 1 "/tmp/test_exists.txt" "r"
1190 FCLOSE 1 "DELETE"
1200 
1210 REM Should not exist anymore
1220 IF FEXISTS("/tmp/test_exists.txt") THEN GOTO 9000
1230 
1240 PRINT "  PASSED: FEXISTS verification successful"
1250 
1260 RETURN
1270 
9000 REM Error handler
9010 PRINT "  ERROR: Test failed"
9020 END
