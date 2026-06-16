10 REM =============================================================
20 REM Character Transform Test
30 REM Tests: FGET, FPUT for character-by-character transformation
40 REM =============================================================
50 
60 PRINT "Testing: Character-by-Character File Transformation"
70 GOSUB 1000
80 END
90 
1000 REM ============================================================
1010 REM Test 1: Replace spaces with underscores using FGET/FPUT
1020 REM ============================================================
1030 PRINT ""
1040 PRINT "Test 1: FGET/FPUT - Replace spaces with underscores"
1050 
1060 REM Create source file
1070 FOPEN 1 "/tmp/source_transform.txt" "w"
1080 FPUT 1, "H"
1090 FPUT 1, "e"
1100 FPUT 1, "l"
1110 FPUT 1, "l"
1120 FPUT 1, "o"
1130 FPUT 1, " "
1140 FPUT 1, "W"
1150 FPUT 1, "o"
1160 FPUT 1, "r"
1170 FPUT 1, "l"
1180 FPUT 1, "d"
1190 FCLOSE 1 ""
1200 
1210 REM Open source for reading, dest for writing
1220 FOPEN 1 "/tmp/source_transform.txt" "r"
1230 FOPEN 2 "/tmp/dest_transform.txt" "w"
1240 
1250 REM Process character by character
1260 WHILE NOT EOF(1)
1270    FGET 1, C$
1280    IF C$ != "EOF" THEN
1290       IF C$ = " " THEN
1300          FPUT 2, "_"
1310       ELSE
1320          FPUT 2, C$
1330       END IF
1340    END IF
1350 WEND
1360 
1370 FCLOSE 1 "DELETE"
1380 FCLOSE 2 ""
1390 
1400 REM Verify result
1410 FOPEN 2 "/tmp/dest_transform.txt" "r"
1420 FINPUT 2 RESULT$
1430 IF RESULT$ != "Hello_World" THEN GOTO 9000
1440 FCLOSE 2 "DELETE"
1450 
1460 PRINT "  PASSED: Character transformation successful"
1470 
1480 RETURN
1490 
9000 REM Error handler
9010 PRINT "  ERROR: Test failed"
9020 END
