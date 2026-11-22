10 REM Test: ON GOSUB - computed subroutine call based on an integer expression (rewritten in Phase 7)
20 PRINT "Testing ON GOSUB statement"
30 hit% = 0
40 X% = 2
50 ON X% GOSUB 1000, 2000, 3000
60 IF hit% != 2 THEN GOTO 9000
70 PRINT "  Test 1: ON 2 GOSUB called the subroutine at 2000 and returned (correct)"
80 hit% = 0
90 X% = 5
100 ON X% GOSUB 1000, 2000, 3000
110 IF hit% != 0 THEN GOTO 9000
120 PRINT "  Test 2: ON 5 GOSUB did not call a subroutine (correct)"
130 PRINT "  ON GOSUB tests PASSED"
140 GOTO 9999
1000 hit% = 1
1010 RETURN
2000 hit% = 2
2010 RETURN
3000 hit% = 3
3010 RETURN
9000 PRINT "ERROR: Test failed"
9010 END
9999 END
