10 REM Test ON statement with out-of-range index
20 X% = 5
30 ON X% GOTO 100, 200, 300
40 PRINT "Success: Out-of-range index (5) did not jump, continued normally"
50 GOTO 9999
100 PRINT "ERROR: Should not jump when index is out of range"
110 GOTO 9999
200 PRINT "ERROR: Should not jump when index is out of range"
210 GOTO 9999
300 PRINT "ERROR: Should not jump when index is out of range"
310 GOTO 9999
9999 END