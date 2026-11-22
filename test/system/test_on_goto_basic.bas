10 REM Test: ON GOTO - computed jump based on an integer expression (rewritten in Phase 7: line numbers ascend)
20 PRINT "Testing ON GOTO statement"
30 hit% = 0
40 X% = 2
50 ON X% GOTO 100, 200, 300
60 PRINT "ERROR: ON GOTO did not jump"
70 GOTO 9000
100 PRINT "ERROR: index 2 jumped to 100"
110 GOTO 9000
200 hit% = 2
210 X% = 5
220 ON X% GOTO 400, 500, 600
230 REM index 5 is out of range: no jump, the program continues here
240 IF hit% != 2 THEN GOTO 9000
250 PRINT "  Test 1: ON 2 GOTO jumped to 200; ON 5 GOTO did not jump (correct)"
260 X% = 1
270 Y% = 1
280 ON X% + Y% GOTO 700, 800
290 PRINT "ERROR: expression index 2 did not jump"
295 GOTO 9000
300 PRINT "ERROR: index 2 jumped to 300"
310 GOTO 9000
400 PRINT "ERROR: index 5 jumped to 400"
410 GOTO 9000
500 PRINT "ERROR: index 5 jumped to 500"
510 GOTO 9000
600 PRINT "ERROR: index 5 jumped to 600"
610 GOTO 9000
700 PRINT "ERROR: index 2 jumped to 700"
710 GOTO 9000
800 PRINT "  Test 2: ON X% + Y% GOTO -> jumped to 800 (correct)"
810 PRINT "  ON GOTO tests PASSED"
820 GOTO 9999
9000 PRINT "ERROR: Test failed"
9010 END
9999 END
