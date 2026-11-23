10 REM Test: a FOR loop that is finished is started again when an outer loop reaches it
20 REM Description: The inner FOR-NEXT loop runs once for each pass of the outer loop. Before the fix,
30 REM the second and later passes skipped the inner loop, because the finished loop was not reset.
40 PRINT "=== Testing nested FOR-NEXT restart ==="
50 total% = 0
60 FOR outer% = 1 TO 4
70   FOR inner% = 1 TO 3
80     total% = total% + 1
90   NEXT
100 NEXT
110 PRINT "Inner loop body ran ";
120 PRINT total%;
130 PRINT " times (expected 12)"
140 IF total% != 12 THEN 9000
150 REM Sieve pattern: a variable STEP in an inner loop, reached again after the first pass
160 limit% = 30
170 p% = 2
180 count% = 0
190 FOR i% = p% TO limit% STEP p%
200   count% = count% + 1
210 NEXT
220 p% = 3
230 FOR i% = p% TO limit% STEP p%
240   count% = count% + 1
250 NEXT
260 PRINT "Multiples counted: ";
270 PRINT count%;
280 PRINT " (expected 25)"
290 IF count% != 25 THEN 9000
300 PRINT ""
310 PRINT "=== All nested FOR-NEXT restart tests PASSED ==="
320 END
9000 PRINT "ERROR: Test failed!"
9010 END
