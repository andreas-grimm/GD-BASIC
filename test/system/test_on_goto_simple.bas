10 REM Test ON GOTO statement
20 X% = 1
30 ON X% GOTO 100, 200, 300
40 PRINT "ERROR: Should have jumped to 100"
50 GOTO 9999
100 PRINT "Success: ON 1 GOTO jumped to line 100"
110 GOTO 9999
200 PRINT "ERROR: Should have jumped to 100, not 200"
210 GOTO 9999
300 PRINT "ERROR: Should have jumped to 100, not 300"
310 GOTO 9999
9999 END