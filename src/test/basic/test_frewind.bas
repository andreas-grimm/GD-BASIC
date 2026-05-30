10 REM Test FREWIND statement - rewind file pointer to beginning
20 REM File I/O: FOPEN, FPRINT, FREWIND, FINPUT, FCLOSE
30 
40 PRINT "Starting FREWIND tests..."
50 GOSUB 1000
60 END
70 
1000 REM Test FREWIND functionality
1010 REM Create test file
1020 FOPEN 1 "/tmp/frewind_test.txt" "w"
1030 FPRINT 1 "Line 1"
1040 FPRINT 1 "Line 2"
1050 FPRINT 1 "Line 3"
1060 FCLOSE 1 ""
1070 
1080 REM Open for reading
1090 FOPEN 1 "/tmp/frewind_test.txt" "r"
1100 
1110 REM Read first line
1120 FINPUT 1 LINE1$
1130 IF LINE1$ != "Line 1" THEN GOTO 9000
1140 
1150 REM Read second line
1160 FINPUT 1 LINE2$
1170 IF LINE2$ != "Line 2" THEN GOTO 9000
1180 
1190 REM Rewind to beginning
1200 FREWIND 1
1210 
1220 REM Read again - should get first line again
1230 FINPUT 1 LINE$
1240 IF LINE$ != "Line 1" THEN GOTO 9000
1250 
1260 FCLOSE 1 "DELETE"
1270 PRINT "PASSED"
1280 RETURN
1290 
9000 REM Error handler
9010 PRINT "ERROR: Test failed"
9020 END
