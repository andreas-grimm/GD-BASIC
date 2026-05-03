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
80 PRINT "First three values: ";
90 PRINT a%;
100 PRINT ", ";
110 PRINT b%;
120 PRINT ", ";
130 PRINT c%
140 IF a% != 10 THEN 9000
150 IF b% != 20 THEN 9000
160 IF c% != 30 THEN 9000
170 REM Test READ with strings
180 PRINT "Testing READ with Strings:"
190 READ name1$
200 READ name2$
210 PRINT "Names: ";
220 PRINT name1$;
230 PRINT ", ";
240 PRINT name2$
250 REM Test READ with mixed types
260 PRINT "Testing READ with Mixed Types:"
270 READ num%
280 READ str$
290 READ val#
300 PRINT "Mixed: ";
310 PRINT num%;
320 PRINT ", ";
330 PRINT str$;
340 PRINT ", ";
350 PRINT val#
360 REM Test READ in loop
370 PRINT "Testing READ in Loop:"
380 FOR i% = 1 TO 3
390   READ x%
400   PRINT "Value ";
410   PRINT i%;
420   PRINT ": ";
430   PRINT x%
440 NEXT
450 PRINT ""
460 PRINT "=== All DATA and READ Tests PASSED ==="
470 END
1000 REM DATA statements
1010 DATA 10, 20, 30
1020 DATA "Alice", "Bob"
1030 DATA 42, "Test", 3.14
1040 DATA 100, 200, 300
9000 PRINT "ERROR: Test failed!"
9010 END
