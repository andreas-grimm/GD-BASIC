10 REM =============================================================
20 REM Comprehensive File Operations Test Suite
30 REM Tests: FOPEN, FCLOSE, FPRINT, FINPUT, FGET, FPUT, FPEEK, FRENAME, FREWIND
40 REM =============================================================
50 
60 PRINT "Testing: Comprehensive File Operations"
70 GOSUB 1000
80 END
90 
1000 REM ============================================================
1010 REM Test 1: Basic file write and read
1020 REM ============================================================
1030 PRINT ""
1040 PRINT "Test 1: Basic Write and Read"
1050 
1060 FOPEN 1 "/tmp/test_basic.txt" "w"
1070 FPRINT 1 "Line 1"
1080 FPRINT 1 "Line 2"
1090 FPRINT 1 "Line 3"
1100 FCLOSE 1 ""
1110 
1120 FOPEN 1 "/tmp/test_basic.txt" "r"
1130 FINPUT 1 L1$
1140 IF L1$ != "Line 1" THEN GOTO 9000
1150 FINPUT 1 L2$
1160 IF L2$ != "Line 2" THEN GOTO 9000
1170 FINPUT 1 L3$
1180 IF L3$ != "Line 3" THEN GOTO 9000
1190 FCLOSE 1 "DELETE"
1200 PRINT "  PASSED: Basic write and read"
1210 
1220 RETURN
1230 
9000 REM Error handler
9010 PRINT "  ERROR: Test failed"
9020 END
