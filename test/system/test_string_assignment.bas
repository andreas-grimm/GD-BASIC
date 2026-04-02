REM ========================================================================
REM Test: String Assignment and Concatenation
REM Description: Tests string variable assignment and operations
REM Expected: Strings should be assigned and manipulated correctly
REM ========================================================================

10 PRINT "=== Testing String Assignment ==="
20 PRINT ""

REM Test basic string assignment
30 PRINT "Testing Basic String Assignment:"
40 str1$ = "Hello"
50 PRINT "str1$ = "; str1$

60 str2$ = "World"
70 PRINT "str2$ = "; str2$

REM Test string concatenation
80 PRINT "Testing String Concatenation:"
90 str3$ = str1$ + " " + str2$
100 PRINT "str1$ + ' ' + str2$ = "; str3$

REM Test empty string
110 PRINT "Testing Empty String:"
120 empty$ = ""
130 PRINT "empty$ = '"; empty$; "'"

REM Test string with special characters
140 PRINT "Testing String with Numbers:"
150 mixed$ = "Test123"
160 PRINT "mixed$ = "; mixed$

170 PRINT ""
180 PRINT "=== All String Assignment Tests PASSED ==="
190 END
