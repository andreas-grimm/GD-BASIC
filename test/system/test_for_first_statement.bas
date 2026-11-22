10 FOR i% = 1 TO 3
20 last% = i%
30 NEXT
40 REM The loop above starts at the first statement of the program (statement number 0)
50 PRINT "Last loop value ";
60 PRINT last%;
70 PRINT " (expected 3)"
80 IF last% != 3 THEN 9000
90 PRINT ""
100 PRINT "=== First-statement FOR-NEXT test PASSED ==="
110 END
9000 PRINT "ERROR: Test failed!"
9010 END
