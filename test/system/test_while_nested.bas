10 REM Test: nested WHILE-END-WHILE loops. The outer WHILE must end at its own END-WHILE, not at the inner one.
20 PRINT "=== Testing nested WHILE loops ==="
30 outer% = 0
40 total% = 0
50 WHILE outer% < 3
60   outer% = outer% + 1
70   inner% = 0
80   WHILE inner% < 2
90     inner% = inner% + 1
100    total% = total% + 1
110  END-WHILE
120 END-WHILE
130 PRINT "Inner loop body ran ";
140 PRINT total%;
150 PRINT " times (expected 6)"
160 IF total% != 6 THEN 9000
170 PRINT "Outer loop ran ";
180 PRINT outer%;
190 PRINT " times (expected 3)"
200 IF outer% != 3 THEN 9000
210 PRINT ""
220 PRINT "=== All nested WHILE tests PASSED ==="
230 END
9000 PRINT "ERROR: Test failed!"
9010 END
