10 REM Test ON GOSUB statement
20 X% = 2
30 ON X% GOSUB 100, 200, 300
40 PRINT "Program returned from ON GOSUB call"
50 GOTO 9999
100 PRINT "ERROR: Called subroutine at 100 (should be 200)"
110 RETURN
200 PRINT "Success: ON 2 GOSUB called subroutine at line 200"
210 RETURN
300 PRINT "ERROR: Called subroutine at 300 (should be 200)"
310 RETURN
9999 END