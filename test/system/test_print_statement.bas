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
170 PRINT y$; " = "; x%

180 REM Test PRINT with expressions
190 PRINT "Testing PRINT with Expressions:"
200 PRINT "5 + 3 = "; 5 + 3

210 REM Test empty PRINT (blank line)
220 PRINT "Testing Empty PRINT:"
230 PRINT "Before"
240 PRINT
250 PRINT "After"

260 REM Test multiple values
270 PRINT "Testing Multiple Values:"
280 a% = 1
290 b% = 2
300 c% = 3
310 PRINT a%; ", "; b%; ", "; c%

320 PRINT ""
330 PRINT "=== All PRINT Statement Tests PASSED ==="
340 END
