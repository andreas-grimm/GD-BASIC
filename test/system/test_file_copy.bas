10 REM =============================================================
20 REM File Copy Test
30 REM Tests: Copy file using line-by-line read/write
40 REM =============================================================
50 
60 PRINT "Testing: File Copy Operations"
70 GOSUB 1000
80 END
90 
1000 REM ============================================================
1010 REM Test 1: Copy file using FINPUT and FPRINT
1020 REM ============================================================
1030 PRINT ""
1040 PRINT "Test 1: Copy file line-by-line"
1050 
1060 REM Create source file
1070 FOPEN 1 "/tmp/copy_source.txt" "w"
1080 FPRINT 1 "Line 1 of copy test"
1090 FPRINT 1 "Line 2 of copy test"
1100 FPRINT 1 "Line 3 of copy test"
1110 FCLOSE 1 ""
1120 
1130 REM Copy file
1140 FOPEN 1 "/tmp/copy_source.txt" "r"
1150 FOPEN 2 "/tmp/copy_dest.txt" "w"
1160 
1170 WHILE NOT EOF(1)
1180    FINPUT 1, LINE$
1190    FPRINT 2, LINE$
1200 WEND
1210 
1220 FCLOSE 1 "DELETE"
1230 FCLOSE 2 ""
1240 
1250 REM Verify copy
1260 FOPEN 2 "/tmp/copy_dest.txt" "r"
1270 FINPUT 2 VERIFY1$
1280 IF VERIFY1$ != "Line 1 of copy test" THEN GOTO 9000
1290 FINPUT 2 VERIFY2$
1300 IF VERIFY2$ != "Line 2 of copy test" THEN GOTO 9000
1310 FINPUT 2 VERIFY3$
1300 IF VERIFY3$ != "Line 3 of copy test" THEN GOTO 9000
1320 FCLOSE 2 "DELETE"
1330 
1340 PRINT "  PASSED: File copy successful"
1350 
1360 RETURN
1370 
9000 REM Error handler
9010 PRINT "  ERROR: Test failed"
9020 END
