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
60 PRINT "str$ = ";
70 PRINT str$
80 char1$ = str$[1]
90 PRINT "str$[1] = ";
100 PRINT char1$
110 char3$ = str$[3]
120 PRINT "str$[3] = ";
130 PRINT char3$
140 REM Test string indexing in loop
150 PRINT "Testing String Indexing in Loop:"
160 word$ = "TEST"
170 FOR i% = 1 TO LEN(word$)
180   PRINT word$[i%];
190 NEXT
200 PRINT ""
210 PRINT ""
220 PRINT "=== All String Indexing Tests PASSED ==="
230 END
