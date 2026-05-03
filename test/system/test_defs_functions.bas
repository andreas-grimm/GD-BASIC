10 REM Test for the fourth main development iteration (version 0.0.7 - 0.1.0)
20 PRINT "Test Series 4"
40 PRINT "Testing DEF macro definition"
50 DEF FNA(X) = "X * 2"
60 DEF FNB(X, Y) = "X * Y"
70 DEF FNC(X, Y, Z) = "X * Y + Z"
80 PRINT "DEF processed"
100 PRINT "Testing Function"
110 X# = 1
120 X# = FNA(X#) * FNA(X#)
130 PRINT "Should be 4: ", X#
200 PRINT "Testing Function with two Parameters"
210 X# = 2
220 Y# = 2
230 X# = FNB(X#, Y#)
240 PRINT "Should be 4: ", X#
300 PRINT "Testing Function with three Parameters"
310 X# = 2
320 Y# = 2
330 Z# = 2
340 X# = FNC(X#, Y#, Z#)
350 PRINT "Should be 6: ", X#
900 PRINT "Ending the Program"
910 END
