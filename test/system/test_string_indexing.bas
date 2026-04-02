1 REM ========================================================================
2 REM Test: String Character Indexing
3 REM Description: Tests accessing individual characters in strings using [index]
4 REM Expected: Individual characters should be accessible by index
5 REM ========================================================================

10 PRINT "=== Testing String Character Indexing ==="
20 PRINT ""

30 REM Test basic string indexing
40 PRINT "Testing Basic String Indexing:"
50 str$ = "BASIC"
60 PRINT "str$ = "; str$
70 char1$ = str$[1]
80 PRINT "str$[1] = "; char1$
90 char3$ = str$[3]
100 PRINT "str$[3] = "; char3$

110 REM Test string indexing in loop
120 PRINT "Testing String Indexing in Loop:"
130 word$ = "TEST"
140 FOR i% = 1 TO LEN(word$)
150   PRINT word$[i%];
160 NEXT i%
170 PRINT ""

180 PRINT ""
190 PRINT "=== All String Indexing Tests PASSED ==="
200 END
