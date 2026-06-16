10 REM =============================================================
20 REM File EOF Handling Test
30 REM Tests: EOF function with file operations
40 REM =============================================================
50
60 PRINT "Testing: File EOF (End-of-File) Handling"
70 GOSUB 1000
80 END
90
1000 REM ============================================================
1010 REM Test 1: EOF detection after reading
1020 REM ============================================================
1030 PRINT ""
1040 PRINT "Test 1: EOF detection"
1050
1060 REM Create test file with one line
1070 FOPEN 1 "/tmp/test_eof_single.txt" "w"
1080 FPRINT 1 "Test Line"
1090 FCLOSE 1 ""
1100
1110 REM Open for reading
1120 FOPEN 1 "/tmp/test_eof_single.txt" "r"
1130
1140 REM Before reading, EOF should be false
1150 IF EOF(1) THEN GOTO 9000
1160
1170 REM Read the line
1180 FINPUT 1 L$
1190 IF L$ != "Test Line" THEN GOTO 9000
1200
1210 REM After reading the only line, EOF should still be false
1220 REM (EOF only becomes true when we try to read past the end)
1230 IF EOF(1) THEN GOTO 9000
1240
1250 REM Try to read past end
1260 FINPUT 1 L2$
1270
1280 REM Now EOF should be true
1290 IF NOT EOF(1) THEN GOTO 9000
1300
1310 FCLOSE 1 "DELETE"
1320 PRINT "  PASSED: EOF detection correct"
1330
1340 RETURN
1350
9000 REM Error handler
9010 PRINT "  ERROR: Test failed"
9020 END
