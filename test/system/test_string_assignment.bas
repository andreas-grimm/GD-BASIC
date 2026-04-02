1 REM ========================================================================
2 REM Test: String Assignment and Concatenation
3 REM Description: Tests string variable assignment and operations
4 REM Expected: Strings should be assigned and manipulated correctly
5 REM ========================================================================

10 PRINT "=== Testing String Assignment ==="
20 PRINT ""

30 REM Test basic string assignment
40 PRINT "Testing Basic String Assignment:"
50 str1$ = "Hello"
60 PRINT "str1$ = "; str1$

70 str2$ = "World"
80 PRINT "str2$ = "; str2$

90 REM Test string concatenation
100 PRINT "Testing String Concatenation:"
110 str3$ = str1$ + " " + str2$
120 PRINT "str1$ + ' ' + str2$ = "; str3$

130 REM Test empty string
140 PRINT "Testing Empty String:"
150 empty$ = ""
160 PRINT "empty$ = '"; empty$; "'"

170 REM Test string with special characters
180 PRINT "Testing String with Numbers:"
190 mixed$ = "Test123"
200 PRINT "mixed$ = "; mixed$

210 PRINT ""
220 PRINT "=== All String Assignment Tests PASSED ==="
230 END
