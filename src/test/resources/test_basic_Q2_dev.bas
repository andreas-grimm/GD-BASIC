10 REM Growing testfile to develop the basic parser
20 PRINT "Testing the FOR Loops"
40 PRINT "Basic FOR loop..."
50 FOR X# = -2 TO 2 STEP .2
60 PRINT X#
70 NEXT
80 REM Test2
140 PRINT "Downwards..."
150 FOR Y# = 2 TO -2 STEP -.2
160 PRINT Y#
170 NEXT
180 GOSUB 910
890 PRINT "Last line of the program"
900 END
910 PRINT "Subprogram - this line should be print before the end message"
920 RETURN
