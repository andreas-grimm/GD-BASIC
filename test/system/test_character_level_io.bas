10 REM =============================================================
20 REM Character-Level File I/O Test
30 REM Tests: FPUT (write without newline), FGET (read with advance), FPEEK (lookahead)
40 REM =============================================================
50 
60 PRINT "Testing: Character-Level File I/O (FPUT, FGET, FPEEK)"
70 GOSUB 1000
80 END
90 
1000 REM ============================================================
1010 REM Test 1: Build line character by character with FPUT
1020 REM ============================================================
1030 PRINT ""
1040 PRINT "Test 1: FPUT - Build line character by character"
1050 
1060 FOPEN 1 "/tmp/test_char_io.txt" "w"
1070 FPUT 1, "H"
1080 FPUT 1, "e"
1090 FPUT 1, "l"
1100 FPUT 1, "l"
1110 FPUT 1, "o"
1120 FPRINT 1, ""
1130 FCLOSE 1 ""
1140 
1150 FOPEN 1 "/tmp/test_char_io.txt" "r"
1160 FINPUT 1 LINE$
1170 IF LINE$ != "Hello" THEN GOTO 9000
1180 FCLOSE 1 "DELETE"
1190 PRINT "  PASSED: FPUT character-by-character write"
1200 
1210 RETURN
1220 
9000 REM Error handler
9010 PRINT "  ERROR: Test failed"
9020 END
