10 REM Growing testfile to develop the basic parser
20 PRINT "Testing the FOR Loops"
40 PRINT "Basic FOR loop..."
50 FOR X# = -2 TO 2 STEP 1
60 PRINT X#
70 NEXT
80 REM Test2
140 PRINT "Downwards; this semicolon should be correctly formatted..."
150 FOR Y# = 2 TO -2 STEP -1
160 PRINT Y#
170 NEXT
180 PRINT " "
190 GOSUB 910
890 PRINT "Last line of the program"
900 END
910 PRINT "Subprogram - this line should be print before the end message"
920 RETURN
