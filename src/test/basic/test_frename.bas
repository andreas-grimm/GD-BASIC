10 REM Test FRENAME statement - rename a file
20 REM File I/O: FOPEN, FPRINT, FCLOSE, FRENAME, FEXISTS
30 
40 PRINT "Starting FRENAME tests..."
50 GOSUB 1000
60 END
70 
1000 REM Test FRENAME functionality
1010 REM Create initial file
1020 FOPEN 1 "/tmp/original_file.txt" "w"
1030 FPRINT 1 "Test content"
1040 FCLOSE 1 ""
1050 
1060 REM Verify original file exists
1070 IF NOT FEXISTS("/tmp/original_file.txt") THEN GOTO 9000
1080 
1090 REM Rename the file
1100 FRENAME 1, "/tmp/renamed_file.txt"
1110 
1120 REM Verify renamed file exists
1130 IF NOT FEXISTS("/tmp/renamed_file.txt") THEN GOTO 9000
1140 
1150 REM Open renamed file to verify content
1160 FOPEN 2 "/tmp/renamed_file.txt" "r"
1170 FINPUT 2 LINE$
1180 IF LINE$ != "Test content" THEN GOTO 9000
1190 
1200 FCLOSE 2 "DELETE"
1210 PRINT "PASSED"
1220 RETURN
1230 
9000 REM Error handler
9010 PRINT "ERROR: Test failed"
9020 END
