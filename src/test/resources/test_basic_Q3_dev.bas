10 REM Test for the fourth main development iteration (version 0.0.7 - 0.1.0)
20 PRINT "Test Series 4"
40 PRINT "Testing DEF macro definition"
50 DEF FNA(X) = "X * 2"
60 DEF FNB(X, Y) = "X * Y"
70 PRINT "DEF processed"
100 PRINT "Testing Function"
110 X# = 1
120 X# = FNA(X#) * FNA(X#)
130 PRINT "Should be 2: ", X#
900 PRINT "Ending the Program"
910 END
