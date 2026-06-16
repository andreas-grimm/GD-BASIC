10 REM Test FPEEK statement - peek at next character without consuming
20 REM File I/O: FOPEN, FPEEK, FGET, FCLOSE
30 
40 PRINT "Starting FPEEK tests..."
50 GOSUB 1000
60 END
70 
1000 REM Test FPEEK functionality
1010 REM Create test file
1020 FOPEN 1 "/tmp/fpeek_test.txt" "w"
1030 FPRINT 1 "Hello World"
1040 FCLOSE 1 ""
1050 
1060 REM Open for reading
1070 FOPEN 1 "/tmp/fpeek_test.txt" "r"
1080 
1090 REM Peek first character
1100 FPEEK 1, C$
1110 IF C$ != "H" THEN GOTO 9000
1120 
1130 REM Peek again - should be same
1140 FPEEK 1, C$
1150 IF C$ != "H" THEN GOTO 9000
1160 
1170 REM Now actually read it
1180 FGET 1, C$
1190 IF C$ != "H" THEN GOTO 9000
1200 
1210 REM Peek next character
1220 FPEEK 1, C$
1230 IF C$ != "e" THEN GOTO 9000
1240 
1250 FCLOSE 1 "DELETE"
1260 PRINT "PASSED"
1270 RETURN
1280 
9000 REM Error handler
9010 PRINT "ERROR: Test failed"
9020 END
