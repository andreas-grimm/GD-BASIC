10 REM Test for the forth main development iteration (version 0.0.4 - 0.0.6)
20 PRINT "Test Series 4"
40 REM DIM X#(4)
100 PRINT "Testing PRINT with square brackets: A$ = TEST, A$[1] = E, Concatenated = TESTE"
110 A$ = "TEST"
120 PRINT A$, A$[1]
130 B$(1,1) = "Array Test"
140 C$(1) = "Another "
150 A$(1,1,1) = "3 dimensions"
160 PRINT C$(1), A$
170 PRINT "This is the ", B$(1,1), " in ", A$(1,1,1);
180 A$(1,1,1) = " or 4?"
190 PRINT A$(1,1,1)
999 END
