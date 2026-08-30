10 REM Test ON GOSUB statement - computed subroutine call based on integer expression
20 PRINT "Testing ON GOSUB statement"
30 GOSUB 100
40 GOSUB 200
50 GOSUB 300
60 GOSUB 400
70 PRINT "All tests passed"
80 GOTO 9999

100 REM Test basic ON GOSUB with valid indices
110 X% = 1
120 ON X% GOSUB 1100, 1200, 1300
130 PRINT "ERROR: Should have returned from subroutine at 1100"
140 GOTO 9000
1100 PRINT "  Test 1: ON 1 GOSUB -> Called subroutine at 1100"
1110 RETURN

200 REM Test ON GOSUB with index 2
210 X% = 2
220 ON X% GOSUB 2100, 2200, 2300
230 PRINT "ERROR: Should have returned from subroutine at 2200"
240 GOTO 9000
2100 PRINT "ERROR: Called 2100 instead of 2200"
2110 RETURN
2200 PRINT "  Test 2: ON 2 GOSUB -> Called subroutine at 2200"
2210 RETURN

300 REM Test ON GOSUB with index 3
310 X% = 3
320 ON X% GOSUB 3100, 3200, 3300
330 PRINT "ERROR: Should have returned from subroutine at 3300"
340 GOTO 9000
3100 PRINT "ERROR: Called 3100"
3110 RETURN
3200 PRINT "ERROR: Called 3200"
3210 RETURN
3300 PRINT "  Test 3: ON 3 GOSUB -> Called subroutine at 3300"
3310 RETURN

400 REM Test ON GOSUB with out-of-range index (no call)
410 X% = 5
420 ON X% GOSUB 4100, 4200, 4300
430 PRINT "  Test 4: ON 5 GOSUB -> Out-of-range, no call (correct)"
440 RETURN

9000 PRINT "TEST FAILED"
9999 END