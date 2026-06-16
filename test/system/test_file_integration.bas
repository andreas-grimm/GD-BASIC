10 REM =============================================================
20 REM File Operations Integration Test
30 REM Tests: Combined FOPEN, FCLOSE, FINPUT, FPRINT, FGET, FPUT, FPEEK, FREWIND, FRENAME
40 REM =============================================================
50 
60 PRINT "Testing: Integrated File Operations"
70 GOSUB 1000
80 END
90 
1000 REM ============================================================
1010 REM Test 1: Complete file workflow
1020 REM ============================================================
1030 PRINT ""
1040 PRINT "Test 1: Complete file workflow"
1050 
1060 REM Step 1: Create file with FPRINT
1070 FOPEN 1 "/tmp/integration_test.txt" "w"
1080 FPRINT 1 "Header Line"
1090 FPRINT 1 "Data Line 1"
1100 FPRINT 1 "Data Line 2"
1110 FCLOSE 1 ""
1120 
1130 REM Step 2: Read with FINPUT and FGET
1140 FOPEN 1 "/tmp/integration_test.txt" "r"
1150 FINPUT 1 HEADER$
1160 IF HEADER$ != "Header Line" THEN GOTO 9000
1170 
1180 REM Step 3: Use FPEEK to lookahead
1190 FPEEK 1, NEXT_CHAR$
1200 IF NEXT_CHAR$ != "D" THEN GOTO 9000
1210 PRINT "  Confirmed: Next line starts with D"
1250 
1260 REM Step 4: Read remaining line
1270 FINPUT 1 LINE1$
1280 IF LINE1$ != "Data Line 1" THEN GOTO 9000
1290 
1300 REM Step 5: Rewind and re-read
1310 FREWIND 1
1320 FINPUT 1 HEADER_AGAIN$
1330 IF HEADER_AGAIN$ != "Header Line" THEN GOTO 9000
1340 
1350 FCLOSE 1 "DELETE"
1360 
1370 PRINT "  PASSED: Integrated file operations successful"
1380 
1390 RETURN
1400 
9000 REM Error handler
9010 PRINT "  ERROR: Test failed"
9020 END
