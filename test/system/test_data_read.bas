REM ========================================================================
REM Test: DATA and READ Statements
REM Description: Tests DATA and READ statements for data storage/retrieval
REM Expected: READ should correctly retrieve values from DATA statements
REM ========================================================================

10 PRINT "=== Testing DATA and READ Statements ==="
20 PRINT ""

REM Test basic READ and DATA
30 PRINT "Testing Basic READ and DATA:"
40 READ a%
50 READ b%
60 READ c%
70 PRINT "First three values: "; a%; ", "; b%; ", "; c%
80 IF a% != 10 THEN GOTO 9000
90 IF b% != 20 THEN GOTO 9000
100 IF c% != 30 THEN GOTO 9000

REM Test READ with strings
110 PRINT "Testing READ with Strings:"
120 READ name1$
130 READ name2$
140 PRINT "Names: "; name1$; ", "; name2$

REM Test READ with mixed types
150 PRINT "Testing READ with Mixed Types:"
160 READ num%
170 READ str$
180 READ val#
190 PRINT "Mixed: "; num%; ", "; str$; ", "; val#

REM Test READ in loop
200 PRINT "Testing READ in Loop:"
210 FOR i% = 1 TO 3
220   READ x%
230   PRINT "Value "; i%; ": "; x%
240 NEXT i%

250 PRINT ""
260 PRINT "=== All DATA and READ Tests PASSED ==="
270 END

REM DATA statements
1000 DATA 10, 20, 30
1010 DATA "Alice", "Bob"
1020 DATA 42, "Test", 3.14
1030 DATA 100, 200, 300

9000 PRINT "ERROR: Test failed!"
9010 END
