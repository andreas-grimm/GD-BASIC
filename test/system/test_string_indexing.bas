REM ========================================================================
REM Test: String Character Indexing
REM Description: Tests accessing individual characters in strings using [index]
REM Expected: Individual characters should be accessible by index
REM ========================================================================

10 PRINT "=== Testing String Character Indexing ==="
20 PRINT ""

REM Test basic string indexing
30 PRINT "Testing Basic String Indexing:"
40 str$ = "BASIC"
50 PRINT "str$ = "; str$
60 char1$ = str$[1]
70 PRINT "str$[1] = "; char1$
80 char3$ = str$[3]
90 PRINT "str$[3] = "; char3$

REM Test string indexing in loop
100 PRINT "Testing String Indexing in Loop:"
110 word$ = "TEST"
120 FOR i% = 1 TO LEN(word$)
130   PRINT word$[i%];
140 NEXT i%
150 PRINT ""

160 PRINT ""
170 PRINT "=== All String Indexing Tests PASSED ==="
180 END
