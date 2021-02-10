10 REM Test for the thrid main development iteration (version 0.0.4 - 0.0.6)
20 PRINT "Test Series 3"
40 PRINT "Testing FOR loop counting upwards"
50 FOR X# = -2 TO 2 STEP 1
60 PRINT X#
70 NEXT
80 PRINT "Downwards; this semicolon should be correctly formatted..."
90 FOR Y# = 2 TO -2 STEP -1
100 PRINT Y#
110 NEXT
200 PRINT "Testing the GoSub Function"
210 GOSUB 920
300 PRINT "Testing the While Loop"
310 X# = 0
320 WHILE X# < 3
330 PRINT X#
340 X# = X# + 1
350 END-WHILE
400 PRINT "Testing the Do Loop"
410 X# = 0
420 DO
430 X# = X# + 1
440 PRINT X#
450 UNTIL X# > 2
900 PRINT "Ending the Program"
910 END
920 PRINT "Subprogram - this line should be print before the end message"
930 RETURN
