REM ========================================================================
REM Test: PRINT Statement
REM Description: Tests PRINT statement with various formats
REM Expected: PRINT should output correctly with different separators
REM ========================================================================

10 PRINT "=== Testing PRINT Statement ==="
20 PRINT ""

REM Test basic PRINT
30 PRINT "Testing Basic PRINT:"
40 PRINT "Hello, World!"

REM Test PRINT with semicolon (no newline)
50 PRINT "Testing PRINT with Semicolon:"
60 PRINT "Part1";
70 PRINT " Part2"

REM Test PRINT with comma (tab separator)
80 PRINT "Testing PRINT with Comma:"
90 PRINT "Column1", "Column2", "Column3"

REM Test PRINT with variables
100 PRINT "Testing PRINT with Variables:"
110 x% = 42
120 y$ = "Answer"
130 PRINT y$; " = "; x%

REM Test PRINT with expressions
140 PRINT "Testing PRINT with Expressions:"
150 PRINT "5 + 3 = "; 5 + 3

REM Test empty PRINT (blank line)
160 PRINT "Testing Empty PRINT:"
170 PRINT "Before"
180 PRINT
190 PRINT "After"

REM Test multiple values
200 PRINT "Testing Multiple Values:"
210 a% = 1
220 b% = 2
230 c% = 3
240 PRINT a%; ", "; b%; ", "; c%

250 PRINT ""
260 PRINT "=== All PRINT Statement Tests PASSED ==="
270 END
