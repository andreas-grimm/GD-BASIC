10 REM =============================================================
20 REM File Rename Test
30 REM Tests: FRENAME - rename/move file by file ID
40 REM =============================================================
50 
60 PRINT "Testing: FRENAME - File Renaming/Moving"
70 GOSUB 1000
80 END
90 
1000 REM ============================================================
1010 REM Test 1: Rename file and verify content preserved
1020 REM ============================================================
1030 PRINT ""
1040 PRINT "Test 1: FRENAME - Rename file with content"
1050 
1060 REM Create initial file
1070 FOPEN 1 "/tmp/original_test.txt" "w"
1080 FPRINT 1 "Important Data"
1090 FPRINT 1 "Line 2"
1100 FCLOSE 1 ""
1110 
1120 REM Verify original file exists
1130 IF NOT FEXISTS("/tmp/original_test.txt") THEN GOTO 9000
1140 
1150 REM Rename the file
1160 FRENAME 1, "/tmp/renamed_test.txt"
1170 
1180 REM Verify renamed file exists
1190 IF NOT FEXISTS("/tmp/renamed_test.txt") THEN GOTO 9000
1200 
1210 REM Verify content is intact
1220 FOPEN 2 "/tmp/renamed_test.txt" "r"
1230 FINPUT 2 CONTENT$
1240 IF CONTENT$ != "Important Data" THEN GOTO 9000
1250 FCLOSE 2 "DELETE"
1260 
1270 PRINT "  PASSED: FRENAME preserved file content"
1280 
1290 RETURN
1300 
9000 REM Error handler
9010 PRINT "  ERROR: Test failed"
9020 END
