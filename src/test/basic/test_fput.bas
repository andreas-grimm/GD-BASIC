10 REM Test FPUT statement - write character without newline
20 REM File I/O: FOPEN, FPUT, FINPUT, FCLOSE
30 
40 PRINT "Starting FPUT tests..."
50 GOSUB 1000
60 END
70 
1000 REM Test FPUT functionality
1010 REM Create test file with FPUT
1020 FOPEN 1 "/tmp/fput_test.txt" "w"
1030 
1040 REM Write single characters without newline
1050 FPUT 1, "H"
1060 FPUT 1, "e"
1070 FPUT 1, "l"
1080 FPUT 1, "l"
1090 FPUT 1, "o"
1100 
1110 REM Add newline with FPRINT
1120 FPRINT 1 ""
1130 
1140 FCLOSE 1 ""
1150 
1160 REM Verify content
1170 FOPEN 1 "/tmp/fput_test.txt" "r"
1180 FINPUT 1 LINE$
1190 IF LINE$ != "Hello" THEN GOTO 9000
1200 
1210 FCLOSE 1 "DELETE"
1220 PRINT "PASSED"
1230 RETURN
1240 
9000 REM Error handler
9010 PRINT "ERROR: Test failed"
9020 END
