1 REM ========================================================================
2 REM Test: PRINT Statement
3 REM Description: Tests PRINT statement with various formats
4 REM Expected: PRINT should output correctly with different separators
5 REM ========================================================================
10 PRINT "=== Testing PRINT Statement ==="
20 PRINT ""
30 REM Test basic PRINT
40 PRINT "Testing Basic PRINT:"
50 PRINT "Hello, World!"
60 REM Test PRINT with semicolon (no newline)
70 PRINT "Testing PRINT with Semicolon:"
80 PRINT "Part1";
90 PRINT " Part2"
100 REM Test PRINT with comma (tab separator)
110 PRINT "Testing PRINT with Comma:"
120 PRINT "Column1", "Column2", "Column3"
130 REM Test PRINT with variables
140 PRINT "Testing PRINT with Variables:"
150 x% = 42
160 y$ = "Answer"
170 PRINT y$;
180 PRINT " = ";
190 PRINT x%
200 REM Test PRINT with expressions
210 PRINT "Testing PRINT with Expressions:"
220 PRINT "5 + 3 = ";
230 PRINT 5 + 3
240 REM Test empty PRINT (blank line)
250 PRINT "Testing Empty PRINT:"
260 PRINT "Before"
270 PRINT
280 PRINT "After"
290 REM Test multiple values
300 PRINT "Testing Multiple Values:"
310 a% = 1
320 b% = 2
330 c% = 3
340 PRINT a%;
350 PRINT ", ";
360 PRINT b%;
370 PRINT ", ";
380 PRINT c%
390 PRINT ""
400 PRINT "=== All PRINT Statement Tests PASSED ==="
410 END
