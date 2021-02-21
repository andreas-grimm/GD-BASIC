10 REM Test for the forth main development iteration (version 0.0.4 - 0.0.6)
20 PRINT "Test Series 5"
50 PRINT "Make sure that parenthesis are working"
60 A# = 4 * ( 2 + 1 )
70 PRINT "This should be 12, not 9: ", A#
80 A# = 4 * 2 + 1
90 PRINT "This should be 9, not 12: ", A#
100 PRINT "Testing a simple function: MEM, should return the available memory"
110 A% = MEM()
120 PRINT "Result: Should be a number > 0: ", A%
130 PRINT "Testing a simple function: RND, should return a random number"
140 A# = RND()
150 PRINT "Result: Should be a number > 0: ", A#
160 PRINT "Testing a function with a parameter: ABS, should return a positive number"
170 A# = ABS( -1.0 )
180 PRINT "Result of test with real: Should be a number numeric 1.0: ", A#
190 A% = ABS( -1 )
200 PRINT "Result of test with integer: Should be a number numeric 1: ", A%
210 A% = ABS(-1)
220 PRINT "Result of test with integer: Should be a number numeric 1: ", A%
999 END
