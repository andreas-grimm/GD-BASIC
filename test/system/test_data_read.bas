1 REM ========================================================================
2 REM Test: DATA and READ Statements
3 REM Description: Tests DATA and READ statements for data storage/retrieval
4 REM Expected: READ should correctly retrieve values from DATA statements
5 REM ========================================================================

10 PRINT "=== Testing DATA and READ Statements ==="
20 PRINT ""

30 REM Test basic READ and DATA
40 PRINT "Testing Basic READ and DATA:"
50 READ a%
60 READ b%
70 READ c%
80 PRINT "First three values: "; a%; ", "; b%; ", "; c%
90 IF a% != 10 THEN GOTO 9000
100 IF b% != 20 THEN GOTO 9000
110 IF c% != 30 THEN GOTO 9000

120 REM Test READ with strings
130 PRINT "Testing READ with Strings:"
140 READ name1$
150 READ name2$
160 PRINT "Names: "; name1$; ", "; name2$

170 REM Test READ with mixed types
180 PRINT "Testing READ with Mixed Types:"
190 READ num%
200 READ str$
210 READ val#
220 PRINT "Mixed: "; num%; ", "; str$; ", "; val#

230 REM Test READ in loop
240 PRINT "Testing READ in Loop:"
250 FOR i% = 1 TO 3
260   READ x%
270   PRINT "Value "; i%; ": "; x%
280 NEXT i%

290 PRINT ""
300 PRINT "=== All DATA and READ Tests PASSED ==="
310 END

1000 REM DATA statements
1010 DATA 10, 20, 30
1020 DATA "Alice", "Bob"
1030 DATA 42, "Test", 3.14
1040 DATA 100, 200, 300

9000 PRINT "ERROR: Test failed!"
9010 END
