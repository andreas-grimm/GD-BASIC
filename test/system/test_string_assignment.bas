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
60 PRINT "str1$ = ";
70 PRINT str1$
80 str2$ = "World"
90 PRINT "str2$ = ";
100 PRINT str2$
110 REM Test string concatenation
120 PRINT "Testing String Concatenation:"
130 str3$ = str1$ + " " + str2$
140 PRINT "str1$ + ' ' + str2$ = ";
150 PRINT str3$
160 REM Test empty string
170 PRINT "Testing Empty String:"
180 empty$ = ""
190 PRINT "empty$ = '";
200 PRINT empty$;
210 PRINT "'"
220 REM Test string with special characters
230 PRINT "Testing String with Numbers:"
240 mixed$ = "Test123"
250 PRINT "mixed$ = ";
260 PRINT mixed$
270 PRINT ""
280 PRINT "=== All String Assignment Tests PASSED ==="
290 END
